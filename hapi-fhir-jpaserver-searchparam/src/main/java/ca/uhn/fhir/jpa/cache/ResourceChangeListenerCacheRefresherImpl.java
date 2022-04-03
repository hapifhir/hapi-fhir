package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
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
 *
 * Think of it like a Ferris Wheel that completes a full rotation once every 10 seconds.
 * Every time a chair passes the bottom it checks to see if it's time to refresh that seat.  If so,
 * the Ferris Wheel stops, removes the riders, and loads a fresh cache for that chair, and calls the listener
 * if any entries in the new cache are different from the last time that cache was loaded.
 */
@Service
public class ResourceChangeListenerCacheRefresherImpl implements IResourceChangeListenerCacheRefresher {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCacheRefresherImpl.class);

	/**
	 * All cache entries are checked at this interval to see if they need to be refreshed
	 */
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

	@VisibleForTesting
	public void setSchedulerService(ISchedulerService theSchedulerService) {
		mySchedulerService = theSchedulerService;
	}

	@VisibleForTesting
	public void setResourceChangeListenerRegistry(ResourceChangeListenerRegistryImpl theResourceChangeListenerRegistry) {
		myResourceChangeListenerRegistry = theResourceChangeListenerRegistry;
	}

	@VisibleForTesting
	public void setResourceVersionSvc(IResourceVersionSvc theResourceVersionSvc) {
		myResourceVersionSvc = theResourceVersionSvc;
	}

	@Override
	public ResourceChangeResult refreshCacheAndNotifyListener(IResourceChangeListenerCache theCache) {
		ResourceChangeResult retVal = new ResourceChangeResult();
		if (mySchedulerService.isStopping()) {
			ourLog.info("Scheduler service is stopping, aborting cache refresh");
			return retVal;
		}
		if (!myResourceChangeListenerRegistry.contains(theCache)) {
			ourLog.warn("Requesting cache refresh for unregistered listener {}.  Aborting.", theCache);
			return retVal;
		}
		SearchParameterMap searchParamMap = theCache.getSearchParameterMap();
		ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theCache.getResourceName(), searchParamMap);
		retVal = retVal.plus(notifyListener(theCache, newResourceVersionMap));

		return retVal;
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
			Long previousValue = theOldResourceVersionCache.put(id, theNewResourceVersionMap.get(id));
			IIdType newId = id.withVersion(theNewResourceVersionMap.get(id).toString());
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
