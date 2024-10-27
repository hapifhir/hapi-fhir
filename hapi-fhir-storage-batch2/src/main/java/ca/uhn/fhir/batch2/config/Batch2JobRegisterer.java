/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.config;

import ca.uhn.fhir.IHapiBootOrder;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.util.Map;

public class Batch2JobRegisterer {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Autowired
	private ApplicationContext myApplicationContext;

	// The timing of this call is sensitive.  It needs to be called after all the job definition beans have been created
	// but before any jobs are run.  E.g. ValidationDataInitializerSvcImpl can start a REINDEX job, so we use an
	// EventListener
	// so we know all the JobDefinition beans have been created, but we use @Order(IHapiBootOrder.ADD_JOB_DEFINITIONS)
	// to ensure it is called
	// before any other EventListeners that might start a job.
	@EventListener(classes = ContextRefreshedEvent.class)
	@Order(IHapiBootOrder.ADD_JOB_DEFINITIONS)
	public void start() {
		Map<String, JobDefinition> batchJobs = myApplicationContext.getBeansOfType(JobDefinition.class);
		JobDefinitionRegistry jobRegistry = myApplicationContext.getBean(JobDefinitionRegistry.class);

		for (Map.Entry<String, JobDefinition> next : batchJobs.entrySet()) {
			JobDefinition<?> jobDefinition = next.getValue();
			ourLog.info(
					"Registering Batch2 Job Definition: {} / {}",
					jobDefinition.getJobDefinitionId(),
					jobDefinition.getJobDefinitionVersion());
			jobRegistry.addJobDefinition(jobDefinition);
		}
	}
}
