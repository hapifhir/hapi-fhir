package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4ConceptMapTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4ConceptMapTest.class);
	@Autowired
	protected ITermConceptMapGroupDao myConceptMapGroupDao;
	@Autowired
	protected ITermConceptMapGroupElementDao myConceptMapGroupElementDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myConceptMapGroupElementTargetDao;
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
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult());
			}
		});

	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to multiple target codes">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());
				assertEquals(translationResult.size(), new HashSet<>(translationResult.getResults()).size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToOne() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to one target code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_2));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				// <editor-fold desc="Attempt to map unknown source code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult());
				assertEquals("No Matches found", translationResult.getMessage());

				assertEquals(0, translationResult.getResults().size());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithCodeOnly() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(3, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem2() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceAndTargetSystem3() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystem() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source code system
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(3, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion1() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceSystemAndVersion3() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithSourceValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   source value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setSource(new UriType(VS_URL));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(3, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateUsingPredicatesWithTargetValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   target value set
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("12345");
				translationRequest.setTarget(new UriType(VS_URL_2));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(3, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("34567", translationMatch.getCode());
				assertEquals("Target Code 34567", translationMatch.getDisplay());
				assertEquals(CS_URL_2, translationMatch.getSystem());
				assertEquals("Version 2", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(2);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverse() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	/**
	 * Make sure we can handle mapping where the system has no explicit version
	 * specified in the ConceptMap
	 */
	@Test
	public void testTranslateWithReverse_NonVersionedSystem() {

		ConceptMap conceptMap1 = new ConceptMap();
		conceptMap1.setUrl(CM_URL);

		conceptMap1.setSource(new UriType(VS_URL));
		conceptMap1.setTarget(new UriType(VS_URL_2));

		ConceptMap.ConceptMapGroupComponent group = conceptMap1.addGroup();
		group.setSource(CS_URL);
		group.setTarget(CS_URL_2);

		ConceptMap.SourceElementComponent element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		ConceptMap.TargetElementComponent target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		element = group.addElement();
		element.setCode("23456");
		element.setDisplay("Source Code 23456");

		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		group = conceptMap1.addGroup();
		group.setSource(CS_URL);
		group.setTarget(CS_URL_3);

		element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		target = element.addTarget();
		target.setCode("56789");
		target.setDisplay("Target Code 56789");
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		target = element.addTarget();
		target.setCode("67890");
		target.setDisplay("Target Code 67890");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		group = conceptMap1.addGroup();
		group.setSource(CS_URL_4);
		group.setTarget(CS_URL_2);

		element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setEquivalence(ConceptMapEquivalence.NARROWER);

		conceptMap1.setId(myConceptMapId);
		myConceptMapDao.update(conceptMap1, mySrd).getId().toUnqualifiedVersionless();


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

			TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

			assertTrue(translationResult.getResult());
			assertEquals("Matches found", translationResult.getMessage());

			assertEquals(2, translationResult.getResults().size());

			TranslateConceptResult translationMatch = translationResult.getResults().get(0);
			assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
			assertEquals("12345", translationMatch.getCode());
			assertEquals("Source Code 12345", translationMatch.getDisplay());
			assertEquals(CS_URL, translationMatch.getSystem());
			assertEquals(null, translationMatch.getSystemVersion());
			assertEquals(CM_URL, translationMatch.getConceptMapUrl());

			translationMatch = translationResult.getResults().get(1);
			assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
			assertEquals("12345", translationMatch.getCode());
			assertEquals("Source Code 12345", translationMatch.getDisplay());
			assertEquals(CS_URL_4, translationMatch.getSystem());
			assertEquals(null, translationMatch.getSystemVersion());
			assertEquals(CM_URL, translationMatch.getConceptMapUrl());
		});

	}

	@Test
	public void testTranslateWithReverseHavingEquivalence() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 3", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
			}
		});
	}

	@Test
	public void testTranslateWithReverseByCodeSystemsAndSourceCodeUnmapped() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				// <editor-fold desc="Attempt to map unknown source code">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL_3)
					.setCode("BOGUS");
				translationRequest.setTargetSystem(new UriType(CS_URL));
				translationRequest.setReverse(true);

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult());
				assertEquals("No Matches found", translationResult.getMessage());

				assertEquals(0, translationResult.getResults().size());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithCodeOnly() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				/*
				 * Provided:
				 *   source code
				 *   reverse = true
				 */
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setCode("34567");
				translationRequest.setReverse(true);

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem1() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceAndTargetSystem4() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(1, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystem() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceSystemAndVersion() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithSourceValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	@Test
	public void testTranslateWithReverseUsingPredicatesWithTargetValueSet() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
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

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("12345", translationMatch.getCode());
				assertEquals("Source Code 12345", translationMatch.getDisplay());
				assertEquals(CS_URL, translationMatch.getSystem());
				assertEquals("Version 1", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(ConceptMapEquivalence.NARROWER.toCode(), translationMatch.getEquivalence());
				assertEquals("78901", translationMatch.getCode());
				assertEquals("Source Code 78901", translationMatch.getDisplay());
				assertEquals(CS_URL_4, translationMatch.getSystem());
				assertEquals("Version 5", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
			}
		});
	}

	/**
	 * Some US core ConceptMaps use this style, e.g:
	 * <p>
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

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem("http://source")
				.setCode("source1");
			translationRequest.setTarget(new UriType("http://target"));

			ourLog.info("*** About to translate");
			TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);
			ourLog.info("*** Done translating");

			assertTrue(translationResult.getResult());
			assertEquals("Matches found", translationResult.getMessage());

			assertEquals(1, translationResult.getResults().size());

			TranslateConceptResult translationMatch = translationResult.getResults().get(0);
			assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
			assertEquals("target1", translationMatch.getCode());
			assertNull(translationMatch.getDisplay());
			assertEquals("http://target", translationMatch.getSystem());

		});

		CodeableConcept sourceCodeableConcept = new CodeableConcept();
		sourceCodeableConcept.addCoding(new Coding("http://source", "source1", null));
		List<TranslateConceptResult> translationResults = myValidationSupport.translateConcept(new IValidationSupport.TranslateCodeRequest(Collections.unmodifiableList(sourceCodeableConcept.getCoding()), "http://target")).getResults();
		assertThat(translationResults.toString(), translationResults, hasItem(
			new TranslateConceptResult()
				.setSystem("http://target")
				.setCode("target1")
				.setEquivalence("equal")
				.setConceptMapUrl("http://foo")
				.setValueSet("http://target"))
		);
		assertEquals(translationResults.size(), new HashSet<>(translationResults).size());
	}

	/**
	 * Handle ConceptMaps where targets are missing, such as this one:
	 * <p>
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

		runInTransaction(() -> {
			TranslationRequest translationRequest = new TranslationRequest();
			translationRequest.getCodeableConcept().addCoding()
				.setSystem("http://source")
				.setCode("source1");
			translationRequest.setTarget(new UriType("http://target"));

			ourLog.info("*** About to translate");
			TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);
			ourLog.info("*** Done translating");

			assertTrue(translationResult.getResult());
			assertEquals("Matches found", translationResult.getMessage());

			assertEquals(1, translationResult.getResults().size());

			TranslateConceptResult translationMatch = translationResult.getResults().get(0);
			assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
			assertEquals("target1", translationMatch.getCode());
			assertNull(translationMatch.getDisplay());
			assertEquals("http://target", translationMatch.getSystem());
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
		TranslateConceptResults outcome = myConceptMapDao.translate(request, mySrd);

		assertEquals("S52.209A", outcome.getResults().get(0).getCode());
	}

	@Test
	public void testConceptMapFindTermConceptMapByUrl() {
		runInTransaction(()-> {
			Pageable page = PageRequest.of(0, 1);
			List<TermConceptMap> theExpConceptMapList = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
			assertEquals(1, theExpConceptMapList.size());
			assertEquals(CM_URL, theExpConceptMapList.get(0).getUrl());
		});
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

		runInTransaction(()-> {
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
		});
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

		runInTransaction(()-> {
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
		});
	}
}
