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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.Parameters;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider.RESP_PARAM_SUCCESS;

public class ReindexTerminologyCommand extends BaseRequestGeneratingCommand {
	public static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReindexTerminologyCommand.class);

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
		ourLog.info("Beginning freetext indexing - This may take a while...");

		IBaseParameters response;
		// non-null errorMessage means failure
		String errorMessage = null;
		try {
			response = theClient
					.operation()
					.onServer()
					.named(REINDEX_TERMINOLOGY)
					.withNoParameters(Parameters.class)
					.execute();

		} catch (BaseServerResponseException e) {
			int statusCode = e.getStatusCode();
			errorMessage = e.getMessage();

			if (e.getOperationOutcome() != null) {
				errorMessage += " : " + e.getOperationOutcome().getFormatCommentsPre();
			}
			throw new CommandFailureException(
					Msg.code(2228) + "FAILURE: Received HTTP " + statusCode + ": " + errorMessage);
		}

		Optional<String> isSuccessResponse =
				ParametersUtil.getNamedParameterValueAsString(myFhirCtx, response, RESP_PARAM_SUCCESS);
		if (!isSuccessResponse.isPresent()) {
			errorMessage = "Internal error. Command result unknown. Check system logs for details.";
		} else {
			boolean succeeded = Boolean.parseBoolean(isSuccessResponse.get());
			if (!succeeded) {
				errorMessage = getResponseMessage(response);
			}
		}

		if (errorMessage != null) {
			throw new CommandFailureException(Msg.code(2229) + "FAILURE: " + errorMessage);
		} else {
			ourLog.info("Recreation of terminology freetext indexes complete!");
			ourLog.info("Response:{}{}", NL, getResponseMessage(response));
		}
	}

	@Nonnull
	private String getResponseMessage(IBaseParameters response) {
		List<String> message = ParametersUtil.getNamedParameterValuesAsString(myFhirCtx, response, "message");
		return String.join(NL, message);
	}

	public static final String NL = System.getProperty("line.separator");
}
