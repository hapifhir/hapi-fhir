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
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.ExpectedEntry;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
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
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceEnabledByTypeInterceptor;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirPatchBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
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
import org.hl7.fhir.r4.model.Coding;
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
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inAnyCompartment;
import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onAllThreads;
import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
		myPartitionSettings.setAllPartitionSearchSupported(defaultSettings.isAllPartitionSearchSupported());

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

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testSearchChainedValue_noResolvedReference(boolean theSupportsAllPartitionSearch) {
		myPartitionSettings.setAllPartitionSearchSupported(theSupportsAllPartitionSearch);

		createPatientA(); // Patient/A has no identifier, so the chained identifier below matches nothing
		createObservationB();

		SearchParameterMap map = SearchParameterMap.newSynchronous()
				.add("subject", new ReferenceParam("identifier", "http://foo|123"));

		if (theSupportsAllPartitionSearch) {
			// Can fan out: the search runs across all partitions, matches no Patient, and returns an empty bundle.
			IBundleProvider outcome = myObservationDao.search(map, mySrd);
			assertEquals(0, outcome.size());
		} else {
			// Cannot fan out: the unresolved chained parameter is rejected up front, before any search runs.
			MethodNotAllowedException e =
					assertThrows(MethodNotAllowedException.class, () -> myObservationDao.search(map, mySrd));
			assertEquals(
					"HAPI-2928: Could not resolve chained parameter(s) [identifier] on parameter subject. Consider adding a direct Patient reference to your search (?subject=Patient/abc&subject.identifier=...)",
					e.getMessage());
		}
	}

	@Test
	void testSearchChainedValue_resolvedByIdentifier_searchesAllPartitionsAndFinds() {
		// Default all-partition search (isAllPartitionSearchSupported() == true): a chained
		// subject.identifier with no direct reference searches all partitions and resolves to the
		// matching Patient's Observation.
		myPartitionSettings.setAllPartitionSearchSupported(true);

		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());
		IIdType obsId = createObservation(withSubject("Patient/A"), withStatus("final"));

		myTestDaoSearch.assertSearchFinds(
				"find Observation by patient identifier chain",
				"Observation?subject.identifier=http://acme.org/mrn|PT00062",
				obsId);
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


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testTransaction_CreateObservationWithPatientInlineMatchUrl(boolean theSupportsAllPartitionSearch) {
		myPartitionSettings.setAllPartitionSearchSupported(theSupportsAllPartitionSearch);

		// Patient already exists, identified by a business (MRN) identifier
		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://acme.org/mrn|PT00062");
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(obs);

		if (theSupportsAllPartitionSearch) {
			Bundle response = mySystemDao.transaction(mySrd, bb.getBundleTyped());

			// The inline match URL was resolved to a literal Patient/A reference before storage
			IIdType obsId = new IdType(response.getEntry().get(0).getResponse().getLocation()).toUnqualifiedVersionless();
			Observation stored = myObservationDao.read(obsId, mySrd);
			assertEquals("Patient/A", stored.getSubject().getReference());
		} else {
			MethodNotAllowedException e = assertThrows(
					MethodNotAllowedException.class, () -> mySystemDao.transaction(mySrd, bb.getBundleTyped()));
			assertEquals(
					"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment",
					e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testTransaction_UpdateObservationWithPatientInlineMatchUrl(boolean theSupportsAllPartitionSearch) {
		myPartitionSettings.setAllPartitionSearchSupported(theSupportsAllPartitionSearch);

		// Patient and Observation already exist, linked by a literal Patient/A reference
		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());
		IIdType obsId = createObservation(withId("O"), withSubject("Patient/A"), withStatus("final"));

		// Update the Observation, re-expressing the same subject as an inline match URL
		Observation update = new Observation();
		update.setId(obsId.toUnqualifiedVersionless().getIdPart());
		update.setStatus(Observation.ObservationStatus.AMENDED);
		update.getSubject().setReference("Patient?identifier=http://acme.org/mrn|PT00062");
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(update);

		if (theSupportsAllPartitionSearch) {
			mySystemDao.transaction(mySrd, bb.getBundleTyped());

			// The inline match URL was resolved back to the literal Patient/A reference
			Observation stored = myObservationDao.read(obsId.toUnqualifiedVersionless(), mySrd);
			assertEquals("Patient/A", stored.getSubject().getReference());
			assertEquals(Observation.ObservationStatus.AMENDED, stored.getStatus());
		} else {
			MethodNotAllowedException e = assertThrows(
					MethodNotAllowedException.class, () -> mySystemDao.transaction(mySrd, bb.getBundleTyped()));
			assertEquals(
					"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment",
					e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testTransaction_CreateObservationWithPatientInlineMatchUrl_noMatch_fails(boolean theSupportsAllPartitionSearch) {
		myPartitionSettings.setAllPartitionSearchSupported(theSupportsAllPartitionSearch);

		// No Patient carries this identifier
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://acme.org/mrn|NO_SUCH_PATIENT");
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(obs);

		// With no matching Patient the inline match URL is never resolved, so the reference stays unroutable and
		// per-entry partition determination rejects the resource with HAPI-1326 — under both configs.
		MethodNotAllowedException e = assertThrows(
				MethodNotAllowedException.class, () -> mySystemDao.transaction(mySrd, bb.getBundleTyped()));
		assertEquals(
				"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment",
				e.getMessage());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testTransaction_CreateObservationWithPatientInlineMatchUrl_multipleMatches_fails(boolean theSupportsAllPartitionSearch) {
		myPartitionSettings.setAllPartitionSearchSupported(theSupportsAllPartitionSearch);

		// Two Patients carry the same identifier
		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());
		createPatient(withId("B"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://acme.org/mrn|PT00062");
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(obs);

		if (theSupportsAllPartitionSearch) {
			// The pre-fetch resolves the inline match URL across partitions and rejects the ambiguous
			// match with HAPI-2207 before any entry is written.
			PreconditionFailedException e = assertThrows(
					PreconditionFailedException.class, () -> mySystemDao.transaction(mySrd, bb.getBundleTyped()));
			assertEquals(
					Msg.code(2207)
							+ "Invalid match URL \"Patient?identifier=http://acme.org/mrn|PT00062\" - Multiple resources match this search",
					e.getMessage());
		} else {
			// When all-partition search is not supported, the inline match URL cannot be routed to a Patient
			// compartment and the resource is rejected with HAPI-1326.
			MethodNotAllowedException e = assertThrows(
					MethodNotAllowedException.class, () -> mySystemDao.transaction(mySrd, bb.getBundleTyped()));
			assertEquals(
					"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment",
					e.getMessage());
		}
	}

	@Test
	void testTransaction_ConditionalUpdatePatientByIdentifier_differentBodyId_rejectedWith2279_noDuplicate() {
		myPartitionSettings.setAllPartitionSearchSupported(true);

		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());

		Patient update = new Patient();
		update.setId("B");
		update.addIdentifier().setSystem("http://acme.org/mrn").setValue("PT00062");
		update.setActive(false);
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(update).conditional("Patient?identifier=http://acme.org/mrn|PT00062");

		assertThatThrownBy(() -> mySystemDao.transaction(mySrd, bb.getBundleTyped()))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage(
				"HAPI-2279: Failed to UPDATE resource with match URL \"Patient?identifier=http://acme.org/mrn|PT00062\" because the matching resource does not match the provided ID");

		// Only the original Patient A carries the MRN — no duplicate was created.
		assertThat(myTestDaoSearch.searchForIds("Patient?identifier=http://acme.org/mrn|PT00062"))
			.containsExactly("A");
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testTransaction_ConditionalUpdatePatientByIdentifier_updatesInPlace(boolean theBodyCarriesMatchingId) {
		myPartitionSettings.setAllPartitionSearchSupported(true);

		createPatient(withId("A"), withIdentifier("http://acme.org/mrn", "PT00062"), withActiveTrue());

		Patient update = new Patient();
		if (theBodyCarriesMatchingId) {
			update.setId("A");
		}
		update.addIdentifier().setSystem("http://acme.org/mrn").setValue("PT00062");
		update.setActive(false);
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(update).conditional("Patient?identifier=http://acme.org/mrn|PT00062");

		mySystemDao.transaction(mySrd, bb.getBundleTyped());

		assertThat(myTestDaoSearch.searchForIds("Patient?identifier=http://acme.org/mrn|PT00062"))
			.containsExactly("A");
		assertThat(myPatientDao.read(new IdType("Patient/A"), mySrd).getActive()).isFalse();
	}

	@Test
	public void testTransaction_SplitAncillaryAndPatient_NewTransactionPerPartition() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
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
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		// One commit per sub-bundle: the compartment writes join their sub-bundle's all-partitions
		// transaction rather than each committing a REQUIRES_NEW transaction of its own.
		assertEquals(2, myCaptureQueriesListener.countCommits());
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
		// This bundle contains 517 resources. The split path pays one extra select: references to
		// resources committed by an earlier sub-bundle must be identity-checked in the database.
		assertEquals(theSplitTransaction ? 27 : 26, myCaptureQueriesListener.countSelectQueries());
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
	// (the ExpectedEntry model + factories live in PatientIdPartitionReferenceScenarios)
	// -----------------------------------------------------------------------

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

			// OperationOutcome: the primary issue must be indistinguishable from a natively produced one
			OperationOutcome oo = (OperationOutcome) response.getOutcome();
			assertThat(oo).as("entry[%d] must have an OperationOutcome", i).isNotNull();
			assertThat(oo.getIssue()).as("entry[%d] OO must have at least one issue", i).isNotEmpty();
			OperationOutcome.OperationOutcomeIssueComponent issue = oo.getIssueFirstRep();
			assertThat(issue.getSeverity())
					.as("entry[%d] issue severity", i)
					.isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getCode())
					.as("entry[%d] issue code", i)
					.isEqualTo(OperationOutcome.IssueType.INFORMATIONAL);
			Coding coding = issue.getDetails().getCodingFirstRep();
			assertThat(coding.getSystem())
					.as("entry[%d] StorageResponseCode system", i)
					.isEqualTo(expected.outcome().getSystem());
			assertThat(coding.getCode())
					.as("entry[%d] OperationOutcome StorageResponseCode for %s", i, expected.resourceType())
					.isEqualTo(expected.outcome().name());
			assertThat(coding.getDisplay())
					.as("entry[%d] StorageResponseCode display", i)
					.isEqualTo(expected.outcome().getDisplay());
			assertThat(issue.getDiagnostics())
					.as("entry[%d] diagnostics must name the versioned id, like a native outcome", i)
					.contains(new IdType(location).toUnqualified().getValue());
			assertThat(issue.getDiagnostics())
					.as("entry[%d] diagnostics must not leak unformatted message arguments", i)
					.doesNotContain("{0}")
					.doesNotContain("{1}");

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

	@ParameterizedTest
	@ArgumentsSource(PatientIdPartitionReferenceScenarios.class)
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

	/**
	 * Two conditional creates with the same match URL in one bundle: the duplicate is consolidated onto the first
	 * entry and silently dropped from the response — the canonical semantics of
	 * {@code BaseTransactionProcessor#consolidateDuplicateConditionals} (cf.
	 * {@code FhirSystemDaoR4Test#testTransactionWithDuplicateConditionalCreates}). Exactly one patient is created.
	 */
	@Test
	void testTransaction_duplicateConditionalCreateInBundle_dedup() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		String bundle =
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
				""";

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundle);
		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);

		assertReferenceScenario(
				resultBundle,
				List.of(inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)));
		assertPatientCountInDatabase(1);
	}

	/**
	 * Same as {@link #testTransaction_duplicateConditionalCreateInBundle_dedup} but with conditional PUTs: the
	 * duplicate consolidates onto the first entry, which creates the patient (update-no-conditional-match).
	 */
	@Test
	void testTransaction_duplicateConditionalUpdateInBundle_dedup() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		String bundle =
				"""
				{ "resourceType" : "Bundle", "type" : "transaction",
					"entry" : [
						{
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "dup-put"} ]
							},
							"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|dup-put"}
						}, {
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "dup-put"} ]
							},
							"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|dup-put"}
						}
					]
				}
				""";

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundle);
		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);

		assertReferenceScenario(
				resultBundle,
				List.of(inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH)));
		assertPatientCountInDatabase(1);

		// The restored message fills the match-URL slot the native message leaves as a literal "{1}"
		OperationOutcome oo =
				(OperationOutcome) resultBundle.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics())
				.contains("Patient?identifier=old-sys|dup-put")
				.doesNotContain("{1}");
	}

	/**
	 * A conditional create and a conditional update sharing one match URL consolidate too (both are rewritten to
	 * the same conditional PUT), keeping the first entry's outcome. Stock HAPI would not consolidate across verbs;
	 * in patient-id partition mode both target the same logical patient, so one create is the correct result.
	 */
	@Test
	void testTransaction_mixedDuplicateConditionalWritesInBundle_dedup() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		String bundle =
				"""
				{ "resourceType" : "Bundle", "type" : "transaction",
					"entry" : [
						{
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "dup-mixed"} ]
							},
							"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|dup-mixed"}
						}, {
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "dup-mixed"} ]
							},
							"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|dup-mixed"}
						}
					]
				}
				""";

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundle);
		Bundle resultBundle = mySystemDao.transaction(mySrd, requestBundle);

		assertReferenceScenario(
				resultBundle,
				List.of(inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)));
		assertPatientCountInDatabase(1);
	}

	/**
	 * A NOT_FOUND-conditional patient create must leave an HFJ_RES_SEARCH_URL row for its match URL — the unique
	 * constraint on that row is what makes a concurrent transaction creating the same conditional URL collide
	 * instead of creating a duplicate patient (see {@code ResourceSearchUrlSvc#enforceMatchUrlResourceUniqueness}).
	 */
	@Test
	void testTransaction_conditionalCreatePatient_leavesSearchUrlRowForConcurrencyGuard() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		String bundle =
				"""
				{ "resourceType" : "Bundle", "type" : "transaction",
					"entry" : [
						{
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "race-guard"} ]
							},
							"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|race-guard"}
						}
					]
				}
				""";

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundle);
		mySystemDao.transaction(mySrd, requestBundle);

		List<String> searchUrls = runInTransaction(() -> myResourceSearchUrlDao.findAll().stream()
				.map(ResourceSearchUrlEntity::getSearchUrl)
				.toList());
		assertThat(searchUrls)
				.as("conditional-create concurrency guard row")
				.hasSize(1)
				.allSatisfy(url -> assertThat(url).startsWith("Patient?identifier="));
	}

	@Test
	void testTransaction_rewrittenPatientOutcomeKeepsAutoCreatedPlaceholderIssue() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		String bundle =
				"""
				{ "resourceType" : "Bundle", "type" : "transaction",
					"entry" : [
						{
							"resource" : {
								"resourceType" : "Patient",
								"identifier" : [ { "system" : "old-sys", "value" : "placeholder-keeper"} ],
								"managingOrganization" : { "reference" : "Organization/auto-created-org" }
							},
							"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|placeholder-keeper"}
						}
					]
				}
				""";

		Bundle requestBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundle);
		Bundle output = mySystemDao.transaction(mySrd, requestBundle);

		OperationOutcome oo =
				(OperationOutcome) output.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode())
				.isEqualTo(StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH.name());

		// The native issue reporting the auto-created placeholder Organization must survive the restore
		assertThat(oo.getIssue()).as("restored outcome must keep the placeholder issue").hasSize(2);
		OperationOutcome.OperationOutcomeIssueComponent placeholderIssue =
				oo.getIssue().get(1);
		assertThat(placeholderIssue.getDetails().getCodingFirstRep().getCode())
				.isEqualTo(StorageResponseCodeEnum.AUTOMATICALLY_CREATED_PLACEHOLDER_RESOURCE.name());
		IdType placeholderId = (IdType) placeholderIssue
				.getExtensionByUrl(HapiExtensions.EXTENSION_PLACEHOLDER_ID)
				.getValue();
		assertThat(placeholderId.getValue()).contains("Organization/auto-created-org");
	}

	private void assertPatientCountInDatabase(int theExpectedCount) {
		List<String> patientIds = runInTransaction(() -> myResourceTableDao.findAll().stream()
				.filter(t -> "Patient".equals(t.getResourceType()))
				.map(t -> t.getIdDt().toUnqualifiedVersionless().getValue())
				.toList());
		assertThat(patientIds).as("patients in database").hasSize(theExpectedCount);
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
