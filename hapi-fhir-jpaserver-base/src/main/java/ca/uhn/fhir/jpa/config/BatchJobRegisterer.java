package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Map;

public class BatchJobRegisterer {

	@Autowired
	private ApplicationContext myApplicationContext;


	@PostConstruct
	public void start() throws DuplicateJobException {

		Map<String, Job> batchJobs = myApplicationContext.getBeansOfType(Job.class);
		JobRegistry jobRegistry = myApplicationContext.getBean(JobRegistry.class);

		for (Map.Entry<String, Job> next : batchJobs.entrySet()) {
			jobRegistry.register(new JobFactory() {
				@Override
				public Job createJob() {
					return next.getValue();
				}

				@Override
				public String getJobName() {
					return next.getKey();
				}
			});
		}

	}

}
