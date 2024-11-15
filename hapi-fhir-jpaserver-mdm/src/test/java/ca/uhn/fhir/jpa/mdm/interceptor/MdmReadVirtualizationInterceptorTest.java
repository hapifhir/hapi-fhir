package ca.uhn.fhir.jpa.mdm.interceptor;

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
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.SEARCH_TYPE);
	}

	@Override
	@BeforeEach
	public void after() throws IOException {
		super.after();
		myInterceptorRegistry.unregisterInterceptor(myInterceptor);
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
	}

	/**
	 * If we fetch an observation referencing a source patient, that reference should
	 * be remapped to the equivalent golden resource ID
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testRead_ObservationReferencingSourcePatient(boolean theUseClientAssignedIds) {
		// Setup
		createTestData(theUseClientAssignedIds);
		registerVirtualizationInterceptor();

		// Test
		Observation obs = myObservationDao.read(myObservationReferencingSourcePatientA0Id, mySrd);

		// Verify
		assertEquals(myGoldenResourcePatientAId.getValue(), obs.getSubject().getReference());
	}

	/**
	 * If we fetch an observation referencing a golden resource, we should just
	 * leave it as is
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testRead_ObservationReferencingGoldenPatient(boolean theUseClientAssignedIds) {
		// Setup
		createTestData(theUseClientAssignedIds);
		registerVirtualizationInterceptor();

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
		createTestData(false);
		registerVirtualizationInterceptor();

		// Test
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(myGoldenResourcePatientAId.getValue(), myGoldenResourcePatientBId.getValue());
	}

	/**
	 * If we search for patients but only include source patients, these should be remapped to
	 * golden patients
	 */
	@Test
	public void testSearch_Patient_FetchOnlySource() {
		// Setup
		createTestData(false);
		registerVirtualizationInterceptor();

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(IAnyResource.SP_RES_ID, new TokenOrListParam()
			.add(mySourcePatientA0Id.getValue())
			.add(mySourcePatientB0Id.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(myGoldenResourcePatientAId.getValue(), myGoldenResourcePatientBId.getValue());
	}

	/**
	 * If we search for all patients and _revinclude things that point to them,
	 * only the golden resource ones should be returned
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_Patient_FetchAll_AlsoRevIncludeDependentResources(boolean theUseClientAssginedId) {
		// Setup
		createTestData(theUseClientAssginedId);
		registerVirtualizationInterceptor();

		// Test
		SearchParameterMap params = new SearchParameterMap();
		params.addRevInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			myGoldenResourcePatientAId.getValue(),
			myGoldenResourcePatientBId.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue(),
			myObservationReferencingSourcePatientB0Id.getValue()
		);
		Map<String, IBaseResource> resources = toResourceIdValueMap(outcome);
		Observation obs;
		obs = (Observation) resources.get(myObservationReferencingGoldenPatientAId.getValue());
		assertEquals(myGoldenResourcePatientAId.getValue(), obs.getSubject().getReference());
		obs = (Observation) resources.get(myObservationReferencingSourcePatientB0Id.getValue());
		assertEquals(myGoldenResourcePatientBId.getValue(), obs.getSubject().getReference());
	}

	@Test
	public void testSearch_Observation_SpecificSourcePatient() {
		// Setup
		createTestData(false);
		registerVirtualizationInterceptor();

		// Test
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(mySourcePatientA2Id.getValue()));
		params.addInclude(Observation.INCLUDE_PATIENT);
		IBundleProvider outcome = myObservationDao.search(params, mySrd);

		// Verify
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).asList().containsExactlyInAnyOrder(
			myGoldenResourcePatientAId.getValue(),
			myObservationReferencingGoldenPatientAId.getValue(),
			myObservationReferencingSourcePatientA0Id.getValue(),
			myObservationReferencingSourcePatientA1Id.getValue(),
			myObservationReferencingSourcePatientA2Id.getValue()
		);
		Map<String, IBaseResource> resources = toResourceIdValueMap(outcome);
		Observation obs = (Observation) resources.get(myObservationReferencingGoldenPatientAId.getValue());
		assertEquals(myGoldenResourcePatientAId.getValue(), obs.getSubject().getReference());
	}

	private void registerVirtualizationInterceptor() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}

	private void createTestData(boolean theUseClientAssignedIds) {
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

		myObservationReferencingSourcePatientA0Id = createObservation(theUseClientAssignedIds, mySourcePatientA0Id, "code0");
		myObservationReferencingSourcePatientA1Id = createObservation(theUseClientAssignedIds, mySourcePatientA1Id, "code1");
		myObservationReferencingSourcePatientA2Id = createObservation(theUseClientAssignedIds, mySourcePatientA2Id, "code2");
		myObservationReferencingGoldenPatientAId = createObservation(theUseClientAssignedIds, myGoldenResourcePatientAId, "code2");
		myObservationReferencingSourcePatientB0Id = createObservation(theUseClientAssignedIds, mySourcePatientB0Id, "code0");

		assertEquals(!theUseClientAssignedIds, mySourcePatientA0Id.isIdPartValidLong());
		assertEquals(!theUseClientAssignedIds, myGoldenResourcePatientAId.isIdPartValidLong());

		logAllResources();
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
