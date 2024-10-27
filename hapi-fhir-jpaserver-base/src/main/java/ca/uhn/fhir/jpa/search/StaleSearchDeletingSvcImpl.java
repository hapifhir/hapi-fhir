/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl.SEARCH_CLEANUP_JOB_INTERVAL_MILLIS;

/**
 * Deletes old searches
 */
//
// NOTE: This is not a @Service because we manually instantiate
// it in BaseConfig. This is so that we can override the definition
// in Smile.
//
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc, IHasScheduledJobs {

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void pollForStaleSearchesAndDeleteThem() {
		mySearchCacheSvc.pollForStaleSearchesAndDeleteThem(RequestPartitionId.allPartitions(), getDeadline());
	}

	/**
	 * Calculate a deadline to finish before the next scheduled run.
	 */
	protected Instant getDeadline() {
		return Instant.ofEpochMilli(DatabaseSearchCacheSvcImpl.now())
				// target a 90% duty-cycle to avoid confusing quartz
				.plus((long) (SEARCH_CLEANUP_JOB_INTERVAL_MILLIS * 0.90), ChronoUnit.MILLIS);
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		theSchedulerService.scheduleClusteredJob(SEARCH_CLEANUP_JOB_INTERVAL_MILLIS, jobDetail);
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
		if (!myStorageSettings.isSchedulingDisabled() && myStorageSettings.isEnableTaskStaleSearchCleanup()) {
			pollForStaleSearchesAndDeleteThem();
		}
	}
}
