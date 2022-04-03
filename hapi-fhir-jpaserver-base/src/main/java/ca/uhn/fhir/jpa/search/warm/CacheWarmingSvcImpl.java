package ca.uhn.fhir.jpa.search.warm;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.WarmCacheEntry;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CacheWarmingSvcImpl implements ICacheWarmingSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(CacheWarmingSvcImpl.class);
	@Autowired
	private DaoConfig myDaoConfig;
	private Map<WarmCacheEntry, Long> myCacheEntryToNextRefresh = new LinkedHashMap<>();
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private ISchedulerService mySchedulerService;

	@Override
	public synchronized void performWarmingPass() {
		ourLog.trace("Starting cache warming pass for {} tasks", myCacheEntryToNextRefresh.size());

		for (WarmCacheEntry nextCacheEntry : new ArrayList<>(myCacheEntryToNextRefresh.keySet())) {

			long nextRefresh = myCacheEntryToNextRefresh.get(nextCacheEntry);
			if (nextRefresh < System.currentTimeMillis()) {

				// Perform the search
				refreshNow(nextCacheEntry);

				// Set the next time to warm this search
				nextRefresh = nextCacheEntry.getPeriodMillis() + System.currentTimeMillis();
				myCacheEntryToNextRefresh.put(nextCacheEntry, nextRefresh);

			}

		}

	}

	private void refreshNow(WarmCacheEntry theCacheEntry) {
		String nextUrl = theCacheEntry.getUrl();

		RuntimeResourceDefinition resourceDef = UrlUtil.parseUrlResourceType(myCtx, nextUrl);
		IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(resourceDef.getName());
		String queryPart = parseWarmUrlParamPart(nextUrl);
		SearchParameterMap responseCriteriaUrl = myMatchUrlService.translateMatchUrl(queryPart, resourceDef);

		callingDao.search(responseCriteriaUrl);
	}

	private String parseWarmUrlParamPart(String theNextUrl) {
		int paramIndex = theNextUrl.indexOf('?');
		if (paramIndex == -1) {
			throw new ConfigurationException(Msg.code(1172) + "Invalid warm cache URL (must have ? character)");
		}
		return theNextUrl.substring(paramIndex);
	}

	@PostConstruct
	public void start() {
		initCacheMap();
		scheduleJob();
	}

	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ICacheWarmingSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.performWarmingPass();
		}
	}

	public synchronized Set<WarmCacheEntry> initCacheMap() {

		myCacheEntryToNextRefresh.clear();
		List<WarmCacheEntry> warmCacheEntries = myDaoConfig.getWarmCacheEntries();
		for (WarmCacheEntry next : warmCacheEntries) {

			// Validate
			parseWarmUrlParamPart(next.getUrl());
			UrlUtil.parseUrlResourceType(myCtx, next.getUrl());

			myCacheEntryToNextRefresh.put(next, 0L);
		}

		return Collections.unmodifiableSet(myCacheEntryToNextRefresh.keySet());
	}
}
