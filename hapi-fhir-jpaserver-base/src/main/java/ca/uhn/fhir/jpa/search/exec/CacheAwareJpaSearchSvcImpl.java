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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This service performs cache-aware searches. In other words, when executing a search,
 * it will check the {@link ISearchCacheSvc} for any existing cached searches, and if appropriate, will store any results it finds back in the
 * search cache.
 */
public class CacheAwareJpaSearchSvcImpl implements ICacheAwareJpaSearchSvc {

	@Autowired
	private ExceptionService myExceptionSvc;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Autowired
	private ISearchResultCacheSvc mySearchResultCacheSvc;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IPagingProvider myPagingProvider;

	/**
	 * Constructor
	 */
	public CacheAwareJpaSearchSvcImpl() {
		super();
	}

	/**
	 * Unit test constructor
	 */
	public CacheAwareJpaSearchSvcImpl(
			FhirContext theFhirContext,
			IHapiTransactionService theTxService,
			JpaStorageSettings theStorageSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			EntityManager theEntityManager,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myFhirContext = theFhirContext;
		myTxService = theTxService;
		myStorageSettings = theStorageSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myEntityManager = theEntityManager;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			Search theSearchEntity,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId) {
		return new CacheAwareJpaSearchBundleProviderFirstPage(
				myFhirContext,
				theParams,
				theRequestDetails,
				theCacheControlDirective,
				theRequestPartitionId,
				theSearchEntity,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);
	}

	@Override
	public IBundleProvider continueExistingSearch(String theUuid, RequestDetails theRequestDetails) {
		CacheAwareJpaSearchBundleProviderSubsequentPage retVal = new CacheAwareJpaSearchBundleProviderSubsequentPage(
				myFhirContext,
				theRequestDetails,
				theUuid,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);

		if (myPartitionSettings.isPartitioningEnabled()) {
			ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchUuid(theUuid);
			RequestPartitionId requestPartitionId =
					myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, details);
			retVal.setRequestPartitionId(requestPartitionId);
		}

		return retVal;
	}
}
