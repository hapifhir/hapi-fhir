/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.rest.gclient;

public class RawRequestEntity {
	private final String myContentType;
	private final byte[] myBytes;

	/**
	 * Constructor
	 *
	 * @param theContentType The "Content-Type" header value of the request (e.g. "application/json")
	 * @param theBytes       The bytes to use as the request body payload
	 * @since 8.12.0
	 */
	public RawRequestEntity(String theContentType, byte[] theBytes) {
		myContentType = theContentType;
		myBytes = theBytes;
	}

	public String getContentType() {
		return myContentType;
	}

	public byte[] getBytes() {
		return myBytes;
	}
}
