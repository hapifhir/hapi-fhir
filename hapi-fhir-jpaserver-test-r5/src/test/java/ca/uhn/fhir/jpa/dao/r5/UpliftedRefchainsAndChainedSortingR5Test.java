package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Composition;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = { TestHSearchAddInConfig.NoFT.class, TestDaoSearch.Config.class })
@SuppressWarnings({"Duplicates"})
public class UpliftedRefchainsAndChainedSortingR5Test extends BaseJpaR5Test {
	public static final String PRACTITIONER_PR1 = "Practitioner/PR1";
	public static final String ENCOUNTER_E1 = "Encounter/E1";
	public static final String PATIENT_P1 = "Patient/P1";
	public static final String PATIENT_P2 = "Patient/P2";
	public static final String PATIENT_P3 = "Patient/P3";
	public static final String ENCOUNTER_E2 = "Encounter/E2";
	public static final String ENCOUNTER_E3 = "Encounter/E3";
	public static final String ORGANIZATION_O1 = "Organization/O1";
	@Autowired
	protected TestDaoSearch myTestDaoSearch;

	@Override
	@BeforeEach
	public void beforeResetConfig() {
		super.beforeResetConfig();

		myStorageSettings.setIndexOnUpliftedRefchains(true);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Override
	@AfterEach
	public void afterCleanupDao() {
		super.afterCleanupDao();
		myStorageSettings.setIndexOnUpliftedRefchains(new StorageSettings().isIndexOnUpliftedRefchains());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@Test
	public void testCreate() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();

		// Test

		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);

		// Verify
		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}

	@Test
	public void testCreate_NoUpliftRefchainsDefined() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();

		// Test

		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);

		// Verify
		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).isEmpty();
		});
	}

	/**
	 * Index for
	 *   [base]/Bundle?composition.type=foo
	 * Using an uplifted refchain on the Bundle:composition SearchParameter
	 */
	@Test
	public void testCreate_BundleWithComposition_UsingSimpleUplift() {
		// Setup

		RuntimeSearchParam subjectSp = mySearchParamRegistry.getRuntimeSearchParam("Bundle", "composition");
		SearchParameter sp = new SearchParameter();
		Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType("type"));

		sp.setId(subjectSp.getId());
		sp.setCode(subjectSp.getName());
		sp.setName(subjectSp.getName());
		sp.setUrl(subjectSp.getUri());
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Bundle.entry[0].resource.as(Composition)");
		subjectSp.getBase().forEach(t->sp.addBase(Enumerations.VersionIndependentResourceTypesAll.fromCode(t)));
		subjectSp.getTargets().forEach(t->sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.fromCode(t)));
		ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.create(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		// Test

		Composition composition = new Composition();
		composition.getType().addCoding().setSystem("http://foo").setCode("bar");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		myBundleDao.create(bundle, mySrd);

		// Verify
		runInTransaction(() -> {
			logAllTokenIndexes();

			List<String> params = myResourceIndexedSearchParamTokenDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getSystem() + "|" + t.getValue())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("composition.type http://foo|bar");
		});
	}

	@Test
	public void testCreate_InvalidTarget() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		// Test
		try {
			createEncounter(ENCOUNTER_E1, PATIENT_P1);
			fail();
		} catch (ResourceNotFoundException e) {

			// Verify
			assertThat(e.getMessage()).contains("Resource Patient/P1 is not known");
		}

	}

	@Test
	public void testCreate_InTransaction_InvalidPlaceholderReferenceTarget() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, IdType.newRandomUuid().getValue()));
		Bundle requestBundle = bb.getBundleTyped();

		try {
			mySystemDao.transaction(mySrd, requestBundle);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Unable to satisfy placeholder ID");
		}

	}


	@Test
	public void testCreate_InTransaction_SourceAndTarget() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newPatientP1_HomerSimpson());
		bb.addTransactionUpdateEntry(newPatientP2_MargeSimpson());
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, PATIENT_P1));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, PATIENT_P2));
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}

	@Test
	public void testCreate_InTransaction_TargetCreated_RefsUsePlaceholderIds() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		// Put the creates second to ensure that order doesn't matter
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		bb.addTransactionCreateEntry(newPatientP1_HomerSimpson().setId(p1Id));
		bb.addTransactionCreateEntry(newPatientP2_MargeSimpson().setId(p2Id));
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}


	@Test
	public void testCreate_InTransaction_TargetConditionalUpdated_NotAlreadyExisting() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		bb.addTransactionUpdateEntry(newPatientP1_HomerSimpson().setId(p1Id)).conditional("Patient?identifier=http://system|200");
		bb.addTransactionUpdateEntry(newPatientP2_MargeSimpson().setId(p2Id)).conditional("Patient?identifier=http://system|300");
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}

	@Test
	public void testCreate_InTransaction_TargetConditionalUpdated_AlreadyExisting() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();
		myPatientDao.create(new Patient().addIdentifier(new Identifier().setSystem("http://system").setValue("200")), mySrd);
		myPatientDao.create(new Patient().addIdentifier(new Identifier().setSystem("http://system").setValue("300")), mySrd);

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		bb.addTransactionUpdateEntry(newPatientP1_HomerSimpson().setId(p1Id)).conditional("Patient?identifier=http://system|200");
		bb.addTransactionUpdateEntry(newPatientP2_MargeSimpson().setId(p2Id)).conditional("Patient?identifier=http://system|300");
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(9, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}

	@Test
	public void testCreate_InTransaction_TargetConditionalCreatedNotAlreadyExisting_RefsUsePlaceholderIds() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		// Put the creates second to ensure that order doesn't matter
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		bb.addTransactionCreateEntry(newPatientP1_HomerSimpson().setId(p1Id)).conditional("identifier=http://system|200");
		bb.addTransactionCreateEntry(newPatientP2_MargeSimpson().setId(p2Id)).conditional("identifier=http://system|300");

		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}


	@Test
	public void testCreate_InTransaction_TargetConditionalCreatedAlreadyExisting_RefsUsePlaceholderIds() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		// Put the creates second to ensure that order doesn't matter
		// Also, patients being conditionally created here don't have names, which
		// ensures we pull the names in the uplift params from the pre-existing
		// resources that do have names.
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		bb.addTransactionCreateEntry(new Patient().addIdentifier(new Identifier().setSystem("http://system").setValue("200")).setId(p1Id)).conditional("identifier=http://system|200");
		bb.addTransactionCreateEntry(new Patient().addIdentifier(new Identifier().setSystem("http://system").setValue("300")).setId(p2Id)).conditional("identifier=http://system|300");

		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(10, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("subject.name Homer", "subject.name Simpson", "subject.name Marge", "subject.name Simpson");
		});
	}

	@Test
	public void testSearch_WithoutUpliftRefchain_SingleChain() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous("subject", new ReferenceParam("name", "homer"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).containsExactly(ENCOUNTER_E1);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(2);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(0);
	}

	@Test
	public void testSearch_UsingUpliftRefchain_SingleChain() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous("subject", new ReferenceParam("name", "homer"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).containsExactly(ENCOUNTER_E1);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(0);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(0);
	}

	/**
	 * Only single chains are supported. Should fall back to a normal
	 * search otherwise.
	 */
	@Test
	public void testSearch_UsingUpliftRefchain_DoubleChain() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		createOrganizationO1_SpringfieldHospital();
		createPractitionerPr1_BarneyGumble();
		createPatientP1_HomerSimpson(ORGANIZATION_O1);
		createPatientP2_MargeSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous("subject", new ReferenceParam("organization.name", "springfield"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).containsExactly(ENCOUNTER_E1);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(2);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(0);
	}

	@Test
	public void testSearch_SortOnUpliftRefchain_SingleChain() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("subject.name"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(0);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(0);
	}

	@Test
	public void testSearch_SortOnUpliftRefchain_SingleChain_Qualified() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("Patient:subject.name"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(0);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(0);
	}

	@Test
	public void testSearch_SortWithoutUpliftRefchain_SingleChain_String() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);
		logAllResourceLinks();
		logAllStringIndexes("name");

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("patient.name"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(0);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(1);
	}

	@Test
	public void testSearch_SortWithoutUpliftRefchain_SingleChain_String_AlsoIncludesSearch() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);
		logAllResourceLinks();
		logAllStringIndexes("name");

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add("patient", new ReferenceParam("name", "Simpson"))
			.setSort(new SortSpec("patient.name"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(2);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(2);
	}

	@Test
	public void testSearch_SortWithoutUpliftRefchain_SingleChain_Token() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);
		logAllResourceLinks();
		logAllStringIndexes("name");

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("patient.identifier"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_TOKEN")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(1);
	}

	@Test
	public void testSearch_SortWithoutUpliftRefchain_SingleChain_Date() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);
		logAllResourceLinks();
		logAllStringIndexes("name");

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("patient.birthdate"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_DATE")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(1);
	}

	@Test
	public void testSearch_SortWithoutUpliftRefchain_SingleChain_MultiTargetReference_Qualified() {
		// Setup

		createPractitionerPr1_BarneyGumble();
		// Add these with given names out of order for sorting
		createPatientP1_HomerSimpson();
		createPatientP2_MargeSimpson();
		createPatientP3_AbacusSimpson();
		createEncounter(ENCOUNTER_E1, PATIENT_P1);
		createEncounter(ENCOUNTER_E2, PATIENT_P2);
		createEncounter(ENCOUNTER_E3, PATIENT_P3);
		logAllResourceLinks();
		logAllStringIndexes("name");

		// Test

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.setSort(new SortSpec("Patient:subject.name"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(actual).as(actual.toString()).containsExactly(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2);
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		String querySql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(countMatches(querySql, "HFJ_SPIDX_STRING")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HASH_NORM_PREFIX")).as(querySql).isEqualTo(0);
		assertThat(countMatches(querySql, "HASH_IDENTITY")).as(querySql).isEqualTo(1);
		assertThat(countMatches(querySql, "HFJ_RES_LINK")).as(querySql).isEqualTo(1);
	}


	@Test
	void testChainedSortWithNulls() {
		final IIdType practitionerId1 = createPractitioner(withFamily("Chan"));
		final IIdType practitionerId2 = createPractitioner(withFamily("Jones"));

		final String id1 = createPatient(withFamily("Smithy")).getIdPart();
		final String id2 = createPatient(withFamily("Smithwick"),
			withReference("generalPractitioner", practitionerId2)).getIdPart();
		final String id3 = createPatient(
			withFamily("Smith"),
			withReference("generalPractitioner", practitionerId1)).getIdPart();


		final IBundleProvider iBundleProvider = myTestDaoSearch.searchForBundleProvider("Patient?_total=ACCURATE&_sort=Practitioner:general-practitioner.family");
		final List<IBaseResource> allResources = iBundleProvider.getAllResources();
		assertEquals(3, iBundleProvider.size());
		assertEquals(3, allResources.size());

		final List<String> actualIds = allResources.stream().map(IBaseResource::getIdElement).map(IIdType::getIdPart).toList();
		assertTrue(actualIds.containsAll(List.of(id1, id2, id3)));
	}

	@Test
	void testChainedReverseStringSort() {
		final IIdType practitionerId = createPractitioner(withFamily("Jones"));

		final String id1 = createPatient(withFamily("Smithy")).getIdPart();
		final String id2 = createPatient(withFamily("Smithwick")).getIdPart();
		final String id3 = createPatient(
			withFamily("Smith"),
			withReference("generalPractitioner", practitionerId)).getIdPart();

		final IBundleProvider iBundleProvider = myTestDaoSearch.searchForBundleProvider("Patient?_total=ACCURATE&_sort=-Practitioner:general-practitioner.family");
		assertEquals(3, iBundleProvider.size());

		final List<IBaseResource> allResources = iBundleProvider.getAllResources();
		final List<String> actualIds = allResources.stream().map(IBaseResource::getIdElement).map(IIdType::getIdPart).toList();
		assertTrue(actualIds.containsAll(List.of(id3, id2, id1)));
	}

	/**
	 * Observation:focus is a Reference(Any) so it can't be used in a sort chain because
	 * this would be horribly, horribly inefficient.
	 */
	@Test
	public void testSearch_SortWithUnsupportedExpression_AnyTargetReference_Unqualified() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("focus.name"));
			myObservationDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Unable to sort on a chained parameter from 'focus' as this parameter has multiple target types. Please specify the target type.");
		}

	}

	/**
	 * Observation:date isn't a Reference so it can't be used in a chained expression
	 */
	@Test
	public void testSearch_SortWithUnsupportedExpression_SingleChain_NonReferenceType() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("date.name"));
			myObservationDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid chain, date is not a reference SearchParameter");
		}

	}

	@Test
	public void testSearch_SortWithUnsupportedExpression_SingleChain_MultiTargetReference_Unqualified() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("subject.name"));
			myCaptureQueriesListener.clear();
			myEncounterDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Unable to sort on a chained parameter from 'subject' as this parameter has multiple target types. Please specify the target type");

		}
	}

	@Test
	public void testSearch_SortWithUnsupportedExpression_ThreeLevelChains() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("patient.organization.name"));
			myCaptureQueriesListener.clear();
			myEncounterDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Invalid _sort expression, can not chain more than once in a sort expression: patient.organization.name");

		}
	}

	@Test
	public void testSearch_SortWithUnsupportedExpression_UnknownFirstPart() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("foo.name"));
			myCaptureQueriesListener.clear();
			myEncounterDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Unknown _sort parameter value \"foo\" for resource type \"Encounter\"");

		}
	}

	@Test
	public void testSearch_SortWithUnsupportedExpression_UnknownSecondPart() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("patient.foo"));
			myCaptureQueriesListener.clear();
			myEncounterDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Unknown _sort parameter value \"foo\" for resource type \"Patient\"");

		}
	}

	@Test
	public void testSearch_SortWithUnsupportedExpression_UnsupportedTargetType() {

		// Test

		try {
			SearchParameterMap map = SearchParameterMap
				.newSynchronous()
				.setSort(new SortSpec("result.value-quantity"));
			myCaptureQueriesListener.clear();
			myDiagnosticReportDao.search(map, mySrd);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			assertThat(e.getMessage()).contains("Unable to sort on a chained parameter result.value-quantity as this parameter. Can not sort on chains of target type: QUANTITY");

		}
	}

	private void createOrganizationO1_SpringfieldHospital() {
		Organization org = new Organization();
		org.setId(ORGANIZATION_O1);
		org.setName("Springfield Hospital");
		myOrganizationDao.update(org, mySrd);
	}

	private void createEncounter(String theEncounterId, String theSubjectReference) {
		Encounter e1 = newEncounter(theEncounterId, theSubjectReference);
		myEncounterDao.update(e1, mySrd);
	}

	private void createPatientP1_HomerSimpson() {
		createPatientP1_HomerSimpson(null);
	}

	private void createPatientP1_HomerSimpson(String theOrganizationId) {
		Patient p1 = newPatientP1_HomerSimpson(theOrganizationId);
		myPatientDao.update(p1, mySrd);
	}

	private void createPatientP2_MargeSimpson() {
		Patient p2 = newPatientP2_MargeSimpson();
		myPatientDao.update(p2, mySrd);
	}

	private void createPatientP3_AbacusSimpson() {
		Patient p3 = new Patient();
		p3.setId(PATIENT_P3);
		p3.addIdentifier().setSystem("http://system").setValue("100");
		p3.addName().setFamily("Simpson").addGiven("Abacus");
		p3.setBirthDateElement(new DateType("2020-01-01"));
		myPatientDao.update(p3, mySrd);
	}

	private void createPractitionerPr1_BarneyGumble() {
		Practitioner practitioner = new Practitioner();
		practitioner.setId(PRACTITIONER_PR1);
		practitioner.addName().setFamily("Gumble").addGiven("Barney");
		myPractitionerDao.update(practitioner, mySrd);
	}

	private void createSearchParam_EncounterSubject_WithUpliftOnName() {
		RuntimeSearchParam subjectSp = mySearchParamRegistry.getRuntimeSearchParam("Encounter", "subject");
		SearchParameter sp = new SearchParameter();
		Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType("name"));

		sp.setId(subjectSp.getId());
		sp.setCode(subjectSp.getName());
		sp.setName(subjectSp.getName());
		sp.setUrl(subjectSp.getUri());
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression(subjectSp.getPath());
		subjectSp.getBase().forEach(t->sp.addBase(Enumerations.VersionIndependentResourceTypesAll.fromCode(t)));
		subjectSp.getTargets().forEach(t->sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.fromCode(t)));
		mySearchParameterDao.create(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

	@Nonnull
	private static Encounter newEncounter(String theEncounterId, String theSubjectReference) {
		Encounter e1 = new Encounter();
		e1.setId(theEncounterId);
		e1.setSubject(new Reference(theSubjectReference));
		e1.addParticipant().setActor(new Reference(PRACTITIONER_PR1));
		return e1;
	}

	@Nonnull
	private static Patient newPatientP1_HomerSimpson() {
		return newPatientP1_HomerSimpson(null);
	}

	@Nonnull
	private static Patient newPatientP1_HomerSimpson(String theOrganizationId) {
		Patient p1 = new Patient();
		p1.setId(PATIENT_P1);
		p1.addIdentifier().setSystem("http://system").setValue("200");
		p1.addName().setFamily("Simpson").addGiven("Homer");
		p1.setBirthDateElement(new DateType("2020-01-02"));
		if (theOrganizationId != null) {
			p1.setManagingOrganization(new Reference(theOrganizationId));
		}
		return p1;
	}

	@Nonnull
	private static Patient newPatientP2_MargeSimpson() {
		Patient p2 = new Patient();
		p2.setId(PATIENT_P2);
		p2.addIdentifier().setSystem("http://system").setValue("300");
		p2.setBirthDateElement(new DateType("2020-01-03"));
		p2.addName().setFamily("Simpson").addGiven("Marge");
		return p2;
	}


}
