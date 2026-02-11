/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.cache.IResourceTypeCacheSvc;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.config.util.ResourceTypeUtil;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboTokensNonUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamCoordsDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamDateDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamNumberDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTypeDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import ca.uhn.fhir.test.utilities.server.SpringContextGrabbingTestExecutionListener;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.Constants.HEADER_CACHE_CONTROL;
import static ca.uhn.fhir.util.TestUtil.doRandomizeLocaleAndTimezone;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	UnregisterScheduledProcessor.SCHEDULING_DISABLED_EQUALS_TRUE
})
@Import(BaseJpaTest.TestSearchParamConfig.class)
@TestExecutionListeners(value = SpringContextGrabbingTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class BaseJpaTest extends BaseTest {

	public static final String WEBSOCKET_CONTEXT = "/ws";
	protected static final String CM_URL = "http://example.com/my_concept_map";
	protected static final String CS_URL = "http://example.com/my_code_system";
	protected static final String CS_URL_2 = "http://example.com/my_code_system2";
	protected static final String CS_URL_3 = "http://example.com/my_code_system3";
	protected static final String CS_URL_4 = "http://example.com/my_code_system4";
	protected static final String VS_URL = "http://example.com/my_value_set";
	protected static final String VS_URL_2 = "http://example.com/my_value_set2";
	protected static final long VALIDATION_CACHE_TIMEOUT_MILLIS = 1000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaTest.class);

	static {
		HapiSystemProperties.setValidationResourceCacheTimeoutMillis(VALIDATION_CACHE_TIMEOUT_MILLIS);
		HapiSystemProperties.enableTestMode();
		HapiSystemProperties.enableUnitTestMode();
		TestUtil.setShouldRandomizeTimezones(false);
	}

	@RegisterExtension
	public LoggingExtension myLoggingExtension = new LoggingExtension();
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	protected ServletRequestDetails mySrd;
	protected InterceptorService mySrdInterceptorService;
	@Autowired
	protected IMdmLinkDao<JpaPid, MdmLink> myMdmLinkDao;
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected JpaStorageSettings myStorageSettings;
	@Autowired
	protected SubscriptionSettings mySubscriptionSettings;
	@Autowired
	protected DatabaseBackedPagingProvider myDatabaseBackedPagingProvider;
	@Autowired
	protected INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;
	@Autowired
	protected ISearchResultCacheSvc mySearchResultCacheSvc;
	@Autowired
	protected PartitionSettings myPartitionSettings;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	protected ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	protected IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	protected IResourceIndexedSearchParamNumberDao myResourceIndexedSearchParamNumberDao;
	@Autowired
	protected IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamDateDao myResourceIndexedSearchParamDateDao;
	@Autowired
	protected IResourceIndexedSearchParamCoordsDao myResourceIndexedSearchParamCoordsDao;
	@Autowired
	protected IResourceIndexedComboTokensNonUniqueDao myResourceIndexedComboTokensNonUniqueDao;
	@Autowired
	protected IResourceIndexedComboStringUniqueDao myResourceIndexedComboStringUniqueDao;
	@Autowired
	protected IResourceIndexedSearchParamIdentityDao myResourceIndexedSearchParamIdentityDao;
	@Autowired(required = false)
	protected IFulltextSearchSvc myFulltestSearchSvc;
	@Autowired(required = false)
	protected Batch2JobHelper myBatch2JobHelper;
	@Autowired
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	protected ITermValueSetConceptDao myTermValueSetConceptDao;
	@Autowired
	protected ITermValueSetDao myTermValueSetDao;
	@Autowired
	protected ITermConceptDesignationDao myTermConceptDesignationDao;
	@Autowired
	protected ITermConceptPropertyDao myTermConceptPropertyDao;
	@Autowired
	private MemoryCacheService myMemoryCacheService;
	@Autowired
	protected ISchedulerService mySchedulerService;
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT)
	@Autowired
	private IValidationSupport myJpaPersistedValidationSupport;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private FhirInstanceValidator myFhirInstanceValidator;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private IResourceSearchUrlDao myResourceSearchUrlDao;
	@Autowired
	private IResourceTagDao myResourceTagDao;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected ITermDeferredStorageSvc myTermDeferredStorageSvc;
	@Autowired
	protected IResourceSearchUrlDao mySearchUrlDao;
	private final List<Object> myRegisteredInterceptors = new ArrayList<>(1);
	@Autowired
	private IResourceHistoryTagDao myResourceHistoryTagDao;
	@Autowired
	protected IResourceTypeDao myResourceTypeDao;
	@Autowired
	protected IResourceTypeCacheSvc myResourceTypeCacheSvc;

	@Autowired
	protected ApplicationContext myApplicationContext;

	@TestConfiguration
	public static class TestSearchParamConfig {

		@Autowired(required = false)
		private SearchParamRegistryImpl mySearchParamRegistry;

		@Autowired(required = false)
		protected JpaStorageSettings myStorageSettings;

		@PostConstruct
		public void preConfigure() {
			if (myStorageSettings != null) {
				myStorageSettings.setWriteToSearchParamIdentityTable(false);
			}

			if (mySearchParamRegistry != null) {
				mySearchParamRegistry.setPopulateSearchParamIdentities(false);
			}
		}
	}

	@BeforeEach
	public void beforeInitSearchParams() {
		myStorageSettings.setWriteToSearchParamIdentityTable(false);
	}

	@SuppressWarnings("BusyWait")
	public static void waitForSize(int theTarget, List<?> theList) {
		StopWatch sw = new StopWatch();
		while (theList.size() != theTarget && sw.getMillis() <= 16000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= 16000 || theList.size() > theTarget) {
			String describeResults = theList
				.stream()
				.map(t -> {
					if (t == null) {
						return "null";
					}
					if (t instanceof IBaseResource) {
						return ((IBaseResource) t).getIdElement().getValue();
					}
					return t.toString();
				})
				.collect(Collectors.joining(", "));
			fail("Size " + theList.size() + " is != target " + theTarget + " - Got: " + describeResults);
		}
	}

	@BeforeAll
	public static void beforeClassRandomizeLocale() {
		doRandomizeLocaleAndTimezone();
	}

	@SuppressWarnings("BusyWait")
	public static void purgeDatabase(JpaStorageSettings theStorageSettings, IFhirSystemDao<?, ?> theSystemDao, IResourceReindexingSvc theResourceReindexingSvc, ISearchCoordinatorSvc theSearchCoordinatorSvc, ISearchParamRegistry theSearchParamRegistry, IBulkDataExportJobSchedulingHelper theBulkDataJobActivator) {
		theSearchCoordinatorSvc.cancelAllActiveSearches();
		theResourceReindexingSvc.cancelAndPurgeAllJobs();
		theBulkDataJobActivator.cancelAndPurgeAllJobs();

		boolean expungeEnabled = theStorageSettings.isExpungeEnabled();
		boolean multiDeleteEnabled = theStorageSettings.isAllowMultipleDelete();
		theStorageSettings.setExpungeEnabled(true);
		theStorageSettings.setAllowMultipleDelete(true);

		for (int count = 0; ; count++) {
			try {
				ourLog.info("Calling Expunge count {}", count);
				theSystemDao.expunge(new ExpungeOptions().setExpungeEverything(true), new SystemRequestDetails());
				break;
			} catch (Exception e) {
				if (count >= 3) {
					ourLog.error("Failed during expunge", e);
					fail(e.toString());
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e2) {
						fail(e2.toString());
					}
				}
			}
		}
		theStorageSettings.setExpungeEnabled(expungeEnabled);
		theStorageSettings.setAllowMultipleDelete(multiDeleteEnabled);

		theSearchParamRegistry.forceRefresh();
	}

	protected static Set<String> toCodes(Set<TermConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<>();
		for (TermConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	protected static Set<String> toCodes(List<FhirVersionIndependentConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<>();
		for (FhirVersionIndependentConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	public static void waitForSize(int theTarget, Callable<Number> theCallable, Callable<String> theFailureMessage) throws Exception {
		waitForSize(theTarget, 10000, theCallable, theFailureMessage);
	}

	@SuppressWarnings("BusyWait")
	public static void waitForSize(int theTarget, int theTimeoutMillis, Callable<Number> theCallable, Callable<String> theFailureMessage) throws Exception {
		await()
			.alias("Waiting for size " + theTarget + ". Current size is " + theCallable.call().intValue() + ": " + theFailureMessage.call())
			.atMost(Duration.of(theTimeoutMillis, ChronoUnit.MILLIS))
			.until(() -> theCallable.call().intValue() == theTarget);
	}

	protected <T extends IBaseResource> T loadResourceFromClasspath(Class<T> type, String resourceName) throws IOException {
		return ClasspathUtil.loadResource(myFhirContext, type, resourceName);
	}

	@AfterEach
	public void afterPerformCleanup() {
		BaseHapiFhirDao.setDisableIncrementOnUpdateForUnitTest(false);
		if (myCaptureQueriesListener != null) {
			myCaptureQueriesListener.clear();
		}
		if (myMemoryCacheService != null) {
			myMemoryCacheService.invalidateAllCaches();
		}
		if (myFhirInstanceValidator != null) {
			myFhirInstanceValidator.invalidateCaches();
		}
		if (myValidationSupport != null) {
			myValidationSupport.invalidateCaches();
		}

		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		BeanUtils.copyProperties(defaultConfig, myStorageSettings);

		ParserOptions defaultParserOptions = new ParserOptions();
		BeanUtils.copyProperties(defaultParserOptions, myFhirContext.getParserOptions());
		// version stripping parser options configured through this function, not in the class itself, so apply them, this function supports multiple fhir versions
		FhirContextR4Config.configureDefaultParserOptions(myFhirContext);

		PartitionSettings defaultPartConfig = new PartitionSettings();
		BeanUtils.copyProperties(defaultPartConfig, myPartitionSettings);
	}

	@AfterEach
	public void afterValidateNoTransaction() {
		PlatformTransactionManager txManager = getTxManager();
		if (txManager instanceof JpaTransactionManager) {
			JpaTransactionManager hibernateTxManager = (JpaTransactionManager) txManager;
			SessionFactory sessionFactory = (SessionFactory) hibernateTxManager.getEntityManagerFactory();
			AtomicBoolean isReadOnly = new AtomicBoolean();
			Session currentSession;
			try {
				assert sessionFactory != null;
				currentSession = sessionFactory.getCurrentSession();
			} catch (HibernateException e) {
				currentSession = null;
			}
			if (currentSession != null) {
				currentSession.doWork(connection -> isReadOnly.set(connection.isReadOnly()));

				assertFalse(isReadOnly.get());
			}
		}
	}

	@BeforeEach
	public void beforeInitPartitions() {
		if (myPartitionConfigSvc != null) {
			myPartitionConfigSvc.start();
		}
	}

	@BeforeEach
	public void beforeInitMocks() throws Exception {
		mySrdInterceptorService = new InterceptorService();

		MockitoAnnotations.initMocks(this);

		when(mySrd.getInterceptorBroadcaster()).thenReturn(mySrdInterceptorService);
		when(mySrd.getUserData()).thenReturn(new HashMap<>());
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(new ArrayList<>());
		when(mySrd.getHeaders(eq(HEADER_CACHE_CONTROL))).thenReturn(new ArrayList<>());
		// TODO enforce strict mocking everywhere
		lenient().when(mySrd.getServer().getDefaultPageSize()).thenReturn(null);
		lenient().when(mySrd.getServer().getMaximumPageSize()).thenReturn(null);
	}

	protected CountDownLatch registerLatchHookInterceptor(int theCount, Pointcut theLatchPointcut) {
		CountDownLatch deliveryLatch = new CountDownLatch(theCount);
		IAnonymousInterceptor interceptor = (thePointcut, t) -> deliveryLatch.countDown();
		myRegisteredInterceptors.add(interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(theLatchPointcut, Integer.MAX_VALUE, interceptor);
		return deliveryLatch;
	}

	public void registerInterceptor(Object theInterceptor) {
		myRegisteredInterceptors.add(theInterceptor);
		myInterceptorRegistry.registerInterceptor(theInterceptor);
	}

	protected void unregisterInterceptor(Object theInterceptor) {
		myInterceptorRegistry.unregisterInterceptor(theInterceptor);
		myRegisteredInterceptors.remove(theInterceptor);
	}

	protected void purgeHibernateSearch(EntityManager theEntityManager) {
		runInTransaction(() -> {
			if (myFulltestSearchSvc != null && !myFulltestSearchSvc.isDisabled()) {
				SearchSession searchSession = Search.session(theEntityManager);
				searchSession.workspace(ResourceTable.class).purge();
				searchSession.indexingPlan().execute();
			}
		});
	}

	protected abstract FhirContext getFhirContext();

	protected abstract PlatformTransactionManager getTxManager();

	public void logAllCodeSystemsAndVersionsCodeSystemsAndVersions() {
		runInTransaction(() -> {
			ourLog.info("CodeSystems:\n * " + myTermCodeSystemDao.findAll()
				.stream()
				.map(t -> t.toString())
				.collect(joining("\n * ")));
			ourLog.info("CodeSystemVersions:\n * " + myTermCodeSystemVersionDao.findAll()
				.stream()
				.map(t -> t.toString())
				.collect(Collectors.joining("\n * ")));
		});
	}

	public void logAllPackageVersions() {
		runInTransaction(() -> {
			ourLog.info("Package Versions:\n * {}", myPackageVersionDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllResourcesOfType(String type) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(type);
		IBundleProvider allResources = dao.search(SearchParameterMap.newSynchronous().setLoadSynchronousUpTo(100), newSrd());
		List<IBaseResource> resources = allResources.getAllResources();
		IParser parser = myFhirContext.newJsonParser();
		for (int i = 0; i < resources.size(); i++) {
			IBaseResource next = resources.get(i);
			ourLog.info("{} #{}:\n{}", type, i, parser.setPrettyPrint(true).encodeResourceToString(next));
		}
	}

	protected int countAllMdmLinks() {
		return runInTransaction(()-> myMdmLinkDao.findAll().size());
	}

	protected int logAllMdmLinks() {
		return runInTransaction(()->{
			List<MdmLink> links = myMdmLinkDao.findAll();
			if (links.isEmpty()) {
				ourLog.info("MDM Links: NONE");
			} else {
				ourLog.info("MDM Links:\n * {}", links.stream().map(t -> t.toString()).collect(joining("\n * ")));
			}
			return links.size();
		});
	}

	public void logAllResourceLinks() {
		runInTransaction(() -> {
			ourLog.info("Resource Links:\n * {}", myResourceLinkDao.findAll().stream().map(ResourceLink::toString).collect(Collectors.joining("\n * ")));
		});
	}

	public int logAllResources() {
		return runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			ourLog.info("Resources:\n * {}", resources.stream().map(ResourceTable::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	public int logAllResourceSearchUrls() {
		return runInTransaction(() -> {
			List<ResourceSearchUrlEntity> resources = myResourceSearchUrlDao.findAll();
			ourLog.info("Search URLs:\n * {}", resources.stream().map(ResourceSearchUrlEntity::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	protected int logAllConceptDesignations() {
		return runInTransaction(() -> {
			List<TermConceptDesignation> resources = myTermConceptDesignationDao.findAll();
			ourLog.info("Concept Designations:\n * {}", resources.stream().map(TermConceptDesignation::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	protected int logAllConceptProperties() {
		return runInTransaction(() -> {
			List<TermConceptProperty> resources = myTermConceptPropertyDao.findAll();
			ourLog.info("Concept Properties:\n * {}", resources.stream().map(TermConceptProperty::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	public int logAllConcepts() {
		return runInTransaction(() -> {
			List<TermConcept> resources = myTermConceptDao.findAll();
			ourLog.info("Concepts:\n * {}", resources.stream().map(TermConcept::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	protected int logAllConceptParentChildLinks() {
		return runInTransaction(() -> {
			List<TermConceptParentChildLink> resources = myTermConceptParentChildLinkDao.findAll();
			ourLog.info("Concept Parent/Child Links:\n * {}", resources.stream().map(TermConceptParentChildLink::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	public int logAllValueSetConcepts() {
		return runInTransaction(() -> {
			List<TermValueSetConcept> resources = myTermValueSetConceptDao.findAll();
			ourLog.info("ValueSet Concepts:\n * {}", resources.stream().map(TermValueSetConcept::toString).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	protected int logAllValueSets() {
		return runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findAll();
			ourLog.info("ValueSets:\n * {}", valueSets.stream().map(TermValueSet::toString).collect(Collectors.joining("\n * ")));
			return valueSets.size();
		});
	}

	protected void logAllDateIndexes() {
		runInTransaction(() -> {
			ourLog.info("Date indexes:\n * {}", myResourceIndexedSearchParamDateDao.findAll().stream().map(ResourceIndexedSearchParamDate::toString).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllNonUniqueIndexes() {
		runInTransaction(() -> {
			ourLog.info("Non unique indexes:\n * {}", myResourceIndexedComboTokensNonUniqueDao.findAll().stream().map(ResourceIndexedComboTokenNonUnique::toString).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllUniqueIndexes() {
		runInTransaction(() -> {
			ourLog.info("Unique indexes:\n * {}", myResourceIndexedComboStringUniqueDao.findAll().stream().map(ResourceIndexedComboStringUnique::toString).collect(Collectors.joining("\n * ")));
		});
	}

    protected void logAllSearchUrls() {
        runInTransaction(() -> {
            ourLog.info("Token indexes:\n * {}", mySearchUrlDao.findAll().stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));
        });
    }
    
	protected void logAllTokenIndexes(String... theParamNames) {
		String messageSuffix = theParamNames.length > 0 ? " containing " + Arrays.asList(theParamNames) : "";
		runInTransaction(() -> {
			String message = getAllTokenIndexes(theParamNames)
				.stream()
				.map(ResourceIndexedSearchParamToken::toString)
				.collect(Collectors.joining("\n * "));
			ourLog.info("Token indexes{}:\n * {}", messageSuffix, message);
		});
	}

	@Nonnull
	protected List<ResourceIndexedSearchParamToken> getAllTokenIndexes(String... theParamNames) {
		List<Long> searchParamHashIdentities = getSearchParamHashIdentities(theParamNames);
		return runInTransaction(() -> myResourceIndexedSearchParamTokenDao
			.findAll()
			.stream()
			.filter(t -> theParamNames.length == 0 || searchParamHashIdentities.contains(t.getHashIdentity()))
			.toList());
	}

	protected void logAllCoordsIndexes() {
		runInTransaction(() -> {
			ourLog.info("Coords indexes:\n * {}", myResourceIndexedSearchParamCoordsDao.findAll().stream().map(ResourceIndexedSearchParamCoords::toString).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllNumberIndexes() {
		runInTransaction(() -> {
			ourLog.info("Number indexes:\n * {}", myResourceIndexedSearchParamNumberDao.findAll().stream().map(ResourceIndexedSearchParamNumber::toString).collect(Collectors.joining("\n * ")));
		});
	}

	public void logAllUriIndexes() {
		runInTransaction(() -> {
			ourLog.info("URI indexes:\n * {}", myResourceIndexedSearchParamUriDao.findAll().stream().map(ResourceIndexedSearchParamUri::toString).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllStringIndexes(String... theParamNames) {
		String messageSuffix = theParamNames.length > 0 ? " containing " + Arrays.asList(theParamNames) : "";
		runInTransaction(() -> {
			String message = getAllStringIndexes(theParamNames)
				.stream()
				.map(ResourceIndexedSearchParamString::toString)
				.collect(Collectors.joining("\n * "));
			ourLog.info("String indexes{}:\n * {}", messageSuffix, message);
		});
	}

	@Nonnull
	protected List<ResourceIndexedSearchParamString> getAllStringIndexes(String... theParamNames) {
		List<Long> searchParamHashIdentities = getSearchParamHashIdentities(theParamNames);
		return myResourceIndexedSearchParamStringDao
			.findAll()
			.stream()
			.filter(t -> theParamNames.length == 0 || searchParamHashIdentities.contains(t.getHashIdentity()))
			.toList();
	}

	protected List<Long> getSearchParamHashIdentities(String[] theParamNames) {
		List<Long> searchParamHashIdentities = new ArrayList<>(theParamNames.length);
		if (theParamNames.length != 0) {
			searchParamHashIdentities.addAll(myResourceIndexedSearchParamIdentityDao
				.findAll()
				.stream()
				.filter(t -> Arrays.asList(theParamNames).contains(t.getParamName()))
				.map(IndexedSearchParamIdentity::getHashIdentity)
				.toList());
		}
		return searchParamHashIdentities;
	}

	protected void logAllResourceTags() {
		runInTransaction(() -> {
			ourLog.info("Resource tags:\n * {}", myResourceTagDao.findAll().stream().map(ResourceTag::toString).collect(Collectors.joining("\n * ")));
		});
	}

	protected void logAllResourceHistoryTags() {
		runInTransaction(() -> {
			ourLog.info("Resource history tags:\n * {}", myResourceHistoryTagDao.findAll().stream().map(ResourceHistoryTag::toString).collect(Collectors.joining("\n * ")));
		});
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(getTxManager());
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		retVal.afterPropertiesSet();
		return retVal;
	}

	public void runInTransaction(Runnable theRunnable) {
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				theRunnable.run();
			}
		});
	}

	public <T> T runInTransaction(Callable<T> theRunnable) {
		return newTxTemplate().execute(t -> {
			try {
				return theRunnable.call();
			} catch (Exception theE) {
				throw new InternalErrorException(theE);
			}
		});
	}

	/**
	 * Sleep until time change on the clocks
	 */
	public void sleepUntilTimeChange() {
		StopWatch sw = new StopWatch();
		await().until(() -> sw.getMillis() > 0);
	}

	protected org.hl7.fhir.dstu3.model.Bundle toBundle(IBundleProvider theSearch) {
		org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.sizeOrThrowNpe())) {
			bundle.addEntry().setResource((Resource) next);
		}
		return bundle;
	}

	protected org.hl7.fhir.r4.model.Bundle toBundleR4(IBundleProvider theSearch) {
		org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.sizeOrThrowNpe())) {
			bundle.addEntry().setResource((org.hl7.fhir.r4.model.Resource) next);
		}
		return bundle;
	}

	@SuppressWarnings({"rawtypes"})
	protected List toList(IBundleProvider theSearch) {
		return theSearch.getResources(0, theSearch.sizeOrThrowNpe());
	}

	protected List<String> toUnqualifiedIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getFhirContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedIdValues(IBundleProvider theFound) {
		List<String> retVal = new ArrayList<>();
		int size = theFound.sizeOrThrowNpe();
		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getFhirContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		int fromIndex = 0;
		Integer toIndex = theFound.containsAllResources() ? (Integer) theFound.getResourceListComplete().size() : theFound.size();
		return toUnqualifiedVersionlessIdValues(theFound, fromIndex, toIndex, true);
	}

	/**
	 * Keys will be unqualified versionless IDs (Patient/ABC) and values will be the resources
	 * themselves.
	 */
	protected Map<String, IBaseResource> toResourceIdValueMap(IBundleProvider theFound) {
		Map<String, IBaseResource> retVal = new HashMap<>();
		List<IBaseResource> resources = theFound.getAllResources();
		for (IBaseResource next : resources) {
			retVal.put(next.getIdElement().toUnqualifiedVersionless().getValue(), next);
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound, int theFromIndex, Integer theToIndex, boolean theFirstCall) {
		if (theToIndex == null) {
			theToIndex = 99999;
		}

		List<String> retVal = new ArrayList<>();

		IBundleProvider bundleProvider;
		if (theFirstCall) {
			bundleProvider = theFound;
		} else {
			bundleProvider = myDatabaseBackedPagingProvider.retrieveResultList(null, theFound.getUuid());
		}

		List<IBaseResource> resources = bundleProvider.getResources(theFromIndex, theToIndex);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(IBundleProvider theProvider) {

		List<IIdType> retVal = new ArrayList<>();

		Integer size = theProvider.size();

		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theProvider.getResources(0, Integer.MAX_VALUE / 4);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(List<? extends IBaseResource> theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (IBaseResource next : theFound) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(List<? extends IBaseResource> theFound) {
		List<String> retVal = new ArrayList<>();
		for (IBaseResource next : theFound) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(org.hl7.fhir.dstu3.model.Bundle theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(org.hl7.fhir.r4.model.Bundle theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected String[] toValues(IIdType... theValues) {
		ArrayList<String> retVal = new ArrayList<>();
		for (IIdType next : theValues) {
			retVal.add(next.getValue());
		}
		return retVal.toArray(new String[0]);
	}

	@SuppressWarnings("BusyWait")
	protected void waitForActivatedSubscriptionCount(int theSize) throws Exception {
		for (int i = 0; ; i++) {
			if (i == 10) {
				fail("Failed to init subscriptions");
			}
			try {
				mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
				break;
			} catch (ResourceVersionConflictException e) {
				Thread.sleep(250);
			}
		}

		TestUtil.waitForSize(theSize, () -> mySubscriptionRegistry.size());
		Thread.sleep(500);
	}

	protected int logAllResourceVersions() {
		return runInTransaction(() -> {
			List<ResourceHistoryTable> resources = myResourceHistoryTableDao.findAll();
			ourLog.info("Resources Versions:\n * {}", resources.stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			return resources.size();
		});
	}

	protected TermValueSetConcept assertTermValueSetContainsConceptAndIsInDeclaredOrder(TermValueSet theValueSet, String theSystem, String theCode, String theDisplay, Integer theDesignationCount) {
		List<TermValueSetConcept> contains = theValueSet.getConcepts();

		Stream<TermValueSetConcept> stream = contains.stream();
		if (theSystem != null) {
			stream = stream.filter(concept -> theSystem.equalsIgnoreCase(concept.getSystem()));
		}
		if (theCode != null) {
			stream = stream.filter(concept -> theCode.equalsIgnoreCase(concept.getCode()));
		}
		if (theDisplay != null) {
			stream = stream.filter(concept -> theDisplay.equalsIgnoreCase(concept.getDisplay()));
		}
		if (theDesignationCount != null) {
			stream = stream.filter(concept -> concept.getDesignations().size() == theDesignationCount);
		}

		Optional<TermValueSetConcept> first = stream.findFirst();
		if (!first.isPresent()) {
			String failureMessage = String.format("Expanded ValueSet %s did not contain concept [%s|%s|%s] with [%d] designations", theValueSet.getId(), theSystem, theCode, theDisplay, theDesignationCount);
			fail(failureMessage);
			return null;
		} else {
			TermValueSetConcept termValueSetConcept = first.get();
			assertEquals(termValueSetConcept.getOrder(), theValueSet.getConcepts().indexOf(termValueSetConcept));
			return termValueSetConcept;
		}
	}

	protected TermValueSetConceptDesignation assertTermConceptContainsDesignation(TermValueSetConcept theConcept, String theLanguage, String theUseSystem, String theUseCode, String theUseDisplay, String theDesignationValue) {
		Stream<TermValueSetConceptDesignation> stream = theConcept.getDesignations().stream();
		if (theLanguage != null) {
			stream = stream.filter(designation -> theLanguage.equalsIgnoreCase(designation.getLanguage()));
		}
		if (theUseSystem != null) {
			stream = stream.filter(designation -> theUseSystem.equalsIgnoreCase(designation.getUseSystem()));
		}
		if (theUseCode != null) {
			stream = stream.filter(designation -> theUseCode.equalsIgnoreCase(designation.getUseCode()));
		}
		if (theUseDisplay != null) {
			stream = stream.filter(designation -> theUseDisplay.equalsIgnoreCase(designation.getUseDisplay()));
		}
		if (theDesignationValue != null) {
			stream = stream.filter(designation -> theDesignationValue.equalsIgnoreCase(designation.getValue()));
		}

		Optional<TermValueSetConceptDesignation> first = stream.findFirst();
		if (!first.isPresent()) {
			String failureMessage = String.format("Concept %s did not contain designation [%s|%s|%s|%s|%s] ", theConcept, theLanguage, theUseSystem, theUseCode, theUseDisplay, theDesignationValue);
			fail(failureMessage);
			return null;
		} else {
			return first.get();
		}

	}

	@BeforeEach
	protected void before() throws Exception {
		// nothing - just here so other test can override it. We used to do stuff here,
		// and having this stub is easier than removing all of the super.before() calls
		// in the existing overridden versions
	}

	@Order(Integer.MAX_VALUE)
	@AfterEach
	protected void afterResetInterceptors() {
		myRegisteredInterceptors.forEach(t -> myInterceptorRegistry.unregisterInterceptor(t));
		myRegisteredInterceptors.clear();
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
	}

	/**
	 * Asserts that the resource with {@literal theId} is deleted
	 */
	protected void assertGone(String theId) {
		assertGone(myFhirContext.getVersion().newIdType(theId));
	}

	/**
	 * Asserts that the resource with {@literal theId} is deleted
	 */
	protected void assertGone(IIdType theId) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		IBaseResource result = dao.read(theId, newSrd(), true);
		assertTrue(result.isDeleted());
	}

	/**
	 * Asserts that the resource with {@literal theId} exists and is not deleted.
	 * Note that {@link #assertExists(IIdType)} and {@link #assertNotGone(IIdType)}
	 * are synonyms but both exist for better readability in different kinds
	 * of tests.
	 */
	protected void assertExists(String theId) {
		IIdType id = myFhirContext.getVersion().newIdType(theId);
		assertExists(id);
	}

	/**
	 * Asserts that the resource with {@literal theId} exists and is not deleted.
	 * Note that {@link #assertExists(IIdType)} and {@link #assertNotGone(IIdType)}
	 * are synonyms but both exist for better readability in different kinds
	 * of tests.
	 */
	protected void assertExists(IIdType theId) {
		assertNotGone(theId);
	}

	/**
	 * Asserts that the resource with {@literal theId} exists and is not deleted.
	 * Note that {@link #assertExists(IIdType)} and {@link #assertNotGone(IIdType)}
	 * are synonyms but both exist for better readability in different kinds
	 * of tests.
	 */
	protected void assertNotGone(String theId) {
		assertNotGone(myFhirContext.getVersion().newIdType(theId));
	}

	/**
	 * Asserts that the resource with {@literal theId} exists and is not deleted.
	 * Note that {@link #assertExists(IIdType)} and {@link #assertNotGone(IIdType)}
	 * are synonyms but both exist for better readability in different kinds
	 * of tests.
	 */
	protected void assertNotGone(IIdType theId) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		assertNotNull(dao.read(theId, mySrd));
	}

	/**
	 * Asserts that the resource with {@literal theId} exists and is not deleted.
	 * Note that {@link #assertExists(IIdType)} and {@link #assertNotGone(IIdType)}
	 * are synonyms but both exist for better readability in different kinds
	 * of tests.
	 */
	protected void assertNotGone(IIdType theId, RequestPartitionId theRequestPartitionId) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		assertNotNull(dao.read(theId, newSrd().setRequestPartitionId(theRequestPartitionId)));
	}

	/**
	 * Asserts that the resource with {@literal theId} does not exist (i.e. not that
	 * it exists but that it was deleted, but rather that the ID doesn't exist at all).
	 * This can be used to test that a resource was expunged.
	 */
	protected void assertDoesntExist(String theId) {
		IIdType id = myFhirContext.getVersion().newIdType(theId);
		assertDoesntExist(id);
	}

	/**
	 * Asserts that the resource with {@literal theId} does not exist (i.e. not that
	 * it exists but that it was deleted, but rather that the ID doesn't exist at all).
	 * This can be used to test that a resource was expunged.
	 */
	protected void assertDoesntExist(IIdType theId) {
		assertDoesntExist(theId, mySrd);
	}

	/**
	 * Asserts that the resource with {@literal theId} does not exist (i.e. not that
	 * it exists but that it was deleted, but rather that the ID doesn't exist at all).
	 * This can be used to test that a resource was expunged.
	 */
	protected void assertDoesntExist(IIdType theId, RequestPartitionId theRequestPartitionId) {
		assertDoesntExist(theId, newSrd().setRequestPartitionId(theRequestPartitionId));
	}

	/**
	 * Asserts that the resource with {@literal theId} does not exist (i.e. not that
	 * it exists but that it was deleted, but rather that the ID doesn't exist at all).
	 * This can be used to test that a resource was expunged.
	 */
	protected void assertDoesntExist(IIdType theId, RequestDetails requestDetails) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		try {
			dao.read(theId, requestDetails);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).containsAnyOf(
				Msg.code(1996) + "Resource " + theId.toUnqualifiedVersionless().getValue() + " is not known",
				Msg.code(2001) + "Resource " + theId.toUnqualifiedVersionless().getValue() + " is not known"
			);
		}
	}

	/**
	 * Asserts that the version of the resource with {@literal theId} does not exist
	 * (i.e. that the resource exists, but that the given version does not)
	 */
	protected void assertVersionDoesntExist(String theId) {
		IIdType id = myFhirContext.getVersion().newIdType(theId);
		assertVersionDoesntExist(id);
	}

	/**
	 * Asserts that the version of the resource with {@literal theId} does not exist
	 * (i.e. that the resource exists, but that the given version does not)
	 */
	@SuppressWarnings("rawtypes")
	protected void assertVersionDoesntExist(IIdType theId) {
		assertTrue(theId.hasResourceType());
		assertTrue(theId.hasIdPart());
		assertTrue(theId.hasVersionIdPart());
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		assertThatThrownBy(()->dao.read(theId, mySrd))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage(Msg.code(979) + "Version \"3\" is not valid for resource Patient/A");
	}

	/**
	 * Initializes the resource type cache from the configuration instead of the database.
	 * This is useful for some tests that validate the SELECT query counts. It prevents an extra SELECT query
	 * being added to the count
	 */
	protected void initResourceTypeCacheFromConfig() {
		myMemoryCacheService.invalidateCaches(MemoryCacheService.CacheEnum.RES_TYPE_TO_RES_TYPE_ID);

		List<String> resTypes = ResourceTypeUtil.generateResourceTypes();
		for (int i = 0; i < resTypes.size(); i++) {
			myResourceTypeCacheSvc.addToCache(resTypes.get(i), (short) (i+1));
		}
	}

	public static Date fromLocalDate(LocalDate theLocalDate) {
		return Date.from(theLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	@Nonnull
	public static SystemRequestDetails newSrd() {
		return new SystemRequestDetails();
	}

}
