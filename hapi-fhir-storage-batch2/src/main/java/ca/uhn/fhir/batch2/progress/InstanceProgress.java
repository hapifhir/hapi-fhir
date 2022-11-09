package ca.uhn.fhir.batch2.progress;

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

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

class InstanceProgress {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private int myRecordsProcessed = 0;
	private int myIncompleteChunkCount = 0;
	private int myCompleteChunkCount = 0;
	private int myErroredChunkCount = 0;
	private int myFailedChunkCount = 0;
	private int myErrorCountForAllStatuses = 0;
	private Long myEarliestStartTime = null;
	private Long myLatestEndTime = null;
	private String myErrormessage = null;
	private StatusEnum myNewStatus = null;

	public void addChunk(WorkChunk theChunk) {
		myErrorCountForAllStatuses += theChunk.getErrorCount();

		updateRecordsProcessed(theChunk);
		updateEarliestTime(theChunk);
		updateLatestEndTime(theChunk);
		updateCompletionStatus(theChunk);
	}

	private void updateCompletionStatus(WorkChunk theChunk) {
		switch (theChunk.getStatus()) {
			case QUEUED:
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
			case CANCELLED:
				break;
		}
		ourLog.trace("Chunk has status {} with errored chunk count {}", theChunk.getStatus(), myErroredChunkCount);
	}

	private void updateLatestEndTime(WorkChunk theChunk) {
		if (theChunk.getEndTime() != null) {
			if (myLatestEndTime == null || myLatestEndTime < theChunk.getEndTime().getTime()) {
				myLatestEndTime = theChunk.getEndTime().getTime();
			}
		}
	}

	private void updateEarliestTime(WorkChunk theChunk) {
		if (theChunk.getStartTime() != null) {
			if (myEarliestStartTime == null || myEarliestStartTime > theChunk.getStartTime().getTime()) {
				myEarliestStartTime = theChunk.getStartTime().getTime();
			}
		}
	}

	private void updateRecordsProcessed(WorkChunk theChunk) {
		if (theChunk.getRecordsProcessed() != null) {
			myRecordsProcessed += theChunk.getRecordsProcessed();
		}
	}

	public void updateInstance(JobInstance theInstance) {
		if (myEarliestStartTime != null) {
			theInstance.setStartTime(new Date(myEarliestStartTime));
		}
		theInstance.setErrorCount(myErrorCountForAllStatuses);
		theInstance.setCombinedRecordsProcessed(myRecordsProcessed);

		updateStatus(theInstance);

		setEndTime(theInstance);

		theInstance.setErrorMessage(myErrormessage);
	}

	private void setEndTime(JobInstance theInstance) {
		if (myLatestEndTime != null) {
			if (myFailedChunkCount > 0) {
				theInstance.setEndTime(new Date(myLatestEndTime));
			} else if (myCompleteChunkCount > 0 && myIncompleteChunkCount == 0 && myErroredChunkCount == 0) {
				theInstance.setEndTime(new Date(myLatestEndTime));
			}
		}
	}

	private void updateStatus(JobInstance theInstance) {
		ourLog.trace("Updating status for instance with errors: {}", myErroredChunkCount);
		if (myCompleteChunkCount >= 1 || myErroredChunkCount >= 1) {

			double percentComplete = (double) (myCompleteChunkCount) / (double) (myIncompleteChunkCount + myCompleteChunkCount + myFailedChunkCount + myErroredChunkCount);
			theInstance.setProgress(percentComplete);

			if (jobSuccessfullyCompleted()) {
				myNewStatus = StatusEnum.COMPLETED;
			} else if (myErroredChunkCount > 0) {
				myNewStatus = StatusEnum.ERRORED;
			}

			ourLog.trace("Status is now {} with errored chunk count {}", myNewStatus, myErroredChunkCount);
			if (myEarliestStartTime != null && myLatestEndTime != null) {
				long elapsedTime = myLatestEndTime - myEarliestStartTime;
				if (elapsedTime > 0) {
					double throughput = StopWatch.getThroughput(myRecordsProcessed, elapsedTime, TimeUnit.SECONDS);
					theInstance.setCombinedRecordsProcessedPerSecond(throughput);

					String estimatedTimeRemaining = StopWatch.formatEstimatedTimeRemaining(myCompleteChunkCount, (myCompleteChunkCount + myIncompleteChunkCount), elapsedTime);
					theInstance.setEstimatedTimeRemaining(estimatedTimeRemaining);
				}
			}
		}
	}

	private boolean jobSuccessfullyCompleted() {
		return myIncompleteChunkCount == 0 && myErroredChunkCount == 0 && myFailedChunkCount == 0;
	}

	public boolean failed() {
		return myFailedChunkCount > 0;
	}

	public boolean changed() {
		return (myIncompleteChunkCount + myCompleteChunkCount + myErroredChunkCount) >= 2 || myErrorCountForAllStatuses > 0;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myIncompleteChunkCount", myIncompleteChunkCount)
			.append("myCompleteChunkCount", myCompleteChunkCount)
			.append("myErroredChunkCount", myErroredChunkCount)
			.append("myFailedChunkCount", myFailedChunkCount)
			.append("myErrormessage", myErrormessage)
			.append("myRecordsProcessed", myRecordsProcessed)
			.toString();
	}

	public StatusEnum getNewStatus() {
		return myNewStatus;
	}

	public boolean hasNewStatus() {
		return myNewStatus != null;
	}
}
