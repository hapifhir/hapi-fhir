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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Search result provider for fetching pages of a previously executed cache-aware search.
 *
 * @see CacheAwareJpaSearchSvcImpl
 * @since 8.14.0
 */
public class CacheAwareJpaSearchBundleProviderSubsequentPage extends BaseCacheAwareJpaSearchBundleProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareJpaSearchBundleProviderSubsequentPage.class);

	private final String mySearchUuid;

	/**
	 * Constructor
	 */
	public CacheAwareJpaSearchBundleProviderSubsequentPage(
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			String theSearchUuid,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IPagingProvider thePagingProvider,
			JpaStorageSettings theStorageSettings,
			EntityManager theEntityManager,
			IHapiTransactionService theTxService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			ExceptionService theExceptionService,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory) {
		super(
				theFhirContext,
				theRequestDetails,
				theInterceptorBroadcaster,
				thePagingProvider,
				theStorageSettings,
				theEntityManager,
				theTxService,
				theRequestPartitionHelperSvc,
				theSearchCacheSvc,
				theSearchResultCacheSvc,
				theExceptionService,
				theSearchBuilderFactory,
				null,
				null);

		mySearchUuid = theSearchUuid;
	}

	@Override
	protected Search provideSearchEntity() {
		ourLog.debug("Fetching cached search with UUID: {}", mySearchUuid);

		Optional<Search> searchEntityOpt = mySearchCacheSvc.fetchByUuid(mySearchUuid, myRequestPartitionId);
		return searchEntityOpt.orElseThrow(() -> myExceptionService.newUnknownSearchException(mySearchUuid));
	}

	@Nullable
	@Override
	public String getUuid() {
		return mySearchUuid;
	}

	@Nullable
	@Override
	public Integer size() {

		/// In case someone calls this before calling {@link #getResources(int, int, ResponsePage.ResponsePageBuilder)}
		if (!super.hasLoadedSearchEntity() && mySearchUuid != null) {
			initializeSearchIfNecessary();
		}

		return super.size();
	}
}
