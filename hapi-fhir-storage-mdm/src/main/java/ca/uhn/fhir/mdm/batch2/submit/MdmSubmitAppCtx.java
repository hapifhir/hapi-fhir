package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.export.BulkExportCreateReportStep;
import ca.uhn.fhir.batch2.jobs.export.WriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MdmSubmitAppCtx {

	private static final String MDM_SUBMIT_JOB_BEAN_NAME = "mdmSubmitJobDefinition";
	public static String MDM_SUBMIT_JOB= "MDM_SUBMIT";

	@Bean
	public GenerateRangeChunksStep submitGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}


	@Bean(name = MDM_SUBMIT_JOB_BEAN_NAME)
	public JobDefinition mdmSubmitJobDefinition(IBatch2DaoSvc theBatch2DaoSvc) {
		return JobDefinition.newBuilder()
		.setJobDefinitionId(MDM_SUBMIT_JOB)
		.setJobDescription("MDM Batch Submission")
		.setJobDefinitionVersion(1)
		.setParametersType(MdmSubmitJobParameters.class)
		.setParametersValidator(mdmSubmitJobParametersValidator())
		.gatedExecution()
		.addFirstStep(
			"generate-ranges",
			"generate data ranges to submit to mdm",
			PartitionedUrlChunkRangeJson.class,
			submitGenerateRangeChunksStep())
		.addIntermediateStep(
			"load-ids",
			"Load the IDs",
			ResourceIdListWorkChunkJson.class,
			new LoadIdsStep(theBatch2DaoSvc))
		.addIntermediateStep(
			"expand-resources",
			"Expand out resources",
			ExpandedResourcesList.class,
			mdmExpandResourcesStep())
		.addLastStep(
			"write-to-broker",
			"Writes the expanded resources to the broker topic",
			submitToBrokerStep())
		.build();
	}

@Bean
	public MdmSubmitJobParametersValidator mdmSubmitJobParametersValidator() {
		return new MdmSubmitJobParametersValidator();
	}


	@Bean
	public MdmExpandResourcesStep mdmExpandResourcesStep() {
		return new MdmExpandResourcesStep();
	}

	@Bean
	public WriteBinaryStep writeBinaryStep() {
		return new WriteBinaryStep();
	}

	@Bean
	public SubmitToBrokerStep submitToBrokerStep() {
		return new SubmitToBrokerStep();
	}

	@Bean
	@Scope("prototype")
	public BulkExportCreateReportStep createReportStep() {
		return new BulkExportCreateReportStep();
	}
}
