package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.jobs.imprt.ConsumeFilesStep;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ConsumeFilesStepR4Test extends BaseJpaR4Test {

	@Autowired
	private ConsumeFilesStep mySvc;

	@Test
	public void testAlreadyExisting_NoChanges() {
		// Setup

		Patient patient = new Patient();
		patient.setId("A");
		patient.setActive(true);
		myPatientDao.update(patient);

		patient = new Patient();
		patient.setId("B");
		patient.setActive(false);
		myPatientDao.update(patient);


		List<IBaseResource> resources = new ArrayList<>();

		patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		resources.add(patient);

		patient = new Patient();
		patient.setId("Patient/B");
		patient.setActive(false);
		resources.add(patient);

		// Execute

		myMemoryCacheService.invalidateAllCaches();
		myCaptureQueriesListener.clear();
		mySvc.storeResources(resources);

		// Validate

		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		patient = myPatientDao.read(new IdType("Patient/A"));
		assertTrue(patient.getActive());
		patient = myPatientDao.read(new IdType("Patient/B"));
		assertFalse(patient.getActive());

	}

	@Test
	public void testAlreadyExisting_WithChanges() {
		// Setup

		Patient patient = new Patient();
		patient.setId("A");
		patient.setActive(false);
		myPatientDao.update(patient);

		patient = new Patient();
		patient.setId("B");
		patient.setActive(true);
		myPatientDao.update(patient);


		List<IBaseResource> resources = new ArrayList<>();

		patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		resources.add(patient);

		patient = new Patient();
		patient.setId("Patient/B");
		patient.setActive(false);
		resources.add(patient);

		// Execute

		myMemoryCacheService.invalidateAllCaches();
		myCaptureQueriesListener.clear();
		mySvc.storeResources(resources);

		// Validate

		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(2, myCaptureQueriesListener.logInsertQueries());
		assertEquals(4, myCaptureQueriesListener.logUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		patient = myPatientDao.read(new IdType("Patient/A"));
		assertTrue(patient.getActive());
		patient = myPatientDao.read(new IdType("Patient/B"));
		assertFalse(patient.getActive());

	}

	@Test
	public void testNotAlreadyExisting() {

		// Setup

		List<IBaseResource> resources = new ArrayList<>();

		Patient patient = new Patient();
		patient.setId("A");
		patient.setActive(true);
		resources.add(patient);

		patient = new Patient();
		patient.setId("B");
		patient.setActive(false);
		resources.add(patient);

		// Execute

		myCaptureQueriesListener.clear();
		mySvc.storeResources(resources);

		// Validate

		assertEquals(1, myCaptureQueriesListener.logSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false),
			either(containsString("forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='B' or forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='A'"))
				.or(containsString("forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='A' or forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='B'")));
		assertEquals(10, myCaptureQueriesListener.logInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());


		patient = myPatientDao.read(new IdType("Patient/A"));
		assertTrue(patient.getActive());
		patient = myPatientDao.read(new IdType("Patient/B"));
		assertFalse(patient.getActive());

	}

	@Test
	public void testNotAlreadyExisting_InvalidIdForStorage() {
		// Setup

		List<IBaseResource> resources = new ArrayList<>();

		Patient patient = new Patient();
		patient.setId("1");
		patient.setActive(true);
		resources.add(patient);

		patient = new Patient();
		patient.setId("2");
		patient.setActive(false);
		resources.add(patient);

		// Execute

		myCaptureQueriesListener.clear();
		try {

			mySvc.storeResources(resources);
			fail();

		} catch (JobExecutionFailedException e) {

			// Validate
			assertThat(e.getMessage(), containsString("no resource with this ID exists and clients may only assign IDs"));

		}


	}

}
