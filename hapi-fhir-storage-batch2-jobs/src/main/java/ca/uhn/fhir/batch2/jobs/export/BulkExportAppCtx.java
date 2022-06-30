package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BulkExportAppCtx {

	@Bean
	public JobDefinition bulkExportJobDefinition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(1);

		JobDefinition def =  builder.setParametersType(BulkExportJobParameters.class)
			// validator
			.setParametersValidator(bulkExportJobParametersValidator())
			// first step - load in (all) ids and create id chunks of 1000 each
			.addFirstStep(
			"fetch-resources",
			"Fetches resource PIDs for exporting",
			BulkExportIdList.class,
			fetchResourceIdsStep()
		)
		// expand out - fetch resources
		.addIntermediateStep(
			"expand-resources",
			"Expand out resources",
			BulkExportExpandedResources.class,
			expandResourcesStep()
		)
		// write binaries and save to db
		.addIntermediateStep(
			"write-to-binaries",
			"Writes the expanded resources to the binaries and saves",
			BulkExportBinaryFileId.class,
			writeBinaryStep()
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
	public BulkExportJobParametersValidator bulkExportJobParametersValidator() {
		return new BulkExportJobParametersValidator();
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
	@Scope("prototype")
	public BulkExportCreateReportStep createReportStep() {
		return new BulkExportCreateReportStep();
	}
}
