/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cli;

import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportFileServlet;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportReportJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkImportCommand extends BaseCommand {

	public static final String BULK_IMPORT = "bulk-import";
	public static final String SOURCE_BASE = "source-base";
	public static final String SOURCE_DIRECTORY = "source-directory";
	public static final String TARGET_BASE = "target-base";
	public static final String PORT = "port";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportCommand.class);
	public static final String CHUNK_BY_COMPARTMENT_NAME = "chunk-by-compartment-name";
	public static final String BATCH_SIZE = "batch-size";
	private BulkImportFileServlet myServlet;
	private Server myServer;
	private Integer myPort;

	@Override
	public String getCommandDescription() {
		return "Initiates a bulk import against a FHIR server using the $import "
				+ "operation, and creates a local HTTP server to serve the contents. "
				+ "This command does not currently support HTTPS so it is only intended "
				+ "for testing scenarios.";
	}

	@Override
	public String getCommandName() {
		return BULK_IMPORT;
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);
		addRequiredOption(
				options,
				null,
				PORT,
				PORT,
				"The port to listen on. If set to 0, an available free port will be selected.");
		addOptionalOption(
				options,
				null,
				SOURCE_BASE,
				"base url",
				"The URL to advertise as the base URL for accessing the files (i.e. this is the address that this command will declare that it is listening on). If not present, the server will default to \"http://localhost:[port]\" which will only work if the server is on the same host.");
		addOptionalOption(
				options,
				null,
				BATCH_SIZE,
				"batch size",
				"If set, specifies the number of resources to process in a single work chunk. Smaller numbers can avoid timeouts, larger numbers can give better throughput. Always measure the effect of different settings.");
		addRequiredOption(
				options,
				null,
				SOURCE_DIRECTORY,
				"directory",
				"The source directory. This directory will be scanned for files with an extensions of .json, .ndjson, .json.gz and .ndjson.gz, and any files in this directory will be assumed to be NDJSON and uploaded. This command will read the first resource from each file to verify its resource type, and will assume that all resources in the file are of the same type.");
		addOptionalOption(
				options,
				null,
				CHUNK_BY_COMPARTMENT_NAME,
				"compartment name",
				"If specified, requests that the bulk import process try to process resources in chunks with other resources beloning to the same compartment. For example, if the value of \"Patient\" is used, resources belonging to the same patient will be placed in the same work chunk(s) for ingestion. This should not functionally affect processing and there is no guarantee that all resources in the same compartment will end up in the same work chunk, but specifying a grouping can help to make processing more efficient by grouping related resources in a single database transaction, and may reduce constraint errors if 'Automatically Create Placeholder Reference Targets' is enabled on the target server.");
		addRequiredOption(options, null, TARGET_BASE, "base url", "The base URL of the target FHIR server.");
		addBasicAuthOption(options);
		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {
		parseFhirContext(theCommandLine);

		String baseDirectory = theCommandLine.getOptionValue(SOURCE_DIRECTORY);
		myPort = getAndParseNonNegativeIntegerParam(theCommandLine, PORT);

		ourLog.info("Scanning directory for NDJSON files: {}", baseDirectory);
		List<String> resourceTypes = new ArrayList<>();
		List<File> files = new ArrayList<>();
		scanDirectoryForJsonFiles(baseDirectory, resourceTypes, files);
		ourLog.info("Found {} files", files.size());

		ourLog.info("Starting server on port: {}", myPort);
		List<String> indexes = startServer(files);
		String sourceBaseUrl = "http://localhost:" + myPort;
		if (theCommandLine.hasOption(SOURCE_BASE)) {
			sourceBaseUrl = theCommandLine.getOptionValue(SOURCE_BASE);
		}
		ourLog.info("Server has been started in port: {}", myPort);

		String targetBaseUrl = theCommandLine.getOptionValue(TARGET_BASE);
		ourLog.info("Initiating bulk import against server: {}", targetBaseUrl);
		IGenericClient client = newClient(
				theCommandLine, TARGET_BASE, BASIC_AUTH_PARAM, BEARER_TOKEN_PARAM_LONGOPT, TLS_AUTH_PARAM_LONGOPT);

		IBaseParameters request = createRequest(sourceBaseUrl, indexes, resourceTypes, null, theCommandLine);

		IBaseResource outcome = client.operation()
				.onServer()
				.named(JpaConstants.OPERATION_IMPORT)
				.withParameters(request)
				.returnResourceType(
						myFhirCtx.getResourceDefinition("OperationOutcome").getImplementingClass())
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
				.execute();

		ourLog.debug(
				"Got response: {}",
				myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		ourLog.info("Bulk import is now running. Do not terminate this command until all files have been uploaded.");

		checkJobComplete(outcome.getIdElement().toString(), client);

		try {
			myServer.stop();
		} catch (Exception e) {
			ourLog.warn("Failed to stop server: {}", e.getMessage());
		}
	}

	private void checkJobComplete(String url, IGenericClient client) {
		String jobId = url.substring(url.indexOf("=") + 1);

		MethodOutcome response = null;
		IBaseOperationOutcome operationOutcomeResponse = null;
		while (true) {
			// handle NullPointerException
			if (jobId == null) {
				ourLog.error("The jobId cannot be null.");
				break;
			}

			try {
				response = client.operation()
						.onServer()
						.named(JpaConstants.OPERATION_IMPORT_POLL_STATUS)
						.withSearchParameter(Parameters.class, "_jobId", new StringParam(jobId))
						.returnMethodOutcome()
						.execute();
			} catch (InternalErrorException e) {
				// handle ERRORED status
				ourLog.error(e.getMessage());
				operationOutcomeResponse = e.getOperationOutcome();
				break;
			}

			if (response.getResponseStatusCode() == 200) {
				operationOutcomeResponse = response.getOperationOutcome();
				break;
			} else if (response.getResponseStatusCode() == 202) {
				// still in progress
				ThreadUtils.sleepQuietly(Duration.ofSeconds(1));
			} else {
				throw new InternalErrorException(
						Msg.code(2138) + "Unexpected response status code: " + response.getResponseStatusCode() + ".");
			}
		}

		boolean haveOutput = false;
		if (operationOutcomeResponse != null) {
			int issueCount = OperationOutcomeUtil.getIssueCount(myFhirCtx, operationOutcomeResponse);
			for (int i = 0; i < issueCount; i++) {
				String diagnostics = OperationOutcomeUtil.getIssueDiagnostics(myFhirCtx, operationOutcomeResponse, i);
				if (isNotBlank(diagnostics)) {
					if (diagnostics.startsWith("{")) {
						BulkImportReportJson report = JsonUtil.deserialize(diagnostics, BulkImportReportJson.class);
						ourLog.info("Output:\n{}", report.getReportMsg());
					} else {
						ourLog.info(diagnostics);
					}
					haveOutput = true;
				}
			}
		}
		if (!haveOutput) {
			ourLog.info("No diagnostics response received from bulk import URL: {}", url);
		}
	}

	@Nonnull
	private IBaseParameters createRequest(
			String theBaseUrl,
			List<String> theIndexes,
			List<String> theResourceTypes,
			Integer theBatchSize_,
			CommandLine theCommandLine)
			throws ParseException {

		FhirContext ctx = getFhirContext();
		IBaseParameters retVal = ParametersUtil.newInstance(ctx);

		ParametersUtil.addParameterToParameters(
				ctx, retVal, BulkDataImportProvider.PARAM_INPUT_FORMAT, "code", Constants.CT_FHIR_NDJSON);
		ParametersUtil.addParameterToParameters(
				ctx, retVal, BulkDataImportProvider.PARAM_INPUT_SOURCE, "code", theBaseUrl);

		IBase storageDetail =
				ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_STORAGE_DETAIL);
		ParametersUtil.addPartString(
				ctx,
				storageDetail,
				BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE,
				BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS);

		// Batch size
		Integer batchSize = null;
		if (theCommandLine.hasOption(BATCH_SIZE)) {
			batchSize = getAndParsePositiveIntegerParam(theCommandLine, BATCH_SIZE);
		}
		if (batchSize != null) {
			ParametersUtil.addPartInteger(
					ctx,
					storageDetail,
					BulkDataImportProvider.PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT,
					batchSize);
		}

		// Chunk by compartment
		if (theCommandLine.hasOption(CHUNK_BY_COMPARTMENT_NAME)) {
			String chunkByCompartmentName = theCommandLine.getOptionValue(CHUNK_BY_COMPARTMENT_NAME);
			ParametersUtil.addPartString(
					ctx,
					storageDetail,
					BulkDataImportProvider.PARAM_STORAGE_DETAIL_CHUNK_BY_COMPARTMENT_NAME,
					chunkByCompartmentName);
		}

		for (int i = 0; i < theIndexes.size(); i++) {
			IBase input = ParametersUtil.addParameterToParameters(ctx, retVal, BulkDataImportProvider.PARAM_INPUT);
			ParametersUtil.addPartCode(ctx, input, BulkDataImportProvider.PARAM_INPUT_TYPE, theResourceTypes.get(i));
			String nextUrl = theBaseUrl + "/download?index=" + theIndexes.get(i);
			ParametersUtil.addPartUrl(ctx, input, BulkDataImportProvider.PARAM_INPUT_URL, nextUrl);
		}

		return retVal;
	}

	private List<String> startServer(List<File> files) {
		List<String> indexes = new ArrayList<>();

		myServer = new Server();
		ServerConnector connector = new ServerConnector(myServer);
		connector.setIdleTimeout(DateUtils.MILLIS_PER_MINUTE);
		connector.setPort(myPort);
		myServer.setConnectors(new Connector[] {connector});

		myServlet = new BulkImportFileServlet();

		// The logback used by the CLI tool only allows loggers in this package
		// to emit log lines, and the servlet's logs are useful.
		myServlet.setLog(ourLog);

		for (File t : files) {
			indexes.add(myServlet.registerFile(t));
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
			final String[] extensions = new String[] {".json", ".ndjson", ".json.gz", ".ndjson.gz"};
			final IOFileFilter filter =
					FileFileFilter.INSTANCE.and(new SuffixFileFilter(extensions, IOCase.INSENSITIVE));
			PathUtils.walk(directory.toPath(), filter, 1, false, FileVisitOption.FOLLOW_LINKS)
					.map(Path::toFile)
					.filter(t -> t.isFile())
					.filter(t -> t.exists())
					.forEach(t -> files.add(t));
			if (files.isEmpty()) {
				throw new CommandFailureException(Msg.code(2058) + "No files found in directory \""
						+ directory.getAbsolutePath() + "\". Allowed extensions: " + Arrays.asList(extensions));
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
}
