package ca.uhn.fhir.jpa.dao.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.util.ValueSetTestUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR5ValueSetTest extends BaseJpaR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5ValueSetTest.class);

	private final ValueSetTestUtil myValueSetTestUtil = new ValueSetTestUtil(FhirVersionEnum.R5);

	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;

	private IIdType myExtensionalVsId;

	@AfterEach
	public void after() {
		myStorageSettings.setPreExpandValueSets(new JpaStorageSettings().isPreExpandValueSets());
		myStorageSettings.setMaximumExpansionSize(new JpaStorageSettings().getMaximumExpansionSize());
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
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
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
		assertEquals("Concept Display \"Systolic blood pressure at First encounterXXXX\" does not match expected \"Systolic blood pressure at First encounter\" for 'http://acme.org#11378-7' for in-memory expansion of ValueSet: http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", result.getMessage());
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
		myStorageSettings.setPreExpandValueSets(true);

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
		myStorageSettings.setPreExpandValueSets(true);

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
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp).contains("<ValueSet xmlns=\"http://hl7.org/fhir\">");
		assertThat(resp).contains("<expansion>");
		assertThat(resp).contains("<contains>");
		assertThat(resp).contains("<system value=\"http://acme.org\"/>");
		assertThat(resp).contains("<code value=\"8450-9\"/>");
		assertThat(resp).contains("<display value=\"Systolic blood pressure--expiration\"/>");
		assertThat(resp).contains("</contains>");
		assertThat(resp).contains("<contains>");
		assertThat(resp).contains("<system value=\"http://acme.org\"/>");
		assertThat(resp).contains("<code value=\"11378-7\"/>");
		assertThat(resp).contains("<display value=\"Systolic blood pressure at First encounter\"/>");
		assertThat(resp).contains("</contains>");
		assertThat(resp).contains("</expansion>");

		/*
		 * Filter with display name
		 */

		expanded = myValueSetDao.expand(myExtensionalVsId, new ValueSetExpansionOptions().setFilter("systolic"), mySrd);
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp).containsSubsequence(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>");
		//@formatter:on

	}


	/**
	 * See #4305
	 */
	@Test
	public void testDeleteExpungePreExpandedValueSet() {
		myStorageSettings.setExpungeEnabled(true);

		// Create valueset
		ValueSet vs = myValidationSupport.fetchResource(ValueSet.class, "http://hl7.org/fhir/ValueSet/address-use");
		assertNotNull(vs);
		IIdType id = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();

		// Update valueset
		vs.setName("Hello");
		assertEquals("2", myValueSetDao.update(vs, mySrd).getId().getVersionIdPart());
		runInTransaction(()->{
			Optional<ResourceTable> resource = myResourceTableDao.findById(id.getIdPartAsLong());
			assertTrue(resource.isPresent());
		});

		// Precalculate
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		logAllValueSets();

		// Delete
		myValueSetDao.delete(id, mySrd);

		// Verify it's deleted
		assertThatExceptionOfType(ResourceGoneException.class).isThrownBy(() -> myValueSetDao.read(id, mySrd));

		// Expunge
		myValueSetDao.expunge(id, new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), mySrd);

		// Verify expunged
		runInTransaction(()->{
			Optional<ResourceTable> resource = myResourceTableDao.findById(id.getIdPartAsLong());
			assertFalse(resource.isPresent());
		});
	}


	@Test
	public void testExpandByValueSet_ExceedsMaxSize() {
		// Add a bunch of codes
		CustomTerminologySet codesToAdd = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			codesToAdd.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://loinc.org", codesToAdd);
		myStorageSettings.setMaximumExpansionSize(50);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://example.com/fhir/ValueSet/observation-vitalsignresult");
		vs.getCompose().addInclude().setSystem("http://loinc.org");
		myValueSetDao.create(vs);

		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains(Msg.code(832) + "Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!");
		}
	}

	@Test
	public void testPrecalculatedValueSet() {
		// Add a bunch of codes
		CustomTerminologySet codesToAdd = new CustomTerminologySet();
		for (int i = 0; i < 10; i++) {
			codesToAdd.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://cs", codesToAdd);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs.getCompose().addInclude().setSystem("http://cs");
		myValueSetDao.create(vs);

		// Precalculate

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		logAllValueSets();
		myCachingValidationSupport.invalidateCaches();

		// Validate code

		ValidationSupportContext ctx = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options= new ConceptValidationOptions();
		IValidationSupport.CodeValidationResult outcome = myValidationSupport.validateCode(ctx, options, "http://cs", "CODE4", null, "http://vs");
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertThat(outcome.getMessage()).startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at ");

		// Expand valueset

		ValueSet outcomeVs = myValueSetDao.expand(vs, null);
		String expansionMessage = myValueSetTestUtil.extractExpansionMessage(outcomeVs);
		assertThat(expansionMessage).startsWith("ValueSet was expanded using an expansion that was pre-calculated");
	}

	@Autowired
	protected CachingValidationSupport myCachingValidationSupport;

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
		assertThat(result.isOk()).as(result.getMessage()).isTrue();
		assertEquals("Male", result.getDisplay());
	}


}


