package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4ComboNonUniqueParamTest extends BaseComboParamsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4ComboNonUniqueParamTest.class);

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

	private void createNamesAndGenderSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family + '|'");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-given");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.given");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

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
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	@Test
	public void testCreateAndUse() {
		createNamesAndGenderSp();

		IIdType id1 = createPatient1();
		assertNotNull(id1);

		IIdType id2 = createPatient2();
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(t -> t.getId()));
			assertEquals(2, indexedTokens.size());
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

		boolean found = false;
		for (SqlQuery query : myCaptureQueriesListener.getSelectQueries()) {
			String sql = query.getSql(true, false);
			if ("SELECT t0.RES_ID FROM HFJ_IDX_CMB_TOK_NU t0 WHERE (t0.IDX_STRING = 'Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1')".equals(sql)) {
				found = true;
			}
		}
		assertTrue(found, "Found expected sql");

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

		// Remove 1, add another

		myPatientDao.delete(id1);

		IIdType id3 = createPatient1();
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
	public void testCreateAndUpdateResource() {
		createNamesAndGenderSp();

		// Create a resource patching the unique SP
		myCaptureQueriesListener.clear();
		IIdType id1 = createPatient1();
		assertNotNull(id1);

		assertEquals(0, myCaptureQueriesListener.countSelectQueries(),
			String.join(",", "\n" + myCaptureQueriesListener.getSelectQueries().stream().map(q -> q.getThreadName()).collect(Collectors.toList()))
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
	public void testSearchWithExtraParameters() {
		createNamesAndGenderSp();

		IIdType id1 = createPatient1();
		assertNotNull(id1);

		IIdType id2 = createPatient2();
		assertNotNull(id2);

		logAllNonUniqueIndexes();
		runInTransaction(() -> {
			List<ResourceIndexedComboTokenNonUnique> indexedTokens = myResourceIndexedComboTokensNonUniqueDao.findAll();
			indexedTokens.sort(Comparator.comparing(t -> t.getId()));
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
		assertEquals("SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_IDX_CMB_TOK_NU t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_DATE t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t0.IDX_STRING = 'Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1') AND ((t2.HASH_IDENTITY = '5247847184787287691') AND ((t2.SP_VALUE_LOW_DATE_ORDINAL >= '20210202') AND (t2.SP_VALUE_HIGH_DATE_ORDINAL <= '20210202'))))", sql);

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("[INFO Using NON_UNIQUE index for query for search: Patient?family=FAMILY1%5C%7C&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&given=GIVEN1]"));
		myMessages.clear();

	}


	private IIdType createPatient2() {
		Patient pt2 = new Patient();
		pt2.getNameFirstRep().setFamily("Family2").addGiven("Given2");
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2021-02-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualified();
		return id2;
	}

	private IIdType createPatient1() {
		Patient pt1 = new Patient();
		pt1.getNameFirstRep().setFamily("Family1").addGiven("Given1");
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2021-02-02"));
		return myPatientDao.create(pt1).getId().toUnqualified();
	}


}
