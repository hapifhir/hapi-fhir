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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Observation;

// START SNIPPET: interceptor
/**
 * This interceptor demonstrates how to customize full-text indexing. It
 * implements a fairly contrived set of rules, intended to demonstrate
 * possibilities:
 * <ul>
 * <li>
 *     Observations with an <code>Observation.category</code> code of
 *     "vital-signs" will not be full-text indexed.
 * </li>
 * <li>
 *     Observations with an <code>Observation.value</code> type of
 *     "string" will only have the string value indexed, and no other
 *     strings in the resource will be indexed.
 * </li>
 * <li>
 *     All other resource types are indexed normally.
 * </li>
 * </ul>
 */
@Interceptor
public class FullTextSelectiveIndexingInterceptor {

	/**
	 * Override the default behaviour for the <code>_content</code>
	 * SearchParameter indexing.
	 */
	@Hook(Pointcut.JPA_INDEX_EXTRACT_FULLTEXT_CONTENT)
	public FullTextExtractionResponse indexPayload(FullTextExtractionRequest theRequest) {
		IBaseResource resource = theRequest.getResource();
		if (resource instanceof Observation observation) {

			// Do not full-text index vital signs
			if (isVitalSignsObservation(observation)) {
				return FullTextExtractionResponse.doNotIndex();
			}

			if (observation.hasValueStringType()) {
				String stringValue = observation.getValueStringType().getValue();
				return FullTextExtractionResponse.indexPayload(stringValue);
			}
		}

		return FullTextExtractionResponse.indexNormally();
	}

	/**
	 * Returns {@literal true} if the Observation has the vital-signs category
	 */
	private boolean isVitalSignsObservation(Observation theObservation) {
		for (var category : theObservation.getCategory()) {
			for (var coding : category.getCoding()) {
				if ("http://hl7.org/fhir/codesystem/Observation-category".equals(coding.getSystem())) {
					if ("vital-signs".equals(coding.getCode())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
// END SNIPPET: interceptor
