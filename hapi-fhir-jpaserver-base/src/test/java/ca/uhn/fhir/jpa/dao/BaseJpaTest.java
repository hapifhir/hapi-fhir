package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.TestUtil.randomizeLocale;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	UnregisterScheduledProcessor.SCHEDULING_DISABLED_EQUALS_TRUE
})
public abstract class BaseJpaTest extends BaseTest {

	protected static final String CM_URL = "http://example.com/my_concept_map";
	protected static final String CS_URL = "http://example.com/my_code_system";
	protected static final String CS_URL_2 = "http://example.com/my_code_system2";
	protected static final String CS_URL_3 = "http://example.com/my_code_system3";
	protected static final String CS_URL_4 = "http://example.com/my_code_system4";
	protected static final String VS_URL = "http://example.com/my_value_set";
	protected static final String VS_URL_2 = "http://example.com/my_value_set2";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaTest.class);

	static {
		System.setProperty(Constants.TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS, "1000");
		System.setProperty("test", "true");
		TestUtil.setShouldRandomizeTimezones(false);
	}

	@RegisterExtension
	public LoggingExtension myLoggingExtension = new LoggingExtension();
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	protected ServletRequestDetails mySrd;
	protected InterceptorService myRequestOperationCallback;
	@Autowired
	protected DatabaseBackedPagingProvider myDatabaseBackedPagingProvider;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;
	@Autowired
	protected ISearchResultCacheSvc mySearchResultCacheSvc;
	@Autowired
	protected ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	protected IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private MemoryCacheService myMemoryCacheService;
	@Qualifier(BaseConfig.JPA_VALIDATION_SUPPORT)
	@Autowired
	private IValidationSupport myJpaPersistedValidationSupport;
	@Autowired
	private FhirInstanceValidator myFhirInstanceValidator;

	@AfterEach
	public void afterPerformCleanup() {
		BaseHapiFhirDao.setDisableIncrementOnUpdateForUnitTest(false);
		if (myCaptureQueriesListener != null) {
			myCaptureQueriesListener.clear();
		}
		if (myPartitionConfigSvc != null) {
			myPartitionConfigSvc.clearCaches();
		}
		if (myMemoryCacheService != null) {
			myMemoryCacheService.invalidateAllCaches();
		}
		if (myJpaPersistedValidationSupport != null) {
			ProxyUtil.getSingletonTarget(myJpaPersistedValidationSupport, JpaPersistedResourceValidationSupport.class).clearCaches();
		}
		if (myFhirInstanceValidator != null) {
			myFhirInstanceValidator.invalidateCaches();
		}

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
				currentSession = sessionFactory.getCurrentSession();
			} catch (HibernateException e) {
				currentSession = null;
			}
			if (currentSession != null) {
				currentSession.doWork(new Work() {

					@Override
					public void execute(Connection connection) throws SQLException {
						isReadOnly.set(connection.isReadOnly());
					}
				});

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
	public void beforeInitMocks() {
		myRequestOperationCallback = new InterceptorService();

		MockitoAnnotations.initMocks(this);

		when(mySrd.getInterceptorBroadcaster()).thenReturn(myRequestOperationCallback);
		when(mySrd.getUserData()).thenReturn(new HashMap<>());
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(new ArrayList<>());
	}

	protected CountDownLatch registerLatchHookInterceptor(int theCount, Pointcut theLatchPointcut) {
		CountDownLatch deliveryLatch = new CountDownLatch(theCount);
		myInterceptorRegistry.registerAnonymousInterceptor(theLatchPointcut, Integer.MAX_VALUE, (thePointcut, t) -> deliveryLatch.countDown());
		return deliveryLatch;
	}

	protected abstract FhirContext getContext();

	protected abstract PlatformTransactionManager getTxManager();

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(getTxManager());
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	public void runInTransaction(Runnable theRunnable) {
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
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
	 * Sleep until at least 1 ms has elapsed
	 */
	public void sleepUntilTimeChanges() throws InterruptedException {
		StopWatch sw = new StopWatch();
		while (sw.getMillis() == 0) {
			Thread.sleep(10);
		}
	}

	protected org.hl7.fhir.dstu3.model.Bundle toBundle(IBundleProvider theSearch) {
		org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.size())) {
			bundle.addEntry().setResource((Resource) next);
		}
		return bundle;
	}

	protected org.hl7.fhir.r4.model.Bundle toBundleR4(IBundleProvider theSearch) {
		org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
		for (IBaseResource next : theSearch.getResources(0, theSearch.size())) {
			bundle.addEntry().setResource((org.hl7.fhir.r4.model.Resource) next);
		}
		return bundle;
	}

	@SuppressWarnings({"rawtypes"})
	protected List toList(IBundleProvider theSearch) {
		return theSearch.getResources(0, theSearch.size());
	}

	protected List<String> toUnqualifiedIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<String>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedIdValues(IBundleProvider theFound) {
		List<String> retVal = new ArrayList<String>();
		int size = theFound.size();
		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theFound.getResources(0, size);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualified().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBaseBundle theFound) {
		List<String> retVal = new ArrayList<String>();

		List<IBaseResource> res = BundleUtil.toListOfResources(getContext(), theFound);
		int size = res.size();
		ourLog.info("Found {} results", size);
		for (IBaseResource next : res) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	protected List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		int fromIndex = 0;
		Integer toIndex = theFound.size();
		return toUnqualifiedVersionlessIdValues(theFound, fromIndex, toIndex, true);
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

	protected List<IIdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (Entry next : theFound.getEntry()) {
			// if (next.getResource()!= null) {
			retVal.add(next.getResource().getId().toUnqualifiedVersionless());
			// }
		}
		return retVal;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(IBundleProvider theProvider) {

		List<IIdType> retVal = new ArrayList<>();
		Integer size = theProvider.size();
		StopWatch sw = new StopWatch();
		while (size == null) {
			int timeout = 20000;
			if (sw.getMillis() > timeout) {
				String message = "Waited over " + timeout + "ms for search " + theProvider.getUuid();
				ourLog.info(message);
				fail(message);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException theE) {
				//ignore
			}

			if (theProvider instanceof PersistedJpaBundleProvider) {
				PersistedJpaBundleProvider provider = (PersistedJpaBundleProvider) theProvider;
				provider.clearCachedDataForUnitTest();
			}
			size = theProvider.size();
		}

		ourLog.info("Found {} results", size);
		List<IBaseResource> resources = theProvider.getResources(0, size);
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

	@BeforeAll
	public static void beforeClassRandomizeLocale() {
		randomizeLocale();
	}

	@AfterAll
	public static void afterClassShutdownDerby() {
		// DriverManager.getConnection("jdbc:derby:;shutdown=true");
		// try {
		// DriverManager.getConnection("jdbc:derby:memory:myUnitTestDB;drop=true");
		// } catch (SQLNonTransientConnectionException e) {
		// // expected.. for some reason....
		// }
	}

	public static String loadClasspath(String resource) throws IOException {
		return new String(loadClasspathBytes(resource), Constants.CHARSET_UTF8);
	}

	public static byte[] loadClasspathBytes(String resource) throws IOException {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream(resource);
		if (bundleRes == null) {
			throw new NullPointerException("Can not load " + resource);
		}
		return IOUtils.toByteArray(bundleRes);
	}

	protected static void purgeDatabase(DaoConfig theDaoConfig, IFhirSystemDao<?, ?> theSystemDao, IResourceReindexingSvc theResourceReindexingSvc, ISearchCoordinatorSvc theSearchCoordinatorSvc, ISearchParamRegistry theSearchParamRegistry, IBulkDataExportSvc theBulkDataExportSvc) {
		theSearchCoordinatorSvc.cancelAllActiveSearches();
		theResourceReindexingSvc.cancelAndPurgeAllJobs();
		theBulkDataExportSvc.cancelAndPurgeAllJobs();

		boolean expungeEnabled = theDaoConfig.isExpungeEnabled();
		theDaoConfig.setExpungeEnabled(true);

		for (int count = 0; ; count++) {
			try {
				theSystemDao.expunge(new ExpungeOptions().setExpungeEverything(true), null);
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
		theDaoConfig.setExpungeEnabled(expungeEnabled);

		theSearchParamRegistry.forceRefresh();
	}

	protected static Set<String> toCodes(Set<TermConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<>();
		for (TermConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	protected static Set<String> toCodes(List<VersionIndependentConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<>();
		for (VersionIndependentConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	public static void waitForSize(int theTarget, List<?> theList) {
		StopWatch sw = new StopWatch();
		while (theList.size() != theTarget && sw.getMillis() <= 16000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= 16000) {
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

	public static void waitForSize(int theTarget, Callable<Number> theCallable) throws Exception {
		waitForSize(theTarget, 10000, theCallable);
	}

	public static void waitForSize(int theTarget, int theTimeout, Callable<Number> theCallable) throws Exception {
		StopWatch sw = new StopWatch();
		while (theCallable.call().intValue() != theTarget && sw.getMillis() < theTimeout) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= theTimeout) {
			fail("Size " + theCallable.call() + " is != target " + theTarget);
		}
		Thread.sleep(500);
	}

	public static void waitForSize(int theTarget, Callable<Number> theCallable, Callable<String> theFailureMessage) throws Exception {
		waitForSize(theTarget, 10000, theCallable, theFailureMessage);
	}

	public static void waitForSize(int theTarget, int theTimeout, Callable<Number> theCallable, Callable<String> theFailureMessage) throws Exception {
		StopWatch sw = new StopWatch();
		while (theCallable.call().intValue() != theTarget && sw.getMillis() < theTimeout) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if (sw.getMillis() >= theTimeout) {
			fail("Size " + theCallable.call() + " is != target " + theTarget + " - " + theFailureMessage.call());
		}
		Thread.sleep(500);
	}

}
