package ca.uhn.fhir.jpa.util;

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
