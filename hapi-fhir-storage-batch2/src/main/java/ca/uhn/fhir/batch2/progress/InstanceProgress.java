/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class InstanceProgress {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private int myRecordsProcessed = 0;

	// these 4 cover all chunks
	private int myIncompleteChunkCount = 0;
	private int myCompleteChunkCount = 0;
	private int myErroredChunkCount = 0;
	private int myFailedChunkCount = 0;

	private int myErrorCountForAllStatuses = 0;
	private Date myEarliestStartTime = null;
	private Date myLatestEndTime = null;
	private String myErrormessage = null;
	private StatusEnum myNewStatus = null;
	private final Map<String, Map<WorkChunkStatusEnum, Integer>> myStepToStatusCountMap = new HashMap<>();
	private final Set<String> myWarningMessages = new HashSet<>();

	public void addChunk(WorkChunk theChunk) {
		myErrorCountForAllStatuses += theChunk.getErrorCount();
		if (theChunk.getWarningMessage() != null) {
			myWarningMessages.add(theChunk.getWarningMessage());
		}
		updateRecordsProcessed(theChunk);
		updateEarliestTime(theChunk);
		updateLatestEndTime(theChunk);
		updateCompletionStatus(theChunk);
	}

	private void updateCompletionStatus(WorkChunk theChunk) {
		// Update the status map first.
		Map<WorkChunkStatusEnum, Integer> statusToCountMap =
				myStepToStatusCountMap.getOrDefault(theChunk.getTargetStepId(), new HashMap<>());
		statusToCountMap.put(theChunk.getStatus(), statusToCountMap.getOrDefault(theChunk.getStatus(), 0) + 1);

		switch (theChunk.getStatus()) {
			case GATE_WAITING:
			case READY:
			case QUEUED:
			case POLL_WAITING:
			case IN_PROGRESS:
				myIncompleteChunkCount++;
				break;
			case COMPLETED:
				myCompleteChunkCount++;
				break;
			case ERRORED:
				myErroredChunkCount++;
				if (myErrormessage == null) {
					myErrormessage = theChunk.getErrorMessage();
				}
				break;
			case FAILED:
				myFailedChunkCount++;
				myErrormessage = theChunk.getErrorMessage();
				break;
		}
		ourLog.trace("Chunk has status {} with errored chunk count {}", theChunk.getStatus(), myErroredChunkCount);
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
	public void updateInstanceForReductionStep(JobInstance theInstance) {
		updateInstance(theInstance, true);
	}

	public void updateInstance(JobInstance theInstance) {
		updateInstance(theInstance, false);

		String newWarningMessage = StringUtils.right(String.join("\n", myWarningMessages), 4000);
		theInstance.setWarningMessages(newWarningMessage);
	}

	/**
	 * Update the job instance with status information.
	 * We shouldn't read any values from theInstance here -- just write.
	 *
	 * @param theInstance the instance to update with progress statistics
	 */
	public void updateInstance(JobInstance theInstance, boolean theCalledFromReducer) {
		ourLog.debug("updateInstance {}: {}", theInstance.getInstanceId(), this);
		if (myEarliestStartTime != null) {
			theInstance.setStartTime(myEarliestStartTime);
		}
		if (myLatestEndTime != null && hasNewStatus() && myNewStatus.isEnded()) {
			theInstance.setEndTime(myLatestEndTime);
		}
		theInstance.setErrorCount(myErrorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(myRecordsProcessed);

		if (getChunkCount() > 0) {
			final int chunkCount = getChunkCount();
			final int conditionalChunkCount = theCalledFromReducer ? (chunkCount - myIncompleteChunkCount) : chunkCount;
			final double percentComplete = (double) (myCompleteChunkCount) / (double) conditionalChunkCount;
			theInstance.setProgress(percentComplete);
		}

		if (myEarliestStartTime != null && myLatestEndTime != null) {
			long elapsedTime = myLatestEndTime.getTime() - myEarliestStartTime.getTime();
			if (elapsedTime > 0) {
				double throughput = StopWatch.getThroughput(myRecordsProcessed, elapsedTime, TimeUnit.SECONDS);
				theInstance.setCombinedRecordsProcessedPerSecond(throughput);

				String estimatedTimeRemaining =
						StopWatch.formatEstimatedTimeRemaining(myCompleteChunkCount, getChunkCount(), elapsedTime);
				theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
			}
		}

		theInstance.setErrorMessage(myErrormessage);

		if (hasNewStatus()) {
			ourLog.trace("Status will change for {}: {}", theInstance.getInstanceId(), myNewStatus);
		}

		ourLog.trace("Updating status for instance with errors: {}", myErroredChunkCount);
		ourLog.trace(
				"Statistics for job {}: complete/in-progress/errored/failed chunk count {}/{}/{}/{}",
				theInstance.getInstanceId(),
				myCompleteChunkCount,
				myIncompleteChunkCount,
				myErroredChunkCount,
				myFailedChunkCount);
	}

	private int getChunkCount() {
		return myIncompleteChunkCount + myCompleteChunkCount + myFailedChunkCount + myErroredChunkCount;
	}

	/**
	 * Transitions from IN_PROGRESS/ERRORED based on chunk statuses.
	 */
	public void calculateNewStatus(boolean theLastStepIsReduction) {
		if (myFailedChunkCount > 0) {
			myNewStatus = StatusEnum.FAILED;
		} else if (myErroredChunkCount > 0) {
			myNewStatus = StatusEnum.ERRORED;
		} else if (myIncompleteChunkCount == 0 && myCompleteChunkCount > 0 && !theLastStepIsReduction) {
			myNewStatus = StatusEnum.COMPLETED;
		}
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this)
				.append("myIncompleteChunkCount", myIncompleteChunkCount)
				.append("myCompleteChunkCount", myCompleteChunkCount)
				.append("myErroredChunkCount", myErroredChunkCount)
				.append("myFailedChunkCount", myFailedChunkCount)
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
}
