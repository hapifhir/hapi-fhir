package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;

import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.provider.ResourceProviderDstu2Test;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/hapi-fhir-server-resourceproviders-dstu2.xml", "/fhir-jpabase-spring-test-config.xml" })
public class FhirResourceDaoValueSetDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoValueSetDstu2Test.class);

	private static IIdType vsid;

	@Autowired
	private FhirContext myCtx;

	@Autowired
	@Qualifier("mySystemDaoDstu2")
	private IFhirSystemDao<Bundle> mySystemDao;

	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;

	@Before
	@Transactional
	public void before01() {
		if (vsid == null) {
			FhirSystemDaoDstu2Test.doDeleteEverything(mySystemDao);
		}
	}

	@Before
	@Transactional
	public void before02() {
		if (vsid == null) {
			ValueSet upload = myCtx.newXmlParser().parseResource(ValueSet.class, new InputStreamReader(ResourceProviderDstu2Test.class.getResourceAsStream("/extensional-case-2.xml")));
			upload.setId("");
			vsid = myValueSetDao.create(upload).getId().toUnqualifiedVersionless();
		}
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
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
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
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
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
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
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
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
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
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeableConcept() {
		UriDt valueSetIdentifier = null;
		IIdType id = vsid;
		CodeDt code = null;
		UriDt system = null;
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = new CodeableConceptDt("http://loinc.org", "11378-7");
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystem() {
		UriDt valueSetIdentifier = null;
		IIdType id = vsid;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://loinc.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValueSetExpandOperation() throws IOException {
		String resp;

		ValueSet expanded = myValueSetDao.expand(vsid, null);
		resp = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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

		expanded = myValueSetDao.expand(vsid, new StringDt("systolic"));
		resp = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		/*
		 * Filter with code
		 */

		expanded = myValueSetDao.expand(vsid, new StringDt("11378"));
		resp = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on
	}

}
