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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Response to a Raw HTTP call done through IGenericClient.
 */
public interface IEntityResult {

	String getMimeType();

	@NonNull
	/**
	 * Gets the response body as an InputStream.
	 *
	 * <p><strong>Warning:</strong> The returned stream can only be read once.
	 * Subsequent calls to this method will return the same exhausted stream.
	 * Consider copying the content to a byte array if multiple reads are needed.</p>
	 *
	 * @return The response body as an InputStream (single-use only)
	 */
	InputStream getInputStream();

	int getStatusCode();

	@NonNull
	Map<String, List<String>> getHeaders();
}
