
package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyUtil {
    
    /**
     * Gets the local port for the given server. The server must be started.
     */
    public static int getPortForStartedServer(Server server) {
        assert server.isStarted();
        Connector[] connectors = server.getConnectors();
        assert connectors.length == 1;
        return ((ServerConnector) (connectors[0])).getLocalPort();
    }
    
    /**
     * Starts the given Jetty server, and configures it for graceful shutdown
     */
    public static void startServer(Server server) throws Exception {
        //Needed for graceful shutdown, see https://github.com/eclipse/jetty.project/issues/2076#issuecomment-353717761
        server.insertHandler(new StatisticsHandler());
        server.start();
    }
    
    /**
     * Shut down the given Jetty server, and release held resources. 
     */
    public static void closeServer(Server server) throws Exception {
        server.stop();
        server.destroy();
    }
    
}
