/*
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchIndexData;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public interface IFulltextSearchSvc {

	/**
	 * Search the Lucene/Elastic index for pids using params supported in theParams,
	 * consuming entries from theParams when used to query.
	 *
	 * @param theResourceName   the resource name to restrict the query.
	 * @param theParams         the full query - modified to return only params unused by the index.
	 * @param theRequestDetails The request details
	 * @return the pid list for the matchign resources.
	 */
	<T extends IResourcePersistentId> List<T> search(
			String theResourceName, SearchParameterMap theParams, RequestDetails theRequestDetails);

	/**
	 * Query the index for a plain list (non-scrollable) iterator of results.
	 *
	 * @param theResourceName      e.g. Patient
	 * @param theParams            The search query
	 * @param theMaxResultsToFetch maximum results to fetch
	 * @param theRequestDetails The request details
	 * @return Iterator of result PIDs
	 */
	ISearchQueryExecutor searchNotScrolled(
			String theResourceName,
			SearchParameterMap theParams,
			Integer theMaxResultsToFetch,
			RequestDetails theRequestDetails);

	/**
	 * Autocomplete search for NIH $expand contextDirection=existing
	 * @param theOptions operation options
	 * @return a ValueSet with the search hits as the expansion.
	 */
	IBaseResource tokenAutocompleteValueSetSearch(ValueSetAutocompleteOptions theOptions);

	<T extends IResourcePersistentId> List<T> everything(
			String theResourceName,
			SearchParameterMap theParams,
			T theReferencingPid,
			RequestDetails theRequestDetails);

	boolean isDisabled();

	ExtendedHSearchIndexData extractLuceneIndexData(
			IBaseResource theResource, ResourceIndexedSearchParams theNewParams);

	/**
	 * Returns true if the parameter map can be handled for hibernate search.
	 * We have to filter out any queries that might use search params
	 * we only know how to handle in JPA.
	 * -
	 * See {@link ca.uhn.fhir.jpa.dao.search.ExtendedHSearchSearchBuilder#addAndConsumeAdvancedQueryClauses}
	 */
	boolean canUseHibernateSearch(String theResourceType, SearchParameterMap theParameterMap);

	/**
	 * Re-publish the resource to the full-text index.
	 * -
	 * During update, hibernate search only republishes the entity if it has changed.
	 * During $reindex, we want to force the re-index.
	 *
	 * @param theEntity the fully populated ResourceTable entity
	 */
	void reindex(ResourceTable theEntity);

	List<IResourcePersistentId> lastN(SearchParameterMap theParams, Integer theMaximumResults);

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

	List<IBaseResource> searchForResources(
			String theResourceType, SearchParameterMap theParams, RequestDetails theRequestDetails);

	boolean supportsAllOf(SearchParameterMap theParams);

	/**
	 * Given a resource type that is indexed by hibernate search, and a list of objects reprenting the IDs you wish to delete,
	 * this method will delete the resources from the Hibernate Search index. This is useful for situations where a deletion occurred
	 * outside a Hibernate ORM session, leaving dangling documents in the index.
	 *
	 * @param theClazz The class, which must be annotated with {@link  org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed}
	 * @param theGivenIds The list of IDs for the given document type. Note that while this is a List<Object>, the type must match the type of the `@Id` field on the given class.
	 */
	void deleteIndexedDocumentsByTypeAndId(Class theClazz, List<Object> theGivenIds);

	/**
	 * Given a resource type and a {@link SearchParameterMap}, return true only if all sort terms are supported.
	 *
	 * @param theResourceName The resource type for the query.
	 * @param theParams The {@link SearchParameterMap} being searched with.
	 * @return true if all sort terms are supported, false otherwise.
	 */
	boolean supportsAllSortTerms(String theResourceName, SearchParameterMap theParams);
}
