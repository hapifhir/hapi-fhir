package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientCompartmentEnforcingInterceptorTest extends BaseResourceProviderR4Test {

	public static final int ALTERNATE_DEFAULT_ID = -1;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	private ForceOffsetSearchModeInterceptor myForceOffsetSearchModeInterceptor;
	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;
	private PatientCompartmentEnforcingInterceptor mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myForceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings);
		mySvc = new PatientCompartmentEnforcingInterceptor(getFhirContext(), mySearchParamExtractor);

		myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		myInterceptorRegistry.registerInterceptor(myForceOffsetSearchModeInterceptor);
		myInterceptorRegistry.registerInterceptor(mySvc);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		myInterceptorRegistry.unregisterInterceptor(myForceOffsetSearchModeInterceptor);
		myInterceptorRegistry.unregisterInterceptor(mySvc);

		myPartitionSettings.setPartitioningEnabled(false);
		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setUnnamedPartitionMode(defaultPartitionSettings.isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(defaultPartitionSettings.getDefaultPartitionId());
		myPartitionSettings.setAllowReferencesAcrossPartitions(defaultPartitionSettings.getAllowReferencesAcrossPartitions());

		myStorageSettings.setMassIngestionMode(false);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testUpdateResource_whenCrossingPatientCompartment_throws(boolean theMassIngestionEnabled) {
		myStorageSettings.setMassIngestionMode(theMassIngestionEnabled);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
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

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testUpdateResource_whenNotCrossingPatientCompartment_allows(boolean theMassIngestionEnabled) {
		myStorageSettings.setMassIngestionMode(theMassIngestionEnabled);
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		myObservationDao.create(obs, new SystemRequestDetails());

		obs.getNote().add(new Annotation().setText("some text"));
		obs.setStatus(Observation.ObservationStatus.CORRECTED);

		DaoMethodOutcome outcome = myObservationDao.update(obs, new SystemRequestDetails());
		assertEquals("Patient/A", ((Observation) outcome.getResource()).getSubject().getReference());
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
