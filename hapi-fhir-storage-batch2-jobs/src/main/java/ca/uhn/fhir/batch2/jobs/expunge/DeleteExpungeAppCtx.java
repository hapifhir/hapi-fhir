package ca.uhn.fhir.batch2.jobs.expunge;

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

import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteExpungeAppCtx {

	public static final String JOB_DELETE_EXPUNGE = "DELETE_EXPUNGE";


	@Bean
	public JobDefinition<DeleteExpungeJobParameters> expungeJobDefinition(
		IBatch2DaoSvc theBatch2DaoSvc,
		HapiTransactionService theHapiTransactionService,
		IDeleteExpungeSvc theDeleteExpungeSvc
	) {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_DELETE_EXPUNGE)
			.setJobDescription("Expunge resources")
			.setJobDefinitionVersion(1)
			.setParametersType(DeleteExpungeJobParameters.class)
			.setParametersValidator(expungeJobParametersValidator(theBatch2DaoSvc))
			.gatedExecution()
			.addFirstStep(
				"generate-ranges",
				"Generate data ranges to expunge",
				PartitionedUrlChunkRangeJson.class,
				expungeGenerateRangeChunksStep())
			.addIntermediateStep(
				"load-ids",
				"Load IDs of resources to expunge",
				ResourceIdListWorkChunkJson.class,
				new LoadIdsStep(theBatch2DaoSvc))
			.addLastStep("expunge",
				"Perform the resource expunge",
				expungeStep(theHapiTransactionService, theDeleteExpungeSvc)
			)
			.build();
	}

	@Bean
	public DeleteExpungeJobParametersValidator expungeJobParametersValidator(IBatch2DaoSvc theBatch2DaoSvc) {
		return new DeleteExpungeJobParametersValidator(new UrlListValidator(ProviderConstants.OPERATION_EXPUNGE, theBatch2DaoSvc));
	}

	@Bean
	public DeleteExpungeStep expungeStep(HapiTransactionService theHapiTransactionService, IDeleteExpungeSvc theDeleteExpungeSvc) {
		return new DeleteExpungeStep(theHapiTransactionService, theDeleteExpungeSvc);
	}

	@Bean
	public GenerateRangeChunksStep expungeGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}

	@Bean
	public DeleteExpungeProvider deleteExpungeProvider(FhirContext theFhirContext, IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		return new DeleteExpungeProvider(theFhirContext, theDeleteExpungeJobSubmitter);
	}
}
