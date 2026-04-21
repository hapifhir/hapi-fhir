package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class TerminologySvcImplR4Test extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplR4Test.class);

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	ConceptValidationOptions optsNoGuess = new ConceptValidationOptions();
	ConceptValidationOptions optsGuess = new ConceptValidationOptions().setInferSystem(true);
	@Autowired
	private Batch2JobHelper myBatchJobHelper;

	@Override
	@AfterEach
	public void after() {
		super.after();
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(new JpaStorageSettings().getDeferIndexingForCodesystemsOfSize());
		TermCodeSystemDeleteJobSvcWithUniTestFailures.setFailNextDeleteCodeSystemVersion(false);
	}

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
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

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
				myTermValueSetDao.deleteById(new IdAndPartitionId(termValueSetId));
				assertFalse(myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable).isPresent());
			}
		});
	}

	@Test
	public void testDeleteValueSetWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

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
				myTermValueSetDao.deleteById(new IdAndPartitionId(termValueSetId));
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
		myStorageSettings.setPreExpandValueSets(true);

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

		// The CodeSystem created by createCodeSystem() has content=not-present BUT it is backed by a
		// local TermCodeSystem row populated with concepts. In that case the local DB is authoritative
		// for this CodeSystem, so a miss must surface as a "code not found" error — not a fall-through.
		// See GH-7796 / TermReadSvcImpl#isCodeSystemNotPresentAndHasNoLocalContent.
		validation = myTermSvc.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "ZZZZZZZ", null, null);
		assertFalse(validation.isOk());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSet() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);

		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, null, null);
		assertNull(result);

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, "BOGUS", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, "11378-7", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsGuess, valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, coding, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, null, codeableConcept);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());
	}

	@Test
	public void testValidateCodeIsInPreExpandedValueSetWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);

		IValidationSupport.CodeValidationResult result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, null, null);
		assertNull(result);


		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, "BOGUS", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, "11378-7", null, null, null);
		assertFalse(result.isOk());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsGuess, valueSet, null, "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsGuess, valueSet, null, "11378-7", "Systolic blood pressure at First encounter", null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, "http://acme.org", "11378-7", null, null, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		Coding coding = new Coding("http://acme.org", "11378-7", "Systolic blood pressure at First encounter");
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, coding, null);
		assertTrue(result.isOk());
		assertEquals("Systolic blood pressure at First encounter", result.getDisplay());

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding("BOGUS", "BOGUS", "BOGUS"));
		codeableConcept.addCoding(coding);
		result = myTermSvc.validateCodeIsInPreExpandedValueSet(valCtx, optsNoGuess, valueSet, null, null, null, null, codeableConcept);
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
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("A");

		codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "B");
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("B");

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions.size(), 1);
			TermCodeSystemVersion termCodeSystemVersion_1 = termCodeSystemVersions.get(0);
			assertEquals(termCodeSystemVersion_1.getConcepts().size(), 2);
			Set<TermConcept> termConcepts = new HashSet<>(termCodeSystemVersion_1.getConcepts());
			assertThat(toCodes(termConcepts)).containsExactlyInAnyOrder("A", "B");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id.getIdPartAsLong()));
			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());

		});

		codeSystem.setVersion("2");
		codeSystem
			.addConcept().setCode("C").setDisplay("Code C");

		IIdType id_v2 = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		codes = myTermSvc.findCodesBelow(id_v2.getIdPartAsLong(), id_v2.getVersionIdPartAsLong(), "C");
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("C");

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions_updated.size(), 2);
			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(1);
			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 3);
			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
			assertThat(toCodes(termConcepts_updated)).containsExactlyInAnyOrder("A", "B", "C");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id_v2.getIdPartAsLong()));
			assertEquals("2", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
		});
	}

	@Test
	public void testUpdateCodeSystemUrlAndVersion() {
		// create code system
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
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("A");

		codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "B");
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("B");

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions.size(), 1);
			TermCodeSystemVersion termCodeSystemVersion_1 = termCodeSystemVersions.get(0);
			assertEquals(termCodeSystemVersion_1.getConcepts().size(), 2);
			Set<TermConcept> termConcepts = new HashSet<>(termCodeSystemVersion_1.getConcepts());
			assertThat(toCodes(termConcepts)).containsExactlyInAnyOrder("A", "B");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id.getIdPartAsLong()));
			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
		});

		codeSystem.setVersion("2");
		codeSystem.setUrl(CS_URL_2);

		IIdType id_v2 = myCodeSystemDao.update(codeSystem, mySrd).getId().toUnqualified();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions_updated = myTermCodeSystemVersionDao.findAll();
			assertEquals(1, termCodeSystemVersions_updated.size());
			TermCodeSystemVersion termCodeSystemVersion_2 = termCodeSystemVersions_updated.get(0);
			assertEquals(termCodeSystemVersion_2.getConcepts().size(), 2);
			Set<TermConcept> termConcepts_updated = new HashSet<>(termCodeSystemVersion_2.getConcepts());
			assertThat(toCodes(termConcepts_updated)).containsExactlyInAnyOrder("A", "B");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id_v2.getIdPartAsLong()));
			assertEquals("2", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
			assertEquals(CS_URL_2, termCodeSystem.getCodeSystemUri());
		});
	}

	/**
	 * See #4206
	 */
	@Test
	public void testUpdateLargeCodeSystemInRapidSuccession() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(100);
		TermCodeSystemDeleteJobSvcWithUniTestFailures.setFailNextDeleteCodeSystemVersion(true);

		CodeSystem codeSystem;
		codeSystem = createCodeSystemWithManyCodes(0, 1000);
		myCodeSystemDao.update(codeSystem, mySrd);
		codeSystem = createCodeSystemWithManyCodes(1, 1001);
		myCodeSystemDao.update(codeSystem, mySrd);
		codeSystem = createCodeSystemWithManyCodes(2, 1002);
		myCodeSystemDao.update(codeSystem, mySrd);

		await().until(() -> {
			myBatch2JobHelper.runMaintenancePass();
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
			return myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true);
			});

		IValidationSupport.CodeValidationResult outcome;

		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();

		outcome = myValidationSupport.validateCode(context, options, CS_URL, "A1002", null, null);
		assertTrue(outcome.isOk());

		outcome = myValidationSupport.validateCode(context, options, CS_URL, "A1003", null, null);
		assertFalse(outcome.isOk());

	}

	@Nonnull
	private static CodeSystem createCodeSystemWithManyCodes(int theCodeCountStart, int theCodeCountEnd) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("CodeSystem/A");
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.setVersion("1");
		for (int i = theCodeCountStart; i <= theCodeCountEnd; i++) {
			codeSystem
				.addConcept().setCode("A" + i).setDisplay("Code A" + i);
		}
		return codeSystem;
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
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("A");

		codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "B");
		assertThat(toCodes(codes)).containsExactlyInAnyOrder("B");

		runInTransaction(() -> {
			List<TermCodeSystemVersion> termCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
			assertEquals(termCodeSystemVersions.size(), 1);
			TermCodeSystemVersion termCodeSystemVersion_1 = termCodeSystemVersions.get(0);
			assertEquals(termCodeSystemVersion_1.getConcepts().size(), 2);
			Set<TermConcept> termConcepts = new HashSet<>(termCodeSystemVersion_1.getConcepts());
			assertThat(toCodes(termConcepts)).containsExactlyInAnyOrder("A", "B");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id.getIdPartAsLong()));
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
			assertThat(toCodes(termConcepts_updated)).containsExactlyInAnyOrder("A", "B");

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByResourcePid(JpaPid.fromId(id.getIdPartAsLong()));
			assertEquals("1", termCodeSystem.getCurrentVersion().getCodeSystemVersionId());
		});
	}

	@Test
	void deleteCodeSystem_lookupCode_returnsNotFound() {
		// Setup: create a CodeSystem with concepts
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("codeA").setDisplay("displayA");
		cs.addConcept().setCode("codeB").setDisplay("displayB");
		myCodeSystemDao.create(cs, mySrd);

		// Verify lookup succeeds (populates the cache)
		IValidationSupport.LookupCodeResult resultA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(resultA).isNotNull();
		assertThat(resultA.isFound()).isTrue();

		// Delete via the FHIR DAO (same as a user calling DELETE /CodeSystem/...)
		myCodeSystemDao.deleteByUrl("CodeSystem?url=http://foo/cs", mySrd);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete("termCodeSystemDeleteJob");

		// Verify lookup fails after deletion
		IValidationSupport.LookupCodeResult afterDeleteA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(afterDeleteA).isNotNull();
		assertThat(afterDeleteA.isFound()).isFalse();

		IValidationSupport.LookupCodeResult afterDeleteB = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(afterDeleteB).isNotNull();
		assertThat(afterDeleteB.isFound()).isFalse();
	}

	@Test
	void updateCodeSystem_lookupCode_reflectsNewConcepts() {
		// Setup: create a CodeSystem with content=complete and one concept
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("codeA").setDisplay("displayA");
		IIdType id = myCodeSystemDao.create(cs, mySrd).getId();

		// Verify initial lookup succeeds
		IValidationSupport.LookupCodeResult resultA = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(resultA).isNotNull();
		assertThat(resultA.isFound()).isTrue();
		assertThat(resultA.getCodeDisplay()).isEqualTo("displayA");

		// Update the CodeSystem with different concepts
		CodeSystem csUpdated = new CodeSystem();
		csUpdated.setId(id.toVersionless());
		csUpdated.setUrl("http://foo/cs");
		csUpdated.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		csUpdated.addConcept().setCode("codeB").setDisplay("displayB");
		csUpdated.addConcept().setCode("codeC").setDisplay("displayC");
		myCodeSystemDao.update(csUpdated, mySrd);

		// Verify new concepts are found
		IValidationSupport.LookupCodeResult resultB = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeB"));
		assertThat(resultB).isNotNull();
		assertThat(resultB.isFound()).isTrue();
		assertThat(resultB.getCodeDisplay()).isEqualTo("displayB");

		// Verify old concept from replaced version is no longer found
		IValidationSupport.LookupCodeResult resultAAfter = myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport), new LookupCodeRequest("http://foo/cs", "codeA"));
		assertThat(resultAAfter).isNotNull();
		assertThat(resultAAfter.isFound()).isFalse();
	}

	@Test
	public void testFindCodeInvalidCodeSystem() {
		runInTransaction(() -> {
			Optional<TermConcept> termConcept = myTermSvc.findCode("http://InvalidSystem", "mycode");
			assertFalse(termConcept.isPresent());
		});
	}

	@Test
	void updateValueSet_preExpanded_isValueSetPreExpandedForCodeValidation_returnsFalse() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		// Setup: code system + ValueSet, then pre-expand so the TermValueSet has EXPANDED status
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Prime: read and verify pre-expanded, which populates myValueSetCache with EXPANDED status
		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId, mySrd);
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(valueSet)).isTrue();

		// Update the ValueSet — JpaResourceDaoValueSet calls storeTermValueSet, which
		// deletes the old TermValueSet and creates a new one with NOT_EXPANDED status.
		// Without the fix, myValueSetCache still holds the stale EXPANDED entry.
		// A content change is required so the DAO considers the resource modified nd invokes storeTermValueSet.
		valueSet.setTitle("Updated");
		myValueSetDao.update(valueSet, mySrd);

		// The cache must be cleared: re-checking should reflect NOT_EXPANDED from the DB
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(valueSet)).isFalse();
	}

	@Test
	void deleteValueSet_preExpanded_isValueSetPreExpandedForCodeValidation_returnsFalse() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		// Setup: code system + ValueSet, then pre-expand so the TermValueSet has EXPANDED status
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Prime: read and verify pre-expanded, which populates myValueSetCache with EXPANDED status
		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId, mySrd);
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(valueSet)).isTrue();

		// Delete the ValueSet — JpaResourceDaoValueSet calls deleteValueSetAndChildren,
		// which must invalidate the cache.
		// Without the fix, myValueSetCache still holds the stale EXPANDED entry.
		myValueSetDao.delete(myExtensionalVsId, mySrd);

		// The cache must be cleared: the TermValueSet no longer exists in the DB
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(valueSet)).isFalse();
	}

	@Test
	void invalidateCaches_codeSystemAndValueSetLookupsStillReflectDatabaseState() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		// Setup: code system used by getCurrentCodeSystemVersion
		createCodeSystem();

		// Setup: pre-expanded ValueSet used by getValueSetEntity
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);
		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId, mySrd);

		// Prime both caches with valid lookups
		IValidationSupport.CodeValidationResult csResult = myTermSvc.validateCode(
			valCtx, new ConceptValidationOptions(), CS_URL, "ParentWithNoChildrenA", null, null);
		assertThat(csResult).isNotNull();
		assertThat(csResult.isOk()).isTrue();

		IValidationSupport.CodeValidationResult vsResult = myTermSvc.validateCodeIsInPreExpandedValueSet(
			valCtx, optsGuess, valueSet, null, "11378-7", null, null, null);
		assertThat(vsResult.isOk()).isTrue();

		// Directly invalidate both caches (not via a delta operation)
		myTermSvc.invalidateCaches();

		// Code system version cache must repopulate from DB — lookup still correct
		IValidationSupport.CodeValidationResult csResultAfter = myTermSvc.validateCode(
			valCtx, new ConceptValidationOptions(), CS_URL, "ParentWithNoChildrenA", null, null);
		assertThat(csResultAfter).isNotNull();
		assertThat(csResultAfter.isOk()).isTrue();

		// Value set cache must repopulate from DB — validation still correct
		IValidationSupport.CodeValidationResult vsResultAfter = myTermSvc.validateCodeIsInPreExpandedValueSet(
			valCtx, optsGuess, valueSet, null, "11378-7", null, null, null);
		assertThat(vsResultAfter.isOk()).isTrue();
	}

	@Test
	void lookupCode_versionedAndUnversionedCodeSystem_resolvesIndependently() {
		// Setup: two CodeSystem resources with same URL, different versions
		CodeSystem csV1 = new CodeSystem();
		csV1.setUrl("http://foo/cs");
		csV1.setVersion("v1");
		csV1.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		csV1.addConcept().setCode("code-v1").setDisplay("Display V1");
		myCodeSystemDao.create(csV1, mySrd);

		CodeSystem csV2 = new CodeSystem();
		csV2.setUrl("http://foo/cs");
		csV2.setVersion("v2");
		csV2.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		csV2.addConcept().setCode("code-v2").setDisplay("Display V2");
		myCodeSystemDao.create(csV2, mySrd);

		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);

		// Versioned lookup url|v1 resolves to v1 concepts
		IValidationSupport.LookupCodeResult v1Found = myTermSvc.lookupCode(
			valCtx, new LookupCodeRequest("http://foo/cs|v1", "code-v1"));
		assertThat(v1Found).isNotNull();
		assertThat(v1Found.isFound()).isTrue();
		assertThat(v1Found.getCodeDisplay()).isEqualTo("Display V1");

		// Versioned lookup url|v2 resolves to v2 concepts
		IValidationSupport.LookupCodeResult v2Found = myTermSvc.lookupCode(
			valCtx, new LookupCodeRequest("http://foo/cs|v2", "code-v2"));
		assertThat(v2Found).isNotNull();
		assertThat(v2Found.isFound()).isTrue();
		assertThat(v2Found.getCodeDisplay()).isEqualTo("Display V2");

		// Cross-version: v2 concept not found via v1 cache key
		IValidationSupport.LookupCodeResult v1ForV2Code = myTermSvc.lookupCode(
			valCtx, new LookupCodeRequest("http://foo/cs|v1", "code-v2"));
		assertThat(v1ForV2Code).isNotNull();
		assertThat(v1ForV2Code.isFound()).isFalse();

		// Unversioned lookup resolves to current version (v2, the most recently stored)
		IValidationSupport.LookupCodeResult unversioned = myTermSvc.lookupCode(
			valCtx, new LookupCodeRequest("http://foo/cs", "code-v2"));
		assertThat(unversioned).isNotNull();
		assertThat(unversioned.isFound()).isTrue();
	}

	@Test
	void isValueSetPreExpandedForCodeValidation_versionedValueSets_resolveIndependently() {
		myStorageSettings.setPreExpandValueSets(true);

		// Setup: code system referenced by both ValueSet versions
		createCodeSystem();

		// Create two ValueSet resources with same URL but different versions
		ValueSet vsV1 = new ValueSet();
		vsV1.setUrl("http://foo/vs");
		vsV1.setVersion("v1");
		vsV1.getCompose().addInclude().setSystem(CS_URL);
		IIdType vsV1Id = myValueSetDao.create(vsV1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vsV2 = new ValueSet();
		vsV2.setUrl("http://foo/vs");
		vsV2.setVersion("v2");
		vsV2.getCompose().addInclude().setSystem(CS_URL);
		IIdType vsV2Id = myValueSetDao.create(vsV2, mySrd).getId().toUnqualifiedVersionless();

		// Pre-expand both TermValueSet entities
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Read back so url and version fields are populated from stored resources
		ValueSet vsV1Read = myValueSetDao.read(vsV1Id, mySrd);
		ValueSet vsV2Read = myValueSetDao.read(vsV2Id, mySrd);

		// Both versioned ValueSets independently resolve to their own TermValueSet entity.
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(vsV1Read)).isTrue();
		assertThat(myTermSvc.isValueSetPreExpandedForCodeValidation(vsV2Read)).isTrue();
	}

}
