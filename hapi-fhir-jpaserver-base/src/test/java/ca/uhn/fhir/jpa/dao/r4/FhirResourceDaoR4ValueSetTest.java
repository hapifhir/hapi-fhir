package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FhirResourceDaoR4ValueSetTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValueSetTest.class);

	private IIdType myExtensionalVsId;

	@After
	public void after() {
		myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
		myDaoConfig.setMaximumExpansionSize(new DaoConfig().getMaximumExpansionSize());
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeableConceptWithExistingValueSetAndPreExpansionEnabled() {
		myDaoConfig.setPreExpandValueSets(true);

		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = null;
		UriType system = null;
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setSystem("http://acme.org").setCode("11378-7");
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTerminologyDeferredStorageSvc.saveDeferred();
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
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeOperationByResourceIdAndCodeAndSystemWithExistingValueSetAndPreExpansionEnabled() {
		myDaoConfig.setPreExpandValueSets(true);

		UriType valueSetIdentifier = null;
		IIdType id = myExtensionalVsId;
		CodeType code = new CodeType("11378-7");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTerminologyDeferredStorageSvc.saveDeferred();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isResult());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testExpandById() {
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
	public void testExpandByValueSet_ExceedsMaxSize() {
		// Add a bunch of codes
		CustomTerminologySet codesToAdd = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			codesToAdd.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://loinc.org", codesToAdd);
		myDaoConfig.setMaximumExpansionSize(50);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!", e.getMessage());
		}
	}

	
	@Test
	public void testValidateCodeAgainstBuiltInValueSetAndCodeSystemWithValidCode() {
		IPrimitiveType<String> display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		StringType vsIdentifier = new StringType("http://hl7.org/fhir/ValueSet/administrative-gender");
		StringType code = new StringType("male");
		StringType system = new StringType("http://hl7.org/fhir/administrative-gender");
		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(vsIdentifier, null, code, system, display, coding, codeableConcept, mySrd);
		
		ourLog.info(result.getMessage());
		assertTrue(result.getMessage(), result.isResult());
		assertEquals("Male", result.getDisplay());
	}

	
}


