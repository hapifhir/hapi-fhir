/*-
 * #%L
 * HAPI FHIR - Docs
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
package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionRequest;
import ca.uhn.fhir.jpa.searchparam.fulltext.FullTextExtractionResponse;

import java.util.Set;

// FIXME: finish and document
// START SNIPPET: interceptor
@Interceptor
public class FullTextSelectiveIndexingInterceptor {

	/**
	 * These types will be indexed for structured data content (supporting
	 * the <code>_content</code> Search Parameter)
	 */
	private static final Set<String> INDEX_CONTENT_TYPES = Set.of("Patient");

	/**
	 * These types will be indexed for narrative (supporting
	 * the <code>_text</code> Search Parameter)
	 */
	private static final Set<String> INDEX_TEXT_TYPES = Set.of("DiagnosticReport", "Observation");

	/**
	 * This method is called twice for each resource being stored, to cover
	 * the two FullText search parameters.
	 */
	@Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT)
	public FullTextExtractionResponse indexPayload(FullTextExtractionRequest theRequest) {

		if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.CONTENT) {
			if (!INDEX_CONTENT_TYPES.contains(theRequest.getResourceType())) {
				return FullTextExtractionResponse.doNotIndex();
			}
		}

		if (theRequest.getIndexType() == FullTextExtractionRequest.IndexTypeEnum.TEXT) {
			if (!INDEX_TEXT_TYPES.contains(theRequest.getResourceType())) {
				return FullTextExtractionResponse.doNotIndex();
			}
		}

		return FullTextExtractionResponse.indexNormally();
	}
}
// END SNIPPET: interceptor
