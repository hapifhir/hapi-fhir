/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

import java.time.Duration;

public class JobStepExecutorFactory {

	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final WorkChunkProcessor myJobStepExecutorSvc;
	private final IJobMaintenanceService myJobMaintenanceService;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final IInterceptorService myInterceptorService;
	private final ISchedulerService myIHapiScheduler;

	private Duration myAckTimeout;

	public JobStepExecutorFactory(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull BatchJobSender theBatchJobSender,
			@Nonnull WorkChunkProcessor theExecutorSvc,
			@Nonnull IJobMaintenanceService theJobMaintenanceService,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull IInterceptorService theInterceptorService,
			ISchedulerService theScheduler) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myJobStepExecutorSvc = theExecutorSvc;
		myJobMaintenanceService = theJobMaintenanceService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myInterceptorService = theInterceptorService;
		myIHapiScheduler = theScheduler;
	}

	public void setAckTimeout(Duration theAckTimeout) {
		myAckTimeout = theAckTimeout;
	}

	/**
	 * Time before message redelivery.
	 *
	 * If this isn't set by the broker (or the broker value is
	 * extremely low), a default value of 1001ms will be
	 * returned.
	 * (the '1ms' is deliberate in hopes to return a value that is
	 * hopefully not set by users in case we see it in logs)
	 */
	public @Nonnull Duration getAckTimeout() {
		if (myAckTimeout == null || myAckTimeout.toMillis() < 500) {
			myAckTimeout = Duration.ofMillis(1001);
		}
		return myAckTimeout;
	}

	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
			JobStepExecutor<PT, IT, OT> newJobStepExecutor(
					@Nonnull JobInstance theInstance,
					WorkChunk theWorkChunk,
					@Nonnull JobWorkCursor<PT, IT, OT> theCursor) {
		return new JobStepExecutor<>(
				myJobPersistence,
				theInstance,
				theWorkChunk,
				theCursor,
				myJobStepExecutorSvc,
				myJobMaintenanceService,
				myJobDefinitionRegistry,
				myInterceptorService,
				myIHapiScheduler,
				getAckTimeout());
	}
}
