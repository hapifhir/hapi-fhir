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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.instance.model.api.IBaseParameters;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UploadTerminologyCommand extends BaseCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadTerminologyCommand.class);
	private static final String UPLOAD_EXTERNAL_CODE_SYSTEM = "upload-external-code-system";

	@Override
	public String getCommandDescription() {
		return "Uploads a terminology package (e.g. a SNOMED CT ZIP file) to a server, using the $" + UPLOAD_EXTERNAL_CODE_SYSTEM + " operation.";
	}

	@Override
	public String getCommandName() {
		return "upload-terminology";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();

		addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, "u", "url", true, "The code system URL associated with this upload (e.g. " + IHapiTerminologyLoaderSvc.SCT_URI + ")");
		addOptionalOption(options, "d", "data", true, "Local file to use to upload (can be a raw file or a ZIP containing the raw file)");
		addBasicAuthOption(options);
		addVerboseLoggingOption(options);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);
		FhirContext ctx = getFhirContext();

		String termUrl = theCommandLine.getOptionValue("u");
		if (isBlank(termUrl)) {
			throw new ParseException("No URL provided");
		}

		String[] datafile = theCommandLine.getOptionValues("d");
		if (datafile == null || datafile.length == 0) {
			throw new ParseException("No data file provided");
		}

		IGenericClient client = super.newClient(theCommandLine);
		IBaseParameters inputParameters;
		if (ctx.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.Parameters p = new org.hl7.fhir.dstu3.model.Parameters();
			p.addParameter().setName("url").setValue(new org.hl7.fhir.dstu3.model.UriType(termUrl));
			for (String next : datafile) {
				p.addParameter().setName("localfile").setValue(new org.hl7.fhir.dstu3.model.StringType(next));
			}
			inputParameters = p;
		} else if (ctx.getVersion().getVersion() == FhirVersionEnum.R4) {
			org.hl7.fhir.r4.model.Parameters p = new org.hl7.fhir.r4.model.Parameters();
			p.addParameter().setName("url").setValue(new org.hl7.fhir.r4.model.UriType(termUrl));
			for (String next : datafile) {
				p.addParameter().setName("localfile").setValue(new org.hl7.fhir.r4.model.StringType(next));
			}
			inputParameters = p;
		} else {
			throw new ParseException("This command does not support FHIR version " + ctx.getVersion().getVersion());
		}

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		ourLog.info("Beginning upload - This may take a while...");
		IBaseParameters response = client
			.operation()
			.onServer()
			.named(UPLOAD_EXTERNAL_CODE_SYSTEM)
			.withParameters(inputParameters)
			.execute();

		ourLog.info("Upload complete!");
		ourLog.info("Response:\n{}", ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

}
