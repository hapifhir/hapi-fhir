package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
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
import ca.uhn.fhir.jpa.dao.JpaResourceDao;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer;
import ca.uhn.fhir.jpa.dao.index.SearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
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
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
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
import org.quartz.JobKey;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private DaoConfig myDaoConfig;
	private HapiTransactionService myHapiTransactionService;
	private DaoRegistry myDaoRegistry;
	private JpaResourceDao<ExplanationOfBenefit> myEobDao;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private ApplicationContext myAppCtx;
	@Mock
	private IInstanceValidatorModule myInstanceValidatorSvc;
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

	@AfterEach
	public void afterEach() {
		myDaoConfig.setEnforceReferenceTargetTypes(new DaoConfig().isEnforceReferenceTargetTypes());
		myDaoConfig.setAllowInlineMatchUrlReferences(new DaoConfig().isAllowInlineMatchUrlReferences());
	}

	@BeforeEach
	public void beforeEach() {
		myDaoConfig = new DaoConfig();

		mySearchParamPresenceSvc = new SearchParamPresenceSvcImpl();
		mySearchParamPresenceSvc.setDaoConfig(myDaoConfig);

		myTransactionManager = new MockTransactionManager();

		myResourceHistoryTableDao = new MockResourceHistoryTableDao();

		myEntityManager = new MockEntityManager();

		myInterceptorSvc = new InterceptorService();

		myDaoRegistry = new DaoRegistry(ourFhirContext);

		myPartitionSettings = new PartitionSettings();

		myHapiTransactionService = new HapiTransactionService();
		myHapiTransactionService.setTransactionManager(myTransactionManager);
		myHapiTransactionService.setInterceptorBroadcaster(myInterceptorSvc);
		myHapiTransactionService.start();

		myTransactionProcessor = new TransactionProcessor();
		myTransactionProcessor.setContext(ourFhirContext);
		myTransactionProcessor.setDao(mySystemDao);
		myTransactionProcessor.setTxManager(myTransactionManager);
		myTransactionProcessor.setEntityManagerForUnitTest(myEntityManager);
		myTransactionProcessor.setVersionAdapter(new TransactionProcessorVersionAdapterR4());
		myTransactionProcessor.setDaoConfig(myDaoConfig);
		myTransactionProcessor.setModelConfig(myDaoConfig.getModelConfig());
		myTransactionProcessor.setHapiTransactionService(myHapiTransactionService);
		myTransactionProcessor.setDaoRegistry(myDaoRegistry);
		myTransactionProcessor.setPartitionSettingsForUnitTest(this.myPartitionSettings);
		myTransactionProcessor.setIdHelperServiceForUnitTest(myIdHelperService);
		myTransactionProcessor.setFhirContextForUnitTest(ourFhirContext);
		myTransactionProcessor.setApplicationContextForUnitTest(myAppCtx);
		myTransactionProcessor.start();

		mySystemDao = new FhirSystemDaoR4();
		mySystemDao.setTransactionProcessorForUnitTest(myTransactionProcessor);
		mySystemDao.setDaoConfigForUnitTest(myDaoConfig);
		mySystemDao.setPartitionSettingsForUnitTest(myPartitionSettings);
		mySystemDao.start();

		when(myAppCtx.getBean(eq(IInstanceValidatorModule.class))).thenReturn(myInstanceValidatorSvc);
		when(myAppCtx.getBean(eq(IFhirSystemDao.class))).thenReturn(mySystemDao);

		myInMemoryResourceMatcher = new InMemoryResourceMatcher();

		myResourceVersionSvc = new MockResourceVersionSvc();

		myResourceChangeListenerCacheRefresher = new ResourceChangeListenerCacheRefresherImpl();
		myResourceChangeListenerCacheRefresher.setSchedulerService(new MockSchedulerSvc());
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
		mySearchParamRegistry.setModelConfig(myDaoConfig.getModelConfig());
		mySearchParamRegistry.registerListener();

		mySearchParamExtractor = new SearchParamExtractorR4();
		mySearchParamExtractor.setContext(ourFhirContext);
		mySearchParamExtractor.setSearchParamRegistry(mySearchParamRegistry);
		mySearchParamExtractor.setPartitionSettings(this.myPartitionSettings);
		mySearchParamExtractor.setModelConfig(myDaoConfig.getModelConfig());
		mySearchParamExtractor.start();

		mySearchParamExtractorSvc = new SearchParamExtractorService();
		mySearchParamExtractorSvc.setContext(ourFhirContext);
		mySearchParamExtractorSvc.setSearchParamExtractor(mySearchParamExtractor);
		mySearchParamExtractorSvc.setModelConfig(myDaoConfig.getModelConfig());

		myDaoSearchParamSynchronizer = new DaoSearchParamSynchronizer();
		myDaoSearchParamSynchronizer.setEntityManager(myEntityManager);

		mySearchParamWithInlineReferencesExtractor = new SearchParamWithInlineReferencesExtractor();
		mySearchParamWithInlineReferencesExtractor.setDaoConfig(myDaoConfig);
		mySearchParamWithInlineReferencesExtractor.setContext(ourFhirContext);
		mySearchParamWithInlineReferencesExtractor.setPartitionSettings(this.myPartitionSettings);
		mySearchParamWithInlineReferencesExtractor.setSearchParamExtractorService(mySearchParamExtractorSvc);
		mySearchParamWithInlineReferencesExtractor.setSearchParamRegistry(mySearchParamRegistry);
		mySearchParamWithInlineReferencesExtractor.setDaoSearchParamSynchronizer(myDaoSearchParamSynchronizer);

		myEobDao = new JpaResourceDao<>();
		myEobDao.setContext(ourFhirContext);
		myEobDao.setDaoConfigForUnitTest(myDaoConfig);
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
		myEobDao.setDaoConfigForUnitTest(myDaoConfig);
		myEobDao.setIdHelperSvcForUnitTest(myIdHelperService);
		myEobDao.setPartitionSettingsForUnitTest(myPartitionSettings);
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

		assertThat(myEntityManager.myPersistCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList()), Matchers.contains("ResourceTable"));
		assertThat(myEntityManager.myMergeCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList()), Matchers.containsInAnyOrder("ResourceTable", "ResourceIndexedSearchParamToken", "ResourceIndexedSearchParamToken"));
		assertEquals(1, myEntityManager.myFlushCount);
		assertEquals(1, myResourceVersionSvc.myGetVersionMap);
		assertEquals(1, myResourceHistoryTableDao.mySaveCount);
	}

	@Test
	@Disabled
	public void testTransactionStressTest() {
		myDaoConfig.setEnforceReferenceTargetTypes(false);
		myDaoConfig.setAllowInlineMatchUrlReferences(false);


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

		assertThat(myEntityManager.myPersistCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList()), Matchers.contains("ResourceTable"));
		assertThat(myEntityManager.myMergeCount.stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList()), Matchers.containsInAnyOrder("ResourceTable", "ResourceIndexedSearchParamToken", "ResourceIndexedSearchParamToken"));
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

	private class MockResourceHistoryTableDao implements IResourceHistoryTableDao {
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
		public List<ResourceHistoryTable> findAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<ResourceHistoryTable> findAll(Sort sort) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Page<ResourceHistoryTable> findAll(Pageable pageable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<ResourceHistoryTable> findAllById(Iterable<Long> ids) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long count() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteById(Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void delete(ResourceHistoryTable entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllById(Iterable<? extends Long> ids) {

		}

		@Override
		public void deleteAll(Iterable<? extends ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> S save(S entity) {
			mySaveCount++;
			return entity;
		}

		@Override
		public <S extends ResourceHistoryTable> List<S> saveAll(Iterable<S> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Optional<ResourceHistoryTable> findById(Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean existsById(Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void flush() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> S saveAndFlush(S entity) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> List<S> saveAllAndFlush(Iterable<S> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteInBatch(Iterable<ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllInBatch(Iterable<ResourceHistoryTable> entities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllByIdInBatch(Iterable<Long> ids) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAllInBatch() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable getOne(Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceHistoryTable getById(Long theLong) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> Optional<S> findOne(Example<S> example) {
			return Optional.empty();
		}

		@Override
		public <S extends ResourceHistoryTable> List<S> findAll(Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> List<S> findAll(Example<S> example, Sort sort) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> Page<S> findAll(Example<S> example, Pageable pageable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> long count(Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable> boolean exists(Example<S> example) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends ResourceHistoryTable, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
			throw new UnsupportedOperationException();
		}
	}

	private class MockEntityManager implements EntityManager {
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

	private static class MockSchedulerSvc implements ISchedulerService {
		@Override
		public void purgeAllScheduledJobsForUnitTest() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void logStatusForUnitTest() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scheduleLocalJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void scheduleClusteredJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<JobKey> getLocalJobKeysForUnitTest() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<JobKey> getClusteredJobKeysForUnitTest() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isStopping() {
			return false;
		}
	}

	private static class MockServletRequest extends MockHttpServletRequest {
	}

	private static class MockRequestPartitionHelperSvc implements ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc {
		@Nonnull
		@Override
		public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType, @Nonnull ReadPartitionIdRequestDetails theDetails) {
			return RequestPartitionId.defaultPartition();
		}

		@Nonnull
		@Override
		public RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
			return RequestPartitionId.defaultPartition();
		}

		@Override
		@Nonnull
		public PartitionablePartitionId toStoragePartition(@Nonnull RequestPartitionId theRequestPartitionId) {
			return new PartitionablePartitionId(theRequestPartitionId.getFirstPartitionIdOrNull(), theRequestPartitionId.getPartitionDate());
		}

		@Nonnull
		@Override
		public Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId) {
			assert theRequestPartitionId.getPartitionIds().size() == 1;
			return Collections.singleton(theRequestPartitionId.getFirstPartitionIdOrNull());
		}


	}

	private static class MockTransactionManager implements PlatformTransactionManager {


		@Nonnull
		@Override
		public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
			return new SimpleTransactionStatus();
		}

		@Override
		public void commit(@Nonnull TransactionStatus status) throws TransactionException {

		}

		@Override
		public void rollback(@Nonnull TransactionStatus status) throws TransactionException {

		}
	}
}
