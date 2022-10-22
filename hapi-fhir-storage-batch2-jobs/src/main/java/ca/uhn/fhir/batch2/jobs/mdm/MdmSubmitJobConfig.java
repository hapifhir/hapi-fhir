package ca.uhn.fhir.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.export.BulkExportCreateReportStep;
import ca.uhn.fhir.batch2.jobs.export.WriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.mdm.models.MdmSubmitJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MdmSubmitJobConfig {


	@Bean
	public GenerateRangeChunksStep submitGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}

	@Bean
	public JobDefinition mdmSubmitJobDefinition(IBatch2DaoSvc theBatch2DaoSvc) {
		return JobDefinition.newBuilder()
		.setJobDefinitionId(Batch2JobDefinitionConstants.MDM_SUBMIT)
		.setJobDescription("MDM Batch Submission")
		.setJobDefinitionVersion(1)
		.setParametersType(MdmSubmitJobParameters.class)
		.setParametersValidator(mdmSubmitJobParametersValidator())
		.addFirstStep(
			"generate-ranges",
			"generate data ranges to submit to mdm",
			PartitionedUrlChunkRangeJson.class,
			submitGenerateRangeChunksStep())
		.addIntermediateStep(
			"load-ids",
			"Load the IDs",
			ExpandedResourcesList.class,
			new LoadIdsStep(theBatch2DaoSvc))
		.addIntermediateStep(
			"expand-resources",
			"Expand out resources",
			ExpandedResourcesList.class,
			expandResourcesStep())
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
	public ExpandResourcesStep expandResourcesStep() {
		return new ExpandResourcesStep();
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
