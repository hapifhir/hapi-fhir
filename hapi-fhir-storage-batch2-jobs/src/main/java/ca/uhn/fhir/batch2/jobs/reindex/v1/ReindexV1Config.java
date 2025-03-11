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
package ca.uhn.fhir.batch2.jobs.reindex.v1;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
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

@Deprecated(forRemoval = true, since = "7.6.0")
@Configuration
public class ReindexV1Config {
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
	@Qualifier("reindexGenerateRangeChunkStepV1")
	private IJobStepWorker<ReindexJobParameters, VoidModel, ChunkRangeJson> myReindexGenerateRangeChunkStep;

	@Autowired
	@Qualifier("reindexLoadIdsStepV1")
	private IJobStepWorker<ReindexJobParameters, ChunkRangeJson, ResourceIdListWorkChunkJson> myReindexLoadIdsStep;

	@Autowired
	private ReindexJobParametersValidatorV1 myReindexJobParametersValidatorV1;

	// Version 1
	@Bean
	public JobDefinition<ReindexJobParameters> reindexJobDefinitionV1() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REINDEX)
				.setJobDescription("Reindex resources")
				.setJobDefinitionVersion(1)
				.setParametersType(ReindexJobParameters.class)
				.setParametersValidator(myReindexJobParametersValidatorV1)
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
				.addLastStep("reindex", "Start the resource reindex", reindexStepV1())
				.build();
	}

	@Bean
	public ReindexStepV1 reindexStepV1() {
		return new ReindexStepV1(myHapiTransactionService, mySystemDao, myRegistry, myIdHelperService);
	}

	@Bean("reindexGenerateRangeChunkStepV1")
	public IJobStepWorker<ReindexJobParameters, VoidModel, ChunkRangeJson> reindexGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("reindexLoadIdsStepV1")
	public IJobStepWorker<ReindexJobParameters, ChunkRangeJson, ResourceIdListWorkChunkJson> reindexLoadIdsStep(
			IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep<>(theBatch2DaoSvc);
	}

	@Bean
	public ReindexJobParametersValidatorV1 reindexJobParametersValidatorV1(IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReindexJobParametersValidatorV1(
				new UrlListValidator(ProviderConstants.OPERATION_REINDEX, theBatch2DaoSvc));
	}
}
