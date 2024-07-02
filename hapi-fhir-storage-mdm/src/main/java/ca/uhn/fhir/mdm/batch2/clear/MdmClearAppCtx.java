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
package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.batch2.LoadGoldenIdsStep;
import ca.uhn.fhir.mdm.batch2.MdmGenerateRangeChunksStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmClearAppCtx {
	public static final String JOB_MDM_CLEAR = "MDM_CLEAR";
	public static final String MDM_CLEAR_JOB_BEAN_NAME = "mdmClearJobDefinition";

	@Bean(name = MDM_CLEAR_JOB_BEAN_NAME)
	public JobDefinition<MdmClearJobParameters> mdmClearJobDefinition(
			DaoRegistry theDaoRegistry,
			IGoldenResourceSearchSvc theGoldenResourceSearchSvc,
			IMdmSettings theMdmSettings) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_MDM_CLEAR)
				.setJobDescription("Clear mdm links and golden resrouces")
				.setJobDefinitionVersion(1)
				.setParametersType(MdmClearJobParameters.class)
				.setParametersValidator(MdmJobParametersValidator(theDaoRegistry, theMdmSettings))
				.gatedExecution()
				.addFirstStep(
						"generate-ranges",
						"Generate date ranges to Mdm Clear",
						ChunkRangeJson.class,
						mdmGenerateRangeChunksStep())
				.addIntermediateStep(
						"find-golden-resource-ids",
						"Load ids of golden resources to be cleared",
						ResourceIdListWorkChunkJson.class,
						loadGoldenIdsStep(theGoldenResourceSearchSvc))
				.addLastStep(
						"remove-golden-resources-and-links", "Remove golden resources and mdm links", mdmClearStep())
				.build();
	}

	@Bean
	public MdmClearJobParametersValidator MdmJobParametersValidator(
			DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings) {
		return new MdmClearJobParametersValidator(theDaoRegistry, theMdmSettings);
	}

	@Bean
	public MdmClearStep mdmClearStep() {
		return new MdmClearStep();
	}

	@Bean
	public MdmGenerateRangeChunksStep mdmGenerateRangeChunksStep() {
		return new MdmGenerateRangeChunksStep();
	}

	@Bean
	public LoadGoldenIdsStep loadGoldenIdsStep(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		return new LoadGoldenIdsStep(theGoldenResourceSearchSvc);
	}
}
