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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class LoadingValidationSupportDstu3 implements IValidationSupport {

	private FhirContext myCtx = FhirContext.forDstu3();

	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoadingValidationSupportDstu3.class);

	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		String resName = myCtx.getResourceType(theClass);
		ourLog.info("Attempting to fetch {} at URL: {}", resName, theUri);
		
		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com");
		
		T result;
		try {
			result = client.read(theClass, theUri);
		} catch (BaseServerResponseException e) {
			throw new CommandFailureException(Msg.code(1565) + "FAILURE: Received HTTP " + e.getStatusCode() + ": " + e.getMessage());
		}
		ourLog.info("Successfully loaded resource");
		return result;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

}
