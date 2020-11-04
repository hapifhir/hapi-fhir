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
public class VersionChangeListenerRegistryImpl implements IVersionChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeListenerRegistryImpl.class);

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
	private ListenerNotifier myListenerNotifier;
	@Autowired
	private FhirContext myFhirContext;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private final VersionChangeListenerMap myListenerMap = new VersionChangeListenerMap();

	private RefreshVersionCacheAndNotifyListenersOnUpdate myInterceptor;

	@Override
	public void registerResourceVersionChangeListener(String theResourceType, SearchParameterMap map, IVersionChangeListener theVersionChangeListener) {
		myListenerMap.add(theResourceType, theVersionChangeListener, map);
	}

	@PostConstruct
	public void start() {
		myInterceptor = new RefreshVersionCacheAndNotifyListenersOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IVersionChangeListenerRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshAllCachesIfNecessary();
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
	public void clearListenersForUnitTest() {
		myListenerMap.clearListenersForUnitTest();
	}

	@Override
	@VisibleForTesting
	public void clearCacheForUnitTest() {
		myResourceVersionCache.clearForUnitTest();
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
		for (String resourceType : myListenerMap.keySet()) {
			count += doRefresh(resourceType);
		}
		ourLog.debug("Refreshed all caches in {}ms", sw.getMillis());
		return count;
	}

	private long doRefresh(String theResourceName) {
		Class<? extends IBaseResource> resourceType = myFhirContext.getResourceDefinition(theResourceName).getImplementingClass();
		Map<IVersionChangeListener, SearchParameterMap> map = myListenerMap.getListenerMap(theResourceName);
		long count = 0;
		for (IVersionChangeListener listener : map.keySet()) {
			try {
				ResourceVersionMap resourceVersionMap = myResourceVersionCacheSvc.getVersionLookup(theResourceName, resourceType, map.get(listener));
				count += myListenerNotifier.compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(myResourceVersionCache, resourceVersionMap, listener);
			} catch (IOException e) {
				ourLog.error("Failed to refresh {}", theResourceName, e);
			}
		}
		return count;
	}

	@Interceptor
	public class RefreshVersionCacheAndNotifyListenersOnUpdate {

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
			if (myListenerMap.hasListenersFor(resourceName)) {
				synchronized (this) {
					requestRefresh(resourceName);
				}
			}
		}
	}
}
