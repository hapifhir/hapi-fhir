package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.Composition;
import org.hl7.fhir.r5.model.Coverage;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.HealthcareService;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComboNonUniqueOnUpliftedRefchainR5Test extends BaseJpaR5Test {

	@Override
	protected void before() throws Exception {
		super.before();

		myStorageSettings.setIndexOnUpliftedRefchains(true);
		myStorageSettings.setUniqueIndexesEnabled(true);
	}

	/**
	 * Happy path test with two uplifted refchain parameters and a combo search parameter
	 * combining them
	 */
	@Test
	void testIndexAndSearch_ReferenceOnToken() {
		// Setup
		createSpEncounterSubjectWithChains("identifier");
		createSpEncounterServiceTypeWithChains("service-category");
		createComboSpForChainedParams(
			"Encounter",
			"SearchParameter/Encounter-subject.identifier",
			"SearchParameter/Encounter-service-type.service-category"
		);

		// Test 1 - Create a matching resource and ensure that the right index gets created

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("12345");
		IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

		HealthcareService hs = new HealthcareService();
		hs.setId("HCS");
		hs.addCategory().addCoding().setSystem("http://snomed.info/sct").setCode("123456789");
		IIdType hsId = myHealthcareServiceDao.update(hs, newSrd()).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.addServiceType(new CodeableReference(new Reference(hsId)));
		enc.setSubject(new Reference(patientId));
		DaoMethodOutcome encId = myEncounterDao.create(enc, newSrd());

		// Verify 1 - Ensure that the right combo index is created

		assertIndexStringsExactly("Encounter?service-type.service-category=http%3A%2F%2Fsnomed.info%2Fsct%7C123456789&subject.identifier=http%3A%2F%2Fpatient%7C12345");

		// Test 2 - Perform a search which should leverage the index

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add("service-type", new ReferenceParam(null, "service-category", "http://snomed.info/sct|123456789"))
			.add("subject", new ReferenceParam(null, "identifier", "http://patient|12345"));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			encId.getId().toUnqualifiedVersionless().getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);
		assertThat(searchSql).doesNotContainIgnoringCase("JOIN");
	}

	@Test
	public void testIndexAndSearch_ReferenceOnStringTokenDate() {
		createSpCoveragePatientWithChains("family", "given", "gender", "address-postalcode", "birthdate");
		createComboSpForChainedParams(
			"Coverage",
			"SearchParameter/Coverage-patient.family",
			"SearchParameter/Coverage-patient.given",
			"SearchParameter/Coverage-patient.gender",
			"SearchParameter/Coverage-patient.address-postalcode",
			"SearchParameter/Coverage-patient.birthdate"
		);

		// Test 1 - Create a matching resource and ensure that the right index gets created

		Patient patient = new Patient();
		patient.addName().setFamily("Simpson").addGiven("Homer");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.addAddress().setPostalCode("90701");
		patient.setBirthDateElement(new DateType("1966-01-02"));
		IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

		Coverage hs = new Coverage();
		hs.setId("COV");
		hs.setBeneficiary(new Reference(patientId));
		IIdType covId = myCoverageDao.update(hs, newSrd()).getId().toUnqualifiedVersionless();

		// Verify 1 - Ensure that the right combo index is created

		assertIndexStringsExactly(
			"Coverage?patient.address-postalcode=90701&patient.birthdate=1966-01-02&patient.family=SIMPSON&patient.gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&patient.given=HOMER"
		);

		// Test 2 - Perform a search which should leverage the index

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add("patient", new ReferenceParam(null, "address-postalcode", "90701"))
			.add("patient", new ReferenceParam(null, "birthdate", "1966-01-02"))
			.add("patient", new ReferenceParam(null, "family", "Simpson"))
			.add("patient", new ReferenceParam(null, "given", "Homer"))
			.add("patient", new ReferenceParam(null, "gender", "male"));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myCoverageDao.search(map, mySrd);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			covId.toUnqualifiedVersionless().getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);
		assertThat(searchSql).doesNotContainIgnoringCase("JOIN");
	}

	/**
	 * A combo is declared on 5 parameters, but then the search only includes 4 of them.
	 * We should still use the uplifted refchains, but we can't use the combo param.
	 */
	@Test
	public void testSearchNotCoveringAllParameters() {
		createSpCoveragePatientWithChains("family", "given", "gender", "address-postalcode", "birthdate");
		createComboSpForChainedParams(
			"Coverage",
			"SearchParameter/Coverage-patient.family",
			"SearchParameter/Coverage-patient.given",
			"SearchParameter/Coverage-patient.gender",
			"SearchParameter/Coverage-patient.address-postalcode",
			"SearchParameter/Coverage-patient.birthdate"
		);

		// This patient/coverage will match
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson").addGiven("Homer");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.addAddress().setPostalCode("90701");
		patient.setBirthDateElement(new DateType("1966-01-02"));
		IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

		Coverage hs = new Coverage();
		hs.setId("COV");
		hs.setBeneficiary(new Reference(patientId));
		IIdType covId = myCoverageDao.update(hs, newSrd()).getId().toUnqualifiedVersionless();

		// Create a mon-matching patient/coverage
		// Only the given name is different from the first one
		Patient nonMatchingPatient = new Patient();
		nonMatchingPatient.addName().setFamily("Flanders").addGiven("Ned");
		nonMatchingPatient.setGender(Enumerations.AdministrativeGender.MALE);
		nonMatchingPatient.addAddress().setPostalCode("90701");
		nonMatchingPatient.setBirthDateElement(new DateType("1966-01-02"));
		IIdType nonMatchingPatientId = myPatientDao.create(nonMatchingPatient, newSrd()).getId().toUnqualifiedVersionless();

		Coverage nonMatchingCoverage = new Coverage();
		nonMatchingCoverage.setBeneficiary(new Reference(nonMatchingPatientId));
		myCoverageDao.create(nonMatchingCoverage, newSrd()).getId().toUnqualifiedVersionless();

		// Test

		// Gender isn't in the query so we can't use the combo param
		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add("patient", new ReferenceParam(null, "address-postalcode", "90701"))
			.add("patient", new ReferenceParam(null, "birthdate", "1966-01-02"))
			.add("patient", new ReferenceParam(null, "family", "Simpson"))
			.add("patient", new ReferenceParam(null, "gender", "male"));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myCoverageDao.search(map, mySrd);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			covId.toUnqualifiedVersionless().getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).doesNotContainIgnoringCase(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);
	}

	/**
	 * Two different chains on the same parameter (Encounter.subject in this case)
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testTwoRefchainsOnSameParameter(boolean theUseChainedParamNameOnSearch) {
		createSpEncounterSubjectWithChains("identifier", "language");
		createComboSpForChainedParams(
			"Encounter",
			"SearchParameter/Encounter-subject.identifier",
			"SearchParameter/Encounter-subject.language"
		);

		// Test 1 - Create a matching resource and ensure that the right index gets created

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("12345");
		patient.addCommunication().getLanguage().addCoding().setSystem("http://language").setCode("ABC");
		IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(patientId));
		DaoMethodOutcome encId = myEncounterDao.create(enc, newSrd());

		// Verify 1 - Ensure that the right combo index is created

		assertIndexStringsExactly("Encounter?subject.identifier=http%3A%2F%2Fpatient%7C12345&subject.language=http%3A%2F%2Flanguage%7CABC");

		// Test 2 - Perform a search which should leverage the index

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		if (theUseChainedParamNameOnSearch) {
			map.add("subject.language", new TokenParam("http://language", "ABC"));
			map.add("subject.identifier", new TokenParam("http://patient", "12345"));
		} else {
			map.add("subject", new ReferenceParam(null, "language", "http://language|ABC"));
			map.add("subject", new ReferenceParam(null, "identifier", "http://patient|12345"));
		}

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			encId.getId().toUnqualifiedVersionless().getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);

	}

	@Test
	public void testComboOfPreChainedParameters() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/Bundle-composition-patient-identifier");
		sp.setCode("composition.patient.identifier");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		createOrUpdateSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/Bundle-composition-patient-gender");
		sp.setCode("composition.patient.gender");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		createOrUpdateSearchParameter(sp);

		createComboSpForChainedParams(
			"Bundle",
			"SearchParameter/Bundle-composition-patient-identifier",
			"SearchParameter/Bundle-composition-patient-gender"
		);

		// Create non-matching resources
		{
			String patientId = "Patient/A";
			Bundle document = new Bundle();
			document.setType(Bundle.BundleType.DOCUMENT);
			Composition composition = new Composition();
			composition.addSubject(new Reference(patientId));
			document.addEntry().setResource(composition);
			Patient patient = new Patient();
			patient.setId(patientId);
			patient.addIdentifier().setSystem("http://patient").setValue("123");
			patient.setGender(Enumerations.AdministrativeGender.MALE);
			document.addEntry().setResource(patient);
			myBundleDao.create(document, newSrd());
		}
		{
			String patientId = "Patient/B";
			Bundle document = new Bundle();
			document.setType(Bundle.BundleType.DOCUMENT);
			Composition composition = new Composition();
			composition.addSubject(new Reference(patientId));
			document.addEntry().setResource(composition);
			Patient patient = new Patient();
			patient.setId(patientId);
			patient.addIdentifier().setSystem("http://patient").setValue("456");
			patient.setGender(Enumerations.AdministrativeGender.MALE);
			document.addEntry().setResource(patient);
			myBundleDao.create(document, newSrd());
		}
		// Matching resource
		String patientId = "Patient/A";
		Bundle document = new Bundle();
		document.setType(Bundle.BundleType.DOCUMENT);
		Composition composition = new Composition();
		composition.addSubject(new Reference(patientId));
		document.addEntry().setResource(composition);
		Patient patient = new Patient();
		patient.setId(patientId);
		patient.addIdentifier().setSystem("http://patient").setValue("123");
		patient.addIdentifier().setSystem("http://patient").setValue("789");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		document.addEntry().setResource(patient);
		IIdType documentId = myBundleDao.create(document, newSrd()).getId().toUnqualifiedVersionless();

		assertIndexStringsExactly(
			"Bundle?composition.patient.gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&composition.patient.identifier=http%3A%2F%2Fpatient%7C123",
			"Bundle?composition.patient.gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&composition.patient.identifier=http%3A%2F%2Fpatient%7C456",
			"Bundle?composition.patient.gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&composition.patient.identifier=http%3A%2F%2Fpatient%7C789",
			"Bundle?composition.patient.gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&composition.patient.identifier=http%3A%2F%2Fpatient%7C123"
		);

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("composition.patient.identifier", new TokenParam("http://patient", "789"));
		map.add("composition.patient.identifier", new TokenParam("http://patient", "123"));
		map.add("composition.patient.gender", new TokenParam(Enumerations.AdministrativeGender.MALE.getSystem(), "male"));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myBundleDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			documentId.getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);

	}


	/**
	 * Two different chains on the same parameter (Encounter.subject in this case)
	 */
	@Test
	public void testTwoANDsOfSameParameter() {
		createSpEncounterSubjectWithChains("identifier", "language");
		createComboSpForChainedParams(
			"Encounter",
			"SearchParameter/Encounter-subject.identifier",
			"SearchParameter/Encounter-subject.language"
		);

		// Test 1 - Create a matching resource and ensure that the right index gets created

		// Non-matching resources
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://patient").setValue("123");
			patient.addCommunication().getLanguage().addCoding().setSystem("http://language").setCode("ABC");
			IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

			Encounter enc = new Encounter();
			enc.setSubject(new Reference(patientId));
			DaoMethodOutcome encId = myEncounterDao.create(enc, newSrd());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://patient").setValue("456");
			patient.addCommunication().getLanguage().addCoding().setSystem("http://language").setCode("ABC");
			IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

			Encounter enc = new Encounter();
			enc.setSubject(new Reference(patientId));
			DaoMethodOutcome encId = myEncounterDao.create(enc, newSrd());
		}

		// Matching resources

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://patient").setValue("123");
		patient.addIdentifier().setSystem("http://patient").setValue("456");
		patient.addCommunication().getLanguage().addCoding().setSystem("http://language").setCode("ABC");
		IIdType patientId = myPatientDao.create(patient, newSrd()).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(patientId));
		DaoMethodOutcome encId = myEncounterDao.create(enc, newSrd());

		// Verify 1 - Ensure that the right combo index is created

		assertIndexStringsExactly(
			"Encounter?subject.identifier=http%3A%2F%2Fpatient%7C123&subject.language=http%3A%2F%2Flanguage%7CABC",
			"Encounter?subject.identifier=http%3A%2F%2Fpatient%7C456&subject.language=http%3A%2F%2Flanguage%7CABC",
			"Encounter?subject.identifier=http%3A%2F%2Fpatient%7C456&subject.language=http%3A%2F%2Flanguage%7CABC",
			"Encounter?subject.identifier=http%3A%2F%2Fpatient%7C123&subject.language=http%3A%2F%2Flanguage%7CABC"
		);

		// Test 2 - Perform a search which should leverage the index

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("subject", new ReferenceParam(null, "language", "http://language|ABC"));
		map.add("subject", new ReferenceParam(null, "identifier", "http://patient|123"));
		map.add("subject", new ReferenceParam(null, "identifier", "http://patient|456"));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myEncounterDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			encId.getId().toUnqualifiedVersionless().getValue()
		);

		// Verify 2 - Ensure that we actually use the combo index

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU);

	}

	private void assertIndexStringsExactly(String... expectedIndexString) {
		logAllTokenIndexes();
		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> nonUniqueIndexes = myResourceIndexedComboTokensNonUniqueDao.findAll();
			assertThat(nonUniqueIndexes.stream().map(ResourceIndexedComboTokenNonUnique::getIndexString).toList()).containsExactlyInAnyOrder(
				expectedIndexString
			);
		});
	}


	/**
	 * @param theParams Format is "SearchParameterReference.chainName" or just "SearchParameterReference" if no chain
	 */
	private void createComboSpForChainedParams(String theBaseName, String... theParams) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/" + theBaseName + "-combo");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.fromCode(theBaseName));
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));

		for (String param : theParams) {

			SearchParameter.SearchParameterComponentComponent component = sp.addComponent();
			component.setExpression(theBaseName);

			if (param.contains(".")) {
				component.setDefinition(param.substring(0, param.indexOf(".")));
				String upliftRefChainParamCode = param.substring(param.indexOf(".") + 1);
				assert upliftRefChainParamCode.matches("^[a-zA-Z-]+$");
				component.addExtension(HapiExtensions.EXT_SP_COMBO_UPLIFT_CHAIN, new CodeType(upliftRefChainParamCode));
			} else {
				component.setDefinition(param);
			}

		}

		createOrUpdateSearchParameter(sp);
	}

	private void createSpEncounterServiceTypeWithChains(String... theUpliftChains) {
		SearchParameter sp = new SearchParameter();
		for (String upliftChain : theUpliftChains) {
			Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
			upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType(upliftChain));
		}
		sp.setId("SearchParameter/Encounter-service-type");
		sp.setName("EncounterServiceType");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setDescription("Enabling search parameter to search by Encounter.serviceType");
		sp.setCode("service-type");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.ENCOUNTER);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Encounter.serviceType");
		sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.HEALTHCARESERVICE);
		sp.setProcessingMode(SearchParameter.SearchProcessingModeType.NORMAL);
		ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		createOrUpdateSearchParameter(sp);
	}

	private void createSpEncounterSubjectWithChains(String... theUpliftChains) {
		SearchParameter sp = new SearchParameter();
		for (String upliftChain : theUpliftChains) {
			Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
			upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType(upliftChain));
		}
		sp.setId("SearchParameter/Encounter-subject");
		sp.setName("subject");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setUrl("http://hl7.org/fhir/SearchParameter/Encounter-subject");
		sp.setDescription("The patient or group present at the encounter");
		sp.setCode("subject");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.ENCOUNTER);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Encounter.subject");
		sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.GROUP);
		sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.setProcessingMode(SearchParameter.SearchProcessingModeType.NORMAL);
		createOrUpdateSearchParameter(sp);
	}

	private void createSpCoveragePatientWithChains(String... theUpliftChains) {
		SearchParameter sp = new SearchParameter();
		for (String upliftChain : theUpliftChains) {
			Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
			upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType(upliftChain));
		}
		sp.setId("SearchParameter/Coverage-patient");
		sp.setName("patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setUrl("http://hl7.org/fhir/SearchParameter/clinical-patient");
		sp.setDescription("The patient for this coverage");
		sp.setCode("patient");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.COVERAGE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Coverage.beneficiary");
		sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.setProcessingMode(SearchParameter.SearchProcessingModeType.NORMAL);
		createOrUpdateSearchParameter(sp);
	}


}
