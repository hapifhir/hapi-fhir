/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.rest.gclient;

/**
 * Interface for making raw HTTP requests using the GenericClient infrastructure.
 * This allows non-FHIR HTTP calls to benefit from the same interceptor chain,
 * authentication, and configuration as regular FHIR operations.
 *
 * <p>Particularly useful for operations like polling $export status endpoints
 * or calling external services that return non-FHIR content.</p>
 *
 * @since 8.6.0
 */
public interface IRawHttp {
	/**
	 * Creates a GET request to the specified URL.
	 *
	 * @param theUrl The URL to send the GET request to. Can be absolute or relative
	 *               to the client's base URL.
	 * @return A builder for configuring and executing the HTTP GET request
	 */
	IClientHttpExecutable<IClientHttpExecutable<?, IEntityResult>, IEntityResult> get(String theUrl);
}
