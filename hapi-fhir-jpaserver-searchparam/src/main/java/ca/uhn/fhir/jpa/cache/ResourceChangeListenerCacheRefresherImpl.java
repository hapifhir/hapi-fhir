package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Iterator;

@Service
public class ResourceChangeListenerCacheRefresherImpl implements IResourceChangeListenerCacheRefresher {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCacheRefresherImpl.class);

	static long LOCAL_REFRESH_INTERVAL_MS = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final int MAX_RETRIES = 60; // 5 minutes

	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@PostConstruct
	public void start() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(LOCAL_REFRESH_INTERVAL_MS, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IResourceChangeListenerCacheRefresher myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshAllCachesIfNecessary();
		}
	}

	@Override
	public ResourceChangeResult refreshAllCachesIfNecessary() {
		ResourceChangeResult retval = new ResourceChangeResult();
		Iterator<RegisteredResourceChangeListener> iterator = myResourceChangeListenerRegistry.iterator();
		while (iterator.hasNext()) {
			RegisteredResourceChangeListener entry = iterator.next();
			retval = retval.plus(entry.refreshCacheIfNecessary());
		}
		return retval;
	}

	@VisibleForTesting
	public ResourceChangeResult forceRefreshAllCachesForUnitTest() {
		ResourceChangeResult retval = new ResourceChangeResult();
		Iterator<RegisteredResourceChangeListener> iterator = myResourceChangeListenerRegistry.iterator();
		while (iterator.hasNext()) {
			RegisteredResourceChangeListener entry = iterator.next();
			retval = retval.plus(entry.forceRefresh());
		}
		return retval;
	}

	@Override
	public void requestRefreshIfWatching(IBaseResource theResource) {
		myResourceChangeListenerRegistry.requestRefreshIfWatching(theResource);
	}

	@Override
	public ResourceChangeResult refreshCacheWithRetry(RegisteredResourceChangeListener theEntry) {
		Retrier<ResourceChangeResult> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshCachesAndNotifyListeners(theEntry);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	private synchronized ResourceChangeResult doRefreshCachesAndNotifyListeners(RegisteredResourceChangeListener theEntry) {
		ResourceChangeResult retval = new ResourceChangeResult();
		if (!myResourceChangeListenerRegistry.contains(theEntry)) {
			ourLog.warn("Requesting cache refresh for unregistered listener {}", theEntry);
		}
		SearchParameterMap searchParamMap = theEntry.getSearchParameterMap();
		ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theEntry.getResourceName(), searchParamMap);
		retval = retval.plus(myResourceChangeListenerRegistry.notifyListener(theEntry, newResourceVersionMap));

		return retval;
	}
}
