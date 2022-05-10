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

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.Parameters;

import java.util.Optional;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;

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

		Optional<String> isSuccessResponse = ParametersUtil.getNamedParameterValueAsString(myFhirCtx, response, RESP_PARAM_SUCCESS);
		if ( ! isSuccessResponse.isPresent() ) {
			ParametersUtil.addParameterToParametersBoolean(myFhirCtx, response, RESP_PARAM_SUCCESS, false);
			ParametersUtil.addParameterToParametersString(myFhirCtx, response, "message",
				"Internal error. Command result unknown. Check system logs for details");
			ourLog.error("Response:{}{}", NL, myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
			return;
		}

		boolean succeeded = Boolean.parseBoolean( isSuccessResponse.get() );
		if ( ! succeeded) {
			ourLog.info("Response:{}{}", NL, myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
			return;
		}

		ourLog.info("Recreation of terminology freetext indexes complete!");
		ourLog.info("Response:{}{}", NL, myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
	}


	public static final String NL = System.getProperty("line.separator");

}
