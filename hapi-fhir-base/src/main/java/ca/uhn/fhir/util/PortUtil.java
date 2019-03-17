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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides server ports that are free, in order for tests to use them
 *
 * This class is not designed for non-testing usage, as it holds on to server ports
 * for a long time (potentially lots of them!)
 */
@CoverageIgnore
public class PortUtil {
	public static final int SPACE_SIZE = 100;
	private static final Logger ourLog = LoggerFactory.getLogger(PortUtil.class);
	private static final PortUtil INSTANCE = new PortUtil();
	private List<ServerSocket> myControlSockets = new ArrayList<>();
	private Integer myCurrentControlSocketPort = null;
	private int myCurrentOffset = 0;


	/*
	 * Non instantiable
	 */
	public PortUtil() {
		// nothing
	}

	public synchronized void clear() {
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

	public synchronized int getNextFreePort() {

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
				try (ServerSocket server = new ServerSocket()) {
					server.setReuseAddress(true);
					server.bind(new InetSocketAddress("localhost", nextCandidatePort));
					try (Socket client = new Socket()) {
						client.setReuseAddress(true);
						client.connect(new InetSocketAddress("localhost", nextCandidatePort));
					}
				} catch (IOException e) {
					continue;
				}

				// Log who asked for the port, just in case that's useful
				StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
				StackTraceElement previousElement = stackTraceElements[2];
				ourLog.info("Returned available port {} for: {}", nextCandidatePort, previousElement.toString());

				/*
				 * This is an attempt to make sure the port is actually
				 * free before releasing it. For whatever reason on Linux
				 * it seems like even after we close the ServerSocket there
				 * is a short while where it is not possible to bind the
				 * port, even though it should be released by then.
				 *
				 * I don't have any solid evidence that this is a good
				 * way to do this, but it seems to help...
				 */
				for (int i = 0; i < 10; i++) {
					try (Socket client = new Socket()) {
						client.setReuseAddress(true);
						client.connect(new InetSocketAddress(nextCandidatePort), 1000);
						ourLog.info("Socket still seems open");
						Thread.sleep(250);
					} catch (Exception e) {
						break;
					}
				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException theE) {
					// ignore
				}

				return nextCandidatePort;

			}


		}
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
 
