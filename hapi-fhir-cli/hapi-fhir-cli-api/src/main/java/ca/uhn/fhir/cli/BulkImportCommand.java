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

import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportFileServlet;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

public class BulkImportCommand extends BaseCommand {

	public static final String BULK_IMPORT = "bulk-import";
	public static final String SOURCE_BASE = "source-base";
	public static final String SOURCE_DIRECTORY = "source-directory";
	public static final String TARGET_BASE = "target-base";
	public static final String PORT = "port";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportCommand.class);
	private static volatile boolean ourEndNow;
	private BulkImportFileServlet myServlet;
	private Server myServer;
	private Integer myPort;

	@Override
	public String getCommandDescription() {
		return "Initiates a bulk import against a FHIR server using the $import " +
			"operation, and creates a local HTTP server to serve the contents. " +
			"This command does not currently support HTTPS so it is only intended " +
			"for testing scenarios.";
	}

	@Override
	public String getCommandName() {
		return BULK_IMPORT;
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);
		addRequiredOption(options, null, PORT, PORT, "The port to listen on. If set to 0, an available free port will be selected.");
		addOptionalOption(options, null, SOURCE_BASE, "base url", "The URL to advertise as the base URL for accessing the files (i.e. this is the address that this command will declare that it is listening on). If not present, the server will default to \"http://localhost:[port]\" which will only work if the server is on the same host.");
		addRequiredOption(options, null, SOURCE_DIRECTORY, "directory", "The source directory. This directory will be scanned for files with an extensions of .json, .ndjson, .json.gz and .ndjson.gz, and any files in this directory will be assumed to be NDJSON and uploaded. This command will read the first resource from each file to verify its resource type, and will assume that all resources in the file are of the same type.");
		addRequiredOption(options, null, TARGET_BASE, "base url", "The base URL of the target FHIR server.");
		addBasicAuthOption(options);
		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {
		ourEndNow = false;

		parseFhirContext(theCommandLine);

		String baseDirectory = theCommandLine.getOptionValue(SOURCE_DIRECTORY);
		myPort = getAndParseNonNegativeIntegerParam(theCommandLine, PORT);

		ourLog.info("Scanning directory for NDJSON files: {}", baseDirectory);
		List<String> resourceTypes = new ArrayList<>();
		List<File> files = new ArrayList<>();
		scanDirectoryForJsonFiles(baseDirectory, resourceTypes, files);
		ourLog.info("Found {} files", files.size());

		ourLog.info("Starting server on port: {}", myPort);
		List<String> indexes = startServer(myPort, files);
		String sourceBaseUrl = "http://localhost:" + myPort;
		if (theCommandLine.hasOption(SOURCE_BASE)) {
			sourceBaseUrl = theCommandLine.getOptionValue(SOURCE_BASE);
		}
		ourLog.info("Server has been started in port: {}", myPort);

		String targetBaseUrl = theCommandLine.getOptionValue(TARGET_BASE);
		ourLog.info("Initiating bulk import against server: {}", targetBaseUrl);
		IGenericClient client = newClient(theCommandLine, TARGET_BASE, BASIC_AUTH_PARAM, BEARER_TOKEN_PARAM_LONGOPT);
		client.registerInterceptor(new LoggingInterceptor(false));

		IBaseParameters request = createRequest(sourceBaseUrl, indexes, resourceTypes);
		IBaseResource outcome = client
			.operation()
			.onServer()
			.named(JpaConstants.OPERATION_IMPORT)
			.withParameters(request)
			.returnResourceType(myFhirCtx.getResourceDefinition("OperationOutcome").getImplementingClass())
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.execute();

		ourLog.info("Got response: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		ourLog.info("Bulk import is now running. Do not terminate this command until all files have been downloaded.");

		while (true) {
			if (ourEndNow) {
				break;
			}
		}

	}

	@Nonnull
	private IBaseParameters createRequest(String theBaseUrl, List<String> theIndexes, List<String> theResourceTypes) {

		FhirContext ctx = getFhirContext();
		IBaseParameters retVal = ParametersUtil.newInstance(ctx);

		ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_INPUT_FORMAT, "code", Constants.CT_FHIR_NDJSON);
		ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_INPUT_SOURCE, "code", theBaseUrl);

		IBase storageDetail = ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_STORAGE_DETAIL);
		ParametersUtil.addPartString(ctx, storageDetail, BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE, BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS);

		for (int i = 0; i < theIndexes.size(); i++) {
			IBase input = ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_INPUT);
			ParametersUtil.addPartCode(ctx, input, BulkDataImportProvider.PARAM_INPUT_TYPE, theResourceTypes.get(i));
			String nextUrl = theBaseUrl + "/download?index=" + theIndexes.get(i);
			ParametersUtil.addPartUrl(ctx, input, BulkDataImportProvider.PARAM_INPUT_URL, nextUrl);
		}

		return retVal;
	}

	private List<String> startServer(int thePort, List<File> files) {
		List<String> indexes = new ArrayList<>();
		myServer = new Server(thePort);

		myServlet = new BulkImportFileServlet();
		for (File t : files) {

			BulkImportFileServlet.IFileSupplier fileSupplier = new BulkImportFileServlet.IFileSupplier() {
				@Override
				public boolean isGzip() {
					return t.getName().toLowerCase(Locale.ROOT).endsWith(".gz");
				}

				@Override
				public InputStream get() throws IOException {
					return new FileInputStream(t);
				}
			};
			indexes.add(myServlet.registerFile(fileSupplier));
		}

		ServletHolder servletHolder = new ServletHolder(myServlet);

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/");
		contextHandler.addServlet(servletHolder, "/*");

		myServer.setHandler(contextHandler);
		try {
			myServer.start();
		} catch (Exception e) {
			throw new CommandFailureException(Msg.code(2057) + e.getMessage(), e);
		}

		Connector[] connectors = myServer.getConnectors();
		myPort = ((ServerConnector) (connectors[0])).getLocalPort();

		return indexes;
	}

	private void scanDirectoryForJsonFiles(String baseDirectory, List<String> types, List<File> files) {
		try {
			File directory = new File(baseDirectory);
			final String[] extensions = new String[]{".json", ".ndjson", ".json.gz", ".ndjson.gz"};
			final IOFileFilter filter = FileFileFilter.INSTANCE.and(new SuffixFileFilter(extensions, IOCase.INSENSITIVE));
			PathUtils
				.walk(directory.toPath(), filter, 1, false, FileVisitOption.FOLLOW_LINKS)
				.map(Path::toFile)
				.filter(t -> t.isFile())
				.filter(t -> t.exists())
				.forEach(t -> files.add(t));
			if (files.isEmpty()) {
				throw new CommandFailureException(Msg.code(2058) + "No files found in directory \"" + directory.getAbsolutePath() + "\". Allowed extensions: " + Arrays.asList(extensions));
			}

			FhirContext ctx = getFhirContext();
			for (File next : files) {
				try (InputStream nextIs = new FileInputStream(next)) {
					InputStream is;
					if (next.getName().toLowerCase(Locale.ROOT).endsWith(".gz")) {
						is = new GZIPInputStream(nextIs);
					} else {
						is = nextIs;
					}
					Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
					LineIterator lineIterator = new LineIterator(reader);
					String firstLine = lineIterator.next();
					IBaseResource resource = ctx.newJsonParser().parseResource(firstLine);
					types.add(myFhirCtx.getResourceType(resource));
				}
			}

		} catch (IOException e) {
			throw new CommandFailureException(Msg.code(2059) + e.getMessage(), e);
		}
	}

	public static void setEndNowForUnitTest(boolean theEndNow) {
		ourEndNow = theEndNow;
	}

}

