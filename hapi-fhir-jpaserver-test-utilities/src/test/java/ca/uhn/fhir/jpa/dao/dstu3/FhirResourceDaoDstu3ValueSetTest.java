package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3ValueSetTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ValueSetTest.class);

	private IIdType myExtensionalVsId;
	@Autowired
	private IValidationSupport myValidationSupport;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem upload2 = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(upload2, mySrd).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testExpandValueSetWithIso3166() throws IOException {
		ValueSet vs = loadResourceFromClasspath(ValueSet.class, "/dstu3/nl/LandISOCodelijst-2.16.840.1.113883.2.4.3.11.60.40.2.20.5.2--20171231000000.json");
		myValueSetDao.create(vs);

		runInTransaction(() -> {
			TermValueSet vsEntity = myTermValueSetDao.findByUrl("http://decor.nictiz.nl/fhir/ValueSet/2.16.840.1.113883.2.4.3.11.60.40.2.20.5.2--20171231000000").orElseThrow(() -> new IllegalStateException());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, vsEntity.getExpansionStatus());
		});

		IValidationSupport.CodeValidationResult validationOutcome;
		UriType vsIdentifier = new UriType("http://decor.nictiz.nl/fhir/ValueSet/2.16.840.1.113883.2.4.3.11.60.40.2.20.5.2--20171231000000");
		CodeType code = new CodeType();
		CodeType system = new CodeType("urn:iso:std:iso:3166");

		// Validate good
		code.setValue("NL");
		validationOutcome = myValueSetDao.validateCode(vsIdentifier, null, code, system, null, null, null, mySrd);
		assertEquals(true, validationOutcome.isOk());

		// Validate bad
		code.setValue("QQ");
		validationOutcome = myValueSetDao.validateCode(vsIdentifier, null, code, system, null, null, null, mySrd);
		assertEquals(false, validationOutcome.isOk());

		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			TermValueSet vsEntity = myTermValueSetDao.findByUrl("http://decor.nictiz.nl/fhir/ValueSet/2.16.840.1.113883.2.4.3.11.60.40.2.20.5.2--20171231000000").orElseThrow(() -> new IllegalStateException());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, vsEntity.getExpansionStatus());
		});

		// Validate good
		code.setValue("NL");
		validationOutcome = myValueSetDao.validateCode(vsIdentifier, null, code, system, null, null, null, mySrd);
		assertEquals(true, validationOutcome.isOk());

		// Validate bad
		code.setValue("QQ");
		validationOutcome = myValueSetDao.validateCode(vsIdentifier, null, code, system, null, null, null, mySrd);
		assertEquals(false, validationOutcome.isOk());

	}

	private boolean clearDeferredStorageQueue() {

		if (!myTerminologyDeferredStorageSvc.isStorageQueueEmpty()) {
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			return false;
		} else {
			return true;
		}

	}

	@Test
	@Disabled
	public void testBuiltInValueSetFetchAndExpand() {

		try {
			myValueSetDao.read(new IdType("ValueSet/endpoint-payload-type"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		ValueSet vs = myValidationSupport.fetchResource(ValueSet.class, "http://hl7.org/fhir/ValueSet/endpoint-payload-type");
		myValueSetDao.update(vs);

		vs = myValueSetDao.read(new IdType("ValueSet/endpoint-payload-type"));
		assertNotNull(vs);
		assertEquals("http://hl7.org/fhir/ValueSet/endpoint-payload-type", vs.getUrl());

		ValueSet expansion = myValueSetDao.expand(vs.getIdElement(), null, mySrd);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expansion));
	}

	@Test
	public void testExpandById() {
		String resp;

		ValueSet expanded = myValueSetDao.expand(myExtensionalVsId, null, mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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

		expanded = myValueSetDao.expand(myExtensionalVsId, new ValueSetExpansionOptions().setFilter("systolic"), mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

	}

	@Test
	@Disabled
	public void testExpandByIdentifier() {
		ValueSet expanded = myValueSetDao.expandByIdentifier("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", new ValueSetExpansionOptions().setFilter("11378"));
		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
	@Disabled
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		ValueSet expanded = myValueSetDao.expand(toExpand, new ValueSetExpansionOptions().setFilter("11378"));
		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertFalse(result.isOk());
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeAgainstBuiltInValueSetAndCodeSystemWithValidCode() {
		IPrimitiveType<String> display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		StringType vsIdentifier = new StringType("http://hl7.org/fhir/ValueSet/administrative-gender");
		StringType code = new StringType("male");
		StringType system = new StringType("http://hl7.org/fhir/administrative-gender");
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(vsIdentifier, null, code, system, display, coding, codeableConcept, mySrd);

		ourLog.info(result.getMessage());
		assertTrue(result.isOk(), result.getMessage());
	}


}

