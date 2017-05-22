package ca.uhn.fhir.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import ca.uhn.fhir.jpa.demo.ContextHolder;
import ca.uhn.fhir.jpa.demo.FhirServerConfig;
import ca.uhn.fhir.jpa.demo.FhirServerConfigDstu3;

public class RunServerCommand extends BaseCommand {

	private static final String OPTION_DISABLE_REFERENTIAL_INTEGRITY = "disable-referential-integrity";
	private static final String OPTION_LOWMEM = "lowmem";
	private static final String OPTION_ALLOW_EXTERNAL_REFS = "allow-external-refs";
	private static final int DEFAULT_PORT = 8080;
	private static final String OPTION_P = "p";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RunServerCommand.class);
	private int myPort;

	private Server myServer;

	@Override
	public String getCommandName() {
		return "run-server";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);
		options.addOption(OPTION_P, "port", true, "The port to listen on (default is " + DEFAULT_PORT + ")");
		options.addOption(null, OPTION_LOWMEM, false, "If this flag is set, the server will operate in low memory mode (some features disabled)");
		options.addOption(null, OPTION_ALLOW_EXTERNAL_REFS, false, "If this flag is set, the server will allow resources to be persisted contaning external resource references");
		options.addOption(null, OPTION_DISABLE_REFERENTIAL_INTEGRITY, false, "If this flag is set, the server will not enforce referential integrity");
		return options;
	}

	private int parseOptionInteger(CommandLine theCommandLine, String opt, int defaultPort) throws ParseException {
		try {
			return Integer.parseInt(theCommandLine.getOptionValue(opt, Integer.toString(defaultPort)));
		} catch (NumberFormatException e) {
			throw new ParseException("Invalid value '" + theCommandLine.getOptionValue(opt) + " (must be numeric)");
		}
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		myPort = parseOptionInteger(theCommandLine, OPTION_P, DEFAULT_PORT);
		
		if (theCommandLine.hasOption(OPTION_LOWMEM)) {
			ourLog.info("Running in low memory mode, some features disabled");
			System.setProperty(OPTION_LOWMEM, OPTION_LOWMEM);
		}
		
		if (theCommandLine.hasOption(OPTION_ALLOW_EXTERNAL_REFS)) {
			ourLog.info("Server is configured to allow external references");
			ContextHolder.setAllowExternalRefs(true);
		}

		if (theCommandLine.hasOption(OPTION_DISABLE_REFERENTIAL_INTEGRITY)) {
			ourLog.info("Server is configured to not enforce referential integrity");
			ContextHolder.setDisableReferentialIntegrity(true);
		}

		ContextHolder.setCtx(getSpecVersionContext(theCommandLine));

		ourLog.info("Preparing HAPI FHIR JPA server on port {}", myPort);
		File tempWarFile;
		try {
			tempWarFile = File.createTempFile("hapi-fhir", ".war");
			tempWarFile.deleteOnExit();
			
			InputStream inStream = RunServerCommand.class.getResourceAsStream("/hapi-fhir-cli-jpaserver.war");
			OutputStream outStream = new BufferedOutputStream(new FileOutputStream(tempWarFile, false));
			IOUtils.copy(inStream, outStream);
		} catch (IOException e) {
			ourLog.error("Failed to create temporary file", e);
			return;
		}

		final ContextLoaderListener cll = new ContextLoaderListener();
		
		ourLog.info("Starting HAPI FHIR JPA server in {} mode", ContextHolder.getCtx().getVersion().getVersion());
		WebAppContext root = new WebAppContext();
		root.setAllowDuplicateFragmentNames(true);
		root.setWar(tempWarFile.getAbsolutePath());
		root.setParentLoaderPriority(true);
		root.setContextPath("/");
		root.addEventListener(new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent theSce) {
				theSce.getServletContext().setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
				switch (ContextHolder.getCtx().getVersion().getVersion()) {
				case DSTU2:
					theSce.getServletContext().setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, FhirServerConfig.class.getName());
					break;
				case DSTU3:
					theSce.getServletContext().setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, FhirServerConfigDstu3.class.getName());
					break;
				case DSTU1:
				case DSTU2_1:
				case DSTU2_HL7ORG:
					break;
				}
				cll.contextInitialized(theSce);
			}

			@Override
			public void contextDestroyed(ServletContextEvent theSce) {
				cll.contextDestroyed(theSce);
			}
		});
		
		String path = ContextHolder.getPath();
		root.addServlet("ca.uhn.fhir.jpa.demo.JpaServerDemo", path + "*");
		
		myServer = new Server(myPort);
		myServer.setHandler(root);
		try {
			myServer.start();
		} catch (SocketException e) {
			throw new CommandFailureException("Server failed to start on port " + myPort + " because of the following error \"" + e.toString() + "\". Note that you can use the '-p' option to specify an alternate port."); 
		} catch (Exception e) {
			ourLog.error("Server failed to start", e);
			throw new CommandFailureException("Server failed to start", e);
		}

		ourLog.info("Server started on port {}", myPort);
		ourLog.info("Web Testing UI : http://localhost:{}/", myPort);
		ourLog.info("Server Base URL: http://localhost:{}{}", myPort, path);
		
		
	}

	public static void main(String[] theArgs) {


		 Server server = new Server(22);
		 String path = "../hapi-fhir-cli-jpaserver";
		 WebAppContext webAppContext = new WebAppContext();
		 webAppContext.setContextPath("/");
		 webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		 webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-example");
		 webAppContext.setParentLoaderPriority(true);
		
		 server.setHandler(webAppContext);
		 try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		 ourLog.info("Started");
	}

	@Override
	public String getCommandDescription() {
		return "Start a FHIR server which can be used for testing";
	}

}
