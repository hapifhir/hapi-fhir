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
package ca.uhn.test.util;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CloseableHttpResponseUtil {

	/**
	 * Non instantiable
	 */
	private CloseableHttpResponseUtil() {
		super();
	}

	/**
	 * Parses an HTTP response to make it easier to work with for tests
	 */
	public static ParsedHttpResponse parse(CloseableHttpResponse response) throws IOException {
		Multimap<String, String> headers = MultimapBuilder.hashKeys().arrayListValues().build();

		for (Header header : response.getAllHeaders()) {
			headers.put(header.getName(), header.getValue());
		}

		String body = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		return new ParsedHttpResponse(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), headers, body);
	}

}
