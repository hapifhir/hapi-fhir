package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.api.model.TranslationMatch;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.entity.TermConceptMap;

public class FhirResourceDaoR4ConceptMapTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4ConceptMapTest.class);

	private IIdType myConceptMapId;

	@BeforeEach
	@Transactional
	public void before02() {
		myConceptMapId = myConceptMapDao.create(createConceptMap(), mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testDeleteConceptMap() {
		myConceptMapDao.delete(myConceptMapId);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult().booleanValue());
			}
		});

	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to multiple target codes">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to one target code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Attempt to map unknown source code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult().booleanValue());
				assertEquals("No matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(0, translationResult.getMatches().size());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(3, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(3, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(3, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(3, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertEquals("Version 2", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverse() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseHavingEquivalence() {
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
					.setSystem(CS_URL_3)
					.setCode("67890");
				translationRequest.setTargetSystem(new UriType(CS_URL));
				translationRequest.setReverse(true);

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 3", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
				assertEquals("wider", translationMatch.getEquivalence().getCode());
			}
		});
	}

	@Test
	public void testTranslateWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Attempt to map unknown source code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_3)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL));
				translationRequest.setReverse(true);

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult().booleanValue());
				assertEquals("No matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(0, translationResult.getMatches().size());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeOnly() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystem() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystemAndVersion() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceValueSet() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithTargetValueSet() {
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

				TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());

				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals("equal", translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("12345", concept.getCode());
				assertEquals("Source Code 12345", concept.getDisplay());
				assertEquals(CS_URL, concept.getSystem());
				assertEquals("Version 1", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals("narrower", translationMatch.getEquivalence().getCode());
				concept = translationMatch.getConcept();
				assertEquals("78901", concept.getCode());
				assertEquals("Source Code 78901", concept.getDisplay());
				assertEquals(CS_URL_4, concept.getSystem());
				assertEquals("Version 5", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
			}
		});
	}

	/**
	 * Some US core ConceptMaps use this style, e.g:
	 *
	 * http://hl7.org/fhir/us/core/ConceptMap/ndc-cvx
	 */
	@Test
	public void testUploadConceptMapWithOnlyCanonicalSourceAtConceptMapLevel() {

		ConceptMap cm = new ConceptMap();
		cm.setUrl("http://foo");
		cm.setSource(new CanonicalType("http://source"));
		cm.setTarget(new CanonicalType("http://target"));
		cm.addGroup().addElement().setCode("source1").addTarget().setCode("target1").setEquivalence(ConceptMapEquivalence.EQUAL);
		myConceptMapDao.create(cm);

		runInTransaction(()->{
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem("http://source")
				.setCode("source1");
			translationRequest.setTarget(new UriType("http://target"));

			ourLog.info("*** About to translate");
			TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);
			ourLog.info("*** Done translating");

			assertTrue(translationResult.getResult().booleanValue());
			assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

			assertEquals(1, translationResult.getMatches().size());

			TranslationMatch translationMatch = translationResult.getMatches().get(0);
			assertEquals("equal", translationMatch.getEquivalence().getCode());
			Coding concept = translationMatch.getConcept();
			assertEquals("target1", concept.getCode());
			assertEquals(null, concept.getDisplay());
			assertEquals("http://target", concept.getSystem());
		});


	}


	/**
	 * Handle ConceptMaps where targets are missing, such as this one:
	 *
	 * https://www.hl7.org/fhir/conceptmap-example-specimen-type.html
	 */
	@Test
	public void testUploadConceptMapWithMappingTargetsMissing() {

		ConceptMap cm = new ConceptMap();
		cm.setUrl("http://foo");
		cm.setSource(new CanonicalType("http://source"));
		cm.setTarget(new CanonicalType("http://target"));
		cm.addGroup().addElement().setCode("source1").addTarget().setCode("target1").setEquivalence(ConceptMapEquivalence.EQUAL);
		cm.addGroup().addElement().setCode("source2"); // no target
		cm.addGroup().addElement().setCode("source3").addTarget().setComment("No target code"); // no target code
		myConceptMapDao.create(cm);

		runInTransaction(()->{
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem("http://source")
				.setCode("source1");
			translationRequest.setTarget(new UriType("http://target"));

			ourLog.info("*** About to translate");
			TranslationResult translationResult = myConceptMapDao.translate(translationRequest, null);
			ourLog.info("*** Done translating");

			assertTrue(translationResult.getResult().booleanValue());
			assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

			assertEquals(1, translationResult.getMatches().size());

			TranslationMatch translationMatch = translationResult.getMatches().get(0);
			assertEquals("equal", translationMatch.getEquivalence().getCode());
			Coding concept = translationMatch.getConcept();
			assertEquals("target1", concept.getCode());
			assertEquals(null, concept.getDisplay());
			assertEquals("http://target", concept.getSystem());
		});


	}



	@Test
	public void testUploadAndApplyR4DemoConceptMap() throws IOException {
		upload("/r4/ConceptMap-icd-sct.xml");

		CodeableConcept sourceCode = new CodeableConcept();
		sourceCode.addCoding()
			.setSystem("http://snomed.info/sct")
			.setCode("263204007");
		TranslationRequest request = new TranslationRequest();
		request.setCodeableConcept(sourceCode);
		request.setTargetSystem(new UriType("http://hl7.org/fhir/sid/icd-10-us"));
		TranslationResult outcome = myConceptMapDao.translate(request, mySrd);

		assertEquals("S52.209A", outcome.getMatches().get(0).getConcept().getCode());
	}

	@Test
	public void testConceptMapFindTermConceptMapByUrl() {
			
		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpConceptMapList = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
		assertEquals(1, theExpConceptMapList.size());
		assertEquals(CM_URL, theExpConceptMapList.get(0).getUrl());
		
	}

	@Test
	public void testConceptMapTwoConceptMapWithSameUrlDifferentVersion() {
				
		String theUrl = "http://loinc.org/property/analyte-suffix";
		ConceptMap theConceptMap1 = new ConceptMap();
		ConceptMap theConceptMap2 = new ConceptMap();
		
		theConceptMap1.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name1").setVersion("v1");
		theConceptMap2.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name2").setVersion("v2");
		
		myConceptMapDao.create(theConceptMap1);
		myConceptMapDao.create(theConceptMap2);
		
		Optional<TermConceptMap> theExpConceptMapV1 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v1");
		Optional<TermConceptMap> theExpConceptMapV2 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v2");
		
		assertTrue(theExpConceptMapV1.isPresent());
		assertEquals(theUrl, theExpConceptMapV1.get().getUrl());
		assertEquals("v1", theExpConceptMapV1.get().getVersion());
		
		assertTrue(theExpConceptMapV2.isPresent());
		assertEquals(theUrl, theExpConceptMapV2.get().getUrl());
		assertEquals("v2", theExpConceptMapV2.get().getVersion());

		// should return the latest one which is v2
		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpSecondOne = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, theUrl);
		
		assertEquals(1, theExpSecondOne.size());
		assertEquals(theUrl, theExpSecondOne.get(0).getUrl());
		assertEquals("v2", theExpSecondOne.get(0).getVersion());
	}

	@Test
	public void testConceptMapTwoConceptMapWithSameUrlOneWithoutVersion() {
				
		String theUrl = "http://loinc.org/property/analyte-suffix";
		ConceptMap theConceptMap1 = new ConceptMap();
		ConceptMap theConceptMap2 = new ConceptMap();
		
		theConceptMap1.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name1").setVersion("v1");
		theConceptMap2.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name2");
		
		myConceptMapDao.create(theConceptMap1);
		myConceptMapDao.create(theConceptMap2);
		
		Optional<TermConceptMap> theExpConceptMapV1 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v1");
		
		assertTrue(theExpConceptMapV1.isPresent());
		assertEquals(theUrl, theExpConceptMapV1.get().getUrl());
		assertEquals("v1", theExpConceptMapV1.get().getVersion());
		
		// should return the latest one which is v2
		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpSecondOne = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, theUrl);
		
		assertEquals(1, theExpSecondOne.size());
		assertEquals(theUrl, theExpSecondOne.get(0).getUrl());
		assertNull(theExpSecondOne.get(0).getVersion());
	}
}
