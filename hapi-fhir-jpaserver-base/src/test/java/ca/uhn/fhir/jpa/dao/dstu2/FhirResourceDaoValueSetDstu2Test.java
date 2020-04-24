package ca.uhn.fhir.jpa.dao.dstu2;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoValueSetDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoValueSetDstu2Test.class);

	private IIdType myExtensionalVsId;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}



	@Before
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		upload.setId("");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemBad() {
		UriDt valueSetIdentifier = null;
		IdDt id = null;
		CodeDt code = new CodeDt("8450-9-XXX");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isResult());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemGood() {
		UriDt valueSetIdentifier = null;
		IdDt id = null;
		CodeDt code = new CodeDt("8450-9");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure--expiration", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystem() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndBadDisplay() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = new StringDt("Systolic blood pressure at First encounterXXXX");
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndGoodDisplay() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = new StringDt("Systolic blood pressure at First encounter");
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeableConcept() {
		UriDt valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeDt code = null;
		UriDt system = null;
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = new CodeableConceptDt("http://loinc.org", "11378-7");
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystem() {
		UriDt valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testExpandById() {
		String resp;

		ValueSet expanded = myValueSetDao.expand(myExtensionalVsId, null, mySrd);
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		// @formatter:off
		assertThat(resp,
			stringContainsInOrder("<ValueSet xmlns=\"http://hl7.org/fhir\">", 
				"<expansion>", 
					"<contains>", 
						"<system value=\"http://loinc.org\"/>",
						"<code value=\"11378-7\"/>",
						"<display value=\"Systolic blood pressure at First encounter\"/>", 
					"</contains>",
					"<contains>", 
						"<system value=\"http://loinc.org\"/>",
						"<code value=\"8450-9\"/>", 
						"<display value=\"Systolic blood pressure--expiration\"/>", 
					"</contains>",
				"</expansion>" 
					));
		//@formatter:on

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

		/*
		 * Filter with code
		 */

		expanded = myValueSetDao.expand(myExtensionalVsId, ("11378"), mySrd);
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on
	}
	
	@Test
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

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
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

}
