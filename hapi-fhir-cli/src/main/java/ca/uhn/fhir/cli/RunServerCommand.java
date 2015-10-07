package ca.uhn.fhir.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class RunServerCommand extends BaseCommand {

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
		options.addOption(OPTION_P, "port", true, "The port to listen on (default is " + DEFAULT_PORT + ")");
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

//		((ch.qos.logback.classic.Logger)LoggerFactory.getLogger("/")).setLevel(Level.ERROR);

		ourLog.info("Preparing HAPI FHIR JPA server");
		File tempWarFile;
		try {
			tempWarFile = File.createTempFile("hapi-fhir", ".war");
			tempWarFile.deleteOnExit();
			
			InputStream inStream = RunServerCommand.class.getResourceAsStream("/hapi-fhir-jpaserver-example.war");
			OutputStream outStream = new BufferedOutputStream(new FileOutputStream(tempWarFile, false));
			IOUtils.copy(inStream, outStream);
		} catch (IOException e) {
			ourLog.error("Failed to create temporary file", e);
			return;
		}

		ourLog.info("Starting HAPI FHIR JPA server");
		WebAppContext root = new WebAppContext();
		root.setAllowDuplicateFragmentNames(true);
		root.setWar(tempWarFile.getAbsolutePath());
		root.setParentLoaderPriority(true);
		root.setContextPath("/");
		
		myServer = new Server(myPort);
		myServer.setHandler(root);
		try {
			myServer.start();
		} catch (Exception e) {
			ourLog.error("Server failed to start", e);
			return;
		}

		ourLog.info("Server started on port {}", myPort);
		ourLog.info("Web Testing UI : http://localhost:{}/", myPort);
		ourLog.info("Server Base URL: http://localhost:{}/baseDstu2/", myPort);
		
		
	}

	public void run(String[] theArgs) {

		getOptions();

		// myServer = new Server(myPort);
		//
		// WebAppContext webAppContext = new WebAppContext();
		// webAppContext.setContextPath("/");
		// webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		// webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-example");
		// webAppContext.setParentLoaderPriority(true);
		//
		// myServer.setHandler(webAppContext);
		// myServer.start();

	}

	@Override
	public String getCommandDescription() {
		return "Start a FHIR server which can be used for testing";
	}

}
