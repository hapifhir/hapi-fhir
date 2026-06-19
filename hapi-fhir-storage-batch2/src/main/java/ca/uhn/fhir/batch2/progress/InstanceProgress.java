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
package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.StepWeightingForProgressCalculator;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class InstanceProgress {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final Map<String, Map<WorkChunkStatusEnum, Integer>> myStepToStatusCountMap = new HashMap<>();
	private final Map<String, StepProgressData> myStepProgressMap = new HashMap<>();
	private final Set<String> myWarningMessages = new HashSet<>();
	private int myRecordsProcessed = 0;
	// these 4 cover all chunks
	private final Map<String, IntCounter> myStepIdToIncompleteChunkCount = new HashMap<>();
	private final Map<String, IntCounter> myStepIdToCompleteChunkCount = new HashMap<>();
	private final Map<String, IntCounter> myStepIdToErroredChunkCount = new HashMap<>();
	private final Map<String, IntCounter> myStepIdToFailedChunkCount = new HashMap<>();
	private int myErrorCountForAllStatuses = 0;
	private Date myEarliestStartTime = null;
	private Date myLatestEndTime = null;
	private String myErrormessage = null;
	private StatusEnum myNewStatus = null;
	private int myAllIncompleteChunkCount;
	private int myAllCompleteChunkCount;
	private int myAllErroredChunkCount;
	private int myAllFailedChunkCount;

	public void addChunk(WorkChunk theChunk) {
		myErrorCountForAllStatuses += theChunk.getErrorCount();
		if (theChunk.getWarningMessage() != null) {
			myWarningMessages.add(theChunk.getWarningMessage());
		}
		updateRecordsProcessed(theChunk);
		updateEarliestTime(theChunk);
		updateLatestEndTime(theChunk);
		updateCompletionStatus(theChunk);
		updateStepProgress(theChunk);
	}

	private void updateStepProgress(WorkChunk theChunk) {
		String stepId = theChunk.getTargetStepId();
		if (stepId != null) {
			myStepProgressMap.computeIfAbsent(stepId, StepProgressData::new).addChunk(theChunk);
		}
	}

	private void updateCompletionStatus(WorkChunk theChunk) {
		// Update the status map.
		Map<WorkChunkStatusEnum, Integer> statusToCountMap =
				myStepToStatusCountMap.computeIfAbsent(theChunk.getTargetStepId(), k -> new HashMap<>());
		statusToCountMap.merge(theChunk.getStatus(), 1, Integer::sum);

		// Track instance-level counts and error messages.
		// Note: this switch intentionally remains here (rather than deriving from StepProgressData)
		// because it also captures error messages which are not tracked per-step.
		switch (theChunk.getStatus()) {
			case GATE_WAITING:
			case READY:
			case QUEUED:
			case POLL_WAITING:
			case IN_PROGRESS:
				myAllIncompleteChunkCount++;
				myStepIdToIncompleteChunkCount
						.computeIfAbsent(theChunk.getTargetStepId(), k -> new IntCounter())
						.increment();
				break;
			case COMPLETED:
				myAllCompleteChunkCount++;
				myStepIdToCompleteChunkCount
						.computeIfAbsent(theChunk.getTargetStepId(), k -> new IntCounter())
						.increment();
				break;
			case ERRORED:
				myAllErroredChunkCount++;
				myStepIdToErroredChunkCount
						.computeIfAbsent(theChunk.getTargetStepId(), k -> new IntCounter())
						.increment();
				if (myErrormessage == null) {
					myErrormessage = theChunk.getErrorMessage();
				}
				break;
			case FAILED:
				myAllFailedChunkCount++;
				myStepIdToFailedChunkCount
						.computeIfAbsent(theChunk.getTargetStepId(), k -> new IntCounter())
						.increment();
				myErrormessage = theChunk.getErrorMessage();
				break;
		}
		ourLog.trace(
				"Chunk has status {} with errored chunk count {}", theChunk.getStatus(), myStepIdToErroredChunkCount);
	}

	private void updateLatestEndTime(WorkChunk theChunk) {
		if (theChunk.getEndTime() != null
				&& (myLatestEndTime == null || myLatestEndTime.before(theChunk.getEndTime()))) {
			myLatestEndTime = theChunk.getEndTime();
		}
	}

	private void updateEarliestTime(WorkChunk theChunk) {
		if (theChunk.getStartTime() != null
				&& (myEarliestStartTime == null || myEarliestStartTime.after(theChunk.getStartTime()))) {
			myEarliestStartTime = theChunk.getStartTime();
		}
	}

	private void updateRecordsProcessed(WorkChunk theChunk) {
		if (theChunk.getRecordsProcessed() != null) {
			myRecordsProcessed += theChunk.getRecordsProcessed();
		}
	}

	/**
	 * Signal to the progress calculator to skip the incomplete work chunk count when determining the completed percentage.
	 * <p/>
	 * This is a hack:  The reason we do this is to get around a race condition in which all work chunks are complete but
	 * the last chunk is * still in QUEUED status and will only be marked COMPLETE later.
	 *
	 * @param theInstance The Batch 2 {@link JobInstance} that we're updating
	 */
	public void updateInstanceForReductionStep(
			JobDefinitionRegistry theJobDefinitionRegistry, JobInstance theInstance) {
		updateInstance(theJobDefinitionRegistry, theInstance, true);
	}

	public void updateInstance(JobDefinitionRegistry theJobDefinitionRegistry, JobInstance theInstance) {
		updateInstance(theJobDefinitionRegistry, theInstance, false);

		String newWarningMessage = StringUtils.right(String.join("\n", myWarningMessages), 4000);
		theInstance.setWarningMessages(newWarningMessage);
	}

	/**
	 * Update the job instance with status information.
	 * We shouldn't read any values from theInstance here -- just write.
	 *
	 * @param theInstance the instance to update with progress statistics
	 */
	public void updateInstance(
			JobDefinitionRegistry theJobDefinitionRegistry, JobInstance theInstance, boolean theCalledFromReducer) {

		JobDefinition<?> jobDefinition = theJobDefinitionRegistry.getJobDefinitionOrThrowException(
				theInstance.getJobDefinitionId(), theInstance.getJobDefinitionVersion());

		ourLog.debug("updateInstance {}: {}", theInstance.getInstanceId(), this);
		if (myEarliestStartTime != null) {
			theInstance.setStartTime(myEarliestStartTime);
		}
		if (myLatestEndTime != null && hasNewStatus() && myNewStatus.isEnded()) {
			theInstance.setEndTime(myLatestEndTime);
		}
		theInstance.setErrorCount(myErrorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(myRecordsProcessed);

		StepWeightingForProgressCalculator stepWeightingForProgressCalculator =
				jobDefinition.getStepWeightingForProgressCalculator();

		Set<String> alreadyCompletedStepIds = getAlreadyCompletedStepIds(jobDefinition, theInstance);

		double totalPercentComplete = 0.0;
		for (String stepId : stepWeightingForProgressCalculator.getStepIdsWithExplicitWeight()) {
			double stepWeight = stepWeightingForProgressCalculator.getWeightForStepId(stepId);
			if (alreadyCompletedStepIds.contains(stepId)) {
				totalPercentComplete += stepWeight;
				continue;
			}

			final int chunkCount = getChunkCount(stepId);
			final int conditionalChunkCount = theCalledFromReducer
					? (chunkCount - getChunkCount(myStepIdToIncompleteChunkCount, stepId))
					: chunkCount;
			if (conditionalChunkCount == 0) {
				continue;
			}

			final double stepPercentComplete =
					(double) getChunkCount(myStepIdToCompleteChunkCount, stepId) / (double) conditionalChunkCount;

			totalPercentComplete += stepWeight * stepPercentComplete;
		}

		Set<String> stepIdsWithoutExplicitWeight = stepWeightingForProgressCalculator.getStepIdsWithoutExplicitWeight();
		if (!stepIdsWithoutExplicitWeight.isEmpty()) {
			final int chunkCount = getChunkCount(stepIdsWithoutExplicitWeight);
			final int conditionalChunkCount = theCalledFromReducer
					? (chunkCount - getChunkCount(myStepIdToIncompleteChunkCount, stepIdsWithoutExplicitWeight))
					: chunkCount;
			if (conditionalChunkCount > 0) {
				final double stepPercentComplete =
						(double) getChunkCount(myStepIdToCompleteChunkCount, stepIdsWithoutExplicitWeight)
								/ (double) conditionalChunkCount;

				double stepWeight =
						stepWeightingForProgressCalculator.getCombinedWeightForStepIdsWithoutExplicitWeight();
				totalPercentComplete += stepWeight * stepPercentComplete;
			}
		}

		theInstance.setProgress(totalPercentComplete);

		if (myEarliestStartTime != null && myLatestEndTime != null) {
			long elapsedTime = myLatestEndTime.getTime() - myEarliestStartTime.getTime();
			if (elapsedTime > 0) {
				double throughput = StopWatch.getThroughput(myRecordsProcessed, elapsedTime, TimeUnit.SECONDS);
				theInstance.setCombinedRecordsProcessedPerSecond(throughput);

				String estimatedTimeRemaining =
						StopWatch.formatEstimatedTimeRemaining(totalPercentComplete, 1.0, elapsedTime);
				theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
			}
		}

		theInstance.setErrorMessage(myErrormessage);

		if (hasNewStatus()) {
			ourLog.trace("Status will change for {}: {}", theInstance.getInstanceId(), myNewStatus);
		}

		ourLog.trace("Updating status for instance with errors: {}", myStepIdToErroredChunkCount);
		ourLog.trace(
				"Statistics for job {}: complete/in-progress/errored/failed chunk count {}/{}/{}/{}",
				theInstance.getInstanceId(),
				myStepIdToCompleteChunkCount,
				myStepIdToIncompleteChunkCount,
				myStepIdToErroredChunkCount,
				myStepIdToFailedChunkCount);
	}

	/**
	 * Provide a Set of step IDs in a job execution that have already finished
	 * executing.
	 */
	@Nonnull
	private Set<String> getAlreadyCompletedStepIds(JobDefinition<?> theJobDefinition, JobInstance theInstance) {
		Set<String> retVal = new HashSet<>();
		if (theJobDefinition.isGatedExecution()) {
			/*
			 * For gated jobs, we know exactly which step we're in, so all the
			 * steps before that step are completed.
			 */
			String currentGatedStepId = theInstance.getCurrentGatedStepId();
			if (isNotBlank(currentGatedStepId)) {
				for (JobDefinitionStep<?, ?, ?> step : theJobDefinition.getSteps()) {
					if (step.getStepId().equals(currentGatedStepId)) {
						break;
					}
					retVal.add(step.getStepId());
				}
			}
		} else {
			/*
			 * For non-gated jobs, we consider a step to be completed if
			 * all the chunks for it are complete, or if it has no chunks in
			 * any state and all the previous steps are complete.
			 */
			boolean lastStepWasComplete = false;
			for (JobDefinitionStep<?, ?, ?> step : theJobDefinition.getSteps()) {
				String stepId = step.getStepId();

				int completeChunksForStep = getChunkCount(myStepIdToCompleteChunkCount, stepId);
				int totalChunksForStep = getChunkCount(stepId);
				if (completeChunksForStep > 0 && completeChunksForStep == totalChunksForStep) {
					lastStepWasComplete = true;
					retVal.add(stepId);
				} else if (lastStepWasComplete && totalChunksForStep == 0) {
					retVal.add(stepId);
				} else {
					lastStepWasComplete = false;
				}
			}
		}

		return retVal;
	}

	private int getChunkCount(String theStepId) {
		return getChunkCount(List.of(theStepId));
	}

	private int getChunkCount(Collection<String> theStepId) {
		return getChunkCount(myStepIdToIncompleteChunkCount, theStepId)
				+ getChunkCount(myStepIdToCompleteChunkCount, theStepId)
				+ getChunkCount(myStepIdToFailedChunkCount, theStepId)
				+ getChunkCount(myStepIdToErroredChunkCount, theStepId);
	}

	/**
	 * Transitions from IN_PROGRESS/ERRORED based on chunk statuses.
	 */
	public void calculateNewStatus(boolean theLastStepIsReduction) {
		if (myAllFailedChunkCount > 0) {
			myNewStatus = StatusEnum.FAILED;
		} else if (myAllErroredChunkCount > 0) {
			//noinspection deprecation
			myNewStatus = StatusEnum.ERRORED;
		} else if (myAllIncompleteChunkCount == 0 && myAllCompleteChunkCount > 0 && !theLastStepIsReduction) {
			myNewStatus = StatusEnum.COMPLETED;
		}
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this)
				.append("myIncompleteChunkCount", myStepIdToIncompleteChunkCount)
				.append("myCompleteChunkCount", myStepIdToCompleteChunkCount)
				.append("myErroredChunkCount", myStepIdToErroredChunkCount)
				.append("myFailedChunkCount", myStepIdToFailedChunkCount)
				.append("myErrormessage", myErrormessage)
				.append("myRecordsProcessed", myRecordsProcessed);

		builder.append("myStepToStatusCountMap", myStepToStatusCountMap);

		return builder.toString();
	}

	public StatusEnum getNewStatus() {
		return myNewStatus;
	}

	public boolean hasNewStatus() {
		return myNewStatus != null;
	}

	/**
	 * Returns per-step progress data for all steps that have been observed.
	 * The returned map is keyed by step ID.
	 */
	public Map<String, StepProgressData> getStepProgressMap() {
		return Collections.unmodifiableMap(myStepProgressMap);
	}

	/**
	 * Returns the progress data for a specific step, or null if no chunks
	 * have been observed for that step.
	 */
	public StepProgressData getStepProgress(String theStepId) {
		return myStepProgressMap.get(theStepId);
	}

	private static int getChunkCount(Map<String, IntCounter> theCounterMap, String theStepId) {
		return getChunkCount(theCounterMap, List.of(theStepId));
	}

	private static int getChunkCount(Map<String, IntCounter> theCounterMap, Collection<String> theStepIds) {
		int retVal = 0;
		for (String stepId : theStepIds) {
			IntCounter counter = theCounterMap.get(stepId);
			if (counter != null) {
				retVal += counter.get();
			}
		}
		return retVal;
	}
}
