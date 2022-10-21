package ca.uhn.fhir.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.BulkExportCreateReportStep;
import ca.uhn.fhir.batch2.jobs.export.WriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.mdm.models.MdmSubmitJobParameters;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MdmSubmitJobConfig {

	@Bean
	public JobDefinition bulkExportJobDefinition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.MDM_SUBMIT);
		builder.setJobDescription("MDM Batch Submission");
		builder.setJobDefinitionVersion(1);

		JobDefinition def =  builder.setParametersType(MdmSubmitJobParameters.class)
			// validator
			.setParametersValidator(mdmSubmitJobParametersValidator())
			// first step - load in (all) ids and create id chunks of 1000 each
			.addFirstStep(
				"fetch-resources",
				"Fetches resource PIDs for MDM submission",
				ResourceIdList.class,
				fetchResourceIdsStep()
			)
			// expand out - fetch resources
			.addIntermediateStep(
				"expand-resources",
				"Expand out resources",
				ExpandedResourcesList.class,
				expandResourcesStep()
			)
			// write to broker topic.
			.addLastStep(
				"write-to-broker",
				"Writes the expanded resources to the broker topic",
				submitToBrokerStep()
			)
			// finalize the job (set to complete)
			.addFinalReducerStep(
				"create-report-step",
				"Creates the output report from a bulk export job",
				BulkExportJobResults.class,
				createReportStep()
			)
			.build();

		return def;
	}

	@Bean
	public MdmSubmitJobParametersValidator mdmSubmitJobParametersValidator() {
		return new MdmSubmitJobParametersValidator();
	}

	@Bean
	public FetchResourceIdsStep fetchResourceIdsStep() {
		return new FetchResourceIdsStep();
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
