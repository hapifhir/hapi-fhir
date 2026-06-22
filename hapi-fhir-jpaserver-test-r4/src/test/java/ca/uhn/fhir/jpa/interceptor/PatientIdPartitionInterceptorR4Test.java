package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.cache.IResourceIdentifierCacheSvc;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.TransactionPrePartitionResponse;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceEnabledByTypeInterceptor;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirPatchBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.MultimapCollector;
import com.google.common.base.Charsets;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onAllThreads;
import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Import({PatientIdPartitionInterceptorR4Test.TestConfig.class, TestDaoSearch.Config.class})
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PatientIdPartitionInterceptorR4Test extends BaseResourceProviderR4Test {
	// Captures the contents between parentheses in a "PARTITION_ID IN (...)" SQL clause
	private static final Pattern PARTITION_ID_IN_PATTERN = Pattern.compile("PARTITION_ID IN \\(([^)]+)\\)");
	public static final int ALTERNATE_DEFAULT_ID = -1;
	public static final int PATIENT_A_COMPARTMENT_ID = 65;

	@Autowired
	private HapiTransactionService myTransactionService;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	TestDaoSearch myTestDaoSearch;

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IResourceIdentifierCacheSvc myResourceIdentifierCacheSvc;

	private ForceOffsetSearchModeInterceptor myForceOffsetSearchModeInterceptor;
	private PatientIdPartitionInterceptor mySvc;

	private final String oldPayerSystem = "urn:uudid:old-payer-sytem";

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myForceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();
		mySvc = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);

		myInterceptorRegistry.registerInterceptor(mySvc);
		myInterceptorRegistry.registerInterceptor(myForceOffsetSearchModeInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);

		initResourceTypeCacheFromConfig();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptor(mySvc);
		myInterceptorRegistry.unregisterInterceptor(myForceOffsetSearchModeInterceptor);

		PartitionSettings defaultSettings = new PartitionSettings();
		myPartitionSettings.setPartitioningEnabled(defaultSettings.isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(defaultSettings.isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(defaultSettings.getDefaultPartitionId());
		myPartitionSettings.setAllowReferencesAcrossPartitions(defaultSettings.getAllowReferencesAcrossPartitions());

		myTransactionService.setTransactionPropagationWhenChangingPartitions(HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
	}


	@Test
	public void testDeleteResource_Patient_InTransaction_ById() {
		// Setup
		createPatientA();
		myMemoryCacheService.invalidateAllCaches();

		// Test
		myCaptureQueriesListener.clear();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionDeleteEntry("Patient", "A");
		mySystemDao.transaction(newSrd(), bb.getBundleTyped());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true)).contains("rt1_0.PARTITION_ID='65'");
	}



	@Test
	public void testDeleteResource_InPatientCompartment() {
		// Setup
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("O"), withSubject("Patient/A"));

		// Test
		myCaptureQueriesListener.clear();
		myObservationDao.delete(new IdType("Observation/O"), newSrd());
		myCaptureQueriesListener.logSelectQueries();

		// Verify
		assertGone("Observation/O");
	}

	@Test
	public void testDeleteResource_NotInPatientCompartment() {
		createOrganization(withId("O"), withName("Org O"));

		// Test
		myCaptureQueriesListener.clear();
		myOrganizationDao.delete(new IdType("Organization/O"), newSrd());
		myCaptureQueriesListener.logSelectQueries();

		// Verify
		assertGone("Organization/O");
	}

	@Test
	void testCreateProvenance_InPatientCompartmentThroughSecondTargetReference() {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		createResource("Bundle", withId("B"), withResourcePrimitiveAttribute("type", "collection"));
		createPatient(withId("A"), withActiveTrue());

		// Test
		Provenance provenance = new Provenance();
		provenance.addTarget().setReference("Bundle/B");
		provenance.addTarget().setReference("Patient/A");
		IIdType provenanceId = myProvenanceDao.create(provenance, newSrd()).getId();

		// Verify
		assertResourceIsInPartition(PATIENT_A_COMPARTMENT_ID, provenanceId);
	}

	@Test
	void testCreateProvenance_InMultiplePatientCompartments() {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		// Test
		Provenance provenance = new Provenance();
		provenance.addTarget().setReference("Patient/A");
		provenance.addTarget().setReference("Patient/B");
		IIdType provenanceId = myProvenanceDao.create(provenance, newSrd()).getId();

		// Verify - default policy for Provenance is NON_UNIQUE_COMPARTMENT_IN_DEFAULT,
		// so multi-patient Provenance goes to default partition
		assertResourceIsInPartition(ALTERNATE_DEFAULT_ID, provenanceId);
	}

	@Test
	void testCreateProvenance_NoPatientCompartment() {
		// Setup
		createResource("Organization", withId("O"), withName("Test Org"));

		// Test
		Provenance provenance = new Provenance();
		provenance.addTarget().setReference("Organization/O");
		IIdType provenanceId = myProvenanceDao.create(provenance, newSrd()).getId();

		// Verify - Provenance with no Patient targets goes to default partition
		assertResourceIsInPartition(ALTERNATE_DEFAULT_ID, provenanceId);
	}

	@ParameterizedTest
	@CsvSource({"MANDATORY_SINGLE_COMPARTMENT", "OPTIONAL_SINGLE_COMPARTMENT", "NON_UNIQUE_COMPARTMENT_IN_DEFAULT"})
	void testCreateProvenance_InMultiplePatientCompartments_withCompartmentExtension(String thePolicy) {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		mySvc.setResourceTypePolicies(Map.of("Provenance", ResourceCompartmentStoragePolicy.parse(thePolicy)));
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		// Test
		Provenance provenance = new Provenance();
		provenance.addTarget().setReference("Patient/A");
		provenance.addTarget().setReference("Patient/B");
		provenance.addExtension()
			.setUrl("http://hapifhir.io/fhir/StructureDefinition/patient-compartment")
			.setValue(new StringType("Patient/A"));
		IIdType provenanceId = myProvenanceDao.create(provenance, newSrd()).getId();

		// Verify
		int patientBPartitionId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("B");
		assertThat(patientBPartitionId).isNotEqualTo(PATIENT_A_COMPARTMENT_ID);
		assertResourceIsInPartition(PATIENT_A_COMPARTMENT_ID, provenanceId);
	}

	@Test
	public void testCreatePatient_ClientAssignedId() {
		createPatientA();

		runInTransaction(() -> {
			ResourceTable pt = myResourceTableDao.findAll().iterator().next();
			assertEquals("A", pt.getIdDt().getIdPart());
			assertEquals(65, pt.getPartitionId().getPartitionId());
		});
	}

	/**
	 * This is an edge case where a client assigned ID has a Java hashCode equal to Integer.MIN_VALUE.
	 */
	@Test
	public void testCreatePatient_polygenelubricants() {
		Patient patient = new Patient();
		patient.setId("Patient/polygenelubricants");
		patient.setActive(true);
		DaoMethodOutcome update = myPatientDao.update(patient);

		runInTransaction(() -> {
			ResourceTable pt = myResourceTableDao.findAll().iterator().next();
			assertEquals("polygenelubricants", pt.getIdDt().getIdPart());
			assertEquals(8648, pt.getPartitionId().getPartitionId());
		});
	}

	@Test
	public void testCreatePatient_NonClientAssignedId() {
		Patient patient = new Patient();
		patient.setActive(true);
		try {
			myPatientDao.create(patient);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1321) + "Patient resource IDs must be client-assigned in patient compartment mode, or server id strategy must be UUID", e.getMessage());
		}
	}

	@Test
	public void testCreateObservation_ValidMembershipInCompartment() {
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Observation", observation.getResourceType());
			assertEquals(65, observation.getPartitionId().getPartitionId());
		});
	}

	/**
	 * Encounter.subject has a FHIRPath expression with a resolve() on it
	 */
	@Test
	public void testCreateEncounter_ValidMembershipInCompartment() {
		createPatientA();

		Encounter encounter = new Encounter();
		encounter.getSubject().setReference("Patient/A");
		Long id = myEncounterDao.create(encounter).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Encounter", observation.getResourceType());
			assertEquals(65, observation.getPartitionId().getPartitionId());
		});
	}

	/**
	 * SMILE-11985
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCreateEob_AutoPlaceholderCreationOfOtherResourceInCompartment(boolean theRegisterOtherInterceptor) {
		// Setup
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		if (theRegisterOtherInterceptor) {
			AutoCreatePlaceholderReferenceEnabledByTypeInterceptor interceptor = new AutoCreatePlaceholderReferenceEnabledByTypeInterceptor("Patient", "Coverage");
			registerInterceptor(interceptor);
		}

		// Test
		ExplanationOfBenefit request = new ExplanationOfBenefit();
		request.setId("A");
		request.setPatient(new Reference("Patient/PAT1"));
		request.addInsurance().setCoverage(new Reference("Coverage/COV1"));

		myExplanationOfBenefitDao.update(request, newSrd());

		 // Verify
		Coverage actualCoverage = myCoverageDao.read(new IdType("Coverage/COV1"), newSrd());
		List<IIdType> compartmentOwners = myFhirContext.newTerser().getCompartmentOwnersForResource("Patient", actualCoverage, Set.of());
		assertThat(compartmentOwners).containsExactly(new IdType("Patient/PAT1"));
	}

	/**
	 * Type is not in the patient compartment
	 */
	@Test
	public void testCreateOrganization_ValidMembershipInCompartment() {
		Organization org = new Organization();
		org.setName("Foo");
		Long id = myOrganizationDao.create(org, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Organization", observation.getResourceType());
			assertEquals(ALTERNATE_DEFAULT_ID, observation.getPartitionId().getPartitionId().intValue());
		});
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testPatch(boolean theById) {
		createPatientA();

		Observation obs = new Observation();
		obs.setId("B");
		obs.getSubject().setReference("Patient/A");
		IIdType obsId = myObservationDao.update(obs).getId().toUnqualifiedVersionless();

		// Test
		FhirPatchBuilder patchBuilder = new FhirPatchBuilder(myFhirContext);
		patchBuilder
			.insert()
			.path("Observation.identifier")
			.index(0)
			.value(new Identifier().setSystem("http://foo").setValue("123"));
		if (theById) {
			myObservationDao.patch(obsId, null, PatchTypeEnum.FHIR_PATCH_JSON, myFhirContext.newJsonParser().encodeResourceToString(patchBuilder.build()), patchBuilder.build(), newSrd());
		} else {
			myObservationDao.patch(null, "Observation?subject=Patient/A", PatchTypeEnum.FHIR_PATCH_JSON, myFhirContext.newJsonParser().encodeResourceToString(patchBuilder.build()), patchBuilder.build(), newSrd());
		}

		// Verify
		Observation actual = myObservationDao.read(obsId, mySrd);
		assertEquals("123", actual.getIdentifier().get(0).getValue());
	}


	@Test
	public void testCreateGroupWithNoPatientReferenceValueIsInTheDefaultPartition() {
		//Given: A Group resource with no members
		Group group = new Group();
		group.setActual(false);

		Long id = myGroupDao.create(group, mySrd).getId().getIdPartAsLong();
		//Verify: Group is successfully created and put in the default partition
		runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Group", table.getResourceType());
			assertEquals(ALTERNATE_DEFAULT_ID, table.getPartitionId().getPartitionId().intValue());
		});
	}

	@Test
	public void testReadPatient_Good() {
		createPatientA();

		myCaptureQueriesListener.clear();
		Patient patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertTrue(patient.getActive());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("rt1_0.PARTITION_ID=?");
	}

	@Test
	public void testReadObservation_Good() {
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		try {
			myObservationDao.read(new IdType("Observation/" + id), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals("This server is not able to handle this request of type READ", e.getMessage());
		}
	}

	@Test
	public void testReadPatientHistory_Good() {
		Patient patientA = createPatientA();
		patientA.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(patientA, newSrd());


		IdType patientVersionOne = new IdType("Patient", "A", "1");
		myCaptureQueriesListener.clear();
		Patient patient = myPatientDao.read(patientVersionOne);
		assertEquals("1", patient.getIdElement().getVersionIdPart());

		assertThat(myCaptureQueriesListener).has(
			onCurrentThread()
				.selectCount(1)
				.commitCount(1)
				.noOtherCounts()
				.selectSqlAtIndex(0).contains("PARTITION_ID='65'")
		);
	}

	@SuppressWarnings("GrazieInspection")
	@ParameterizedTest
	@CsvSource(textBlock =
		//  Policy                            | Part 1: Create                       | Part 2: Search
		//                                    | DocRefWithPatient | DocRefNoPatient  | IncludePatientIdInSearch | ExpectedSearchPartition
		//                                    | Expected Part ID  | Expected Part ID |                          |
		"""
			ALWAYS_USE_DEFAULT_PARTITION      , -1                , -1               , false                    , -1
			OPTIONAL_SINGLE_COMPARTMENT       , 65                , -1               , false                    , ALL
			MANDATORY_SINGLE_COMPARTMENT      , 65                , FAIL             , false                    , ALL
			ALWAYS_USE_PARTITION_ID/5         , 5                 , 5                , false                    , 5
			ALWAYS_USE_DEFAULT_PARTITION      , -1                , -1               , true                     , -1
			OPTIONAL_SINGLE_COMPARTMENT       , 65                , -1               , true                     , 65
			MANDATORY_SINGLE_COMPARTMENT      , 65                , FAIL             , true                     , 65
			ALWAYS_USE_PARTITION_ID/5         , 5                 , 5                , true                     , 5
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT , 65                , -1               , false                    , ALL
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT , 65                , -1               , true                     , '65,-1'
			""")
	void testResourceTypePolicies_PatientCompartmentResource(String thePolicyString, int thePatientSpecificDocRefCompartmentId, String theDocRefNoPatientCompartmentString, boolean theIncludePatientIdInSearch, String theExpectedSearchPartitionString) {
		/*
		 * Part 1: Create DocumentReference resource
		 */

		// Setup
		ResourceCompartmentStoragePolicy policy = ResourceCompartmentStoragePolicy.parse(thePolicyString);

		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		mySvc.setResourceTypePolicies(Map.of("DocumentReference", policy));
		createPatient(withId("A"), withActiveTrue());

		// Test

		// Create a DocRef in the patient compartment
		DocumentReference docRef = new DocumentReference();
		docRef.setSubject(new Reference("Patient/A"));
		docRef.getContentFirstRep().getAttachment().setContentType("text/plain");
		IIdType ptCompartmentDocRefId = myDocumentReferenceDao.create(docRef, newSrd()).getId().toUnqualifiedVersionless();
		assertResourceIsInPartition(thePatientSpecificDocRefCompartmentId, ptCompartmentDocRefId);

		// Create a DocRef NOT in the patient compartment
		DocumentReference nonCompartmentDocRef = new DocumentReference();
		nonCompartmentDocRef.getContentFirstRep().getAttachment().setContentType("text/plain");
		IIdType nonCompartmentDocRefId;
		if (theDocRefNoPatientCompartmentString.equals("FAIL")) {
			assertThatThrownBy(() -> myDocumentReferenceDao.create(nonCompartmentDocRef, newSrd()))
				.isInstanceOf(MethodNotAllowedException.class)
				.hasMessageContaining("Resource of type DocumentReference has no values placing it in the Patient compartment");
			nonCompartmentDocRefId = null;
		} else {
			nonCompartmentDocRefId = myDocumentReferenceDao.create(nonCompartmentDocRef, newSrd()).getId().toUnqualifiedVersionless();
			int partition = Integer.parseInt(theDocRefNoPatientCompartmentString);
			assertResourceIsInPartition(partition, nonCompartmentDocRefId);
		}
		logAllResources();

		/*
		 * Part 2: Perform a Search for DOcumentReference Resources
		 */

		// Test: Search for DocRefs with no parameters
		SearchParameterMap paramMap = SearchParameterMap.newSynchronous();
		if (theIncludePatientIdInSearch) {
			paramMap.add(DocumentReference.SP_PATIENT, new ReferenceParam("Patient/A"));
		}
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myDocumentReferenceDao.search(paramMap, newSrd());
		myCaptureQueriesListener.logSelectQueries();

		// Verify: All compartments searched
		SqlQuery searchQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0);
		if (theExpectedSearchPartitionString.equals("ALL")) {
			assertTrue(searchQuery.getRequestPartitionId().isAllPartitions());
			assertThat(searchQuery.getSql(true, true)).doesNotContain("PARTITION_ID =");
			assertThat(searchQuery.getSql(true, true)).doesNotContain("PARTITION_ID IN");
		} else if (theExpectedSearchPartitionString.contains(",")) {
			// Multiple expected partitions — verify PARTITION_ID IN (...) clause
			List<Integer> expectedPartitions = Arrays.stream(theExpectedSearchPartitionString.split(","))
				.map(String::trim)
				.map(Integer::parseInt)
				.toList();
			assertThat(searchQuery.getRequestPartitionId().getPartitionIds()).containsExactlyInAnyOrderElementsOf(expectedPartitions);
			assertThat(parseMultiplePartitionIdsFromSqlInClause(searchQuery.getSql(true, true))).containsExactlyInAnyOrderElementsOf(expectedPartitions);
		} else {
			// Single expected partition — verify PARTITION_ID = ... clause
			int partition = Integer.parseInt(theExpectedSearchPartitionString);
			assertThat(searchQuery.getRequestPartitionId().getPartitionIds()).containsExactly(partition);
			assertThat(searchQuery.getSql(true, true)).containsAnyOf(
				"PARTITION_ID = '" + partition + "'",
				"PARTITION_ID='" + partition + "'"
			);
		}

		// Verify: Both DocRefs should be found
		if (nonCompartmentDocRefId != null && !theIncludePatientIdInSearch) {
			assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(ptCompartmentDocRefId.getValue(), nonCompartmentDocRefId.getValue());
		} else {
			assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(ptCompartmentDocRefId.getValue());
		}
	}

	@Test
	public void testSearchPatient_Good() {
		createPatientA();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous("_id", new TokenParam("A")), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("PARTITION_ID = ?");
	}

	@Test
	public void testSearchObservation_Good() {
		createPatientA();
		createObservationB();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous("subject", new ReferenceParam("Patient/A")), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.PARTITION_ID = ?)");

		// Typed
		myCaptureQueriesListener.clear();
		ReferenceParam referenceParam = new ReferenceParam();
		referenceParam.setValueAsQueryToken(myFhirContext, "subject", ":Patient", "A");
		outcome = myObservationDao.search(SearchParameterMap.newSynchronous("subject", referenceParam), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.PARTITION_ID = ?)");

		// superset parameter
		myCaptureQueriesListener.clear();
		outcome = myObservationDao.search(SearchParameterMap.newSynchronous("patient", new ReferenceParam("Patient/A")), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.PARTITION_ID = ?)");

	}

	@Test
	public void testSearchObservation_NoCompartmentMembership() {
		createPatientA();
		createObservationB();

		myCaptureQueriesListener.clear();
		myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd);
		myCaptureQueriesListener.logSelectQueries();
		assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Observation') AND (t0.RES_DELETED_AT IS NULL)) fetch first '10000' rows only", myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false));
	}

	@Test
	void testSearchObservation_mixedCompartmentParam_findsAll() {
	    // given
		mySvc.setResourceTypePolicies(Map.of(
			"Observation",
			ResourceCompartmentStoragePolicy.optionalSingleCompartment()
		));
		createPatientA();
		IIdType patAObsId = createObservation(withSubject("Patient/A"), withStatus("final"), withIdentifier("http://example.com", "patObsIdentifier"));
		IIdType g1 = createGroup(withId("G1"));
		IIdType g1ObsId = createObservation(withSubject(g1), withStatus("final"), withIdentifier("http://example.com", "groupObsIdentifier"));

		myTestDaoSearch.assertSearchFinds("find both cross partition", "Observation?status=final", patAObsId, g1ObsId);
		myTestDaoSearch.assertSearchFinds("find only patient Obs", "Observation?status=final&subject=Patient/A", patAObsId);
		myTestDaoSearch.assertSearchFinds("find only group Obs", "Observation?status=final&subject=Group/G1", g1ObsId);
		myTestDaoSearch.assertSearchFinds("find by identifier for patient observation", "Observation?identifier=patObsIdentifier", patAObsId);
		myTestDaoSearch.assertSearchFinds("find by identifier for group observation", "Observation?identifier=groupObsIdentifier", g1ObsId);
	}

	@Test
	public void testSearchWithChainedNonCompartmentResources() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		createPatient(withId("A"), withActiveTrue());
		IIdType orgId = createOrganization(withId("org1"), withName("orgName"), withActiveTrue());
		IIdType encounterId = createEncounter(withSubject("Patient/A"), withReference("serviceProvider", orgId));

		myTestDaoSearch.assertSearchFinds("find Encounter", "Encounter?service-provider.name=orgName&service-provider.active=true", encounterId);
	}

	@Test
	public void testSearchWithChainedAndResolvedReference() {
		createPatient(withId("A"), withActiveTrue());
		IIdType patAObsId = createObservation(withSubject("Patient/A"), withStatus("final"));

		myTestDaoSearch.assertSearchFinds("find patient Observation", "Observation?subject=Patient/A&subject.active=true", patAObsId);
		myTestDaoSearch.assertSearchNotFound("find patient Observation", "Observation?subject=Patient/A&subject.active=false", patAObsId);
		myTestDaoSearch.assertSearchFinds("find patient Observation", "Observation?subject.active=true&subject=Patient/A", patAObsId);
	}

	@Test
	public void testSearchChainedValue_noResolvedReference_fails() {
		createPatientA();
		createObservationB();

		// Chain
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous().add("subject", new ReferenceParam("identifier", "http://foo|123")), mySrd);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-2928: Could not resolve chained parameter(s) [identifier] on parameter subject. Consider adding a direct Patient reference to your search (?subject=Patient/abc&subject.identifier=...)", e.getMessage());
		}

	}

	@Test
	public void testSearchObservation_ChainedSubjectParameterNoDirectReference() {
		createPatientA();
		createObservationB();

		// Multiple ANDs
		assertThatThrownBy(() -> myObservationDao.search(SearchParameterMap
				.newSynchronous()
				.add("performer", new ReferenceParam("identifier", "http://patient|1")), mySrd))
			.isInstanceOf(MethodNotAllowedException.class)
			.hasMessageContaining(Msg.code(1323) + "The parameter performer.identifier is not supported in patient compartment mode");
	}

	@Test
	public void testSearchObservation_MultipleCompartmentMembership() {
		createPatientA();
		createPatient(withId("B"), withActiveTrue());
		createObservationB();

		// Multiple ANDs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
					.add("subject", new ReferenceParam("Patient/A"))
					.add("subject", new ReferenceParam("Patient/B"))
				, mySrd);
//			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1324) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}

		// Multiple ORs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
				.add(
					"subject", new ReferenceOrListParam().add(new ReferenceParam("Patient/A")).add(new ReferenceParam("Patient/B"))
				), mySrd);
//			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1324) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}
	}

	@Test
	void testTransaction_ObservationInMultipleCompartments_throws() {
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveTrue());

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		obs.addPerformer().setReference("Patient/B");

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(obs);

		assertThatThrownBy(() -> mySystemDao.transaction(newSrd(), bb.getBundleTyped()))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining(Msg.code(1324));
	}

	@Test
	public void testSearchObservation_allowsMdmQualifier() {
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous().add("subject", new ReferenceParam("Patient/ABC").setMdmExpand(true)), mySrd);
		} catch (MethodNotAllowedException e) {
			fail("Searching with :mdm qualifier is allowed and should not trow an exception");
		}
	}

	/**
	 * Type is not in the patient compartment
	 */
	@Test
	public void testSearchOrganization_Good() {
		createOrganizationC();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myOrganizationDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("t0.PARTITION_ID = ?");
	}

	@Test
	public void testSearch_MultipleCompartments() {
		// Setup

		createPatient(withId("PAT-0"), withIdentifier("http://patient", "0"));
		createEncounter(withId("ENC-0"), withSubject("Patient/PAT-0"));

		createPatient(withId("PAT-1"), withIdentifier("http://patient", "1"));
		createEncounter(withId("ENC-1"), withSubject("Patient/PAT-1"));

		createPatient(withId("PAT-2"), withIdentifier("http://patient", "2"));
		createEncounter(withId("ENC-2"), withSubject("Patient/PAT-2"));

		// Test

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Encounter.SP_PATIENT, new ReferenceOrListParam("Patient/PAT-0", "Patient/PAT-1"));
		myCaptureQueriesListener.clear();
		IBundleProvider actual = myEncounterDao.search(map, newSrd());
		myCaptureQueriesListener.logSelectQueries();

		// Verify

		SqlQuery searchQuery = myCaptureQueriesListener.getSelectQueries().get(1);
		assertThat(searchQuery.getRequestPartitionId().getPartitionIds()).contains(262, 263);
		assertThat(searchQuery.getSql(true, true)).contains("PARTITION_ID IN ('262', '263')");

		List<String> actualIds = toUnqualifiedVersionlessIdValues(actual);
		assertThat(actualIds).containsExactlyInAnyOrder(
			"Encounter/ENC-0",
			"Encounter/ENC-1"
		);
	}


	@Test
	public void testHistory_Instance() {
		Organization org = createOrganizationC();
		org.setName("name 2");

		logAllResources();

		myOrganizationDao.update(org, newSrd());

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myOrganizationDao.history(new IdType("Organization/C"), null, null, null, mySrd);
		assertEquals(2, outcome.size());
		assertThat(myCaptureQueriesListener).has(
			onAllThreads()
				.selectCount(1)
				.commitCount(2)
				.noOtherCounts()
				.selectSqlAtIndex(0).contains("PARTITION_ID='-1'")
		);
	}

	@Test
	public void testTransactionWithPlaceholderReferencesToNormallyUpdatedResource() {
		// pepe
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);

		String patientFullUrl = "urn:uuid:123-456-789";

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(
			buildPatient(withId("A"), withActiveTrue()),
			"Patient/A",
			patientFullUrl
		);
		bb.addTransactionCreateEntry(
			buildEncounter(withSubject(patientFullUrl), withStatus("cancelled"))
		);
		Bundle request = bb.getBundleTyped();

		Bundle result = mySystemDao.transaction(mySrd, request);
		TransactionUtil.TransactionResponse parsedResult = TransactionUtil.parseTransactionResponse(myFhirContext, request, result);
		assertEquals(2, parsedResult.getStorageOutcomes().size());
		assertEquals("Patient/A/_history/1", parsedResult.getStorageOutcomes().get(0).getTargetId().getValue());
		assertThat(parsedResult.getStorageOutcomes().get(1).getTargetId().getValue()).matches("Encounter/.*/_history/1");

		Encounter actual = myEncounterDao.read(new IdType(parsedResult.getStorageOutcomes().get(1).getTargetId().getValue()), mySrd);
		assertEquals("Patient/A", actual.getSubject().getReference());
	}

	@Test
	public void testTransaction_NoRequestDetails() throws IOException {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");

		// Maybe in the future we'll make request details mandatory and if that
		// causes this to fail that's ok
		Bundle outcome = mySystemDao.transaction(null, input);

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient")).containsExactly(4267);
		assertThat(resourcesByType.get("ExplanationOfBenefit")).containsExactly(4267);
		assertThat(resourcesByType.get("Coverage")).containsExactly(4267);
		assertThat(resourcesByType.get("Organization")).containsExactly(-1, -1);
		assertThat(resourcesByType.get("Practitioner")).containsExactly(-1, -1, -1);
	}

	@Test
	public void testTransaction_SystemRequestDetails() throws IOException {
		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");
		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(), input);
		myCaptureQueriesListener.logSelectQueries();
		List<String> selectQueryStrings = myCaptureQueriesListener
			.getSelectQueries()
			.stream()
			.map(t -> t.getSql(false, false).toUpperCase(Locale.US))
			.filter(t -> !t.contains("FROM HFJ_TAG_DEF"))
			.collect(Collectors.toList());
		for (String next : selectQueryStrings) {
			assertThat(next).satisfiesAnyOf(s -> assertThat(s).contains("PARTITION_ID ="),
				s -> assertThat(s).contains("PARTITION_ID IN"));
		}

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		String patientId = resourceIds.get("Patient").get(0);

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient")).containsExactly(4267);
		assertThat(resourcesByType.get("ExplanationOfBenefit")).containsExactly(4267);
		assertThat(resourcesByType.get("Coverage")).containsExactly(4267);
		assertThat(resourcesByType.get("Organization")).containsExactly(-1, -1);
		assertThat(resourcesByType.get("Practitioner")).containsExactly(-1, -1, -1);

		// Try Searching
		SearchParameterMap map = new SearchParameterMap();
		map.add(ExplanationOfBenefit.SP_PATIENT, new ReferenceParam(patientId));
		map.addInclude(new Include("*"));
		myCaptureQueriesListener.clear();
		IBundleProvider result = myExplanationOfBenefitDao.search(map);
		List<String> resultIds = toUnqualifiedVersionlessIdValues(result);
		assertThat(resultIds).as(resultIds.toString()).containsExactlyInAnyOrder(resourceIds.get("Coverage").get(0), resourceIds.get("Organization").get(0), resourceIds.get("ExplanationOfBenefit").get(0), resourceIds.get("Patient").get(0), resourceIds.get("Practitioner").get(0), resourceIds.get("Practitioner").get(1), resourceIds.get("Practitioner").get(2));

		myCaptureQueriesListener.logSelectQueries();

		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		assertThat(selectQueries.get(0).getSql(true, false).toUpperCase(Locale.US)).matches("SELECT.*FROM HFJ_RES_LINK.*WHERE.*PARTITION_ID = '4267'.*");

	}

	@Test
	public void testTransaction_ConditionallyCreatedPatientAndConditionallyCreatedObservation() {

		BundleBuilder tx = new BundleBuilder(myFhirContext);

		Patient p = new Patient();
		p.setId(IdType.newRandomUuid());
		p.addIdentifier().setSystem("http://ids").setValue("A");
		tx.addTransactionCreateEntry(p).conditional("Patient?identifier=http://ids|A");

		Observation o = new Observation();
		o.addIdentifier().setSystem("http://ids").setValue("B");
		o.setSubject(new Reference(p.getId()));
		tx.addTransactionCreateEntry(o).conditional("Observation?identifier=http://ids|B");

		try {
			mySystemDao.transaction(mySrd, (Bundle) tx.getBundle());
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-1321: Patient resource IDs must be client-assigned in patient compartment mode, or server id strategy must be UUID", e.getMessage());
		}
	}

	@Test
	public void testTransaction_createObservationWithInlineMatchUrlReferenceToNonExistingPatient() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		Identifier patientIdentifier = new Identifier().setSystem(oldPayerSystem).setValue("A");
		String patientInlineMatchUrl = buildInlineMatchUrlStr("Patient", patientIdentifier);

		Observation o = new Observation();
		Identifier obsIdentifier = new Identifier().setSystem(oldPayerSystem).setValue("B");
		o.addIdentifier(obsIdentifier);
		o.setSubject(new Reference(patientInlineMatchUrl));  // Patient?identifier=urn:uudid:old-payer-sytem|A

		// when operating with PatientId partitioning mode disabled, processing a resource with an inlineMatchUrl reference
		// to the related Patient will succeed with the Patient resource being create as a placeholder.
		// but in PatientId partitioning mode, a resource in the patient compartment needs a reference by fhirId
		// (Patient/123) to derive the partitionId for the resource and that happens before the placeholder creation process.
		// in order to ingest the above observation, its patient reference needs resolution/placeholder before invocation of
		// pointcut STORAGE_PARTITION_IDENTIFY_CREATE on PatientIdPartitioningInterceptor.
		//
		// Placeholder creation is equivalent to front loading the inlineMatchUrl reference with a POST with conditional Url.
		// see PatientInlineMatchUrlPreCreationService.conditionallyCreatePatientsForInlineMatchUrls.

		BundleBuilder tx = new BundleBuilder(myFhirContext);
		tx.addTransactionUpdateEntry(o).conditional(buildInlineMatchUrlStr("Observation", obsIdentifier));

		Bundle requestBundle = tx.getBundleTyped();
		String bundleAsString = myFhirContext.newJsonParser().encodeResourceToString(requestBundle);
		ourLog.info("Bundle: {}", bundleAsString);

		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);

		// front loading will have the submitted bundle massaged into:
//		{ "resourceType" : "Bundle",
//			"type" : "transaction",
//			"entry" : [ {
//			"resource" : {
//				"resourceType" : "Patient",
//				"identifier" : [ {
//					"system" : "urn:uudid:old-payer-sytem",
//						"value" : "A"} ]},
//			"request" : {
//				"method" : "POST",
//					"url" : "Patient",
//					"ifNoneExist" : "Patient?identifier=urn:uudid:old-payer-sytem|A"}
//		}, {"resource" : {
//				"resourceType" : "Observation",
//					"identifier" : [ {
//					"system" : "urn:uudid:old-payer-sytem",
//						"value" : "B"} ],
//				"subject" : {
//					"reference" : "Patient?identifier=urn:uudid:old-payer-sytem|A"}
//			},
//			"request" : {
//				"method" : "PUT",
//					"url" : "Observation?identifier=urn:uudid:old-payer-sytem|B"}} ]}

		// then, the bundle is submitted to the PatientIdentifierPreResolutionInterceptor on pointcut STORAGE_TRANSACTION_PROCESSING.
		// where the interceptor transforms the bundle into:
//		{ "resourceType" : "Bundle",
//			"type" : "transaction",
//			"entry" : [ {
//			"resource" : {
//				"resourceType" : "Patient",
//					"id" : "bfce5151-deb5-43c3-b613-12f5ca0ef4d5",
//					"identifier" : [ {
//					"system" : "urn:uudid:old-payer-sytem",
//						"value" : "A"} ]},
//			"request" : {
//				"method" : "POST",
//					"url" : "Patient",
//					"ifNoneExist" : "Patient?_id=Patient/bfce5151-deb5-43c3-b613-12f5ca0ef4d5"}
//		}, {"resource" : {
//				"resourceType" : "Observation",
//					"identifier" : [ {
//					"system" : "urn:uudid:old-payer-sytem",
//						"value" : "B"} ],
//				"subject" : {
//					"reference" : "Patient/bfce5151-deb5-43c3-b613-12f5ca0ef4d5"}},
//			"request" : {
//				"method" : "PUT",
//					"url" : "Observation?identifier=urn:uudid:old-payer-sytem|B"}} ]}

		// now that the Patient resource has an id and the Observation has a resolved reference to its Patient, both
		// resources can be processed by the PatientIdPartitioningInterceptor on pointcut STORAGE_PARTITION_IDENTIFY_CREATE
		// where a requestpartitioningId is derived allowing ingestion/creation of both resource.

		TransactionUtil.TransactionResponse parsedResult = TransactionUtil.parseTransactionResponse(myFhirContext, requestBundle, resultBundle);
		assertEquals(2, parsedResult.getStorageOutcomes().size());

		IIdType firstCreatedResourceIdType = parsedResult.getStorageOutcomes().get(0).getTargetId();
		assertThat(firstCreatedResourceIdType.getResourceType()).isEqualTo("Patient");
		assertThat(firstCreatedResourceIdType.getValue()).matches("Patient/.*/_history/1");
		String expectedPatientReference = firstCreatedResourceIdType.toUnqualifiedVersionless().getValue();

		IIdType secondCreatedResourceIdType = parsedResult.getStorageOutcomes().get(1).getTargetId();
		assertThat(secondCreatedResourceIdType.getValue()).matches("Observation/.*/_history/1");

		//
		Observation createdObservation = myObservationDao.read(new IdType(secondCreatedResourceIdType.getValue()), mySrd);
		assertThat(createdObservation.getSubject().getReference()).isEqualTo(expectedPatientReference);

		//
		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		Collection<Integer> patientPartitionIds = resourcesByType.get("Patient");
		Collection<Integer> observationPartitionIds = resourcesByType.get("Observation");

		assertThat(patientPartitionIds).hasSize(1);
		assertThat(observationPartitionIds).hasSize(1);

		assertThat(patientPartitionIds).containsExactlyElementsOf(observationPartitionIds);

	}

	@Test
	public void testTransaction_createPatientWithConditionalUrl(){
		BundleBuilder tx = new BundleBuilder(myFhirContext);

		Identifier patientIdentifier = new Identifier().setSystem(oldPayerSystem).setValue("A");

		Patient p = new Patient();
		p.setActive(true);
		p.addIdentifier(patientIdentifier);
		tx.addTransactionCreateEntry(p).conditional(buildInlineMatchUrlStr("Patient", patientIdentifier));
		// the above will generate requet:
		// "request":{"method":"POST","url":"Patient","ifNoneExist":"Patient?identifier=urn:uudid:old-payer-sytem|A"}}]}

		Bundle requestBundle = tx.getBundleTyped();
		String bundleAsString = myFhirContext.newJsonParser().encodeResourceToString(requestBundle);
		ourLog.info("Bundle: {}", bundleAsString);

		// it should be noted that the Patient resource does not have an id at the start of the ingestion process.
		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);

		// the id is generated & added to the resource by the (new)PatientIdentifierPreResolutionInterceptor on pointcut
		// STORAGE_TRANSACTION_PROCESSING. when encountering a request with a conditional create, the pointcut transforms it
		// in a conditional create with ID:
		// "entry": [ {
		// resource": {"resourceType":"Patient", "id":"02e8afaf",...},
		// "request": {"method":"POST", "url":"Patient", "ifNoneExist":"Patient?_id=Patient/02e8afaf"}} ]
		//
		// and update all inlineMatchUlr references of 'Patient?identifier=urn:uudid:old-payer-sytem|A' to
		// Patient/02e8afaf.
		// as a result, interceptor PatientIdPartitioningInterceptor is able to derive a partitionId from the Patient id
		// on pointcut STORAGE_PARTITION_IDENTIFY_CREATE.

	}

	private String asCanonicalString(Identifier theIdentifier) {
		return theIdentifier.getSystem() + "|" + theIdentifier.getValue();
	}

	private String buildInlineMatchUrlStr(String theResourceType, Identifier theIdentifier) {
		return theResourceType + "?identifier=" + asCanonicalString(theIdentifier);
	}

	@Test
	public void testTransaction_SplitAncillaryAndPatient_NewTransactionPerPartition() {
		registerInterceptor(new MyTransactionSplitInterceptor());
		myTransactionService.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);

		String practitionerFullUrl = IdType.newRandomUuid().getValue();
		String patientFullUrl = IdType.newRandomUuid().getValue();
		String encounterFullUrl = IdType.newRandomUuid().getValue();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(
			buildPractitioner(
				withIdentifier("http://practitioner", "1")
			),
			practitionerFullUrl
		);
		bb.addTransactionCreateEntry(
			buildPatient(
				withIdentifier("http://patient", "1"),
				withReference("generalPractitioner", practitionerFullUrl)
			),
			patientFullUrl
		);
		bb.addTransactionCreateEntry(
			buildEncounter(
				withIdentifier("http://encounter", "1"),
				withSubject(patientFullUrl),
				withReference("participant.individual", practitionerFullUrl)
			),
			encounterFullUrl
		);

		Bundle request = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, request);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

	}

	@Test
	public void testSearch() throws IOException {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(), input);

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		String patientId = resourceIds.get("Patient").get(0);

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient")).containsExactly(4267);
		assertThat(resourcesByType.get("ExplanationOfBenefit")).containsExactly(4267);
		assertThat(resourcesByType.get("Coverage")).containsExactly(4267);
		assertThat(resourcesByType.get("Organization")).containsExactly(-1, -1);
		assertThat(resourcesByType.get("Practitioner")).containsExactly(-1, -1, -1);

		// Try Searching
		SearchParameterMap map = new SearchParameterMap();
		map.add(ExplanationOfBenefit.SP_PATIENT, new ReferenceParam(patientId));
		map.addInclude(new Include("*"));
		myCaptureQueriesListener.clear();
		IBundleProvider result = myExplanationOfBenefitDao.search(map, newSrd());
		List<String> resultIds = toUnqualifiedVersionlessIdValues(result);
		assertThat(resultIds).as(resultIds.toString()).containsExactlyInAnyOrder(resourceIds.get("Coverage").get(0), resourceIds.get("Organization").get(0), resourceIds.get("ExplanationOfBenefit").get(0), resourceIds.get("Patient").get(0), resourceIds.get("Practitioner").get(0), resourceIds.get("Practitioner").get(1), resourceIds.get("Practitioner").get(2));

		myCaptureQueriesListener.logSelectQueries();

		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		assertThat(selectQueries.get(0).getSql(true, false).toUpperCase(Locale.US)).matches("SELECT.*FROM HFJ_RES_LINK.*WHERE.*PARTITION_ID = '4267'.*");

	}


	@Test
	public void testHistory_Type() {
		myOrganizationDao.history(null, null, null, mySrd);
	}

	@Test
	public void testHistory_System() {
		mySystemDao.history(null, null, null, mySrd);
	}

	private Organization createOrganizationC() {
		Organization org = new Organization();
		org.setId("C");
		org.setName("Foo");
		myOrganizationDao.update(org, mySrd);
		return org;
	}

	private void createObservationB() {
		Observation obs = new Observation();
		obs.setId("B");
		obs.getSubject().setReference("Patient/A");
		myObservationDao.update(obs, mySrd);
	}

	private Patient createPatientA() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		DaoMethodOutcome update = myPatientDao.update(patient, mySrd);
		return (Patient) update.getResource();
	}

	@Test
	public void testIdentifyForRead_serverOperation_returnsAllPartitions() {
		ReadPartitionIdRequestDetails readRequestDetails = ReadPartitionIdRequestDetails.forServerOperation(ProviderConstants.OPERATION_EXPORT);
		RequestPartitionId requestPartitionId = mySvc.identifyForRead(readRequestDetails, mySrd);
		assertEquals(requestPartitionId, RequestPartitionId.allPartitions());
		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER, readRequestDetails.getRestOperationType());
	}

	@Test
	public void testSystemBulkExport_withPatientIdPartitioningWithNoResourceType_usesNonPatientSpecificPartition() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());
		}
	}

	@Test
	public void testSystemBulkExport_withPatientIdPartitioningWithResourceType_exportUsesNonPatientSpecificPartition() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(JpaConstants.PARAM_EXPORT_TYPE, "Patient");
		post.addHeader(JpaConstants.PARAM_EXPORT_TYPE_FILTER, "Patient?");

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());
		}
	}

	@Test
	public void testSystemBulkExport_withPatientIdPartitioningWithResourceType_pollSuccessful() throws IOException {
		final BulkExportJobParameters options = new BulkExportJobParameters();
		options.setExportStyle(BulkExportJobParameters.ExportStyle.SYSTEM);
		options.setOutputFormat(Constants.CT_FHIR_NDJSON);

		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(JpaConstants.PARAM_EXPORT_TYPE, "Patient"); // ignored when computing partition
		post.addHeader(JpaConstants.PARAM_EXPORT_TYPE_FILTER, "Patient?");

		String locationUrl;

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());

			Header locationHeader = postResponse.getFirstHeader(Constants.HEADER_CONTENT_LOCATION);
			assertNotNull(locationHeader);
			locationUrl = locationHeader.getValue();
		}

		HttpGet get = new HttpGet(locationUrl);
		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(get)) {
			String responseContent = IOUtils.toString(postResponse.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testSystemOperation_withNoResourceType_success() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testSyntheaLoad(boolean theSplitTransaction) throws IOException {

		if (theSplitTransaction) {
			registerInterceptor(new MyTransactionSplitInterceptor());
		}

		// given
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		// Load the Synthea supporting bundles
		TransactionUtil.TransactionResponse hospitalParsedResponse;
		hospitalParsedResponse = performTransactionAndParseResponse(loadResourceFromClasspath(Bundle.class, "transaction-bundles/synthea/hospitalInformation1743689610792.json"));
		IIdType locationId = hospitalParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Location")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();
		TransactionUtil.TransactionResponse practitionerParsedResponse = performTransactionAndParseResponse(loadResourceFromClasspath(Bundle.class, "transaction-bundles/synthea/practitionerInformation1743689610792.json"));
		IIdType practitionerId = practitionerParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Practitioner")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();

		// when
		myCaptureQueriesListener.clear();
		TransactionUtil.TransactionResponse mainParsedResponse = performTransactionAndParseResponse(loadResourceFromClasspath(Bundle.class, "transaction-bundles/synthea/Sherise735_Zofia65_Swaniawski813_e0f7758e-a749-4357-858c-53e1db808e37.json"));

		// verify
		// This bundle contains 517 resources.
		assertEquals(27, myCaptureQueriesListener.countSelectQueries());
		// this is so high because we limit Hibernate to batches of 30 rows.
		if (theSplitTransaction) {
			assertEquals(328, myCaptureQueriesListener.getInsertQueries().size());
			assertEquals(9379, myCaptureQueriesListener.countInsertQueries());
			assertEquals(36, myCaptureQueriesListener.getUpdateQueries().size());
			assertEquals(1016, myCaptureQueriesListener.countUpdateQueries());
			assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
			assertEquals(2, myCaptureQueriesListener.countCommits());
		} else {
			assertEquals(322, myCaptureQueriesListener.getInsertQueries().size());
			assertEquals(9379, myCaptureQueriesListener.countInsertQueries());
			assertEquals(35, myCaptureQueriesListener.getUpdateQueries().size());
			assertEquals(1016, myCaptureQueriesListener.countUpdateQueries());
			assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
			assertEquals(1, myCaptureQueriesListener.countCommits());
		}

		IIdType patientId = mainParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Patient")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();
		IIdType encounterId = mainParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Encounter")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();
		IIdType observationId = mainParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Observation")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();
		IIdType medicationId = mainParsedResponse.getStorageOutcomes().stream().filter(t -> t.getTargetId().getResourceType().equals("Medication")).findFirst().orElseThrow().getTargetId().toUnqualifiedVersionless();

		int patientPartition = getResourcePartition(patientId);
		assertThat(patientPartition).isGreaterThan(0);
		assertResourceIsInPartition(patientPartition, encounterId);
		assertResourceIsInPartition(patientPartition, observationId);
		assertResourceIsInPartition(-1, medicationId);
		assertResourceIsInPartition(-1, locationId);
		assertResourceIsInPartition(-1, practitionerId);
	}

	private void assertResourceIsInPartition(int theExpectedPartitionId, IIdType theResourceId) {
		assert theResourceId != null;
		assert theResourceId.hasResourceType();
		assert theResourceId.hasIdPart();

		runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findByTypeAndFhirId(theResourceId.getResourceType(), theResourceId.getIdPart()).orElseThrow();
			assertEquals(theExpectedPartitionId, Objects.requireNonNull(table.getPartitionId().getPartitionId()).intValue());
		});
	}

	/**
	 * Extracts partition IDs from a PARTITION_ID IN (...) clause in a SQL string.
	 */
	private List<Integer> parseMultiplePartitionIdsFromSqlInClause(String theSql) {
		Matcher matcher = PARTITION_ID_IN_PATTERN.matcher(theSql);
		assertThat(matcher.find()).as("Expected PARTITION_ID IN (...) in SQL: " + theSql).isTrue();
		return Arrays.stream(matcher.group(1).split(","))
			.map(String::trim)
			.map(s -> s.replace("'", ""))
			.map(Integer::parseInt)
			.toList();
	}

	private int getResourcePartition(IIdType theResourceId) {
		return runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findByTypeAndFhirId(theResourceId.getResourceType(), theResourceId.getIdPart()).orElseThrow();
			return entity.getPartitionId().getPartitionId();
		});
	}

	@Nonnull
	private TransactionUtil.TransactionResponse performTransactionAndParseResponse(Bundle request) {
		Bundle response = assertDoesNotThrow(() -> myServer.getFhirClient().transaction().withBundle(request).execute());
		return TransactionUtil.parseTransactionResponse(myFhirContext, request, response);
	}

	@Test
	void testIdReferenceToDefaultPartition_resolvesWithoutError() {
		// given
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		IIdType practitionerId = createPractitioner();
		Patient patient = buildResource(Patient.class,
			withReference("generalPractitioner", practitionerId));

		// when
		assertDoesNotThrow(() -> myServer.getFhirClient().create().resource(patient).execute());
	}


	@Test
	void testLoadBundle_resourceInCompartmentReferencesExistingNonCompartmentResource() {
		// given
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		IParser parser = myFhirContext.newJsonParser();

		// an organization
		IIdType orgId = createOrganization(withIdentifier("https://example.com/ns", "123"));
		logAllResources();

		// and a bundle with a Patient and linked Encounter
		Patient patient = buildResource(
			Patient.class,
			withIdentifier("https://example.com/ns", "456"));
		Encounter encounter = buildResource(
			Encounter.class,
			withSubject("urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"),
			// that refers to the existing organization
			withReference("serviceProvider", orgId)
		);

		Bundle bundle = new BundleBuilder(myFhirContext)
			.addTransactionCreateEntry(patient, "urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea").andThen()
			.addTransactionCreateEntry(encounter).andThen()
			.getBundleTyped();

		// when processed
		// Warning: this is weird.
		// It works going through the server, but not through the dao without this parser dance.
		// The parser is stitching the resources into the references during parsing, and we need this to allow a server-assigned UUID.
		bundle = parser.parseResource(Bundle.class, parser.encodeResourceToString(bundle));
		Bundle outcomes = mySystemDao.transaction(mySrd, bundle);

		// then
		String patientId = outcomes.getEntry().get(0).getId();
		//
		Bundle search = myServer.getFhirClient().search().forResource("Encounter").where(Encounter.PATIENT.hasId(patientId)).and(Encounter.SERVICE_PROVIDER.hasId(orgId)).returnBundle(Bundle.class).execute();
		assertEquals(1, search.getEntry().size(), "we find the Encounter linked to the organization and the patient");
	}


	/**
	 * Searching for Organization via DAO with a SystemRequestDetails that has
	 * a blank tenant ID should work in PATIENT_ID partition mode (unnamed).
	 * The blank tenant ID should not bypass the interceptor chain and cause
	 * "Partition IDs have not been set".
	 */
	@Test
	public void testSearchOrganization_withBlankTenantId_shouldReturnDefaultPartition() {
		// Create an Organization in the default partition
		Organization org = new Organization();
		org.setId("Organization/ORG1");
		org.setName("Test Org");
		org.addIdentifier().setSystem("http://example.com").setValue("ORG1");
		myOrganizationDao.update(org, mySrd);

		// Search using a SystemRequestDetails with blank tenant ID
		SystemRequestDetails blankTenantRequest = new SystemRequestDetails();
		blankTenantRequest.setTenantId("");

		SearchParameterMap searchMap = new SearchParameterMap()
				.add(Organization.SP_IDENTIFIER, new TokenParam("http://example.com", "ORG1"))
				.setLoadSynchronous(true);

		// This should NOT throw "HAPI-2223: Partition IDs have not been set"
		IBundleProvider result = myOrganizationDao.search(searchMap, blankTenantRequest);
		assertThat(result.size()).isEqualTo(1);
	}


	@SafeVarargs
	static Consumer<Bundle> bundleAssert(int theExpectedSize, Consumer<Bundle> ...theOtherAssertions) {
		return theBundle -> {
			assertThat(theBundle.getEntry()).size().isEqualTo(theExpectedSize);
			for (Consumer<Bundle> theAssertion : theOtherAssertions) {
				theAssertion.accept(theBundle);
			}
		};
	}

	// -----------------------------------------------------------------------
	// Per-entry assertion infrastructure for testTransaction_allReferenceScenarios
	// -----------------------------------------------------------------------

	/**
	 * Expectation for a single transaction response entry (in input order).
	 * The two nullable partition fields encode three cases:
	 * <ul>
	 *   <li>{@code expectedPartition} (non-null) — exact partition id</li>
	 *   <li>{@code sameAsEntryIndex} (non-null) — must co-locate with that response entry</li>
	 *   <li>both null — "any compartment", partition must be {@code > 0}</li>
	 * </ul>
	 */
	record ExpectedEntry(
			String resourceType,
			StorageResponseCodeEnum outcome,
			Integer expectedPartition,
			Integer sameAsEntryIndex) {}

	/** Resource in the configured default partition (ALTERNATE_DEFAULT_ID = -1). */
	static ExpectedEntry inDefaultPartition(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, ALTERNATE_DEFAULT_ID, null);
	}

	/** Resource in the compartment of the patient whose id-part is {@code thePatientIdPart}. */
	static ExpectedEntry inCompartmentOf(String theType, StorageResponseCodeEnum theOutcome, String thePatientIdPart) {
		int partition = PatientIdPartitionInterceptor.defaultPartitionAlgorithm(thePatientIdPart);
		return new ExpectedEntry(theType, theOutcome, partition, null);
	}

	/** Resource must co-locate with the response entry at {@code theOtherEntryIndex}. */
	static ExpectedEntry inSamePartitionAsEntry(String theType, StorageResponseCodeEnum theOutcome, int theOtherEntryIndex) {
		return new ExpectedEntry(theType, theOutcome, null, theOtherEntryIndex);
	}

	/** Resource must be in some patient-compartment partition (partition {@code > 0}). */
	static ExpectedEntry inAnyCompartment(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, null, null);
	}

	/**
	 * Drives per-entry assertions for {@code testTransaction_allReferenceScenarios}.
	 * <ul>
	 *   <li>Entry count matches {@code theExpectedEntries.size()}</li>
	 *   <li>For each entry (in input order): resource type, HTTP status, OO code, partition</li>
	 * </ul>
	 */
	private void assertReferenceScenario(Bundle theResultBundle, List<ExpectedEntry> theExpectedEntries) {
		assertThat(theResultBundle.getEntry())
				.as("response entry count")
				.hasSize(theExpectedEntries.size());

		for (int i = 0; i < theExpectedEntries.size(); i++) {
			ExpectedEntry expected = theExpectedEntries.get(i);
			Bundle.BundleEntryResponseComponent response = theResultBundle.getEntry().get(i).getResponse();

			// Type: parse location to verify resource type
			String location = response.getLocation();
			assertThat(location).as("entry[%d] location must not be blank", i).isNotBlank();
			IIdType resourceId = new IdType(location).toUnqualifiedVersionless();
			assertThat(resourceId.getResourceType())
					.as("entry[%d] resource type (location=%s)", i, location)
					.isEqualTo(expected.resourceType());

			// HTTP status: created-class codes → 201, everything else → 200
			String expectedStatusPrefix = isCreatedOutcome(expected.outcome()) ? "201" : "200";
			assertThat(response.getStatus())
					.as("entry[%d] HTTP status for %s/%s", i, expected.resourceType(), expected.outcome())
					.startsWith(expectedStatusPrefix);

			// OperationOutcome code
			OperationOutcome oo = (OperationOutcome) response.getOutcome();
			assertThat(oo).as("entry[%d] must have an OperationOutcome", i).isNotNull();
			assertThat(oo.getIssue()).as("entry[%d] OO must have at least one issue", i).isNotEmpty();
			String actualCode = oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode();
			assertThat(actualCode)
					.as("entry[%d] OperationOutcome StorageResponseCode for %s", i, expected.resourceType())
					.isEqualTo(expected.outcome().name());

			// Partition
			if (expected.expectedPartition() != null) {
				assertResourceIsInPartition(expected.expectedPartition(), resourceId);
			} else if (expected.sameAsEntryIndex() != null) {
				String otherLocation =
						theResultBundle.getEntry().get(expected.sameAsEntryIndex()).getResponse().getLocation();
				IIdType otherId = new IdType(otherLocation).toUnqualifiedVersionless();
				assertResourceIsInPartition(getResourcePartition(otherId), resourceId);
			} else {
				// compartments span [0,14999] so we can't assert > 0; assert != default partition instead
				assertThat(getResourcePartition(resourceId))
						.as("entry[%d] (%s) must be in a patient-compartment partition, not the default", i, resourceId)
						.isNotEqualTo(ALTERNATE_DEFAULT_ID);
			}
		}
	}

	private static boolean isCreatedOutcome(StorageResponseCodeEnum theCode) {
		return switch (theCode) {
			case SUCCESSFUL_CREATE,
				 SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH,
				 SUCCESSFUL_UPDATE_AS_CREATE,
				 SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH -> true;
			default -> false;
		};
	}

	static List<Arguments> referenceScenarioSupplier() {
		return List.of(
			Arguments.of(
				"unconditionally created Patient | new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// Patient has no fullUrl → Task 2 assigns one → Synthea hack fires → creates with UUID id.
				// Task 4 corrects OO from SUCCESSFUL_UPDATE_AS_CREATE back to SUCCESSFUL_CREATE.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"conditionally created Patient | new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}
						]
					}
					""",
				// Patient has no fullUrl → Task 2 assigns one → Synthea hack fires → creates with UUID id.
				// Task 4 corrects OO from SUCCESSFUL_UPDATE_AS_CREATE back to SUCCESSFUL_CREATE.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"conditionally created Patient | existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}
						]
					}
					""",
				// Patient has no fullUrl → Task 2 assigns one → Synthea hack fires → creates with UUID id.
				// Task 4 corrects OO from SUCCESSFUL_UPDATE_AS_CREATE back to SUCCESSFUL_CREATE.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"create Observation | local reference to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient/pat1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Direct Patient/pat1 reference → Observation in pat1's compartment. No transformer involved.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"create Observation | placeholder reference to unconditional new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient has fullUrl → Synthea hack fires → UUID id assigned → Patient & Observation in same compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"create Observation | placeholder reference to conditional new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient conditional create: identNew doesn't exist → creates with server-assigned UUID.
				// Task 3 allPartitions fallback enables routing; actual create succeeds (UUID assigned before identifyForCreate).
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"create Observation | placeholder reference to unconditional new patient | reverse order",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
							    "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// Input order [Obs, Patient]; response preserves order.
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"create Observation | placeholder reference to conditional new patient | reverse order",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							},
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}
						]
					}
					""",
				// Patient conditional create: identNew doesn't exist → creates with server-assigned UUID.
				// Input order preserved in response: [0]=Observation, [1]=Patient.
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"create Observation | placeholder reference to conditional-create of existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
							    "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient conditional create: ident1=pat1 exists → NOP (200 OK).
				// Observation subject resolved to pat1 via PLACEHOLDER_TO_REFERENCE_KEY after NOP completes.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"create Observation with logical reference to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Inline match URL → transformer prepends synthetic conditional-create (pat1 exists → NOP).
				// 1 synthetic stripped; response has 1 entry. Observation in pat1's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"create Observation with logical reference to new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=new-sys|new-val" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Inline match URL → synthetic conditional-create for new-sys|new-val (doesn't exist → creates with UUID).
				// 1 synthetic stripped; response has 1 entry. Observation in the new patient's compartment.
				List.of(
					inAnyCompartment("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"conditional-update Observation with logical reference to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1"}
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obs1"}
							}
						]
					}
					""",
				// Inline match URL → synthetic for pat1 (NOP). 1 synthetic stripped.
				// Conditional PUT Observation: obs1 doesn't exist → creates new.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"Observation with logical reference to patient in bundle with redundant conditional create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obs1"}
							}
						]
					}
					""",
				// Transformer rewrites Obs subject (inline match URL) using Patient conditional-create entry's fullUrl.
				// Patient: NOP (ident1=pat1 exists). Obs: PUT no match → creates new.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"two Observations with logical references to two different existing patients | cross-partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs2"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident2" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Two inline match URLs → two synthetics prepended (both NOP: pat1 and pat2 exist). Both stripped.
				// obs1 → pat1's compartment; obs2 → pat2's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat2")
				)
			),
			Arguments.of(
				"Encounter + Observation with logical references to the same new patient | shared compartment, new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Encounter",
									"status" : "finished",
									"class" : {
										"system" : "http://terminology.hl7.org/CodeSystem/v3-ActCode",
										"code" : "AMB",
										"display" : "ambulatory"
									},
									"subject" : { "reference" : "Patient?identifier=old-sys|identChain" }
								},
								"request" : { "method" : "POST", "url" : "Encounter"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsChain"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|identChain" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Both inline match URLs → one shared synthetic (de-duplicated by transformer). identChain doesn't exist → creates with UUID.
				// 1 synthetic stripped; response has 2 entries. Both in the new patient's compartment.
				List.of(
					inAnyCompartment("Encounter", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"create Organization | non-compartment resource standalone | default partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "org1"} ],
									"name" : "Acme Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}
						]
					}
					""",
				// Organization is non-compartment → goes to default partition (-1 = ALTERNATE_DEFAULT_ID).
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"mixed compartment + non-compartment | Organization + Observation (logical ref to existing patient)",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "org-mixed"} ],
									"name" : "Mixed Bundle Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs-mixed"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Organization → default partition. Obs inline match URL → synthetic (pat1 NOP); 1 stripped.
				// Obs in pat1's compartment.
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"conditional-update Patient (matches existing) + Observation with logical ref to it | Option A on PUT-with-match-URL",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondUpdMatched"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Transformer rewrites Obs subject using Patient conditional-update entry's fullUrl (Option A).
				// Patient PUT matches pat1 → update (200). Obs in pat1's compartment.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"conditional-update Patient (no match, creates new) + Observation with logical ref to it",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "brand-new-cu"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|brand-new-cu"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondUpdNew"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|brand-new-cu" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient PUT: brand-new-cu doesn't exist → creates with server-assigned UUID. Obs references it.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"conditional-update Observation with patient ref in match URL | match URL contains Patient reference",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference" : "Patient/pat1" },
									"code" : { "coding" : [{ "system" : "http://loinc.org", "code" : "9999-9" }] }
								},
								"request" : { "method" : "PUT", "url" : "Observation?subject=Patient/pat1&code=http://loinc.org|9999-9"}
							}
						]
					}
					""",
				// Observation subject = Patient/pat1 (direct reference, no inline match URL).
				// No match found → creates new Observation in pat1's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"conditional-create Patient (new) + conditional-update Observation with logical ref to it | mixed verbs",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newCreate"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newCreate"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCC"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newCreate" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsCC"}
							}
						]
					}
					""",
				// Transformer rewrites Obs subject using Patient conditional-create entry's fullUrl.
				// Patient creates new (newCreate doesn't exist). Obs conditional PUT: obsCC doesn't exist → creates.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"two conditional-create Patients (distinct) + two Observations each referencing one | cross-partition + Option A",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newA"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newA"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newB"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newB"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsA"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newA" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsB"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newB" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Transformer rewrites ObsA/ObsB subjects using PatA/PatB fullUrls. Both patients created new.
				// All 4 entries remain in response. Cross-partition writes land in each patient's own compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1)
				)
			),
			Arguments.of(
				"two conditional-create Patients with the same identifier | duplicate conditional create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "duplicate"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|duplicate"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "duplicate"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|duplicate"}
							}
						]
					}
					""",
				// First entry creates a new patient (duplicate doesn't exist). Second entry matches it → NOP (200 OK).
				// Both response entries point to the same patient.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"[cell 7] create Observation | inline match URL ref to an explicit unconditional new patient | contrived",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c7" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC7" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|c7" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				// CONTRIVED: the transformer only indexes conditional-write entries, so the unconditional Patient is
				// NOT matched by the inline URL — a separate synthetic conditional-create for old-sys|c7 is prepended.
				// Intended: Observation co-locates with the (single) Patient. Observe whether a duplicate patient appears.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"[cell 11] conditional-update Observation | urn id-ref to an unconditional new patient in bundle",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:c1111111-1111-1111-1111-111111111111",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c11" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC11" } ],
									"subject" : { "reference" : "urn:uuid:c1111111-1111-1111-1111-111111111111" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC11" }
							}
						]
					}
					""",
				// Unconditional Patient → hack assigns an id and substitutes the urn ref → Observation routes to its compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"[cell 12] conditional-update Observation | urn id-ref to a conditional new patient in bundle",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:c1222222-2222-2222-2222-222222222222",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c12" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|c12" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC12" } ],
									"subject" : { "reference" : "urn:uuid:c1222222-2222-2222-2222-222222222222" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC12" }
							}
						]
					}
					""",
				// Conditional Patient gets no id at routing → Observation's urn ref can't resolve → 1326 (needs Task 3).
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"[cell 13] conditional-update Observation | inline match URL ref to an explicit unconditional new patient | contrived",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c13" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC13" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|c13" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC13" }
							}
						]
					}
					""",
				// CONTRIVED: same shape as cell 7 but the Observation is a conditional PUT. Observe.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"explicit-id PUT to an existing patient stays SUCCESSFUL_UPDATE (must not be reconciled to a create)",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat1",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat1"}
							}
						]
					}
					""",
				// Non-rewritten direct PUT-by-id: the OO reconciliation must leave it a plain update, not a create.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE, "pat1")
				)
			),
			Arguments.of(
				"explicit-id PUT with unchanged content stays a no-change update (must not be reconciled)",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat2",
									"identifier" : [ { "system" : "old-sys", "value" : "ident2"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat2"}
							}
						]
					}
					""",
				// Identical to the stored pat2: a no-change update; the reconciliation must preserve the no-change code.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE, "pat2")
				)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("referenceScenarioSupplier()")
	void testTransaction_allReferenceScenarios(String theComment, String theBundle, List<ExpectedEntry> theExpectedEntries) {
		// fixed setup
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		createPatient(
			withId("pat1"),
			withIdentifier("old-sys", "ident1"),
			withIdentifier("new-sys", "newId1")
		);
		// Second patient enables cross-partition scenarios in the supplier (e.g. one bundle, two patients).
		createPatient(
			withId("pat2"),
			withIdentifier("old-sys", "ident2")
		);

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);
		ourLog.info("Test case: {}", theComment);
		ourLog.info("Request bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(requestBundle));

		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);
		ourLog.info("Response bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resultBundle));

		assertNotNull(resultBundle);
		assertReferenceScenario(resultBundle, theExpectedEntries);
	}

	@Interceptor
	public class MyTransactionSplitInterceptor {

		@Hook(Pointcut.STORAGE_TRANSACTION_PRE_PARTITION)
		public TransactionPrePartitionResponse transactionPartition(IBaseBundle theInput) {
			List<IBaseBundle> bundles = new ArrayList<>();
			FhirTerser terser = myFhirContext.newTerser();

			Bundle patientBundle = new Bundle();
			patientBundle.setType(Bundle.BundleType.TRANSACTION);
			Bundle nonPatientBundle = new Bundle();
			nonPatientBundle.setType(Bundle.BundleType.TRANSACTION);

			List<Bundle.BundleEntryComponent> entries = terser.getValues(theInput, "entry", Bundle.BundleEntryComponent.class);
			for (Bundle.BundleEntryComponent entry : entries) {
				Resource resource = entry.getResource();
				switch (resource.getResourceType()) {
					case Medication, Practitioner, Location -> {
						nonPatientBundle.addEntry(entry);
					}
					default -> {
						patientBundle.addEntry(entry);
					}
				}
			}

			if (!nonPatientBundle.getEntry().isEmpty()) {
				bundles.add(nonPatientBundle);
			}
			if (!patientBundle.getEntry().isEmpty()) {
				bundles.add(patientBundle);
			}

			return new TransactionPrePartitionResponse().setSplitBundles(bundles);
		}

	}

	@Configuration
	public static class TestConfig{

		@Bean
		public IMdmSettings mdmSettings(){
			MdmSettings settings = new MdmSettings(null);
			settings.setSearchAllPartitionForMatch(true);
			return settings;
		}
	}

}
