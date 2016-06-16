package ca.uhn.fhir.util;

import java.net.ServerSocket;

/**
 * Provides server ports
 */
@CoverageIgnore
public class PortUtil {

	/*
	 * Non instantiable
	 */
	private PortUtil() {
		// nothing
	} 
	
	/**
	 * This is really only used for unit tests but is included in the library so it can be reused across modules. Use with caution.
	 */
	public static int findFreePort() {
		ServerSocket server;
		try {
			server = new ServerSocket(0);
			int port = server.getLocalPort();
			server.close();
			Thread.sleep(500);
			return port;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

}
 
