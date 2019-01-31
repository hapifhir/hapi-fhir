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

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Provides server ports
 */
@CoverageIgnore
public class PortUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(PortUtil.class);

	/*
	 * Non instantiable
	 */
	private PortUtil() {
		// nothing
	}

	/**
	 * The entire purpose here is to find an available port that can then be
	 * bound for by server in a unit test without conflicting with other tests.
	 * <p>
	 * This is really only used for unit tests but is included in the library
	 * so it can be reused across modules. Use with caution.
	 */
	public static int findFreePort() {
		ServerSocket server;
		try {
			server = new ServerSocket(0);
			server.setReuseAddress(true);
			int port = server.getLocalPort();

			/*
			 * Try to connect to the newly allocated port to make sure
			 * it's free
			 */
			for (int i = 0; i < 10; i++) {
				try {
					Socket client = new Socket();
					client.connect(new InetSocketAddress(port), 1000);
					break;
				} catch (Exception e) {
					if (i == 9) {
						throw new InternalErrorException("Can not connect to port: " + port);
					}
					Thread.sleep(250);
				}
			}

			server.close();

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
				try {
					Socket client = new Socket();
					client.connect(new InetSocketAddress(port), 1000);
					ourLog.info("Socket still seems open");
					Thread.sleep(250);
				} catch (Exception e) {
					break;
				}
			}

			// ....annnd sleep a bit for the same reason.
			Thread.sleep(500);

			// Log who asked for the port, just in case that's useful
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement previousElement = stackTraceElements[2];
			ourLog.info("Returned available port {} for: {}", port, previousElement.toString());

			return port;
		} catch (IOException | InterruptedException e) {
			throw new Error(e);
		}
	}

}
 
