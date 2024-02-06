package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteByUrlTest extends BaseJpaR4Test {
	private static final String TEST_SYSTEM_NAME = "http://something.com";
	private static final String TEST_VALUE_NAME = "testValue";

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setAllowMultipleDelete(true);
	}

	@AfterEach
	public void after() {
		final JpaStorageSettings defaultStorageSettings = new JpaStorageSettings();
		myStorageSettings.setAllowMultipleDelete(defaultStorageSettings.isAllowMultipleDelete());
	}

	@Test
	public void testDeleteWithUrl() {
		final IFhirResourceDao<Patient> patientDao = unsafeCast(myDaoRegistry.getResourceDao("Patient"));
		final String testFamilyNameModified = "Jackson";

		querySpIndexAndAssertSize(0, myResourceIndexedCompositeStringUniqueDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamCoordsDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamDateDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamNumberDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamQuantityDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamStringDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamTokenDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamUriDao);

		assertEquals(0, myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails()).getAllResources().size());

		for (int index = 0; index < 50; index++) {
			final Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier()
				.setSystem(TEST_SYSTEM_NAME)
				.setValue(TEST_VALUE_NAME);
			patient.addName().setFamily(testFamilyNameModified);
			patient.setBirthDate(Date.from(LocalDate.of(2024, Month.FEBRUARY, 5).atStartOfDay(ZoneId.systemDefault()).toInstant()));
			patientDao.create(patient, new SystemRequestDetails());
		}

		assertEquals(50, myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails()).getAllResources().size());

		querySpIndexAndAssertSize(0, myResourceIndexedCompositeStringUniqueDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamCoordsDao);
		querySpIndexAndAssertSize(50, myResourceIndexedSearchParamDateDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamNumberDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamQuantityDao);
		querySpIndexAndAssertSize(150, myResourceIndexedSearchParamStringDao);
		querySpIndexAndAssertSize(150, myResourceIndexedSearchParamTokenDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamUriDao);

		final DeleteMethodOutcome deleteMethodOutcome = patientDao.deleteByUrl("Patient?_lastUpdated=gt2024-01-21", new SystemRequestDetails());

		assertEquals(50, deleteMethodOutcome.getDeletedEntities().size());

		assertEquals(0, myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails()).getAllResources().size());

		querySpIndexAndAssertSize(0, myResourceIndexedCompositeStringUniqueDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamCoordsDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamDateDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamNumberDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamQuantityDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamStringDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamTokenDao);
		querySpIndexAndAssertSize(0, myResourceIndexedSearchParamUriDao);
	}

	private <T> void querySpIndexAndAssertSize(int theExpectedCount, JpaRepository<T, Long> theDao) {
		assertEquals(theExpectedCount, theDao.findAll().size());
	}

	@SuppressWarnings("unchecked")
	private static <T>  T unsafeCast(Object theObject) {
		return (T)theObject;
	}
}
