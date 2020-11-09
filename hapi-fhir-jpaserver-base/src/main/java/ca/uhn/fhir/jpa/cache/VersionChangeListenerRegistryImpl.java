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
import ca.uhn.fhir.model.primitive.IdDt;
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
import java.util.Map;

// FIXME KHS retool searchparam and subscription to use this
@Repository
public class VersionChangeListenerRegistryImpl implements IVersionChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeListenerRegistryImpl.class);

	static long LOCAL_REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	static long REMOTE_REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;
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
		System.out.println("myLastRefresh = " + myLastRefresh);
		if (myLastRefresh == 0 || System.currentTimeMillis() - LOCAL_REFRESH_INTERVAL > myLastRefresh) {
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
		// FIXME KHS Refresh after Tx completes when an Interceptor calls this code (after Tx closes?)
	}

	@Override
	public void requestRefresh(String theResourceName) {
		// TODO KBD Is this what KHS intended when suggesting that this should only refresh specific Resource Types?
		if (myListenerMap.hasListenersForResourceName(theResourceName)) {
			requestRefresh();
		} else {
			// This will add a new Resource to the cache without affecting any existing entries
			doRefresh(theResourceName);
		}
	}

	private int refreshAllCachesWithRetry() {
		Retrier<Integer> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshAllCaches(LOCAL_REFRESH_INTERVAL);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Override
	public int doRefreshAllCaches(long theRefreshInterval) {
		if (System.currentTimeMillis() - theRefreshInterval <= myLastRefresh) {
			return 0;
		}
		StopWatch sw = new StopWatch();
		myLastRefresh = System.currentTimeMillis();

		int count = 0;
		for (String resourceType : myListenerMap.resourceNames()) {
			count += doRefresh(resourceType);
		}
		ourLog.debug("Refreshed all caches in {}ms", sw.getMillis());
		return count;
	}

	// FIXME KBD I don't think this class is supposed to expose the Cache, so find another way to allow access to it
	public boolean cacheContainsKey(IdDt theIdDt) {
		return myResourceVersionCache.keySet().contains(theIdDt);
	}

	private long doRefresh(String theResourceName) {
		Class<? extends IBaseResource> resourceType = myFhirContext.getResourceDefinition(theResourceName).getImplementingClass();
		Map<IVersionChangeListener, SearchParameterMap> map = myListenerMap.getListenerMap(theResourceName);
		long count = 0;
		System.out.println("map.isEmpty() = " + map.isEmpty());
		if (map.isEmpty()) {
			// FIXME KBD Ask KHS if this is what he intended for this part...
			ResourceVersionMap resourceVersionMap = myResourceVersionCacheSvc.getVersionLookup(theResourceName, SearchParameterMap.newSynchronous());
			IdDt resourceId = resourceVersionMap.keySet().stream().findFirst().get();
			String resourceVersion = resourceVersionMap.get(resourceId);
			myResourceVersionCache.addOrUpdate(resourceId, resourceVersion);
		} else {
			for (IVersionChangeListener listener : map.keySet()) {
				ResourceVersionMap resourceVersionMap = myResourceVersionCacheSvc.getVersionLookup(theResourceName, map.get(listener));
				count += myListenerNotifier.compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(myResourceVersionCache, resourceVersionMap, listener);
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
			if (myListenerMap.hasListenersForResourceName(resourceName)) {
				synchronized (this) {
					requestRefresh(resourceName);
				}
			}
		}
	}
}
