package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.binstore.BinaryAccessProvider;
import ca.uhn.fhir.jpa.binstore.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.bulk.BulkDataExportProvider;
import ca.uhn.fhir.jpa.bulk.BulkDataExportSvcImpl;
import ca.uhn.fhir.jpa.bulk.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.index.DaoResourceLinkResolver;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.graphql.JpaStorageServices;
import ca.uhn.fhir.jpa.interceptor.JpaConsentContextServices;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.sched.AutowiringSpringBeanJobFactory;
import ca.uhn.fhir.jpa.sched.HapiSchedulerServiceImpl;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchResultCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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


@Configuration
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
@ComponentScan(basePackages = "ca.uhn.fhir.jpa", excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BaseConfig.class),
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSocketConfigurer.class),
	@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.test\\..*"),
	@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Test.*"),
	@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ca.uhn.fhir.jpa.subscription.*"),
	@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ca.uhn.fhir.jpa.searchparam.*")
})
@Import({
	SearchParamConfig.class
})
public abstract class BaseConfig {

	public static final String JPA_VALIDATION_SUPPORT_CHAIN = "myJpaValidationSupportChain";
	public static final String TASK_EXECUTOR_NAME = "hapiJpaTaskExecutor";
	public static final String GRAPHQL_PROVIDER_NAME = "myGraphQLProvider";
	private static final String HAPI_DEFAULT_SCHEDULER_GROUP = "HAPI";
	public static final String PERSISTED_JPA_BUNDLE_PROVIDER = "PersistedJpaBundleProvider";
	public static final String PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER = "PersistedJpaSearchFirstPageBundleProvider";

	@Autowired
	protected Environment myEnv;

	@Bean("myDaoRegistry")
	public DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

	@Bean
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		return new DatabaseBackedPagingProvider();
	}

	/**
	 * This method should be overridden to provide an actual completed
	 * bean, but it provides a partially completed entity manager
	 * factory with HAPI FHIR customizations
	 */
	protected LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new HapiFhirLocalContainerEntityManagerFactoryBean();
		configureEntityManagerFactory(retVal, fhirContext());
		return retVal;
	}

	public abstract FhirContext fhirContext();

	@Bean
	@Lazy
	public IGraphQLStorageServices graphqlStorageServices() {
		return new JpaStorageServices();
	}

	@Bean
	public ScheduledExecutorFactoryBean scheduledExecutorService() {
		ScheduledExecutorFactoryBean b = new ScheduledExecutorFactoryBean();
		b.setPoolSize(5);
		b.afterPropertiesSet();
		return b;
	}

	@Bean(name = "mySubscriptionTriggeringProvider")
	@Lazy
	public SubscriptionTriggeringProvider subscriptionTriggeringProvider() {
		return new SubscriptionTriggeringProvider();
	}

	@Bean(name = "myAttachmentBinaryAccessProvider")
	@Lazy
	public BinaryAccessProvider binaryAccessProvider() {
		return new BinaryAccessProvider();
	}

	@Bean(name = "myBinaryStorageInterceptor")
	@Lazy
	public BinaryStorageInterceptor binaryStorageInterceptor() {
		return new BinaryStorageInterceptor();
	}

	@Bean
	@Primary
	public IResourceLinkResolver daoResourceLinkResolver() {
		return new DaoResourceLinkResolver();
	}

	@Bean
	public ISearchCacheSvc searchCacheSvc() {
		return new DatabaseSearchCacheSvcImpl();
	}

	@Bean
	public ISearchResultCacheSvc searchResultCacheSvc() {
		return new DatabaseSearchResultCacheSvcImpl();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler();
		retVal.setConcurrentExecutor(scheduledExecutorService().getObject());
		retVal.setScheduledExecutor(scheduledExecutorService().getObject());
		return retVal;
	}

	@Bean(name = TASK_EXECUTOR_NAME)
	public AsyncTaskExecutor taskExecutor() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler();
		retVal.setConcurrentExecutor(scheduledExecutorService().getObject());
		retVal.setScheduledExecutor(scheduledExecutorService().getObject());
		return retVal;
	}

	@Bean
	public IResourceReindexingSvc resourceReindexingSvc() {
		return new ResourceReindexingSvcImpl();
	}

	@Bean
	public IStaleSearchDeletingSvc staleSearchDeletingSvc() {
		return new StaleSearchDeletingSvcImpl();
	}

	@Bean
	public HapiFhirHibernateJpaDialect hibernateJpaDialect() {
		return new HapiFhirHibernateJpaDialect(fhirContext().getLocalizer());
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public IInterceptorService jpaInterceptorService() {
		return new InterceptorService();
	}

	/**
	 * Subclasses may override
	 */
	protected boolean isSupported(String theResourceType) {
		return daoRegistry().getResourceDaoOrNull(theResourceType) != null;
	}

	@Bean
	public IConsentContextServices consentContextServices() {
		return new JpaConsentContextServices();
	}

	@Bean
	@Lazy
	public TerminologyUploaderProvider terminologyUploaderProvider() {
		TerminologyUploaderProvider retVal = new TerminologyUploaderProvider();
		return retVal;
	}

	@Bean
	public ISchedulerService schedulerService() {
		return new HapiSchedulerServiceImpl().setDefaultGroup(HAPI_DEFAULT_SCHEDULER_GROUP);
	}

	@Bean
	public AutowiringSpringBeanJobFactory schedulerJobFactory() {
		return new AutowiringSpringBeanJobFactory();
	}

	@Bean
	@Lazy
	public IBulkDataExportSvc bulkDataExportSvc() {
		return new BulkDataExportSvcImpl();
	}

	@Bean
	@Lazy
	public BulkDataExportProvider bulkDataExportProvider() {
		return new BulkDataExportProvider();
	}


	@Bean
	public PersistedJpaBundleProviderFactory persistedJpaBundleProviderFactory() {
		return new PersistedJpaBundleProviderFactory();
	}

	@Bean(name= PERSISTED_JPA_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaBundleProvider persistedJpaBundleProvider(RequestDetails theRequest, String theUuid) {
		return new PersistedJpaBundleProvider(theRequest, theUuid);
	}

	@Bean(name= PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaSearchFirstPageBundleProvider persistedJpaSearchFirstPageBundleProvider(RequestDetails theRequest, Search theSearch, SearchCoordinatorSvcImpl.SearchTask theSearchTask, ISearchBuilder theSearchBuilder) {
		return new PersistedJpaSearchFirstPageBundleProvider(theSearch, theSearchTask, theSearchBuilder, theRequest);
	}

	public static void configureEntityManagerFactory(LocalContainerEntityManagerFactoryBean theFactory, FhirContext theCtx) {
		theFactory.setJpaDialect(hibernateJpaDialect(theCtx.getLocalizer()));
		theFactory.setPackagesToScan("ca.uhn.fhir.jpa.model.entity", "ca.uhn.fhir.jpa.entity");
		theFactory.setPersistenceProvider(new HibernatePersistenceProvider());
	}

	private static HapiFhirHibernateJpaDialect hibernateJpaDialect(HapiLocalizer theLocalizer) {
		return new HapiFhirHibernateJpaDialect(theLocalizer);
	}
}
