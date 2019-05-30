
package ca.uhn.fhir.util;

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
