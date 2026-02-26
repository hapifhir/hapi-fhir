package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.FhirPatchBuilder;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PatientCompartmentEnforcingInterceptorTest extends BaseResourceProviderR4Test {

	public static final int ALTERNATE_DEFAULT_ID = -1;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	private PatientCompartmentEnforcingInterceptor mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		ForceOffsetSearchModeInterceptor forceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();
		PatientIdPartitionInterceptor patientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);

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
		Observation updatedObs = new Observation();
		updatedObs.setId("O");
		updatedObs.getSubject().setReference("Patient/" + otherId);

		assertThatThrownBy(() -> myObservationDao.update(updatedObs, new SystemRequestDetails()))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("HAPI-2476: Resource compartment for Observation/O changed. Was a referenced Patient changed?");
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

	/**
	 * Updating a member of a group should not cause any issues
	 */
	@Test
	public void testCompartmentEnforcement_GroupUpdate() {
		registerInterceptor(mySvc);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		logAllResources();

		Group group = new Group();
		group.setId("G");
		group.addMember().setEntity(new Reference("Patient/A"));
		doUpdateResource(group);

		// Test
		group = new Group();
		group.setId("G");
		group.addMember().setEntity(new Reference("Patient/B"));
		group.addMember().setEntity(new Reference("Patient/A"));
		doUpdateResource(group);

		// Verify
		Group actual = myGroupDao.read(new IdType("G"), mySrd);
		assertEquals(2, actual.getMember().size());
		assertEquals("Patient/B", actual.getMember().get(0).getEntity().getReference());
		assertEquals("Patient/A", actual.getMember().get(1).getEntity().getReference());
	}

	/**
	 * Updating an encounter's patient should be blocked
	 */
	@Test
	public void testCompartmentEnforcement_EncounterUpdate() {
		registerInterceptor(mySvc);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());
		createEncounter(withId("E"), withSubject("Patient/A"), withIdentifier("http://foo", "123"));

		// Test
		FhirPatchBuilder pb = new FhirPatchBuilder(myFhirContext);
		pb.replace()
			.path("Encounter.subject")
			.value(new Reference("Patient/B"));
		IBaseParameters patch = pb.build();
		assertThatThrownBy(() -> myEncounterDao.patch(new IdType("Encounter/E"), null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, newSrd()))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("HAPI-2476: Resource compartment for Encounter/E changed. Was a referenced Patient changed?");

		// Verify
		Encounter actual = myEncounterDao.read(new IdType("E"), mySrd);
		assertEquals("Patient/A", actual.getSubject().getReference());
	}

	@Test
	public void testDeleteAndRecreateResource_InPatientCompartmentSucceeds() {
		// Setup
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("O"), withSubject("Patient/A"));

		// Test - Delete the observation
		myCaptureQueriesListener.clear();
		DaoMethodOutcome deleteOutcome = myObservationDao.delete(new IdType("Observation/O"), newSrd());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertGone("Observation/O");
		long deletedVersion = deleteOutcome.getId().getVersionIdPartAsLong();

		// Test - Re-create the same observation
		myCaptureQueriesListener.clear();
		createObservation(withId("O"), withSubject("Patient/A"));
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();

		// Verify - Assert the observation exists and version is incremented
		Observation recreated = myObservationDao.read(new IdType("Observation/O"), mySrd);
		assertThat(recreated).isNotNull();
		assertThat(recreated.getSubject().getReference()).isEqualTo("Patient/A");

		assertThat(recreated.getIdElement().getVersionIdPartAsLong()).isEqualTo(deletedVersion + 2);
	}

	@Test
	public void testDeleteAndRecreateResource_InDifferentCompartmentFail() {
		registerInterceptor(mySvc);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		// Setup
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("O"), withSubject("Patient/A"));


		myObservationDao.delete(new IdType("Observation/O"), newSrd());
		assertGone("Observation/O");

		// Create a second patient with second resource that lands in the same partition as Patient/A
		int patientAPartition = Math.abs("A".hashCode() % 15000);
		int count = 0;
		String otherId;
		do {
			otherId = "A" + ++count;
		} while (patientAPartition != Math.abs(otherId.hashCode() % 15000));

		Patient patient = new Patient();
		patient.setId("Patient/" + otherId);
		patient.setActive(true);
		doUpdateResource(patient);

		try {
			createObservation(withId("O"), withSubject("Patient/" + otherId));
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("HAPI-2476: Resource compartment for Observation/O changed. Was a referenced Patient changed?");
		}
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
