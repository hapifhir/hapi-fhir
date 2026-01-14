package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientCompartmentEnforcingInterceptorTest extends BaseResourceProviderR4Test {

	public static final int ALTERNATE_DEFAULT_ID = -1;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ISearchParamExtractor myRequestPartitionHelperSvc;
	@Autowired
	private PatientCompartmentEnforcingInterceptor mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		ForceOffsetSearchModeInterceptor forceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();
		PatientIdPartitionInterceptor patientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings);

		registerInterceptor(patientIdPartitionInterceptor);
		registerInterceptor(forceOffsetSearchModeInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myPartitionSettings.setPartitioningEnabled(false);
		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setUnnamedPartitionMode(defaultPartitionSettings.isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(defaultPartitionSettings.getDefaultPartitionId());
		myPartitionSettings.setAllowReferencesAcrossPartitions(defaultPartitionSettings.getAllowReferencesAcrossPartitions());

		myStorageSettings.setMassIngestionMode(false);
	}

	@ParameterizedTest
	@CsvSource({
		"true,  true",
		"true,  false",
		"false, true",
		"false, false"})
	public void testUpdateResource_whenCrossingPatientCompartment_throws(boolean theMassIngestionEnabled, boolean theUseNewInterceptor) {
		registerInterceptor(theUseNewInterceptor);
		myStorageSettings.setMassIngestionMode(theMassIngestionEnabled);
		createPatientA();

		// Create a second resource that lands in the same partition as Patient/A
		int patientAPartition = Math.abs("A".hashCode() % 15000);
		int count = 0;
		String otherId;
		do {
			otherId = "A" + ++count;
		} while (patientAPartition != Math.abs(otherId.hashCode() % 15000));
		Patient patient = new Patient();
		patient.setId("Patient/" + otherId);
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());

		Observation obs = new Observation();
		obs.setId("O");
		obs.getSubject().setReference("Patient/A");
		myObservationDao.update(obs, new SystemRequestDetails()).getId().getIdPart();

		// try updating observation's patient, which would cross partition boundaries
		obs.getSubject().setReference("Patient/" + otherId);

		assertThatThrownBy(() -> myObservationDao.update(obs, new SystemRequestDetails()))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("HAPI-2476: Resource compartment changed. Was a referenced Patient changed?");
	}

	@ParameterizedTest
	@CsvSource({
		"true,  true",
		"true,  false",
		"false, true",
		"false, false"})
	public void testUpdateResource_whenNotCrossingPatientCompartment_allows(boolean theMassIngestionEnabled, boolean theUseNewInterceptor) {
		registerInterceptor(theUseNewInterceptor);
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

	@SuppressWarnings("removal")
	private void registerInterceptor(boolean theUseNewInterceptor) {
		if (theUseNewInterceptor) {
			registerInterceptor(mySvc);
		} else {
			PatientCompartmentEnforcingInterceptor interceptor = new PatientCompartmentEnforcingInterceptor(getFhirContext(), myRequestPartitionHelperSvc);
			registerInterceptor(interceptor);
		}
	}

	private void createPatientA() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());
	}

}
