package ca.uhn.fhir.batch2.jobs.bulkmodify;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkModifyJobAppCtx {

	public static final String BULK_MODIFY = "BULK_MODIFY";

	private final IBatch2DaoSvc myBatch2DaoSvc;

	/**
	 * Constructor
	 */
	public BulkModifyJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Bean
	public JobDefinition<BulkModifyJobParameters> bulkModifyJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(BULK_MODIFY)
			.setJobDescription("Bulk modify resources")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(BulkModifyJobParameters.class)
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
				ModifyResourcesChunkOutcomeJson.class,
				modifyResourcesStep())
			.addFinalReducerStep(
				"generate-report",
				"Generate a report outlining the changes made",
				ModifyResourcesResultsJson.class,
				generateReportStep())
			.build();
	}

	@Bean("bulkModifyGenerateReportStep")
	public GenerateReportStep generateReportStep() {
		return new GenerateReportStep();
	}

	@Bean("bulkModifyModifyResourcesStep")
	public ModifyResourcesStep modifyResourcesStep() {
		return new ModifyResourcesStep();
	}

	@Bean("bulkModifyGenerateRangesStep")
	public GenerateRangeChunksStep<BulkModifyJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("bulkModifyLoadIdsStep")
	public LoadIdsStep<BulkModifyJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}


}
