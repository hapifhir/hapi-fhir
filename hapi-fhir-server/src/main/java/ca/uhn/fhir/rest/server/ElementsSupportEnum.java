package ca.uhn.fhir.rest.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

/**
 * @see <a href="http://hapifhir.io/doc_rest_server.html#extended_elements_support">Extended Elements Support</a>
 */
public enum ElementsSupportEnum {

	/**
	 * The server will support only the FHIR standard features for the <code>_elements</code>
	 * parameter.
	 *
	 * @see <a href="http://hl7.org/fhir/search.html#elements">http://hl7.org/fhir/search.html#elements</a>
	 */
	STANDARD,

	/**
	 * The server will support both the standard features as well as support for elements
	 * exclusion.
	 */
	EXTENDED

}
