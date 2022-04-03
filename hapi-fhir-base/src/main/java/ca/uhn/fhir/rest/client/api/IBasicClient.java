package ca.uhn.fhir.rest.client.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.hl7.fhir.instance.model.api.IBaseConformance;

import ca.uhn.fhir.rest.annotation.Metadata;

/**
 * Base interface for a client supporting the mandatory operations as defined by
 * the FHIR specification.
 */
public interface IBasicClient extends IRestfulClient {

	/**
	 * Returns the server conformance statement
	 * 
	 * See the <a href="http://hl7.org/implement/standards/fhir/http.html#conformance">FHIR HTTP Conformance</a> definition
	 * for more information.
	 */
	@Metadata
	IBaseConformance getServerConformanceStatement();

}
