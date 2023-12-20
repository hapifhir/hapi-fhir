package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientCompartmentEnforcingInterceptorTest extends BaseJpaR4Test {

	public static final int ALTERNATE_DEFAULT_ID = -1;
	private PatientIdPartitionInterceptor mySvc;
	private ForceOffsetSearchModeInterceptor myForceOffsetSearchModeInterceptor;
	private PatientCompartmentEnforcingInterceptor myUpdateCrossPartitionInterceptor;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor, myPartitionSettings);
		myForceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();
		myUpdateCrossPartitionInterceptor = new PatientCompartmentEnforcingInterceptor(
			myFhirContext, mySearchParamExtractor);

		myInterceptorRegistry.registerInterceptor(mySvc);
		myInterceptorRegistry.registerInterceptor(myForceOffsetSearchModeInterceptor);
		myInterceptorRegistry.registerInterceptor(myUpdateCrossPartitionInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(mySvc);
		myInterceptorRegistry.unregisterInterceptor(myForceOffsetSearchModeInterceptor);
		myInterceptorRegistry.unregisterInterceptor(myUpdateCrossPartitionInterceptor);

		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
	}


	@Test
	public void testUpdateResource_whenCrossingPatientCompartment_throws() {
		createPatientA();
		createPatientB();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		myObservationDao.create(obs, new SystemRequestDetails());

		// try updating observation's patient, which would cross partition boundaries
		obs.getSubject().setReference("Patient/B");

		InternalErrorException thrown = assertThrows(InternalErrorException.class, () -> myObservationDao.update(obs, new SystemRequestDetails()));
		assertEquals("HAPI-2476: Resource compartment changed. Was a referenced Patient changed?", thrown.getMessage());
	}

	@Test
	public void testUpdateResource_whenNotCrossingPatientCompartment_allows() {
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		myObservationDao.create(obs, new SystemRequestDetails());

		obs.getNote().add(new Annotation().setText("some text"));
		obs.setStatus(Observation.ObservationStatus.CORRECTED);

		myObservationDao.update(obs, new SystemRequestDetails());
	}

	private void createPatientA() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());
	}

	private void createPatientB() {
		Patient patient = new Patient();
		patient.setId("Patient/B");
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());
	}

}
