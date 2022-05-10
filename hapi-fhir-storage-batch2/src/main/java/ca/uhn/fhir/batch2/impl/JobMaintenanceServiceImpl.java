package ca.uhn.fhir.batch2.impl;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class performs regular polls of the stored jobs in order to
 * perform maintenance. This includes two major functions.
 *
 * <p>
 * First, we calculate statistics and delete expired tasks. This class does
 * the following things:
 * <ul>
 *    <li>For instances that are IN_PROGRESS, calculates throughput and percent complete</li>
 *    <li>For instances that are IN_PROGRESS where all chunks are COMPLETE, marks instance as COMPLETE</li>
 *    <li>For instances that are COMPLETE, purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS where at least one chunk is FAILED, marks instance as FAILED and propagates the error message to the instance, and purges chunk data</li>
 *    <li>For instances that are IN_PROGRESS with an error message set where no chunks are ERRORED or FAILED, clears the error message in the instance (meaning presumably there was an error but it cleared)</li>
 *    <li>For instances that are IN_PROGRESS and isCancelled flag is set marks them as ERRORED and indicating the current running step if any</li>
 *    <li>For instances that are COMPLETE or FAILED and are old, delete them entirely</li>
 * </ul>
 * 	</p>
 *
 * 	<p>
 * Second, we check for any job instances where the job is configured to
 * have gated execution. For these instances, we check if the current step
 * is complete (all chunks are in COMPLETE status) and trigger the next step.
 * </p>
 */
public class JobMaintenanceServiceImpl extends BaseJobService implements IJobMaintenanceService {

	public static final int INSTANCES_PER_PASS = 100;
	public static final long PURGE_THRESHOLD = 7L * DateUtils.MILLIS_PER_DAY;
	private static final Logger ourLog = LoggerFactory.getLogger(JobMaintenanceServiceImpl.class);
	private final ISchedulerService mySchedulerService;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;


	/**
	 * Constructor
	 */
	public JobMaintenanceServiceImpl(ISchedulerService theSchedulerService, IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		super(theJobPersistence);
		Validate.notNull(theSchedulerService);
		Validate.notNull(theJobDefinitionRegistry);
		Validate.notNull(theBatchJobSender);

		mySchedulerService = theSchedulerService;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
	}

	@PostConstruct
	public void start() {
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(JobMaintenanceScheduledJob.class.getName());
		jobDefinition.setJobClass(JobMaintenanceScheduledJob.class);
		mySchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_MINUTE, jobDefinition);
	}

	@Override
	public void runMaintenancePass() {

		// NB: If you add any new logic, update the class javadoc

		Set<String> processedInstanceIds = new HashSet<>();
		JobChunkProgressAccumulator progressAccumulator = new JobChunkProgressAccumulator();
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);

			for (JobInstance instance : instances) {
				if (processedInstanceIds.add(instance.getInstanceId())) {
					handleCancellation(instance);
					cleanupInstance(instance, progressAccumulator);
					triggerGatedExecutions(instance, progressAccumulator);
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
	}

	private void handleCancellation(JobInstance theInstance) {
		if (! theInstance.isCancelled()) { return; }

		if (theInstance.getStatus() == StatusEnum.QUEUED || theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			String msg = "Job instance cancelled";
			if (theInstance.getCurrentGatedStepId() != null) {
				msg += " while running step " + theInstance.getCurrentGatedStepId();
			}
			theInstance.setErrorMessage(msg);
			theInstance.setStatus(StatusEnum.CANCELLED);
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private void cleanupInstance(JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		switch (theInstance.getStatus()) {
			case QUEUED:
				break;
			case IN_PROGRESS:
			case ERRORED:
				calculateInstanceProgress(theInstance, theProgressAccumulator);
				break;
			case COMPLETED:
			case FAILED:
			case CANCELLED:
				if (theInstance.getEndTime() != null) {
					long cutoff = System.currentTimeMillis() - PURGE_THRESHOLD;
					if (theInstance.getEndTime().getTime() < cutoff) {
						ourLog.info("Deleting old job instance {}", theInstance.getInstanceId());
						myJobPersistence.deleteInstanceAndChunks(theInstance.getInstanceId());
						return;
					}
				}
				break;
		}

		if ((theInstance.getStatus() == StatusEnum.COMPLETED || theInstance.getStatus() == StatusEnum.FAILED
				|| theInstance.getStatus() == StatusEnum.CANCELLED) && !theInstance.isWorkChunksPurged()) {
			theInstance.setWorkChunksPurged(true);
			myJobPersistence.deleteChunks(theInstance.getInstanceId());
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private void calculateInstanceProgress(JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		int resourcesProcessed = 0;
		int incompleteChunkCount = 0;
		int completeChunkCount = 0;
		int erroredChunkCount = 0;
		int failedChunkCount = 0;
		int errorCountForAllStatuses = 0;
		Long earliestStartTime = null;
		Long latestEndTime = null;
		String errorMessage = null;
		for (int page = 0; ; page++) {
			List<WorkChunk> chunks = myJobPersistence.fetchWorkChunksWithoutData(theInstance.getInstanceId(), INSTANCES_PER_PASS, page);

			for (WorkChunk chunk : chunks) {
				theProgressAccumulator.addChunk(chunk.getInstanceId(), chunk.getId(), chunk.getTargetStepId(), chunk.getStatus());

				errorCountForAllStatuses += chunk.getErrorCount();

				if (chunk.getRecordsProcessed() != null) {
					resourcesProcessed += chunk.getRecordsProcessed();
				}
				if (chunk.getStartTime() != null) {
					if (earliestStartTime == null || earliestStartTime > chunk.getStartTime().getTime()) {
						earliestStartTime = chunk.getStartTime().getTime();
					}
				}
				if (chunk.getEndTime() != null) {
					if (latestEndTime == null || latestEndTime < chunk.getEndTime().getTime()) {
						latestEndTime = chunk.getEndTime().getTime();
					}
				}
				switch (chunk.getStatus()) {
					case QUEUED:
					case IN_PROGRESS:
						incompleteChunkCount++;
						break;
					case COMPLETED:
						completeChunkCount++;
						break;
					case ERRORED:
						erroredChunkCount++;
						if (errorMessage == null) {
							errorMessage = chunk.getErrorMessage();
						}
						break;
					case FAILED:
						failedChunkCount++;
						errorMessage = chunk.getErrorMessage();
						break;
					case CANCELLED:
						break;
				}
			}

			if (chunks.size() < INSTANCES_PER_PASS) {
				break;
			}
		}

		if (earliestStartTime != null) {
			theInstance.setStartTime(new Date(earliestStartTime));
		}
		theInstance.setErrorCount(errorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(resourcesProcessed);

		boolean changedStatus = false;
		if (completeChunkCount > 1 || erroredChunkCount > 1) {

			double percentComplete = (double) (completeChunkCount) / (double) (incompleteChunkCount + completeChunkCount + failedChunkCount + erroredChunkCount);
			theInstance.setProgress(percentComplete);

			changedStatus = false;
			if (incompleteChunkCount == 0 && erroredChunkCount == 0 && failedChunkCount == 0) {
				boolean completed = updateInstanceStatus(theInstance, StatusEnum.COMPLETED);
				if (completed) {
					JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion()).orElseThrow(() -> new IllegalStateException("Unknown job " + theInstance.getJobDefinitionId() + "/" + theInstance.getJobDefinitionVersion()));
					invokeJobCompletionHandler(theInstance, definition);
				}
				changedStatus |= completed;
			}
			if (erroredChunkCount > 0) {
				changedStatus |= updateInstanceStatus(theInstance, StatusEnum.ERRORED);
			}

			if (earliestStartTime != null && latestEndTime != null) {
				long elapsedTime = latestEndTime - earliestStartTime;
				if (elapsedTime > 0) {
					double throughput = StopWatch.getThroughput(resourcesProcessed, elapsedTime, TimeUnit.SECONDS);
					theInstance.setCombinedRecordsProcessedPerSecond(throughput);

					String estimatedTimeRemaining = StopWatch.formatEstimatedTimeRemaining(completeChunkCount, (completeChunkCount + incompleteChunkCount), elapsedTime);
					theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
				}
			}

		}

		if (latestEndTime != null) {
			if (failedChunkCount > 0) {
				theInstance.setEndTime(new Date(latestEndTime));
			} else if (completeChunkCount > 0 && incompleteChunkCount == 0 && erroredChunkCount == 0) {
				theInstance.setEndTime(new Date(latestEndTime));
			}
		}

		theInstance.setErrorMessage(errorMessage);

		if (changedStatus || theInstance.getStatus() == StatusEnum.IN_PROGRESS) {
			ourLog.info("Job {} of type {} has status {} - {} records processed ({}/sec) - ETA: {}", theInstance.getInstanceId(), theInstance.getJobDefinitionId(), theInstance.getStatus(), theInstance.getCombinedRecordsProcessed(), theInstance.getCombinedRecordsProcessedPerSecond(), theInstance.getEstimatedTimeRemaining());
		}

		if (failedChunkCount > 0) {
			updateInstanceStatus(theInstance, StatusEnum.FAILED);
			myJobPersistence.updateInstance(theInstance);
			return;
		}

		if ((incompleteChunkCount + completeChunkCount + erroredChunkCount) >= 2 || errorCountForAllStatuses > 0) {
			myJobPersistence.updateInstance(theInstance);
		}

	}

	private <PT extends IModelJson> void invokeJobCompletionHandler(JobInstance theInstance, JobDefinition<PT> definition) {
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {

			String instanceId = theInstance.getInstanceId();
			PT jobParameters = theInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}

	private boolean updateInstanceStatus(JobInstance theInstance, StatusEnum newStatus) {
		if (theInstance.getStatus() != newStatus) {
			ourLog.info("Marking job instance {} of type {} as {}", theInstance.getInstanceId(), theInstance.getJobDefinitionId(), newStatus);
			theInstance.setStatus(newStatus);
			return true;
		}
		return false;
	}

	private void triggerGatedExecutions(JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		if (!theInstance.isRunning()) {
			return;
		}

		String jobDefinitionId = theInstance.getJobDefinitionId();
		int jobDefinitionVersion = theInstance.getJobDefinitionVersion();
		String instanceId = theInstance.getInstanceId();

		JobDefinition<?> definition = myJobDefinitionRegistry.getJobDefinition(jobDefinitionId, jobDefinitionVersion).orElseThrow(() -> new IllegalStateException("Unknown job definition: " + jobDefinitionId + " " + jobDefinitionVersion));
		if (!definition.isGatedExecution()) {
			return;
		}

		String currentStepId = theInstance.getCurrentGatedStepId();
		if (isBlank(currentStepId)) {
			return;
		}

		if (definition.isLastStep(currentStepId)) {
			return;
		}

		int incompleteChunks = theProgressAccumulator.countChunksWithStatus(instanceId, currentStepId, StatusEnum.getIncompleteStatuses());
		if (incompleteChunks == 0) {

			int currentStepIndex = definition.getStepIndex(currentStepId);
			String nextStepId = definition.getSteps().get(currentStepIndex + 1).getStepId();

			ourLog.info("All processing is complete for gated execution of instance {} step {}. Proceeding to step {}", instanceId, currentStepId, nextStepId);
			List<String> chunksForNextStep = theProgressAccumulator.getChunkIdsWithStatus(instanceId, nextStepId, EnumSet.of(StatusEnum.QUEUED));
			for (String nextChunkId : chunksForNextStep) {
				JobWorkNotification workNotification = new JobWorkNotification(jobDefinitionId, jobDefinitionVersion, instanceId, nextStepId, nextChunkId);
				myBatchJobSender.sendWorkChannelMessage(workNotification);
			}

			theInstance.setCurrentGatedStepId(nextStepId);
			myJobPersistence.updateInstance(theInstance);
		}

	}


	public static class JobMaintenanceScheduledJob implements HapiJob {
		@Autowired
		private IJobMaintenanceService myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runMaintenancePass();
		}
	}


	/**
	 * While performing cleanup, the cleanup job loads all of the known
	 * work chunks to examine their status. This bean collects the counts that
	 * are found, so that they can be reused for maintenance jobs without
	 * needing to hit the database a second time.
	 */
	private static class JobChunkProgressAccumulator {

		private final Set<String> myConsumedInstanceAndChunkIds = new HashSet<>();
		private final Multimap<String, ChunkStatusCountKey> myInstanceIdToChunkStatuses = ArrayListMultimap.create();

		public void addChunk(String theInstanceId, String theChunkId, String theStepId, StatusEnum theStatus) {
			// Note: If chunks are being written while we're executing, we may see the same chunk twice. This
			// check avoids adding it twice.
			if (myConsumedInstanceAndChunkIds.add(theInstanceId + " " + theChunkId)) {
				myInstanceIdToChunkStatuses.put(theInstanceId, new ChunkStatusCountKey(theChunkId, theStepId, theStatus));
			}
		}

		public int countChunksWithStatus(String theInstanceId, String theStepId, Set<StatusEnum> theStatuses) {
			return getChunkIdsWithStatus(theInstanceId, theStepId, theStatuses).size();
		}

		public List<String> getChunkIdsWithStatus(String theInstanceId, String theStepId, Set<StatusEnum> theStatuses) {
			return getChunkStatuses(theInstanceId).stream().filter(t -> t.myStepId.equals(theStepId)).filter(t -> theStatuses.contains(t.myStatus)).map(t -> t.myChunkId).collect(Collectors.toList());
		}

		@Nonnull
		private Collection<ChunkStatusCountKey> getChunkStatuses(String theInstanceId) {
			Collection<ChunkStatusCountKey> chunkStatuses = myInstanceIdToChunkStatuses.get(theInstanceId);
			chunkStatuses = defaultIfNull(chunkStatuses, emptyList());
			return chunkStatuses;
		}


		private static class ChunkStatusCountKey {
			public final String myChunkId;
			public final String myStepId;
			public final StatusEnum myStatus;

			private ChunkStatusCountKey(String theChunkId, String theStepId, StatusEnum theStatus) {
				myChunkId = theChunkId;
				myStepId = theStepId;
				myStatus = theStatus;
			}
		}


	}


}
