package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
		IIdType id1 = createPatient1(null);

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
			String expected = "Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1";
			assertEquals(expected, indexedTokens.get(0).getIndexString());
			assertEquals(-7504889232313729794L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '-7504889232313729794')";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

		// Remove 1, add another

		myPatientDao.delete(id1, mySrd);

		IIdType id3 = createPatient1(null);
		assertNotNull(id3);

		params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params, mySrd);
		actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id3.toUnqualifiedVersionless().getValue()));

	}

	@Test
	public void testStringAndToken_CreateAndUpdate() {
		createStringAndTokenCombo_NameAndGender();

		// Create a resource patching the unique SP
		myCaptureQueriesListener.clear();
		IIdType id1 = createPatient1(null);
		assertNotNull(id1);

		assertEquals(0, myCaptureQueriesListener.countSelectQueries(),
			String.join(",", "\n" + myCaptureQueriesListener.getSelectQueries().stream().map(SqlQuery::getThreadName).toList())
		);
		assertEquals(12, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(()->{
			List<String> indexes = myResourceIndexedComboTokensNonUniqueDao
				.findAll()
				.stream()
				.map(ResourceIndexedComboTokenNonUnique::getIndexString)
				.toList();
			assertThat(indexes.toString(), indexes, contains("Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1"));
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

		runInTransaction(()->{
			List<String> indexes = myResourceIndexedComboTokensNonUniqueDao
				.findAll()
				.stream()
				.map(ResourceIndexedComboTokenNonUnique::getIndexString)
				.toList();
			assertThat(indexes.toString(), indexes, contains("Patient?family=FAMILY2%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1"));
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
			assertEquals(-7504889232313729794L, indexedTokens.get(0).getHashComplete().longValue());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("given", new StringParam("gIVEn1"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2021-02-02"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		String expected = "SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_IDX_CMB_TOK_NU t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_DATE t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t0.HASH_COMPLETE = '-7504889232313729794') AND ((t2.HASH_IDENTITY = '5247847184787287691') AND ((t2.SP_VALUE_LOW_DATE_ORDINAL >= '20210202') AND (t2.SP_VALUE_HIGH_DATE_ORDINAL <= '20210202'))))";
		assertEquals(expected, sql);

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

	}


	@Test
	public void testStringAndDate_Create() {
		createStringAndTokenCombo_NameAndBirthdate();

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
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '7196518367857292879')";
		assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?birthdate=2021-02-02&family=FAMILY1]"));
		myMessages.clear();
	}

	/**
	 * Can't create or search for combo params with partial dates
	 */
	@Test
	public void testStringAndDate_Create_PartialDate() {
		createStringAndTokenCombo_NameAndBirthdate();

		Patient pt1 = new Patient();
		pt1.getNameFirstRep().setFamily("Family1").addGiven("Given1");
		pt1.setBirthDateElement(new DateType("2021-02"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualified();

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			assertEquals(0, indexedTokens.size());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("family1"));
		params.add("birthdate", new DateParam("2021-02"));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false),
			is(not(containsString("HFJ_IDX_CMB_TOK_NU"))));

		logCapturedMessages();
		assertThat(myMessages, empty());
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
			assertEquals("Patient?family=FAMILY1%5C%7C&organization=Organization%2Fmy-org", indexedTokens.get(0).getIndexString());
		});

		myMessages.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add("family", new StringParam("fAmIlY1|")); // weird casing to test normalization
		params.add("organization", new ReferenceParam(ORG_ID_QUALIFIED));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		String expected = "SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.HASH_COMPLETE = '2277801301223576208')";
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
		assertThat(actual, containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));

		myCaptureQueriesListener.logSelectQueries();
		String expected;
        expected = "select rt1_0.RES_ID,rt1_0.RES_TYPE,rt1_0.FHIR_ID from HFJ_RESOURCE rt1_0 where rt1_0.FHIR_ID='my-org'";
        assertEquals(expected, myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false));
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, false);
		assertThat(sql, sql, containsString("SP_VALUE_NORMALIZED LIKE 'FAMILY1%'"));
		assertThat(sql, sql, containsString("t1.TARGET_RESOURCE_ID"));

		assertThat(myMessages.toString(), myMessages.get(0), containsString("This search uses an unqualified resource"));
	}

	private void createOrg() {
		Organization org = new Organization();
		org.setName("Some Org");
		org.setId(ORG_ID_QUALIFIED);
		myOrganizationDao.update(org, mySrd);
	}

	private void createStringAndTokenCombo_NameAndBirthdate() {
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

	private void createStringAndTokenCombo_NameAndGender() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family + '|'");
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
		sp.setExpression("Patient.name.family + '|'");
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


}
