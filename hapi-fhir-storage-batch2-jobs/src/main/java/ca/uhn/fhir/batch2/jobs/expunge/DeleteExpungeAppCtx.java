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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.UrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteExpungeAppCtx {

	public static final String JOB_DELETE_EXPUNGE = "DELETE_EXPUNGE";


	@Bean
	public JobDefinition<DeleteExpungeJobParameters> expungeJobDefinition(IBatch2DaoSvc theBatch2DaoSvc) {
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
				UrlChunkRangeJson.class,
				expungeGenerateRangeChunksStep())
			.addIntermediateStep(
				"load-ids",
				"Load IDs of resources to expunge",
				ResourceIdListWorkChunkJson.class,
				loadIdsStep(theBatch2DaoSvc))
			.addLastStep("expunge",
				"Perform the resource expunge",
				expungeStep()
			)
			.build();
	}

	@Bean
	public DeleteExpungeJobParametersValidator expungeJobParametersValidator(IBatch2DaoSvc theBatch2DaoSvc) {
		return new DeleteExpungeJobParametersValidator(new UrlListValidator(ProviderConstants.OPERATION_EXPUNGE, theBatch2DaoSvc));
	}

	@Bean
	public DeleteExpungeStep expungeStep() {
		return new DeleteExpungeStep();
	}

	@Bean
	public GenerateRangeChunksStep expungeGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep();
	}

	@Bean
	public LoadIdsStep loadIdsStep(IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep(theBatch2DaoSvc);
	}

	@Bean
	public DeleteExpungeProvider expungeProvider(FhirContext theFhirContext, IJobCoordinator theJobCoordinator, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new DeleteExpungeProvider(theFhirContext, theJobCoordinator, theRequestPartitionHelperSvc);
	}

}
