package ca.uhn.fhir.util;

import org.junit.After;
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

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

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

	@After
	public void after() {
		PortUtil.setPortDelay(null);
	}

	@Test
	public void testPortsAreNotReused() throws InterruptedException {
		PortUtil.setPortDelay(0);

		List<Integer> ports = Collections.synchronizedList(new ArrayList<>());
		List<PortUtil> portUtils = Collections.synchronizedList(new ArrayList<>());
		List<String> errors = Collections.synchronizedList(new ArrayList<>());

		int tasksCount = 20;
		ExecutorService pool = Executors.newFixedThreadPool(tasksCount);
		int portsPerTaskCount = 151;
		for (int i = 0; i < tasksCount; i++) {
			pool.submit(() -> {
				PortUtil portUtil = new PortUtil();
				portUtils.add(portUtil);
				for (int j = 0; j < portsPerTaskCount; j++) {
					int nextFreePort = portUtil.getNextFreePort();

					boolean bound;
					try (ServerSocket ss = new ServerSocket()) {
						ss.bind(new InetSocketAddress("localhost", nextFreePort));
						bound = true;
					} catch (IOException e) {
						bound = false;
					}

					if (!bound) {
						try (ServerSocket ss = new ServerSocket()) {
							Thread.sleep(1000);
							ss.bind(new InetSocketAddress("localhost", nextFreePort));
						} catch (Exception e) {
							String msg = "Failure binding new port (second attempt) " + nextFreePort + ": " + e.toString();
							ourLog.error(msg, e);
							errors.add(msg);
						}
					}

					ports.add(nextFreePort);
				}

			});
		}
		pool.shutdown();
		pool.awaitTermination(60, TimeUnit.SECONDS);

		assertThat(errors.toString(), errors, empty());
		assertEquals(tasksCount * portsPerTaskCount, ports.size());

		while (ports.size() > 0) {
			Integer nextPort = ports.remove(0);
			if (ports.contains(nextPort)) {
				fail("Port " + nextPort + " was given out more than once");
			}
		}

		for (PortUtil next : portUtils) {
			next.clearInstance();
		}
	}
}
