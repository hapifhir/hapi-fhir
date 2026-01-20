/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.List;

/**
 * Interface for processing special search parameters like "_count" and "_sort"
 * that are not stored as AND/OR lists in the SearchParameterMap.
 */
interface ISpecialParameterProcessor {
	static String paramAsQueryString(IQueryParameterType theParameter) {
		return theParameter.getValueAsQueryToken(null);
	}

	/**
	 * Apply this processor to the theValues and update the SearchParameterMap.
	 * @param theKey the key of the parameter being processed, e.g. "_sort"
	 * @param theValues the values of the parameter being processed, e.g. new TokenParam("-date")
	 * @param theSearchParameterMap the target to modify
	 */
	void process(String theKey, List<IQueryParameterType> theValues, SearchParameterMap theSearchParameterMap);
}
