package ca.uhn.fhir.jpa.search;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl.SEARCH_CLEANUP_JOB_INTERVAL_MILLIS;

/**
 * Deletes old searches
 */
//
// NOTE: This is not a @Service because we manually instantiate
// it in BaseConfig. This is so that we can override the definition
// in Smile.
//
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcImpl.class);
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	private ISchedulerService mySchedulerService;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void pollForStaleSearchesAndDeleteThem() {
		mySearchCacheSvc.pollForStaleSearchesAndDeleteThem();
	}

	@PostConstruct
	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(SEARCH_CLEANUP_JOB_INTERVAL_MILLIS, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IStaleSearchDeletingSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.schedulePollForStaleSearches();
		}
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void schedulePollForStaleSearches() {
		if (!myDaoConfig.isSchedulingDisabled() && myDaoConfig.isEnableTaskStaleSearchCleanup()) {
			pollForStaleSearchesAndDeleteThem();
		}
	}
}
