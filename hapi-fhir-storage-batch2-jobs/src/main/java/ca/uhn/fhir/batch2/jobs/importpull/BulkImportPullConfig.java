package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkImportPullConfig {

	@Bean
	public JobDefinition bulkImportPullJobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(BatchConstants.BULK_IMPORT_JOB_NAME)
			.setJobDescription("Performs bulk import pull job")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(Batch2BulkImportPullJobParameters.class)
			.setParametersValidator(importParameterValidator())
			.addFirstStep(
				"ReadInResourcesStep",
				"Reads an import file and extracts the resources",
				BulkImportFilePartitionResult.class,
				fetchPartitionedFilesStep()
			)
			.addIntermediateStep(
				"ReadInResourcesFromFileStep",
				"Reads the import file to get the serialized bundles",
				BulkImportRecord.class,
				readInResourcesFromFileStep()
			)
			.addLastStep(
				"WriteBundleStep",
				"Parses the bundle from previous step and writes it to the dv",
				writeBundleForImportStep()
			)
			.build();
	}

	@Bean
	public BulkImportParameterValidator importParameterValidator() {
		return new BulkImportParameterValidator();
	}

	@Bean
	public FetchPartitionedFilesStep fetchPartitionedFilesStep() {
		return new FetchPartitionedFilesStep();
	}

	@Bean
	public ReadInResourcesFromFileStep readInResourcesFromFileStep() {
		return new ReadInResourcesFromFileStep();
	}

	@Bean
	public WriteBundleForImportStep writeBundleForImportStep() {
		return new WriteBundleForImportStep();
	}
}
