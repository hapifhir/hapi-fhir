package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkImportPullConfig {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

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
		return new BulkImportParameterValidator(myBulkDataImportSvc);
	}

	@Bean
	public FetchPartitionedFilesStep fetchPartitionedFilesStep() {
		return new FetchPartitionedFilesStep(myBulkDataImportSvc);
	}

	@Bean
	public ReadInResourcesFromFileStep readInResourcesFromFileStep() {
		return new ReadInResourcesFromFileStep(myBulkDataImportSvc);
	}

	@Bean
	public WriteBundleForImportStep writeBundleForImportStep() {
		return new WriteBundleForImportStep(myFhirContext, myDaoRegistry);
	}
}
