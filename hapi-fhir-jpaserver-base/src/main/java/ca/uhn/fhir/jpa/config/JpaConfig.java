/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeJobSubmitterImpl;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchUrlJobMaintenanceSvc;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkDataExportJobSchedulingHelperImpl;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportHelperService;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.svc.BulkDataImportSvcImpl;
import ca.uhn.fhir.jpa.cache.IResourceIdentifierCacheSvc;
import ca.uhn.fhir.jpa.cache.IResourceTypeCacheSvc;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.cache.ResourceIdentifierCacheSvcImpl;
import ca.uhn.fhir.jpa.cache.ResourceTypeCacheSvcImpl;
import ca.uhn.fhir.jpa.cache.ResourceVersionSvcDaoImpl;
import ca.uhn.fhir.jpa.dao.CacheTagDefinitionDao;
import ca.uhn.fhir.jpa.dao.DaoSearchParamProvider;
import ca.uhn.fhir.jpa.dao.HistoryBuilder;
import ca.uhn.fhir.jpa.dao.HistoryBuilderFactory;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.JpaBulkDataExportHistoryHelper;
import ca.uhn.fhir.jpa.dao.JpaDaoResourceLinkResolver;
import ca.uhn.fhir.jpa.dao.JpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.ResourceHistoryCalculator;
import ca.uhn.fhir.jpa.dao.ResourceMetadataExtractorSvcImpl;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierPatientUniqueEntityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIdentifierSystemEntityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTypeDao;
import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeOperation;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.JpaResourceExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.delete.DeleteConflictFinderService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.delete.ThreadSafeResourceDeleterSvc;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.graphql.DaoRegistryGraphQLStorageServices;
import ca.uhn.fhir.jpa.interceptor.AuthResourceResolver;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.interceptor.JpaConsentContextServices;
import ca.uhn.fhir.jpa.interceptor.OverridePathBasedReferentialIntegrityForDeletesInterceptor;
import ca.uhn.fhir.jpa.interceptor.PatientCompartmentEnforcingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.packages.IHapiPackageCacheManager;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.JpaPackageCache;
import ca.uhn.fhir.jpa.packages.NpmJpaValidationSupport;
import ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.PartitionLookupSvcImpl;
import ca.uhn.fhir.jpa.partition.PartitionManagementProvider;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.ConceptMapAddAndRemoveMappingProvider;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.IReplaceReferencesSvc;
import ca.uhn.fhir.jpa.provider.InstanceReindexProvider;
import ca.uhn.fhir.jpa.provider.ProcessMessageProvider;
import ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProviderDstu2;
import ca.uhn.fhir.jpa.sched.AutowiringSpringBeanJobFactory;
import ca.uhn.fhir.jpa.sched.HapiSchedulerServiceImpl;
import ca.uhn.fhir.jpa.search.ISynchronousSearchSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.PersistedJpaIdSearchBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.search.SearchUrlJobMaintenanceSvcImpl;
import ca.uhn.fhir.jpa.search.SynchronousSearchSvcImpl;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityNormalizedPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceHistoryPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceHistoryProvenancePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkForHasParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.GeneratedSql;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryExecutor;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchResultCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.elastic.IndexNamePrefixLayoutStrategy;
import ca.uhn.fhir.jpa.search.reindex.IInstanceReindexService;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.reindex.InstanceReindexServiceImpl;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexer;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.extractor.IResourceLinkResolver;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.sp.SearchParamIdentityCacheSvcImpl;
import ca.uhn.fhir.jpa.sp.SearchParamPresenceSvcImpl;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessagePersistenceSvcImpl;
import ca.uhn.fhir.jpa.term.TermCodeSystemStorageSvcImpl;
import ca.uhn.fhir.jpa.term.TermConceptMappingSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcImpl;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.term.ValueSetConceptAccumulator;
import ca.uhn.fhir.jpa.term.ValueSetConceptAccumulatorFactory;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermReindexingSvc;
import ca.uhn.fhir.jpa.term.config.TermCodeSystemConfig;
import ca.uhn.fhir.jpa.util.JpaHapiTransactionService;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.PartitionedIdModeVerificationSvc;
import ca.uhn.fhir.jpa.util.PersistenceContextProvider;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.jpa.validation.ResourceLoaderImpl;
import ca.uhn.fhir.jpa.validation.ValidationSettings;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.replacereferences.UndoReplaceReferencesSvc;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import ca.uhn.fhir.util.IMetaTagSorter;
import ca.uhn.fhir.util.MetaTagSorterAlphabetical;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.common.hapi.validation.support.UnknownCodeSystemWarningValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.WorkerContextValidationSupportAdapter;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.List;

@Configuration
// repositoryFactoryBeanClass: EnversRevisionRepositoryFactoryBean is needed primarily for unit testing
@EnableJpaRepositories(
		basePackages = "ca.uhn.fhir.jpa.dao.data",
		repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@Import({
	BeanPostProcessorConfig.class,
	TermCodeSystemConfig.class,
	SearchParamConfig.class,
	ValidationSupportConfig.class,
	Batch2SupportConfig.class,
	JpaBulkExportConfig.class,
	SearchConfig.class,
	PackageLoaderConfig.class,
	EnversAuditConfig.class,
	MdmJpaConfig.class
})
public class JpaConfig {
	public static final String JPA_VALIDATION_SUPPORT_CHAIN = "myJpaValidationSupportChain";
	public static final String JPA_VALIDATION_SUPPORT = "myJpaValidationSupport";
	public static final String TASK_EXECUTOR_NAME = "hapiJpaTaskExecutor";
	public static final String GRAPHQL_PROVIDER_NAME = "myGraphQLProvider";
	public static final String PERSISTED_JPA_BUNDLE_PROVIDER = "PersistedJpaBundleProvider";
	public static final String PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH = "PersistedJpaBundleProvider_BySearch";
	public static final String PERSISTED_JPA_ID_SEARCH_BUNDLE_PROVIDER = "PersistedJpaIdSearchBundleProvider";
	public static final String PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER =
			"PersistedJpaSearchFirstPageBundleProvider";
	public static final String HISTORY_BUILDER = "HistoryBuilder";
	public static final String HISTORY_BUILDER_WITH_IDS = "HistoryBuilderWithIds";
	public static final String DEFAULT_PROFILE_VALIDATION_SUPPORT = "myDefaultProfileValidationSupport";
	private static final String HAPI_DEFAULT_SCHEDULER_GROUP = "HAPI";

	@Autowired
	public JpaStorageSettings myStorageSettings;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private FhirContext myFhirContext;

	@Bean
	public ValidationSupportChain.CacheConfiguration validationSupportChainCacheConfiguration() {
		return ValidationSupportChain.CacheConfiguration.defaultValues();
	}

	/**
	 * Note, there is a circular dependency between {@link WorkerContextValidationSupportAdapter}
	 * and {@link JpaValidationSupportChain}. The WorkerContextValidationSupportAdapter wraps
	 * an instance of {@link IValidationSupport} (which is what JpaValidationSupportChain is)
	 * but we also need to pass in the WorkerContextValidationSupportAdapter instance to the
	 * snapshot generator which is created within the {@literal @PostConstruct} method within
	 * JpaValidationSupportChain.
	 * <p>
	 * In order to allow the circular dependency to be created (since Spring doesn't like
	 * these), JpaValidationSupportChain calls {@link WorkerContextValidationSupportAdapter#setValidationSupport(IValidationSupport)}
	 * to pass itself in.
	 * </p>
	 * <p>
	 * This is obviously not ideal, but is the best we can do since the corelib
	 * tools all use {@link org.hl7.fhir.r5.context.IWorkerContext} interface as their
	 * input. See {@link WorkerContextValidationSupportAdapter} for more info.
	 * </p>
	 */
	@Bean
	public WorkerContextValidationSupportAdapter workerContextValidationSupportAdapter() {
		return new WorkerContextValidationSupportAdapter();
	}

	@Bean(name = JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	@Primary
	public IValidationSupport jpaValidationSupportChain() {
		return new JpaValidationSupportChain(
				myFhirContext, validationSupportChainCacheConfiguration(), workerContextValidationSupportAdapter());
	}

	@Bean("myDaoRegistry")
	public DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

	@Lazy
	@Bean
	public CascadingDeleteInterceptor cascadingDeleteInterceptor(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ThreadSafeResourceDeleterSvc threadSafeResourceDeleterSvc) {
		return new CascadingDeleteInterceptor(
				theFhirContext, theDaoRegistry, theInterceptorBroadcaster, threadSafeResourceDeleterSvc);
	}

	@Bean
	public ExternallyStoredResourceServiceRegistry ExternallyStoredResourceServiceRegistry() {
		return new ExternallyStoredResourceServiceRegistry();
	}

	@Lazy
	@Bean
	public ThreadSafeResourceDeleterSvc safeDeleter(
			DaoRegistry theDaoRegistry,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			HapiTransactionService hapiTransactionService) {
		return new ThreadSafeResourceDeleterSvc(theDaoRegistry, theInterceptorBroadcaster, hapiTransactionService);
	}

	@Lazy
	@Bean
	public ResponseTerminologyTranslationInterceptor responseTerminologyTranslationInterceptor(
			IValidationSupport theValidationSupport,
			ResponseTerminologyTranslationSvc theResponseTerminologyTranslationSvc) {
		return new ResponseTerminologyTranslationInterceptor(
				theValidationSupport, theResponseTerminologyTranslationSvc);
	}

	@Bean
	public ResponseTerminologyTranslationSvc responseTerminologyTranslationSvc(
			IValidationSupport theValidationSupport) {
		return new ResponseTerminologyTranslationSvc(theValidationSupport);
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
	public PatientCompartmentEnforcingInterceptor patientCompartmentEnforcingInterceptor(
			FhirContext theFhirContext, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new PatientCompartmentEnforcingInterceptor(theFhirContext, theRequestPartitionHelperSvc);
	}

	@Bean
	@Lazy
	public ValueSetOperationProvider valueSetOperationProvider(FhirContext theFhirContext) {
		if (theFhirContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU2)) {
			return new ValueSetOperationProviderDstu2();
		}
		return new ValueSetOperationProvider();
	}

	@Bean
	public IJpaStorageResourceParser jpaStorageResourceParser() {
		return new JpaStorageResourceParser();
	}

	@Bean
	public IResourceMetadataExtractorSvc resourceMetadataExtractorSvc(
			JpaStorageSettings theStorageSettings,
			IResourceHistoryTagDao theResourceHistoryTagDao,
			IResourceTagDao theResourceTagDao,
			IResourceHistoryProvenanceDao theResourceHistoryProvenanceDao) {
		return new ResourceMetadataExtractorSvcImpl(
				theStorageSettings, theResourceHistoryTagDao, theResourceTagDao, theResourceHistoryProvenanceDao);
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
	public BinaryStorageInterceptor<? extends IPrimitiveDatatype<byte[]>> binaryStorageInterceptor(
			JpaStorageSettings theStorageSettings, FhirContext theCtx) {
		BinaryStorageInterceptor<? extends IPrimitiveDatatype<byte[]>> interceptor =
				new BinaryStorageInterceptor<>(theCtx);
		interceptor.setAllowAutoInflateBinaries(theStorageSettings.isAllowAutoInflateBinaries());
		interceptor.setAutoInflateBinariesMaximumSize(theStorageSettings.getAutoInflateBinariesMaximumBytes());
		return interceptor;
	}

	@Bean
	public MemoryCacheService memoryCacheService(JpaStorageSettings theStorageSettings) {
		return new MemoryCacheService(theStorageSettings);
	}

	@Bean
	@Primary
	public IResourceLinkResolver daoResourceLinkResolver() {
		return new JpaDaoResourceLinkResolver();
	}

	@Bean(name = PackageUtils.LOADER_WITH_CACHE)
	public IHapiPackageCacheManager packageCacheManager() {
		return new JpaPackageCache();
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
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler(
				scheduledExecutorService().getObject(),
				scheduledExecutorService().getObject());
		return retVal;
	}

	@Bean(name = TASK_EXECUTOR_NAME)
	public AsyncTaskExecutor taskExecutor() {
		ConcurrentTaskScheduler retVal = new ConcurrentTaskScheduler(
				scheduledExecutorService().getObject(),
				scheduledExecutorService().getObject());
		return retVal;
	}

	@Bean
	public IResourceReindexingSvc resourceReindexingSvc() {
		return new ResourceReindexingSvcImpl();
	}

	@Bean
	@Lazy
	public IInstanceReindexService instanceReindexService() {
		return new InstanceReindexServiceImpl();
	}

	@Bean
	@Lazy
	public InstanceReindexProvider instanceReindexProvider(IInstanceReindexService theInstanceReindexService) {
		return new InstanceReindexProvider(theInstanceReindexService);
	}

	@Bean
	public ResourceReindexer resourceReindexer(FhirContext theFhirContext) {
		return new ResourceReindexer(theFhirContext);
	}

	@Bean
	public HapiFhirHibernateJpaDialect hibernateJpaDialect(FhirContext theFhirContext) {
		return new HapiFhirHibernateJpaDialect(theFhirContext.getLocalizer());
	}

	@Bean
	@Lazy
	public OverridePathBasedReferentialIntegrityForDeletesInterceptor
			overridePathBasedReferentialIntegrityForDeletesInterceptor() {
		return new OverridePathBasedReferentialIntegrityForDeletesInterceptor();
	}

	@Bean
	public IRequestPartitionHelperSvc requestPartitionHelperService() {
		return new RequestPartitionHelperSvc();
	}

	@Bean
	public HapiTransactionService hapiTransactionService() {
		return new JpaHapiTransactionService();
	}

	@Bean
	public IInterceptorService jpaInterceptorService() {
		return new InterceptorService();
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
	public IAuthResourceResolver authResourceResolver(
			DaoRegistry theDaoRegistry, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new AuthResourceResolver(theDaoRegistry, theRequestPartitionHelperSvc);
	}

	@Bean
	@Lazy
	public DiffProvider diffProvider() {
		return new DiffProvider();
	}

	@Bean
	public IResourceIdentifierCacheSvc resourceIdentifierCacheSvc(
			IHapiTransactionService theTransactionService,
			MemoryCacheService theMemoryCache,
			IResourceIdentifierSystemEntityDao theResourceIdentifierSystemEntityDao,
			IResourceIdentifierPatientUniqueEntityDao theResourceIdentifierPatientUniqueEntityDao,
			EntityManager theEntityManager) {
		return new ResourceIdentifierCacheSvcImpl(
				theTransactionService,
				theMemoryCache,
				theResourceIdentifierSystemEntityDao,
				theResourceIdentifierPatientUniqueEntityDao,
				theEntityManager);
	}

	@Bean
	@Lazy
	public ConceptMapAddAndRemoveMappingProvider conceptMapAddAndRemoveMappingProvider(
			IFhirResourceDaoConceptMap<?> theConceptMapDao) {
		return new ConceptMapAddAndRemoveMappingProvider(theConceptMapDao);
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
	@Lazy
	public TerminologyUploaderProvider terminologyUploaderProvider() {
		return new TerminologyUploaderProvider();
	}

	@Bean
	@Lazy
	public ProcessMessageProvider processMessageProvider() {
		return new ProcessMessageProvider();
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
	public IBulkDataExportJobSchedulingHelper bulkDataExportJobSchedulingHelper(
			DaoRegistry theDaoRegistry,
			PlatformTransactionManager theTxManager,
			JpaStorageSettings theStorageSettings,
			BulkExportHelperService theBulkExportHelperSvc,
			IJobPersistence theJpaJobPersistence) {
		return new BulkDataExportJobSchedulingHelperImpl(
				theDaoRegistry, theTxManager, theStorageSettings, theBulkExportHelperSvc, theJpaJobPersistence, null);
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
	public IBulkDataImportSvc bulkDataImportSvc() {
		return new BulkDataImportSvcImpl();
	}

	@Bean
	public PersistedJpaBundleProviderFactory persistedJpaBundleProviderFactory() {
		return new PersistedJpaBundleProviderFactory();
	}

	@Bean
	public SearchBuilderFactory<JpaPid> searchBuilderFactory() {
		return new SearchBuilderFactory<>();
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
	public ISearchParamIdentityCacheSvc searchParamIdentityCacheSvc(
			@Autowired IResourceIndexedSearchParamIdentityDao theResourceIndexedSearchParamIdentityDao,
			@Autowired PlatformTransactionManager theTxManager,
			@Autowired MemoryCacheService theMemoryCacheService) {
		return new SearchParamIdentityCacheSvcImpl(
				myStorageSettings, theResourceIndexedSearchParamIdentityDao, theTxManager, theMemoryCacheService);
	}

	@Bean
	public IResourceTypeCacheSvc resourceTypeCacheSvc(
			@Autowired IHapiTransactionService theHapiTransactionService,
			@Autowired IResourceTypeDao theResourceTypeDao,
			@Autowired MemoryCacheService theMemoryCacheService) {
		return new ResourceTypeCacheSvcImpl(theHapiTransactionService, theResourceTypeDao, theMemoryCacheService);
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
	public PersistedJpaBundleProvider newPersistedJpaBundleProviderBySearch(
			RequestDetails theRequest, Search theSearch) {
		return new PersistedJpaBundleProvider(theRequest, theSearch);
	}

	@Bean(name = PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaSearchFirstPageBundleProvider newPersistedJpaSearchFirstPageBundleProvider(
			RequestDetails theRequest,
			SearchTask theSearchTask,
			ISearchBuilder<?> theSearchBuilder,
			RequestPartitionId theRequestPartitionId) {
		return new PersistedJpaSearchFirstPageBundleProvider(
				theSearchTask, theSearchBuilder, theRequest, theRequestPartitionId);
	}

	@Bean(name = PERSISTED_JPA_ID_SEARCH_BUNDLE_PROVIDER)
	@Scope("prototype")
	public PersistedJpaIdSearchBundleProvider newPersistedJpaIdSearchBundleProvider(
			String theResourceType,
			List<IResourcePersistentId<?>> theResourceIds,
			RequestPartitionId thePartitionId,
			@Nullable Date theRangeStartInclusive,
			@Nonnull Date theRangeEndInclusive) {
		return new PersistedJpaIdSearchBundleProvider(
				theResourceType, theResourceIds, thePartitionId, theRangeStartInclusive, theRangeEndInclusive);
	}

	@Bean(name = RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER)
	@Scope("prototype")
	public RepositoryValidatingRuleBuilder repositoryValidatingRuleBuilder(IValidationSupport theValidationSupport) {
		return new RepositoryValidatingRuleBuilder(theValidationSupport);
	}

	@Bean
	@Scope("prototype")
	public ComboUniqueSearchParameterPredicateBuilder newComboUniqueSearchParameterPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
		return new ComboUniqueSearchParameterPredicateBuilder(theSearchSqlBuilder);
	}

	@Bean
	@Scope("prototype")
	public ComboNonUniqueSearchParameterPredicateBuilder newComboNonUniqueSearchParameterPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
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
	public QuantityNormalizedPredicateBuilder newQuantityNormalizedPredicateBuilder(
			SearchQueryBuilder theSearchBuilder) {
		return new QuantityNormalizedPredicateBuilder(theSearchBuilder);
	}

	@Bean("newResourceLinkPredicateBuilder")
	@Scope("prototype")
	public ResourceLinkPredicateBuilder newResourceLinkPredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theSearchBuilder) {
		return new ResourceLinkPredicateBuilder(theQueryStack, theSearchBuilder);
	}

	@Bean("newHasLinkPredicateBuilder")
	@Scope("prototype")
	public ResourceLinkForHasParameterPredicateBuilder newHasLinkPredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theSearchBuilder) {
		return new ResourceLinkForHasParameterPredicateBuilder(theQueryStack, theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public ResourceTablePredicateBuilder newResourceTablePredicateBuilder(
			SearchQueryBuilder theSearchBuilder, SearchIncludeDeletedEnum theSearchIncludeDeleted) {
		return new ResourceTablePredicateBuilder(theSearchBuilder, theSearchIncludeDeleted);
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
	public SearchParamPresentPredicateBuilder newSearchParamPresentPredicateBuilder(
			SearchQueryBuilder theSearchBuilder) {
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
	public ResourceHistoryPredicateBuilder newResourceHistoryPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new ResourceHistoryPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public ResourceHistoryProvenancePredicateBuilder newResourceHistoryProvenancePredicateBuilder(
			SearchQueryBuilder theSearchBuilder) {
		return new ResourceHistoryProvenancePredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public UriPredicateBuilder newUriPredicateBuilder(SearchQueryBuilder theSearchBuilder) {
		return new UriPredicateBuilder(theSearchBuilder);
	}

	@Bean
	@Scope("prototype")
	public SearchQueryExecutor newSearchQueryExecutor(GeneratedSql theGeneratedSql, Integer theMaxResultsToFetch) {
		return new SearchQueryExecutor(theGeneratedSql, theMaxResultsToFetch);
	}

	@Bean(name = HISTORY_BUILDER)
	@Scope("prototype")
	public HistoryBuilder newHistoryBuilder(
			@Nullable String theResourceType,
			@Nullable JpaPid theResourceId,
			@Nullable Date theRangeStartInclusive,
			@Nullable Date theRangeEndInclusive) {
		return new HistoryBuilder(theResourceType, theResourceId, theRangeStartInclusive, theRangeEndInclusive);
	}

	@Bean(name = HISTORY_BUILDER_WITH_IDS)
	@Scope("prototype")
	public HistoryBuilder newHistoryBuilderWithIds(
			@Nonnull String theResourceType,
			@Nonnull List<IResourcePersistentId<?>> theResourceIds,
			@Nullable Date theRangeStartInclusive,
			@Nonnull Date theRangeEndInclusive) {
		return new HistoryBuilder(theResourceType, theResourceIds, theRangeStartInclusive, theRangeEndInclusive);
	}

	@Bean
	@Primary
	public ISearchParamProvider searchParamProvider() {
		return new DaoSearchParamProvider();
	}

	@Bean
	public IIdHelperService idHelperService() {
		return new IdHelperService();
	}

	@Bean
	public SearchStrategyFactory searchStrategyFactory(@Autowired(required = false) IFulltextSearchSvc theFulltextSvc) {
		return new SearchStrategyFactory(myStorageSettings, theFulltextSvc);
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
	public ExpungeOperation expungeOperation(
			String theResourceName,
			IResourcePersistentId theResourceId,
			ExpungeOptions theExpungeOptions,
			RequestDetails theRequestDetails) {
		return new ExpungeOperation(theResourceName, theResourceId, theExpungeOptions, theRequestDetails);
	}

	@Bean
	public IExpungeEverythingService expungeEverythingService() {
		return new ExpungeEverythingService();
	}

	@Bean
	public IResourceExpungeService resourceExpungeService() {
		return new JpaResourceExpungeService();
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
	public IndexNamePrefixLayoutStrategy indexLayoutStrategy() {
		return new IndexNamePrefixLayoutStrategy();
	}

	@Bean
	public ResourceLoaderImpl jpaResourceLoader() {
		return new ResourceLoaderImpl();
	}

	@Bean
	public UnknownCodeSystemWarningValidationSupport unknownCodeSystemWarningValidationSupport(
			FhirContext theFhirContext) {
		return new UnknownCodeSystemWarningValidationSupport(theFhirContext);
	}

	@Bean
	public ISynchronousSearchSvc synchronousSearchSvc() {
		return new SynchronousSearchSvcImpl();
	}

	@Bean
	public VersionCanonicalizer versionCanonicalizer(FhirContext theFhirContext) {
		return new VersionCanonicalizer(theFhirContext);
	}

	@Bean
	public SearchParameterDaoValidator searchParameterDaoValidator(
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings,
			ISearchParamRegistry theSearchParamRegistry) {
		return new SearchParameterDaoValidator(theFhirContext, theStorageSettings, theSearchParamRegistry);
	}

	@Bean
	public ITermReadSvc terminologyService() {
		return new TermReadSvcImpl();
	}

	@Bean
	public ValueSetConceptAccumulatorFactory valueSetConceptAccumulatorFactory() {
		return new ValueSetConceptAccumulatorFactory();
	}

	@Bean
	@Scope("prototype")
	public ValueSetConceptAccumulator valueSetConceptAccumulator(TermValueSet theTermValueSet) {
		return valueSetConceptAccumulatorFactory().create(theTermValueSet);
	}

	@Bean
	public ITermCodeSystemStorageSvc termCodeSystemStorageSvc() {
		return new TermCodeSystemStorageSvcImpl();
	}

	@Bean
	public ITermReindexingSvc termReindexingSvc() {
		return new TermReindexingSvcImpl();
	}

	@Bean
	@Scope("prototype")
	public PersistenceContextProvider persistenceContextProvider() {
		return new PersistenceContextProvider();
	}

	@Bean
	public ResourceSearchUrlSvc resourceSearchUrlSvc(
			PersistenceContextProvider thePersistenceContextProvider,
			IResourceSearchUrlDao theResourceSearchUrlDao,
			MatchUrlService theMatchUrlService,
			FhirContext theFhirContext,
			PartitionSettings thePartitionSettings) {
		return new ResourceSearchUrlSvc(
				thePersistenceContextProvider.getEntityManager(),
				theResourceSearchUrlDao,
				theMatchUrlService,
				theFhirContext,
				thePartitionSettings);
	}

	@Bean
	public ISearchUrlJobMaintenanceSvc searchUrlJobMaintenanceSvc(ResourceSearchUrlSvc theResourceSearchUrlSvc) {
		return new SearchUrlJobMaintenanceSvcImpl(theResourceSearchUrlSvc);
	}

	@Bean
	public IResourceModifiedMessagePersistenceSvc subscriptionMessagePersistence(
			FhirContext theFhirContext,
			IResourceModifiedDao theIResourceModifiedDao,
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService) {
		return new ResourceModifiedMessagePersistenceSvcImpl(
				theFhirContext, theIResourceModifiedDao, theDaoRegistry, theHapiTransactionService);
	}

	@Bean
	public IMetaTagSorter metaTagSorter() {
		return new MetaTagSorterAlphabetical();
	}

	@Bean
	public ResourceHistoryCalculator resourceHistoryCalculator(
			FhirContext theFhirContext, HibernatePropertiesProvider theHibernatePropertiesProvider) {
		return new ResourceHistoryCalculator(theFhirContext, theHibernatePropertiesProvider.isOracleDialect());
	}

	@Bean
	public CacheTagDefinitionDao tagDefinitionDao(
			ITagDefinitionDao tagDefinitionDao, MemoryCacheService memoryCacheService) {
		return new CacheTagDefinitionDao(tagDefinitionDao, memoryCacheService);
	}

	@Bean
	public Batch2TaskHelper batch2TaskHelper() {
		return new Batch2TaskHelper();
	}

	@Bean
	public IReplaceReferencesSvc replaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService,
			IResourceLinkDao theResourceLinkDao,
			IJobCoordinator theJobCoordinator,
			ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundle,
			Batch2TaskHelper theBatch2TaskHelper,
			JpaStorageSettings theStorageSettings,
			ReplaceReferencesProvenanceSvc theProvenanceSvc) {
		return new ReplaceReferencesSvcImpl(
				theDaoRegistry,
				theHapiTransactionService,
				theResourceLinkDao,
				theJobCoordinator,
				theReplaceReferencesPatchBundle,
				theBatch2TaskHelper,
				theStorageSettings,
				theProvenanceSvc);
	}

	@Primary
	@Bean
	public ReplaceReferencesProvenanceSvc replaceReferencesProvenanceSvc(DaoRegistry theDaoRegistry) {
		return new ReplaceReferencesProvenanceSvc(theDaoRegistry);
	}

	@Bean
	public ReplaceReferencesPatchBundleSvc replaceReferencesPatchBundleSvc(DaoRegistry theDaoRegistry) {
		return new ReplaceReferencesPatchBundleSvc(theDaoRegistry);
	}

	@Bean
	public PreviousResourceVersionRestorer resourceVersionRestorer(
			DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService) {
		return new PreviousResourceVersionRestorer(theDaoRegistry, theHapiTransactionService);
	}

	@Bean
	public UndoReplaceReferencesSvc getUndoReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			ReplaceReferencesProvenanceSvc theProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer) {
		return new UndoReplaceReferencesSvc(theDaoRegistry, theProvenanceSvc, theResourceVersionRestorer);
	}

	@Bean
	public PartitionedIdModeVerificationSvc partitionedIdModeVerificationSvc(
			PartitionSettings thePartitionSettings,
			HibernatePropertiesProvider theHibernatePropertiesProvider,
			PlatformTransactionManager theTxManager) {
		return new PartitionedIdModeVerificationSvc(thePartitionSettings, theHibernatePropertiesProvider, theTxManager);
	}

	@Bean
	public IBulkDataExportHistoryHelper bulkDataExportHistoryHelper() {
		return new JpaBulkDataExportHistoryHelper();
	}
}
