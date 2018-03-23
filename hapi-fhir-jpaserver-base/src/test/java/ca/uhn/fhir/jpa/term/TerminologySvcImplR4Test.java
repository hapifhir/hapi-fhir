package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TerminologySvcImplR4Test extends BaseJpaR4Test {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testStoreNewConceptMap() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="ConceptMap">
				Optional<TermConceptMap> optionalConceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
				assertTrue(optionalConceptMap.isPresent());

				TermConceptMap conceptMap = optionalConceptMap.get();
				assertEquals(2, conceptMap.getConceptMapGroups().size());

				// <editor-fold desc="ConceptMap.group(0)">
				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_2, group.getTargetUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(0).element(0)">
				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(0).target(0)">
				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("34567", target.getTargetCode());
				assertEquals("Target Code 34567", target.getTargetDisplay());
				// End ConceptMap.group(0).element(0).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(0).element(1)">
				element = group.getConceptMapGroupElements().get(1);
				assertEquals("23456", element.getSourceCode());
				assertEquals("Source Code 23456", element.getSourceDisplay());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(1).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("45678", target.getTargetCode());
				assertEquals("Target Code 45678", target.getTargetDisplay());
				// End ConceptMap.group(0).element(1).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(1)
				// </editor-fold>

				// End ConceptMap.group(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1)">
				group = conceptMap.getConceptMapGroups().get(1);
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_3, group.getTargetUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(1).element(0)">
				element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(1).element(0).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("56789", target.getTargetCode());
				assertEquals("Target Code 56789", target.getTargetDisplay());
				// End ConceptMap.group(1).element(0).target(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1).element(0).target(1)">
				target = element.getConceptMapGroupElementTargets().get(1);
				assertEquals("67890", target.getTargetCode());
				assertEquals("Target Code 67890", target.getTargetDisplay());
				// End ConceptMap.group(1).element(0).target(1)
				// </editor-fold>

				// End ConceptMap.group(1).element(0)
				// </editor-fold>

				// End ConceptMap.group(0)
				// </editor-fold>

				// End ConceptMap
				// </editor-fold>
			}
		});
	}

	@Test
	public void testTranslate() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to one target code">
				List<TermConceptMapGroupElementTarget> targets =
					myTermSvc.translate(CS_URL,"12345",CS_URL_2);
				assertNotNull(targets);
				assertEquals(1, targets.size());

				TermConceptMapGroupElementTarget target = targets.get(0);
				assertEquals("34567", target.getTargetCode());
				assertEquals("Target Code 34567", target.getTargetDisplay());

				// <editor-fold desc="Access associated entities">
				TermConceptMapGroupElement element = target.getConceptMapGroupElement();
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());

				TermConceptMapGroup group = element.getConceptMapGroup();
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_2, group.getTargetUrl());

				TermConceptMap conceptMap = group.getConceptMap();
				assertNotNull(conceptMap);
				// </editor-fold>
				// </editor-fold>

				// <editor-fold desc="Map one source code to multiple target codes">
				targets = myTermSvc.translate(CS_URL,"12345",CS_URL_3);
				assertNotNull(targets);
				assertEquals(2, targets.size());

				target = targets.get(0);
				assertEquals("56789", target.getTargetCode());
				assertEquals("Target Code 56789", target.getTargetDisplay());

				target = targets.get(1);
				assertEquals("67890", target.getTargetCode());
				assertEquals("Target Code 67890", target.getTargetDisplay());
				// </editor-fold>

				// <editor-fold desc="Attempt to map unknown source code">
				targets = myTermSvc.translate(CS_URL,"BOGUS",CS_URL_3);
				assertNotNull(targets);
				assertTrue(targets.isEmpty());
				// </editor-fold>
			}
		});
	}
}
