package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.jobs.imprt.ConsumeFilesStep;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.r4.BasePartitioningR4Test;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ConsumeFilesStepR4Test extends BasePartitioningR4Test {

	@Autowired
	private ConsumeFilesStep mySvc;
	private final RequestPartitionId myRequestPartitionId = RequestPartitionId.fromPartitionIdAndName(1, "PART-1");

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(false);
		myStorageSettings.setInlineResourceTextBelowSize(10000);
	}

	@AfterEach
	@Override
	public void after() {
		super.after();
		myStorageSettings.setInlineResourceTextBelowSize(new JpaStorageSettings().getInlineResourceTextBelowSize());
	}

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
		mySvc.storeResources(resources, null);

		// Validate

		assertEquals(7, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread(), myCaptureQueriesListener.getInsertQueriesForCurrentThread().stream().map(t->t.getSql(true, false)).collect(Collectors.joining("\n")));
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		patient = myPatientDao.read(new IdType("Patient/A"));
		assertTrue(patient.getActive());
		patient = myPatientDao.read(new IdType("Patient/B"));
		assertFalse(patient.getActive());

	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testAlreadyExisting_WithChanges(boolean partitionEnabled) {
		// Setup
		if (partitionEnabled) {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionSettings.setIncludePartitionInSearchHashes(true);
			addCreatePartition(1);
			addCreatePartition(1);
		}
		Patient patient = new Patient();
		patient.setId("A");
		patient.setActive(false);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("B");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);


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
		if (partitionEnabled) {
			addReadPartition(1);
			addReadPartition(1);
			mySvc.storeResources(resources, myRequestPartitionId);
		} else {
			mySvc.storeResources(resources, null);
		}

		// Validate

		if (partitionEnabled) {
			assertEquals(8, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		} else {
			assertEquals(7, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		}
		assertEquals(2, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(4, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertTrue(patient.getActive());
		patient = myPatientDao.read(new IdType("Patient/B"), mySrd);
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
		mySvc.storeResources(resources, null);

		// Validate

		assertEquals(1, myCaptureQueriesListener.logSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false),
			either(containsString("forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='B' and (forcedid0_.PARTITION_ID is null) or forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='A' and (forcedid0_.PARTITION_ID is null)"))
				.or(containsString("forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='A' and (forcedid0_.PARTITION_ID is null) or forcedid0_.RESOURCE_TYPE='Patient' and forcedid0_.FORCED_ID='B' and (forcedid0_.PARTITION_ID is null)")));
		assertEquals(52, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
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

			mySvc.storeResources(resources, null);
			fail();

		} catch (JobExecutionFailedException e) {

			// Validate
			assertThat(e.getMessage(), containsString("no resource with this ID exists and clients may only assign IDs"));

		}


	}

}
