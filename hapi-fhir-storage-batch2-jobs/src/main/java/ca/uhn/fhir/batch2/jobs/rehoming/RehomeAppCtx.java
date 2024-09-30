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
package ca.uhn.fhir.batch2.jobs.rehoming;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RehomeAppCtx {

	public static final String JOB_REHOME = "REHOME";

	@Bean
	public JobDefinition<RehomeJobParameters> rehomeJobDefinition(
			IBatch2DaoSvc theBatch2DaoSvc,
			HapiTransactionService theHapiTransactionService,
			IIdHelperService<JpaPid> theIdHelperService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {

		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REHOME)
				.setJobDescription("Rehome references")
				.setJobDefinitionVersion(1)
				.setParametersType(RehomeJobParameters.class)
				.setParametersValidator(rehomeParametersValidator(
						theBatch2DaoSvc, theRequestPartitionHelperSvc))
				.addFirstStep(
						"generate-ranges",
						"Generate data ranges to rehome",
						ChunkRangeJson.class,
						rehomeGenerateRangeChunksStep())
				.addIntermediateStep(
						"load-ids",
						"Load IDs of resources to rehome",
						ResourceIdListWorkChunkJson.class,
						rehomeLoadIdsStep(theBatch2DaoSvc))
				.addLastStep(
						"rehome",
						"Perform the reference rehoming",
						rehomeStep(theHapiTransactionService, theIdHelperService))
				.build();
	}

	@Bean
	public RehomeJobParametersValidator rehomeParametersValidator(
			IBatch2DaoSvc theBatch2DaoSvc,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new RehomeJobParametersValidator(
				new UrlListValidator(ProviderConstants.OPERATION_REHOME, theBatch2DaoSvc),
				theRequestPartitionHelperSvc);
	}

	@Bean
	public LoadIdsStep<RehomeJobParameters> rehomeLoadIdsStep(IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep<>(theBatch2DaoSvc);
	}

	@Bean
	public RehomeStep rehomeStep(
			HapiTransactionService theHapiTransactionService,
			IIdHelperService<JpaPid> theIdHelperService) {
		return new RehomeStep(theHapiTransactionService, theIdHelperService);
	}

	@Bean
	public GenerateRangeChunksStep<RehomeJobParameters> rehomeGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep<>();
	}

}
