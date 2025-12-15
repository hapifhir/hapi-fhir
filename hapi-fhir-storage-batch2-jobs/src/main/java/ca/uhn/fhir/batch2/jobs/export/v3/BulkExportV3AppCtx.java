/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportWorkPackageJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.batch2.jobs.export.BulkExportAppCtx.WRITE_TO_BINARIES;

@Configuration
public class BulkExportV3AppCtx {

	@Bean("bulkExportV3JobDefinition")
	public JobDefinition<BulkExportJobParameters> bulkExportJobDefinition() {
		JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
		builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		builder.setJobDescription("FHIR Bulk Export");
		builder.setJobDefinitionVersion(3);

		JobDefinition<BulkExportJobParameters> def = builder.setParametersType(BulkExportJobParameters.class)
				// validator
				.setParametersValidator(bulkExportJobParametersValidator())
				.gatedExecution()
				// first step - load in (all) ids and create id chunks of 1000 each
				.addFirstStep(
						"generate-work-packages",
						"Generate export work packages for processing",
						BulkExportWorkPackageJson.class,
						generateWorkPackagesStep())
				// load in (all) ids and create id chunks of 1000 each
				.addIntermediateStep(
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

	@Bean("bulkExportV3GenerateWorkPackagesStep")
	public GenerateWorkPackagesStep generateWorkPackagesStep() {
		return new GenerateWorkPackagesStep();
	}

	@Bean("bulkExportV3JobParametersValidator")
	public BulkExportJobParametersValidator bulkExportJobParametersValidator() {
		return new BulkExportJobParametersValidator();
	}

	@Bean("bulkExportV3fetchResourceIdsStep")
	public FetchResourceIdsV3Step fetchResourceIdsStep() {
		return new FetchResourceIdsV3Step();
	}

	@Bean("bulkExportV3ExpandResourceAndWriteBinaryStep")
	public ExpandResourceAndWriteBinaryStep expandResourceAndWriteBinaryStep() {
		return new ExpandResourceAndWriteBinaryStep();
	}

	@Bean("bulkExportV3CreateReportStep")
	public BulkExportCreateReportStep createReportStep() {
		return new BulkExportCreateReportStep();
	}
}
