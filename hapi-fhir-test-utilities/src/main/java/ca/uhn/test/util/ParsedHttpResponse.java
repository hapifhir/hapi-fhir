/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.test.util;

import com.google.common.collect.Multimap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public record ParsedHttpResponse(int statusCode, String statusReason, Multimap<String, String> headers, String body) {

	@Override
	public String toString() {
		StringBuilder logBuilder = new StringBuilder();

		// Log Status Line
		logBuilder.append(statusCode())
			.append(" ").append(statusReason()).append("\n");

		// Log Headers
		for (Map.Entry<String, String> header : headers.entries()) {
			logBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
		}

		logBuilder.append("\n");

		if (isNotBlank(body)) {
			logBuilder.append(body);
		}

		return logBuilder.toString();
	}
}
