package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoConceptMap.TranslationMatch;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoConceptMap.TranslationResult;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.StringType;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FhirResourceDaoR4ConceptMapTest extends BaseJpaR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testTranslateOneToMany() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to multiple target codes">
				CodeableConcept codeableConcept = new CodeableConcept();
				codeableConcept.addCoding()
					.setSystem(CS_URL)
					.setCode("12345");

				TranslationResult translationResult = myConceptMapDao.translate(
					codeableConcept,
					new StringType(CS_URL_3),
					null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(2, translationResult.getMatches().size());
				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());

				Coding concept = translationMatch.getConcept();
				assertEquals("56789", concept.getCode());
				assertEquals("Target Code 56789", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertFalse(concept.getUserSelected());

				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());

				translationMatch = translationResult.getMatches().get(1);
				assertEquals(ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence().getCode());

				concept = translationMatch.getConcept();
				assertEquals("67890", concept.getCode());
				assertEquals("Target Code 67890", concept.getDisplay());
				assertEquals(CS_URL_3, concept.getSystem());
				assertFalse(concept.getUserSelected());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateOneToOne() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to one target code">
				CodeableConcept codeableConcept = new CodeableConcept();
				codeableConcept.addCoding()
					.setSystem(CS_URL)
					.setCode("12345");

				TranslationResult translationResult = myConceptMapDao.translate(
					codeableConcept,
					new StringType(CS_URL_2),
					null);

				assertTrue(translationResult.getResult().booleanValue());
				assertEquals("Matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(1, translationResult.getMatches().size());
				TranslationMatch translationMatch = translationResult.getMatches().get(0);
				assertEquals(ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence().getCode());

				Coding concept = translationMatch.getConcept();
				assertEquals("34567", concept.getCode());
				assertEquals("Target Code 34567", concept.getDisplay());
				assertEquals(CS_URL_2, concept.getSystem());
				assertFalse(concept.getUserSelected());

				assertEquals(CM_URL, translationMatch.getSource().getValueAsString());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslateUnmapped() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Attempt to map unknown source code">
				CodeableConcept codeableConcept = new CodeableConcept();
				codeableConcept.addCoding()
					.setSystem(CS_URL)
					.setCode("BOGUS");

				TranslationResult translationResult = myConceptMapDao.translate(
					codeableConcept,
					new StringType(CS_URL_3),
					null);

				assertFalse(translationResult.getResult().booleanValue());
				assertEquals("No matches found!", translationResult.getMessage().getValueAsString());

				assertEquals(0, translationResult.getMatches().size());
				// </editor-fold>
			}
		});
	}
}
