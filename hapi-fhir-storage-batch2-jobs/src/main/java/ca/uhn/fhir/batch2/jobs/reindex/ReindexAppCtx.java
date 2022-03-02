package ca.uhn.fhir.batch2.jobs.reindex;

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
import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.jobs.imprt.ConsumeFilesStep;
import ca.uhn.fhir.batch2.jobs.imprt.FetchFilesStep;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReindexAppCtx {

	private static final String JOB_REINDEX = "REINDEX";

	@Bean
	public JobDefinition reindexJobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_REINDEX)
			.setJobDescription("Reindex resources")
			.setJobDefinitionVersion(1)
			.setParametersType(ReindexJobParameters.class)
			.addFirstStep(
				"generate-ranges",
				"Generate data ranges to reindex",
				ReindexChunkRange.class,
				reindexGenerateRangeChunksStep())
			.addIntermediateStep(
				"process-files",
				"Process files",
				ReindexChunkRange.class,
				ReindexChunkIds.class,
				loadIdsStep())
			.build();
	}

	@Bean
	public GenerateRangeChunksStep reindexGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}

	@Bean
	public LoadIdsStep loadIdsStep() {
		return new LoadIdsStep();
	}
}
