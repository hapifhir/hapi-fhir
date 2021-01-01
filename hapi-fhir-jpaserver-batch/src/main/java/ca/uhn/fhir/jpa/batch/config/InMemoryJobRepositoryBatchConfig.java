package ca.uhn.fhir.jpa.batch.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch Task Processor
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

@Component
@Configuration
public class InMemoryJobRepositoryBatchConfig implements BatchConfigurer {

	private PlatformTransactionManager myPlatformTransactionManager;
	private JobLauncher myJobLauncher;
	private JobRepository myJobRepository;
	private JobExplorer myJobExplorer;


	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myPlatformTransactionManager;
	}

	@Override
	public JobRepository getJobRepository() {
		return myJobRepository;
	}

	@Override
	public JobLauncher getJobLauncher() {
		return myJobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() {
		return myJobExplorer;
	}

	@PostConstruct
	public void setup() throws Exception{
		if (myPlatformTransactionManager == null) {
			myPlatformTransactionManager = new ResourcelessTransactionManager();
		}
		MapJobRepositoryFactoryBean jobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(myPlatformTransactionManager);
		jobRepositoryFactoryBean.afterPropertiesSet();
		myJobRepository = jobRepositoryFactoryBean.getObject();

		MapJobExplorerFactoryBean jobExplorerFactoryBean = new MapJobExplorerFactoryBean(jobRepositoryFactoryBean);
		jobExplorerFactoryBean.afterPropertiesSet();
		myJobExplorer = jobExplorerFactoryBean.getObject();

		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(myJobRepository);
		jobLauncher.afterPropertiesSet();
		myJobLauncher = jobLauncher;
	}
}
