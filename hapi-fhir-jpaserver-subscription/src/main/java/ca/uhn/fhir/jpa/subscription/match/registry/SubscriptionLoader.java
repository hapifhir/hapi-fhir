package ca.uhn.fhir.jpa.subscription.match.registry;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;


public class SubscriptionLoader implements IResourceChangeListener {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionLoader.class);
	private static final int MAX_RETRIES = 60; // 60 * 5 seconds = 5 minutes
	private static long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;

	private final Object mySyncSubscriptionsLock = new Object();
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	DaoRegistry myDaoRegistry;
	private Semaphore mySyncSubscriptionsSemaphore = new Semaphore(1);
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private SubscriptionActivatingSubscriber mySubscriptionActivatingInterceptor;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	private SearchParameterMap mySearchParameterMap;

	/**
	 * Constructor
	 */
	public SubscriptionLoader() {
		super();
	}

	@PostConstruct
	public void registerListener() {
		mySearchParameterMap = getSearchParameterMap();
		IResourceChangeListenerCache subscriptionCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Subscription", mySearchParameterMap, this, REFRESH_INTERVAL);
		subscriptionCache.forceRefresh();
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	private boolean subscriptionsDaoExists() {
		return myDaoRegistry != null && myDaoRegistry.isResourceTypeSupported("Subscription");
	}

	/**
	 * Read the existing subscriptions from the database
	 */
	public void syncSubscriptions() {
		if (!subscriptionsDaoExists()) {
			return;
		}
		if (!mySyncSubscriptionsSemaphore.tryAcquire()) {
			return;
		}
		try {
			doSyncSubscriptionsWithRetry();
		} finally {
			mySyncSubscriptionsSemaphore.release();
		}
	}

	@VisibleForTesting
	public void acquireSemaphoreForUnitTest() throws InterruptedException {
		mySyncSubscriptionsSemaphore.acquire();
	}

	@VisibleForTesting
	public int doSyncSubscriptionsForUnitTest() {
		// Two passes for delete flag to take effect
		int first = doSyncSubscriptionsWithRetry();
		int second = doSyncSubscriptionsWithRetry();
		return first + second;
	}

	synchronized int doSyncSubscriptionsWithRetry() {
		Retrier<Integer> syncSubscriptionRetrier = new Retrier<>(this::doSyncSubscriptions, MAX_RETRIES);
		return syncSubscriptionRetrier.runWithRetry();
	}

	private int doSyncSubscriptions() {
		if (mySchedulerService.isStopping()) {
			return 0;
		}

		synchronized (mySyncSubscriptionsLock) {
			ourLog.debug("Starting sync subscriptions");

			IBundleProvider subscriptionBundleList =  getSubscriptionDao().search(mySearchParameterMap);

			Integer subscriptionCount = subscriptionBundleList.size();
			assert subscriptionCount != null;
			if (subscriptionCount >= SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS) {
				ourLog.error("Currently over " + SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS + " subscriptions.  Some subscriptions have not been loaded.");
			}

			List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionCount);

			return updateSubscriptionRegistry(resourceList);
		}
	}

	private IFhirResourceDao<?> getSubscriptionDao() {
		return myDaoRegistry.getSubscriptionDao();
	}

	@Nonnull
	private SearchParameterMap getSearchParameterMap() {
		SearchParameterMap map = new SearchParameterMap();

		if (mySearchParamRegistry.getActiveSearchParam("Subscription", "status") != null) {
			map.add(Subscription.SP_STATUS, new TokenOrListParam()
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
		}
		map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);
		return map;
	}

	private int updateSubscriptionRegistry(List<IBaseResource> theResourceList) {
		Set<String> allIds = new HashSet<>();
		int activatedCount = 0;
		int registeredCount = 0;

		for (IBaseResource resource : theResourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);

			boolean activated = mySubscriptionActivatingInterceptor.activateSubscriptionIfRequired(resource);
			if (activated) {
				activatedCount++;
			}

			boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(resource);
			if (registered) {
				registeredCount++;
			}
		}

		mySubscriptionRegistry.unregisterAllSubscriptionsNotInCollection(allIds);
		ourLog.debug("Finished sync subscriptions - activated {} and registered {}", theResourceList.size(), registeredCount);
		return activatedCount;
	}

	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		if (!subscriptionsDaoExists()) {
			ourLog.warn("Subsriptions are enabled on this server, but there is no Subscription DAO configured.");
			return;
		}
		IFhirResourceDao<?> subscriptionDao = getSubscriptionDao();
		List<IBaseResource> resourceList = theResourceIds.stream().map(subscriptionDao::read).collect(Collectors.toList());
		updateSubscriptionRegistry(resourceList);
	}

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		// For now ignore the contents of theResourceChangeEvent.  In the future, consider updating the registry based on
		// known subscriptions that have been created, updated & deleted
		syncSubscriptions();
	}
}

