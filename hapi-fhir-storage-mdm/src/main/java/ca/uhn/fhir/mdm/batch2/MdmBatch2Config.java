package ca.uhn.fhir.mdm.batch2;

/*-
 * #%L
 * hapi-fhir-storage-mdm
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

import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

import static ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx.MDM_CLEAR_JOB_BEAN_NAME;
import static ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx.MDM_SUBMIT_JOB_BEAN_NAME;

@Configuration
@Import({
	MdmClearAppCtx.class,
	MdmSubmitAppCtx.class
})
public class MdmBatch2Config {
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;

	@Autowired
	ApplicationContext myApplicationContext;

	@PostConstruct
	public void start() {
		JobDefinition clearJobDefinition = myApplicationContext.getBean(MDM_CLEAR_JOB_BEAN_NAME, JobDefinition.class);
		myJobDefinitionRegistry.addJobDefinitionIfNotRegistered(clearJobDefinition);
		JobDefinition submitJobDefinition = myApplicationContext.getBean(MDM_SUBMIT_JOB_BEAN_NAME, JobDefinition.class);
		myJobDefinitionRegistry.addJobDefinitionIfNotRegistered(clearJobDefinition);
	}
}
