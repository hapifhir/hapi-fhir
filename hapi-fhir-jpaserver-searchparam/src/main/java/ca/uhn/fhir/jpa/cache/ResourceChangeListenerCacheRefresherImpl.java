package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		retval = retval.plus(notifyListener(theEntry, newResourceVersionMap));

		return retval;
	}

	/**
	 * Notify a listener with all matching resources if it hasn't been initialized yet, otherwise only notify it if
	 * any resources have changed
	 * @param theListenerEntry
	 * @param theNewResourceVersionMap the measured new resources
	 * @return the list of created, updated and deleted ids
	 */
	// FIXME KHS move notification stuff out to its own service
	ResourceChangeResult notifyListener(RegisteredResourceChangeListener theListenerEntry, ResourceVersionMap theNewResourceVersionMap) {
		ResourceChangeResult retval;
		IResourceChangeListener resourceChangeListener = theListenerEntry.getResourceChangeListener();
		if (theListenerEntry.isInitialized()) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(resourceChangeListener, theListenerEntry.getResourceVersionCache(), theNewResourceVersionMap);
		} else {
			theListenerEntry.getResourceVersionCache().initialize(theNewResourceVersionMap);
			resourceChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = ResourceChangeResult.fromCreated(theNewResourceVersionMap.size());
			theListenerEntry.setInitialized(true);
		}
		return retval;
	}

	private ResourceChangeResult compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(IResourceChangeListener theListener, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		// If the new ResourceVersionMap does not have the old key - delete it
		List<IIdType> deletedIds = new ArrayList<>();
		theOldResourceVersionCache.keySet()
			.forEach(id -> {
				if (!theNewResourceVersionMap.containsKey(id)) {
					deletedIds.add(id);
				}
			});
		deletedIds.forEach(theOldResourceVersionCache::removeResourceId);

		List<IIdType> createdIds = new ArrayList<>();
		List<IIdType> updatedIds = new ArrayList<>();

		for (IIdType id : theNewResourceVersionMap.keySet()) {
			String previousValue = theOldResourceVersionCache.put(id, theNewResourceVersionMap.get(id));
			IIdType newId = id.withVersion(theNewResourceVersionMap.get(id));
			if (previousValue == null) {
				createdIds.add(newId);
			} else if (!theNewResourceVersionMap.get(id).equals(previousValue)) {
				updatedIds.add(newId);
			}
		}

		IResourceChangeEvent resourceChangeEvent = ResourceChangeEvent.fromCreatedUpdatedDeletedResourceIds(createdIds, updatedIds, deletedIds);
		if (!resourceChangeEvent.isEmpty()) {
			theListener.handleChange(resourceChangeEvent);
		}
		return ResourceChangeResult.fromResourceChangeEvent(resourceChangeEvent);
	}
}
