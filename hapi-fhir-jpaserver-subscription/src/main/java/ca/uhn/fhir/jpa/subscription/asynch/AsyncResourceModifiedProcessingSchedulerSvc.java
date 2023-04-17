package ca.uhn.fhir.jpa.subscription.asynch;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This service is responsible for scheduling a job that will process messages
 * where submission to the subscription pipeline previously failed.
 */
public class AsyncResourceModifiedProcessingSchedulerSvc implements IHasScheduledJobs {

	public static final long ONE_MINUTE = 60 * DateUtils.MILLIS_PER_SECOND;

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(AsyncResourceModifiedProcessingSchedulerSvc.Job.class);

		theSchedulerService.scheduleClusteredJob(ONE_MINUTE, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private AsyncResourceModifiedSubmitterSvc myAsyncResourceModifiedSubmitterSvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		}
	}


}
