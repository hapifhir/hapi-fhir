package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;

import javax.annotation.Nonnull;

public class JobInstanceUtil {

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
		return retVal;
	}

	/**
	 * Converts a Batch2WorkChunkEntity into a WorkChunk object
	 * @param theEntity - the entity to convert
	 * @param theIncludeData - whether or not to include the Data attached to the chunk
	 * @return - the WorkChunk object
	 */
	@Nonnull
	public static WorkChunk fromEntityToWorkChunk(@Nonnull Batch2WorkChunkEntity theEntity, boolean theIncludeData) {
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
		retVal.setEndTime(theEntity.getEndTime());
		retVal.setErrorMessage(theEntity.getErrorMessage());
		retVal.setErrorCount(theEntity.getErrorCount());
		retVal.setRecordsProcessed(theEntity.getRecordsProcessed());
		if (theIncludeData) {
			if (theEntity.getSerializedData() != null) {
				retVal.setData(theEntity.getSerializedData());
			}
		}
		return retVal;
	}
}
