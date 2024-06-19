package ca.uhn.fhir.jpa.dao.dstu2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.provider.ResourceProviderDstu2ValueSetTest;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class FhirResourceDaoValueSetDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoValueSetDstu2Test.class);

	private IIdType myExtensionalVsId;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		upload.setId("");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemBadCode() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("8450-9-XXX");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isOk());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemBadSystem() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("8450-9-XXX");
		UriDt system = new UriDt("http://zzz");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isOk());
	}

	@Test
	public void testValidateCodeOperationByIdentifierCodeInCsButNotInVs() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("8493-9");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isOk());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystem() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndBadDisplay() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = new StringDt("Systolic blood pressure at First encounterXXXX");
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Concept Display \"Systolic blood pressure at First encounterXXXX\" does not match expected \"Systolic blood pressure at First encounter\" for 'http://acme.org#11378-7' for in-memory expansion of ValueSet: http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", result.getMessage());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.WARNING, result.getSeverity());
	}

	@Test
	public void testValidateCodeOperationByIdentifierAndCodeAndSystemAndGoodDisplay() {
		UriDt valueSetIdentifier = new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		IdDt id = null;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = new StringDt("Systolic blood pressure at First encounter");
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertThat(result.isOk()).as(result.getMessage()).isTrue();
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
		CodeableConceptDt codeableConcept = new CodeableConceptDt("http://acme.org", "11378-7");
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystem() {
		UriDt valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeDt code = new CodeDt("11378-7");
		UriDt system = new UriDt("http://acme.org");
		StringDt display = null;
		CodingDt coding = null;
		CodeableConceptDt codeableConcept = null;
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testExpandById() {
		String resp;

		ValueSet expanded = myValueSetDao.expand(myExtensionalVsId, null, mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		// @formatter:off
		assertThat(resp).
			containsSubsequence("<ValueSet xmlns=\"http://hl7.org/fhir\">",
				"<expansion>",
				"<contains>",
				"<system value=\"http://acme.org\"/>",
				"<code value=\"11378-7\"/>",
				"<display value=\"Systolic blood pressure at First encounter\"/>",
				"</contains>",
				"<contains>",
				"<system value=\"http://acme.org\"/>",
				"<code value=\"8450-9\"/>",
				"<display value=\"Systolic blood pressure--expiration\"/>",
				"</contains>",
				"</expansion>"
			);
		//@formatter:on

		/*
		 * Filter with display name
		 */

		expanded = myValueSetDao.expand(myExtensionalVsId, new ValueSetExpansionOptions().setFilter("systolic"), mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp).containsSubsequence(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>");
		//@formatter:on

		/*
		 * Filter with code
		 */

		expanded = myValueSetDao.expand(myExtensionalVsId, new ValueSetExpansionOptions().setFilter("11378"), mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp).containsSubsequence(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>");
		//@formatter:on
	}

	@Test
	public void testExpandByIdentifier() {
		ValueSet expanded = myValueSetDao.expandByIdentifier("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", new ValueSetExpansionOptions().setFilter("11378"));
		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		List<String> codes = ResourceProviderDstu2ValueSetTest.toCodes(expanded);
		assertThat(codes).containsExactly("11378-7", "8450-9");
	}

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		ValueSet expanded = myValueSetDao.expand(toExpand, new ValueSetExpansionOptions().setFilter("11378"));
		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		List<String> codes = ResourceProviderDstu2ValueSetTest.toCodes(expanded);
		assertThat(codes).containsExactly("11378-7", "8450-9");
	}

}
