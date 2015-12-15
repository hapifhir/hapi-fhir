package ca.uhn.fhir.jpa.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
public class BaseConfig implements SchedulingConfigurer {

	@Autowired
	protected Environment myEnv;

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Resource
	private ApplicationContext myAppCtx;

	@Override
	public void configureTasks(ScheduledTaskRegistrar theTaskRegistrar) {
		theTaskRegistrar.setTaskScheduler(taskScheduler());
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler retVal = new ThreadPoolTaskScheduler();
		retVal.setPoolSize(5);
		return retVal;
	}

	// @PostConstruct
	// public void wireResourceDaos() {
	// Map<String, IDao> daoBeans = myAppCtx.getBeansOfType(IDao.class);
	// List bean = myAppCtx.getBean("myResourceProvidersDstu2", List.class);
	// for (IDao next : daoBeans.values()) {
	// next.setResourceDaos(bean);
	// }
	// }

}
