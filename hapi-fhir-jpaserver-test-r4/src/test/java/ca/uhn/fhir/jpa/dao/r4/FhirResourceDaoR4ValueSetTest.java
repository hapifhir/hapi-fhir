package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4ValueSetTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValueSetTest.class);

	private IIdType myExtensionalVsId;

	@AfterEach
	public void after() {
		BaseTermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);
		myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
		myDaoConfig.setMaximumExpansionSize(new DaoConfig().getMaximumExpansionSize());
	}


	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem upload2 = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(upload2, mySrd).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testValidateCodeInValueSet_ValueSetUrlUsedInsteadOfCodeSystem() throws IOException {
		myCodeSystemDao.update(loadResourceFromClasspath(CodeSystem.class, "r4/adi-cs.json"));
		myValueSetDao.update(loadResourceFromClasspath(ValueSet.class, "r4/adi-vs.json"));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome = myValidationSupport.validateCode(context, options, "http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc", "378397893", null, "http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc");
		assertFalse(outcome.isOk());
		assertEquals("Unable to validate code http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc#378397893 - Supplied system URL is a ValueSet URL and not a CodeSystem URL, check if it is correct: http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc", outcome.getMessage());
	}


	@Test
	public void testValidateCodeInValueSet_SystemThatAppearsNowhereInValueSet() throws IOException {
		myCodeSystemDao.update(loadResourceFromClasspath(CodeSystem.class, "r4/adi-cs.json"));
		myValueSetDao.update(loadResourceFromClasspath(ValueSet.class, "r4/adi-vs.json"));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		logAllValueSetConcepts();

		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome = myValidationSupport.validateCode(context, options, "http://payer-to-payer-exchange/fhir/CodeSystem/ndc", "378397893", null, "http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc");
		assertFalse(outcome.isOk());
		assertEquals("Unable to validate code http://payer-to-payer-exchange/fhir/CodeSystem/ndc#378397893 - No codes in ValueSet belong to CodeSystem with URL http://payer-to-payer-exchange/fhir/CodeSystem/ndc", outcome.getMessage());
	}


	@Test
	public void testValidateCodeInValueSet_HierarchicalAndEnumeratedValueset() {
		myValueSetDao.delete(myExtensionalVsId);

		ourLog.info("Creating CodeSystem");
		CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/cs");
		cs.setUrl("http://cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.update(cs);

		ourLog.info("Adding codes to codesystem");
		CustomTerminologySet delta = new CustomTerminologySet();
		TermConcept parent = delta.addRootConcept("parent");
		for (int j = 0; j < 1200; j++) {
			parent
				.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA)
				.setCode("child" + j);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://cs", delta);

		ourLog.info("Creating ValueSet");
		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/vs");
		vs.setUrl("http://vs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addFilter()
			.setProperty("concept")
			.setOp(ValueSet.FilterOperator.ISA)
			.setValue("parent");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs-np")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code0")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")));
		myValueSetDao.update(vs);

		IValidationSupport.CodeValidationResult outcome;
		ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();

		// In memory - Hierarchy in existing CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "child10", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "childX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://cs#childX' for in-memory expansion of ValueSet 'http://vs'", outcome.getMessage());

		// In memory - Enumerated in non-present CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "code1", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "codeX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://cs-np#codeX' for in-memory expansion of ValueSet 'http://vs'", outcome.getMessage());

		// Precalculated

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		logAllValueSets();
		myCachingValidationSupport.invalidateCaches();

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "child10", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertThat(outcome.getMessage(), startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "childX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertThat(outcome.getMessage(), containsString("Unknown code http://cs#childX"));
		assertThat(outcome.getMessage(), containsString("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		// Precalculated - Enumerated in non-present CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "code1", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertThat(outcome.getMessage(), startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "codeX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertThat(outcome.getMessage(), containsString("Unknown code http://cs-np#codeX"));
		assertThat(outcome.getMessage(), containsString("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

	}

	@Test
	public void testValidateCodeInValueSet_HierarchicalAndEnumeratedValueset_HibernateSearchDisabled() {
		BaseTermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(true);

		myValueSetDao.delete(myExtensionalVsId);

		ourLog.info("Creating CodeSystem");
		CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/cs");
		cs.setUrl("http://cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.update(cs);

		ourLog.info("Adding codes to codesystem");
		CustomTerminologySet delta = new CustomTerminologySet();
		TermConcept parent = delta.addRootConcept("parent");
		for (int j = 0; j < 1200; j++) {
			parent
				.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA)
				.setCode("child" + j);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://cs", delta);

		ourLog.info("Creating ValueSet");
		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/vs");
		vs.setUrl("http://vs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addFilter()
			.setProperty("concept")
			.setOp(ValueSet.FilterOperator.ISA)
			.setValue("parent");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs-np")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code0")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")));
		myValueSetDao.update(vs);

		IValidationSupport.CodeValidationResult outcome;
		ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();

		// In memory - Hierarchy in existing CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "child10", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "childX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://cs#childX' for in-memory expansion of ValueSet 'http://vs'", outcome.getMessage());

		// In memory - Enumerated in non-present CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "code1", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://vs", outcome.getMessage());

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "codeX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://cs-np#codeX' for in-memory expansion of ValueSet 'http://vs'", outcome.getMessage());

		// Precalculated

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		logAllValueSets();
		myCachingValidationSupport.invalidateCaches();

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "child10", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertThat(outcome.getMessage(), startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "childX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertThat(outcome.getMessage(), containsString("Unknown code http://cs#childX"));
		assertThat(outcome.getMessage(), containsString("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		// Precalculated - Enumerated in non-present CS

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "code1", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertThat(outcome.getMessage(), startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

		outcome = myValidationSupport.validateCode(ctx, options, "http://cs-np", "codeX", null, "http://vs");
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertThat(outcome.getMessage(), containsString("Unknown code http://cs-np#codeX"));
		assertThat(outcome.getMessage(), containsString("Code validation occurred using a ValueSet expansion that was pre-calculated at "));

	}

	@Test
	public void testValidateCodeOperationNoValueSet() {
		UriType valueSetIdentifier = null;
		IdType id = null;
		CodeType code = new CodeType("8450-9-XXX");
		UriType system = new UriType("http://acme.org");
		StringType display = null;
		Coding coding = null;
		CodeableConcept codeableConcept = null;
		try {
			myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(901) + "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.", e.getMessage());
		}
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
		assertEquals("Concept Display \"Systolic blood pressure at First encounterXXXX\" does not match expected \"Systolic blood pressure at First encounter\" for in-memory expansion of ValueSet: http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", result.getMessage());
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTerminologyDeferredStorageSvc.saveDeferred();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
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
	public void testValidateCodeOperationByResourceIdAndCodeAndSystemWithExistingValueSetAndPreExpansionEnabled() {
		myDaoConfig.setPreExpandValueSets(true);

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

		myTerminologyDeferredStorageSvc.saveDeferred();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		result = myValueSetDao.validateCode(valueSetIdentifier, id, code, system, display, coding, codeableConcept, mySrd);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
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
			assertThat(e.getMessage(), containsString(Msg.code(832) + "Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!"));
			assertThat(e.getMessage(), containsString("Performing in-memory expansion"));
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
		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(vsIdentifier, null, code, system, display, coding, codeableConcept, mySrd);

		ourLog.info(result.getMessage());
		assertTrue(result.isOk(), result.getMessage());
		assertEquals("Male", result.getDisplay());
	}


	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsFragmentWithoutCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("0.1.17");
		myCodeSystemDao.update(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs);


		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(null, new IdType("ValueSet/vaccinecode"), new CodeType("28571000087109"), new CodeType("http://snomed.info/sct"), null, null, null, mySrd);
		assertTrue(outcome.isOk());
		outcome = myTermSvc.validateCodeInValueSet(
			new ValidationSupportContext(myValidationSupport),
			new ConceptValidationOptions(),
			"http://snomed.info/sct",
			"28571000087109",
			"MODERNA COVID-19 mRNA-1273",
			vs
		);
		assertTrue(outcome.isOk());

		ValueSet expansion = myValueSetDao.expand(new IdType("ValueSet/vaccinecode"), new ValueSetExpansionOptions(), mySrd);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

	}


}


