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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BulkExportAppCtx {

	public static final String WRITE_TO_BINARIES = "write-to-binaries";
	public static final String CREATE_REPORT_STEP = "create-report-step";

	@Bean
	public JobDefinition bulkExportJobDefinition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(1);

		JobDefinition def = builder.setParametersType(BulkExportJobParameters.class)
				// validator
				.setParametersValidator(bulkExportJobParametersValidator())
				.gatedExecution()
				// first step - load in (all) ids and create id chunks of 1000 each
				.addFirstStep(
						"fetch-resources",
						"Fetches resource PIDs for exporting",
						ResourceIdList.class,
						fetchResourceIdsStep())
				// expand out - fetch resources
				.addIntermediateStep(
						"expand-resources", "Expand out resources", ExpandedResourcesList.class, expandResourcesStep())
				// write binaries and save to db
				.addIntermediateStep(
						WRITE_TO_BINARIES,
						"Writes the expanded resources to the binaries and saves",
						BulkExportBinaryFileId.class,
						writeBinaryStep())
				// finalize the job (set to complete)
				.addFinalReducerStep(
						CREATE_REPORT_STEP,
						"Creates the output report from a bulk export job",
						BulkExportJobResults.class,
						createReportStep())
				.build();

		return def;
	}

	@Bean
	public JobDefinition bulkExportJobV2Definition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(2);

		JobDefinition def = builder.setParametersType(BulkExportJobParameters.class)
				// validator
				.setParametersValidator(bulkExportJobParametersValidator())
				.gatedExecution()
				// first step - load in (all) ids and create id chunks of 1000 each
				.addFirstStep(
						"fetch-resources",
						"Fetches resource PIDs for exporting",
						ResourceIdList.class,
						fetchResourceIdsStep())
				// expand out - fetch resources
				// and write binaries and save to db
				.addIntermediateStep(
						WRITE_TO_BINARIES,
						"Writes the expanded resources to the binaries and saves",
						BulkExportBinaryFileId.class,
						expandResourceAndWriteBinaryStep())
				// finalize the job (set to complete)
				.addFinalReducerStep(
						"create-report-step",
						"Creates the output report from a bulk export job",
						BulkExportJobResults.class,
						createReportStep())
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

	/**
	 * Note, this bean is only used for version 1 of the bulk export job definition
	 */
	@Bean
	public ExpandResourcesStep expandResourcesStep() {
		return new ExpandResourcesStep();
	}

	/**
	 * Note, this bean is only used for version 1 of the bulk export job definition
	 */
	@Bean
	public WriteBinaryStep writeBinaryStep() {
		return new WriteBinaryStep();
	}

	/**
	 * Note, this bean is only used for version 2 of the bulk export job definition
	 */
	@Bean
	public ExpandResourceAndWriteBinaryStep expandResourceAndWriteBinaryStep() {
		return new ExpandResourceAndWriteBinaryStep();
	}

	@Bean
	@Scope("prototype")
	public BulkExportCreateReportStep createReportStep() {
		return new BulkExportCreateReportStep();
	}
}
