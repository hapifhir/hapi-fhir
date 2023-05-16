package ca.uhn.fhir.storage;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreviousVersionReaderTest extends BaseJpaR5Test {
	PreviousVersionReader<Patient> mySvc;
	SystemRequestDetails mySrd = new SystemRequestDetails();
	@Autowired
	DaoRegistry myDaoRegistry;

	@BeforeEach
	public void before() throws Exception {
		super.before();
		mySvc = new PreviousVersionReader<>(myDaoRegistry, Patient.class);
	}

	@Test
	void readPreviousVersion() {
		// setup
		Patient male = new Patient();
		male.setGender(Enumerations.AdministrativeGender.MALE);
		Patient patient = (Patient) myPatientDao.create(male, mySrd).getResource();
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(patient, mySrd);
		assertEquals(Enumerations.AdministrativeGender.FEMALE, myPatientDao.read(patient.getIdElement(), mySrd).getGenderElement().getValue());

		// execute
		Optional<Patient> oPreviousPatient = mySvc.readPreviousVersion(patient);

		// verify
		assertTrue(oPreviousPatient.isPresent());
		Patient previousPatient = oPreviousPatient.get();
		assertEquals(Enumerations.AdministrativeGender.MALE, previousPatient.getGenderElement().getValue());
	}
}
