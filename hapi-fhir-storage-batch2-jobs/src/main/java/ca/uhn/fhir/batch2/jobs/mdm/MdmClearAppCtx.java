package ca.uhn.fhir.batch2.jobs.mdm;

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
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmClearAppCtx {

	public static final String JOB_Mdm = "Mdm";

	@Bean
	public JobDefinition<MdmJobParameters> mdmJobDefinition(DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings) {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_Mdm)
			.setJobDescription("Clear mdm links and golden resrouces")
			.setJobDefinitionVersion(1)
			.setParametersType(MdmJobParameters.class)
			.setParametersValidator(MdmJobParametersValidator(theDaoRegistry, theMdmSettings))
			.gatedExecution()
			.addFirstStep(
				"generate-ranges",
				"Generate date ranges to Mdm Clear",
				MdmChunkRangeJson.class,
				mdmGenerateRangeChunksStep())
			.addIntermediateStep(
				"find-golden-resource-ids",
				"Load ids of golden resources to be cleared",
				ResourceIdListWorkChunkJson.class,
				loadGoldenIdsStep())
			.addLastStep("Mdm",
				"Remove golden resources and mdm links",
				mdmClearStep()
			)
			.build();
	}

	@Bean
	public MdmJobParametersValidator MdmJobParametersValidator(DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings) {
		return new MdmJobParametersValidator(theDaoRegistry, theMdmSettings);
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
	public LoadGoldenIdsStep loadGoldenIdsStep() {
		return new LoadGoldenIdsStep();
	}

	@Bean
	public MdmClearProvider MdmProvider(FhirContext theFhirContext, IJobCoordinator theJobCoordinator, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new MdmClearProvider(theFhirContext, theJobCoordinator, theRequestPartitionHelperSvc);
	}

}
