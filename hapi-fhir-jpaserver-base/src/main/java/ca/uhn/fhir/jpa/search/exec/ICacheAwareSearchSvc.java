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
package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.ResponsePage;

/**
 * This service performs cache-aware searches. In other words, when executing a search
 * it will check the {@link ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc} for any existing
 * cached searches, and if appropriate will store any results it finds back in the
 * search cache.
 *
 * @see IStatelessSearchSvc The equivalent service for non-cache-aware searches.
 */
public interface ICacheAwareSearchSvc {

	/**
	 * Perform a new search using a set of search parameters, either by finding an
	 * existing cached search or by executing a new search.
	 * <p>
	 * <b>Performance Note:</b> Callers of this method should try to call {@link IBundleProvider#getResources(int, int)} or
	 * {@link IBundleProvider#getResources(int, int, ResponsePage.ResponsePageBuilder)} before calling
	 * any other method on the returned bundle provider. This ensures that an appropriate number of
	 * results are fetched from the database during the initial call, avoiding additional round-trips.
	 * </p>
	 */
	IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			Search theSearchEntity,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId);

	/**
	 * Continue a previously returned search, fetching existing results from the
	 * search cache if possible or fetching new search results if necessary.
	 *
	 * @param theUuid The UUID associated with the initial search request. This is the value returned by {@link IBundleProvider#getUuid()}.
	 * @param theRequestDetails The request details for the current request
	 */
	IBundleProvider continueExistingSearch(String theUuid, RequestDetails theRequestDetails);
}
