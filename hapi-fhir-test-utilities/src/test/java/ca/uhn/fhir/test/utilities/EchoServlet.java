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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.rest.api.Constants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Writes back a {@literal text/plain} rendering of the request it received, one
 * {@literal name=value} per line, so a test can assert on what actually went over the wire with a
 * substring check.
 * <p>
 * Four query parameters shape the response instead of echoing: {@code ?redirect=true} returns a
 * {@literal 302} back to the same path without the parameter, {@code ?status=NNN} sets the status
 * code, {@code ?binary=true} returns {@link #PNG_MAGIC} as {@literal image/png}, and
 * {@code ?delayMillis=NNN} stalls before responding so a read timeout can be exercised.
 * </p>
 * <p>
 * Shared by every test in this package that needs a server to talk to, so that a field added here
 * is visible to all of them rather than to whichever copy happened to be edited.
 * </p>
 */
// Created by claude-opus-5
class EchoServlet extends HttpServlet {

	/**
	 * The eight-byte PNG signature — enough to prove a binary payload survived without a fixture file.
	 */
	static final byte[] PNG_MAGIC = new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};

	@Override
	protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
		if (theRequest.getParameter("redirect") != null) {
			theResponse.setStatus(302);
			// The request URL without the query string, so following the redirect lands on the echo.
			theResponse.addHeader("Location", theRequest.getRequestURL().toString());
			return;
		}

		String delayParameter = theRequest.getParameter("delayMillis");
		if (delayParameter != null) {
			try {
				Thread.sleep(Long.parseLong(delayParameter));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}

		String statusParameter = theRequest.getParameter("status");
		int status = statusParameter != null ? Integer.parseInt(statusParameter) : 200;
		theResponse.setStatus(status);
		theResponse.addHeader("X-Echo-Header", "echo-value");
		if (status == 204) {
			return;
		}

		if (theRequest.getParameter("binary") != null) {
			theResponse.setContentType("image/png");
			theResponse.getOutputStream().write(PNG_MAGIC);
			return;
		}

		String requestBody = IOUtils.toString(theRequest.getInputStream(), StandardCharsets.UTF_8);
		String parameters = renderParameters(theRequest);
		theResponse.setContentType("text/plain");
		theResponse
				.getWriter()
				.write("method=" + theRequest.getMethod() + "\nauthorization="
						+ theRequest.getHeader(Constants.HEADER_AUTHORIZATION) + "\ncontentType="
						+ stripCharset(theRequest.getContentType()) + "\nrawContentType="
						+ theRequest.getContentType() + "\ncustom="
						+ theRequest.getHeader("X-Custom") + "\nprefer="
						+ theRequest.getHeader(Constants.HEADER_PREFER) + "\nacceptEncoding="
						+ theRequest.getHeader("Accept-Encoding") + "\nparams=" + parameters + "\nbody="
						+ requestBody);
	}

	/**
	 * Renders the parsed parameters as {@literal name=value} joined by {@literal &}, sorted by name
	 * so an assertion does not depend on map iteration order, with a multi-valued name's values
	 * joined by a comma.
	 * <p>
	 * This is the only view of a {@literal application/x-www-form-urlencoded} body: reading a
	 * parameter consumes the input stream, so {@code body=} comes back empty for a form POST.
	 * </p>
	 */
	private String renderParameters(HttpServletRequest theRequest) {
		return theRequest.getParameterMap().entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.map(param -> param.getKey() + "=" + String.join(",", param.getValue()))
				.collect(Collectors.joining("&"));
	}

	private String stripCharset(String theContentType) {
		return theContentType == null ? null : theContentType.replaceAll(";.*", "").trim();
	}
}
