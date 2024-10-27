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

import ca.uhn.fhir.jpa.api.svc.ISearchUrlJobMaintenanceSvc;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * The purpose of this service is to define and register a job that will clean up
 * entries created by an instance of {@link ResourceSearchUrlSvc}.
 */
public class SearchUrlJobMaintenanceSvcImpl implements ISearchUrlJobMaintenanceSvc, IHasScheduledJobs {

	private ResourceSearchUrlSvc myResourceSearchUrlSvc;

	/**
	 * An hour at 3k resources/second is ~10M resources.  That's easy to manage with deletes by age.
	 * We can shorten this if we have memory or storage pressure.  MUST be longer that longest transaction
	 * possible to work.
	 */
	public static final long OUR_CUTOFF_IN_MILLISECONDS = 1 * DateUtils.MILLIS_PER_HOUR;

	public SearchUrlJobMaintenanceSvcImpl(ResourceSearchUrlSvc theResourceSearchUrlSvc) {
		myResourceSearchUrlSvc = theResourceSearchUrlSvc;
	}

	@Override
	public void removeStaleEntries() {
		final Date cutoffDate = calculateCutoffDate();
		myResourceSearchUrlSvc.deleteEntriesOlderThan(cutoffDate);
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(SearchUrlMaintenanceJob.class.getName());
		jobDetail.setJobClass(SearchUrlMaintenanceJob.class);
		theSchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_MINUTE, jobDetail);
	}

	private Date calculateCutoffDate() {
		return new Date(System.currentTimeMillis() - OUR_CUTOFF_IN_MILLISECONDS);
	}

	public static class SearchUrlMaintenanceJob implements HapiJob {

		@Autowired
		private ISearchUrlJobMaintenanceSvc mySearchUrlJobMaintenanceSvc;

		@Override
		public void execute(JobExecutionContext theJobExecutionContext) throws JobExecutionException {
			mySearchUrlJobMaintenanceSvc.removeStaleEntries();
		}
	}
}
