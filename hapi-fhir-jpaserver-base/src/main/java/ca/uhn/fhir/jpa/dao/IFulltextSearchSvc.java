package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.List;

public interface IFulltextSearchSvc {


	/**
	 * Search the Lucene/Elastic index for pids using params supported in theParams,
	 * consuming entries from theParams when used to query.
	 *
	 * @param theResourceName the resource name to restrict the query.
	 * @param theParams       the full query - modified to return only params unused by the index.
	 * @return the pid list for the matchign resources.
	 */
	List<ResourcePersistentId> search(String theResourceName, SearchParameterMap theParams);


	/**
	 * Query the index for a scrollable iterator of results.
	 * No max size to the result iterator.
	 *
	 * @param theResourceName e.g. Patient
	 * @param theParams The search query
	 * @return Iterator of result PIDs
	 */
	ISearchQueryExecutor searchAsync(String theResourceName, SearchParameterMap theParams);

	/**
	 * Autocomplete search for NIH $expand contextDirection=existing
	 * @param theOptions operation options
	 * @return a ValueSet with the search hits as the expansion.
	 */
	IBaseResource tokenAutocompleteValueSetSearch(ValueSetAutocompleteOptions theOptions);

	List<ResourcePersistentId> everything(String theResourceName, SearchParameterMap theParams, ResourcePersistentId theReferencingPid);

	boolean isDisabled();

	ExtendedLuceneIndexData extractLuceneIndexData(IBaseResource theResource, ResourceIndexedSearchParams theNewParams);

	boolean supportsSomeOf(SearchParameterMap myParams);

	/**
	 * Re-publish the resource to the full-text index.
	 *
	 * During update, hibernate search only republishes the entity if it has changed.
	 * During $reindex, we want to force the re-index.
	 *
	 * @param theEntity the fully populated ResourceTable entity
	 */
	 void reindex(ResourceTable theEntity);

	List<ResourcePersistentId> lastN(SearchParameterMap theParams, Integer theMaximumResults);

	/**
	 * Returns inlined resource stored along with index mappings for matched identifiers
	 *
	 * @param thePids raw pids - we dont support versioned references
	 * @return Resources list or empty if nothing found
	 */
	List<IBaseResource> getResources(Collection<Long> thePids);

	/**
	 * Returns accurate hit count
	 */
	long count(String theResourceName, SearchParameterMap theParams);
}
