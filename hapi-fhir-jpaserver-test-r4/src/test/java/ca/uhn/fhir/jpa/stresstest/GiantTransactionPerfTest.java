package ca.uhn.fhir.jpa.stresstest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.cache.ResourcePersistentIdMap;
import ca.uhn.fhir.jpa.cache.ResourceVersionMap;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.JpaResourceDao;
import ca.uhn.fhir.jpa.dao.ResourceHistoryCalculator;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.jpa.sp.SearchParamPresenceSvcImpl;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.IMetaTagSorter;
import ca.uhn.fhir.util.MetaTagSorterAlphabetical;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import com.google.common.collect.Lists;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiantTransactionPerfTest {

	private static final Logger ourLog = LoggerFactory.getLogger(GiantTransactionPerfTest.class);
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private FhirSystemDaoR4 mySystemDao;
	private IInterceptorBroadcaster myInterceptorSvc;
	private TransactionProcessor myTransactionProcessor;
	private PlatformTransactionManager myTransactionManager;
	private MockEntityManager myEntityManager;
	private JpaStorageSettings myStorageSettings;
	private HapiTransactionService myHapiTransactionService;
	private DaoRegistry myDaoRegistry;
	private JpaResourceDao<ExplanationOfBenefit> myEobDao;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private ApplicationContext myAppCtx;
	@Mock
	private IInstanceValidatorModule myInstanceValidatorSvc;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private SearchParamWithInlineReferencesExtractor mySearchParamWithInlineReferencesExtractor;
	private PartitionSettings myPartitionSettings;
	private SearchParamExtractorService mySearchParamExtractorSvc;
	private SearchParamExtractorR4 mySearchParamExtractor;
	private SearchParamRegistryImpl mySearchParamRegistry;
	private ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@Mock
	private ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;
	private ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	private MockResourceVersionSvc myResourceVersionSvc;
	private MockResourceHistoryTableDao myResourceHistoryTableDao;
	private SearchParamPresenceSvcImpl mySearchParamPresenceSvc;
	private DaoSearchParamSynchronizer myDaoSearchParamSynchronizer;
	@Mock
	private IIdHelperService myIdHelperService;
	@Mock
	private IJpaStorageResourceParser myJpaStorageResourceParser;
	private final ResourceHistoryCalculator myResourceHistoryCalculator = new ResourceHistoryCalculator(FhirContext.forR4Cached(), false);
	private IMetaTagSorter myMetaTagSorter;

	@AfterEach
	public void afterEach() {
		myStorageSettings.setEnforceReferenceTargetTypes(new JpaStorageSettings().isEnforceReferenceTargetTypes());
		myStorageSettings.setAllowInlineMatchUrlReferences(new JpaStorageSettings().isAllowInlineMatchUrlReferences());
	}

	@BeforeEach
	public void beforeEach() {
		myStorageSettings = new JpaStorageSettings();

		mySearchParamPresenceSvc = new SearchParamPresenceSvcImpl();
		mySearchParamPresenceSvc.setStorageSettings(myStorageSettings);

		myTransactionManager = new MockTransactionManager();

		myResourceHistoryTableDao = new MockResourceHistoryTableDao();

		myEntityManager = new MockEntityManager();

		myInterceptorSvc = new InterceptorService();

		myDaoRegistry = new DaoRegistry(ourFhirContext);

		myPartitionSettings = new PartitionSettings();

		myMetaTagSorter = new MetaTagSorterAlphabetical();

		myHapiTransactionService = new HapiTransactionService();
		myHapiTransactionService.setTransactionManager(myTransactionManager);
		myHapiTransactionService.setInterceptorBroadcaster(myInterceptorSvc);
		myHapiTransactionService.setRequestPartitionSvcForUnitTest(myRequestPartitionHelperSvc);
		myHapiTransactionService.setPartitionSettingsForUnitTest(new PartitionSettings());

		myTransactionProcessor = new TransactionProcessor();
		myTransactionProcessor.setContext(ourFhirContext);
		myTransactionProcessor.setTxManager(myTransactionManager);
		myTransactionProcessor.setEntityManagerForUnitTest(myEntityManager);
		myTransactionProcessor.setVersionAdapter(new TransactionProcessorVersionAdapterR4());
		myTransactionProcessor.setStorageSettings(myStorageSettings);
		myTransactionProcessor.setHapiTransactionService(myHapiTransactionService);
		myTransactionProcessor.setDaoRegistry(myDaoRegistry);
		myTransactionProcessor.setPartitionSettingsForUnitTest(this.myPartitionSettings);
		myTransactionProcessor.setIdHelperServiceForUnitTest(myIdHelperService);
		myTransactionProcessor.setFhirContextForUnitTest(ourFhirContext);
		myTransactionProcessor.setApplicationContextForUnitTest(myAppCtx);

		mySystemDao = new FhirSystemDaoR4();
		mySystemDao.setTransactionProcessorForUnitTest(myTransactionProcessor);
		mySystemDao.setStorageSettingsForUnitTest(myStorageSettings);

		when(myAppCtx.getBean(eq(IInstanceValidatorModule.class))).thenReturn(myInstanceValidatorSvc);
		when(myAppCtx.getBean(eq(IFhirSystemDao.class))).thenReturn(mySystemDao);

		myInMemoryResourceMatcher = new InMemoryResourceMatcher();

		myResourceVersionSvc = new MockResourceVersionSvc();

		myResourceChangeListenerCacheRefresher = new ResourceChangeListenerCacheRefresherImpl();
		myResourceChangeListenerCacheRefresher.setResourceVersionSvc(myResourceVersionSvc);

		when(myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(any(), any(), any(), anyLong())).thenAnswer(t -> {
			String resourceName = t.getArgument(0, String.class);
			SearchParameterMap searchParameterMap = t.getArgument(1, SearchParameterMap.class);
			IResourceChangeListener changeListener = t.getArgument(2, IResourceChangeListener.class);
			long refreshInterval = t.getArgument(3, Long.class);
			ResourceChangeListenerCache retVal = new ResourceChangeListenerCache(resourceName, changeListener, searchParameterMap, refreshInterval);
			retVal.setResourceChangeListenerCacheRefresher(myResourceChangeListenerCacheRefresher);
			return retVal;
		});

		myResourceChangeListenerRegistry = new ResourceChangeListenerRegistryImpl(ourFhirContext, myResourceChangeListenerCacheFactory, myInMemoryResourceMatcher);
		myResourceChangeListenerCacheRefresher.setResourceChangeListenerRegistry(myResourceChangeListenerRegistry);

		mySearchParamRegistry = new SearchParamRegistryImpl();
		mySearchParamRegistry.setResourceChangeListenerRegistry(myResourceChangeListenerRegistry);
		mySearchParamRegistry.setSearchParameterCanonicalizerForUnitTest(new SearchParameterCanonicalizer(ourFhirContext));
		mySearchParamRegistry.setFhirContext(ourFhirContext);
		mySearchParamRegistry.setStorageSettings(myStorageSettings);
		mySearchParamRegistry.registerListener();

		mySearchParamExtractor = new SearchParamExtractorR4();
		mySearchParamExtractor.setContext(ourFhirContext);
		mySearchParamExtractor.setSearchParamRegistry(mySearchParamRegistry);
		mySearchParamExtractor.setPartitionSettings(this.myPartitionSettings);
		mySearchParamExtractor.setStorageSettings(myStorageSettings);
		mySearchParamExtractor.start();

		mySearchParamExtractorSvc = new SearchParamExtractorService();
		mySearchParamExtractorSvc.setContext(ourFhirContext);
		mySearchParamExtractorSvc.setSearchParamExtractor(mySearchParamExtractor);
		mySearchParamExtractorSvc.setStorageSettings(myStorageSettings);

		myDaoSearchParamSynchronizer = new DaoSearchParamSynchronizer();
		myDaoSearchParamSynchronizer.setEntityManager(myEntityManager);
		myDaoSearchParamSynchronizer.setStorageSettings(myStorageSettings);

		mySearchParamWithInlineReferencesExtractor = new SearchParamWithInlineReferencesExtractor();
		mySearchParamWithInlineReferencesExtractor.setStorageSettings(myStorageSettings);
		mySearchParamWithInlineReferencesExtractor.setContext(ourFhirContext);
		mySearchParamWithInlineReferencesExtractor.setSearchParamExtractorService(mySearchParamExtractorSvc);

		myEobDao = new JpaResourceDao<>();
		myEobDao.setContext(ourFhirContext);
		myEobDao.setStorageSettingsForUnitTest(myStorageSettings);
		myEobDao.setResourceType(ExplanationOfBenefit.class);
		myEobDao.setApplicationContext(myAppCtx);
		myEobDao.setTransactionService(myHapiTransactionService);
		myEobDao.setRequestPartitionHelperService(new MockRequestPartitionHelperSvc());
		myEobDao.setEntityManager(myEntityManager);
		myEobDao.setSearchParamWithInlineReferencesExtractor(mySearchParamWithInlineReferencesExtractor);
		myEobDao.setResourceHistoryTableDao(myResourceHistoryTableDao);
		myEobDao.setSearchParamRegistry(mySearchParamRegistry);
		myEobDao.setSearchParamPresenceSvc(mySearchParamPresenceSvc);
		myEobDao.setDaoSearchParamSynchronizer(myDaoSearchParamSynchronizer);
		myEobDao.setIdHelperSvcForUnitTest(myIdHelperService);
		myEobDao.setPartitionSettingsForUnitTest(myPartitionSettings);
		myEobDao.setJpaStorageResourceParserForUnitTest(myJpaStorageResourceParser);
		myEobDao.setExternallyStoredResourceServiceRegistryForUnitTest(new ExternallyStoredResourceServiceRegistry());
		myEobDao.setMyMetaTagSorter(myMetaTagSorter);
		myEobDao.setResourceHistoryCalculator(myResourceHistoryCalculator);
		myEobDao.start();

		myDaoRegistry.setResourceDaos(Lists.newArrayList(myEobDao));
	}

	@Test
	public void testTransaction() {
		Bundle input = ClasspathUtil.loadResource(ourFhirContext, Bundle.class, "/r4/large-transaction.json");
		while (input.getEntry().size() > 1) {
			input.getEntry().remove(1);
		}

		ServletRequestDetails requestDetails = new ServletRequestDetails(myInterceptorSvc);
		requestDetails.setServletRequest(new MockServletRequest());

		mySystemDao.transaction(requestDetails, input);

		ourLog.info("Merges:\n * " + myEntityManager.myMergeCount.stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));

		assertThat(myEntityManager.myPersistCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList())).containsExactly("ResourceTable");
		assertThat(myEntityManager.myMergeCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList())).containsExactlyInAnyOrder("ResourceTable", "ResourceIndexedSearchParamToken", "ResourceIndexedSearchParamToken");
		assertEquals(1, myEntityManager.myFlushCount);
		assertEquals(1, myResourceVersionSvc.myGetVersionMap);
		assertEquals(1, myResourceHistoryTableDao.mySaveCount);
	}

	@Test
	@Disabled
	public void testTransactionStressTest() {
		myStorageSettings.setEnforceReferenceTargetTypes(false);
		myStorageSettings.setAllowInlineMatchUrlReferences(false);


		Bundle input = ClasspathUtil.loadResource(ourFhirContext, Bundle.class, "/r4/large-transaction.json");

		ServletRequestDetails requestDetails = new ServletRequestDetails(myInterceptorSvc);
		requestDetails.setServletRequest(new MockServletRequest());

		// Pre-warmup
		ourLog.info("Warming up...");
		for (int i = 0; i < 10; i++) {
			mySystemDao.transaction(requestDetails, input);
		}
		ourLog.info("Done warming up");

		StopWatch sw = new StopWatch();
		for (int i = 1; i < 10000; i++) {
			mySystemDao.transaction(requestDetails, input);
			if (i % 5 == 0) {
				ourLog.info("Processed {} - {}/second", i, sw.formatThroughput(i, TimeUnit.SECONDS));
			}

			myEntityManager.clearCounts();
		}

		assertThat(myEntityManager.myPersistCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList())).containsExactly("ResourceTable");
		assertThat(myEntityManager.myMergeCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList())).containsExactlyInAnyOrder("ResourceTable", "ResourceIndexedSearchParamToken", "ResourceIndexedSearchParamToken");
		assertEquals(1, myEntityManager.myFlushCount);
		assertEquals(1, myResourceVersionSvc.myGetVersionMap);
		assertEquals(1, myResourceHistoryTableDao.mySaveCount);
	}

	private class MockResourceVersionSvc implements IResourceVersionSvc {
		private int myGetVersionMap;

		@Nonnull
		@Override
		public ResourceVersionMap getVersionMap(RequestPartitionId theRequestPartitionId, String theResourceName, SearchParameterMap theSearchParamMap) {
			myGetVersionMap++;
			return ResourceVersionMap.fromResources(Lists.newArrayList());
		}

		@Override
		public ResourcePersistentIdMap getLatestVersionIdsForResourceIds(RequestPartitionId thePartition, List<IIdType> theIds) {
			return null;
		}
	}

	private static class MockResourceHistoryTableDao implements IResourceHistoryTableDao {
		private int mySaveCount;

		@Override
		public List<ResourceHistoryTable> findAllVersionsForResourceIdInOrder(Long theId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable findForIdAndVersionAndFetchProvenance(long theId, long theVersion) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Slice<Long> findForResourceId(Pageable thePage, Long theId, Long theDontWantVersion) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Slice<ResourceHistoryTable> findForResourceIdAndReturnEntitiesAndFetchProvenance(Pageable thePage, Long theId, Long theDontWantVersion) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Slice<Long> findIdsOfPreviousVersionsOfResourceId(Pageable thePage, Long theResourceId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Slice<Long> findIdsOfPreviousVersionsOfResources(Pageable thePage, String theResourceName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Slice<Long> findIdsOfPreviousVersionsOfResources(Pageable thePage) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateVersion(long theId, long theOldVersion, long theNewVersion) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteByPid(Long theId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateNonInlinedContents(byte[] theText, long thePid) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public List<ResourceHistoryTable> findAll() {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public List<ResourceHistoryTable> findAll(@Nonnull Sort sort) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public Page<ResourceHistoryTable> findAll(@Nonnull Pageable pageable) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public List<ResourceHistoryTable> findAllById(@Nonnull Iterable<Long> ids) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long count() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteById(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void delete(@Nonnull ResourceHistoryTable entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllById(@Nonnull Iterable<? extends Long> ids) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll(@Nonnull Iterable<? extends ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> S save(@Nonnull S entity) {
			mySaveCount++;
			return entity;
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> List<S> saveAll(@Nonnull Iterable<S> entities) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public Optional<ResourceHistoryTable> findById(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean existsById(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void flush() {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> S saveAndFlush(@Nonnull S entity) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> List<S> saveAllAndFlush(@Nonnull Iterable<S> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteInBatch(@Nonnull Iterable<ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllInBatch(@Nonnull Iterable<ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllByIdInBatch(@Nonnull Iterable<Long> ids) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllInBatch() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable getOne(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable getById(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable getReferenceById(@Nonnull Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> Optional<S> findOne(@Nonnull Example<S> example) {
			return Optional.empty();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> List<S> findAll(@Nonnull Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> List<S> findAll(@Nonnull Example<S> example, @Nonnull Sort sort) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable> Page<S> findAll(@Nonnull Example<S> example, @Nonnull Pageable pageable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> long count(@Nonnull Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> boolean exists(@Nonnull Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@Override
		public <S extends ResourceHistoryTable, R> R findBy(@Nonnull Example<S> example, @Nonnull Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
			throw new UnsupportedOperationException();
		}
	}

	private static class MockEntityManager implements EntityManager {
		private final List<Object> myPersistCount = new ArrayList<>();
		private final List<Object> myMergeCount = new ArrayList<>();
		private long ourNextId = 0L;
		private int myFlushCount;

		@Override
		public void persist(Object entity) {
			myPersistCount.add(entity);
			if (entity instanceof ResourceTable) {
				((ResourceTable) entity).setId(ourNextId++);
			}
		}

		@Override
		public <T> T merge(T entity) {
			myMergeCount.add(entity);
			return entity;
		}

		@Override
		public void remove(Object entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T getReference(Class<T> entityClass, Object primaryKey) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void flush() {
			myFlushCount++;
		}

		@Override
		public FlushModeType getFlushMode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlushMode(FlushModeType flushMode) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void lock(Object entity, LockModeType lockMode) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void refresh(Object entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void refresh(Object entity, Map<String, Object> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void refresh(Object entity, LockModeType lockMode) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void detach(Object entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(Object entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public LockModeType getLockMode(Object entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProperty(String propertyName, Object value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, Object> getProperties() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createQuery(String qlString) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createQuery(CriteriaUpdate updateQuery) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createQuery(CriteriaDelete deleteQuery) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createNamedQuery(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createNativeQuery(String sqlString) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createNativeQuery(String sqlString, Class resultClass) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Query createNativeQuery(String sqlString, String resultSetMapping) {
			throw new UnsupportedOperationException();
		}

		@Override
		public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
			throw new UnsupportedOperationException();
		}

		@Override
		public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void joinTransaction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isJoinedToTransaction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T unwrap(Class<T> cls) {
			if (cls.equals(SessionImpl.class)) {
				return null;
			}
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getDelegate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void close() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isOpen() {
			throw new UnsupportedOperationException();
		}

		@Override
		public EntityTransaction getTransaction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public EntityManagerFactory getEntityManagerFactory() {
			throw new UnsupportedOperationException();
		}

		@Override
		public CriteriaBuilder getCriteriaBuilder() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Metamodel getMetamodel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public EntityGraph<?> createEntityGraph(String graphName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public EntityGraph<?> getEntityGraph(String graphName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
			throw new UnsupportedOperationException();
		}

		public void clearCounts() {
			myMergeCount.clear();
			myPersistCount.clear();
		}
	}

	private static class MockServletRequest extends MockHttpServletRequest {
	}

	private static class MockRequestPartitionHelperSvc implements ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc {
		@Nonnull
		@Override
		public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull ReadPartitionIdRequestDetails theDetails) {
			return RequestPartitionId.defaultPartition();
		}

		@Override
		public RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails) {
			return RequestPartitionId.defaultPartition();
		}

		@Nonnull
		@Override
		public RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
			return RequestPartitionId.defaultPartition();
		}

		@Nonnull
		@Override
		public Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId) {
			assert theRequestPartitionId.getPartitionIds().size() == 1;
			return Collections.singleton(theRequestPartitionId.getFirstPartitionIdOrNull());
		}

		@Override
		public boolean isResourcePartitionable(String theResourceType) {
			return true;
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId) {
			return RequestPartitionId.defaultPartition();
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId) {
			return RequestPartitionId.defaultPartition();
		}


	}

	private static class MockTransactionManager implements PlatformTransactionManager {


		@Nonnull
		@Override
		public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
			TransactionSynchronizationManager.setActualTransactionActive(true);
			return new SimpleTransactionStatus();
		}

		@Override
		public void commit(@Nonnull TransactionStatus status) throws TransactionException {
			TransactionSynchronizationManager.setActualTransactionActive(false);
		}

		@Override
		public void rollback(@Nonnull TransactionStatus status) throws TransactionException {
			TransactionSynchronizationManager.setActualTransactionActive(false);
		}
	}
}
