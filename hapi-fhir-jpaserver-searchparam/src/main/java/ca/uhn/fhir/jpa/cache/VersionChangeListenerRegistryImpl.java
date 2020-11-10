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
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIXME KHS retool searchparam and subscription to use this
@Repository
public class VersionChangeListenerRegistryImpl implements IVersionChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeListenerRegistryImpl.class);

	static long LOCAL_REFRESH_INTERVAL = 10 * DateUtils.MILLIS_PER_SECOND;
	static long REMOTE_REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static volatile Map<String, Long> myLastRefreshPerResourceType = new HashMap<>();

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private ListenerNotifier myListenerNotifier;
	@Autowired
	private FhirContext myFhirContext;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private final VersionChangeListenerMap myListenerMap = new VersionChangeListenerMap();

	private RefreshVersionCacheAndNotifyListenersOnUpdate myInterceptor;

	@Override
	public void registerResourceVersionChangeListener(String theResourceName, SearchParameterMap theSearchParamMap, IVersionChangeListener theVersionChangeListener) {
		myListenerMap.add(theResourceName, theVersionChangeListener, theSearchParamMap);
	}

	@PostConstruct
	public void start() {
		myInterceptor = new RefreshVersionCacheAndNotifyListenersOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(LOCAL_REFRESH_INTERVAL, jobDetail);
	}

	@PreDestroy
	public void stop() {
		myInterceptorBroadcaster.unregisterInterceptor(myInterceptor);
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
	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenerMap.clearListenersForUnitTest();
	}

	@Override
	@VisibleForTesting
	public void clearCacheForUnitTest() {
		myResourceVersionCache.clearForUnitTest();
		myLastRefreshPerResourceType.clear();
	}

	@Override
	public long refreshAllCachesIfNecessary() {
		long retval = 0;
		for (String resourceName : myListenerMap.resourceNames()) {
			retval += refreshCacheIfNecessary(resourceName);
		}
		// This will return true if at least 1 of the Resource Caches was refreshed
		return retval;
	}

	@Override
	public long refreshCacheIfNecessary(String theResourceName) {
		long retval = 0;
		long lastRefresh = myLastRefreshPerResourceType.computeIfAbsent(theResourceName, key -> 0L);
		if (lastRefresh == 0 || System.currentTimeMillis() - REMOTE_REFRESH_INTERVAL > lastRefresh) {
			retval = refreshCacheWithRetry(theResourceName);
		}
		return retval;
	}

	@Override
	public void requestRefresh(String theResourceName) {
		synchronized (this) {
			myLastRefreshPerResourceType.put(theResourceName, 0L);
		}
	}

	@Override
	public long forceRefresh(String theResourceName) {
		requestRefresh(theResourceName);
		return refreshCacheWithRetry(theResourceName);
	}

	@Override
	public long refreshCacheWithRetry(String theResourceName) {
		Retrier<Long> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshCachesAndNotifyListeners(theResourceName);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Override
	public long refreshAllCachesImmediately() {
		StopWatch sw = new StopWatch();
		long count = 0;
		for (String resourceType : myListenerMap.resourceNames()) {
			count += doRefreshCachesAndNotifyListeners(resourceType);
		}
		ourLog.debug("Refreshed all caches.  Updated {} entries in {}ms", count, sw.getMillis());
		return count;
	}

	private synchronized long doRefreshCachesAndNotifyListeners(String theResourceName) {
		List<VersionChangeListenerEntry> listenerEntries = myListenerMap.getListenerEntries(theResourceName);
		if (listenerEntries.isEmpty()) {
			return 0;
		}
		long count = 0;
		for (VersionChangeListenerEntry listenerEntry : listenerEntries) {
			SearchParameterMap searchParamMap = listenerEntry.getSearchParameterMap();
			ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theResourceName, searchParamMap);
			count += listenerEntry.notifyListener(myResourceVersionCache, newResourceVersionMap);
		}
		myLastRefreshPerResourceType.put(theResourceName, System.currentTimeMillis());
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
			synchronized (this) {
				requestRefresh(resourceName);
			}
		}
	}
}
