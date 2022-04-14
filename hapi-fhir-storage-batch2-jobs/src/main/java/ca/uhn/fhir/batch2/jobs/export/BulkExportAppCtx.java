package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkExportAppCtx {

	public static final String BULK_EXPORT = "BULK_EXPORT";

	@Bean
	public JobDefinition bulkExportJobDefinition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(1);

		builder.setParametersType(BulkExportJobParameters.class)
		// validator?
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
		.addLastStep(
			"write-to-binaries",
			"Writes the expanded resources to the binaries and saves",
			writeBinaryStep()
		);

		return builder.build();
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
}
