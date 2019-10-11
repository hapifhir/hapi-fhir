package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.base.Charsets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.ICompositeType;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UploadTerminologyCommand extends BaseCommand {
	static final String UPLOAD_TERMINOLOGY = "upload-terminology";
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);

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
		Options options = new Options();

		addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, "u", "url", true, "The code system URL associated with this upload (e.g. " + ITermLoaderSvc.SCT_URI + ")");
		addOptionalOption(options, "d", "data", true, "Local file to use to upload (can be a raw file or a ZIP containing the raw file)");
		addOptionalOption(options, "m", "mode", true, "The upload mode: SNAPSHOT (default), ADD, REMOVE");
		addBasicAuthOption(options);
		addVerboseLoggingOption(options);

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
			throw new ParseException("Invalid mode: " + modeString);
		}

		String termUrl = theCommandLine.getOptionValue("u");
		if (isBlank(termUrl)) {
			throw new ParseException("No URL provided");
		}

		String[] datafile = theCommandLine.getOptionValues("d");
		if (datafile == null || datafile.length == 0) {
			throw new ParseException("No data file provided");
		}

		IGenericClient client = super.newClient(theCommandLine);
		IBaseParameters inputParameters = ParametersUtil.newInstance(myFhirCtx);

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		switch (mode) {
			case SNAPSHOT:
				invokeOperation(theCommandLine, termUrl, datafile, client, inputParameters, JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM);
				break;
			case ADD:
				invokeOperation(theCommandLine, termUrl, datafile, client, inputParameters, JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD);
				break;
			case REMOVE:
				invokeOperation(theCommandLine, termUrl, datafile, client, inputParameters, JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE);
				break;
		}

	}

	private void invokeOperation(CommandLine theCommandLine, String theTermUrl, String[] theDatafile, IGenericClient theClient, IBaseParameters theInputParameters, String theOperationName) throws ParseException {
		ParametersUtil.addParameterToParametersUri(myFhirCtx, theInputParameters, TerminologyUploaderProvider.PARAM_SYSTEM, theTermUrl);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, Charsets.UTF_8);
		boolean haveCompressedContents = false;
		try {
			for (String nextDataFile : theDatafile) {

				try (FileInputStream fileInputStream = new FileInputStream(nextDataFile)) {
					if (!nextDataFile.endsWith(".zip")) {

						ourLog.info("Compressing and adding file: {}", nextDataFile);
						ZipEntry nextEntry = new ZipEntry(stripPath(nextDataFile));
						zipOutputStream.putNextEntry(nextEntry);

						IOUtils.copy(fileInputStream, zipOutputStream);
						haveCompressedContents = true;

						zipOutputStream.flush();
						ourLog.info("Finished compressing {} into {}", nextEntry.getSize(), nextEntry.getCompressedSize());

					} else {

						ourLog.info("Adding file: {}", nextDataFile);
						ICompositeType attachment = AttachmentUtil.newInstance(myFhirCtx);
						AttachmentUtil.setUrl(myFhirCtx, attachment, "file:" + nextDataFile);
						AttachmentUtil.setData(myFhirCtx, attachment, IOUtils.toByteArray(fileInputStream));
						ParametersUtil.addParameterToParameters(myFhirCtx, theInputParameters, TerminologyUploaderProvider.PARAM_FILE, attachment);

					}
				}

			}
			zipOutputStream.flush();
			zipOutputStream.close();
		} catch (IOException e) {
			throw new ParseException(e.toString());
		}

		if (haveCompressedContents) {
			ICompositeType attachment = AttachmentUtil.newInstance(myFhirCtx);
			AttachmentUtil.setUrl(myFhirCtx, attachment, "file:/files.zip");
			AttachmentUtil.setData(myFhirCtx, attachment, byteArrayOutputStream.toByteArray());
			ParametersUtil.addParameterToParameters(myFhirCtx, theInputParameters, TerminologyUploaderProvider.PARAM_FILE, attachment);
		}

		ourLog.info("Beginning upload - This may take a while...");

		if (ourLog.isDebugEnabled() || "true".equals(System.getProperty("test"))) {
		ourLog.info("Submitting parameters: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theInputParameters));
		}

		IBaseParameters response;
		try {
			response = theClient
				.operation()
				.onType(myFhirCtx.getResourceDefinition("CodeSystem").getImplementingClass())
				.named(theOperationName)
				.withParameters(theInputParameters)
				.execute();
		} catch (BaseServerResponseException e) {
			if (e.getOperationOutcome() != null) {
				ourLog.error("Received the following response:\n{}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			}
			throw e;
		}


		ourLog.info("Upload complete!");
		ourLog.info("Response:\n{}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

	private enum ModeEnum {
		SNAPSHOT, ADD, REMOVE
	}

	public static String stripPath(String thePath) {
		String retVal = thePath;
		if (retVal.contains("/")) {
			retVal = retVal.substring(retVal.lastIndexOf("/"));
		}
		return retVal;
	}

}
