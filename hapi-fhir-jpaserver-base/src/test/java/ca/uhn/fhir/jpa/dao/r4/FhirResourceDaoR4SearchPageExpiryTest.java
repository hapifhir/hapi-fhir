package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class FhirResourceDaoR4SearchPageExpiryTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchPageExpiryTest.class);

	@After()
	public void after() {
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_CUTOFF_SLACK);
		StaleSearchDeletingSvcImpl.setNowForUnitTests(null);
	}

	@Before
	public void before() {
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
	}

	@Before
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

		myDaoConfig.setExpireSearchResultsAfterMillis(1000L);
		myDaoConfig.setReuseCachedSearchResultsForMillis(500L);
		long start = System.currentTimeMillis();
		StaleSearchDeletingSvcImpl.setNowForUnitTests(start);

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

		TestUtil.sleepAtLeast(501);

		// We're now past 500ms so we shouldn't reuse the search

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

		StaleSearchDeletingSvcImpl.setNowForUnitTests(start + 500);
		final AtomicLong search3timestamp = new AtomicLong();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search3 = mySearchEntityDao.findByUuid(searchUuid3);
				assertNotNull(search3);
				Search search2 = mySearchEntityDao.findByUuid(searchUuid2);
				assertNotNull(search2);
				search3timestamp.set(search2.getSearchLastReturned().getTime());
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(search3timestamp.get() + 800);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid3));
			}
		});
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid1));
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(search3timestamp.get() + 1100);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull("Search 1 still exists", mySearchEntityDao.findByUuid(searchUuid1));
				assertNotNull("Search 3 still exists", mySearchEntityDao.findByUuid(searchUuid3));
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(search3timestamp.get() + 2100);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull("Search 1 still exists", mySearchEntityDao.findByUuid(searchUuid1));
				assertNull("Search 3 still exists", mySearchEntityDao.findByUuid(searchUuid3));
			}
		});

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
		assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));

		waitForSearchToSave(bundleProvider.getUuid());
		final AtomicLong start = new AtomicLong();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuid(bundleProvider.getUuid());
				assertNotNull("Failed after " + sw.toString(), search);
				start.set(search.getCreated().getTime());
				ourLog.info("Search was created: {}", new InstantType(new Date(start.get())));
			}
		});

		myDaoConfig.setExpireSearchResultsAfterMillis(500);
		myDaoConfig.setReuseCachedSearchResultsForMillis(500L);
		StaleSearchDeletingSvcImpl.setNowForUnitTests(start.get() + 499);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(bundleProvider.getUuid()));
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(start.get() + 600);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull(mySearchEntityDao.findByUuid(bundleProvider.getUuid()));
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

		myDaoConfig.setExpireSearchResultsAfterMillis(1000L);
		myDaoConfig.setReuseCachedSearchResultsForMillis(500L);
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

		TestUtil.sleepAtLeast(501);

		// We're now past 500ms so we shouldn't reuse the search

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

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		final AtomicLong search3timestamp = new AtomicLong();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search3 = mySearchEntityDao.findByUuid(searchUuid3);
				assertNotNull(search3);
				search3timestamp.set(search3.getCreated().getTime());
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(search3timestamp.get() + 800);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid3));
			}
		});
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull(mySearchEntityDao.findByUuid(searchUuid1));
			}
		});

		StaleSearchDeletingSvcImpl.setNowForUnitTests(search3timestamp.get() + 1100);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull("Search 1 still exists", mySearchEntityDao.findByUuid(searchUuid1));
				assertNull("Search 3 still exists", mySearchEntityDao.findByUuid(searchUuid3));
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
					return mySearchEntityDao.findByUuid(bundleProvider.getUuid());
				}
			});
			if (search == null) {
				TestUtil.sleepAtLeast(100);
			}
		}
		assertNotNull("Search " + bundleProvider.getUuid() + " not found on disk after 10 seconds", search);


		myDaoConfig.setExpireSearchResults(false);
		StaleSearchDeletingSvcImpl.setNowForUnitTests(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuid(bundleProvider.getUuid());
				assertNotNull(search);
			}
		});

		mySearchCoordinatorSvc.cancelAllActiveSearches();

		myDaoConfig.setExpireSearchResults(true);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search search = mySearchEntityDao.findByUuid(bundleProvider.getUuid());
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
					search = theSearchEntityDao.findByUuid(theUuid);
					if (search == null || search.getStatus() == SearchStatusEnum.LOADING) {
						TestUtil.sleepAtLeast(100);
					}
				}
				assertNotNull(search);
			}
		});
	}
}
