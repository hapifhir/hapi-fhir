/*-
 * #%L
 * HAPI FHIR - Docs
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
package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
public class ServerETagExamples {

	// START SNIPPET: disablingETags
	@WebServlet(
			urlPatterns = {"/fhir/*"},
			displayName = "FHIR Server")
	public class RestfulServerWithLogging extends RestfulServer {

		@Override
		protected void initialize() throws ServletException {
			// ... define your resource providers here ...

			// ETag support is enabled by default
			setETagSupport(ETagSupportEnum.ENABLED);
		}
	}
	// END SNIPPET: disablingETags

}
