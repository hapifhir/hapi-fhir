package ca.uhn.fhir.rest.server.util;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * This interface isn't used by hapi-fhir-base, but is used by the 
 * <a href="https://hapifhir.io/hapi-fhir/docs/server_plain/web_testpage_overlay.html">Web Testing UI</a>
 */
public interface ITestingUiClientFactory {

	/**
	 * Instantiate a new client
	 */
	IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl);
	
}
