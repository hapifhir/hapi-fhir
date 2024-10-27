package ca.uhn.fhir.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.SimplePartitionTestHelper;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PreviousVersionReaderPartitionedTest extends BaseJpaR5Test {
	PreviousVersionReader<Patient> mySvc;
	SystemRequestDetails mySrd;
	@Autowired
	DaoRegistry myDaoRegistry;
	SimplePartitionTestHelper mySimplePartitionTestHelper;

	@BeforeEach
	public void before() throws Exception {
		super.before();

		mySimplePartitionTestHelper = new SimplePartitionTestHelper(myPartitionSettings, myPartitionConfigSvc, myInterceptorRegistry);
		mySimplePartitionTestHelper.beforeEach(null);

		mySvc = new PreviousVersionReader<>(myPatientDao);
		mySrd = new SystemRequestDetails();
		RequestPartitionId part1 = RequestPartitionId.fromPartitionId(SimplePartitionTestHelper.TEST_PARTITION_ID);
		mySrd.setRequestPartitionId(part1);
	}

	@AfterEach
	public void after() throws Exception {
		mySimplePartitionTestHelper.afterEach(null);
	}

	@Test
	void readPreviousVersion() {
		// setup
		Patient patient = createMale();
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(patient, mySrd);
		assertEquals(Enumerations.AdministrativeGender.FEMALE, myPatientDao.read(patient.getIdElement(), mySrd).getGenderElement().getValue());

		// execute
		Optional<Patient> oPreviousPatient = mySvc.readPreviousVersion(patient);

		// verify
		assertThat(oPreviousPatient).isPresent();
		Patient previousPatient = oPreviousPatient.get();
		assertEquals(Enumerations.AdministrativeGender.MALE, previousPatient.getGenderElement().getValue());
	}

	private Patient createMale() {
		Patient male = new Patient();
		male.setGender(Enumerations.AdministrativeGender.MALE);
		return (Patient) myPatientDao.create(male, mySrd).getResource();
	}

	@Test
	void noPrevious() {
		// setup
		Patient patient = createMale();

		// execute
		Optional<Patient> oPreviousPatient = mySvc.readPreviousVersion(patient);

		// verify
		assertFalse(oPreviousPatient.isPresent());
	}

	@Test
	void currentDeleted() {
		// setup
		Patient patient = createMale();
		IdType patientId = patient.getIdElement().toVersionless();
		myPatientDao.delete(patientId, mySrd);

		Patient currentDeletedVersion = myPatientDao.read(patientId, mySrd, true);

		// execute
		Optional<Patient> oPreviousPatient = mySvc.readPreviousVersion(currentDeletedVersion);

		// verify
		assertThat(oPreviousPatient).isPresent();
		Patient previousPatient = oPreviousPatient.get();
		assertEquals(Enumerations.AdministrativeGender.MALE, previousPatient.getGenderElement().getValue());
	}

	@Test
	void previousDeleted() {
		// setup
		Patient latestUndeletedVersion = setupPreviousDeletedResource();

		// execute
		Optional<Patient> oDeletedPatient = mySvc.readPreviousVersion(latestUndeletedVersion);
		assertFalse(oDeletedPatient.isPresent());
	}

	@Test
	void previousDeletedDeletedOk() {
		// setup
		Patient latestUndeletedVersion = setupPreviousDeletedResource();

		// execute
		Optional<Patient> oPreviousPatient = mySvc.readPreviousVersion(latestUndeletedVersion, true);

		// verify
		assertThat(oPreviousPatient).isPresent();
		Patient previousPatient = oPreviousPatient.get();
		assertTrue(previousPatient.isDeleted());
	}

	@Nonnull
	private Patient setupPreviousDeletedResource() {
		Patient patient = createMale();
		assertEquals(1L, patient.getIdElement().getVersionIdPartAsLong());
		IdType patientId = patient.getIdElement().toVersionless();
		myPatientDao.delete(patientId, mySrd);

		Patient currentDeletedVersion = myPatientDao.read(patientId, mySrd, true);
		assertEquals(2L, currentDeletedVersion.getIdElement().getVersionIdPartAsLong());

		currentDeletedVersion.setGender(Enumerations.AdministrativeGender.FEMALE);
		currentDeletedVersion.setId(currentDeletedVersion.getIdElement().toVersionless());
		myPatientDao.update(currentDeletedVersion, mySrd);

		Patient latestUndeletedVersion = myPatientDao.read(patientId, mySrd);
		assertEquals(3L, latestUndeletedVersion.getIdElement().getVersionIdPartAsLong());

		assertFalse(latestUndeletedVersion.isDeleted());
		assertEquals(Enumerations.AdministrativeGender.FEMALE, latestUndeletedVersion.getGenderElement().getValue());
		return latestUndeletedVersion;
	}

}
