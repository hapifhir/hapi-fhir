package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FhirResourceDaoR4ComboNonUniqueParamTest extends BaseComboParamsR4Test {
	public static final String ORG_ID_UNQUALIFIED = "my-org";
	public static final String ORG_ID_QUALIFIED = "Organization/" + ORG_ID_UNQUALIFIED;

	@Autowired
	SearchParamValidatingInterceptor mySearchParamValidatingInterceptor;
	@Autowired
	IInterceptorService myInterceptorService;
	private boolean myInterceptorFound = false;

	@BeforeEach
	public void removeInterceptor() {
		myInterceptorFound = myInterceptorService.unregisterInterceptor(mySearchParamValidatingInterceptor);
	}

	@AfterEach
	public void restoreInterceptor() {
		myStorageSettings.setIndexMissingFields(new StorageSettings().getIndexMissingFields());

		if (myInterceptorFound) {
			myInterceptorService.unregisterInterceptor(mySearchParamValidatingInterceptor);
		}
	}

	@Test
	public void testTokenFromCodeableConcept_Create() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.family");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-maritalstatus");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.maritalStatus");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names-and-maritalstatus");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-maritalstatus");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
		myMessages.clear();


		Patient pt = new Patient();
		pt.addName().setFamily("FAMILY1");
		pt.addName().setFamily("FAMILY2");
		pt.getMaritalStatus().addCoding().setSystem("http://foo1").setCode("bar1");
		pt.getMaritalStatus().addCoding().setSystem("http://foo2").setCode("bar2");
		myPatientDao.create(pt, mySrd);
		createPatient1(null);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(ResourceIndexedComboTokenNonUnique::getIndexString));
			assertEquals(4, indexedTokens.size());
			String expected;
			expected = "Patient?gender=http%3A%2F%2Ffoo1%7Cbar1&given=FAMILY1";
			assertEquals(expected, indexedTokens.get(0).getIndexString());
			expected = "Patient?gender=http%3A%2F%2Ffoo1%7Cbar1&given=FAMILY2";
			assertEquals(expected, indexedTokens.get(1).getIndexString());
			expected = "Patient?gender=http%3A%2F%2Ffoo2%7Cbar2&given=FAMILY1";
			assertEquals(expected, indexedTokens.get(2).getIndexString());
			expected = "Patient?gender=http%3A%2F%2Ffoo2%7Cbar2&given=FAMILY2";
			assertEquals(expected, indexedTokens.get(3).getIndexString());
		});
	}


	@Test
	public void testStringAndToken_Create() {
		createStringAndTokenCombo_NameAndGender();

		IIdType id1 = createPatient1(null);
		assertNotNull(id1);

		IIdType id2 = createPatient2(null);
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(ResourceIndexedComboTokenNonUnique::getId));
			assertEquals(2, indexedTokens.size());
			String expected = "Patient?family=FAMILY1&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1";
			assertEquals(expected, indexedTokens.get(0).getIndexString());
			assertEquals(-2634469377090377342L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactlyInAnyOrder(id1.toUnqualifiedVersionless().getValue());

		assertThat(myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList()).contains(
			"SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '-2634469377090377342')"
		);

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("[INFO Using NON_UNIQUE index(es) for query for search: Patient?family=FAMILY1&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]");
		myMessages.clear();

		// Remove 1, add another

		myPatientDao.delete(id1, mySrd);

		IIdType id3 = createPatient1(null);
		assertNotNull(id3);

		params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params, mySrd);
		actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactlyInAnyOrder(id3.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testEmptyParamLists() {
		createStringAndTokenCombo_NameAndGender();

		IIdType id1 = createPatient1(null);
		IIdType id2 = createPatient2(null);

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringAndListParam());
		params.add("given", new StringAndListParam());
		params.add("gender", new TokenAndListParam());
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		assertThat(actual).containsExactlyInAnyOrder(id1.toUnqualifiedVersionless().getValue(), id2.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testStringAndToken_CreateAndUpdate() {
		createStringAndTokenCombo_NameAndGender();

		// Create a resource patching the unique SP
		myCaptureQueriesListener.clear();
		IIdType id1 = createPatient1(null);
		assertNotNull(id1);

		assertThat(myCaptureQueriesListener.countSelectQueries()).as(String.join(",", "\n" + myCaptureQueriesListener.getSelectQueries().stream().map(SqlQuery::getThreadName).toList())).isEqualTo(0);
		assertEquals(12, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			List<String> indexes = myResourceIndexedComboTokensNonUniqueDao
				.findAll()
				.stream()
				.map(ResourceIndexedComboTokenNonUnique::getIndexString)
				.toList();
			assertThat(indexes).as(indexes.toString()).containsExactly("Patient?family=FAMILY1&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1");
		});

		/*
		 * Now update the resource
		 */

		Patient patient = myPatientDao.read(id1, mySrd);
		patient.getNameFirstRep().setFamily("Family2");

		myCaptureQueriesListener.clear();
		myPatientDao.update(patient, mySrd);

		assertEquals(6, myCaptureQueriesListener.countSelectQueries());
		assertEquals(1, myCaptureQueriesListener.countInsertQueries());
		assertEquals(5, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			List<String> indexes = myResourceIndexedComboTokensNonUniqueDao
				.findAll()
				.stream()
				.map(ResourceIndexedComboTokenNonUnique::getIndexString)
				.toList();
			assertThat(indexes).as(indexes.toString()).containsExactly("Patient?family=FAMILY2&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1");
		});

	}

	@Test
	public void testStringAndToken_SearchWithExtraParameters() {
		createStringAndTokenCombo_NameAndGender();

		IIdType id1 = createPatient1(null);
		assertNotNull(id1);

		IIdType id2 = createPatient2(null);
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(ResourceIndexedComboTokenNonUnique::getId));
			assertEquals(2, indexedTokens.size());
			assertEquals(-2634469377090377342L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2021-02-02"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactlyInAnyOrder(id1.toUnqualifiedVersionless().getValue());

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		String expected = "SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_IDX_CMB_TOK_NU t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_DATE t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t0.HASH_COMPLETE = '-2634469377090377342') AND ((t2.HASH_IDENTITY = '5247847184787287691') AND ((t2.SP_VALUE_LOW_DATE_ORDINAL >= '20210202') AND (t2.SP_VALUE_HIGH_DATE_ORDINAL <= '20210202'))))";
		assertEquals(expected, sql);

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("[INFO Using NON_UNIQUE index(es) for query for search: Patient?family=FAMILY1&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]");
		myMessages.clear();

	}


	@Test
	public void testStringAndToken_MultipleAnd() {
		createStringAndTokenCombo_NameAndGender();

		IIdType id1 = createPatient(withFamily("SIMPSON"), withGiven("HOMER"), withGiven("JAY"), withGender("male"));
		assertNotNull(id1);

		logAllNonUniqueIndexes();
		logAllStringIndexes();

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("Simpson"));
		params.add("given", new StringAndListParam().addAnd(new StringParam("Homer")).addAnd(new StringParam("Jay")));
		params.add("gender", new TokenParam("male"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactlyInAnyOrder(id1.toUnqualifiedVersionless().getValue());

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 INNER JOIN HFJ_SPIDX_STRING t1 ON (t0.RES_ID = t1.RES_ID) WHERE ((t0.HASH_COMPLETE = '7545664593829342272') AND ((t1.HASH_NORM_PREFIX = '6206712800146298788') AND (t1.SP_VALUE_NORMALIZED LIKE 'JAY%')))";
		assertEquals(expected, sql);

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using NON_UNIQUE index(es) for query for search: Patient?family=SIMPSON&gender=male&given=HOMER");
		myMessages.clear();

	}


	@Test
	public void testStringAndDate_Create() {
		createStringAndDateCombo_NameAndBirthdate();

		IIdType id1 = createPatient1(null);
		assertNotNull(id1);

		IIdType id2 = createPatient2(null);
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(ResourceIndexedComboTokenNonUnique::getId));
			assertEquals(2, indexedTokens.size());
			String expected = "Patient?birthdate=2021-02-02&family=FAMILY1";
			assertEquals(expected, indexedTokens.get(0).getIndexString());
			assertEquals(7196518367857292879L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("family1"));
		params.add("birthdate", new DateParam("2021-02-02"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '7196518367857292879')";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("[INFO Using NON_UNIQUE index(es) for query for search: Patient?birthdate=2021-02-02&family=FAMILY1]");
		myMessages.clear();
	}

	@Test
	public void testStringAndReference_Create() {
		createStringAndReferenceCombo_FamilyAndOrganization();

		createOrg();

		IIdType id1 = createPatient1(ORG_ID_QUALIFIED);
		createPatient2(ORG_ID_QUALIFIED);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(ResourceIndexedComboTokenNonUnique::getId));
			assertEquals(2, indexedTokens.size());
			assertEquals("Patient?family=FAMILY1&organization=Organization%2Fmy-org", indexedTokens.get(0).getIndexString());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1")); // weird casing to test normalization
		params.add("organization", new ReferenceParam(ORG_ID_QUALIFIED));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '2591238402961312979')";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));
	}

	@Test
	public void testStringAndReference_SearchByUnqualifiedReference() {
		createStringAndReferenceCombo_FamilyAndOrganization();

		createOrg();

		IIdType id1 = createPatient1(ORG_ID_QUALIFIED);
		createPatient2(ORG_ID_QUALIFIED);

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("family1"));
		// "orgid" instead of "Organization/orgid"
		params.add("organization", new ReferenceParam(ORG_ID_UNQUALIFIED));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

		myCaptureQueriesListener.logSelectQueries();
		String expected;
		expected = "select rt1_0.RES_ID,rt1_0.RES_TYPE,rt1_0.FHIR_ID from HFJ_RESOURCE rt1_0 where rt1_0.FHIR_ID='my-org'";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, false);
		assertThat(sql).contains("SP_VALUE_NORMALIZED LIKE 'FAMILY1%'");
		assertThat(sql).contains("t1.TARGET_RESOURCE_ID");

		assertThat(myMessages.get(0)).contains("This search uses an unqualified resource");
	}


	/**
	 * If there are two parameters as a part of a combo param, and we have
	 * multiple AND repetitions for both, then we can just join on the
	 * combo index twice.
	 */
	@Test
	public void testMultipleAndCombinations_EqualNumbers() {
		createStringAndStringCombo_FamilyAndGiven();

		createPatient(
			withId("A"),
			withFamily("SIMPSON"), withGiven("HOMER"),
			withFamily("jones"), withGiven("frank")
		);
		createPatient(
			withId("B"),
			withFamily("SIMPSON"), withGiven("MARGE")
		);

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringAndListParam().addAnd(new StringParam("simpson")).addAnd(new StringParam("JONES")));
		params.add("given", new StringAndListParam().addAnd(new StringParam("homer")).addAnd(new StringParam("frank")));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).contains("Patient/A");

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 INNER JOIN HFJ_IDX_CMB_TOK_NU t1 ON (t0.RES_ID = t1.RES_ID) WHERE ((t0.HASH_COMPLETE = '822090206952728926') AND (t1.HASH_COMPLETE = '-8088946700286918311'))";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

	}

	/**
	 * If there are two parameters as a part of a combo param, and we have
	 * multiple AND repetitions for one but not the other, than we'll use
	 * the combo index for the first pair, but we don't create a second pair.
	 * We could probably optimize this to use the combo index for both, but
	 * it's not clear that this would actually help and this is probably
	 * a pretty contrived use case.
	 */
	@Test
	public void testMultipleAndCombinations_NonEqualNumbers() {
		createStringAndStringCombo_FamilyAndGiven();

		createPatient(
			withId("A"),
			withFamily("SIMPSON"), withGiven("HOMER"),
			withFamily("jones"), withGiven("frank")
		);
		createPatient(
			withId("B"),
			withFamily("SIMPSON"), withGiven("MARGE")
		);

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringAndListParam().addAnd(new StringParam("simpson")).addAnd(new StringParam("JONES")));
		params.add("given", new StringAndListParam().addAnd(new StringParam("homer")));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).contains("Patient/A");

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 INNER JOIN HFJ_SPIDX_STRING t1 ON (t0.RES_ID = t1.RES_ID) WHERE ((t0.HASH_COMPLETE = '822090206952728926') AND ((t1.HASH_NORM_PREFIX = '-3664262414674370905') AND (t1.SP_VALUE_NORMALIZED LIKE 'JONES%')))";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

	}


	@Test
	public void testOrQuery() {
		createTokenAndReferenceCombo_FamilyAndOrganization();

		createPatient(withId("PAT"), withActiveTrue());
		createObservation(withId("O1"), withSubject("Patient/PAT"), withStatus("final"));
		createObservation(withId("O2"), withSubject("Patient/PAT"), withStatus("registered"));

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("patient", new ReferenceParam("Patient/PAT"));
		params.add("status", new TokenOrListParam(null, "preliminary", "final", "amended"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myObservationDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).contains("Observation/O1");

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE IN ('2445648980345828396','-6884698528022589694','-8034948665712960724') )";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Observation?patient=Patient%2FPAT&status=amended", "Observation?patient=Patient%2FPAT&status=final", "Observation?patient=Patient%2FPAT&status=preliminary");
		myMessages.clear();

	}


	private void createOrg() {
		Organization org = new Organization();
		org.setName("Some Org");
		org.setId(ORG_ID_QUALIFIED);
		myOrganizationDao.update(org, mySrd);
	}

	private void createStringAndDateCombo_NameAndBirthdate() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names-and-birthdate");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createStringAndStringCombo_FamilyAndGiven() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-given");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.given");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-given");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createStringAndTokenCombo_NameAndGender() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-given");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.given");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names-and-gender");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-given");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createStringAndReferenceCombo_FamilyAndOrganization() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-managingorg");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode(Patient.SP_ORGANIZATION);
		sp.setExpression("Patient.managingOrganization");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family-and-org");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-managingorg");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createTokenAndReferenceCombo_FamilyAndOrganization() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/Observation-status");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode(Observation.SP_STATUS);
		sp.setExpression("Observation.status");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/clinical-patient");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode(Observation.SP_PATIENT);
		sp.setExpression("Observation.subject.where(resolve() is Patient)");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-status-and-patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/Observation-status");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/clinical-patient");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private IIdType createPatient1(String theOrgId) {
		Patient pt1 = new Patient();
		pt1.getNameFirstRep().setFamily("Family1").addGiven("Given1");
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2021-02-02"));
		pt1.getManagingOrganization().setReference(theOrgId);
		return myPatientDao.create(pt1, mySrd).getId().toUnqualified();
	}

	private IIdType createPatient2(String theOrgId) {
		Patient pt2 = new Patient();
		pt2.getNameFirstRep().setFamily("Family2").addGiven("Given2");
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2021-02-02"));
		pt2.getManagingOrganization().setReference(theOrgId);
		return myPatientDao.create(pt2, mySrd).getId().toUnqualified();
	}

	/**
	 * A few tests where the combo index should not be used
	 */
	@Nested
	public class ShouldNotUseComboIndexTest {

		@BeforeEach
		public void before() {
			SearchParameter sp = new SearchParameter();
			sp.setId("SearchParameter/Observation-date");
			sp.setType(Enumerations.SearchParamType.DATE);
			sp.setCode("date");
			sp.setExpression("Observation.effective");
			sp.setStatus(PublicationStatus.ACTIVE);
			sp.addBase("Observation");
			mySearchParameterDao.update(sp, mySrd);

			sp = new SearchParameter();
			sp.setId("SearchParameter/Observation-note-text");
			sp.setType(Enumerations.SearchParamType.STRING);
			sp.setCode("note-text");
			sp.setExpression("Observation.note.text");
			sp.setStatus(PublicationStatus.ACTIVE);
			sp.addBase("Observation");
			mySearchParameterDao.update(sp, mySrd);

			sp = new SearchParameter();
			sp.setId("SearchParameter/observation-date-and-note-text");
			sp.setType(Enumerations.SearchParamType.COMPOSITE);
			sp.setStatus(PublicationStatus.ACTIVE);
			sp.addBase("Observation");
			sp.addComponent()
				.setExpression("Observation")
				.setDefinition("SearchParameter/Observation-date");
			sp.addComponent()
				.setExpression("Observation")
				.setDefinition("SearchParameter/Observation-note-text");
			sp.addExtension()
				.setUrl(HapiExtensions.EXT_SP_UNIQUE)
				.setValue(new BooleanType(false));
			mySearchParameterDao.update(sp, mySrd);

			mySearchParamRegistry.forceRefresh();

			myMessages.clear();
		}

		@Test
		public void testTooManyPermutations() {
			// Test
			StringOrListParam noteTextParam = new StringOrListParam();
			DateOrListParam dateParam = new DateOrListParam();
			for (int i = 0; i < 30; i++) {
				noteTextParam.add(new StringParam("A" + i));
				noteTextParam.add(new StringParam("B" + i));
				noteTextParam.add(new StringParam("C" + i));
				noteTextParam.add(new StringParam("D" + i));
				dateParam.add(new DateParam("2020-01-" + String.format("%02d", i+1)));
			}

			SearchParameterMap params = SearchParameterMap
				.newSynchronous()
				.add("note-text", noteTextParam)
				.add("date", dateParam);
			myCaptureQueriesListener.clear();
			IBundleProvider results = myObservationDao.search(params, mySrd);
			assertThat(toUnqualifiedIdValues(results)).isEmpty();

			// Verify
			myCaptureQueriesListener.logSelectQueries();
			String formatted = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);
			assertThat(formatted).doesNotContain("HFJ_IDX_CMB_TOK_NU");
		}


		/**
		 * Can't create or search for combo params with dateTimes that don't have DAY precision
		 */
		@ParameterizedTest
		@CsvSource(value = {
			"2021-01-02T12:00:01.000Z , false",
			"2021-01-02T12:00:01Z     , false",
			"2021-01-02               , true", // <-- DAY precision
			"2021-01                  , false",
			"2021                     , false",
		})
		public void testPartialDateTime(String theDateValue, boolean theShouldUseComboIndex) {
			IIdType id1 = createObservation(theDateValue);

			logAllNonUniqueIndexes();
			runInTransaction(() -> {
				List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
				if (theShouldUseComboIndex) {
					assertEquals(1, indexedTokens.size());
				} else {
					assertEquals(0, indexedTokens.size());
				}
			});

			SearchParameterMap params = SearchParameterMap
				.newSynchronous()
				.add("note-text", new StringParam("Hello"))
				.add("date", new DateParam(theDateValue));
			myCaptureQueriesListener.clear();
			IBundleProvider results = myObservationDao.search(params, mySrd);
			List<String> actual = toUnqualifiedVersionlessIdValues(results);
			myCaptureQueriesListener.logSelectQueries();
			assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

			if (theShouldUseComboIndex) {
				assertComboIndexUsed();
			} else {
				assertComboIndexNotUsed();
				assertThat(myMessages.toString()).contains("INFO Search with params [date, note-text] is not a candidate for combo searching - Date search with non-DAY precision for parameter 'date'");
			}
		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		public void testDateModifier(boolean theUseComparator) {
			IIdType id1 = createObservation("2021-01-02");
			createObservation("2023-01-02");

			SearchParameterMap params = SearchParameterMap
				.newSynchronous()
				.add("note-text", new StringParam("Hello"))
				.add("date", new DateParam("2021-01-02").setPrefix(theUseComparator ? ParamPrefixEnum.LESSTHAN_OR_EQUALS : null));
			myCaptureQueriesListener.clear();
			IBundleProvider results = myObservationDao.search(params, mySrd);
			List<String> actual = toUnqualifiedVersionlessIdValues(results);
			myCaptureQueriesListener.logSelectQueries();
			assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

			if (theUseComparator) {
				assertComboIndexNotUsed();
				assertThat(myMessages.toString()).contains("INFO Search with params [date, note-text] is not a candidate for combo searching - Parameter 'date' has prefix: 'le'");
			} else {
				assertComboIndexUsed();
			}

		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		public void testStringModifier(boolean theUseExact) {
			IIdType id1 = createObservation("2021-01-02");
			createObservation("2023-01-02");

			SearchParameterMap params = SearchParameterMap
				.newSynchronous()
				.add("note-text", new StringParam("Hello").setExact(theUseExact))
				.add("date", new DateParam("2021-01-02"));
			myCaptureQueriesListener.clear();
			IBundleProvider results = myObservationDao.search(params, mySrd);
			List<String> actual = toUnqualifiedVersionlessIdValues(results);
			myCaptureQueriesListener.logSelectQueries();
			assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

			if (theUseExact) {
				assertComboIndexNotUsed();
				assertThat(myMessages.toString()).contains("INFO Search with params [date, note-text] is not a candidate for combo searching - Parameter 'note-text' has modifier: ':exact'");
			} else {
				assertComboIndexUsed();
			}

		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		public void testMissing(boolean theUseMissing) {
			myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.ENABLED);

			IIdType id1 = createObservation("2021-01-02");
			createObservation("2023-01-02");

			SearchParameterMap params = SearchParameterMap
				.newSynchronous()
				.add("note-text", new StringParam("Hello").setMissing(theUseMissing ? false : null))
				.add("date", new DateParam("2021-01-02"));
			myCaptureQueriesListener.clear();
			IBundleProvider results = myObservationDao.search(params, mySrd);
			List<String> actual = toUnqualifiedVersionlessIdValues(results);
			myCaptureQueriesListener.logSelectQueries();
			assertThat(actual).contains(id1.toUnqualifiedVersionless().getValue());

			if (theUseMissing) {
				assertComboIndexNotUsed();
				assertThat(myMessages.toString()).contains("INFO Search with params [date, note-text] is not a candidate for combo searching - Parameter 'note-text' has modifier: ':missing'");
			} else {
				assertComboIndexUsed();
			}

		}

		private void assertComboIndexUsed() {
			String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(sql).contains("HFJ_IDX_CMB_TOK_NU");

			assertThat(myMessages.toString()).contains("Using NON_UNIQUE index(es) for query");
		}

		private void assertComboIndexNotUsed() {
			String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(sql).doesNotContain("HFJ_IDX_CMB_TOK_NU");

			assertThat(myMessages.toString()).doesNotContain("Using NON_UNIQUE index(es) for query");
		}

		private IIdType createObservation(String theDateValue) {
			Observation pt1 = new Observation();
			pt1.addNote().setText("Hello");
			pt1.setEffective(new DateTimeType(theDateValue));
			IIdType id1 = myObservationDao.create(pt1, mySrd).getId().toUnqualified();
			return id1;
		}

	}


}
