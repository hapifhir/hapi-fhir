/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.cache;

import ca.uhn.fhir.IHapiBootOrder;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.util.IResourceRepositoryCache;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public abstract class BaseResourceCacheSynchronizer implements IResourceChangeListener, IResourceRepositoryCache {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseResourceCacheSynchronizer.class);
	public static final int MAX_RETRIES = 60; // 60 * 5 seconds = 5 minutes
	public static final long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	private static final long FORCE_REFRESH_TIMEOUT_SECONDS = 300;
	private final String myResourceName;

	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Autowired
	private DaoRegistry myDaoRegistry;

	private SearchParameterMap mySearchParameterMap;
	private final SystemRequestDetails mySystemRequestDetails;
	private boolean myStopping;
	private final Semaphore mySyncResourcesSemaphore = new Semaphore(1);
	private final Object mySyncResourcesLock = new Object();

	private Integer myMaxRetryCount = null;
	private boolean myInitialized = false;
	private final RequestPartitionId myRequestPartitionId;

	protected BaseResourceCacheSynchronizer(String theResourceName, RequestPartitionId theRequestPartitionId) {
		this(theResourceName, theRequestPartitionId, null, null);
	}

	protected BaseResourceCacheSynchronizer(
			String theResourceName,
			RequestPartitionId theRequestPartitionId,
			IResourceChangeListenerRegistry theResourceChangeListenerRegistry,
			DaoRegistry theDaoRegistry) {
		myResourceName = theResourceName;
		myRequestPartitionId = theRequestPartitionId;
		myResourceChangeListenerRegistry = theResourceChangeListenerRegistry;
		myDaoRegistry = theDaoRegistry;

		mySystemRequestDetails = SystemRequestDetails.forAllPartitions();
		// bypass consent checks for reading Subscription resources since this is a system action
		ConsentInterceptor.skipAllConsentForRequest(mySystemRequestDetails);
	}

	/**
	 * This method performs a search in the DB, so use the {@link ContextRefreshedEvent}
	 * to ensure that it runs after the database initializer
	 */
	@EventListener(classes = ContextRefreshedEvent.class)
	@Order(IHapiBootOrder.AFTER_SUBSCRIPTION_INITIALIZED)
	public void registerListener() {
		if (myInitialized) {
			return;
		}
		if (myDaoRegistry.getResourceDaoOrNull(myResourceName) == null) {
			ourLog.info("No resource DAO found for resource type {}, not registering listener", myResourceName);
			return;
		}

		IResourceChangeListenerCache resourceCache =
				myResourceChangeListenerRegistry.registerResourceResourceChangeListener(
						myResourceName, myRequestPartitionId, provideSearchParameterMap(), this, REFRESH_INTERVAL);
		resourceCache.forceRefresh();
		myInitialized = true;
	}

	private SearchParameterMap provideSearchParameterMap() {
		SearchParameterMap searchParameterMap = mySearchParameterMap;
		if (searchParameterMap == null) {
			searchParameterMap = getSearchParameterMap();
			mySearchParameterMap = searchParameterMap;
		}
		return searchParameterMap;
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	private boolean daoNotAvailable() {
		return myDaoRegistry == null || !myDaoRegistry.isResourceTypeSupported(myResourceName);
	}

	@Override
	public void requestRefresh() {
		if (daoNotAvailable()) {
			return;
		}
		if (!mySyncResourcesSemaphore.tryAcquire()) {
			return;
		}
		try {
			doSyncResourcesWithRetry();
		} finally {
			mySyncResourcesSemaphore.release();
		}
	}

	@VisibleForTesting
	@Override
	public void forceRefresh() {
		if (daoNotAvailable()) {
			throw new ConfigurationException(Msg.code(2652) + "Attempt to force refresh without a dao");
		}
		try {
			if (mySyncResourcesSemaphore.tryAcquire(FORCE_REFRESH_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
				doSyncResourcesWithRetry();
			} else {
				String errorMessage = String.format(
						Msg.code(2653) + "Timed out waiting %s %s to refresh %s cache",
						FORCE_REFRESH_TIMEOUT_SECONDS,
						"seconds",
						myResourceName);
				ourLog.error(errorMessage);
				throw new ConfigurationException(Msg.code(2663) + errorMessage);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(Msg.code(2654) + e);
		} finally {
			mySyncResourcesSemaphore.release();
		}
	}

	@VisibleForTesting
	public void acquireSemaphoreForUnitTest() throws InterruptedException {
		mySyncResourcesSemaphore.acquire();
	}

	@VisibleForTesting
	public int doSyncResourcesForUnitTest() {
		// Two passes for delete flag to take effect
		int first = doSyncResourcesWithRetry();
		int second = doSyncResourcesWithRetry();
		return first + second;
	}

	synchronized int doSyncResourcesWithRetry() {
		// retry runs MAX_RETRIES times
		// and if errors result every time, it will fail
		Retrier<Integer> syncResourceRetrier = new Retrier<>(this::doSyncResources, getMaxRetries());
		return syncResourceRetrier.runWithRetry();
	}

	private int getMaxRetries() {
		if (myMaxRetryCount != null) {
			return myMaxRetryCount;
		}
		return MAX_RETRIES;
	}

	@VisibleForTesting
	public void setMaxRetries(Integer theMaxRetries) {
		myMaxRetryCount = theMaxRetries;
	}

	@SuppressWarnings("unchecked")
	private int doSyncResources() {
		if (isStopping()) {
			return 0;
		}

		synchronized (mySyncResourcesLock) {
			ourLog.debug("Starting sync {}s", myResourceName);

			List<IBaseResource> resourceList = (List<IBaseResource>)
					getResourceDao().searchForResources(provideSearchParameterMap(), mySystemRequestDetails);
			return syncResourcesIntoCache(resourceList);
		}
	}

	protected abstract int syncResourcesIntoCache(@Nonnull List<IBaseResource> resourceList);

	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		myStopping = false;
	}

	@EventListener(ContextClosedEvent.class)
	public void shutdown() {
		myStopping = true;
	}

	private boolean isStopping() {
		return myStopping;
	}

	private IFhirResourceDao<?> getResourceDao() {
		return myDaoRegistry.getResourceDao(myResourceName);
	}

	@Override
	public void handleInit(@Nonnull Collection<IIdType> theResourceIds) {
		if (daoNotAvailable()) {
			ourLog.warn(
					"The resource type {} is enabled on this server, but there is no {} DAO configured.",
					myResourceName,
					myResourceName);
			return;
		}
		IFhirResourceDao<?> resourceDao = getResourceDao();
		SystemRequestDetails systemRequestDetails = SystemRequestDetails.forAllPartitions();
		List<IBaseResource> resourceList = new ArrayList<>();

		/*
		 * We are pretty lenient here, because any failure will block the server
		 * from starting up. We should generally never fail to load here, but it
		 * can potentially happen if a resource is manually deleted in the database
		 * (ie. marked with a deletion time manually) but the indexes aren't cleaned
		 * up.
		 */
		for (IIdType id : theResourceIds) {
			IBaseResource read;
			try {
				read = resourceDao.read(id, systemRequestDetails);
			} catch (BaseServerResponseException e) {
				ourLog.warn("Unable to fetch resource {}", id, e);
				continue;
			}
			resourceList.add(read);
		}
		handleInit(resourceList);
	}

	protected abstract void handleInit(@Nonnull List<IBaseResource> resourceList);

	@Override
	public void handleChange(@Nonnull IResourceChangeEvent theResourceChangeEvent) {
		// For now ignore the contents of theResourceChangeEvent.  In the future, consider updating the registry based
		// on
		// known resources that have been created, updated & deleted
		requestRefresh();
	}

	@Nonnull
	protected abstract SearchParameterMap getSearchParameterMap();
}
