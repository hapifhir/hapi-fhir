/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.springframework.util.unit.DataSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UploadTerminologyCommand extends BaseRequestGeneratingCommand {
	static final String UPLOAD_TERMINOLOGY = "upload-terminology";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);
	private static final long DEFAULT_TRANSFER_SIZE_LIMIT = 10 * FileUtils.ONE_MB;
	private long ourTransferSizeLimit = DEFAULT_TRANSFER_SIZE_LIMIT;

	public long getTransferSizeLimit() {
		return ourTransferSizeLimit;
	}

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
				"The code system URL associated with this upload (e.g. " + ITermLoaderSvc.SCT_URI + ")");
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
				"The maximum size of a single upload (default: 10MB). Examples: 150 kb, 3 mb, 1GB");

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

		ModeEnum mode;
		String modeString = theCommandLine.getOptionValue("m", "SNAPSHOT");
		try {
			mode = ModeEnum.valueOf(modeString);
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

		IGenericClient client = newClient(theCommandLine);

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		String requestName = null;
		switch (mode) {
			case SNAPSHOT:
				requestName = JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM;
				break;
			case ADD:
				requestName = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD;
				break;
			case REMOVE:
				requestName = JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE;
				break;
		}
		invokeOperation(termUrl, datafile, client, requestName);
	}

	private void invokeOperation(
			String theTermUrl, String[] theDatafile, IGenericClient theClient, String theOperationName)
			throws ParseException {
		IBaseParameters inputParameters = ParametersUtil.newInstance(myFhirCtx);

		boolean isDeltaOperation = theOperationName.equals(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				|| theOperationName.equals(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE);

		ParametersUtil.addParameterToParametersUri(
				myFhirCtx, inputParameters, TerminologyUploaderProvider.PARAM_SYSTEM, theTermUrl);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, Charsets.UTF_8);
		int compressedSourceBytesCount = 0;
		int compressedFileCount = 0;
		boolean haveCompressedContents = false;
		try {
			for (String nextDataFile : theDatafile) {
				File dataFile = new File(nextDataFile);
				ourLog.info("Reading {}", dataFile.getAbsolutePath());

				try (FileInputStream fileInputStream = new FileInputStream(dataFile)) {
					boolean isFhirType = nextDataFile.endsWith(".json") || nextDataFile.endsWith(".xml");
					if (nextDataFile.endsWith(".csv") || nextDataFile.endsWith(".properties") || isFhirType) {

						if (isDeltaOperation && isFhirType) {

							ourLog.info("Adding CodeSystem resource file: {}", nextDataFile);

							String contents = IOUtils.toString(fileInputStream, Charsets.UTF_8);
							EncodingEnum encoding = EncodingEnum.detectEncodingNoDefault(contents);
							if (encoding == null) {
								throw new ParseException(
										Msg.code(1541) + "Could not detect FHIR encoding for file: " + nextDataFile);
							}

							IBaseResource resource =
									encoding.newParser(myFhirCtx).parseResource(contents);
							ParametersUtil.addParameterToParameters(
									myFhirCtx, inputParameters, TerminologyUploaderProvider.PARAM_CODESYSTEM, resource);

						} else {

							ourLog.info("Compressing and adding file: {}", nextDataFile);
							ZipEntry nextEntry = new ZipEntry(stripPath(nextDataFile));
							zipOutputStream.putNextEntry(nextEntry);

							CountingInputStream countingInputStream = new CountingInputStream(fileInputStream);
							IOUtils.copy(countingInputStream, zipOutputStream);
							haveCompressedContents = true;
							compressedSourceBytesCount += countingInputStream.getCount();
							++compressedFileCount;

							zipOutputStream.flush();
							ourLog.info("Finished compressing {}", nextDataFile);
						}

					} else if (nextDataFile.endsWith(".zip")) {

						ourLog.info("Adding ZIP file: {}", nextDataFile);
						String fileName = "file:" + nextDataFile;
						addFileToRequestBundle(inputParameters, fileName, IOUtils.toByteArray(fileInputStream));

					} else {

						throw new ParseException(Msg.code(1542) + "Don't know how to handle file: " + nextDataFile);
					}
				}
			}
			zipOutputStream.flush();
			zipOutputStream.close();
		} catch (IOException e) {
			throw new ParseException(Msg.code(1543) + e.toString());
		}

		if (haveCompressedContents) {
			byte[] compressedBytes = byteArrayOutputStream.toByteArray();
			ourLog.info(
					"Compressed {} bytes in {} file(s) into {} bytes",
					FileUtil.formatFileSize(compressedSourceBytesCount),
					compressedFileCount,
					FileUtil.formatFileSize(compressedBytes.length));

			addFileToRequestBundle(inputParameters, "file:/files.zip", compressedBytes);
		}

		ourLog.info("Beginning upload - This may take a while...");

		if (ourLog.isDebugEnabled() || HapiSystemProperties.isTestModeEnabled()) {
			ourLog.debug(
					"Submitting parameters: {}",
					myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inputParameters));
		}

		IBaseParameters response;
		try {
			response = theClient
					.operation()
					.onType(myFhirCtx.getResourceDefinition("CodeSystem").getImplementingClass())
					.named(theOperationName)
					.withParameters(inputParameters)
					.execute();
		} catch (BaseServerResponseException e) {
			if (e.getOperationOutcome() != null) {
				ourLog.error(
						"Received the following response:\n{}",
						myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			}
			throw e;
		}

		ourLog.info("Upload complete!");
		ourLog.debug(
				"Response:\n{}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

	protected void addFileToRequestBundle(IBaseParameters theInputParameters, String theFileName, byte[] theBytes) {

		byte[] bytes = theBytes;
		String fileName = theFileName;
		String suffix = FilenameUtils.getExtension(fileName);

		if (bytes.length > ourTransferSizeLimit) {
			ourLog.info(
					"File size is greater than {} - Going to use a local file reference instead of a direct HTTP transfer. Note that this will only work when executing this command on the same server as the FHIR server itself.",
					FileUtil.formatFileSize(ourTransferSizeLimit));

			try {
				File tempFile = File.createTempFile("hapi-fhir-cli", "." + suffix);
				tempFile.deleteOnExit();
				try (OutputStream fileOutputStream = new FileOutputStream(tempFile, false)) {
					fileOutputStream.write(bytes);
					bytes = null;
					fileName = "localfile:" + tempFile.getAbsolutePath();
				}
			} catch (IOException e) {
				throw new CommandFailureException(Msg.code(1544) + e);
			}
		}

		ICompositeType attachment = AttachmentUtil.newInstance(myFhirCtx);
		AttachmentUtil.setContentType(myFhirCtx, attachment, getContentType(suffix));
		AttachmentUtil.setUrl(myFhirCtx, attachment, fileName);
		if (bytes != null) {
			AttachmentUtil.setData(myFhirCtx, attachment, bytes);
		}
		ParametersUtil.addParameterToParameters(
				myFhirCtx, theInputParameters, TerminologyUploaderProvider.PARAM_FILE, attachment);
	}

	/*
	 * Files may be included in the attachment as raw CSV/JSON/XML files, or may also be combined into a compressed ZIP file.
	 * Content Type reference: https://smilecdr.com/docs/terminology/uploading.html#delta-add-operation
	 */
	private String getContentType(String theSuffix) {
		String retVal = "";
		if (StringUtils.isNotBlank(theSuffix)) {
			switch (theSuffix.toLowerCase()) {
				case "csv":
					retVal = "text/csv";
					break;
				case "xml":
					retVal = "application/xml";
					break;
				case "json":
					retVal = "application/json";
					break;
				case "zip":
					retVal = "application/zip";
					break;
				default:
					retVal = "text/plain";
			}
		}
		ourLog.debug(
				"File suffix given was {} and contentType is {}, defaulting to content type text/plain",
				theSuffix,
				retVal);
		return retVal;
	}

	private enum ModeEnum {
		SNAPSHOT,
		ADD,
		REMOVE
	}

	public void setTransferSizeBytes(long theTransferSizeBytes) {
		if (ourTransferSizeLimit < 0) {
			ourTransferSizeLimit = DEFAULT_TRANSFER_SIZE_LIMIT;
		} else {
			ourTransferSizeLimit = theTransferSizeBytes;
		}
	}

	public void setTransferSizeLimitHuman(String sizeString) {
		if (isBlank(sizeString)) {
			setTransferSizeBytes(DEFAULT_TRANSFER_SIZE_LIMIT);
		} else {
			long bytes = DataSize.parse(sizeString).toBytes();
			if (bytes < 0) {
				bytes = DEFAULT_TRANSFER_SIZE_LIMIT;
			}
			setTransferSizeBytes(bytes);
		}
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
