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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.CodeSystem;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UploadTerminologyCommand extends BaseCommand {
	public static final String UPLOAD_TERMINOLOGY = "upload-terminology";
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
		addRequiredOption(options, "u", "url", true, "The code system URL associated with this upload (e.g. " + IHapiTerminologyLoaderSvc.SCT_URI + ")");
		addOptionalOption(options, "d", "data", true, "Local file to use to upload (can be a raw file or a ZIP containing the raw file)");
		addOptionalOption(options, null, "custom", false, "Indicates that this upload uses the HAPI FHIR custom external terminology format");
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
				uploadSnapshot(inputParameters, termUrl, datafile, theCommandLine, client);
				break;
			case ADD:
				uploadDelta(theCommandLine, termUrl, datafile, client, inputParameters, JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD, false);
				break;
			case REMOVE:
				uploadDelta(theCommandLine, termUrl, datafile, client, inputParameters, JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE, true);
				break;
		}

	}

	private void uploadDelta(CommandLine theCommandLine, String theTermUrl, String[] theDatafile, IGenericClient theClient, IBaseParameters theInputParameters, String theOperationName, boolean theFlatten) {
		ParametersUtil.addParameterToParametersUri(myFhirCtx, theInputParameters, "url", theTermUrl);

		List<IHapiTerminologyLoaderSvc.FileDescriptor> fileDescriptors = new ArrayList<>();

		for (String next : theDatafile) {
			try (FileInputStream inputStream = new FileInputStream(next)) {
				byte[] bytes = IOUtils.toByteArray(inputStream);
				fileDescriptors.add(new IHapiTerminologyLoaderSvc.FileDescriptor() {
					@Override
					public String getFilename() {
						return next;
					}

					@Override
					public InputStream getInputStream() {
						return new ByteArrayInputStream(bytes);
					}
				});
			} catch (IOException e) {
				throw new CommandFailureException("Failed to read from file \"" + next + "\": " + e.getMessage());
			}
		}

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		TerminologyLoaderSvcImpl.LoadedFileDescriptors descriptors = new TerminologyLoaderSvcImpl.LoadedFileDescriptors(fileDescriptors);
		TerminologyLoaderSvcImpl.processCustomTerminologyFiles(descriptors, codeSystemVersion);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(theTermUrl);
		addCodesToCodeSystem(codeSystemVersion.getConcepts(), codeSystem.getConcept(), theFlatten);

		ParametersUtil.addParameterToParameters(myFhirCtx, theInputParameters, "value", codeSystem);

		if (theCommandLine.hasOption("custom")) {
			ParametersUtil.addParameterToParametersCode(myFhirCtx, theInputParameters, "contentMode", "custom");
		}

		ourLog.info("Beginning upload - This may take a while...");

		IBaseParameters response = theClient
			.operation()
			.onType(myFhirCtx.getResourceDefinition("CodeSystem").getImplementingClass())
			.named(theOperationName)
			.withParameters(theInputParameters)
			.execute();

		ourLog.info("Upload complete!");
		ourLog.info("Response:\n{}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

	private void addCodesToCodeSystem(Collection<TermConcept> theSourceConcepts, List<CodeSystem.ConceptDefinitionComponent> theTargetConcept, boolean theFlatten) {
		for (TermConcept nextSourceConcept : theSourceConcepts) {

			CodeSystem.ConceptDefinitionComponent nextTarget = new CodeSystem.ConceptDefinitionComponent();
			nextTarget.setCode(nextSourceConcept.getCode());
			nextTarget.setDisplay(nextSourceConcept.getDisplay());
			theTargetConcept.add(nextTarget);

			List<TermConcept> children = nextSourceConcept.getChildren().stream().map(t -> t.getChild()).collect(Collectors.toList());
			if (theFlatten) {
				addCodesToCodeSystem(children, theTargetConcept, theFlatten);
			} else {
				addCodesToCodeSystem(children, nextTarget.getConcept(), theFlatten);
			}

		}
	}

	private void uploadSnapshot(IBaseParameters theInputparameters, String theTermUrl, String[] theDatafile, CommandLine theCommandLine, IGenericClient theClient) {
		ParametersUtil.addParameterToParametersUri(myFhirCtx, theInputparameters, "url", theTermUrl);
		for (String next : theDatafile) {
			ParametersUtil.addParameterToParametersString(myFhirCtx, theInputparameters, "localfile", next);
		}
		if (theCommandLine.hasOption("custom")) {
			ParametersUtil.addParameterToParametersCode(myFhirCtx, theInputparameters, "contentMode", "custom");
		}

		ourLog.info("Beginning upload - This may take a while...");

		IBaseParameters response = theClient
			.operation()
			.onType(myFhirCtx.getResourceDefinition("CodeSystem").getImplementingClass())
			.named(JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM)
			.withParameters(theInputparameters)
			.execute();

		ourLog.info("Upload complete!");
		ourLog.info("Response:\n{}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

	private enum ModeEnum {
		SNAPSHOT, ADD, REMOVE
	}

}
