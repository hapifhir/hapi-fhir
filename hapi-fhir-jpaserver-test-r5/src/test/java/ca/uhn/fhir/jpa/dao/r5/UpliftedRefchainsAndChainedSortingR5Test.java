package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nonnull;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
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

	@Override
	@BeforeEach
	public void beforeResetConfig() {
		super.beforeResetConfig();

		myStorageSettings.setIndexOnUpliftedRefchains(true);
	}

	@Override
	@AfterEach
	public void afterCleanupDao() {
		super.afterCleanupDao();
		myStorageSettings.setIndexOnUpliftedRefchains(new StorageSettings().isIndexOnUpliftedRefchains());
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
			assertThat(params.toString(), params, containsInAnyOrder(
				"subject.name Homer",
				"subject.name Simpson",
				"subject.name Marge",
				"subject.name Simpson"
			));
		});
	}

	@Test
	public void testCreate_InTransaction_SourceAndTarget() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(newPatientP1_HomerSimpson(null));
		bb.addTransactionUpdateEntry(newPatientP2_MargeSimpson());
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, PATIENT_P1));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, PATIENT_P2));
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params.toString(), params, containsInAnyOrder(
				"subject.name Homer",
				"subject.name Simpson",
				"subject.name Marge",
				"subject.name Simpson"
			));
		});
	}

	@Test
	public void testCreate_InTransaction_SourceAndTarget_RefsUsePlaceholderIds() {
		// Setup

		createSearchParam_EncounterSubject_WithUpliftOnName();
		createPractitionerPr1_BarneyGumble();

		// Test

		String p1Id = IdType.newRandomUuid().getValue();
		String p2Id = IdType.newRandomUuid().getValue();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(newPatientP1_HomerSimpson(null).setId(p1Id));
		bb.addTransactionCreateEntry(newPatientP2_MargeSimpson().setId(p2Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E1, p1Id));
		bb.addTransactionUpdateEntry(newEncounter(ENCOUNTER_E2, p2Id));
		Bundle requestBundle = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, requestBundle);

		// Verify SQL

		// 1- Resolve resource forced IDs, and 2- Resolve Practitioner/PR1 reference
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

		// Verify correct indexes are written

		runInTransaction(() -> {
			logAllStringIndexes();

			List<String> params = myResourceIndexedSearchParamStringDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getValueExact())
				.toList();
			assertThat(params.toString(), params, containsInAnyOrder(
				"subject.name Homer",
				"subject.name Simpson",
				"subject.name Marge",
				"subject.name Simpson"
			));
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, contains(ENCOUNTER_E1));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(2, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HFJ_RES_LINK"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_IDENTITY"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, contains(ENCOUNTER_E1));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(0, countMatches(querySql, "HFJ_RES_LINK"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_IDENTITY"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, contains(ENCOUNTER_E1));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(2, countMatches(querySql, "HFJ_RES_LINK"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_IDENTITY"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(0, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(0, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(1, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(2, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(2, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_TOKEN"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(1, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_DATE"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(1, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual.toString(), actual, contains(ENCOUNTER_E3, ENCOUNTER_E1, ENCOUNTER_E2));
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		String querySql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertEquals(1, countMatches(querySql, "HFJ_SPIDX_STRING"), querySql);
		assertEquals(0, countMatches(querySql, "HASH_NORM_PREFIX"), querySql);
		assertEquals(1, countMatches(querySql, "HASH_IDENTITY"), querySql);
		assertEquals(1, countMatches(querySql, "HFJ_RES_LINK"), querySql);
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
			assertThat(e.getMessage(), containsString("Unable to sort on a chained parameter from 'focus' as this parameter has multiple target types. Please specify the target type."));
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
			assertThat(e.getMessage(), containsString("Invalid chain, date is not a reference SearchParameter"));
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
			assertThat(e.getMessage(), containsString("Unable to sort on a chained parameter from 'subject' as this parameter has multiple target types. Please specify the target type"));

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
			assertThat(e.getMessage(), containsString("Invalid _sort expression, can not chain more than once in a sort expression: patient.organization.name"));

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
			assertThat(e.getMessage(), containsString("Unknown _sort parameter value \"foo\" for resource type \"Encounter\""));

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
			assertThat(e.getMessage(), containsString("Unknown _sort parameter value \"foo\" for resource type \"Patient\""));

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
			assertThat(e.getMessage(), containsString("Unable to sort on a chained parameter result.value-quantity as this parameter. Can not sort on chains of target type: QUANTITY"));

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
		subjectSp.getBase().forEach(sp::addBase);
		subjectSp.getTargets().forEach(sp::addTarget);
		mySearchParameterDao.update(sp, mySrd);
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
