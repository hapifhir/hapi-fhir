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

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Tracks progress metrics for a single step within a batch job instance.
 * Provides per-step throughput, chunk counts, timing, and records processed data
 * to enable operators to identify bottleneck steps.
 */
public class StepProgressData {

	private final String myStepId;
	private int myChunkCount = 0;
	private int myCompleteChunkCount = 0;
	private int myIncompleteChunkCount = 0;
	private int myErroredChunkCount = 0;
	private int myFailedChunkCount = 0;
	private int myRecordsProcessed = 0;
	private Date myEarliestStartTime = null;
	private Date myLatestEndTime = null;

	public StepProgressData(String theStepId) {
		myStepId = theStepId;
	}

	public void addChunk(WorkChunk theChunk) {
		myChunkCount++;
		if (theChunk.getRecordsProcessed() != null) {
			myRecordsProcessed += theChunk.getRecordsProcessed();
		}
		updateEarliestStartTime(theChunk);
		updateLatestEndTime(theChunk);
		updateStatusCounts(theChunk.getStatus());
	}

	private void updateEarliestStartTime(WorkChunk theChunk) {
		if (theChunk.getStartTime() != null
				&& (myEarliestStartTime == null || myEarliestStartTime.after(theChunk.getStartTime()))) {
			myEarliestStartTime = theChunk.getStartTime();
		}
	}

	private void updateLatestEndTime(WorkChunk theChunk) {
		if (theChunk.getEndTime() != null
				&& (myLatestEndTime == null || myLatestEndTime.before(theChunk.getEndTime()))) {
			myLatestEndTime = theChunk.getEndTime();
		}
	}

	private void updateStatusCounts(WorkChunkStatusEnum theStatus) {
		switch (theStatus) {
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
				break;
			case FAILED:
				myFailedChunkCount++;
				break;
		}
	}

	public String getStepId() {
		return myStepId;
	}

	public int getChunkCount() {
		return myChunkCount;
	}

	public int getCompleteChunkCount() {
		return myCompleteChunkCount;
	}

	public int getIncompleteChunkCount() {
		return myIncompleteChunkCount;
	}

	public int getErroredChunkCount() {
		return myErroredChunkCount;
	}

	public int getFailedChunkCount() {
		return myFailedChunkCount;
	}

	public int getRecordsProcessed() {
		return myRecordsProcessed;
	}

	public Date getEarliestStartTime() {
		return myEarliestStartTime;
	}

	public Date getLatestEndTime() {
		return myLatestEndTime;
	}

	/**
	 * @return elapsed time in milliseconds for this step, or 0 if timing data is unavailable
	 */
	public long getElapsedMillis() {
		if (myEarliestStartTime == null || myLatestEndTime == null) {
			return 0;
		}
		return Math.max(0, myLatestEndTime.getTime() - myEarliestStartTime.getTime());
	}

	/**
	 * @return throughput in records per second for this step, or 0.0 if not calculable
	 */
	public double getThroughputPerSecond() {
		long elapsed = getElapsedMillis();
		if (elapsed <= 0 || myRecordsProcessed <= 0) {
			return 0.0;
		}
		return StopWatch.getThroughput(myRecordsProcessed, elapsed, TimeUnit.SECONDS);
	}

	/**
	 * @return completion percentage for this step (0.0 to 1.0)
	 */
	public double getCompletionPercentage() {
		if (myChunkCount == 0) {
			return 0.0;
		}
		return (double) myCompleteChunkCount / (double) myChunkCount;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("stepId", myStepId)
				.append("chunks", myChunkCount)
				.append("complete", myCompleteChunkCount)
				.append("incomplete", myIncompleteChunkCount)
				.append("errored", myErroredChunkCount)
				.append("failed", myFailedChunkCount)
				.append("recordsProcessed", myRecordsProcessed)
				.append("throughput/sec", String.format("%.1f", getThroughputPerSecond()))
				.append("elapsedMs", getElapsedMillis())
				.toString();
	}
}
