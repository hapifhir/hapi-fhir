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
import ca.uhn.fhir.batch2.api.IWorkChunkPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater;
import ca.uhn.fhir.batch2.util.BatchJobOpenTelemetryUtils;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Logs;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Nonnull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static ca.uhn.fhir.batch2.util.BatchJobOpenTelemetryUtils.JOB_STEP_EXECUTION_SPAN_NAME;

public class JobStepExecutor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private static final String CHUNK_ID = "chunk-id";

	private final IJobPersistence myJobPersistence;
	private final WorkChunkProcessor myJobExecutorSvc;
	private final IJobMaintenanceService myJobMaintenanceService;
	private final JobInstanceStatusUpdater myJobInstanceStatusUpdater;

	private final JobDefinition<PT> myDefinition;
	private final JobInstance myInstance;
	private final String myInstanceId;
	private final WorkChunk myWorkChunk;
	private final JobWorkCursor<PT, IT, OT> myCursor;
	private final ISchedulerService myIHapiScheduler;

	private final long myAckTimeoutMs;

	JobStepExecutor(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull JobInstance theInstance,
			WorkChunk theWorkChunk,
			@Nonnull JobWorkCursor<PT, IT, OT> theCursor,
			@Nonnull WorkChunkProcessor theExecutor,
			@Nonnull IJobMaintenanceService theJobMaintenanceService,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull IInterceptorService theInterceptorService,
			ISchedulerService theScheduler,
			long theAckTimeoutMs) {
		myJobPersistence = theJobPersistence;
		myDefinition = theCursor.jobDefinition;
		myInstance = theInstance;
		myInstanceId = theInstance.getInstanceId();
		myWorkChunk = theWorkChunk;
		myCursor = theCursor;
		myJobExecutorSvc = theExecutor;
		myJobMaintenanceService = theJobMaintenanceService;
		myJobInstanceStatusUpdater = new JobInstanceStatusUpdater(theJobDefinitionRegistry, theInterceptorService);
		myIHapiScheduler = theScheduler;
		myAckTimeoutMs = theAckTimeoutMs;
	}

	private void scheduleHeartbeat() {
		ScheduledJobDefinition definition = new ScheduledJobDefinition();
		definition.setJobClass(HeartbeatJob.class);
		definition.setId(String.format("BATCH2-HEARTBEAT-%s-%s", myInstanceId, myWorkChunk.getId()));
		definition.addJobData(CHUNK_ID, myWorkChunk.getId());
		myIHapiScheduler.scheduleClusteredJob(Math.min(myAckTimeoutMs / 3, 500), definition);
	}

	@WithSpan(JOB_STEP_EXECUTION_SPAN_NAME)
	public void processstep() {
		BatchJobOpenTelemetryUtils.addAttributesToCurrentSpan(
				myInstance.getJobDefinitionId(),
				myInstance.getJobDefinitionVersion(),
				myInstance.getInstanceId(),
				myCursor.getCurrentStepId(),
				myWorkChunk == null ? null : myWorkChunk.getId());

		scheduleHeartbeat();

		JobStepExecutorOutput<PT, IT, OT> stepExecutorOutput =
				myJobExecutorSvc.doExecution(myCursor, myInstance, myWorkChunk);

		if (!stepExecutorOutput.isSuccessful()) {
			return;
		}

		/**
		 * Jobs are completed in {@link ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator#calculateInstanceProgress}
		 * We determine if the job is complete based on if there are *any* completed work chunks.
		 * So if there are no COMPLETED work chunks (ie, first step produces no work chunks)
		 * we must complete it here.
		 */
		if (stepExecutorOutput.getDataSink().firstStepProducedNothing() && !myDefinition.isLastStepReduction()) {
			ourLog.info(
					"First step of job myInstance {} produced no work chunks and last step is not a reduction, "
							+ "marking as completed and setting end date",
					myInstanceId);
			myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
				instance.setEndTime(new Date());
				myJobInstanceStatusUpdater.updateInstanceStatus(instance, StatusEnum.COMPLETED);
				return true;
			});
		}

		// This flag could be stale, but checking for fast-track is a safe operation.
		if (myInstance.isFastTracking()) {
			handleFastTracking(stepExecutorOutput.getDataSink());
		}
	}

	private void handleFastTracking(BaseDataSink<PT, IT, OT> theDataSink) {
		if (theDataSink.getWorkChunkCount() <= 1) {
			ourLog.debug(
					"Gated job {} step {} produced exactly one chunk:  Triggering a maintenance pass.",
					myDefinition.getJobDefinitionId(),
					myCursor.currentStep.getStepId());
			// wipmb 6.8 either delete fast-tracking, or narrow this call to just this instance and step
			// This runs full maintenance for EVERY job as each chunk completes in a fast tracked job.  That's a LOT of
			// work.
			boolean success = myJobMaintenanceService.triggerMaintenancePass();
			if (!success) {
				myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
					instance.setFastTracking(false);
					return true;
				});
			}
		} else {
			ourLog.debug(
					"Gated job {} step {} produced {} chunks:  Disabling fast tracking.",
					myDefinition.getJobDefinitionId(),
					myCursor.currentStep.getStepId(),
					theDataSink.getWorkChunkCount());
			myJobPersistence.updateInstance(myInstance.getInstanceId(), instance -> {
				instance.setFastTracking(false);
				return true;
			});
		}
	}

	public static class HeartbeatJob implements HapiJob {

		@Autowired
		private IWorkChunkPersistence myWorkChunkPersistence;

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			String workchunkId = (String) context.getMergedJobDataMap().get(CHUNK_ID);

			WorkChunkStatusEnum status = myWorkChunkPersistence.getWorkChunkStatus(workchunkId);
			ourLog.info("WorkChunk {} has status {}", workchunkId, status.name());

			if (status == WorkChunkStatusEnum.IN_PROGRESS) {
				myWorkChunkPersistence.onWorkChunkHeartbeat(workchunkId);
			} else {
				try {
					context.getScheduler().unscheduleJob(context.getTrigger().getKey());
				} catch (SchedulerException ex) {
					ourLog.info("Failed to unschedule job {}", getClass().getName(), ex);
					throw new RuntimeException(ex);
				}
			}
		}
	}
}
