package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl.SEARCH_CLEANUP_JOB_INTERVAL_MILLIS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("Duplicates")
public class FhirResourceDaoR4SearchPageExpiryTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchPageExpiryTest.class);

	@Autowired
	private ISearchDao mySearchEntityDao;

	@AfterEach()
	public void after() {
		DatabaseSearchCacheSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(mySearchCacheSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(SEARCH_CLEANUP_JOB_INTERVAL_MILLIS);
		DatabaseSearchCacheSvcImpl.setNowForUnitTests(null);
	}

	@BeforeEach
	public void before() {
		DatabaseSearchCacheSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(mySearchCacheSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setCountSearchResultsUpTo(10000);
	}

	@Test
	public void testExpirePagesAfterReuse() throws Exception {
		IIdType pid1;
		IIdType pid2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);

		long reuseCachedSearchResultsForMillis = 500L;
		myDaoConfig.setReuseCachedSearchResultsForMillis(reuseCachedSearchResultsForMillis);
		long millisBetweenReuseAndExpire = 800L;
		long expireSearchResultsAfterMillis = 1000L;
		myDaoConfig.setExpireSearchResultsAfterMillis(expireSearchResultsAfterMillis);
		long start = System.currentTimeMillis();
		DatabaseSearchCacheSvcImpl.setNowForUnitTests(start);

		final String searchUuid1;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid1 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid1);
		}

		final String searchUuid2;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid2 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid2);
		}
		assertEquals(searchUuid1, searchUuid2);

		ca.uhn.fhir.util.TestUtil.sleepAtLeast(reuseCachedSearchResultsForMillis + 1);

		// We're now past reuseCachedSearchResultsForMillis so we shouldn't reuse the search

		final String searchUuid3;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid3 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid3);
		}
		assertNotEquals(searchUuid1, searchUuid3);

		// Search just got used so it shouldn't be deleted

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(start + reuseCachedSearchResultsForMillis);
		final AtomicLong search1timestamp = new AtomicLong();
		final AtomicLong search2timestamp = new AtomicLong();
		final AtomicLong search3timestamp = new AtomicLong();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search1 = mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search1);
				search1timestamp.set(search1.getCreated().getTime());
				Search search2 = mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid2).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search2);
				search2timestamp.set(search2.getCreated().getTime());
				Search search3 = mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search3);
				search3timestamp.set(search3.getCreated().getTime());
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(search1timestamp.get() + millisBetweenReuseAndExpire);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertTrue(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).isPresent());
				assertTrue(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).isPresent());
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(search1timestamp.get() + reuseCachedSearchResultsForMillis + expireSearchResultsAfterMillis + 1);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertFalse(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).isPresent(), "Search 1 still exists");
				assertTrue(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).isPresent(), "Search 3 still exists");
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(search3timestamp.get() + reuseCachedSearchResultsForMillis + expireSearchResultsAfterMillis + 1);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		await().until(() -> newTxTemplate().execute(t -> !mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).isPresent()));
		await().until(() -> newTxTemplate().execute(t -> !mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).isPresent()));
	}

	@Test
	public void testExpirePagesAfterSingleUse() throws Exception {
		IIdType pid1;
		IIdType pid2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);

		final StopWatch sw = new StopWatch();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
		final IBundleProvider bundleProvider = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));

		waitForSearchToSave(bundleProvider.getUuid());
		final AtomicLong start = new AtomicLong();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search, "Failed after " + sw.toString());
				start.set(search.getCreated().getTime());
				ourLog.info("Search was created: {}", new InstantType(new Date(start.get())));
			}
		});

		int expireSearchResultsAfterMillis = 700;
		myDaoConfig.setExpireSearchResultsAfterMillis(expireSearchResultsAfterMillis);
		long reuseCachedSearchResultsForMillis = 400L;
		myDaoConfig.setReuseCachedSearchResultsForMillis(reuseCachedSearchResultsForMillis);
		DatabaseSearchCacheSvcImpl.setNowForUnitTests(start.get() + expireSearchResultsAfterMillis - 1);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()));
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(start.get() + expireSearchResultsAfterMillis + reuseCachedSearchResultsForMillis + 1);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertFalse(mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()).isPresent());
			}
		});
	}

	@Test
	public void testExpirePagesAfterSingleUse2() throws Exception {
		IIdType pid1;
		IIdType pid2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			pid2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		Thread.sleep(10);

		long expireSearchResultsAfterMillis = 1000L;
		myDaoConfig.setExpireSearchResultsAfterMillis(expireSearchResultsAfterMillis);

		long reuseCachedSearchResultsForMillis = 500L;
		myDaoConfig.setReuseCachedSearchResultsForMillis(reuseCachedSearchResultsForMillis);

		long start = System.currentTimeMillis();

		final String searchUuid1;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid1 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid1);
		}

		waitForSearchToSave(searchUuid1);

		String searchUuid2;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid2 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid2);
		}
		assertEquals(searchUuid1, searchUuid2);

		ca.uhn.fhir.util.TestUtil.sleepAtLeast(reuseCachedSearchResultsForMillis + 1);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		// We're now past reuseCachedSearchResultsForMillis so we shouldn't reuse the search

		final String searchUuid3;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid3 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid3);
		}
		assertNotEquals(searchUuid1, searchUuid3);

		waitForSearchToSave(searchUuid3);

		// Search hasn't expired yet so it shouldn't be deleted

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		final AtomicLong search1timestamp = new AtomicLong();
		final AtomicLong search3timestamp = new AtomicLong();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search1 = mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				Search search3 = mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search3);
				search1timestamp.set(search1.getCreated().getTime());
				search3timestamp.set(search3.getCreated().getTime());
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(search1timestamp.get() + expireSearchResultsAfterMillis + reuseCachedSearchResultsForMillis + 1);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertFalse(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).isPresent());
				assertTrue(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).isPresent());
			}
		});

		DatabaseSearchCacheSvcImpl.setNowForUnitTests(search3timestamp.get() + expireSearchResultsAfterMillis + reuseCachedSearchResultsForMillis + 1);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertFalse(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid1).isPresent(), "Search 1 still exists");
				assertFalse(mySearchEntityDao.findByUuidAndFetchIncludes(searchUuid3).isPresent(), "Search 3 still exists");
			}
		});

	}

	@Test
	public void testSearchPagesExpiryDisabled() throws Exception {
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addName().setFamily("EXPIRE");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
		params.setCount(1);
		final IBundleProvider bundleProvider = myPatientDao.search(params);

		Search search = null;
		for (int i = 0; i < 100 && search == null; i++) {
			search = newTxTemplate().execute(new TransactionCallback<Search>() {
				@Nullable
				@Override
				public Search doInTransaction(TransactionStatus status) {
					return mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()).orElse(null);
				}
			});
			if (search == null) {
				ca.uhn.fhir.util.TestUtil.sleepAtLeast(100);
			}
		}
		assertNotNull(search, "Search " + bundleProvider.getUuid() + " not found on disk after 10 seconds");


		myDaoConfig.setExpireSearchResults(false);
		DatabaseSearchCacheSvcImpl.setNowForUnitTests(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()).orElseThrow(() -> new InternalErrorException("Search doesn't exist"));
				assertNotNull(search);
			}
		});

		mySearchCoordinatorSvc.cancelAllActiveSearches();

		myDaoConfig.setExpireSearchResults(true);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(bundleProvider.getUuid()).orElse(null);
				assertNull(search);
			}
		});

	}

	private void waitForSearchToSave(final String theUuid) {
		final ISearchDao searchEntityDao = mySearchEntityDao;
		TransactionTemplate txTemplate = newTxTemplate();
		FhirResourceDaoR4SearchPageExpiryTest.waitForSearchToSave(theUuid, searchEntityDao, txTemplate);
	}

	public static void waitForSearchToSave(final String theUuid, final ISearchDao theSearchEntityDao, TransactionTemplate theTxTemplate) {
		theTxTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = null;
				for (int i = 0; i < 20 && search == null; i++) {
					search = theSearchEntityDao.findByUuidAndFetchIncludes(theUuid).orElse(null);
					if (search == null || search.getStatus() == SearchStatusEnum.LOADING) {
						TestUtil.sleepAtLeast(100);
					}
				}
				assertNotNull(search);
			}
		});
	}
}
