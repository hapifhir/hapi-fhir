package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides server ports that are free, in order for tests to use them
 *
 * <p><b>
 * This class is ONLY designed for unit-testing usage, as it holds on to server ports
 * for a long time (potentially lots of them!) and will leave your system low on
 * ports if you put it into production.
 * </b></p>
 * <p>
 * How it works:
 * <p>
 * We have lots of tests that need a free port because they want to open up
 * a server, and need the port to be unique and unused so that the tests can
 * run multithreaded. This turns out to just be an awful problem to solve for
 * lots of reasons:
 * <p>
 * 1. You can request a free port from the OS by calling <code>new ServerSocket(0);</code>
 * and this seems to work 99% of the time, but occasionally on a heavily loaded
 * server if two processes ask at the exact same time they will receive the
 * same port assignment, and one will fail.
 * 2. Tests run in separate processes, so we can't just rely on keeping a collection
 * of assigned ports or anything like that.
 * <p>
 * So we solve this like this:
 * <p>
 * At random, this class will pick a "control port" and bind it. A control port
 * is just a randomly chosen port that is a multiple of 100. If we can bind
 * successfully to that port, we now own the range of "n+1 to n+99". If we can't
 * bind that port, it means some other process has probably taken it so
 * we'll just try again until we find an available control port.
 * <p>
 * Assuming we successfully bind a control port, we'll give out any available
 * ports in the range "n+1 to n+99" until we've exhausted the whole set, and
 * then we'll pick another control port (if we actually get asked for over
 * 100 ports.. this should be a rare event).
 * <p>
 * This mechanism has the benefit of (fingers crossed) being bulletproof
 * in terms of its ability to give out ports that are actually free, thereby
 * preventing random test failures.
 * <p>
 * This mechanism has the drawback of never giving up a control port once
 * it has assigned one. To be clear, this class is deliberately leaking
 * resources. Again, no production use!
 */
public class PortUtil {
	private static final int SPACE_SIZE = 100;
	private static final Logger ourLog = LoggerFactory.getLogger(PortUtil.class);
	private static final PortUtil INSTANCE = new PortUtil();
	private static int ourPortDelay = 500;
	private List<ServerSocket> myControlSockets = new ArrayList<>();
	private Integer myCurrentControlSocketPort = null;
	private int myCurrentOffset = 0;
	/**
	 * Constructor -
	 */
	PortUtil() {
		// nothing
	}

	/**
	 * Clear and release all control sockets
	 */
	synchronized void clearInstance() {
		for (ServerSocket next : myControlSockets) {
			ourLog.info("Releasing control port: {}", next.getLocalPort());
			try {
				next.close();
			} catch (IOException theE) {
				// ignore
			}
		}
		myControlSockets.clear();
		myCurrentControlSocketPort = null;
	}

	/**
	 * Clear and release all control sockets
	 */
	synchronized int getNextFreePort() {

		while (true) {

			// Acquire a control port
			while (myCurrentControlSocketPort == null) {
				int nextCandidate = (int) (Math.random() * 65000.0);
				nextCandidate = nextCandidate - (nextCandidate % SPACE_SIZE);

				if (nextCandidate < 10000) {
					continue;
				}

				try {
					ServerSocket server = new ServerSocket();
					server.setReuseAddress(true);
					server.bind(new InetSocketAddress("localhost", nextCandidate));
					myControlSockets.add(server);
					ourLog.info("Acquired control socket on port {}", nextCandidate);
					myCurrentControlSocketPort = nextCandidate;
					myCurrentOffset = 0;
				} catch (IOException theE) {
					ourLog.info("Candidate control socket {} is already taken", nextCandidate);
					continue;
				}
			}

			// Find a free port within the allowable range
			while (true) {
				myCurrentOffset++;

				if (myCurrentOffset == SPACE_SIZE) {
					// Current space is exhausted
					myCurrentControlSocketPort = null;
					break;
				}

				int nextCandidatePort = myCurrentControlSocketPort + myCurrentOffset;

				// Try to open a port on this socket and use it
				if (!isAvailable(nextCandidatePort)) {
					continue;
				}

				// Log who asked for the port, just in case that's useful
				StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
				StackTraceElement previousElement = Arrays.stream(stackTraceElements)
					.filter(t -> !t.toString().contains("PortUtil.") && !t.toString().contains("getStackTrace"))
					.findFirst()
					.orElse(stackTraceElements[2]);
				ourLog.info("Returned available port {} for: {}", nextCandidatePort, previousElement.toString());

				try {
					Thread.sleep(ourPortDelay);
				} catch (InterruptedException theE) {
					// ignore
				}

				return nextCandidatePort;

			}


		}
	}

	@VisibleForTesting
	public static void setPortDelay(Integer thePortDelay) {
		if (thePortDelay == null) {
			thePortDelay = 500;
		} else {
			ourPortDelay = thePortDelay;
		}
	}

	/**
	 * This method checks if we are able to bind a given port to both
	 * 0.0.0.0 and localhost in order to be sure it's truly available.
	 */
	private static boolean isAvailable(int thePort) {
		ourLog.info("Testing a bind on thePort {}", thePort);
		try (ServerSocket ss = new ServerSocket()) {
			ss.setReuseAddress(true);
			ss.bind(new InetSocketAddress("0.0.0.0", thePort));
			try (DatagramSocket ds = new DatagramSocket()) {
				ds.setReuseAddress(true);
				ds.connect(new InetSocketAddress("127.0.0.1", thePort));
				ourLog.info("Successfully bound thePort {}", thePort);
			} catch (IOException e) {
				ourLog.info("Failed to bind thePort {}: {}", thePort, e.toString());
				return false;
			}
		} catch (IOException e) {
			ourLog.info("Failed to bind thePort {}: {}", thePort, e.toString());
			return false;
		}

		try (ServerSocket ss = new ServerSocket()) {
			ss.setReuseAddress(true);
			ss.bind(new InetSocketAddress("localhost", thePort));
		} catch (IOException e) {
			ourLog.info("Failed to bind thePort {}: {}", thePort, e.toString());
			return false;
		}

		return true;
	}

	/**
	 * The entire purpose here is to find an available port that can then be
	 * bound for by server in a unit test without conflicting with other tests.
	 * <p>
	 * This is really only used for unit tests but is included in the library
	 * so it can be reused across modules. Use with caution.
	 */
	public static int findFreePort() {
		return INSTANCE.getNextFreePort();
	}

}
 
