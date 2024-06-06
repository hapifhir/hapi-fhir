package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TermConceptMappingSvcImplTest extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptMappingSvcImplTest.class);

	private IIdType myConceptMapId;

	@Test
	public void testCreateConceptMapWithVirtualSourceSystem() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroup().forEach(t -> t.setSource(null));
		conceptMap.setSource(new CanonicalType("http://hl7.org/fhir/uv/livd/StructureDefinition/loinc-livd"));

		persistConceptMap(conceptMap, HttpVerb.POST);

	}

	@Test
	public void testCreateConceptMapWithVirtualSourceSystemWithClientAssignedId() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.getGroup().forEach(t -> t.setSource(null));
		conceptMap.setSource(new CanonicalType("http://hl7.org/fhir/uv/livd/StructureDefinition/loinc-livd"));
		conceptMap.setId("ConceptMap/cm");

		persistConceptMap(conceptMap, HttpVerb.PUT);

	}

	@Test
	public void testByCodeSystemsAndSourceCodeOneToMany() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL)
				.setCode("12345");
			translationRequest.setTargetSystem(CS_URL_3);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testByCodeSystemsAndSourceCodeOneToOne() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL)
				.setCode("12345");
			translationRequest.setTargetSystem(CS_URL_2);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("ConceptMap.group.element.target:\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL)
				.setCode("BOGUS");
			translationRequest.setTargetSystem(CS_URL_3);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertTrue(targets.isEmpty());
		});
	}

	@Test
	public void testByCodeSystemsAndSourceCodeMatchedWithoutCode() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL_4)
				.setCode("89012");
			translationRequest.setTargetSystem(CS_URL_3);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertTrue(targets.isEmpty());
		});
	}

	@Test
	public void testConceptMapWithNoSourceAndTargetValueSet() {

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

		myConceptMapDao.create(conceptMap, mySrd);

		TranslationRequest translationRequest = new TranslationRequest()
			.addCode(CS_URL, "12345")
			.setTargetSystem(CS_URL_2);

		TranslateConceptResults resp = myConceptMappingSvc.translate(translationRequest);
		assertEquals(1, resp.size());
		assertEquals("34567", resp.getResults().get(0).getCode());
	}

	@Test
	public void testUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("12345");

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(2);

			ourLog.info("target(2):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceAndTargetSystem2() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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
			translationRequest.setTargetSystem(CS_URL_2);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target:\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceAndTargetSystem3() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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
			translationRequest.setTargetSystem(CS_URL_3);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   source code system
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL)
				.setCode("12345");

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(2);

			ourLog.info("target(2):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceSystemAndVersion1() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target:\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(1, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceSystemAndVersion3() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(2, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   source value set
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("12345");
			translationRequest.setSource(VS_URL);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(2);

			ourLog.info("target(2):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   target value set
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("12345");
			translationRequest.setTarget(VS_URL_2);

			List<TranslateConceptResult> targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());

			TranslateConceptResult target = targets.get(0);

			ourLog.info("target(0):\n" + target.toString());

			assertEquals("34567", target.getCode());
			assertEquals("Target Code 34567", target.getDisplay());
			assertEquals(CS_URL_2, target.getSystem());
			assertEquals("Version 2", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(1);

			ourLog.info("target(1):\n" + target.toString());

			assertEquals("56789", target.getCode());
			assertEquals("Target Code 56789", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			target = targets.get(2);

			ourLog.info("target(2):\n" + target.toString());

			assertEquals("67890", target.getCode());
			assertEquals("Target Code 67890", target.getDisplay());
			assertEquals(CS_URL_3, target.getSystem());
			assertEquals("Version 4", target.getSystemVersion());
			assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), target.getEquivalence());
			assertEquals(VS_URL_2, target.getValueSet());
			assertEquals(CM_URL, target.getConceptMapUrl());

			// Test caching.
			targets = myConceptMappingSvc.translate(translationRequest).getResults();
			assertNotNull(targets);
			assertEquals(3, targets.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationCache());
		});
	}

	@Test
	public void testWithReverse() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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
			translationRequest.setTargetSystem(CS_URL_4);
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem(CS_URL_3)
				.setCode("BOGUS");
			translationRequest.setTargetSystem(CS_URL);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertTrue(elements.isEmpty());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithCodeOnly() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   reverse = true
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("34567");
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			element = elements.getResults().get(1);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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
			translationRequest.setTargetSystem(CS_URL);
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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
			translationRequest.setTargetSystem(CS_URL_4);
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(1, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithSourceSystem() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			element = elements.getResults().get(1);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithSourceSystemAndVersion() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
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

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			element = elements.getResults().get(1);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithSourceValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   source value set
			 *   reverse = true
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("34567");
			translationRequest.setSource(VS_URL_2);
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			element = elements.getResults().get(1);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testWithReverseUsingPredicatesWithTargetValueSet() {
		createAndPersistConceptMap();
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		runInTransaction(() -> {
			/*
			 * Provided:
			 *   source code
			 *   target value set
			 *   reverse = true
			 */
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setCode("34567");
			translationRequest.setTarget(VS_URL);
			translationRequest.setReverse(true);

			TranslateConceptResults elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertFalse(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());

			TranslateConceptResult element = elements.getResults().get(0);

			ourLog.info("element:\n" + element.toString());

			assertEquals("12345", element.getCode());
			assertEquals("Source Code 12345", element.getDisplay());
			assertEquals(CS_URL, element.getSystem());
			assertEquals("Version 1", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			element = elements.getResults().get(1);

			ourLog.info("element:\n" + element.toString());

			assertEquals("78901", element.getCode());
			assertEquals("Source Code 78901", element.getDisplay());
			assertEquals(CS_URL_4, element.getSystem());
			assertEquals("Version 5", element.getSystemVersion());
			assertEquals(VS_URL, element.getValueSet());
			assertEquals(CM_URL, element.getConceptMapUrl());

			// Test caching.
			elements = myConceptMappingSvc.translateWithReverse(translationRequest);
			assertNotNull(elements);
			assertEquals(2, elements.size());
			assertTrue(TermConceptMappingSvcImpl.isOurLastResultsFromTranslationWithReverseCache());
		});
	}

	@Test
	public void testDuplicateConceptMapUrls() {
		createAndPersistConceptMap();

		try {
			createAndPersistConceptMap();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(840) + "Can not create multiple ConceptMap resources with ConceptMap.url \"http://example.com/my_concept_map\", already have one with resource ID: ConceptMap/" + myConceptMapId.getIdPart(), e.getMessage());
		}

	}

	@Test
	public void testDuplicateConceptMapUrlsAndVersions() {
		createAndPersistConceptMap("v1");

		try {
			createAndPersistConceptMap("v1");
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(841) + "Can not create multiple ConceptMap resources with ConceptMap.url \"http://example.com/my_concept_map\" and ConceptMap.version \"v1\", already have one with resource ID: ConceptMap/" + myConceptMapId.getIdPart(), e.getMessage());
		}

	}


	@Test
	public void testStoreTermConceptMapAndChildren() {
		createAndPersistConceptMap();
		ConceptMap originalConceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(originalConceptMap));

		runInTransaction(() -> {
			Pageable page = PageRequest.of(0, 1);
			List<TermConceptMap> optionalConceptMap = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
			assertEquals(1, optionalConceptMap.size());

			TermConceptMap conceptMap = optionalConceptMap.get(0);

			ourLog.info("ConceptMap:\n" + conceptMap.toString());

			assertEquals(VS_URL, conceptMap.getSource());
			assertEquals(VS_URL_2, conceptMap.getTarget());
			assertEquals(CM_URL, conceptMap.getUrl());
			assertEquals(4, conceptMap.getConceptMapGroups().size());

			TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);

			ourLog.info("ConceptMap.group(0):\n" + group.toString());

			assertGroupHasValues(
				CS_URL,"Version 1", CS_URL_2, "Version 2", group);
			assertEquals(VS_URL, group.getSourceValueSet());
			assertEquals(VS_URL_2, group.getTargetValueSet());
			assertEquals(2, group.getConceptMapGroupElements().size());

			TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(0).element(0):\n" + element.toString());

			assertElementHasValues(
				"12345", "Source Code 12345", CS_URL, "Version 1", element);
			assertEquals(1, element.getConceptMapGroupElementTargets().size());

			TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(0).element(0).target(0):\n" + target.toString());

			assertTargetHasValues(
				"34567", "Target Code 34567", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.EQUAL, target);

			element = group.getConceptMapGroupElements().get(1);

			ourLog.info("ConceptMap.group(0).element(1):\n" + element.toString());

			assertElementHasValues(
				"23456", "Source Code 23456", CS_URL, "Version 1", element);

			assertEquals(2, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);
			ourLog.info("ConceptMap.group(0).element(1).target(0):\n" + target.toString());
			assertTargetHasValues(
				"45678", "Target Code 45678", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.WIDER, target);

			// We had deliberately added a duplicate, and here it is...
			target = element.getConceptMapGroupElementTargets().get(1);
			ourLog.info("ConceptMap.group(0).element(1).target(1):\n" + target.toString());
			assertTargetHasValues(
				"45678", "Target Code 45678", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.WIDER, target);

			group = conceptMap.getConceptMapGroups().get(1);

			ourLog.info("ConceptMap.group(1):\n" + group.toString());

			assertGroupHasValues(
				CS_URL, "Version 3", CS_URL_3, "Version 4", group);
			assertEquals(1, group.getConceptMapGroupElements().size());

			element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(1).element(0):\n" + element.toString());

			assertElementHasValues(
				"12345", "Source Code 12345", CS_URL, "Version 3", element);
			assertEquals(2, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(1).element(0).target(0):\n" + target.toString());

			assertTargetHasValues(
				"56789", "Target Code 56789", CS_URL_3, "Version 4", Enumerations.ConceptMapEquivalence.EQUAL, target);

			target = element.getConceptMapGroupElementTargets().get(1);

			ourLog.info("ConceptMap.group(1).element(0).target(1):\n" + target.toString());

			assertTargetHasValues(
				"67890", "Target Code 67890", CS_URL_3, "Version 4", Enumerations.ConceptMapEquivalence.WIDER, target);

			group = conceptMap.getConceptMapGroups().get(2);

			ourLog.info("ConceptMap.group(2):\n" + group.toString());

			assertGroupHasValues(
				CS_URL_4, "Version 5", CS_URL_2, "Version 2", group);
			assertEquals(1, group.getConceptMapGroupElements().size());

			element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(2).element(0):\n" + element.toString());

			assertElementHasValues(
				"78901", "Source Code 78901", CS_URL_4, "Version 5", element);
			assertEquals(1, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(2).element(0).target(0):\n" + target.toString());
		});
	}

	@Test
	public void testStoreTermConceptMapAndChildren_handleUnmappedElements() {
		createAndPersistConceptMap();
		ConceptMap originalConceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(originalConceptMap));

		runInTransaction(() -> {
			Pageable page = PageRequest.of(0, 1);
			List<TermConceptMap> optionalConceptMap = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
			assertEquals(1, optionalConceptMap.size());

			TermConceptMap conceptMap = optionalConceptMap.get(0);
			TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(3);

			ourLog.info("ConceptMap.group(3):\n" + group.toString());

			assertGroupHasValues(
				CS_URL_4, "Version 1", CS_URL_3, "Version 1", group);
			assertEquals(2, group.getConceptMapGroupElements().size());

			TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(3).element(0):\n" + element.toString());

			assertElementHasValues(
				"89012", "Source Code 89012", CS_URL_4, "Version 1", element);
			assertEquals(0, element.getConceptMapGroupElementTargets().size());

			element = group.getConceptMapGroupElements().get(1);

			ourLog.info("ConceptMap.group(3).element(1):\n" + element.toString());

			assertElementHasValues(
				"89123", "Source Code 89123", CS_URL_4, "Version 1", element);
			assertEquals(1, element.getConceptMapGroupElementTargets().size());

			TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(3).element(1).target(0):\n" + target.toString());

			assertTargetHasValues(
				null, null, CS_URL_3, "Version 1", Enumerations.ConceptMapEquivalence.UNMATCHED, target);
		});
	}

	private void assertGroupHasValues(String theExpectedSourceCodeSystem,
									  String theExpectedSourceCodeSystemVersion,
									  String theExpectedTargetCodeSystem,
									  String theExpectedTargetCodeSystemVersion,
									  TermConceptMapGroup group) {
		assertEquals(theExpectedSourceCodeSystem, group.getSource());
		assertEquals(theExpectedSourceCodeSystemVersion, group.getSourceVersion());
		assertEquals(theExpectedTargetCodeSystem, group.getTarget());
		assertEquals(theExpectedTargetCodeSystemVersion, group.getTargetVersion());
		assertEquals(CM_URL, group.getConceptMapUrl());
	}

	private void assertElementHasValues(String theExpectedCode,
										String theExpectedDisplayText,
										String theExpectedCodeSystem,
										String theExpectedCodeSystemVersion,
										TermConceptMapGroupElement element) {
		assertEquals(theExpectedCode, element.getCode());
		assertEquals(theExpectedDisplayText, element.getDisplay());
		assertEquals(theExpectedCodeSystem, element.getSystem());
		assertEquals(theExpectedCodeSystemVersion, element.getSystemVersion());
		assertEquals(VS_URL, element.getValueSet());
		assertEquals(CM_URL, element.getConceptMapUrl());
	}

	private void assertTargetHasValues(String theExpectedCode,
									   String theExpectedDisplayText,
									   String theExpectedCodeSystem,
									   String theExpectedCodeSystemVersion,
									   Enumerations.ConceptMapEquivalence theExpectedEquivalence,
									   TermConceptMapGroupElementTarget target) {
		assertEquals(theExpectedCode, target.getCode());
		assertEquals(theExpectedDisplayText, target.getDisplay());
		assertEquals(theExpectedCodeSystem, target.getSystem());
		assertEquals(theExpectedCodeSystemVersion, target.getSystemVersion());
		assertEquals(theExpectedEquivalence, target.getEquivalence());
		assertEquals(VS_URL_2, target.getValueSet());
		assertEquals(CM_URL, target.getConceptMapUrl());
	}

	@Test
	public void testStoreTermConceptMapAndChildrenWithClientAssignedId() {
		createAndPersistConceptMap();
		ConceptMap originalConceptMap = myConceptMapDao.read(myConceptMapId, mySrd);

		ourLog.debug("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(originalConceptMap));

		runInTransaction(() -> {
			Pageable page = PageRequest.of(0, 1);
			List<TermConceptMap> optionalConceptMap = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
			assertEquals(1, optionalConceptMap.size());

			TermConceptMap conceptMap = optionalConceptMap.get(0);

			ourLog.info("ConceptMap:\n" + conceptMap.toString());

			assertEquals(VS_URL, conceptMap.getSource());
			assertEquals(VS_URL_2, conceptMap.getTarget());
			assertEquals(CM_URL, conceptMap.getUrl());
			assertEquals(4, conceptMap.getConceptMapGroups().size());

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

			assertElementHasValues("12345", "Source Code 12345", CS_URL, "Version 1", element);
			assertEquals(1, element.getConceptMapGroupElementTargets().size());

			TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(0).element(0).target(0):\n" + target.toString());

			assertTargetHasValues("34567", "Target Code 34567", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.EQUAL, target);

			element = group.getConceptMapGroupElements().get(1);

			ourLog.info("ConceptMap.group(0).element(1):\n" + element.toString());

			assertElementHasValues("23456", "Source Code 23456", CS_URL, "Version 1", element);

			assertEquals(2, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);
			ourLog.info("ConceptMap.group(0).element(1).target(0):\n" + target.toString());
			assertTargetHasValues("45678", "Target Code 45678", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.WIDER, target);

			// We had deliberately added a duplicate, and here it is...
			target = element.getConceptMapGroupElementTargets().get(1);
			ourLog.info("ConceptMap.group(0).element(1).target(1):\n" + target.toString());
			assertTargetHasValues("45678", "Target Code 45678", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.WIDER, target);

			group = conceptMap.getConceptMapGroups().get(1);

			ourLog.info("ConceptMap.group(1):\n" + group.toString());

			assertGroupHasValues(CS_URL, "Version 3", CS_URL_3, "Version 4", group);
			assertEquals(1, group.getConceptMapGroupElements().size());

			element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(1).element(0):\n" + element.toString());

			assertElementHasValues("12345", "Source Code 12345", CS_URL, "Version 3", element);
			assertEquals(2, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(1).element(0).target(0):\n" + target.toString());

			assertTargetHasValues("56789", "Target Code 56789", CS_URL_3, "Version 4", Enumerations.ConceptMapEquivalence.EQUAL, target);

			target = element.getConceptMapGroupElementTargets().get(1);

			ourLog.info("ConceptMap.group(1).element(0).target(1):\n" + target.toString());

			assertTargetHasValues("67890", "Target Code 67890", CS_URL_3, "Version 4", Enumerations.ConceptMapEquivalence.WIDER, target);

			group = conceptMap.getConceptMapGroups().get(2);

			ourLog.info("ConceptMap.group(2):\n" + group.toString());

			assertGroupHasValues(CS_URL_4, "Version 5", CS_URL_2, "Version 2", group);
			assertEquals(1, group.getConceptMapGroupElements().size());

			element = group.getConceptMapGroupElements().get(0);

			ourLog.info("ConceptMap.group(2).element(0):\n" + element.toString());

			assertElementHasValues("78901", "Source Code 78901", CS_URL_4, "Version 5", element);
			assertEquals(1, element.getConceptMapGroupElementTargets().size());

			target = element.getConceptMapGroupElementTargets().get(0);

			ourLog.info("ConceptMap.group(2).element(0).target(0):\n" + target.toString());

			assertTargetHasValues("34567", "Target Code 34567", CS_URL_2, "Version 2", Enumerations.ConceptMapEquivalence.NARROWER, target);
		});
	}

	@Test
	public void testTranslateCodeRequestToTranslationRequestMapping() {
		CodeableConcept codeableConcept = new CodeableConcept();
		Coding coding = new Coding("theSourceSystemUrl", "theSourceCode", null);
		codeableConcept.addCoding(coding);

		IValidationSupport.TranslateCodeRequest theRequest = new IValidationSupport.TranslateCodeRequest(
			Collections.unmodifiableList(codeableConcept.getCoding()),
			"theTargetSystemUrl",
			"theConceptMapUrl",
			"theConceptMapVersion",
			"theSourceValueSetUrl",
			"theTargetValueSetUrl",
			new IdType("ConceptMap/2"),
			false
		);

		CodeableConcept sourceCodeableConcept = new CodeableConcept();
		sourceCodeableConcept
			.addCoding()
			.setSystem(coding.getSystem())
			.setCode(coding.getCode());

		TranslationRequest expected = new TranslationRequest();
		expected.setCodeableConcept(sourceCodeableConcept);
		expected.setConceptMapVersion((theRequest.getConceptMapVersion()));
		expected.setUrl((theRequest.getConceptMapUrl()));
		expected.setSource((theRequest.getSourceValueSetUrl()));
		expected.setTarget((theRequest.getTargetValueSetUrl()));
		expected.setTargetSystem((theRequest.getTargetSystemUrl()));
		expected.setResourceId(theRequest.getResourceId());
		expected.setReverse(theRequest.isReverse());

		ITermConceptMappingSvc mock = mock(TermConceptMappingSvcImpl.class);
		ArgumentCaptor<TranslationRequest> argument = ArgumentCaptor.forClass(TranslationRequest.class);
		when(mock.translate(expected)).thenReturn(new TranslateConceptResults());
		when(mock.translateConcept(theRequest)).thenCallRealMethod();
		mock.translateConcept(theRequest);
		verify(mock).translate(argument.capture());
		assertSameTranslationRequest(expected, argument.getValue());
	}

	@Test
	public void testTranslateCodeRequestWithReverseToTranslationRequestMapping() {
		CodeableConcept codeableConcept = new CodeableConcept();
		Coding coding = new Coding("theSourceSystemUrl", "theSourceCode", null);
		codeableConcept.addCoding(coding);

		IValidationSupport.TranslateCodeRequest theRequest = new IValidationSupport.TranslateCodeRequest(
			Collections.unmodifiableList(codeableConcept.getCoding()),
			"theTargetSystemUrl",
			"theConceptMapUrl",
			"theConceptMapVersion",
			"theSourceValueSetUrl",
			"theTargetValueSetUrl",
			new IdType("ConceptMap/A"),
			true
		);

		CodeableConcept sourceCodeableConcept = new CodeableConcept();
		sourceCodeableConcept
			.addCoding()
			.setSystem(coding.getSystem())
			.setCode(coding.getCode());

		TranslationRequest expected = new TranslationRequest();
		expected.setCodeableConcept(sourceCodeableConcept);
		expected.setConceptMapVersion((theRequest.getConceptMapVersion()));
		expected.setUrl((theRequest.getConceptMapUrl()));
		expected.setSource((theRequest.getSourceValueSetUrl()));
		expected.setTarget((theRequest.getTargetValueSetUrl()));
		expected.setTargetSystem((theRequest.getTargetSystemUrl()));
		expected.setResourceId(theRequest.getResourceId());
		expected.setReverse(theRequest.isReverse());

		ITermConceptMappingSvc mock = mock(TermConceptMappingSvcImpl.class);
		ArgumentCaptor<TranslationRequest> argument = ArgumentCaptor.forClass(TranslationRequest.class);
		when(mock.translate(expected)).thenReturn(new TranslateConceptResults());
		when(mock.translateConcept(theRequest)).thenCallRealMethod();
		mock.translateConcept(theRequest);
		verify(mock).translateWithReverse(argument.capture());
		assertSameTranslationRequest(expected, argument.getValue());
	}

	private static void assertSameTranslationRequest(TranslationRequest expected, TranslationRequest actual) {
		assertTrue(expected.getCodeableConcept().equalsDeep(actual.getCodeableConcept()));
		assertEquals(expected.getConceptMapVersion(), actual.getConceptMapVersion());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals(expected.getSource(), actual.getSource());
		assertEquals(expected.getTarget(), actual.getTarget());
		assertEquals(expected.getTargetSystem(), actual.getTargetSystem());
		assertEquals(expected.getResourceId(), actual.getResourceId());
		assertEquals(expected.getReverseAsBoolean(), actual.getReverseAsBoolean());
	}

	private void createAndPersistConceptMap() {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.setId("ConceptMap/cm");
		persistConceptMap(conceptMap, HttpVerb.POST);
	}

	private void createAndPersistConceptMap(String version) {
		ConceptMap conceptMap = createConceptMap();
		conceptMap.setId("ConceptMap/cm");
		conceptMap.setVersion(version);
		persistConceptMap(conceptMap, HttpVerb.POST);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistConceptMap(ConceptMap theConceptMap, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				runInTransaction(() -> {
					myConceptMapId = myConceptMapDao.create(theConceptMap, mySrd).getId().toUnqualifiedVersionless();
				});
				break;
			case PUT:
				runInTransaction(() -> {
					myConceptMapId = myConceptMapDao.update(theConceptMap, mySrd).getId().toUnqualifiedVersionless();
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
	}

}
