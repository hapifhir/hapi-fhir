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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.StatusEnum;

import java.util.Date;

public interface IJobInstance {
	String getCurrentGatedStepId();

	int getErrorCount();

	String getEstimatedTimeRemaining();

	boolean isWorkChunksPurged();

	StatusEnum getStatus();

	int getJobDefinitionVersion();

	String getInstanceId();

	Date getStartTime();

	Date getEndTime();

	Integer getCombinedRecordsProcessed();

	Double getCombinedRecordsProcessedPerSecond();

	Date getCreateTime();

	Integer getTotalElapsedMillis();

	double getProgress();

	String getErrorMessage();

	String getWarningMessages();

	boolean isCancelled();

	String getReport();

	/**
	 * @return true if every step of the job has produced exactly 1 chunk.
	 */
	boolean isFastTracking();

	void setFastTracking(boolean theFastTracking);
}
