package ca.uhn.fhir.rest.server.security;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides server ports
 */
public class PortUtil {

	private static List<Integer> ourPorts = new ArrayList<Integer>(); 
	
	/**
	 * This is really only used for unit tests but is included in the library so it can be reused across modules. Use with caution.
	 */
	public static int findFreePort() {
		ServerSocket server;
		try {
			server = new ServerSocket(0);
			int port = server.getLocalPort();
			ourPorts.add(port);
			server.close();
			Thread.sleep(500);
			return port;
		} catch (IOException e) {
			throw new Error(e);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	public static List<Integer> list() {
		return ourPorts;
	}

}
 