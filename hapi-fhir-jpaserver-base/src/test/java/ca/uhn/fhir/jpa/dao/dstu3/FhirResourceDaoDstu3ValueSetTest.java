package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoDstu3ValueSetTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ValueSetTest.class);

	private IIdType myExtensionalVsId;
	@Autowired
	private IValidationSupport myValidationSupport;

	@Before
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem upload2 = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(upload2, mySrd).getId().toUnqualifiedVersionless();

	}

	@Test
	@Ignore
	public void testBuiltInValueSetFetchAndExpand() {

		try {
			myValueSetDao.read(new IdType("ValueSet/endpoint-payload-type"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		ValueSet vs = myValidationSupport.fetchResource(myFhirCtx, ValueSet.class, "http://hl7.org/fhir/ValueSet/endpoint-payload-type");
		myValueSetDao.update(vs);

		vs = myValueSetDao.read(new IdType("ValueSet/endpoint-payload-type"));
		assertNotNull(vs);
		assertEquals("http://hl7.org/fhir/ValueSet/endpoint-payload-type", vs.getUrl());

		ValueSet expansion = myValueSetDao.expand(vs.getIdElement(), null, mySrd);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expansion));
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
	public void testValiedateCodeAgainstBuiltInValueSetAndCodeSystemWithValidCode() {
		IPrimitiveType<String> display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		StringType vsIdentifier = new StringType("http://hl7.org/fhir/ValueSet/v2-0487");
		StringType code = new StringType("BRN");
		StringType system = new StringType("http://hl7.org/fhir/v2/0487");
		ValidateCodeResult result = myValueSetDao.validateCode(vsIdentifier, null, code, system, display, coding, codeableConcept, mySrd);

		ourLog.info(result.getMessage());
		assertTrue(result.getMessage(), result.isResult());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}

