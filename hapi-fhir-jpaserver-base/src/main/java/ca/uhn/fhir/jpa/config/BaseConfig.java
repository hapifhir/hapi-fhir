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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.search.*;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.sp.SearchParamPresenceSvcImpl;
import ca.uhn.fhir.jpa.subscription.email.SubscriptionEmailInterceptor;
import ca.uhn.fhir.jpa.subscription.resthook.SubscriptionRestHookInterceptor;
import ca.uhn.fhir.jpa.subscription.websocket.SubscriptionWebsocketInterceptor;
import ca.uhn.fhir.jpa.util.IReindexController;
import ca.uhn.fhir.jpa.util.ReindexController;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.query.criteria.LiteralHandlingMode;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
public abstract class BaseConfig implements SchedulingConfigurer {

	public static final String TASK_EXECUTOR_NAME = "hapiJpaTaskExecutor";

	@Autowired
	protected Environment myEnv;

	@Bean(name = "myDaoRegistry")
	public DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

	@Override
	public void configureTasks(@Nonnull ScheduledTaskRegistrar theTaskRegistrar) {
		theTaskRegistrar.setTaskScheduler(taskScheduler());
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		return new DatabaseBackedPagingProvider();
	}

	/**
	 * This method should be overridden to provide an actual completed
	 * bean, but it provides a partially completed entity manager
	 * factory with HAPI FHIR customizations
	 */
	protected LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean() {
			@Override
			public Map<String, Object> getJpaPropertyMap() {
				Map<String, Object> retVal = super.getJpaPropertyMap();

				if (!retVal.containsKey(AvailableSettings.CRITERIA_LITERAL_HANDLING_MODE)) {
					retVal.put(AvailableSettings.CRITERIA_LITERAL_HANDLING_MODE, LiteralHandlingMode.BIND);
				}

				if (!retVal.containsKey(AvailableSettings.CONNECTION_HANDLING)) {
					retVal.put(AvailableSettings.CONNECTION_HANDLING, PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_HOLD);
				}

				/*
				 * Set some performance options
				 */

				if (!retVal.containsKey(AvailableSettings.STATEMENT_BATCH_SIZE)) {
					retVal.put(AvailableSettings.STATEMENT_BATCH_SIZE, "30");
				}

				if (!retVal.containsKey(AvailableSettings.ORDER_INSERTS)) {
					retVal.put(AvailableSettings.ORDER_INSERTS, "true");
				}

				if (!retVal.containsKey(AvailableSettings.ORDER_UPDATES)) {
					retVal.put(AvailableSettings.ORDER_UPDATES, "true");
				}

				if (!retVal.containsKey(AvailableSettings.BATCH_VERSIONED_DATA)) {
					retVal.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
				}

				return retVal;
			}


		};
		configureEntityManagerFactory(retVal, fhirContext());
		return retVal;
	}

	public abstract FhirContext fhirContext();

	@Bean
	public ICacheWarmingSvc cacheWarmingSvc() {
		return new CacheWarmingSvcImpl();
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	public HibernateJpaDialect hibernateJpaDialectInstance() {
		return new HibernateJpaDialect();
	}

	@Bean
	public IReindexController reindexController() {
		return new ReindexController();
	}

	@Bean()
	public ScheduledExecutorService scheduledExecutorService() {
		ScheduledExecutorFactoryBean b = new ScheduledExecutorFactoryBean();
		b.setPoolSize(5);
		b.afterPropertiesSet();
		return b.getObject();
	}

	@Bean(name="mySubscriptionTriggeringProvider")
	@Lazy
	public SubscriptionTriggeringProvider subscriptionTriggeringProvider() {
		return new SubscriptionTriggeringProvider();
	}

	@Bean(autowire = Autowire.BY_TYPE, name = "mySearchCoordinatorSvc")
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

	@Bean(name = TASK_EXECUTOR_NAME)
	public TaskScheduler taskScheduler() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler();
		retVal.setConcurrentExecutor(scheduledExecutorService());
		retVal.setScheduledExecutor(scheduledExecutorService());
		return retVal;
	}

	public static void configureEntityManagerFactory(LocalContainerEntityManagerFactoryBean theFactory, FhirContext theCtx) {
		theFactory.setJpaDialect(hibernateJpaDialect(theCtx.getLocalizer()));
		theFactory.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		theFactory.setPersistenceProvider(new HibernatePersistenceProvider());
	}

	private static HibernateJpaDialect hibernateJpaDialect(HapiLocalizer theLocalizer) {
		return new HapiFhirHibernateJpaDialect(theLocalizer);
	}

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
