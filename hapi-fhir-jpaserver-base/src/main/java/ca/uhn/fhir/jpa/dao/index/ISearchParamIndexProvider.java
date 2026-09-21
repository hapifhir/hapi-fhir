/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseSearchParamPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.Collection;
import java.util.Optional;

/**
 * Extension point for a custom search-parameter index that replaces HAPI's built-in one. A single
 * provider owns the full lifecycle of its index: reads, writes, expunge, and coordination with the
 * built-in index.
 *
 * <p>Providers are discovered as Spring beans and dispatched through {@link SearchParamIndexProviderRegistry}
 * as an ordered chain: for a given parameter the registry consults each provider (in {@code @Order}) and
 * uses the first whose {@link #supports(SearchParamIndexRouting)} returns {@code true}; if none supports it,
 * HAPI falls back to its built-in index.
 *
 * <p>Note: this is an internal extension API. It is not considered stable and may change or be removed in
 * future releases without notice.
 */
public interface ISearchParamIndexProvider {

	/**
	 * Whether this provider owns the index for the given parameter.
	 *
	 * @param theRouting the parameter being routed
	 * @return {@code true} if this provider supports the parameter's index
	 */
	boolean supports(SearchParamIndexRouting theRouting);

	/**
	 * Build the predicate for the search clause for the custom index.
	 *
	 * @param theSqlBuilder the query builder for the current search
	 * @param theParamName  the name of the search parameter being queried
	 * @return the predicate builder, or {@link Optional#empty()}
	 */
	Optional<BaseSearchParamPredicateBuilder> createPredicateBuilder(
			SearchQueryBuilder theSqlBuilder, String theParamName);

	/**
	 * Synchronizes this custom index for a single resource.
	 *
	 * @param theRequestDetails         the request context
	 * @param theExtractedParams        the extracted parameters of the resource being indexed
	 * @param theEntity                 the resource entity being indexed
	 * @param isNewResource             {@code true} on first-ever persist
	 * @return the number of rows added and removed
	 */
	AddRemoveCount synchronize(
			RequestDetails theRequestDetails,
			Collection<? extends BaseResourceIndex> theExtractedParams,
			ResourceTable theEntity,
			boolean isNewResource);

	/**
	 * Delete every row this custom index holds for the given resource. Invoked whenever a resource's
	 * search-param indexes are cleared (on resource deletion and during {@code $expunge}).
	 *
	 * @param theResourceId the persistent id of the resource whose index rows are being deleted
	 */
	void deleteByResourceId(JpaPid theResourceId);

	/**
	 * Whether HAPI should still write its built-in index for the routed type.
	 *
	 * @param theRouting the built-in index type in question
	 * @return {@code true} to suppress HAPI's built-in write for this type; {@code false} to keep writing it
	 */
	default boolean suppressesBuiltInIndex(SearchParamIndexRouting theRouting) {
		return false;
	}
}
