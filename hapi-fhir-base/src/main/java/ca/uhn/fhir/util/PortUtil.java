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

import java.net.ServerSocket;
import java.util.LinkedHashSet;

/**
 * Provides server ports
 */
@CoverageIgnore
public class PortUtil {
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
		ServerSocket server;
		try {
			int port;
			do {
				server = new ServerSocket(0);
				port = server.getLocalPort();
				server.close();
			} while (!ourPorts.add(port));

			Thread.sleep(500);
			return port;
		} catch (Exception e) {
			//FIXME resource leak
			throw new Error(e);
		}
	}

}
 
