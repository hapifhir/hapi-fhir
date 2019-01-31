package ca.uhn.fhir.util;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class PortUtilTest {

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

}
