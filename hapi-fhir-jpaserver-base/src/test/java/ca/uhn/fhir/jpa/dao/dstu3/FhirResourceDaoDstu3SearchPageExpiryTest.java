package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class FhirResourceDaoDstu3SearchPageExpiryTest extends BaseJpaDstu3Test {
	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@After()
	public void after() {
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_CUTOFF_SLACK);
	}

	@Before
	public void before() {
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
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

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
		final IBundleProvider bundleProvider = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
		assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));

		myDaoConfig.setExpireSearchResultsAfterMillis(500);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(bundleProvider.getUuid()));
			}
		});

		Thread.sleep(750);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull(mySearchEntityDao.findByUuid(bundleProvider.getUuid()));
			}
		});
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

		final String searchUuid1;
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("EXPIRE"));
			final IBundleProvider bundleProvider = myPatientDao.search(params);
			assertThat(toUnqualifiedVersionlessIds(bundleProvider), containsInAnyOrder(pid1, pid2));
			searchUuid1 = bundleProvider.getUuid();
			Validate.notBlank(searchUuid1);
		}

		Thread.sleep(250);

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

		Thread.sleep(500);

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
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid1));
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid3));
			}
		});

		Thread.sleep(750);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull(mySearchEntityDao.findByUuid(searchUuid1));
				assertNotNull(mySearchEntityDao.findByUuid(searchUuid3));
			}
		});

		Thread.sleep(300);

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				assertNull(mySearchEntityDao.findByUuid(searchUuid1));
				assertNull(mySearchEntityDao.findByUuid(searchUuid3));
			}
		});

	}
}
