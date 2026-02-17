package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.TransactionPrePartitionResponse;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
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
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
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
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
@Import(PatientIdPartitionInterceptorR4Test.TestConfig.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PatientIdPartitionInterceptorR4Test extends BaseResourceProviderR4Test {
	public static final int ALTERNATE_DEFAULT_ID = -1;

	@Autowired
	private HapiTransactionService myTransactionService;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
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

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		List<SqlQuery> selectQueriesForCurrentThread = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		assertEquals(2, selectQueriesForCurrentThread.size());
		assertThat(selectQueriesForCurrentThread.get(0).getSql(false, false)).contains("PARTITION_ID=?");
		assertThat(selectQueriesForCurrentThread.get(1).getSql(false, false)).doesNotContain("PARTITION_ID=");
		assertThat(selectQueriesForCurrentThread.get(1).getSql(false, false)).doesNotContain("PARTITION_ID in");
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
	public void testSearchObservation_ChainedSubjectParameter() {
		createPatientA();
		createObservationB();

		// Multiple ANDs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
					.add("subject", new ReferenceParam("identifier", "http://patient|1"))
				, mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1322) + "The parameter subject.identifier is not supported in patient compartment mode", e.getMessage());
		}

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
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1324) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}

		// Multiple ORs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
				.add(
					"subject", new ReferenceOrListParam().add(new ReferenceParam("Patient/A")).add(new ReferenceParam("Patient/B"))
				), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1324) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}
	}

	@Test
	public void testSearchObservation_ChainedValue() {
		createPatientA();
		createObservationB();

		// Chain
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous().add("subject", new ReferenceParam("identifier", "http://foo|123")), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1322) + "The parameter subject.identifier is not supported in patient compartment mode", e.getMessage());
		}

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
	public void testHistory_Instance() {
		Organization org = createOrganizationC();
		org.setName("name 2");

		logAllResources();

		myOrganizationDao.update(org, newSrd());

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myOrganizationDao.history(new IdType("Organization/C"), null, null, null, mySrd);
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, outcome.size());
		assertThat(myCaptureQueriesListener.getSelectQueries()).hasSize(2);
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false)).contains("PARTITION_ID=?");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false)).contains("PARTITION_ID=?");
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
		IBundleProvider result = myExplanationOfBenefitDao.search(map);
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
		runInTransaction(() -> {
			ResourceTable entity = myResourceTableDao.findByTypeAndFhirId(theResourceId.getResourceType(), theResourceId.getIdPart()).orElseThrow();
			assertEquals(theExpectedPartitionId, entity.getPartitionId().getPartitionId());
		});
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
