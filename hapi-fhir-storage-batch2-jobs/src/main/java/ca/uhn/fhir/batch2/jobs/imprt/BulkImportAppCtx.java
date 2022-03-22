package ca.uhn.fhir.batch2.jobs.imprt;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkImportAppCtx {

	public static final String JOB_BULK_IMPORT_PULL = "BULK_IMPORT_PULL";
	public static final int PARAM_MAXIMUM_BATCH_SIZE_DEFAULT = 800; // Avoid the 1000 SQL param limit

	@Bean
	public JobDefinition bulkImport2JobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_BULK_IMPORT_PULL)
			.setJobDescription("FHIR Bulk Import using pull-based data source")
			.setJobDefinitionVersion(1)
			.setParametersType(BulkImportJobParameters.class)
			.addFirstStep(
				"fetch-files",
				"Fetch files for import",
				NdJsonFileJson.class,
				bulkImport2FetchFiles())
			.addLastStep(
				"process-files",
				"Process files",
				bulkImport2ConsumeFiles())
			.build();
	}

	@Bean
	public IJobStepWorker<BulkImportJobParameters, VoidModel, NdJsonFileJson> bulkImport2FetchFiles() {
		return new FetchFilesStep();
	}

	@Bean
	public IJobStepWorker<BulkImportJobParameters, NdJsonFileJson, VoidModel> bulkImport2ConsumeFiles() {
		return new ConsumeFilesStep();
	}

	@Bean
	public BulkDataImportProvider bulkImportProvider() {
		return new BulkDataImportProvider();
	}
}
