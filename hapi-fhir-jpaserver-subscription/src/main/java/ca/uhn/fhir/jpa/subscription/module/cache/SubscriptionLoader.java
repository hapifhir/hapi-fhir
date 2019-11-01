package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;


@Service
@Lazy
public class SubscriptionLoader {
	public static final long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionLoader.class);
	private static final int MAX_RETRIES = 60; // 60 * 5 seconds = 5 minutes
	private final Object mySyncSubscriptionsLock = new Object();
	@Autowired
	private ISubscriptionProvider mySubscriptionProvider;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired(required = false)
	private IDaoRegistry myDaoRegistry;
	private Semaphore mySyncSubscriptionsSemaphore = new Semaphore(1);
	@Autowired
	private ISchedulerService mySchedulerService;

	/**
	 * Read the existing subscriptions from the database
	 */
	public void syncSubscriptions() {
		if (myDaoRegistry != null && !myDaoRegistry.isResourceTypeSupported("Subscription")) {
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
	void acquireSemaphoreForUnitTest() throws InterruptedException {
		mySyncSubscriptionsSemaphore.acquire();
	}


	@PostConstruct
	public void registerScheduledJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(SubscriptionLoader.class.getName());
		jobDetail.setJobClass(SubscriptionLoader.SubmitJob.class);
		mySchedulerService.scheduleFixedDelay(REFRESH_INTERVAL, false, jobDetail);
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
			SearchParameterMap map = new SearchParameterMap();
			map.add(Subscription.SP_STATUS, new TokenOrListParam()
				// TODO KHS Ideally we should only be pulling ACTIVE subscriptions here, but this class is overloaded so that
				// the @Scheduled task also activates requested subscriptions if their type was enabled after they were requested
				// There should be a separate @Scheduled task that looks for requested subscriptions that need to be activated
				// independent of the registry loading process.
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
			map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);

			IBundleProvider subscriptionBundleList = mySubscriptionProvider.search(map);

			Integer subscriptionCount = subscriptionBundleList.size();
			assert subscriptionCount != null;
			if (subscriptionCount >= SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS) {
				ourLog.error("Currently over " + SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS + " subscriptions.  Some subscriptions have not been loaded.");
			}

			List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionCount);

			Set<String> allIds = new HashSet<>();
			int changesCount = 0;
			for (IBaseResource resource : resourceList) {
				String nextId = resource.getIdElement().getIdPart();
				allIds.add(nextId);
				boolean changed = mySubscriptionProvider.loadSubscription(resource);
				if (changed) {
					changesCount++;
				}
			}

			mySubscriptionRegistry.unregisterAllSubscriptionsNotInCollection(allIds);
			ourLog.debug("Finished sync subscriptions - found {}", resourceList.size());

			return changesCount;
		}
	}

	@VisibleForTesting
	public void setSubscriptionProviderForUnitTest(ISubscriptionProvider theSubscriptionProvider) {
		mySubscriptionProvider = theSubscriptionProvider;
	}

	@DisallowConcurrentExecution
	@PersistJobDataAfterExecution
	public static class SubmitJob extends FireAtIntervalJob {
		@Autowired
		private SubscriptionLoader myTarget;

		public SubmitJob() {
			super(REFRESH_INTERVAL);
		}

		@Override
		protected void doExecute(JobExecutionContext theContext) {
			myTarget.syncSubscriptions();
		}
	}
}

