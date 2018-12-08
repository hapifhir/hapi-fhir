package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;

/**
 * Provides server ports
 */
@CoverageIgnore
public class PortUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(PortUtil.class);
	private static LinkedHashSet<Integer> ourPorts = new LinkedHashSet<>();

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
		try {
			int port;
			do {
				try (ServerSocket server = new ServerSocket(0)) {
					server.setReuseAddress(true);
					port = server.getLocalPort();
				}
			} while (!ourPorts.add(port));

			// Make sure that we can't connect to the server, meaning the port is
			// successfully released
			for (int i = 0; i < 20; i++) {
				Socket connector = new Socket();
				try {
					connector.connect(new InetSocketAddress(port));
				} catch (ConnectException e) {
					break;
				}
				ourLog.info("Port {} is still in use - Waiting...", port);
				Thread.sleep(250);
			}

			Thread.sleep(1000);

			return port;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

}
 
