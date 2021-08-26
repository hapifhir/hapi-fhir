package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests load and validate CodeSystem and ValueSet so test names as uploadFirstCurrent... mean uploadCodeSystemAndValueSetCurrent...
 */
public class TerminologySvcImplCurrentVersionR4Test extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplCurrentVersionR4Test.class);

	@Test
	public void uploadFirstCurrentNoVersion() {

	}


	@Test
	public void uploadFirstCurrentWithVersion() {

	}


	@Test
	public void uploadFirstCurrentNoVersionThenNoCurrent() {

	}


	@Test
	public void uploadFirstCurrentWithVersionThenNoCurrent() {

	}


	@Test
	public void uploadFirstCurrentNoVersionThenNoCurrentThenCurrent() {

	}


	@Test
	public void uploadFirstCurrentWithVersionThenNoCurrentThenCurrent() {

	}


//	@Test
//	public void testValidateCode() {
//		createCodeSystem();
//
//		IValidationSupport.CodeValidationResult validation = myTermSvc.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "ParentWithNoChildrenA", null, null);
//		assertTrue(validation.isOk());
//
//		validation = myTermSvc.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "ZZZZZZZ", null, null);
//		assertFalse(validation.isOk());
//	}

//	@Test
//	public void testValidateCodeIsInPreExpandedValueSet() throws Exception {
//		myDaoConfig.setPreExpandValueSets(true);
//
//		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);
//
//		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
//		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
//
//		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
//		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));
//
//		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
//
//		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, null);
//		assertNull(result);
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "BOGUS", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "11378-7", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, coding, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		CodeableConcept codeableConcept = new CodeableConcept();
//		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
//		codeableConcept.addCoding(coding);
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, codeableConcept);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//	}

//	@Test
//	public void testValidateCodeIsInPreExpandedValueSetWithClientAssignedId() throws Exception {
//		myDaoConfig.setPreExpandValueSets(true);
//
//		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);
//
//		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
//		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
//
//		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
//		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));
//
//		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
//
//		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, null);
//		assertNull(result);
//
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "BOGUS", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "11378-7", null, null, null);
//		assertFalse(result.isOk());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, coding, null);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//
//		CodeableConcept codeableConcept = new CodeableConcept();
//		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
//		codeableConcept.addCoding(coding);
//		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, codeableConcept);
//		assertTrue(result.isOk());
//		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
//	}

	@Test
	public void testCreateCodeSystemTwoVersions() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A");
		codeSystem
			.addConcept().setCode("B").setDisplay("Code A");

		codeSystem.setVersion("1");

		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		Set<TermConcept> codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "A");
		assertThat(toCodes(codes), containsInAnyOrder("A"));

		codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "B");
		assertThat(toCodes(codes), containsInAnyOrder("B"));

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions.size(), 1);
			TermCodeSystemVersion termCodeSystemVersion_1 = termCodeSystemVersions.get(0);
			assertEquals(termCodeSystemVersion_1.getConcepts().size(), 2);
			Set<TermConcept> termConcepts = new HashSet<>(termCodeSystemVersion_1.getConcepts());
			assertThat(toCodes(termConcepts), containsInAnyOrder("A", "B"));

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id.getIdPartAsLong());
			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());

		});

		codeSystem.setVersion("2");
		codeSystem
			.addConcept().setCode("C").setDisplay("Code C");

		IIdType id_v2 = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		codes = myTermSvc.findCodesBelow(id_v2.getIdPartAsLong(), id_v2.getVersionIdPartAsLong(), "C");
		assertThat(toCodes(codes), containsInAnyOrder("C"));

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions_updated.size(), 2);
			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(1);
			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 3);
			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
			assertThat(toCodes(termConcepts_updated), containsInAnyOrder("A", "B", "C"));

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id_v2.getIdPartAsLong());
			assertEquals("2", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
		});
	}






}
