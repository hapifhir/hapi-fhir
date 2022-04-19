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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReindexAppCtx {

	public static final String JOB_REINDEX = "REINDEX";

	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_REINDEX)
			.setJobDescription("Reindex resources")
			.setJobDefinitionVersion(1)
			.setParametersType(ReindexJobParameters.class)
			.setParametersValidator(reindexJobParametersValidator())
			.gatedExecution()
			.addFirstStep(
				"generate-ranges",
				"Generate data ranges to reindex",
				ReindexChunkRange.class,
				reindexGenerateRangeChunksStep())
			.addIntermediateStep(
				"load-ids",
				"Load IDs of resources to reindex",
				ReindexChunkIds.class,
				loadIdsStep())
			.addLastStep("reindex",
				"Perform the resource reindex",
				reindexStep()
			)
			.build();
	}

	@Bean
	public ReindexJobParametersValidator reindexJobParametersValidator() {
		return new ReindexJobParametersValidator();
	}

	@Bean
	public ReindexStep reindexStep() {
		return new ReindexStep();
	}

	@Bean
	public GenerateRangeChunksStep reindexGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}

	@Bean
	public LoadIdsStep loadIdsStep() {
		return new LoadIdsStep();
	}

	@Bean
	public ReindexProvider reindexProvider(FhirContext theFhirContext, IJobCoordinator theJobCoordinator, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new ReindexProvider(theFhirContext, theJobCoordinator, theRequestPartitionHelperSvc);
	}

}
