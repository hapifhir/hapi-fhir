package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.CompositeOrListParam;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.checkerframework.checker.units.qual.C;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UsageContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hl7.fhir.r4.model.Observation.SP_VALUE_QUANTITY;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.mock;

public class FhirResourceDaoR4SearchFtTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchFtTest.class);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setAllowContainsSearches(true);
		myStorageSettings.setHibernateSearchIndexFullText(true);
		myStorageSettings.setHibernateSearchIndexSearchParams(false);

		mySearchParamRegistry.forceRefresh();
	}

	/**
	 * TODO mb Extract these tests and run on all: jpa, lucene, es, etc. {@link FhirResourceDaoR4SearchWithElasticSearchIT}
	 * {@link FhirResourceDaoR4SearchWithElasticSearchIT#testStringSearch}
	 */
	@Test
	public void testCodeTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Observation.SP_CODE, new TokenParam(null, "systolic").setModifier(TokenParamModifier.TEXT));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).contains(toValues(id1, id2));
//
//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).isEmpty();
//
//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		map.add(Constants.PARAM_CONTENT, new StringParam("obs1"));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).contains(toValues(id1));

	}

	@Test
	public void testResourceTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("systolic"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));

	}

	private ServletRequestDetails mockSrd() {
		return mySrd;
	}

	@Test
	public void testStringTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("AAAAA");
		obs1.setValue(new StringType("Systolic Blood Pressure"));
		obs1.setStatus(ObservationStatus.FINAL);
		IIdType id1 = myObservationDao.create(obs1, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("AAAAA");
		obs2.setValue(new StringType("Diastolic Blood Pressure"));
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType id2 = myObservationDao.create(obs2, mockSrd()).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Observation.SP_VALUE_STRING, new StringParam("Systol"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).as("Default search matches prefix").containsExactlyInAnyOrder(toValues(id1));

		map = new SearchParameterMap();
		map.add(Observation.SP_VALUE_STRING, new StringParam("Systolic Blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).as("Default search matches prefix, even with space").containsExactlyInAnyOrder(toValues(id1));


		// contains doesn't work
//		map = new SearchParameterMap();
//		map.add(Observation.SP_VALUE_STRING, new StringParam("sure").setContains(true));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).as("contains matches internal fragment").containsExactlyInAnyOrder(toValues(id1, id2));

	}

	@Test
	public void testSearchAndReindex() {
		Patient patient;
		SearchParameterMap map;

		patient = new Patient();
		patient.getText().setDivAsString("<div>DIVAAA</div>");
		patient.addName().addGiven("NAMEAAA");
		IIdType pId1 = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(pId1));

		/*
		 * Reindex
		 */

		patient = new Patient();
		patient.setId(pId1.getValue());
		patient.getText().setDivAsString("<div>DIVBBB</div>");
		patient.addName().addGiven("NAMEBBB");
		myPatientDao.update(patient, mockSrd());

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).isEmpty();

		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(pId1));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(toValues(pId1));

	}

	@Test
	public void testContextTypeValueSearch() {
		assertThat(myStorageSettings.isAutoSupportDefaultSearchParams()).isTrue();
		Library library = new Library();
		library.getType()
			.setCoding(Collections.singletonList(
				new Coding()
					.setSystem("http://terminology.hl7.org/CodeSystem/library-type")
					.setCode("asset-collection")));
		library.setUseContext(Collections.singletonList(
			new UsageContext()
				.setCode(new Coding()
					.setSystem("http://aphl.org/fhir/vsm/CodeSystem/usage-context-type")
					.setCode("specification-category"))));
		library.getUseContext()
			.get(0)
			.getValueCodeableConcept()
			.addCoding()
					.setCode("tes-release")
					.setSystem("http://aphl.org/fhir/vsm/CodeSystem/usage-context-type");
		myLibraryDao.create(library, mockSrd());

		// Build context-type search
		SearchParameterMap map = new SearchParameterMap();
		map.add(Library.SP_CONTEXT_TYPE, new TokenAndListParam()
			.addAnd(new TokenParam(null, "specification-category")) // The first element MUST be null!!!!!!
		);

		//assertThat(toUnqualifiedVersionlessIdValues(myLibraryDao.search(map, mockSrd()))).isNotEmpty();
		IBundleProvider bundleProvider = myLibraryDao.search(map, mockSrd());
		assertThat(bundleProvider.getAllResourceIds()).hasSize(1);

		// Build context-value search map
		map.clean();
		map.add(Library.SP_CONTEXT, new TokenAndListParam()
			.addAnd(new TokenParam(null, "tes-release")));
		bundleProvider = myLibraryDao.search(map, mockSrd());
		assertThat(bundleProvider.getAllResourceIds()).hasSize(1);

		// Build context-type-value map
		map.clean();
		map.add(Library.SP_CONTEXT_TYPE_VALUE, new CompositeAndListParam<>(TokenParam.class, TokenParam.class)
			.addAnd(new CompositeOrListParam<>(TokenParam.class, TokenParam.class)
				.addOr(new CompositeParam<>(
					new TokenParam("specification-category"),
					new TokenParam("http://aphl.org/fhir/vsm/CodeSystem/usage-context-type", "tes-release")
				))));
		myCaptureQueriesListener.clear();
		bundleProvider = myLibraryDao.search(map, mockSrd());
		myCaptureQueriesListener.logSelectQueries();
		assertThat(bundleProvider.getAllResourceIds()).hasSize(1);
	}

	@Test
	public void testEverythingInstanceWithContentFilter() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mockSrd()).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId2 = myPatientDao.create(pt2, mockSrd()).getId().toUnqualifiedVersionless();

		Device dev1 = new Device();
		dev1.setManufacturer("Some Manufacturer");
		IIdType devId1 = myDeviceDao.create(dev1, mockSrd()).getId().toUnqualifiedVersionless();

		Device dev2 = new Device();
		dev2.setManufacturer("Some Manufacturer 2");
		myDeviceDao.create(dev2, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getText().setDivAsString("<div>OBSTEXT1</div>");
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE1");
		obs1.setValue(new StringType("obsvalue1"));
		obs1.getDevice().setReferenceElement(devId1);
		IIdType obsId1 = myObservationDao.create(obs1, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getSubject().setReferenceElement(ptId1);
		obs2.getCode().addCoding().setCode("CODE2");
		obs2.setValue(new StringType("obsvalue2"));
		IIdType obsId2 = myObservationDao.create(obs2, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getSubject().setReferenceElement(ptId2);
		obs3.getCode().addCoding().setCode("CODE3");
		obs3.setValue(new StringType("obsvalue3"));
		IIdType obsId3 = myObservationDao.create(obs3, mockSrd()).getId().toUnqualifiedVersionless();

		HttpServletRequest request;
		List<String> actual;
		request = mock(HttpServletRequest.class);
		StringAndListParam param;
		PatientEverythingParameters everythingParams;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, mockSrd(), everythingParams, ptId1));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, devId1));

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obstext1")));
		everythingParams.setNarrative(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, mockSrd(), everythingParams, ptId1));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, devId1));

		request = mock(HttpServletRequest.class);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, mockSrd(), new PatientEverythingParameters(), ptId1));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, obsId2, devId1));

		/*
		 * Add another match
		 */

		Observation obs4 = new Observation();
		obs4.getSubject().setReferenceElement(ptId1);
		obs4.getCode().addCoding().setCode("CODE1");
		obs4.setValue(new StringType("obsvalue1"));
		IIdType obsId4 = myObservationDao.create(obs4, mockSrd()).getId().toUnqualifiedVersionless();
		assertThat(devId1).as(obsId1.getIdPart()).isNotEqualTo(obsId4.getIdPart());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, mockSrd(), everythingParams, ptId1));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, obsId4, devId1));

		/*
		 * Make one previous match no longer match
		 */

		obs1 = new Observation();
		obs1.setId(obsId1);
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE2");
		obs1.setValue(new StringType("obsvalue2"));
		myObservationDao.update(obs1, mockSrd());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, mockSrd(), everythingParams, ptId1));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId4));

	}

	@Test
	public void testEverythingTypeWithContentFilter() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mockSrd()).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId2 = myPatientDao.create(pt2, mockSrd()).getId().toUnqualifiedVersionless();

		Device dev1 = new Device();
		dev1.setManufacturer("Some Manufacturer");
		IIdType devId1 = myDeviceDao.create(dev1, mockSrd()).getId().toUnqualifiedVersionless();

		Device dev2 = new Device();
		dev2.setManufacturer("Some Manufacturer 2");
		myDeviceDao.create(dev2, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE1");
		obs1.setValue(new StringType("obsvalue1"));
		obs1.getDevice().setReferenceElement(devId1);
		IIdType obsId1 = myObservationDao.create(obs1, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getSubject().setReferenceElement(ptId1);
		obs2.getCode().addCoding().setCode("CODE2");
		obs2.setValue(new StringType("obsvalue2"));
		IIdType obsId2 = myObservationDao.create(obs2, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getSubject().setReferenceElement(ptId2);
		obs3.getCode().addCoding().setCode("CODE3");
		obs3.setValue(new StringType("obsvalue3"));
		IIdType obsId3 = myObservationDao.create(obs3, mockSrd()).getId().toUnqualifiedVersionless();

		HttpServletRequest request;
		List<String> actual;
		request = mock(HttpServletRequest.class);
		StringAndListParam param;
		PatientEverythingParameters everythingParams;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, mockSrd(), everythingParams, null));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, devId1));

		request = mock(HttpServletRequest.class);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, mockSrd(), new PatientEverythingParameters(), null));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId1, obsId2, devId1, ptId2, obsId3));

		/*
		 * Add another match
		 */

		Observation obs4 = new Observation();
		obs4.getSubject().setReferenceElement(ptId1);
		obs4.getCode().addCoding().setCode("CODE1");
		obs4.setValue(new StringType("obsvalue1"));
		IIdType obsId4 = myObservationDao.create(obs4, mockSrd()).getId().toUnqualifiedVersionless();
		assertThat(devId1).as(obsId1.getIdPart()).isNotEqualTo(obsId4.getIdPart());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, mockSrd(), everythingParams, null));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, ptId2, obsId1, obsId4, devId1));

		/*
		 * Make one previous match no longer match
		 */

		obs1 = new Observation();
		obs1.setId(obsId1);
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE2");
		obs1.setValue(new StringType("obsvalue2"));
		myObservationDao.update(obs1, mockSrd());

		param = new StringAndListParam();
		everythingParams = new PatientEverythingParameters();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		everythingParams.setContent(param);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, mockSrd(), everythingParams, null));
		assertThat(actual).containsExactlyInAnyOrder(toValues(ptId1, obsId4));

	}

	/**
	 * When processing transactions, we do two passes. Make sure we don't update the lucene index twice since that would
	 * be inefficient
	 */
	@Test
	public void testSearchDontReindexForUpdateWithIndexDisabled() {
		BaseHapiFhirDao.setDisableIncrementOnUpdateForUnitTest(true);
		Patient patient;
		SearchParameterMap map;

		patient = new Patient();
		patient.getText().setDivAsString("<div>DIVAAA</div>");
		patient.addName().addGiven("NAMEAAA");
		final IIdType pId1 = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		String[] pidTypeArray = toValues(pId1);
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);

		IBundleProvider provider =  myPatientDao.search(new SearchParameterMap(), mockSrd());
		/*
		 * Update but don't reindex
		 */

		patient = new Patient();
		patient.setId(pId1);
		patient.getText().setDivAsString("<div>DIVBBB</div>");
		patient.addName().addGiven("NAMEBBB");
		myPatientDao.update(patient, null, false, mockSrd());

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);
		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
        List<String> actual = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
        AssertionsForInterfaceTypes.assertThat(actual).doesNotContainAnyElementsOf(Arrays.asList(pidTypeArray));

        myPatientDao.update(patient, null, true, mockSrd());

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).isEmpty();

		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(pidTypeArray);

	}

	@Test
	public void testSearchWithChainedParams() {
		String methodName = "testSearchWithChainedParams";
		IIdType pId1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven(methodName);
			patient.addAddress().addLine("My fulltext address");
			pId1 = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();
		}

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(pId1);
		obs.setValue(new StringType("This is the FULLtext of the observation"));
		IIdType oId1 = myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getSubject().setReferenceElement(pId1);
		obs.setValue(new StringType("Another fullText"));
		IIdType oId2 = myObservationDao.create(obs, mockSrd()).getId().toUnqualifiedVersionless();

		List<String> patients;
		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("fulltext"));
		patients = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patients).containsExactlyInAnyOrder(toValues(pId1));

		params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("FULLTEXT"));
		patients = toUnqualifiedVersionlessIdValues(myObservationDao.search(params));
		assertThat(patients).containsExactlyInAnyOrder(toValues(oId1, oId2));

	}

	/**
	 * make sure we provide a clear error message when a feature requires Elastic
	 */
	@Test
	public void tokenAutocompleteFailsWithLucene() {
		try {
			myFulltestSearchSvc.tokenAutocompleteValueSetSearch(new ValueSetAutocompleteOptions("Observation.code", null, null));
			fail("Expected exception");
		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).startsWith(Msg.code(2070));
		}
	}


	@Test
	public void testResourceQuantitySearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(SP_VALUE_QUANTITY, new QuantityParam("ap122"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactly(toValues(id1));

		map = new SearchParameterMap();
		map.add(SP_VALUE_QUANTITY, new QuantityParam("le90"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactly(toValues(id2));

		map = new SearchParameterMap();
		map.add(SP_VALUE_QUANTITY, new QuantityParam("gt80"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id1, id2));

		map = new SearchParameterMap();
		map.add(SP_VALUE_QUANTITY, new QuantityParam("gt80"));
		map.add(SP_VALUE_QUANTITY, new QuantityParam("lt90"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactly(toValues(id2));

	}

	/**
	 * When Hibernate Search is enabled but HibernateSearchIndexFullText is false,
	 * a _text search should still be permitted (returning empty results if nothing
	 * was indexed). This is a regression from HAPI 8.0.0 where an overly strict
	 * validation check was added that throws HAPI-2566.
	 *
	 * @see <a href="https://smile-cdr.atlassian.net/browse/SMILE-11363">SMILE-11363</a>
	 */
	@Test
	void testTextSearch_withHibernateSearchEnabled_andFullTextIndexDisabled_shouldNotThrowError() {
		// Setup: disable fulltext indexing but keep Hibernate Search enabled (bean is present)
		myStorageSettings.setHibernateSearchIndexFullText(false);
		mySearchParamRegistry.forceRefresh();

		// Create a patient with narrative text (manually populated, not auto-indexed)
		Patient patient = new Patient();
		patient.getText().setDivAsString("<div>Some narrative text for searching</div>");
		patient.addName().addGiven("TestPatient");
		myPatientDao.create(patient, mySrd);

		// Test: _text search should execute without error
		// The desired behavior is that the search succeeds (empty or with results),
		// NOT that it throws HAPI-2566
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("narrative"));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		// Assert: we get a result bundle (possibly empty), not an exception
		assertThat(results).isNotNull();
		assertThat(results.getAllResources()).isNotNull();
	}

	/**
	 * Adjacent test: _text search with HibernateSearchIndexFullText=true should work normally.
	 * This verifies the standard (non-regression) path still functions.
	 */
	@Test
	void testTextSearch_withHibernateSearchEnabled_andFullTextIndexEnabled_shouldWork() {
		// Setup: both HS and fulltext indexing enabled (the @BeforeEach default)
		// myStorageSettings.setHibernateSearchIndexFullText(true) is already set in @BeforeEach

		Patient patient = new Patient();
		patient.getText().setDivAsString("<div>DIVTEXT</div>");
		patient.addName().addGiven("TestPatient");
		IIdType patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		// Test: _text search should work and return the patient
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVTEXT"));
		IBundleProvider results = myPatientDao.search(map, mySrd);

		assertThat(results).isNotNull();
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactly(toValues(patientId));
	}

	/**
	 * Adjacent test: when HibernateSearchIndexFullText is false, the search parameter
	 * registry should still register _text and _content as active search parameters
	 * (since Hibernate Search is enabled). Currently, the registry gates these params
	 * behind isHibernateSearchIndexFullText(), which is a secondary bug.
	 */
	@Test
	void testSearchParamRegistry_withFullTextIndexDisabled_shouldStillRegisterTextParam() {
		// Setup: disable fulltext indexing but keep Hibernate Search enabled
		myStorageSettings.setHibernateSearchIndexFullText(false);
		mySearchParamRegistry.forceRefresh();

		// Assert: _text should still be registered as an active search parameter
		boolean hasTextParam = mySearchParamRegistry.hasActiveSearchParam(
			"Patient",
			Constants.PARAM_TEXT,
			ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);

		assertThat(hasTextParam)
			.as("_text search parameter should be active when Hibernate Search is enabled, regardless of fulltext indexing setting")
			.isTrue();
	}

}
