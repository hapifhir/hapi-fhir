package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.model.TranslationMatch;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.UriType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.*;

public class FhirResourceDaoDstu3ConceptMapTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ConceptMapTest.class);

	private IIdType myConceptMapId;

	@Before
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
				assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());
				Coding concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertEquals("Version 4", concept.getVersion());
				assertFalse(concept.getUserSelected());
				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
