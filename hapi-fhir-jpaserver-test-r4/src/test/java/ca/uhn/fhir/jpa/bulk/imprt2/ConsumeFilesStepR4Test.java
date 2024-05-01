package ca.uhn.fhir.jpa.bulk.imprt2;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.jobs.imprt.ConsumeFilesStep;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;

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
	}

	@AfterEach
	@Override
	public void after() {
		super.after();
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

		assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(7);
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).as(myCaptureQueriesListener.getInsertQueriesForCurrentThread().stream().map(t -> t.getSql(true, false)).collect(Collectors.joining("\n"))).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

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
			assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(8);
		} else {
			assertThat(myCaptureQueriesListener.countSelectQueriesForCurrentThread()).isEqualTo(7);
		}
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(2);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(4);
		assertThat(myCaptureQueriesListener.countDeleteQueries()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);

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

		assertThat(myCaptureQueriesListener.logSelectQueries()).hasSize(1);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false),
			either(containsString("rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='B' and rt1_0.PARTITION_ID is null or rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A' and rt1_0.PARTITION_ID is null"))
				.or(containsString("rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A' and rt1_0.PARTITION_ID is null or rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='B' and rt1_0.PARTITION_ID is null")));
		assertThat(myCaptureQueriesListener.countInsertQueriesForCurrentThread()).isEqualTo(52);
		assertThat(myCaptureQueriesListener.countUpdateQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countDeleteQueriesForCurrentThread()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.countCommits()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.countRollbacks()).isEqualTo(0);


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
			fail("");

		} catch (JobExecutionFailedException e) {

			// Validate
			assertThat(e.getMessage()).contains("no resource with this ID exists and clients may only assign IDs");

		}


	}

}
