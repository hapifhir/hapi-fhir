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

import ca.uhn.fhir.jpa.search.builder.predicate.BaseSearchParamPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.Optional;

/**
 * Allows the implementing class to read search parameters from its own index instead of
 * HAPI's built-in search index.
 *
 * <p>See {@link ICustomIndexSynchronizer}, which handles writing to the custom index.
 *
 * <p>Providers are checked in order. If a provider recognizes the search
 * parameter, it should return the appropriate predicate builder. Returning an
 * empty result indicates that the provider does not handle the parameter, and
 * HAPI will fall back to its built-in index.
 *
 * <p>When handling a parameter type, the provider must return the matching
 * predicate builder implementation (for example, {@code BaseTokenPredicateBuilder} for {@code TOKEN}).
 *
 * <p>Note: This is an internal extension API. It is not considered stable and may
 * change or be removed in future releases without notice.
 */
public interface ICustomIndexPredicateProvider {

	/**
	 * Returns a predicate builder for the given search parameter if this provider handles it.
	 *
	 * @param theSqlBuilder the query builder for the current search
	 * @param theParamType  the type of the search parameter being queried
	 * @param theParamName  the name of the search parameter being queried
	 */
	Optional<BaseSearchParamPredicateBuilder> provideCustomPredicateBuilder(
			SearchQueryBuilder theSqlBuilder, RestSearchParameterTypeEnum theParamType, String theParamName);
}
