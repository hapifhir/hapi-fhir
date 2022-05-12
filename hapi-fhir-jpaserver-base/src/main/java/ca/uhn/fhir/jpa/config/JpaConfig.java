package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.PartitionedUrlValidator;
import ca.uhn.fhir.jpa.batch.mdm.MdmClearJobSubmitterImpl;
import ca.uhn.fhir.jpa.batch.reader.BatchResourceSearcher;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkDataExportJobSchedulingHelperImpl;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkDataExportSvcImpl;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.svc.BulkDataImportSvcImpl;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourceVersionSvcDaoImpl;
import ca.uhn.fhir.jpa.dao.DaoSearchParamProvider;
import ca.uhn.fhir.jpa.dao.HistoryBuilder;
import ca.uhn.fhir.jpa.dao.HistoryBuilderFactory;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeOperation;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.ResourceExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.index.DaoResourceLinkResolver;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.dao.index.JpaIdHelperService;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkExpandSvc;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilder;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderCoords;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderDate;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderFactory;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderNumber;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderQuantity;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderReference;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderResourceId;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderString;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderTag;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderToken;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderUri;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictFinderService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.delete.DeleteExpungeJobSubmitterImpl;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.graphql.DaoRegistryGraphQLStorageServices;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.interceptor.JpaConsentContextServices;
import ca.uhn.fhir.jpa.interceptor.OverridePathBasedReferentialIntegrityForDeletesInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.packages.IHapiPackageCacheManager;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.JpaPackageCache;
import ca.uhn.fhir.jpa.packages.NpmJpaValidationSupport;
import ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.PartitionLookupSvcImpl;
import ca.uhn.fhir.jpa.partition.PartitionManagementProvider;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.provider.r4.MemberMatcherR4Helper;
import ca.uhn.fhir.jpa.reindex.ResourceReindexSvcImpl;
import ca.uhn.fhir.jpa.sched.AutowiringSpringBeanJobFactory;
import ca.uhn.fhir.jpa.sched.HapiSchedulerServiceImpl;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ForcedIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityNormalizedPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.GeneratedSql;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryExecutor;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchResultCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.elastic.IndexNamePrefixLayoutStrategy;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexer;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.sp.SearchParamPresenceSvcImpl;
import ca.uhn.fhir.jpa.term.TermConceptMappingSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.validation.JpaResourceLoader;
import ca.uhn.fhir.jpa.validation.ValidationSettings;
import ca.uhn.fhir.mdm.api.IMdmClearJobSubmitter;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.rest.server.provider.DeleteExpungeProvider;
import ca.uhn.fhir.util.ThreadPoolUtil;
import org.hl7.fhir.common.hapi.validation.support.UnknownCodeSystemWarningValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.npm.PackageClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;

/*
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

@Configuration
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.jpa.dao.data")
@Import({
	BeanPostProcessorConfig.class,
	BatchJobsConfig.class,
	SearchParamConfig.class,
	ValidationSupportConfig.class
})
public class JpaConfig {
	public static final String JPA_VALIDATION_SUPPORT_CHAIN = "myJpaValidationSupportChain";
	public static final String JPA_VALIDATION_SUPPORT = "myJpaValidationSupport";
	public static final String TASK_EXECUTOR_NAME = "hapiJpaTaskExecutor";
	public static final String GRAPHQL_PROVIDER_NAME = "myGraphQLProvider";
	public static final String PERSISTED_JPA_BUNDLE_PROVIDER = "PersistedJpaBundleProvider";
	public static final String PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH = "PersistedJpaBundleProvider_BySearch";
	public static final String PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER = "PersistedJpaSearchFirstPageBundleProvider";
	public static final String SEARCH_BUILDER = "SearchBuilder";
	public static final String HISTORY_BUILDER = "HistoryBuilder";
	private static final String HAPI_DEFAULT_SCHEDULER_GROUP = "HAPI";

	@Bean("myDaoRegistry")
	public DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

	@Lazy
	@Bean
	public CascadingDeleteInterceptor cascadingDeleteInterceptor(FhirContext theFhirContext, DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster) {
		return new CascadingDeleteInterceptor(theFhirContext, theDaoRegistry, theInterceptorBroadcaster);
	}

	@Lazy
	@Bean
	public ResponseTerminologyTranslationInterceptor responseTerminologyTranslationInterceptor(IValidationSupport theValidationSupport) {
		return new ResponseTerminologyTranslationInterceptor(theValidationSupport);
	}

	@Bean
	@Lazy
	public IGraphQLStorageServices graphqlStorageServices() {
		return new DaoRegistryGraphQLStorageServices();
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

	@Bean
	@Lazy
	public ValueSetOperationProvider valueSetOperationProvider() {
		return new ValueSetOperationProvider();
	}

	@Bean
	public TransactionProcessor transactionProcessor() {
		return new TransactionProcessor();
	}

	@Bean(name = "myAttachmentBinaryAccessProvider")
	@Lazy
	public BinaryAccessProvider binaryAccessProvider() {
		return new BinaryAccessProvider();
	}

	@Bean(name = "myBinaryStorageInterceptor")
	@Lazy
	public BinaryStorageInterceptor binaryStorageInterceptor(DaoConfig theDaoConfig) {
		BinaryStorageInterceptor interceptor = new BinaryStorageInterceptor();
		interceptor.setAllowAutoInflateBinaries(theDaoConfig.isAllowAutoInflateBinaries());
		interceptor.setAutoInflateBinariesMaximumSize(theDaoConfig.getAutoInflateBinariesMaximumBytes());
		return interceptor;
	}

	@Bean
	public MemoryCacheService memoryCacheService() {
		return new MemoryCacheService();
	}

	@Bean
	@Primary
	public IResourceLinkResolver daoResourceLinkResolver() {
		return new DaoResourceLinkResolver();
	}

	@Bean
	public IHapiPackageCacheManager packageCacheManager() {
		JpaPackageCache retVal = new JpaPackageCache();
		retVal.getPackageServers().clear();
		retVal.getPackageServers().add(PackageClient.PRIMARY_SERVER);
		retVal.getPackageServers().add(PackageClient.SECONDARY_SERVER);
		return retVal;
	}

	@Bean
	public NpmJpaValidationSupport npmJpaValidationSupport() {
		return new NpmJpaValidationSupport();
	}

	@Bean
	public ValidationSettings validationSettings() {
		return new ValidationSettings();
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
	public ITermConceptMappingSvc termConceptMappingSvc() {
		return new TermConceptMappingSvcImpl();
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

	@Bean(name = BatchConstants.JOB_LAUNCHING_TASK_EXECUTOR)
	public TaskExecutor jobLaunchingTaskExecutor() {
		return ThreadPoolUtil.newThreadPool(0, 10, "job-launcher-");
	}

	@Bean
	public IResourceReindexingSvc resourceReindexingSvc() {
		return new ResourceReindexingSvcImpl();
	}

	@Bean
	public ResourceReindexer resourceReindexer(FhirContext theFhirContext) {
		return new ResourceReindexer(theFhirContext);
	}

	@Bean
	public BatchResourceSearcher myBatchResourceSearcher() {
		return new BatchResourceSearcher();
	}

	@Bean
	public HapiFhirHibernateJpaDialect hibernateJpaDialect(FhirContext theFhirContext) {
		return new HapiFhirHibernateJpaDialect(theFhirContext.getLocalizer());
	}

	@Bean
	@Lazy
	public OverridePathBasedReferentialIntegrityForDeletesInterceptor overridePathBasedReferentialIntegrityForDeletesInterceptor() {
		return new OverridePathBasedReferentialIntegrityForDeletesInterceptor();
	}

	@Bean
	public IRequestPartitionHelperSvc requestPartitionHelperService() {
		return new RequestPartitionHelperSvc();
	}

	@Bean
	public HapiTransactionService hapiTransactionService() {
		return new HapiTransactionService();
	}

	@Bean
	public IInterceptorService jpaInterceptorService() {
		return new InterceptorService("JPA");
	}

	@Bean
	public IPackageInstallerSvc npmInstallerSvc() {
		return new PackageInstallerSvcImpl();
	}

	@Bean
	public IConsentContextServices consentContextServices() {
		return new JpaConsentContextServices();
	}

	@Bean
	@Lazy
	public DiffProvider diffProvider() {
		return new DiffProvider();
	}

	@Bean
	@Lazy
	public IPartitionLookupSvc partitionConfigSvc() {
		return new PartitionLookupSvcImpl();
	}

	@Bean
	@Lazy
	public PartitionManagementProvider partitionManagementProvider() {
		return new PartitionManagementProvider();
	}

	@Bean
	@Lazy
	public RequestTenantPartitionInterceptor requestTenantPartitionInterceptor() {
		return new RequestTenantPartitionInterceptor();
	}


	@Bean
	public MdmLinkExpandSvc mdmLinkExpandSvc() {
		return new MdmLinkExpandSvc();
	}


	@Bean
	IMdmClearJobSubmitter mdmClearJobSubmitter() {
		return new MdmClearJobSubmitterImpl();
	}

	@Bean
	@Lazy
	public TerminologyUploaderProvider terminologyUploaderProvider() {
		return new TerminologyUploaderProvider();
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
	public IBulkDataExportJobSchedulingHelper bulkDataExportJobSchedulingHelper() {
		return new BulkDataExportJobSchedulingHelperImpl();
	}

	@Bean
	@Lazy
	public BulkDataExportProvider bulkDataExportProvider() {
		return new BulkDataExportProvider();
	}

	@Bean
	@Lazy
	public IDeleteExpungeJobSubmitter deleteExpungeJobSubmitter() {
		return new DeleteExpungeJobSubmitterImpl();
	}

	@Bean
	@Lazy
	public PartitionedUrlValidator partitionedUrlValidator() {
		return new PartitionedUrlValidator();
	}

	@Bean
	@Lazy
	public DeleteExpungeProvider deleteExpungeProvider(FhirContext theFhirContext, IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		return new DeleteExpungeProvider(theFhirContext, theDeleteExpungeJobSubmitter);
	}

	@Bean
	@Lazy
	public IBulkDataImportSvc bulkDataImportSvc() {
		return new BulkDataImportSvcImpl();
	}

	@Bean
	public PersistedJpaBundleProviderFactory persistedJpaBundleProviderFactory() {
		return new PersistedJpaBundleProviderFactory();
	}

	@Bean
	public SearchBuilderFactory searchBuilderFactory() {
		return new SearchBuilderFactory();
	}

	@Bean
	public SqlObjectFactory sqlBuilderFactory() {
		return new SqlObjectFactory();
	}

	@Bean
	public HibernatePropertiesProvider HibernatePropertiesProvider() {
		return new HibernatePropertiesProvider();
	}

	@Bean
	public HistoryBuilderFactory historyBuilderFactory() {
		return new HistoryBuilderFactory();
	}

	@Bean
	public IResourceVersionSvc resourceVersionSvc() {
		return new ResourceVersionSvcDaoImpl();
	}

	@Bean
	public IResourceReindexSvc resourceReindexSvc() {
		return new ResourceReindexSvcImpl();
	}

	/* **************************************************************** *
	 * Prototype Beans Below                                            *
	 * **************************************************************** */

	@Bean(name = PERSISTED_JPA_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaBundleProvider newPersistedJpaBundleProvider(RequestDetails theRequest, String theUuid) {
		return new PersistedJpaBundleProvider(theRequest, theUuid);
	}

	@Bean(name = PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH)
	@Scope("prototype")
	public PersistedJpaBundleProvider newPersistedJpaBundleProvider(RequestDetails theRequest, Search theSearch) {
		return new PersistedJpaBundleProvider(theRequest, theSearch);
	}

	@Bean(name = PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaSearchFirstPageBundleProvider newPersistedJpaSearchFirstPageBundleProvider(RequestDetails theRequest, Search theSearch, SearchCoordinatorSvcImpl.SearchTask theSearchTask, ISearchBuilder theSearchBuilder) {
		return new PersistedJpaSearchFirstPageBundleProvider(theSearch, theSearchTask, theSearchBuilder, theRequest);
	}

	@Bean(name = RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER)
	@Scope("prototype")
	public RepositoryValidatingRuleBuilder repositoryValidatingRuleBuilder() {
		return new RepositoryValidatingRuleBuilder();
	}

	@Bean
	@Scope("prototype")
	public ComboUniqueSearchParameterPredicateBuilder newComboUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		return new ComboUniqueSearchParameterPredicateBuilder(theSearchSqlBuilder);
	}

	@Bean
	@Scope("prototype")
	public ComboNonUniqueSearchParameterPredicateBuilder newComboNonUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		return new ComboNonUniqueSearchParameterPredicateBuilder(theSearchSqlBuilder);
	}

	@Bean
	@Scope("prototype")
	public CoordsPredicateBuilder newCoordsPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new CoordsPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public DatePredicateBuilder newDatePredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new DatePredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public ForcedIdPredicateBuilder newForcedIdPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new ForcedIdPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public NumberPredicateBuilder newNumberPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new NumberPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public QuantityPredicateBuilder newQuantityPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new QuantityPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public QuantityNormalizedPredicateBuilder newQuantityNormalizedPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new QuantityNormalizedPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public ResourceLinkPredicateBuilder newResourceLinkPredicateBuilder(QueryStack theQueryStack, SearchQueryBuilder theSearchBuilder, boolean theReversed) {
		return new ResourceLinkPredicateBuilder(theQueryStack, theSearchBuilder, theReversed);
	}

	@Bean
	@Scope("prototype")
	public ResourceTablePredicateBuilder newResourceTablePredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new ResourceTablePredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public TagPredicateBuilder newTagPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new TagPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public ResourceIdPredicateBuilder newResourceIdPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new ResourceIdPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public SearchParamPresentPredicateBuilder newSearchParamPresentPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new SearchParamPresentPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public StringPredicateBuilder newStringPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new StringPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public TokenPredicateBuilder newTokenPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new TokenPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public SourcePredicateBuilder newSourcePredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new SourcePredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public UriPredicateBuilder newUriPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new UriPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderCoords newPredicateBuilderCoords(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderCoords(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderDate newPredicateBuilderDate(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderDate(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderNumber newPredicateBuilderNumber(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderNumber(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderQuantity newPredicateBuilderQuantity(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderQuantity(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderReference newPredicateBuilderReference(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return new PredicateBuilderReference(theSearchBuilder, thePredicateBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderResourceId newPredicateBuilderResourceId(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderResourceId(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderString newPredicateBuilderString(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderString(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderTag newPredicateBuilderTag(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderTag(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderToken newPredicateBuilderToken(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return new PredicateBuilderToken(theSearchBuilder, thePredicateBuilder);
	}

	@Bean
	@Scope("prototype")
	public PredicateBuilderUri newPredicateBuilderUri(LegacySearchBuilder theSearchBuilder) {
		return new PredicateBuilderUri(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public SearchQueryExecutor newSearchQueryExecutor(GeneratedSql theGeneratedSql, Integer theMaxResultsToFetch) {
		return new SearchQueryExecutor(theGeneratedSql, theMaxResultsToFetch);
	}

	@Bean(name = SEARCH_BUILDER)
	@Scope("prototype")
	public ISearchBuilder newSearchBuilder(IDao theDao, String theResourceName, Class<? extends IBaseResource> theResourceType, DaoConfig theDaoConfig) {
		if (theDaoConfig.isUseLegacySearchBuilder()) {
			return new LegacySearchBuilder(theDao, theResourceName, theResourceType);
		}
		return new SearchBuilder(theDao, theResourceName, theResourceType);
	}

	@Bean(name = HISTORY_BUILDER)
	@Scope("prototype")
	public HistoryBuilder newPersistedJpaSearchFirstPageBundleProvider(@Nullable String theResourceType, @Nullable Long theResourceId, @Nullable Date theRangeStartInclusive, @Nullable Date theRangeEndInclusive) {
		return new HistoryBuilder(theResourceType, theResourceId, theRangeStartInclusive, theRangeEndInclusive);
	}

	@Bean
	@Primary
	public ISearchParamProvider searchParamProvider() {
		return new DaoSearchParamProvider();
	}

	@Bean
	public IJpaIdHelperService jpaIdHelperService() {
		return new JpaIdHelperService();
	}

	@Bean
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl();
	}

	@Bean
	public DeleteConflictService deleteConflictService() {
		return new DeleteConflictService();
	}

	@Bean
	public DeleteConflictFinderService deleteConflictFinderService() {
		return new DeleteConflictFinderService();
	}

	@Bean
	public ExpungeService expungeService() {
		return new ExpungeService();
	}

	@Bean
	@Scope("prototype")
	public ExpungeOperation expungeOperation(String theResourceName, ResourcePersistentId theResourceId, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		return new ExpungeOperation(theResourceName, theResourceId, theExpungeOptions, theRequestDetails);
	}

	@Bean
	public IExpungeEverythingService expungeEverythingService() {
		return new ExpungeEverythingService();
	}

	@Bean
	public IResourceExpungeService resourceExpungeService() {
		return new ResourceExpungeService();
	}

	@Bean
	public ISearchParamPresenceSvc searchParamPresenceService() {
		return new SearchParamPresenceSvcImpl();
	}

	@Bean
	public SearchParamWithInlineReferencesExtractor searchParamWithInlineReferencesExtractor() {
		return new SearchParamWithInlineReferencesExtractor();
	}

	@Bean
	public MatchResourceUrlService matchResourceUrlService() {
		return new MatchResourceUrlService();
	}

	@Bean
	public DaoSearchParamSynchronizer daoSearchParamSynchronizer() {
		return new DaoSearchParamSynchronizer();
	}

	@Bean
	public ResourceTableFKProvider resourceTableFKProvider() {
		return new ResourceTableFKProvider();
	}

	@Bean
	public ICacheWarmingSvc cacheWarmingSvc() {
		return new CacheWarmingSvcImpl();
	}

	@Bean
	public PredicateBuilderFactory predicateBuilderFactory(ApplicationContext theApplicationContext) {
		return new PredicateBuilderFactory(theApplicationContext);
	}

	@Bean
	public IndexNamePrefixLayoutStrategy indexLayoutStrategy() {
		return new IndexNamePrefixLayoutStrategy();
	}

	@Bean
	public JpaResourceLoader jpaResourceLoader() {
		return new JpaResourceLoader();
	}

	@Bean
	public UnknownCodeSystemWarningValidationSupport unknownCodeSystemWarningValidationSupport(FhirContext theFhirContext) {
		return new UnknownCodeSystemWarningValidationSupport(theFhirContext);
	}

	@Lazy
	@Bean
	public MemberMatcherR4Helper memberMatcherR4Helper(FhirContext theFhirContext) {
		return new MemberMatcherR4Helper(theFhirContext);
	}

	@Lazy
	@Bean
	public NicknameInterceptor nicknameInterceptor() throws IOException {
		return new NicknameInterceptor();
	}
}
