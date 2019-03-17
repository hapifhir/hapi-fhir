package ca.uhn.fhir.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PortUtilTest {

	private static final Logger ourLog = LoggerFactory.getLogger(PortUtilTest.class);

	@Test
	public void testFindFreePort() throws IOException {
		int port = PortUtil.findFreePort();

		// First bind should succeed, second bind should fail
		try (ServerSocket ss = new ServerSocket()) {
			ss.bind(new InetSocketAddress("0.0.0.0", port));

			try (ServerSocket ss2 = new ServerSocket()) {
				ss2.bind(new InetSocketAddress("0.0.0.0", port));
				fail();
			} catch (IOException e) {
				// good
			}

		}
	}

	@Test
	public void testPortsAreNotReused() throws InterruptedException {

		List<Integer> ports = Collections.synchronizedList(new ArrayList<>());
		List<PortUtil> portUtils = Collections.synchronizedList(new ArrayList<>());

		int tasksCount = 50;
		ExecutorService pool = Executors.newFixedThreadPool(tasksCount);
		int portsPerTaskCount = 500;
		for (int i = 0; i < tasksCount; i++) {
			pool.submit(() -> {
				PortUtil portUtil = new PortUtil();
				portUtils.add(portUtil);
				for (int j = 0; j < portsPerTaskCount; j++) {
					int nextFreePort = portUtil.getNextFreePort();

					try (ServerSocket ss = new ServerSocket()) {
						ss.bind(new InetSocketAddress("localhost", nextFreePort));
					} catch (IOException e) {
						ourLog.error("Failure binding new port " + nextFreePort + ": " + e.toString(), e);
						fail(e.toString());
					}

					ports.add(nextFreePort);
				}

			});
		}
		pool.shutdown();
		pool.awaitTermination(60, TimeUnit.SECONDS);

		assertEquals(tasksCount * portsPerTaskCount, ports.size());

		while (ports.size() > 0) {
			Integer nextPort = ports.remove(0);
			if (ports.contains(nextPort)) {
				fail("Port " + nextPort + " was given out more than once");
			}
		}

		for (PortUtil next : portUtils) {
			next.clear();
		}
	}
}
