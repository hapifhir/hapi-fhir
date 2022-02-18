package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TerminologySvcImplR4Test extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplR4Test.class);

	@Autowired private BatchJobHelper myBatchJobHelper;

	ConceptValidationOptions optsNoGuess = new ConceptValidationOptions();
	ConceptValidationOptions optsGuess = new ConceptValidationOptions().setInferSystem(true);

	@Test
	public void testCreateConceptMapWithMissingSourceSystem() {
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setTarget(CS_URL_2);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(838) + "ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.source", e.getMessage());
		}

	}


	@Test
	public void testCreateConceptMapWithMissingTargetSystems() {

		// Missing source
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setSource(CS_URL);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(839) + "ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.target", e.getMessage());
		}

	}

	@Test
	public void testCreateConceptMapWithMissingUrl() {

		// Missing source
		ConceptMap conceptMap = new ConceptMap();
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setTarget(CS_URL_2)
			.setSource(CS_URL);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");

		try {
			runInTransaction(() -> {
				myConceptMapDao.create(conceptMap);
			});
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1766) + "ConceptMap has no value for ConceptMap.url", e.getMessage());
		}

	}

	@Test
	public void testDeleteValueSet() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		TermValueSet termValueSet = runInTransaction(()-> {
			TermValueSet vs = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).get();
			Long termValueSetId = vs.getId();
			assertEquals(3, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
			assertEquals(3, vs.getTotalConceptDesignations().intValue());
			assertEquals(24, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());
			assertEquals(24, vs.getTotalConcepts().intValue());
			return vs;
		});
		Long termValueSetId = termValueSet.getId();

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myTermValueSetConceptDesignationDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetConceptDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetDao.deleteById(termValueSetId);
				assertFalse(myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).isPresent());
			}
		});
	}

	@Test
	public void testDeleteValueSetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		Long termValueSetId = runInTransaction(()-> {
			TermValueSet termValueSet = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).get();
			Long id = termValueSet.getId();
			assertEquals(3, myTermValueSetConceptDesignationDao.countByTermValueSetId(id).intValue());
			assertEquals(3, termValueSet.getTotalConceptDesignations().intValue());
			assertEquals(24, myTermValueSetConceptDao.countByTermValueSetId(id).intValue());
			assertEquals(24, termValueSet.getTotalConcepts().intValue());
			return id;
		});

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myTermValueSetConceptDesignationDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDesignationDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetConceptDao.deleteByTermValueSetId(termValueSetId);
				assertEquals(0, myTermValueSetConceptDao.countByTermValueSetId(termValueSetId).intValue());
				myTermValueSetDao.deleteById(termValueSetId);
				assertFalse(myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).isPresent());
			}
		});
	}

	@Test
	public void testDuplicateCodeSystemUrls() throws Exception {
		loadAndPersistCodeSystem();

		try {
			loadAndPersistCodeSystem();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://acme.org\", already have one with resource ID: CodeSystem/" + myExtensionalCsId.getIdPart(), e.getMessage());
		}
	}


	@Test
	public void testDuplicateValueSetUrls() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		// DM 2019-03-05 - We pre-load our custom CodeSystem otherwise pre-expansion of the ValueSet will fail.
		loadAndPersistCodeSystemAndValueSet();

		try {
			loadAndPersistValueSet(HttpVerb.POST);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(902) + "Can not create multiple ValueSet resources with ValueSet.url \"http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2\", already have one with resource ID: ValueSet/" + myExtensionalVsId.getIdPart(), e.getMessage());
		}

	}


	@Test
	public void testValidateCode() {
		createCodeSystem();

		IValidationSupport.CodeValidationResult validation = myTermSvc.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "ParentWithNoChildrenA", null, null);
		assertTrue(validation.isOk());

		validation = myTermSvc.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "ZZZZZZZ", null, null);
		assertFalse(validation.isOk());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSet() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "BOGUS", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "11378-7", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, coding, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, codeableConcept);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, null);
		assertNull(result);


		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "BOGUS", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, "11378-7", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, coding, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(optsNoGuess, valueSet, null, null, null, null, codeableConcept);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

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

	@Test
	public void testUpdateCodeSystemUrlAndVersion() {
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
		codeSystem.setUrl(CS_URL_2);

		IIdType id_v2 = myCodeSystemDao.update(codeSystem, mySrd).getId().toUnqualified();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
			assertEquals(1, termCodeSystemVersions_updated.size());
			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(0);
			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 2);
			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
			assertThat(toCodes(termConcepts_updated), containsInAnyOrder("A", "B"));

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id_v2.getIdPartAsLong());
			assertEquals("2", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
			assertEquals(CS_URL_2, termCodeSystem.getCodeSystemUri());
		});
	}

	@Test
	public void testUpdateCodeSystemContentModeNotPresent() {
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

		// Remove concepts and changed ContentMode to NOTPRESENT
		codeSystem.setConcept(new ArrayList<>());
		codeSystem.setContent((CodeSystem.CodeSystemContentMode.NOTPRESENT));

		myCodeSystemDao.update(codeSystem, mySrd).getId().toUnqualified();
		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions_updated.size(), 1);
			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(0);
			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 2);
			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
			assertThat(toCodes(termConcepts_updated), containsInAnyOrder("A", "B"));

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(id.getIdPartAsLong());
			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
		});
	}

	@Test
	public void testFindCodeInvalidCodeSystem() {
		runInTransaction(() -> {
			Optional<TermConcept> termConcept = myTermSvc.findCode("http://InvalidSystem", "mycode");
			assertFalse(termConcept.isPresent());
		});
	}
}
