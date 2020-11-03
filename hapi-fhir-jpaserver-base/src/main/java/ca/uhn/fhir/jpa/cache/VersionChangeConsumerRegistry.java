package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

// FIXME KHS retool searchparam and subscription to use this
@Repository
public class VersionChangeConsumerRegistry implements IVersionChangeConsumerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeConsumerRegistry.class);

	private static long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private volatile long myLastRefresh;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private ResourceVersionCacheSvc myResourceVersionCacheSvc;
	@Autowired
	private FhirContext myFhirContext;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private final VersionChangeConsumerMap myConsumerMap = new VersionChangeConsumerMap();

	private RefreshVersionCacheAndNotifyConsumersOnUpdate myInterceptor;

	@Override
	public void registerResourceVersionChangeConsumer(String theResourceType, SearchParameterMap map, IVersionChangeConsumer theVersionChangeConsumer) {
		myConsumerMap.add(theResourceType, theVersionChangeConsumer, map);
	}

	@PostConstruct
	public void start() {
		myInterceptor = new RefreshVersionCacheAndNotifyConsumersOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ISearchParamRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshCacheIfNecessary();
		}
	}

	@Override
	public boolean refreshAllCachesIfNecessary() {
		if (myLastRefresh == 0 || System.currentTimeMillis() - REFRESH_INTERVAL > myLastRefresh) {
			refreshAllCachesWithRetry();
			return true;
		} else {
			return false;
		}
	}

	@Override
	@VisibleForTesting
	public void clearConsumersForUnitTest() {
		myConsumerMap.clearConsumersForUnitTest();
	}

	@Override
	public void forceRefresh() {
		requestRefresh();
		refreshAllCachesWithRetry();
	}

	@Override
	public void requestRefresh() {
		synchronized (this) {
			myLastRefresh = 0;
		}
	}

	private void requestRefresh(String theResourceName) {
		// FIXME KHS this should be resourceName specific.  For now, just refresh all of them.
		requestRefresh();
	}

	private int refreshAllCachesWithRetry() {
		Retrier<Integer> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshAllCaches(REFRESH_INTERVAL);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	public int doRefreshAllCaches(long theRefreshInterval) {
		if (System.currentTimeMillis() - theRefreshInterval <= myLastRefresh) {
			return 0;
		}
		StopWatch sw = new StopWatch();
		// FIXME KHS call myResourceVersionCacheSvc and store the results in myVersionChangeCache
		myLastRefresh = System.currentTimeMillis();

		int count = 0;
		for (String resourceType : myConsumerMap.keySet()) {
			count += doRefresh(resourceType);
		}
		ourLog.debug("Refreshed all caches in {}ms", sw.getMillis());
		return count;
	}

	private long doRefresh(String theResourceName) {
		Class<? extends IBaseResource> resourceType = myFhirContext.getResourceDefinition(theResourceName).getImplementingClass();
		Map<IVersionChangeConsumer, SearchParameterMap> map = myConsumerMap.getConsumerMap(theResourceName);
		long count = 0;
		for (IVersionChangeConsumer consumer : map.keySet()) {
			try {
				IResourceVersionMap resourceVersionMap = myResourceVersionCacheSvc.getVersionLookup(theResourceName, resourceType, map.get(consumer));
				count += resourceVersionMap.populateInto(myResourceVersionCache, consumer);
			} catch (IOException e) {
				ourLog.error("Failed to refresh {}", theResourceName, e);
			}
		}
		return count;
	}

	@Interceptor
	public class RefreshVersionCacheAndNotifyConsumersOnUpdate {

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void created(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
		public void deleted(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
		public void updated(IBaseResource theResource) {
			handle(theResource);
		}

		private void handle(IBaseResource theResource) {
			if (theResource == null) {
				return;
			}
			String resourceName = myFhirContext.getResourceType(theResource);
			if (myConsumerMap.hasConsumersFor(resourceName)) {
				synchronized (this) {
					requestRefresh(resourceName);
				}
			}
		}
	}
}
