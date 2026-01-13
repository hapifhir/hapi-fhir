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
import ca.uhn.fhir.rest.api.SortSpec;

import java.util.List;
import java.util.function.Consumer;

/**
 * Map entry converter for sort
 */
class SortProcessor implements ISpecialParameterProcessor {
	@Override
	public void process(
			String theKey, List<IQueryParameterType> theSortItems, SearchParameterMap theSearchParameterMap) {
		List<SortSpec> sortSpecs = theSortItems.stream()
				.map(ISpecialParameterProcessor::paramAsQueryString)
				.map(SortSpec::fromR3OrLaterParameterValue)
				.toList();

		// SortSpec is an intrusive linked list, with the head as a bare pointer in the SearchParameterMap.
		Consumer<SortSpec> sortAppendAction = theSearchParameterMap::setSort;
		for (SortSpec sortSpec : sortSpecs) {
			sortAppendAction.accept(sortSpec);
			// we append at the tail
			sortAppendAction = sortSpec::setChain;
		}
	}
}
