package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;

import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl.Suggestion;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDstu3SearchFtTest extends BaseJpaDstu3Test {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SearchFtTest.class);

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testCodeTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.setComment("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
		
		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
		
		SearchParameterMap map;
		
		map = new SearchParameterMap();
		map.add(Observation.SP_CODE, new TokenParam(null, "systolic").setModifier(TokenParamModifier.TEXT));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));
//
//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());
//
//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "blood").setModifier(TokenParamModifier.TEXT));
//		map.add(Constants.PARAM_CONTENT, new StringParam("obs1"));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

	}


	@Test
	public void testResourceTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.setComment("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
		
		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();
		
		SearchParameterMap map;
		
		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("systolic"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("obs1"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

	}

	private ServletRequestDetails mockSrd() {
		return mySrd;
	}

	@Test
	@Ignore
	public void testStringTextSearch() {
		Observation obs1 = new Observation();
		obs1.getCode().setText("AAAAA");
		obs1.setValue(new StringType("Systolic Blood Pressure"));
		obs1.setStatus(ObservationStatus.FINAL);
		IIdType id1 = myObservationDao.create(obs1, mockSrd()).getId().toUnqualifiedVersionless();
		
		Observation obs2 = new Observation();
		obs1.getCode().setText("AAAAA");
		obs1.setValue(new StringType("Diastolic Blood Pressure"));
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType id2 = myObservationDao.create(obs2, mockSrd()).getId().toUnqualifiedVersionless();
		
		SearchParameterMap map;
		
		map = new SearchParameterMap();
		map.add(Observation.SP_VALUE_STRING, new StringParam("sure").setContains(true));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));

	}

	
	@Test
	public void testSuggestIgnoresBase64Content() {
		Patient patient = new Patient();
		patient.addName().setFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		Media med = new Media();
		med.getSubject().setReferenceElement(ptId);
		med.getSubtype().setText("Systolic Blood Pressure");
		med.getContent().setContentType("LCws");
		med.getContent().setDataElement(new Base64BinaryType(new byte[] {44,44,44,44,44,44,44,44}));
		med.getContent().setTitle("bbbb syst");
		myMediaDao.create(med, mockSrd());
		ourLog.info(myFhirCtx.newJsonParser().encodeResourceToString(med));
		
		List<Suggestion> output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "press", null);
		ourLog.info("Found: " + output);
		assertEquals(2, output.size());
		assertEquals("Pressure", output.get(0).getTerm());
		assertEquals("Systolic Blood Pressure", output.get(1).getTerm());

		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "prezure", null);
		ourLog.info("Found: " + output);
		assertEquals(2, output.size());
		assertEquals("Pressure", output.get(0).getTerm());
		assertEquals("Systolic Blood Pressure", output.get(1).getTerm());

		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "syst", null);
		ourLog.info("Found: " + output);
		assertEquals(4, output.size());
		assertEquals("syst", output.get(0).getTerm());
		assertEquals("bbbb syst", output.get(1).getTerm());
		assertEquals("Systolic", output.get(2).getTerm());
		assertEquals("Systolic Blood Pressure", output.get(3).getTerm());
		
		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "LCws", null);
		ourLog.info("Found: " + output);
		assertEquals(0, output.size());
	}
	
	@Test
	public void testSuggest() {
		Patient patient = new Patient();
		patient.addName().setFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		myObservationDao.create(obs, mockSrd());

		obs = new Observation();
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("MNBVCXZ");
		myObservationDao.create(obs, mockSrd());

		obs = new Observation();
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXC HELLO");
		obs.addComponent().getCode().setText("HHHHHHHHHH");
		myObservationDao.create(obs, mockSrd());

		/*
		 * These shouldn't match since they're for another patient
		 */
		patient = new Patient();
		patient.addName().setFamily("testSuggest2");
		IIdType ptId2 = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getSubject().setReferenceElement(ptId2);
		obs2.getCode().setText("ZXCVBNMZZ");
		myObservationDao.create(obs2, mockSrd());

		List<Suggestion> output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "ZXCVBNM", null);
		ourLog.info("Found: " + output);
		assertEquals(4, output.size());
		assertEquals("ZXCVBNM", output.get(0).getTerm());
		assertEquals("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL", output.get(1).getTerm());
		assertEquals("ZXC", output.get(2).getTerm());
		assertEquals("ZXC HELLO", output.get(3).getTerm());

		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "ZXC", null);
		ourLog.info("Found: " + output);
		assertEquals(4, output.size());
		assertEquals("ZXC", output.get(0).getTerm());
		assertEquals("ZXC HELLO", output.get(1).getTerm());
		assertEquals("ZXCVBNM", output.get(2).getTerm());
		assertEquals("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL", output.get(3).getTerm());

		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "HELO", null);
		ourLog.info("Found: " + output);
		assertEquals(2, output.size());
		assertEquals("HELLO", output.get(0).getTerm());
		assertEquals("ZXC HELLO", output.get(1).getTerm());
		
		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "Z", null);
		ourLog.info("Found: " + output);
		assertEquals(0, output.size());

		output = mySearchDao.suggestKeywords("Patient/" + ptId.getIdPart() + "/$everything", "_content", "ZX", null);
		ourLog.info("Found: " + output);
		assertEquals(2, output.size());
		assertEquals("ZXC", output.get(0).getTerm());
		assertEquals("ZXC HELLO", output.get(1).getTerm());

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
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

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
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());

		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(toValues(pId1)));

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
		
		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", new Object[] {ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart()});
		
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, ptId1, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, devId1)));

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obstext1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, ptId1, null, null, null, null, param, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, devId1)));

		request = mock(HttpServletRequest.class);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, ptId1, null, null, null, null, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, obsId2, devId1)));

		/*
		 * Add another match
		 */
		
		Observation obs4 = new Observation();
		obs4.getSubject().setReferenceElement(ptId1);
		obs4.getCode().addCoding().setCode("CODE1");
		obs4.setValue(new StringType("obsvalue1"));
		IIdType obsId4 = myObservationDao.create(obs4, mockSrd()).getId().toUnqualifiedVersionless();
		assertNotEquals(obsId1.getIdPart(), obsId4.getIdPart(), devId1);

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, ptId1, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, obsId4, devId1)));

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
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientInstanceEverything(request, ptId1, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId4)));

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
		
		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", new Object[] {ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart()});
		
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, devId1)));

		request = mock(HttpServletRequest.class);
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, null, null, null, null, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, obsId2, devId1, ptId2, obsId3)));

		/*
		 * Add another match
		 */
		
		Observation obs4 = new Observation();
		obs4.getSubject().setReferenceElement(ptId1);
		obs4.getCode().addCoding().setCode("CODE1");
		obs4.setValue(new StringType("obsvalue1"));
		IIdType obsId4 = myObservationDao.create(obs4, mockSrd()).getId().toUnqualifiedVersionless();
		assertNotEquals(obsId1.getIdPart(), obsId4.getIdPart(), devId1);

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId1, obsId4, devId1)));

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
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));
		actual = toUnqualifiedVersionlessIdValues(myPatientDao.patientTypeEverything(request, null, null, null, param, null, null, mockSrd()));
		assertThat(actual, containsInAnyOrder(toValues(ptId1, obsId4)));

	}

	
	/**
	 * When processing transactions, we do two passes. Make sure we don't update the lucene index twice since that would
	 * be inefficient
	 */
	@Test
	public void testSearchDontReindexForUpdateWithIndexDisabled() {
		BaseHapiFhirResourceDao.setDisableIncrementOnUpdateForUnitTest(true);
		Patient patient;
		SearchParameterMap map;

		patient = new Patient();
		patient.getText().setDivAsString("<div>DIVAAA</div>");
		patient.addName().addGiven("NAMEAAA");
		final IIdType pId1 = myPatientDao.create(patient, mockSrd()).getId().toUnqualifiedVersionless();

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		String[] idArray = toValues(pId1);
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));

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
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));
		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), not(contains(idArray)));

		myPatientDao.update(patient, null, true, mockSrd());

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEAAA"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), empty());

		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("NAMEBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_TEXT, new StringParam("DIVBBB"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), contains(idArray));

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
		assertThat(patients, containsInAnyOrder(toValues(pId1)));

		params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("FULLTEXT"));
		patients = toUnqualifiedVersionlessIdValues(myObservationDao.search(params));
		assertThat(patients, containsInAnyOrder(toValues(oId1, oId2)));

	}

}
