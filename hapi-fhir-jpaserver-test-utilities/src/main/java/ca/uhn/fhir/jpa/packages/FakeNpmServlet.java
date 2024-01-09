/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.rest.api.Constants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FakeNpmServlet extends HttpServlet {
	private static final Logger ourLog = LoggerFactory.getLogger(FakeNpmServlet.class);

	final Map<String, byte[]> responses = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String requestUrl = req.getRequestURI();
		if (responses.containsKey(requestUrl)) {
			ourLog.info("Responding to request: {}", requestUrl);

			resp.setStatus(200);

			if (StringUtils.countMatches(requestUrl, "/") == 1) {
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_JSON);
			} else {
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, "application/gzip");
			}
			resp.getOutputStream().write(responses.get(requestUrl));
			resp.getOutputStream().close();
		} else {
			ourLog.warn("Unknown request: {}", requestUrl);

			resp.sendError(404);
		}
	}

	public Map<String, byte[]> getResponses() {
		return responses;
	}
}
