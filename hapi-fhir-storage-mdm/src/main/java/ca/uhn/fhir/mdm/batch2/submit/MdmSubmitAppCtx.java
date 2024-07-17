/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmSubmitAppCtx {

	public static final String MDM_SUBMIT_JOB_BEAN_NAME = "mdmSubmitJobDefinition";
	public static String MDM_SUBMIT_JOB = "MDM_SUBMIT";

	@Bean(name = MDM_SUBMIT_JOB_BEAN_NAME)
	public JobDefinition<MdmSubmitJobParameters> mdmSubmitJobDefinition(
			IBatch2DaoSvc theBatch2DaoSvc,
			MatchUrlService theMatchUrlService,
			FhirContext theFhirContext,
			IMdmSettings theMdmSettings) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(MDM_SUBMIT_JOB)
				.setJobDescription("MDM Batch Submission")
				.setJobDefinitionVersion(1)
				.setParametersType(MdmSubmitJobParameters.class)
				.setParametersValidator(
						mdmSubmitJobParametersValidator(theMatchUrlService, theFhirContext, theMdmSettings))
				.addFirstStep(
						"generate-ranges",
						"generate data ranges to submit to mdm",
						ChunkRangeJson.class,
						submitGenerateRangeChunksStep())
				.addIntermediateStep(
						"load-ids",
						"Load the IDs",
						ResourceIdListWorkChunkJson.class,
						mdmSubmitLoadIdsStep(theBatch2DaoSvc))
				.addLastStep(
						"inflate-and-submit-resources",
						"Inflate and Submit resources",
						mdmInflateAndSubmitResourcesStep())
				.build();
	}

	@Bean
	public MdmSubmitJobParametersValidator mdmSubmitJobParametersValidator(
			MatchUrlService theMatchUrlService, FhirContext theFhirContext, IMdmSettings theMdmSettings) {
		return new MdmSubmitJobParametersValidator(theMdmSettings, theMatchUrlService, theFhirContext);
	}

	@Bean
	public GenerateRangeChunksStep<MdmSubmitJobParameters> submitGenerateRangeChunksStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean
	public LoadIdsStep<MdmSubmitJobParameters> mdmSubmitLoadIdsStep(IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep<>(theBatch2DaoSvc);
	}

	@Bean
	public IJobStepWorker<MdmSubmitJobParameters, ResourceIdListWorkChunkJson, VoidModel>
			mdmInflateAndSubmitResourcesStep() {
		return new MdmInflateAndSubmitResourcesStep();
	}
}
