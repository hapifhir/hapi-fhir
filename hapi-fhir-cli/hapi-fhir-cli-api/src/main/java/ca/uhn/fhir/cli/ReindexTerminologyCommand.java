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
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.hl7.fhir.dstu3.model.codesystems.SystemVersionProcessingModeEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.r4.model.Parameters;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReindexTerminologyCommand extends BaseRequestGeneratingCommand {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReindexTerminologyCommand.class);

	static final String REINDEX_TERMINOLOGY = "reindex-terminology";


	@Override
	public String getCommandDescription() {
		return "Recreates freetext-indexes for terminology data.";
	}

	@Override
	public String getCommandName() {
		return REINDEX_TERMINOLOGY;
	}


	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);

		IGenericClient client = newClient(theCommandLine);

		if (theCommandLine.hasOption(VERBOSE_LOGGING_PARAM)) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		invokeOperation(client);
	}


	private void invokeOperation(IGenericClient theClient) {
		IBaseParameters inputParameters = ParametersUtil.newInstance(myFhirCtx);

		ourLog.info("Beginning freetext indexing - This may take a while...");

		IBaseParameters response;
		try {
			response = theClient
				.operation()
				.onServer()
				.named(REINDEX_TERMINOLOGY)
				.withNoParameters(Parameters.class)
				.execute();

		} catch (BaseServerResponseException e) {
			if (e.getOperationOutcome() != null) {
				ourLog.error("Received the following response: {}{}", NL,
					myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			}
			throw e;
		}

		ourLog.info("Recreation of terminology freetext indexes complete!");
		ourLog.info("Response:\n{}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}

	public static final String NL = System.getProperty("line.separator");

}
