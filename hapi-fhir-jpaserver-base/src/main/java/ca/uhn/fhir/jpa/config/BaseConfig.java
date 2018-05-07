package ca.uhn.fhir.jpa.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.search.*;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.sp.SearchParamPresenceSvcImpl;
import ca.uhn.fhir.jpa.subscription.email.SubscriptionEmailInterceptor;
import ca.uhn.fhir.jpa.subscription.resthook.SubscriptionRestHookInterceptor;
import ca.uhn.fhir.jpa.subscription.websocket.SubscriptionWebsocketInterceptor;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
public class BaseConfig implements SchedulingConfigurer {

	public static final String TASK_EXECUTOR_NAME = "hapiJpaTaskExecutor";

	@Autowired
	protected Environment myEnv;
	@Resource
	private ApplicationContext myAppCtx;

	@Override
	public void configureTasks(ScheduledTaskRegistrar theTaskRegistrar) {
		theTaskRegistrar.setTaskScheduler(taskScheduler());
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		DatabaseBackedPagingProvider retVal = new DatabaseBackedPagingProvider();
		return retVal;
	}

	@Bean()
	public ScheduledExecutorFactoryBean scheduledExecutorService() {
		ScheduledExecutorFactoryBean b = new ScheduledExecutorFactoryBean();
		b.setPoolSize(5);
		return b;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl();
	}

	@Bean
	public ISearchParamPresenceSvc searchParamPresenceSvc() {
		return new SearchParamPresenceSvcImpl();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IStaleSearchDeletingSvc staleSearchDeletingSvc() {
		return new StaleSearchDeletingSvcImpl();
	}

	/**
	 * Note: If you're going to use this, you need to provide a bean
	 * of type {@link ca.uhn.fhir.jpa.subscription.email.IEmailSender}
	 * in your own Spring config
	 */
	@Bean
	@Lazy
	public SubscriptionEmailInterceptor subscriptionEmailInterceptor() {
		return new SubscriptionEmailInterceptor();
	}

	@Bean
	@Lazy
	public SubscriptionRestHookInterceptor subscriptionRestHookInterceptor() {
		return new SubscriptionRestHookInterceptor();
	}

	@Bean
	@Lazy
	public SubscriptionWebsocketInterceptor subscriptionWebsocketInterceptor() {
		return new SubscriptionWebsocketInterceptor();
	}

	@Bean(name=TASK_EXECUTOR_NAME)
	public TaskScheduler taskScheduler() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler();
		retVal.setConcurrentExecutor(scheduledExecutorService().getObject());
		retVal.setScheduledExecutor(scheduledExecutorService().getObject());
		return retVal;
	}

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}


}
