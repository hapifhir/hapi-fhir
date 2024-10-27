/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkImportPullConfig {
	public static final String BULK_IMPORT_JOB_NAME = "bulkImportJob";

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Bean
	public JobDefinition bulkImportPullJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(BULK_IMPORT_JOB_NAME)
				.setJobDescription("Performs bulk import pull job")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(Batch2BulkImportPullJobParameters.class)
				.setParametersValidator(importParameterValidator())
				.addFirstStep(
						"FetchPartitionedFilesStep",
						"Reads an import file and extracts the resources",
						BulkImportFilePartitionResult.class,
						fetchPartitionedFilesStep())
				.addIntermediateStep(
						"ReadInResourcesFromFileStep",
						"Reads the import file to get the serialized bundles",
						BulkImportRecord.class,
						readInResourcesFromFileStep())
				.addLastStep(
						"WriteBundleForImportStep",
						"Parses the bundle from previous step and writes it to the dv",
						writeBundleForImportStep())
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
