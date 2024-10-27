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
package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchClauseBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;

/**
 * This is a parameter class for the
 * {@link ca.uhn.fhir.jpa.dao.search.ExtendedHSearchSearchBuilder#addAndConsumeAdvancedQueryClauses(ExtendedHSearchClauseBuilder, ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams)}
 * method, so that we can keep the signature manageable (small) and allow for updates without breaking
 * implementers so often.
 */
public class ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams {
	/**
	 * Resource type
	 */
	private String myResourceType;
	/**
	 * The registered search
	 */
	private SearchParameterMap mySearchParameterMap;
	/**
	 * Search param registry
	 */
	private ISearchParamRegistry mySearchParamRegistry;

	public String getResourceType() {
		return myResourceType;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setSearchParameterMap(SearchParameterMap theParams) {
		mySearchParameterMap = theParams;
		return this;
	}

	public ISearchParamRegistry getSearchParamRegistry() {
		return mySearchParamRegistry;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setSearchParamRegistry(
			ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
		return this;
	}
}
