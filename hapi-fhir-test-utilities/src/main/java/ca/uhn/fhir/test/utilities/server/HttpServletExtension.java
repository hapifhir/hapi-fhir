/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.test.utilities.HttpTestRequest;
import jakarta.servlet.http.HttpServlet;
import org.apache.commons.lang3.Validate;

public class HttpServletExtension extends BaseJettyServerExtension<HttpServletExtension> {
	private HttpServlet myServlet;

	public HttpServletExtension withServlet(HttpServlet theServlet) {
		myServlet = theServlet;
		return this;
	}

	/**
	 * Starts building a request against this server's base URL, using this server's
	 * {@link #getHttpClient()}. This is the non-FHIR counterpart to
	 * {@link ca.uhn.fhir.test.utilities.server.RestfulServerExtension#fhirRequest(String)}: the
	 * servlet under test here is an arbitrary one, so there is no {@link
	 * ca.uhn.fhir.context.FhirContext} and no way to send a resource body. Callers that need one
	 * can build the request themselves with
	 * {@link HttpTestRequest#to(org.apache.http.impl.client.CloseableHttpClient,
	 * ca.uhn.fhir.context.FhirContext, String)}.
	 *
	 * @param thePath the path below the server base URL, beginning with a slash
	 */
	// Created by claude-opus-5
	public HttpTestRequest request(String thePath) {
		return HttpTestRequest.to(getHttpClient(), getBaseUrl() + thePath);
	}

	@Override
	protected HttpServlet provideServlet() {
		Validate.notNull(myServlet);
		return myServlet;
	}
}
