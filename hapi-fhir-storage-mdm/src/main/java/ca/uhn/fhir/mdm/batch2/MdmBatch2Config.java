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
package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.jobs.config.BatchCommonCtx;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitJobParameters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx.MDM_CLEAR_JOB_BEAN_NAME;
import static ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx.MDM_SUBMIT_JOB_BEAN_NAME;

@Configuration
@Import({MdmClearAppCtx.class, MdmSubmitAppCtx.class, BatchCommonCtx.class})
public class MdmBatch2Config {
	@Bean
	MdmJobDefinitionLoader mdmJobDefinitionLoader(
			JobDefinitionRegistry theJobDefinitionRegistry,
			@Qualifier(MDM_CLEAR_JOB_BEAN_NAME) JobDefinition<MdmClearJobParameters> theClearJobDefinition,
			@Qualifier(MDM_SUBMIT_JOB_BEAN_NAME) JobDefinition<MdmSubmitJobParameters> theSubmitJobDefinition) {
		return new MdmJobDefinitionLoader(theJobDefinitionRegistry, theClearJobDefinition, theSubmitJobDefinition);
	}
}
