package ca.uhn.fhir.jpa.subscription.async;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This service is responsible for scheduling a job that will submit messages
 * to the subscription processing pipeline at a given interval.
 */
public class AsyncResourceModifiedProcessingSchedulerSvc implements IHasScheduledJobs {

	public static final long DEFAULT_SUBMISSION_INTERVAL_IN_MS = 5000;

	public long mySubmissionIntervalInMilliSeconds;

	public AsyncResourceModifiedProcessingSchedulerSvc() {
		this(DEFAULT_SUBMISSION_INTERVAL_IN_MS);
	}

	public AsyncResourceModifiedProcessingSchedulerSvc(long theSubmissionIntervalInMilliSeconds) {
		mySubmissionIntervalInMilliSeconds = theSubmissionIntervalInMilliSeconds;
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(AsyncResourceModifiedProcessingSchedulerSvc.Job.class);

		theSchedulerService.scheduleClusteredJob(mySubmissionIntervalInMilliSeconds, jobDetail);
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
