package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;

public abstract class BaseBulkModifyJobAppCtx<T extends BaseBulkModifyJobParameters> {



	protected JobDefinition<T> buildJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(getJobId())
			.setJobDescription(getJobDescription())
			.setJobDefinitionVersion(getJobDefinitionVersion())
			.gatedExecution()
			.setParametersType(getParametersType())
			.addFirstStep(
				"generate-ranges",
				"Generate data ranges to modify",
				ChunkRangeJson.class,
				generateRangesStep())
			.addIntermediateStep(
				"load-ids",
				"Load IDs of resources to modify",
				ResourceIdListWorkChunkJson.class,
				loadIdsStep())
			.addIntermediateStep(
				"modify-resources",
				"Modify resources",
				BulkModifyResourcesChunkOutcomeJson.class,
				modifyResourcesStep())
			.addFinalReducerStep(
				"generate-report",
				"Generate a report outlining the changes made",
				BulkModifyResourcesResultsJson.class,
				generateReportStep())
			.build();
	}

	protected abstract Class<T> getParametersType();

	protected int getJobDefinitionVersion() {
		return 1;
	}

	protected abstract String getJobDescription();

	protected abstract String getJobId();

	public abstract <C> BaseBulkModifyResourcesStep<T, C> modifyResourcesStep();

	public abstract GenerateRangeChunksStep<T> generateRangesStep();

	public abstract BulkModifyGenerateReportStep<T> generateReportStep();

	public abstract LoadIdsStep<T> loadIdsStep();
}
