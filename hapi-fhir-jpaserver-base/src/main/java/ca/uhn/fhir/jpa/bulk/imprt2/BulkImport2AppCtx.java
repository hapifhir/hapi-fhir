package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionParameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkImport2AppCtx {

	public static final String JOB_BULK_IMPORT_PULL = "BULK_IMPORT_PULL";
	public static final String PARAM_NDJSON_URL = "ndjson-url";
	public static final String PARAM_HTTP_BASIC_CREDENTIALS = "http-basic-credentials";
	public static final String PARAM_MAXIMUM_BATCH_RESOURCE_COUNT = "maximum-batch-resource-count";
	public static final int PARAM_MAXIMUM_BATCH_SIZE_DEFAULT = 800; // Avoid the 1000 SQL param limit

	@Bean
	public JobDefinition bulkImport2JobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_BULK_IMPORT_PULL)
			.setJobDescription("FHIR Bulk Import using pull-based data source")
			.setJobDefinitionVersion(1)
			.addParameter(
				PARAM_NDJSON_URL,
				"A URL that can be used to pull an NDJSON file for consumption",
				JobDefinitionParameter.ParamTypeEnum.STRING,
				false,
				true)
			.addParameter(
				PARAM_HTTP_BASIC_CREDENTIALS,
				"A set of HTTP Basic credentials to include on fetch requests in the format \"username:password\"",
				JobDefinitionParameter.ParamTypeEnum.PASSWORD,
				false,
				false)
			.addParameter(
				PARAM_MAXIMUM_BATCH_RESOURCE_COUNT,
				"Specifies the maximum number of resources that will be ingested in a single database transaction. Default is " + PARAM_MAXIMUM_BATCH_SIZE_DEFAULT + ".",
				JobDefinitionParameter.ParamTypeEnum.POSITIVE_INTEGER,
				false,
				false)
			.addStep(
				"fetch-files",
				"Fetch files for import",
				bulkImport2FetchFiles())
			.addStep(
				"process-files",
				"Process files",
				bulkImport2ConsumeFiles())
			.build();
	}

	@Bean
	public IJobStepWorker bulkImport2FetchFiles() {
		return new FetchFilesStep();
	}

	@Bean
	public IJobStepWorker bulkImport2ConsumeFiles() {
		return new ConsumeFilesStep();
	}

	@Bean
	public BulkImportProvider bulkImportProvider() {
		return new BulkImportProvider();
	}
}
