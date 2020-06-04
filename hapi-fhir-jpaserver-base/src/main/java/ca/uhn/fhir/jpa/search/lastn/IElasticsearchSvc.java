package ca.uhn.fhir.jpa.search.lastn;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.util.List;

public interface IElasticsearchSvc {

	/**
	 * Returns identifiers for the last most recent N observations that meet the specified criteria.
	 * @param theSearchParameterMap SearchParameterMap containing search parameters used for filtering the last N observations. Supported parameters include Subject, Patient, Code, Category and Max (the parameter used to determine N).
	 * @param theFhirContext Current FhirContext.
	 * @param theMaxResultsToFetch The maximum number of results to return for the purpose of paging.
	 * @return
	 */
	List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch);
}
