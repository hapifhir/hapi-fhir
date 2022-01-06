package ca.uhn.fhir.jpa.batch.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch Task Processor
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

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


public class NonPersistedBatchConfigurer extends DefaultBatchConfigurer {
	@Autowired
	@Qualifier("hapiTransactionManager")
	private PlatformTransactionManager myHapiPlatformTransactionManager;

	@Autowired
	@Qualifier(BatchConstants.JOB_LAUNCHING_TASK_EXECUTOR)
	private TaskExecutor myTaskExecutor;

	private MapJobRepositoryFactoryBean myJobRepositoryFactory;

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myHapiPlatformTransactionManager;
	}


	@Override
	protected JobRepository createJobRepository() throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
		factory.setTransactionManager(this.getTransactionManager());
		factory.afterPropertiesSet();
		myJobRepositoryFactory = factory;
		return factory.getObject();
	}

	@Override
	public JobExplorer createJobExplorer() throws Exception {
		MapJobExplorerFactoryBean jobExplorerFactoryBean = new MapJobExplorerFactoryBean(myJobRepositoryFactory);
		jobExplorerFactoryBean.afterPropertiesSet();
		return jobExplorerFactoryBean.getObject();
	}

	@Override
	protected JobLauncher createJobLauncher() throws Exception {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setTaskExecutor(myTaskExecutor);
		launcher.setJobRepository(getJobRepository());
		launcher.afterPropertiesSet();
		return launcher;
	}
}
