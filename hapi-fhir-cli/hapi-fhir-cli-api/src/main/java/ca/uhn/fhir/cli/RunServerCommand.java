package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.demo.ContextHolder;
import ca.uhn.fhir.jpa.demo.FhirServerConfig;
import ca.uhn.fhir.jpa.demo.FhirServerConfigDstu3;
import ca.uhn.fhir.jpa.demo.FhirServerConfigR4;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.net.SocketException;

public class RunServerCommand extends BaseCommand {

	private static final String OPTION_DISABLE_REFERENTIAL_INTEGRITY = "disable-referential-integrity";
	private static final String OPTION_LOWMEM = "lowmem";
	private static final String OPTION_ALLOW_EXTERNAL_REFS = "allow-external-refs";
	private static final String OPTION_REUSE_SEARCH_RESULTS_MILLIS = "reuse-search-results-milliseconds";
	private static final int DEFAULT_PORT = 8080;
	private static final String OPTION_P = "p";

	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RunServerCommand.class);
	public static final String RUN_SERVER_COMMAND = "run-server";
	private int myPort;

	private Server myServer;

	@Override
	public String getCommandName() {
		return RUN_SERVER_COMMAND;
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);
		options.addOption(OPTION_P, "port", true, "The port to listen on (default is " + DEFAULT_PORT + ")");
		options.addOption(null, OPTION_LOWMEM, false, "If this flag is set, the server will operate in low memory mode (some features disabled)");
		options.addOption(null, OPTION_ALLOW_EXTERNAL_REFS, false, "If this flag is set, the server will allow resources to be persisted contaning external resource references");
		options.addOption(null, OPTION_DISABLE_REFERENTIAL_INTEGRITY, false, "If this flag is set, the server will not enforce referential integrity");

		addOptionalOption(options, "u", "url", "Url", "If this option is set, specifies the JDBC URL to use for the database connection");

		Long defaultReuseSearchResults = DaoConfig.DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS;
		String defaultReuseSearchResultsStr = defaultReuseSearchResults == null ? "off" : String.valueOf(defaultReuseSearchResults);
		options.addOption(null, OPTION_REUSE_SEARCH_RESULTS_MILLIS, true, "The time in milliseconds within which the same results will be returned for multiple identical searches, or \"off\" (default is " + defaultReuseSearchResultsStr + ")");
		return options;
	}

	private int parseOptionInteger(CommandLine theCommandLine, String opt, int defaultPort) throws ParseException {
		try {
			return Integer.parseInt(theCommandLine.getOptionValue(opt, Integer.toString(defaultPort)));
		} catch (NumberFormatException e) {
			throw new ParseException(Msg.code(1558) + "Invalid value '" + theCommandLine.getOptionValue(opt) + "' (must be numeric)");
		}
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

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

		ContextHolder.setDatabaseUrl(theCommandLine.getOptionValue("u"));

		String reuseSearchResults = theCommandLine.getOptionValue(OPTION_REUSE_SEARCH_RESULTS_MILLIS);
		if (reuseSearchResults != null) {
			if (reuseSearchResults.equals("off")) {
				ourLog.info("Server is configured to not reuse search results");
				ContextHolder.setReuseCachedSearchResultsForMillis(null);
			} else {
				try {
					long reuseSearchResultsMillis = Long.parseLong(reuseSearchResults);
					if (reuseSearchResultsMillis < 0) {
						throw new NumberFormatException(Msg.code(1559) + "expected a positive integer");
					}
					ourLog.info("Server is configured to reuse search results for " + String.valueOf(reuseSearchResultsMillis) + " milliseconds");
					ContextHolder.setReuseCachedSearchResultsForMillis(reuseSearchResultsMillis);
				} catch (NumberFormatException e) {
					throw new ParseException(Msg.code(1560) + "Invalid value '" + reuseSearchResults + "' (must be a positive integer)");
				}
			}
		}

		ContextHolder.setCtx(getFhirContext());

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
				case R4:
					theSce.getServletContext().setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, FhirServerConfigR4.class.getName());
					break;
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
			throw new CommandFailureException(Msg.code(1561) + "Server failed to start on port " + myPort + " because of the following error \"" + e.toString() + "\". Note that you can use the '-p' option to specify an alternate port."); 
		} catch (Exception e) {
			ourLog.error("Server failed to start", e);
			throw new CommandFailureException(Msg.code(1562) + "Server failed to start", e);
		}

		ourLog.info("Server started on port {}", myPort);
		ourLog.info("Web Testing UI : http://localhost:{}/", myPort);
		ourLog.info("Server Base URL: http://localhost:{}{}", myPort, path);

		// Never quit.. We'll let the user ctrl-C their way out.
		loopForever();

	}

	@SuppressWarnings("InfiniteLoopStatement")
	private void loopForever() {
		while (true) {
			try {
				Thread.sleep(DateUtils.MILLIS_PER_MINUTE);
			} catch (InterruptedException theE) {
				// ignore
			}
		}
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
