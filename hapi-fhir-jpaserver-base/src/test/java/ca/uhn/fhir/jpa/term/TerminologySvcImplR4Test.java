package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.UriType;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TerminologySvcImplR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplR4Test.class);
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	private IIdType myConceptMapId;

	@Before
	public void before() {
		myDaoConfig.setAllowExternalReferences(true);
	}

	@After
	public void after() {
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}
	
	private void createAndPersistConceptMap() {
		ConceptMap conceptMap = createConceptMap();
		persistConceptMap(conceptMap);
	}

	private void persistConceptMap(ConceptMap theConceptMap) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				myConceptMapId = myConceptMapDao.create(theConceptMap, mySrd).getId().toUnqualifiedVersionless();
			}
		});
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
			assertEquals("ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.source", e.getMessage());
		}

	}


	@Test
	public void testCreateConceptMapWithVirtualSourceSystem() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroup().forEach(t->t.setSource(null));
		conceptMap.setSource(new CanonicalType("http://hl7.org/fhir/uv/livd/StructureDefinition/loinc-livd"));

		persistConceptMap(conceptMap);

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
			assertEquals("ConceptMap[url='http://example.com/my_concept_map'] contains at least one group without a value in ConceptMap.group.target", e.getMessage());
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
			assertEquals("ConceptMap has no value for ConceptMap.url", e.getMessage());
		}

	}

	@Test
	public void testDuplicateConceptMapUrls() {
		createAndPersistConceptMap();

		expectedException.expect(UnprocessableEntityException.class);
		expectedException.expectMessage("Can not create multiple ConceptMap resources with ConceptMap.url \"http://example.com/my_concept_map\", already have one with resource ID: ConceptMap/" + myConceptMapId.getIdPart());

		createAndPersistConceptMap();
	}

	@Test
	public void testStoreTermConceptMapAndChildren() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				Optional<TermConceptMap> optionalConceptMap = myTermConceptMapDao.findTermConceptMapByUrl(CM_URL);
				assertTrue(optionalConceptMap.isPresent());

				TermConceptMap conceptMap = optionalConceptMap.get();

				ourLog.info("ConceptMap:\n" + conceptMap.toString());

				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());
				assertEquals(3, conceptMap.getConceptMapGroups().size());

				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);

				ourLog.info("ConceptMap.group(0):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(0).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(0).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				element = group.getConceptMapGroupElements().get(1);

				ourLog.info("ConceptMap.group(0).element(1):\n" + element.toString());

				assertEquals("23456", element.getCode());
				assertEquals("Source Code 23456", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);
				ourLog.info("ConceptMap.group(0).element(1).target(0):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// We had deliberately added a duplicate, and here it is...
				target = element.getConceptMapGroupElementTargets().get(1);
				ourLog.info("ConceptMap.group(0).element(1).target(1):\n" + target.toString());
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(1);

				ourLog.info("ConceptMap.group(1):\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 3", group.getSourceVersion());
				assertEquals(CS_URL_3, group.getTarget());
				assertEquals("Version 4", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(1).element(0):\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 3", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(1).element(0).target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = element.getConceptMapGroupElementTargets().get(1);

				ourLog.info("ConceptMap.group(1).element(0).target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				group = conceptMap.getConceptMapGroups().get(2);

				ourLog.info("ConceptMap.group(2):\n" + group.toString());

				assertEquals(CS_URL_4, group.getSource());
				assertEquals("Version 5", group.getSourceVersion());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				element = group.getConceptMapGroupElements().get(0);

				ourLog.info("ConceptMap.group(2).element(0):\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				target = element.getConceptMapGroupElementTargets().get(0);

				ourLog.info("ConceptMap.group(2).element(0).target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.NARROWER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("ConceptMap.group.element.target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				TermConceptMapGroupElement element = target.getConceptMapGroupElement();

				ourLog.info("ConceptMap.group.element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				TermConceptMapGroup group = element.getConceptMapGroup();

				ourLog.info("ConceptMap.group:\n" + group.toString());

				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());

				TermConceptMap conceptMap = group.getConceptMap();

				ourLog.info("ConceptMap:\n" + conceptMap.toString());

				assertNotNull(conceptMap);
				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertTrue(targets.isEmpty());
			}
		});
	}

	@Test
	public void testTranslateConceptMapWithNoSourceAndTargetValueSet() {

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);
		conceptMap.setSource(null);
		conceptMap.setTarget(null);
		ConceptMap.ConceptMapGroupComponent group = conceptMap.addGroup()
			.setSource(CS_URL)
			.setTarget(CS_URL_2);
		group.addElement()
			.setCode("12345")
			.addTarget()
			.setCode("34567");
		group.addElement()
			.setCode("888")
			.addTarget()
			.setCode("999");

		myConceptMapDao.create(conceptMap);

		TranslationRequest translationRequest = new TranslationRequest()
			.addCode(CS_URL, "12345")
			.setTargetSystem(new UriType(CS_URL_2));

		List<TermConceptMapGroupElementTarget> resp = myTermSvc.translate(translationRequest);
		assertEquals(1, resp.size());
		assertEquals("34567", resp.get(0).getCode());
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #2
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #3
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version #1
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345")
					.setVersion("Version 1");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target:\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(1, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version #3
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345")
					.setVersion("Version 3");

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(2, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setSource(new UriType(VS_URL));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   target value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setTarget(new UriType(VS_URL_2));

				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());

				TermConceptMapGroupElementTarget target = targets.get(0);

				ourLog.info("target(0):\n" + target.toString());

				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);

				ourLog.info("target(1):\n" + target.toString());

				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(2);

				ourLog.info("target(2):\n" + target.toString());

				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// Test caching.
				targets = myTermSvc.translate(translationRequest);
				assertNotNull(targets);
				assertEquals(3, targets.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverse() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL_4));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_3)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL));

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertTrue(elements.isEmpty());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #1
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   target code system #4
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setTargetSystem(new UriType(CS_URL_4));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(1, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystemAndVersion() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 *   source code system version
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_2)
					.setCode("34567")
					.setVersion("Version 2");
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source value set
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setSource(new UriType(VS_URL_2));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   target value set
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setTarget(new UriType(VS_URL));
				translationRequest.setReverse(true);

				List<TermConceptMapGroupElement> elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertFalse(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());

				TermConceptMapGroupElement element = elements.get(0);

				ourLog.info("element:\n" + element.toString());

				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				element = elements.get(1);

				ourLog.info("element:\n" + element.toString());

				assertEquals("78901", element.getCode());
				assertEquals("Source Code 78901", element.getDisplay());
				assertEquals(CS_URL_4, element.getSystem());
				assertEquals("Version 5", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				// Test caching.
				elements = myTermSvc.translateWithReverse(translationRequest);
				assertNotNull(elements);
				assertEquals(2, elements.size());
				assertTrue(BaseHapiTerminologySvcImpl.isOurLastResultsFromTranslationWithReverseCache());
			}
		});
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
