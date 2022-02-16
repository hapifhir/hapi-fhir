package ca.uhn.fhir.batch2.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Map;

public class Batch2JobRegisterer {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobRegisterer.class);

	@Autowired
	private ApplicationContext myApplicationContext;


	@PostConstruct
	public void start() throws DuplicateJobException {

		Map<String, JobDefinition> batchJobs = myApplicationContext.getBeansOfType(JobDefinition.class);
		JobDefinitionRegistry jobRegistry = myApplicationContext.getBean(JobDefinitionRegistry.class);

		for (Map.Entry<String, JobDefinition> next : batchJobs.entrySet()) {
			ourLog.info("Registering Batch2 Job Definition: {} / {}", next.getValue().getJobDefinitionId(), next.getValue().getJobDefinitionVersion());
			jobRegistry.addJobDefinition(next.getValue());
		}
	}

}
