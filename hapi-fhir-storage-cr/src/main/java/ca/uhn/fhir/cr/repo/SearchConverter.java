package ca.uhn.fhir.cr.repo;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.List;
import java.util.Map;

/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested lists.
 * This class (will eventually) convert between them
 */
class SearchConverter {
	static SearchParameterMap convert(Map<String, List<IQueryParameterType>> theSearchMap) {
		var converted = new SearchParameterMap();
		if (theSearchMap == null) {
			return  converted;
		}

		// TODO: This logic is known to be bad. Just prototyping some stuff...
		for (var entry : theSearchMap.entrySet()) {
			for(var value : entry.getValue()) {
				converted.add(entry.getKey(), value);
			}
		}

		return converted;
	}
}
