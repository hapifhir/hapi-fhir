package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoConceptMap.TranslationResult;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.StringType;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FhirResourceDaoR4ConceptMapTest extends BaseJpaR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testTranslate() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationResult translationResult = myConceptMapDao.translate(
					new StringType(CS_URL),
					new StringType(CS_URL_2),
					new StringType("12345"),
					null);

				assertTrue(translationResult.isMatched());
				assertEquals("Matches found!", translationResult.getMessage());
			}
		});
	}
}
