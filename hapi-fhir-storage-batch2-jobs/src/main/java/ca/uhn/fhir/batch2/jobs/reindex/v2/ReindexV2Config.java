/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex.v2;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils.JOB_REINDEX;

@Configuration
public class ReindexV2Config {

	@Autowired
	private ReindexJobService myReindexJobService;

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	@Autowired
	private DaoRegistry myRegistry;

	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	@Autowired
	@Qualifier("reindexGenerateRangeChunkStepV2")
	private IJobStepWorker<ReindexJobParameters, VoidModel, ChunkRangeJson> myReindexGenerateRangeChunkStep;

	@Autowired
	@Qualifier("reindexLoadIdsStepV2")
	private IJobStepWorker<ReindexJobParameters, ChunkRangeJson, ResourceIdListWorkChunkJson> myReindexLoadIdsStep;

	@Autowired
	private ReindexJobParametersValidatorV2 myReindexJobParametersValidator;

	// Version 2
	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinitionV2() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REINDEX)
				.setJobDescription("Reindex resources")
				.setJobDefinitionVersion(2)
				.setParametersType(ReindexJobParameters.class)
				.setParametersValidator(myReindexJobParametersValidator)
				.gatedExecution()
				.addFirstStep(
						"generate-ranges",
						"Generate data ranges to reindex",
						ChunkRangeJson.class,
						myReindexGenerateRangeChunkStep)
				.addIntermediateStep(
						"load-ids",
						"Load IDs of resources to reindex",
						ResourceIdListWorkChunkJson.class,
						myReindexLoadIdsStep)
				.addIntermediateStep(
						"reindex-start", "Perform the resource reindex", ReindexResults.class, reindexStepV2())
				.addLastStep("reindex-pending-work", "Waits for reindex work to complete.", pendingWorkStep())
				.build();
	}

	@Bean
	public CheckPendingReindexWorkStep pendingWorkStep() {
		return new CheckPendingReindexWorkStep(myReindexJobService);
	}

	@Bean
	public ReindexStepV2 reindexStepV2() {
		return new ReindexStepV2(
				myReindexJobService, myHapiTransactionService, mySystemDao, myRegistry, myIdHelperService);
	}

	@Bean("reindexGenerateRangeChunkStepV2")
	public IJobStepWorker<ReindexJobParameters, VoidModel, ChunkRangeJson> reindexGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("reindexLoadIdsStepV2")
	public IJobStepWorker<ReindexJobParameters, ChunkRangeJson, ResourceIdListWorkChunkJson> reindexLoadIdsStep(
			IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep<>(theBatch2DaoSvc);
	}

	@Bean
	public ReindexJobParametersValidatorV2 reindexJobParametersValidatorV2(IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReindexJobParametersValidatorV2(
				new UrlListValidator(ProviderConstants.OPERATION_REINDEX, theBatch2DaoSvc));
	}
}
