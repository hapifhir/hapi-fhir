/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.IEntityResult;
import ca.uhn.fhir.rest.gclient.RawRequestEntity;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.springframework.util.unit.DataSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.SCT_URI;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_APPEND_TO_JOB_ATTACHMENT_ID;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_FILENAME;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_JOB_ATTACHMENT_ID;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID;
import static ca.uhn.fhir.util.FileUtil.formatFileSize;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UploadTerminologyCommand extends BaseRequestGeneratingCommand {
	static final String UPLOAD_TERMINOLOGY = "upload-terminology";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);
	private static final long DEFAULT_TRANSFER_SIZE_LIMIT = 10 * FileUtils.ONE_MB;
	private long myTransferSizeLimit = DEFAULT_TRANSFER_SIZE_LIMIT;

	@Override
	public String getCommandDescription() {
		return "Uploads a terminology package (e.g. a SNOMED CT ZIP file or a custom terminology bundle) to a server, using the appropriate operation.";
	}

	@Override
	public String getCommandName() {
		return UPLOAD_TERMINOLOGY;
	}

	@Override
	public Options getOptions() {
		Options options = super.getOptions();

		addRequiredOption(
				options,
				"u",
				"url",
				true,
				"The code system URL associated with this upload (e.g. \"" + SCT_URI + "|20260501\")");
		addOptionalOption(
				options,
				"d",
				"data",
				true,
				"Local file to use to upload (can be a raw file or a ZIP containing the raw file)");
		addOptionalOption(options, "m", "mode", true, "The upload mode: SNAPSHOT (default), ADD, REMOVE");
		addOptionalOption(
				options,
				"s",
				"size",
				true,
				"The maximum size of a single upload (default: 10MB). If a file to upload exceeds this size, it will be split into smaller chunks before sending over the network. Examples: 150KB, 3MB, 1GB.");
		addOptionalOption(
				options,
				null,
				"dont-make-current",
				false,
				"If specified, the terminology version being uploaded will not be marked as the current version. This option can only be specified on certain CodeSystem URLs.");

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

		ImportTerminologyModeEnum mode;
		String modeString = theCommandLine.getOptionValue("m", "SNAPSHOT");
		try {
			mode = ImportTerminologyModeEnum.valueOf(modeString);
		} catch (IllegalArgumentException e) {
			throw new ParseException(Msg.code(1538) + "Invalid mode: " + modeString);
		}

		String termUrl = theCommandLine.getOptionValue("u");
		if (isBlank(termUrl)) {
			throw new ParseException(Msg.code(1539) + "No URL provided");
		}

		String[] datafile = theCommandLine.getOptionValues("d");
		if (datafile == null || datafile.length == 0) {
			throw new ParseException(Msg.code(1540) + "No data file provided");
		}

		String sizeString = theCommandLine.getOptionValue("s");
		this.setTransferSizeLimitHuman(sizeString);

		boolean dontMakeCurrent = theCommandLine.hasOption("dont-make-current");

		IGenericClient client = newClient(theCommandLine);

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		invokeOperationAsyncJob(termUrl, datafile, client, dontMakeCurrent, mode);
	}

	private void invokeOperationAsyncJob(
			String theUrl,
			String[] theDatafiles,
			IGenericClient theClient,
			boolean theDontMakeCurrent,
			ImportTerminologyModeEnum theMode) {
		ourLog.info("Beginning upload process for terminology system: {}", theUrl);

		// Step 1: Create staging job
		String jobInstanceId = createJob(theUrl, theClient, theDontMakeCurrent, theMode);

		// Step 2: Attach Files
		for (String datafile : theDatafiles) {
			attachFileToJob(theClient, datafile, jobInstanceId);
		}

		// Step 3 - Start job
		String pollUrl = startJob(theClient, jobInstanceId);

		// Step 4 - Poll for progress
		pollForJobStatusUntilComplete(theClient, pollUrl);
	}

	/**
	 * Step 1: Create a new terminology upload job but don't start it yet
	 *
	 * @return The job instance ID
	 */
	@Nonnull
	private String createJob(
			String theUrl, IGenericClient theClient, boolean theDontMakeCurrent, ImportTerminologyModeEnum theMode) {
		ourLog.info("Requesting server to create staging job for terminology system");
		IBaseParameters createStagingRequest = ParametersUtil.newInstance(myFhirCtx);
		ParametersUtil.addParameterToParametersUri(
				myFhirCtx, createStagingRequest, TerminologyUploaderProvider.PARAM_SYSTEM, theUrl);

		if (theDontMakeCurrent) {
			ParametersUtil.addParameterToParametersBoolean(
					myFhirCtx, createStagingRequest, TerminologyUploaderProvider.PARAM_MAKE_CURRENT, false);
		}

		ParametersUtil.addParameterToParametersCode(
				myFhirCtx, createStagingRequest, TerminologyUploaderProvider.PARAM_MODE, theMode.name());

		IBaseParameters createStagingResponse;
		try {
			createStagingResponse = theClient
					.operation()
					.onType("CodeSystem")
					.named(JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
					.withParameters(createStagingRequest)
					.execute();
		} catch (BaseServerResponseException e) {
			throw new CommandFailureException(
					Msg.code(2934) + "Failed to create terminology staging job: " + e.getMessage());
		}
		String outcome = ParametersUtil.getNamedParameterValueAsString(
						myFhirCtx, createStagingResponse, TerminologyUploaderProvider.RESP_PARAM_OUTCOME)
				.orElseThrow();
		ourLog.info("Server responded: {}", outcome);
		String jobInstanceId = ParametersUtil.getNamedParameterValueAsString(
						myFhirCtx, createStagingResponse, PARAM_JOB_INSTANCE_ID)
				.orElseThrow();
		return jobInstanceId;
	}

	/**
	 * Step 2: Attach Files
	 */
	private void attachFileToJob(IGenericClient theClient, String theFilename, String theJobInstanceId) {
		File dataFile = new File(theFilename);

		if (!dataFile.exists() || !dataFile.isFile() || !dataFile.canRead()) {
			throw new CommandFailureException(Msg.code(2935) + "File does not exist or can't be read: " + theFilename);
		}

		long size = FileUtils.sizeOf(dataFile);
		ourLog.info("Attaching file ({}) to job: {}", formatFileSize(size), theFilename);
		StopWatch sw = new StopWatch();

		String attachFileUrlBase = "CodeSystem/" + JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE
				+ "?"
				+ PARAM_JOB_INSTANCE_ID
				+ "="
				+ theJobInstanceId;

		try (FileInputStream fileInputStream = new FileInputStream(dataFile)) {

			String attachmentId = null;

			/*
			 * We will loop through chunks of the file contents without ever loading the entire file into memory.
			 * Each chunk is uploaded to the server in a separate request. The first chunk creates a new
			 * job attachment, and subsequent chunks append to the same attachment.
			 */
			int attachmentChunkIndexForLogs = 1;
			while (true) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte[] bytes;
				try {
					IOUtils.copyLarge(fileInputStream, buffer, 0, myTransferSizeLimit);
					bytes = buffer.toByteArray();
				} catch (IOException e) {
					throw new CommandFailureException(
							Msg.code(2936) + "Failed to read file '" + theFilename + "': " + e.getMessage());
				}

				if (bytes.length == 0) {
					break;
				}

				String url;
				if (attachmentId == null) {
					url = attachFileUrlBase + "&" + PARAM_FILENAME + "=" + UrlUtil.escapeUrlParam(dataFile.getName());
				} else {
					ourLog.info(" * Uploading chunk {} of file {}", attachmentChunkIndexForLogs, dataFile.getName());
					url = attachFileUrlBase + "&" + PARAM_APPEND_TO_JOB_ATTACHMENT_ID + "="
							+ UrlUtil.escapeUrlParam(attachmentId);
				}

				RawRequestEntity requestEntity = new RawRequestEntity(Constants.CT_OCTET_STREAM, bytes);
				IEntityResult response;
				try {
					StopWatch requestStopwatch = new StopWatch();

					response =
							theClient.rawHttpRequest().post(url, requestEntity).execute();

					ourLog.info(
							" * Uploaded and stored {} in {} ({}/sec)",
							formatFileSize(bytes.length),
							requestStopwatch,
							formatFileSize((long) sw.getThroughput(bytes.length, TimeUnit.SECONDS)));

				} catch (InvalidRequestException e) {
					throw new CommandFailureException(Msg.code(2959) + "Failed to attach file \"" + dataFile.getName()
							+ "\" to job, got " + e.getMessage());
				}
				if (response.getStatusCode() != 200) {
					throw new CommandFailureException(Msg.code(2937) + "Failed to attach file \"" + dataFile.getName()
							+ "\" to job, got HTTP " + response.getStatusCode());
				}

				if (attachmentId == null) {
					EncodingEnum responseEncoding = EncodingEnum.forContentTypeStrict(response.getMimeType());
					Validate.notNull(
							responseEncoding,
							"Failed to determine encoding for response from content-type: %s",
							response.getMimeType());
					String responseBody = IOUtils.toString(response.getInputStream(), StandardCharsets.UTF_8);
					IBaseParameters parameters = (IBaseParameters)
							responseEncoding.newParser(myFhirCtx).parseResource(responseBody);
					attachmentId = ParametersUtil.getNamedParameterValueAsString(
									myFhirCtx, parameters, PARAM_JOB_ATTACHMENT_ID)
							.orElseThrow(() -> new CommandFailureException(
									Msg.code(2981) + "Response Parameters from server didn't include parameter "
											+ PARAM_JOB_ATTACHMENT_ID));
				}

				attachmentChunkIndexForLogs++;
			}

		} catch (IOException e) {
			throw new CommandFailureException(
					Msg.code(2982) + "Failed to read file '" + theFilename + "': " + e.getMessage());
		}

		ourLog.info("Attached file in {}", sw);
	}

	/**
	 * Step 3 - Start job
	 */
	@Nonnull
	private String startJob(IGenericClient theClient, String theJobInstanceId) {
		ourLog.info("Starting staged upload job");
		IBaseParameters startRequest = ParametersUtil.newInstance(myFhirCtx);
		ParametersUtil.addParameterToParametersCode(
				myFhirCtx, startRequest, TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID, theJobInstanceId);
		MethodOutcome startResponse = theClient
				.operation()
				.onType("CodeSystem")
				.named(JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB)
				.withParameters(startRequest)
				.returnMethodOutcome()
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
				.execute();

		String pollUrl = startResponse
				.getFirstResponseHeader(Constants.HEADER_CONTENT_LOCATION)
				.orElseThrow();
		ourLog.info("Job is started. Server responded with poll URL: {}", pollUrl);
		return pollUrl;
	}

	/**
	 * Step 4 - Poll for progress
	 */
	private void pollForJobStatusUntilComplete(IGenericClient theClient, String thePollingUrl) {
		IEntityResult pollResponse;
		while (true) {
			ourLog.info("Polling for job status...");
			pollResponse = theClient.rawHttpRequest().get(thePollingUrl).execute();
			if (pollResponse.getStatusCode() != SC_ACCEPTED) {
				break;
			}

			EncodingEnum encoding = EncodingEnum.forContentType(pollResponse.getMimeType());
			Validate.notNull(encoding, "Unknown encoding: %s", pollResponse.getMimeType());
			IBaseOperationOutcome oo =
					(IBaseOperationOutcome) encoding.newParser(myFhirCtx).parseResource(pollResponse.getInputStream());
			String diagnostics = OperationOutcomeUtil.getFirstIssueDiagnostics(myFhirCtx, oo);
			ourLog.info(" - Server response: {}", diagnostics);

			// Sleep
			Duration pollFrequency = Duration.ofSeconds(10);
			if (HapiSystemProperties.isTestModeEnabled()) {
				pollFrequency = Duration.ofMillis(100);
			}
			ThreadUtils.sleepQuietly(pollFrequency);
		}

		ourLog.info("Job completed with status code: {}", pollResponse.getStatusCode());

		EncodingEnum encoding = EncodingEnum.forContentType(pollResponse.getMimeType());
		Validate.notNull(encoding, "Unknown encoding: %s", pollResponse.getMimeType());
		IBaseBundle bundle = (IBaseBundle) encoding.newParser(myFhirCtx).parseResource(pollResponse.getInputStream());
		String report = myFhirCtx
				.newTerser()
				.getSinglePrimitiveValue(bundle, "Bundle.entry.response.outcome.issue.diagnostics")
				.orElse(null);
		ourLog.info("Job completed with report:\n{}", report);
	}

	public void setTransferSizeBytes(long theTransferSizeBytes) {
		if (myTransferSizeLimit < 0) {
			myTransferSizeLimit = DEFAULT_TRANSFER_SIZE_LIMIT;
		} else {
			myTransferSizeLimit = theTransferSizeBytes;
		}
	}

	public void setTransferSizeLimitHuman(String theSize) {
		String size = theSize;
		if (isBlank(size)) {
			setTransferSizeBytes(DEFAULT_TRANSFER_SIZE_LIMIT);
		} else {
			if (size.matches("[0-9]+\\s*[a-zA-Z]+")) {
				size = size.replace(" ", "").toUpperCase(Locale.US);
			}
			long bytes = DataSize.parse(size).toBytes();
			if (bytes < 0) {
				bytes = DEFAULT_TRANSFER_SIZE_LIMIT;
			}
			setTransferSizeBytes(bytes);
		}
	}

	public long getTransferSizeLimit() {
		return myTransferSizeLimit;
	}

	static String stripPath(String thePath) {
		String retVal = thePath;
		if (retVal.contains("/")) {
			retVal = retVal.substring(retVal.lastIndexOf("/"));
		}
		return retVal;
	}

	@VisibleForTesting
	void setFhirContext(FhirContext theFhirContext) {
		myFhirCtx = theFhirContext;
	}
}
