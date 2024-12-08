package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMLinkResults;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.interceptor.MdmReadVirtualizationInterceptor;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmReadVirtualizationInterceptorTest extends BaseMdmR4Test {

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;
	@Autowired
	private MdmReadVirtualizationInterceptor<JpaPid> myInterceptor;
	@Autowired
	private MdmLinkHelper myLinkHelper;

	private IIdType mySourcePatientA0Id;
	private IIdType myGoldenResourcePatientAId;
	private IIdType mySourcePatientA1Id;
	private IIdType mySourcePatientA2Id;
	private IIdType myObservationReferencingSourcePatientA0Id;
	private IIdType myObservationReferencingSourcePatientA1Id;
	private IIdType myObservationReferencingSourcePatientA2Id;
	private IIdType myObservationReferencingGoldenPatientAId;
	private IIdType mySourcePatientB0Id;
	private IIdType myGoldenResourcePatientBId;
	private IIdType myObservationReferencingSourcePatientB0Id;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
	}

	@Override
	@BeforeEach
	public void after() throws IOException {
		super.after();
		myInterceptorRegistry.unregisterInterceptor(myInterceptor);
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
	}

	@Test
	public void testEverything() {
		// Setup
		createTestPatientsAndObservations(true);
		registerVirtualizationInterceptor();
		when(mySrd.getResourceName()).thenReturn("Patient");
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);

		// Test
		PatientEverythingParameters params = new PatientEverythingParameters();
		IBundleProvider outcome = myPatientDao.patientInstanceEverything(null, mySrd, params, mySourcePatientA1Id);

		// Verify
		Map<String, IBaseResource> resources = toResourceIdValueMap(outcome);
		List<String> ids = new ArrayList<>(resources.keySet());
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA1Id.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue()
		);
		assertEquals(mySourcePatientA1Id.getValue(), getObservation(resources, myObservationReferencingGoldenPatientAId).getSubject().getReference());
		assertEquals(mySourcePatientA1Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA0Id).getSubject().getReference());
		assertEquals(mySourcePatientA1Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA1Id).getSubject().getReference());
		assertEquals(mySourcePatientA1Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA2Id).getSubject().getReference());
	}

	/**
	 * If we fetch an observation referencing a source patient, that reference should
	 * be remapped to the equivalent golden resource ID
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testRead_ObservationReferencingSourcePatient(boolean theUseClientAssignedIds) {
		// Setup
		createTestPatientsAndObservations(theUseClientAssignedIds);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);

		// Test
		Observation obs = myObservationDao.read(myObservationReferencingSourcePatientA0Id, mySrd);

		// Verify
		assertEquals(mySourcePatientA0Id.getValue(), obs.getSubject().getReference());
	}

	/**
	 * If we fetch an observation referencing a golden resource, we should just
	 * leave it as is
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testRead_ObservationReferencingGoldenPatient(boolean theUseClientAssignedIds) {
		// Setup
		createTestPatientsAndObservations(theUseClientAssignedIds);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.READ);

		// Test
		Observation obs = myObservationDao.read(myObservationReferencingGoldenPatientAId, mySrd);

		// Verify
		assertEquals(myGoldenResourcePatientAId.getValue(), obs.getSubject().getReference());
	}

	/**
	 * If we search for all patients, only the golden resource ones should be returned
	 */
	@Test
	public void testSearch_Patient_FetchAll() {
		// Setup
		createTestPatientsAndObservations(false);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		// Test
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA0Id.getValue(),
			mySourcePatientA1Id.getValue(),
			mySourcePatientA2Id.getValue(),
			mySourcePatientB0Id.getValue(),
			myGoldenResourcePatientAId.getValue(),
			myGoldenResourcePatientBId.getValue());
	}

	/**
	 * If we search for patients but only include source patients, these should be remapped to
	 * golden patients
	 */
	@Test
	public void testSearch_Patient_FetchOnlySource() {
		// Setup
		createTestPatientsAndObservations(false);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(IAnyResource.SP_RES_ID, new TokenOrListParam()
			.add(mySourcePatientA0Id.getValue())
			.add(mySourcePatientB0Id.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(mySourcePatientA0Id.getValue(), mySourcePatientB0Id.getValue());
	}

	/**
	 * If we search for a patient by _id, and we _revinclude things pointing to the patient, we
	 * should also return things pointing to linked patients and update the references to
	 * point to that patient. The linked patients should not be included.
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_Patient_FetchSourcePatient_AlsoRevIncludeDependentResources(boolean theUseClientAssginedId) {
		// Setup
		createTestPatientsAndObservations(theUseClientAssginedId);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(IAnyResource.SP_RES_ID, new TokenParam(mySourcePatientA2Id.getValue()));
		params.addRevInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA2Id.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue()
		);
		Map<String, IBaseResource> resources = toResourceIdValueMap(outcome);
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingGoldenPatientAId).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA0Id).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA1Id).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA2Id).getSubject().getReference());
	}

	@Test
	public void testSearch_Observation_SpecificSourcePatient() {
		// Setup
		createTestPatientsAndObservations(true);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(mySourcePatientA2Id.getValue()));
		params.addInclude(Observation.INCLUDE_PATIENT);
		IBundleProvider outcome = myObservationDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA2Id.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue()
		);
		Map<String, IBaseResource> resources = toResourceIdValueMap(outcome);
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingGoldenPatientAId).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA0Id).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA1Id).getSubject().getReference());
		assertEquals(mySourcePatientA2Id.getValue(), getObservation(resources, myObservationReferencingSourcePatientA2Id).getSubject().getReference());
	}

	@Test
	public void testSearch_Observation_NonRelativeReferencesAreLeftAlone() {
		// Setup
		createTestPatients(true);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		IIdType obsId = createObservation(withSubject(mySourcePatientA0Id), withObservationCode("http://foo", "code0")).toUnqualifiedVersionless();
		Observation obs = myObservationDao.read(obsId, mySrd);
		Encounter encounter = new Encounter();
		encounter.setId("1");
		encounter.setStatus(Encounter.EncounterStatus.ARRIVED);
		obs.getContained().add(encounter);

		// Add 2 non-relative references. The interceptor should just ignore these
		obs.setEncounter(new Reference("#1"));
		obs.addBasedOn().setIdentifier(new Identifier().setValue("123"));
		myObservationDao.update(obs, mySrd);

		logAllResourceLinks();

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(mySourcePatientA2Id.getValue()));
		params.addInclude(Observation.INCLUDE_PATIENT);
		IBundleProvider outcome = myObservationDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA2Id.getValue(),
			obsId.getValue()
		);

	}

	/**
	 * The CQL evaluator uses a shared RequestDetails across multiple different requests - Make sure
	 * we don't return the wrong cached results
	 */
	@Test
	public void testSearch_RequestDetailsIsReused() {
		// Setup
		createTestPatientsAndObservations(true);
		registerVirtualizationInterceptor();
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);

		// Test
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		// Search for patients
		requestDetails.setResourceName("Patient");
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(IAnyResource.SP_RES_ID, new TokenParam(mySourcePatientA2Id.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA2Id.getValue()
		);

		// Search for Observations
		requestDetails.setResourceName("Observation");
		params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(mySourcePatientA2Id.getValue()));
		params.addInclude(Observation.INCLUDE_PATIENT);
		outcome = myObservationDao.search(params, mySrd);

		// Verify
		ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			mySourcePatientA2Id.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue()
		);

	}

	private static Observation getObservation(Map<String, IBaseResource> resources, IIdType observationReferencingGoldenPatientAId) {
		Observation retVal = (Observation) resources.get(observationReferencingGoldenPatientAId.getValue());
		if (retVal == null) {
			fail("Could not find '" + observationReferencingGoldenPatientAId.getValue() + "' - Valid IDs: " + new TreeSet<>(resources.keySet()));
		}
		return retVal;
	}

	private void registerVirtualizationInterceptor() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}

	private void createTestPatientsAndObservations(boolean theUseClientAssignedIds) {
		createTestPatients(theUseClientAssignedIds);

		myObservationReferencingSourcePatientA0Id = createObservation(theUseClientAssignedIds, mySourcePatientA0Id, "code0");
		myObservationReferencingSourcePatientA1Id = createObservation(theUseClientAssignedIds, mySourcePatientA1Id, "code1");
		myObservationReferencingSourcePatientA2Id = createObservation(theUseClientAssignedIds, mySourcePatientA2Id, "code2");
		myObservationReferencingGoldenPatientAId = createObservation(theUseClientAssignedIds, myGoldenResourcePatientAId, "code2");
		myObservationReferencingSourcePatientB0Id = createObservation(theUseClientAssignedIds, mySourcePatientB0Id, "code0");

		logAllResources();
	}

	private void createTestPatients(boolean theUseClientAssignedIds) {
		String inputState;
		if (theUseClientAssignedIds) {
			inputState = """
							GPA, AUTO, MATCH, PA0
							GPA, AUTO, MATCH, PA1
							GPA, AUTO, MATCH, PA2
							GPB, AUTO, MATCH, PB0
				""";
		} else {
			inputState = """
							SERVER_ASSIGNED_GA, AUTO, MATCH, SERVER_ASSIGNED_PA0
							SERVER_ASSIGNED_GA, AUTO, MATCH, SERVER_ASSIGNED_PA1
							SERVER_ASSIGNED_GA, AUTO, MATCH, SERVER_ASSIGNED_PA2
							SERVER_ASSIGNED_GB, AUTO, MATCH, SERVER_ASSIGNED_PB0
				""";
		}
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		MDMLinkResults outcome = myLinkHelper.setup(state);

		mySourcePatientA0Id = toId(state, outcome.getResults().get(0).getSourcePersistenceId());
		myGoldenResourcePatientAId = toId(state, outcome.getResults().get(0).getGoldenResourcePersistenceId());
		mySourcePatientA1Id = toId(state, outcome.getResults().get(1).getSourcePersistenceId());
		mySourcePatientA2Id = toId(state, outcome.getResults().get(2).getSourcePersistenceId());
		mySourcePatientB0Id = toId(state, outcome.getResults().get(3).getSourcePersistenceId());
		myGoldenResourcePatientBId = toId(state, outcome.getResults().get(3).getGoldenResourcePersistenceId());
		assertEquals(4, logAllMdmLinks());
		assertEquals(!theUseClientAssignedIds, mySourcePatientA0Id.isIdPartValidLong());
		assertEquals(!theUseClientAssignedIds, myGoldenResourcePatientAId.isIdPartValidLong());
	}

	@Nonnull
	private static IdType toId(MDMState<Patient, JpaPid> state, JpaPid persistentId) {
		return new IdType("Patient/" + state.getForcedId(persistentId));
	}

	private IIdType createObservation(boolean theUseClientAssignedIds, IIdType patientId, String code) {
		String resourceId = theUseClientAssignedIds ? UUID.randomUUID().toString() : null;
		return createObservation(withIdOrNull(resourceId), withSubject(patientId), withObservationCode("http://foo", code)).toUnqualifiedVersionless();
	}

}
