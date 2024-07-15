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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReindexAppCtx {

	public static final String JOB_REINDEX = "REINDEX";

	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinition(IBatch2DaoSvc theBatch2DaoSvc) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REINDEX)
				.setJobDescription("Reindex resources")
				.setJobDefinitionVersion(1)
				.setParametersType(ReindexJobParameters.class)
				.setParametersValidator(reindexJobParametersValidator(theBatch2DaoSvc))
				.gatedExecution()
				.addFirstStep(
						"generate-ranges",
						"Generate data ranges to reindex",
						ChunkRangeJson.class,
						reindexGenerateRangeChunksStep())
				.addIntermediateStep(
						"load-ids",
						"Load IDs of resources to reindex",
						ResourceIdListWorkChunkJson.class,
						reindexLoadIdsStep(theBatch2DaoSvc))
				.addLastStep("reindex", "Perform the resource reindex", reindexStep())
				.build();
	}

	@Bean
	public IJobStepWorker<ReindexJobParameters, VoidModel, ChunkRangeJson> reindexGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean
	public IJobStepWorker<ReindexJobParameters, ChunkRangeJson, ResourceIdListWorkChunkJson> reindexLoadIdsStep(
			IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep<>(theBatch2DaoSvc);
	}

	@Bean
	public ReindexJobParametersValidator reindexJobParametersValidator(IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReindexJobParametersValidator(
				new UrlListValidator(ProviderConstants.OPERATION_REINDEX, theBatch2DaoSvc));
	}

	@Bean
	public ReindexStep reindexStep() {
		return new ReindexStep();
	}

	@Bean
	public ReindexProvider reindexProvider(
			FhirContext theFhirContext,
			IJobCoordinator theJobCoordinator,
			IJobPartitionProvider theJobPartitionHandler,
			UrlPartitioner theUrlPartitioner) {
		return new ReindexProvider(theFhirContext, theJobCoordinator, theJobPartitionHandler, theUrlPartitioner);
	}
}
