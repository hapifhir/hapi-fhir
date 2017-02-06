package embedded;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.inject.servlet.GuiceFilter;

public class ServerStartup {

	public static void main(final String[] args) throws Exception {

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		final Server server = new Server(9090);
		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new ContextListener());
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");
		server.start();

		// Service is now accessible through
		// http://localhost:9090/model/Practitioner
	}

}
