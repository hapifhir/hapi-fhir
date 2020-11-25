package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
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

/**
 * This service refreshes the {@link IResourceChangeListenerCache} caches and notifies their listener when
 * those caches change.
 */
@Service
public class ResourceChangeListenerCacheRefresherImpl implements IResourceChangeListenerCacheRefresher {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCacheRefresherImpl.class);

	static long LOCAL_REFRESH_INTERVAL_MS = 10 * DateUtils.MILLIS_PER_SECOND;

	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;

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
			myTarget.refreshExpiredCachesAndNotifyListeners();
		}
	}

	@Override
	public ResourceChangeResult refreshExpiredCachesAndNotifyListeners() {
		ResourceChangeResult retval = new ResourceChangeResult();
		Iterator<ResourceChangeListenerCache> iterator = myResourceChangeListenerRegistry.iterator();
		while (iterator.hasNext()) {
			ResourceChangeListenerCache entry = iterator.next();
			retval = retval.plus(entry.refreshCacheIfNecessary());
		}
		return retval;
	}

	@VisibleForTesting
	public ResourceChangeResult forceRefreshAllCachesForUnitTest() {
		ResourceChangeResult retval = new ResourceChangeResult();
		Iterator<ResourceChangeListenerCache> iterator = myResourceChangeListenerRegistry.iterator();
		while (iterator.hasNext()) {
			IResourceChangeListenerCache entry = iterator.next();
			retval = retval.plus(entry.forceRefresh());
		}
		return retval;
	}

	@Override
	public void requestRefreshIfWatching(IBaseResource theResource) {
		myResourceChangeListenerRegistry.requestRefreshIfWatching(theResource);
	}

	public ResourceChangeResult refreshCacheAndNotifyListener(IResourceChangeListenerCache theCache) {
		ResourceChangeResult retval = new ResourceChangeResult();
		if (!myResourceChangeListenerRegistry.contains(theCache)) {
			ourLog.warn("Requesting cache refresh for unregistered listener {}.  Aborting.", theCache);
			return new ResourceChangeResult();
		}
		SearchParameterMap searchParamMap = theCache.getSearchParameterMap();
		ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theCache.getResourceName(), searchParamMap);
		retval = retval.plus(notifyListener(theCache, newResourceVersionMap));

		return retval;
	}

	/**
	 * Notify a listener with all matching resources if it hasn't been initialized yet, otherwise only notify it if
	 * any resources have changed
	 * @param theCache
	 * @param theNewResourceVersionMap the measured new resources
	 * @return the list of created, updated and deleted ids
	 */
	ResourceChangeResult notifyListener(IResourceChangeListenerCache theCache, ResourceVersionMap theNewResourceVersionMap) {
		ResourceChangeResult retval;
		ResourceChangeListenerCache cache = (ResourceChangeListenerCache) theCache;
		IResourceChangeListener resourceChangeListener = cache.getResourceChangeListener();
		if (theCache.isInitialized()) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(resourceChangeListener, cache.getResourceVersionCache(), theNewResourceVersionMap);
		} else {
			cache.getResourceVersionCache().initialize(theNewResourceVersionMap);
			resourceChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = ResourceChangeResult.fromCreated(theNewResourceVersionMap.size());
			cache.setInitialized(true);
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
