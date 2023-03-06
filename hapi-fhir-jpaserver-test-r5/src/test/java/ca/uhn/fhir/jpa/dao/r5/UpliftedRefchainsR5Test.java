package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r5.model.CodeType;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
@SuppressWarnings({"Duplicates"})
public class UpliftedRefchainsR5Test extends BaseJpaR5Test {
	public static final String PRACTITIONER_PR1 = "Practitioner/PR1";
	public static final String ENCOUNTER_E1 = "Encounter/E1";
	public static final String PATIENT_P1 = "Patient/P1";
	public static final String PATIENT_P2 = "Patient/P2";
	public static final String PATIENT_P3 = "Patient/P3";
	public static final String ENCOUNTER_E2 = "Encounter/E2";
	public static final String ENCOUNTER_E3 = "Encounter/E3";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpliftedRefchainsR5Test.class);
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
		mySystemDao.transaction(mySrd, bb.getBundleTyped());

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

	@Nonnull
	private static Encounter newEncounter(String theEncounterId, String theSubjectReference) {
		Encounter e1 = new Encounter();
		e1.setId(theEncounterId);
		e1.setSubject(new Reference(theSubjectReference));
		e1.addParticipant().setActor(new Reference(PRACTITIONER_PR1));
		return e1;
	}

	private void createPatientP1_HomerSimpson() {
		createPatientP1_HomerSimpson(null);
	}

	private void createPatientP1_HomerSimpson(String theOrganizationId) {
		Patient p1 = newPatientP1_HomerSimpson(theOrganizationId);
		myPatientDao.update(p1, mySrd);
	}

	@Nonnull
	private static Patient newPatientP1_HomerSimpson(String theOrganizationId) {
		Patient p1 = new Patient();
		p1.setId(PATIENT_P1);
		p1.addName().setFamily("Simpson").addGiven("Homer");
		if (theOrganizationId != null) {
			p1.setManagingOrganization(new Reference(theOrganizationId));
		}
		return p1;
	}

	private void createPatientP2_MargeSimpson() {
		Patient p2 = newPatientP2_MargeSimpson();
		myPatientDao.update(p2, mySrd);
	}

	@Nonnull
	private static Patient newPatientP2_MargeSimpson() {
		Patient p2 = new Patient();
		p2.setId(PATIENT_P2);
		p2.addName().setFamily("Simpson").addGiven("Marge");
		return p2;
	}

	private void createPatientP3_AbacusSimpson() {
		Patient p2 = new Patient();
		p2.setId(PATIENT_P3);
		p2.addName().setFamily("Simpson").addGiven("Abacus");
		myPatientDao.update(p2, mySrd);
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


}
