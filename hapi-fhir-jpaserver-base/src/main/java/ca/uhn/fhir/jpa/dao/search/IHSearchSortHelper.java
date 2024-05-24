/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortSpec;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;

/**
 * Helper for building freetext sort clauses
 */
public interface IHSearchSortHelper {

	SortFinalStep getSortClauses(SearchSortFactory theSortFactory, SortSpec theSort, String theResourceType);

	/**
	 * Given a resource type and a {@link SearchParameterMap}, return true only if all sort terms are supported.
	 *
	 * @param theResourceType The resource type for the query.
	 * @param theParams The {@link SearchParameterMap} being searched with.
	 * @return true if all sort terms are supported, false otherwise.
	 */
	boolean supportsAllSortTerms(String theResourceType, SearchParameterMap theParams);
}
