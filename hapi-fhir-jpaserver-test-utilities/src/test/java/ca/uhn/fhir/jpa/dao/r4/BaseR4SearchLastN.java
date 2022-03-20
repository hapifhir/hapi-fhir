package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
abstract public class BaseR4SearchLastN extends BaseJpaTest {

	private static final Map<String, String> observationPatientMap = new HashMap<>();
	private static final Map<String, String> observationCategoryMap = new HashMap<>();
	private static final Map<String, String> observationCodeMap = new HashMap<>();
	private static final Map<String, Date> observationEffectiveMap = new HashMap<>();
	protected static IIdType patient0Id = null;
	protected static IIdType patient1Id = null;
	protected static IIdType patient2Id = null;
	// Using static variables including the flag below so that we can initalize the database and indexes once
	// (all of the tests only read from the DB and indexes and so no need to re-initialze them for each test).
	private static boolean dataLoaded = false;
	private static Calendar observationDate = new GregorianCalendar();
	protected final String observationCd0 = "code0";
	protected final String observationCd1 = "code1";
	protected final String observationCd2 = "code2";
	protected final String categoryCd0 = "category0";
	protected final String codeSystem = "http://mycode.com";
	private final String observationCd3 = "code3";
	private final String categoryCd1 = "category1";
	private final String categoryCd2 = "category2";
	private final String categoryCd3 = "category3";
	private final String categorySystem = "http://mycategory.com";
	@Autowired
	@Qualifier("myPatientDaoR4")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myObservationDaoR4")
	protected IFhirResourceDaoObservation<Observation> myObservationDao;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	@SpyBean
	@Autowired
	protected ElasticsearchSvcImpl myElasticsearchSvc;

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myPlatformTransactionManager;
	}

	@AfterEach
	public void afterDisableLastN() {
		myDaoConfig.setLastNEnabled(new DaoConfig().isLastNEnabled());
	}

	@BeforeEach
	public void beforeCreateTestPatientsAndObservations() throws IOException {
		myDaoConfig.setLastNEnabled(true);

		// Using a static flag to ensure that test data and elasticsearch index is only created once.
		// Creating this data and the index is time consuming and as such want to avoid having to repeat for each test.
		// Normally would use a static @BeforeClass method for this purpose, but Autowired objects cannot be accessed in static methods.
		if (!dataLoaded || patient0Id == null) {
			// enabled to also create extended lucene index during creation of test data
			myDaoConfig.setAdvancedLuceneIndexing(true);
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

			myElasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
			myElasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
			// turn off the setting enabled earlier
			myDaoConfig.setAdvancedLuceneIndexing(false);
		}

	}

	private void createObservationsForPatient(IIdType thePatientId) {
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd0, categoryCd0, 15);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd0, categoryCd1, 10);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd0, categoryCd2, 5);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd1, categoryCd0, 10);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd1, categoryCd1, 5);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd2, categoryCd2, 5);
		createFiveObservationsForPatientCodeCategory(thePatientId, observationCd3, categoryCd3, 5);
	}

	/**
	 * Create and return observation ids
	 */
	protected List<IIdType> createFiveObservationsForPatientCodeCategory(IIdType thePatientId, String theObservationCode, String theCategoryCode,
																								Integer theTimeOffset) {
		List<IIdType> observerationIds = new ArrayList<>();

		for (int idx = 0; idx < 5; idx++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(thePatientId);
			obs.getCode().addCoding().setCode(theObservationCode).setSystem(codeSystem);
			obs.setValue(new StringType(theObservationCode + "_0"));
			Date effectiveDtm = calculateObservationDateFromOffset(theTimeOffset, idx);
			obs.setEffective(new DateTimeType(effectiveDtm));
			obs.getCategoryFirstRep().addCoding().setCode(theCategoryCode).setSystem(categorySystem);
			IIdType observationId = myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless();
			String observationIdValue = observationId.getValue();
			observationPatientMap.put(observationIdValue, thePatientId.getValue());
			observationCategoryMap.put(observationIdValue, theCategoryCode);
			observationCodeMap.put(observationIdValue, theObservationCode);
			observationEffectiveMap.put(observationIdValue, effectiveDtm);
			observerationIds.add(observationId);
		}
		return observerationIds;
	}

	private Date calculateObservationDateFromOffset(Integer theTimeOffset, Integer theObservationIndex) {
		long milliSecondsPerHour = 3600L * 1000L;
		// Generate a Date by subtracting a calculated number of hours from the static observationDate property.
		return new Date(observationDate.getTimeInMillis() - (milliSecondsPerHour * (theTimeOffset + theObservationIndex)));
	}

	protected ServletRequestDetails mockSrd() {
		return mySrd;
	}

	@Test
	public void testLastNAllPatients() {

		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient1Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);
		sortedObservationCodes.add(observationCd3);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 105);
	}

	@Test
	public void testLastNNoPatients() {

		SearchParameterMap params = new SearchParameterMap();

		params.setLastN(true);
		Map<String, String[]> requestParameters = new HashMap<>();
		when(mySrd.getParameters()).thenReturn(requestParameters);

		List<String> actual = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(), null));

		assertEquals(4, actual.size());
	}

	private void executeTestCase(SearchParameterMap params, List<String> sortedPatients, List<String> sortedObservationCodes, List<String> theCategories, int expectedObservationCount) {
		List<String> actual;
		params.setLastN(true);

		Map<String, String[]> requestParameters = new HashMap<>();
		params.setLastNMax(100);

		when(mySrd.getParameters()).thenReturn(requestParameters);

		actual = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(), null));

		assertEquals(expectedObservationCount, actual.size());

		validateSorting(actual, sortedPatients, sortedObservationCodes, theCategories);
	}

	private void validateSorting(List<String> theObservationIds, List<String> thePatientIds, List<String> theCodes, List<String> theCategores) {
		int theNextObservationIdx = 0;
		// Validate patient grouping
		for (String patientId : thePatientIds) {
			assertEquals(patientId, observationPatientMap.get(theObservationIds.get(theNextObservationIdx)));
			theNextObservationIdx = validateSortingWithinPatient(theObservationIds, theNextObservationIdx, theCodes, theCategores, patientId);
		}
		assertEquals(theObservationIds.size(), theNextObservationIdx);
	}

	private int validateSortingWithinPatient(List<String> theObservationIds, int theFirstObservationIdxForPatient, List<String> theCodes,
														  List<String> theCategories, String thePatientId) {
		int theNextObservationIdx = theFirstObservationIdxForPatient;
		for (String codeValue : theCodes) {
			assertEquals(codeValue, observationCodeMap.get(theObservationIds.get(theNextObservationIdx)));
			// Validate sorting within code group
			theNextObservationIdx = validateSortingWithinCode(theObservationIds, theNextObservationIdx,
				observationCodeMap.get(theObservationIds.get(theNextObservationIdx)), theCategories, thePatientId);
		}
		return theNextObservationIdx;
	}

	private int validateSortingWithinCode(List<String> theObservationIds, int theFirstObservationIdxForPatientAndCode, String theObservationCode,
													  List<String> theCategories, String thePatientId) {
		int theNextObservationIdx = theFirstObservationIdxForPatientAndCode;
		Date lastEffectiveDt = observationEffectiveMap.get(theObservationIds.get(theNextObservationIdx));
		theNextObservationIdx++;
		while (theObservationCode.equals(observationCodeMap.get(theObservationIds.get(theNextObservationIdx)))
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
		sortedObservationCodes.add(observationCd3);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 35);

		params = new SearchParameterMap();
		ReferenceParam patientParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_PATIENT, buildReferenceAndListParam(patientParam));

		sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);
		sortedObservationCodes.add(observationCd2);
		sortedObservationCodes.add(observationCd3);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 35);
	}

	protected ReferenceAndListParam buildReferenceAndListParam(ReferenceParam... theReference) {
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
		sortedObservationCodes.add(observationCd3);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 70);

		// Two Patient parameters
		params = new SearchParameterMap();
		ReferenceParam patientParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam patientParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(patientParam1, patientParam3));

		sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());
		sortedPatients.add(patient2Id.getValue());

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 70);

	}

	@Test
	public void testLastNSingleCategory() {

		// One category parameter.
		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));

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
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));
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
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));

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
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));

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
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));
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
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));

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

	protected TokenAndListParam buildTokenAndListParam(TokenParam... theToken) {
		TokenOrListParam myTokenOrListParam = new TokenOrListParam();
		for (TokenParam tokenParam : theToken) {
			myTokenOrListParam.addOr(tokenParam);
		}
		return new TokenAndListParam().addAnd(myTokenOrListParam);
	}

	@Test
	public void testLastNSingleDate() {

		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));

		DateParam myDateParam = new DateParam(ParamPrefixEnum.LESSTHAN, new Date(observationDate.getTimeInMillis() - (3600 * 1000 * 9)));
		params.add(Observation.SP_DATE, myDateParam);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 15);

	}

	@Test
	public void testLastNMultipleDates() {

		SearchParameterMap params = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", patient0Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));

		DateParam lowDateParam = new DateParam(ParamPrefixEnum.LESSTHAN, new Date(observationDate.getTimeInMillis() - (3600 * 1000 * (9))));
		DateParam highDateParam = new DateParam(ParamPrefixEnum.GREATERTHAN, new Date(observationDate.getTimeInMillis() - (3600 * 1000 * (15))));
		DateAndListParam myDateAndListParam = new DateAndListParam();
		myDateAndListParam.addAnd(new DateOrListParam().addOr(lowDateParam));
		myDateAndListParam.addAnd(new DateOrListParam().addOr(highDateParam));

		params.add(Observation.SP_DATE, myDateAndListParam);

		List<String> sortedPatients = new ArrayList<>();
		sortedPatients.add(patient0Id.getValue());

		List<String> sortedObservationCodes = new ArrayList<>();
		sortedObservationCodes.add(observationCd0);
		sortedObservationCodes.add(observationCd1);

		executeTestCase(params, sortedPatients, sortedObservationCodes, null, 10);

	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
