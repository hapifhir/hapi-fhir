package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.*;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticsearchClient;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestR4ConfigWithElasticsearchClient.class })
public class FhirResourceDaoR4SearchLastNIT extends BaseJpaTest {

	@Autowired
	@Qualifier("myPatientDaoR4")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;

	@Autowired
	@Qualifier("myObservationDaoR4")
	protected IFhirResourceDaoObservation<Observation> myObservationDao;

	@Autowired
	protected DaoConfig myDaoConfig;

	@Autowired
	protected FhirContext myFhirCtx;

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myPlatformTransactionManager;
	}

	private final String observationCd0 = "code0";
	private final String observationCd1 = "code1";
	private final String observationCd2 = "code2";

	private final String categoryCd0 = "category0";
	private final String categoryCd1 = "category1";
	private final String categoryCd2 = "category2";

	private final String codeSystem = "http://mycode.com";
	private final String categorySystem = "http://mycategory.com";

	// Using static variables including the flag below so that we can initalize the database and indexes once
	// (all of the tests only read from the DB and indexes and so no need to re-initialze them for each test).
	private static boolean dataLoaded = false;

	private static IIdType patient0Id = null;
	private static IIdType patient1Id = null;
	private static IIdType patient2Id = null;

	private static final Map<String, String> observationPatientMap = new HashMap<>();
	private static final Map<String, String> observationCategoryMap = new HashMap<>();
	private static final Map<String, String> observationCodeMap = new HashMap<>();
	private static final Map<String, Date> observationEffectiveMap = new HashMap<>();

	@Before
	public void beforeCreateTestPatientsAndObservations() {
		// Using a static flag here to ensure that load is only done once. Reason for this is that we cannot
		// access Autowired objects in @BeforeClass method.
		if(!dataLoaded) {
			Patient pt = new Patient();
			pt.addName().setFamily("Lastn").addGiven("Arthur");
			patient0Id = myPatientDao.create(pt, mockSrd()).getId().toUnqualifiedVersionless();
			createObservationsForPatient(patient0Id);
			pt = new Patient();
			pt.addName().setFamily("Lastn").addGiven("Johnathan");
			patient1Id = myPatientDao.create(pt, mockSrd()).getId().toUnqualifiedVersionless();
			createObservationsForPatient(patient1Id);
			pt = new Patient();
			pt.addName().setFamily("Lastn").addGiven("Michael");
			patient2Id = myPatientDao.create(pt, mockSrd()).getId().toUnqualifiedVersionless();
			createObservationsForPatient(patient2Id);
			dataLoaded = true;
		}

	}

	private void createObservationsForPatient(IIdType thePatientId) {
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd0, categoryCd0, 15);
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd0, categoryCd1, 10);
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd0, categoryCd2, 5);
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd1, categoryCd0, 10);
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd1, categoryCd1, 5);
		createFiveObservationsForPatientCodeCategory(thePatientId,observationCd2, categoryCd2, 5);
	}

	private void createFiveObservationsForPatientCodeCategory(IIdType thePatientId, String theObservationCode, String theCategoryCode,
																				 Integer theTimeOffset) {
		Calendar observationDate = new GregorianCalendar();

		for (int idx=0; idx<5; idx++ ) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(thePatientId);
			obs.getCode().addCoding().setCode(theObservationCode).setSystem(codeSystem);
			obs.setValue(new StringType(theObservationCode + "_0"));
			observationDate.add(Calendar.HOUR, -theTimeOffset+idx);
			Date effectiveDtm = observationDate.getTime();
			obs.setEffective(new DateTimeType(effectiveDtm));
			obs.getCategoryFirstRep().addCoding().setCode(theCategoryCode).setSystem(categorySystem);
			String observationId = myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless().getValue();
			observationPatientMap.put(observationId, thePatientId.getValue());
			observationCategoryMap.put(observationId, theCategoryCode);
			observationCodeMap.put(observationId, theObservationCode);
			observationEffectiveMap.put(observationId, effectiveDtm);
		}
	}

	private ServletRequestDetails mockSrd() {
		return mySrd;
	}

	@Test
	public void testLastNNoParams() {

		SearchParameterMap params = new SearchParameterMap();

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null,90);
	}

	private void executeTestCase(SearchParameterMap params, List<String> sortedPatients, List<String> sortedObservationCodes, List<String> theCategories, int expectedObservationCount) {
		List<String> actual;
		params.setLastN(true);

		Map<String, String[]> requestParameters = new HashMap<>();
		String[] maxParam = new String[1];
		maxParam[0] = "100";
		requestParameters.put("max", maxParam);
		when(mySrd.getParameters()).thenReturn(requestParameters);

		actual = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(),null));

		assertEquals(expectedObservationCount, actual.size());

		validateSorting(actual, sortedPatients, sortedObservationCodes, theCategories);
	}

	private void validateSorting(List<String> theObservationIds, List<String> thePatientIds, List<String> theCodes, List<String> theCategores) {
		int theNextObservationIdx = 0;
		// Validate patient grouping
		for (String patientId : thePatientIds) {
			assertEquals(patientId, observationPatientMap.get(theObservationIds.get(theNextObservationIdx)));
			theNextObservationIdx = validateSortingWithinPatient(theObservationIds,theNextObservationIdx,theCodes, theCategores, patientId);
		}
		assertEquals(theObservationIds.size(), theNextObservationIdx);
	}

	private int validateSortingWithinPatient(List<String> theObservationIds, int theFirstObservationIdxForPatient, List<String> theCodes,
														  List<String> theCategories, String thePatientId) {
		int theNextObservationIdx = theFirstObservationIdxForPatient;
		for (String codeValue : theCodes) {
			assertEquals(codeValue, observationCodeMap.get(theObservationIds.get(theNextObservationIdx)));
			// Validate sorting within code group
			theNextObservationIdx = validateSortingWithinCode(theObservationIds,theNextObservationIdx,
				observationCodeMap.get(theObservationIds.get(theNextObservationIdx)), theCategories, thePatientId);
		}
		return theNextObservationIdx;
	}

	private int validateSortingWithinCode(List<String> theObservationIds, int theFirstObservationIdxForPatientAndCode, String theObservationCode,
													  List<String> theCategories, String thePatientId) {
		int theNextObservationIdx = theFirstObservationIdxForPatientAndCode;
		Date lastEffectiveDt = observationEffectiveMap.get(theObservationIds.get(theNextObservationIdx));
		theNextObservationIdx++;
		while(theObservationCode.equals(observationCodeMap.get(theObservationIds.get(theNextObservationIdx)))
		&& thePatientId.equals(observationPatientMap.get(theObservationIds.get(theNextObservationIdx)))) {
			// Check that effective date is before that of the previous observation.
			assertTrue(lastEffectiveDt.compareTo(observationEffectiveMap.get(theObservationIds.get(theNextObservationIdx))) > 0);
			lastEffectiveDt = observationEffectiveMap.get(theObservationIds.get(theNextObservationIdx));

			// Check that observation is in one of the specified categories (if applicable)
			if (theCategories != null && !theCategories.isEmpty()) {
				assertTrue(theCategories.contains(observationCategoryMap.get(theObservationIds.get(theNextObservationIdx))));
			}
			theNextObservationIdx++;
			if (theNextObservationIdx >= theObservationIds.size()) {
				// Have reached the end of the Observation list.
				break;
			}
		}
		return theNextObservationIdx;
	}

	@Test
	public void testLastNSinglePatient() {

		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients,sortedObservationCodes, null,30);

		params = new SearchParameterMap();
		ReferenceParam patientParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_PATIENT, buildReferenceAndListParam(patientParam));

		sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients,sortedObservationCodes, null,30);
	}

	private ReferenceAndListParam buildReferenceAndListParam(ReferenceParam... theReference) {
		ReferenceOrListParam myReferenceOrListParam = new ReferenceOrListParam();
		for (ReferenceParam referenceParam : theReference) {
			myReferenceOrListParam.addOr(referenceParam);
		}
		return new ReferenceAndListParam().addAnd(myReferenceOrListParam);
	}

	@Test
	public void testLastNMultiplePatients() {

		// Two Subject parameters.
		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2));

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null,60);

		// Two Patient parameters
		params = new SearchParameterMap();
		ReferenceParam patientParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam patientParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(patientParam1, patientParam3));

		sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		executeTestCase(params,sortedPatients, sortedObservationCodes, null,60);

	}

	@Test
	public void testLastNSingleCategory() {

		// One category parameter.
		SearchParameterMap params = new SearchParameterMap();
		TokenParam categoryParam = new TokenParam(categorySystem, categoryCd0);
		params.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		List<String> myCategories = new ArrayList<>();
		myCategories.add(categoryCd0);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);

		executeTestCase(params, sortedPatients, sortedObservationCodes, myCategories, 30);

		// Another category parameter.
		params = new SearchParameterMap();
		categoryParam = new TokenParam(categorySystem, categoryCd2);
		params.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		myCategories = new ArrayList<>();
		myCategories.add(categoryCd2);

		sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, myCategories, 30);

	}

	@Test
	public void testLastNMultipleCategories() {

		// Two category parameters.
		SearchParameterMap params = new SearchParameterMap();
		TokenParam categoryParam1 = new TokenParam(categorySystem, categoryCd0);
		TokenParam categoryParam2 = new TokenParam(categorySystem, categoryCd1);
		params.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam1, categoryParam2));
		List<String> myCategories = new ArrayList<>();
		myCategories.add(categoryCd0);
		myCategories.add(categoryCd1);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);

		executeTestCase(params, sortedPatients, sortedObservationCodes, myCategories, 60);
	}

	@Test
	public void testLastNSingleCode() {

		// One code parameter.
		SearchParameterMap params = new SearchParameterMap();
		TokenParam code = new TokenParam(codeSystem, observationCd0);
		params.add(Observation.SP_CODE, buildTokenAndListParam(code));
		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 45);

		// Another code parameter.
		params = new SearchParameterMap();
		code = new TokenParam(codeSystem, observationCd2);
		params.add(Observation.SP_CODE, buildTokenAndListParam(code));
		sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 15);

	}

	@Test
	public void testLastNMultipleCodes() {

		// Two code parameters.
		SearchParameterMap params = new SearchParameterMap();
		TokenParam codeParam1 = new TokenParam(codeSystem, observationCd0);
		TokenParam codeParam2 = new TokenParam(codeSystem, observationCd1);
		params.add(Observation.SP_CODE, buildTokenAndListParam(codeParam1, codeParam2));
		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 75);

	}

	@Test
	public void testLastNSinglePatientCategoryCode() {

		// One patient, category and code.
		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		TokenParam code = new TokenParam(codeSystem, observationCd0);
		params.add(Observation.SP_CODE, buildTokenAndListParam(code));
		TokenParam category = new TokenParam(categorySystem, categoryCd2);
		params.add(Observation.SP_CATEGORY, buildTokenAndListParam(category));

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);

		List<String> myCategories = new ArrayList<>();
		myCategories.add(categoryCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, myCategories, 5);

	}

	@Test
	public void testLastNMultiplePatientsCategoriesCodes() {

		// Two patients, categories and codes.
		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2));
		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());

		TokenParam codeParam1 = new TokenParam(codeSystem, observationCd0);
		TokenParam codeParam2 = new TokenParam(codeSystem, observationCd2);
		params.add(Observation.SP_CODE, buildTokenAndListParam(codeParam1, codeParam2));
		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd2);

		TokenParam categoryParam1 = new TokenParam(categorySystem, categoryCd1);
		TokenParam categoryParam2 = new TokenParam(categorySystem, categoryCd2);
		params.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam1, categoryParam2));
		List<String> myCategories = new ArrayList<>();
		myCategories.add(categoryCd1);
		myCategories.add(categoryCd2);

		executeTestCase(params, sortedPatients, sortedObservationCodes, myCategories, 30);

	}

	private TokenAndListParam buildTokenAndListParam(TokenParam... theToken) {
		TokenOrListParam myTokenOrListParam = new TokenOrListParam();
		for (TokenParam tokenParam : theToken) {
			myTokenOrListParam.addOr(tokenParam);
		}
		return new TokenAndListParam().addAnd(myTokenOrListParam);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
