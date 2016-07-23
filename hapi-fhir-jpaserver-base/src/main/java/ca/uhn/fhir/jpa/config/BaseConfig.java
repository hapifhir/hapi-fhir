package ca.uhn.fhir.jpa.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.term.BaseHapiTerminologySvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
public class BaseConfig implements SchedulingConfigurer {

	private static FhirContext ourFhirContextDstu1;
	private static FhirContext ourFhirContextDstu2;
	private static FhirContext ourFhirContextDstu2Hl7Org;
	private static FhirContext ourFhirContextDstu3;

	@Resource
	private ApplicationContext myAppCtx;

	@Autowired
	protected Environment myEnv;

	@Override
	public void configureTasks(ScheduledTaskRegistrar theTaskRegistrar) {
		theTaskRegistrar.setTaskScheduler(taskScheduler());
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		return new DatabaseBackedPagingProvider(10);
	}

	@Bean(name = "myFhirContextDstu1")
	@Lazy
	public FhirContext fhirContextDstu1() {
		if (ourFhirContextDstu1 == null) {
			ourFhirContextDstu1 = FhirContext.forDstu1();
		}
		return ourFhirContextDstu1;
	}

	@Bean(name = "myFhirContextDstu2")
	@Lazy
	public FhirContext fhirContextDstu2() {
		if (ourFhirContextDstu2 == null) {
			ourFhirContextDstu2 = FhirContext.forDstu2();
		}
		return ourFhirContextDstu2;
	}

	@Bean(name = "myFhirContextDstu2Hl7Org")
	@Lazy
	public FhirContext fhirContextDstu2Hl7Org() {
		if (ourFhirContextDstu2Hl7Org == null) {
			ourFhirContextDstu2Hl7Org = FhirContext.forDstu2Hl7Org();
		}
		return ourFhirContextDstu2Hl7Org;
	}

	@Bean(name = "myFhirContextDstu3")
	@Lazy
	public FhirContext fhirContextDstu3() {
		if (ourFhirContextDstu3 == null) {
			ourFhirContextDstu3 = FhirContext.forDstu3();
			
			// Don't strip versions in some places
			ParserOptions parserOptions = ourFhirContextDstu3.getParserOptions();
			parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		}
		return ourFhirContextDstu3;
	}

	@Bean(autowire=Autowire.BY_TYPE)
	public StaleSearchDeletingSvc staleSearchDeletingSvc() {
		return new StaleSearchDeletingSvc();
	}

	@Bean()
	public ScheduledExecutorFactoryBean scheduledExecutorService() {
		ScheduledExecutorFactoryBean b = new ScheduledExecutorFactoryBean();
		b.setPoolSize(5);
		return b;
	}
	
	@Bean
	public TaskScheduler taskScheduler() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler();
		retVal.setConcurrentExecutor(scheduledExecutorService().getObject());
		retVal.setScheduledExecutor(scheduledExecutorService().getObject());
		return retVal;
//		ThreadPoolTaskScheduler retVal = new ThreadPoolTaskScheduler();
//		retVal.setPoolSize(5);
//		return retVal;
	}
	
	// @PostConstruct
	// public void wireResourceDaos() {
	// Map<String, IDao> daoBeans = myAppCtx.getBeansOfType(IDao.class);
	// List bean = myAppCtx.getBean("myResourceProvidersDstu2", List.class);
	// for (IDao next : daoBeans.values()) {
	// next.setResourceDaos(bean);
	// }
	// }

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
