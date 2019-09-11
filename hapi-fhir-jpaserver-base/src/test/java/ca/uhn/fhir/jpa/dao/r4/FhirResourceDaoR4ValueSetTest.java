package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoR4ValueSetTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValueSetTest.class);

	private IIdType myExtensionalVsId;

	@After
	public void after() {
		myDaoConfig.setPreExpandValueSetsExperimental(new DaoConfig().isPreExpandValueSetsExperimental());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Before
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
		
		CodeSystem upload2 = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(upload2, mySrd).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemBad() {
		UriType valueSetIdentifier = null;
		IdType id = null;
		CodeType code = new CodeType("8450-9-XXX");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isResult());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemGood() {
		UriType valueSetIdentifier = null;
		IdType id = null;
		CodeType code = new CodeType("8450-9");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure--expiration", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystem() {
		UriType valueSetIdentifier = new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdType id = null;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndBadDisplay() {
		UriType valueSetIdentifier = new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdType id = null;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = new StringType("Systolic blood pressure at First encounterXXXX");
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndGoodDisplay() {
		UriType valueSetIdentifier = new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdType id = null;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = new StringType("Systolic blood pressure at First encounter");
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeableConcept() {
		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = null;
		UriType system = null;
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setSystem("http://acme.org").setCode("11378-7");
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeableConceptWithExistingValueSetAndPreExpansionEnabled() {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = null;
		UriType system = null;
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setSystem("http://acme.org").setCode("11378-7");
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.saveDeferred();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystem() {
		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystemWithExistingValueSetAndPreExpansionEnabled() {
		myDaoConfig.setPreExpandValueSetsExperimental(true);

		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.saveDeferred();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testExpandById() throws IOException {
		String resp;

		ValueSet expanded = myValueSetDao.expand(myExtensionalVsId, null, mySrd);
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<ValueSet xmlns=\"http://hl7.org/fhir\">")); 
		assertThat(resp, containsString("<expansion>"));
		assertThat(resp, containsString("<contains>"));
		assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, containsString("<code value=\"8450-9\"/>"));
		assertThat(resp, containsString("<display value=\"Systolic blood pressure--expiration\"/>")); 
		assertThat(resp, containsString("</contains>"));
		assertThat(resp, containsString("<contains>"));
		assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, containsString("<code value=\"11378-7\"/>"));
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>")); 
		assertThat(resp, containsString("</contains>"));
		assertThat(resp, containsString("</expansion>"));

		/*
		 * Filter with display name
		 */

		expanded = myValueSetDao.expand(myExtensionalVsId, ("systolic"), mySrd);
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

	}
	
	@Test
	@Ignore
	public void testExpandByIdentifier() {
		ValueSet expanded = myValueSetDao.expandByIdentifier("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", "11378");
		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	/**
	 * This type of expansion doesn't really make sense..
	 */
	@Test
	@Ignore
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		ValueSet expanded = myValueSetDao.expand(toExpand, "11378");
		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	
	@Test
	public void testValidateCodeAgainstBuiltInValueSetAndCodeSystemWithValidCode() {
		IPrimitiveType<String> display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		StringType vsIdentifier = new StringType("http://hl7.org/fhir/ValueSet/yesnodontknow");
		StringType code = new StringType("Y");
		StringType system = new StringType("http://terminology.hl7.org/CodeSystem/v2-0136");
		ValidateCodeResult result = myValueSetDao.validateCode(vsIdentifier, null, code, system, display, coding, codeableConcept, mySrd);
		
		ourLog.info(result.getMessage());
		assertTrue(result.getMessage(), result.isResult());
		assertEquals("Yes", result.getDisplay());
	}

	
}


