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
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReindexAppCtx {

	public static final String JOB_REINDEX = "REINDEX";

	@Autowired
	private HapiTransactionService myHapiTransactionService;
	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;
	@Autowired
	private DaoRegistry myRegistry;
	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	// Version 2
	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinitionV2(IBatch2DaoSvc theBatch2DaoSvc) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REINDEX)
				.setJobDescription("Reindex resources")
				.setJobDefinitionVersion(2)
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
				.addIntermediateStep("reindex-start",
					"Perform the resource reindex",
					ReindexResults.class,
					reindexStepV2())
				.addLastStep("reindex-pending-work",
					"Waits for reindex work to complete.",
					pendingWorkStep())
				.build();
	}

	// Version 1
	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinitionV1(IBatch2DaoSvc theBatch2DaoSvc) {
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
			.addLastStep("reindex-start",
				"Start the resource reindex",
				reindexStepV1())
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
	public ReindexStepV1 reindexStepV1() {
		return new ReindexStepV1(myHapiTransactionService, mySystemDao, myRegistry, myIdHelperService);
	}

	@Bean
	public ReindexStepV2 reindexStepV2() {
		return new ReindexStepV2(jobService(), myHapiTransactionService, mySystemDao, myRegistry, myIdHelperService);
	}

	@Bean
	public CheckPendingReindexWorkStep pendingWorkStep() {
		return new CheckPendingReindexWorkStep(jobService());
	}

	@Bean
	public ReindexProvider reindexProvider(
		FhirContext theFhirContext,
		IJobCoordinator theJobCoordinator,
		IJobPartitionProvider theJobPartitionHandler) {
		return new ReindexProvider(theFhirContext, theJobCoordinator, theJobPartitionHandler);
	}

	@Bean
	public ReindexJobService jobService() {
		return new ReindexJobService(myRegistry);
	}
}
