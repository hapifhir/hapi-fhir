/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import jakarta.annotation.Nonnull;

class JobInstanceUtil {

	private JobInstanceUtil() {}

	/**
	 * Converts a Batch2JobInstanceEntity into a JobInstance object
	 * @param theEntity - the entity
	 * @return - a job instance
	 */
	@Nonnull
	public static JobInstance fromEntityToInstance(@Nonnull Batch2JobInstanceEntity theEntity) {
		JobInstance retVal = new JobInstance();
		retVal.setInstanceId(theEntity.getId());
		retVal.setJobDefinitionId(theEntity.getDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getDefinitionVersion());
		retVal.setStatus(theEntity.getStatus());
		retVal.setCancelled(theEntity.isCancelled());
		retVal.setFastTracking(theEntity.isFastTracking());
		retVal.setStartTime(theEntity.getStartTime());
		retVal.setCreateTime(theEntity.getCreateTime());
		retVal.setEndTime(theEntity.getEndTime());
		retVal.setUpdateTime(theEntity.getUpdateTime());
		retVal.setCombinedRecordsProcessed(theEntity.getCombinedRecordsProcessed());
		retVal.setCombinedRecordsProcessedPerSecond(theEntity.getCombinedRecordsProcessedPerSecond());
		retVal.setTotalElapsedMillis(theEntity.getTotalElapsedMillis());
		retVal.setWorkChunksPurged(theEntity.getWorkChunksPurged());
		retVal.setProgress(theEntity.getProgress());
		retVal.setErrorMessage(theEntity.getErrorMessage());
		retVal.setErrorCount(theEntity.getErrorCount());
		retVal.setEstimatedTimeRemaining(theEntity.getEstimatedTimeRemaining());
		retVal.setParameters(theEntity.getParams());
		retVal.setCurrentGatedStepId(theEntity.getCurrentGatedStepId());
		retVal.setReport(theEntity.getReport());
		retVal.setEstimatedTimeRemaining(theEntity.getEstimatedTimeRemaining());
		retVal.setWarningMessages(theEntity.getWarningMessages());
		retVal.setTriggeringUsername(theEntity.getTriggeringUsername());
		retVal.setTriggeringClientId(theEntity.getTriggeringClientId());
		return retVal;
	}

	/**
	 * Copies all JobInstance fields to a Batch2JobInstanceEntity
	 * @param theJobInstance the job
	 * @param theJobInstanceEntity the target entity
	 */
	public static void fromInstanceToEntity(
			@Nonnull JobInstance theJobInstance, @Nonnull Batch2JobInstanceEntity theJobInstanceEntity) {
		theJobInstanceEntity.setId(theJobInstance.getInstanceId());
		theJobInstanceEntity.setDefinitionId(theJobInstance.getJobDefinitionId());
		theJobInstanceEntity.setDefinitionVersion(theJobInstance.getJobDefinitionVersion());
		theJobInstanceEntity.setStatus(theJobInstance.getStatus());
		theJobInstanceEntity.setCancelled(theJobInstance.isCancelled());
		theJobInstanceEntity.setFastTracking(theJobInstance.isFastTracking());
		theJobInstanceEntity.setStartTime(theJobInstance.getStartTime());
		theJobInstanceEntity.setCreateTime(theJobInstance.getCreateTime());
		theJobInstanceEntity.setEndTime(theJobInstance.getEndTime());
		theJobInstanceEntity.setUpdateTime(theJobInstance.getUpdateTime());
		theJobInstanceEntity.setCombinedRecordsProcessed(theJobInstance.getCombinedRecordsProcessed());
		theJobInstanceEntity.setCombinedRecordsProcessedPerSecond(
				theJobInstance.getCombinedRecordsProcessedPerSecond());
		theJobInstanceEntity.setTotalElapsedMillis(theJobInstance.getTotalElapsedMillis());
		theJobInstanceEntity.setWorkChunksPurged(theJobInstance.isWorkChunksPurged());
		theJobInstanceEntity.setProgress(theJobInstance.getProgress());
		theJobInstanceEntity.setErrorMessage(theJobInstance.getErrorMessage());
		theJobInstanceEntity.setErrorCount(theJobInstance.getErrorCount());
		theJobInstanceEntity.setEstimatedTimeRemaining(theJobInstance.getEstimatedTimeRemaining());
		theJobInstanceEntity.setParams(theJobInstance.getParameters());
		theJobInstanceEntity.setCurrentGatedStepId(theJobInstance.getCurrentGatedStepId());
		theJobInstanceEntity.setReport(theJobInstance.getReport());
		theJobInstanceEntity.setEstimatedTimeRemaining(theJobInstance.getEstimatedTimeRemaining());
		theJobInstanceEntity.setWarningMessages(theJobInstance.getWarningMessages());
		theJobInstanceEntity.setTriggeringUsername(theJobInstance.getTriggeringUsername());
		theJobInstanceEntity.setTriggeringClientId(theJobInstance.getTriggeringClientId());
	}

	/**
	 * Converts a Batch2WorkChunkEntity into a WorkChunk object
	 *
	 * @param theEntity - the entity to convert
	 * @return - the WorkChunk object
	 */
	@Nonnull
	public static WorkChunk fromEntityToWorkChunk(@Nonnull Batch2WorkChunkEntity theEntity) {
		WorkChunk retVal = new WorkChunk();
		retVal.setId(theEntity.getId());
		retVal.setSequence(theEntity.getSequence());
		retVal.setJobDefinitionId(theEntity.getJobDefinitionId());
		retVal.setJobDefinitionVersion(theEntity.getJobDefinitionVersion());
		retVal.setInstanceId(theEntity.getInstanceId());
		retVal.setTargetStepId(theEntity.getTargetStepId());
		retVal.setStatus(theEntity.getStatus());
		retVal.setCreateTime(theEntity.getCreateTime());
		retVal.setStartTime(theEntity.getStartTime());
		retVal.setUpdateTime(theEntity.getUpdateTime());
		retVal.setEndTime(theEntity.getEndTime());
		retVal.setErrorMessage(theEntity.getErrorMessage());
		retVal.setErrorCount(theEntity.getErrorCount());
		retVal.setRecordsProcessed(theEntity.getRecordsProcessed());
		retVal.setNextPollTime(theEntity.getNextPollTime());
		retVal.setPollAttempts(theEntity.getPollAttempts());
		// note: may be null out if queried NoData
		retVal.setData(theEntity.getSerializedData());
		retVal.setWarningMessage(theEntity.getWarningMessage());
		return retVal;
	}
}
