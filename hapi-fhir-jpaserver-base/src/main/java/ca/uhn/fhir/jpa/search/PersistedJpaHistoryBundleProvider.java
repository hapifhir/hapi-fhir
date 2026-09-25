/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.HistoryBuilder;
import ca.uhn.fhir.jpa.dao.HistoryBuilderFactory;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SearchCacheStatus;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Bundle provider that is used for <code>_history</code> calls
 */
public class PersistedJpaHistoryBundleProvider implements IBundleProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaHistoryBundleProvider.class);

	/*
	 * Autowired fields
	 */
	protected final RequestDetails myRequest;

	@Autowired
	protected HapiTransactionService myTxService;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private HistoryBuilderFactory myHistoryBuilderFactory;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired
	private IJpaStorageResourceParser myJpaStorageResourceParser;
	/*
	 * Non autowired fields (will be different for every instance
	 * of this class, since it's a prototype
	 */
	private Search mySearchEntity;
	private final String myUuid;
	private RequestPartitionId myRequestPartitionId;

	/**
	 * Constructor
	 */
	public PersistedJpaHistoryBundleProvider(RequestDetails theRequest, Search theSearch) {
		Validate.isTrue(
				theSearch.getSearchType() == SearchTypeEnum.HISTORY,
				"This bundle provider may only be used for _history calls");
		myRequest = theRequest;
		mySearchEntity = theSearch;
		myUuid = theSearch.getUuid();
	}

	// Note: Leave as protected, HSPC depends on this
	@SuppressWarnings("WeakerAccess")
	protected void setSearchEntity(Search theSearchEntity) {
		Validate.notNull(theSearchEntity, "theSearchEntity must not be null");
		Validate.isTrue(
				theSearchEntity.getSearchType() == SearchTypeEnum.HISTORY,
				"theSearchEntity must be a history search: %s",
				theSearchEntity.getSearchType());
		mySearchEntity = theSearchEntity;
	}

	/**
	 * Perform a history search
	 */
	private List<IBaseResource> doHistoryInTransaction(Integer theOffset, int theFromIndex, int theToIndex) {

		HistoryBuilder historyBuilder = myHistoryBuilderFactory.newHistoryBuilder(
				mySearchEntity.getResourceType(),
				mySearchEntity.getResourceId(),
				mySearchEntity.getLastUpdatedLow(),
				mySearchEntity.getLastUpdatedHigh());

		RequestPartitionId partitionId = getRequestPartitionId();
		List<ResourceHistoryTable> results = historyBuilder.fetchEntities(
				partitionId, theOffset, theFromIndex, theToIndex, mySearchEntity.getHistorySearchStyle());

		List<IBaseResource> retVal = new ArrayList<>();
		for (ResourceHistoryTable next : results) {
			retVal.add(myJpaStorageResourceParser.toResource(next, true));
		}

		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, myRequest);

		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES)) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(retVal);
			HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			for (int i = retVal.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					retVal.remove(i);
				}
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PRESHOW_RESOURCES)) {
			SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
					.add(IPreResourceShowDetails.class, showDetails)
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			retVal = showDetails.getAllResources();
		}

		return retVal;
	}

	@Nonnull
	protected final RequestPartitionId getRequestPartitionId() {
		if (myRequestPartitionId == null) {
			ReadPartitionIdRequestDetails details;
			if (mySearchEntity == null) {
				details = ReadPartitionIdRequestDetails.forSearchUuid(myUuid);
			} else if (mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY) {
				details = ReadPartitionIdRequestDetails.forHistory(mySearchEntity.getResourceType(), null);
			} else {
				SearchParameterMap params =
						mySearchEntity.getSearchParameterMap().orElse(null);
				details = ReadPartitionIdRequestDetails.forSearchType(mySearchEntity.getResourceType(), params, null);
			}
			myRequestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(myRequest, details);
		}
		assert myRequestPartitionId != null;
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	/**
	 * Returns false if the entity can't be found
	 */
	public boolean ensureSearchEntityLoaded() {
		if (mySearchEntity == null) {
			Optional<Search> searchOpt = myTxService
					.withRequest(myRequest)
					.withRequestPartitionId(myRequestPartitionId)
					.execute(() -> mySearchCacheSvc.fetchByUuid(myUuid, myRequestPartitionId));
			if (searchOpt.isEmpty()) {
				return false;
			}

			setSearchEntity(searchOpt.get());

			ourLog.trace(
					"Retrieved search with version {} and total {}",
					mySearchEntity.getVersion(),
					mySearchEntity.getTotalCount());

			return true;
		}

		if (mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY) {
			if (mySearchEntity.getTotalCount() == null) {
				calculateHistoryCount();
			}
		}

		return true;
	}

	/**
	 * Note that this method is called outside a DB transaction, and uses a loading cache
	 * (assuming the default {@literal COUNT_CACHED} mode) so this effectively throttles
	 * access to the database by preventing multiple concurrent DB calls for an expensive
	 * count operation.
	 */
	private void calculateHistoryCount() {
		MemoryCacheService.HistoryCountKey key;
		if (mySearchEntity.getResourceId() != null) {
			key = MemoryCacheService.HistoryCountKey.forInstance(mySearchEntity.getResourceId());
		} else if (mySearchEntity.getResourceType() != null) {
			key = MemoryCacheService.HistoryCountKey.forType(mySearchEntity.getResourceType());
		} else {
			key = MemoryCacheService.HistoryCountKey.forSystem();
		}

		Function<MemoryCacheService.HistoryCountKey, Integer> supplier = k -> myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(getRequestPartitionId())
				.execute(() -> {
					HistoryBuilder historyBuilder = myHistoryBuilderFactory.newHistoryBuilder(
							mySearchEntity.getResourceType(),
							mySearchEntity.getResourceId(),
							mySearchEntity.getLastUpdatedLow(),
							mySearchEntity.getLastUpdatedHigh());
					Long count = historyBuilder.fetchCount(getRequestPartitionId());
					return count.intValue();
				});

		boolean haveOffset = mySearchEntity.getLastUpdatedLow() != null || mySearchEntity.getLastUpdatedHigh() != null;

		switch (myStorageSettings.getHistoryCountMode()) {
			case COUNT_ACCURATE: {
				int count = supplier.apply(key);
				mySearchEntity.setTotalCount(count);
				break;
			}
			case CACHED_ONLY_WITHOUT_OFFSET: {
				if (!haveOffset) {
					int count = myMemoryCacheService.get(MemoryCacheService.CacheEnum.HISTORY_COUNT, key, supplier);
					mySearchEntity.setTotalCount(count);
				}
				break;
			}
			case COUNT_DISABLED: {
				break;
			}
		}
	}

	@Override
	public InstantDt getPublished() {
		ensureSearchEntityLoaded();
		return new InstantDt(mySearchEntity.getCreated());
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		return getResources(theFromIndex, theToIndex, new ResponsePage.ResponsePageBuilder());
	}

	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		boolean entityLoaded = ensureSearchEntityLoaded();
		assert entityLoaded;
		assert mySearchEntity != null;
		assert mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY;

		return myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(getRequestPartitionId())
				.execute(() -> doHistoryInTransaction(mySearchEntity.getOffset(), theFromIndex, theToIndex));
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	public SearchCacheStatus getCacheStatus() {
		return null;
	}

	@Override
	public Integer preferredPageSize() {
		ensureSearchEntityLoaded();
		return mySearchEntity.getPreferredPageSize();
	}

	@Override
	public Integer size() {
		ensureSearchEntityLoaded();
		assert mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY;
		QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(mySearchEntity);

		Integer size = mySearchEntity.getTotalCount();
		if (size != null) {
			return Math.max(0, size);
		}

		return null;
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}
}
