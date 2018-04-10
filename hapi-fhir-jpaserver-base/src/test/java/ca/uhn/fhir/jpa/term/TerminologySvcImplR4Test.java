package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
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
				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());
				assertEquals(2, conceptMap.getConceptMapGroups().size());

				// <editor-fold desc="ConceptMap.group(0)">
				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);
				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(0).element(0)">
				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(0).target(0)">
				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
				// End ConceptMap.group(0).element(0).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(0).element(1)">
				element = group.getConceptMapGroupElements().get(1);
				assertEquals("23456", element.getCode());
				assertEquals("Source Code 23456", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(1).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("45678", target.getCode());
				assertEquals("Target Code 45678", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
				// End ConceptMap.group(0).element(1).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(1)
				// </editor-fold>

				// End ConceptMap.group(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1)">
				group = conceptMap.getConceptMapGroups().get(1);
				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 3", group.getSourceVersion());
				assertEquals(CS_URL_3, group.getTarget());
				assertEquals("Version 4", group.getTargetVersion());
				assertEquals(CM_URL, group.getConceptMapUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(1).element(0)">
				element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 3", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(1).element(0).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
				// End ConceptMap.group(1).element(0).target(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1).element(0).target(1)">
				target = element.getConceptMapGroupElementTargets().get(1);
				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
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
	public void testTranslateOneToMany() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to multiple target codes">
				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(CS_URL, CS_URL_3, "12345");
				assertNotNull(targets);
				assertEquals(2, targets.size());

				TermConceptMapGroupElementTarget target = targets.get(0);
				assertEquals("56789", target.getCode());
				assertEquals("Target Code 56789", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				target = targets.get(1);
				assertEquals("67890", target.getCode());
				assertEquals("Target Code 67890", target.getDisplay());
				assertEquals(CS_URL_3, target.getSystem());
				assertEquals("Version 4", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());
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
				List<TermConceptMapGroupElementTarget> targets =
					myTermSvc.translate(CS_URL, CS_URL_2, "12345");
				assertNotNull(targets);
				assertEquals(1, targets.size());

				TermConceptMapGroupElementTarget target = targets.get(0);
				assertEquals("34567", target.getCode());
				assertEquals("Target Code 34567", target.getDisplay());
				assertEquals(CS_URL_2, target.getSystem());
				assertEquals("Version 2", target.getSystemVersion());
				assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
				assertEquals(VS_URL_2, target.getValueSet());
				assertEquals(CM_URL, target.getConceptMapUrl());

				// <editor-fold desc="Access associated entities">
				TermConceptMapGroupElement element = target.getConceptMapGroupElement();
				assertEquals("12345", element.getCode());
				assertEquals("Source Code 12345", element.getDisplay());
				assertEquals(CS_URL, element.getSystem());
				assertEquals("Version 1", element.getSystemVersion());
				assertEquals(VS_URL, element.getValueSet());
				assertEquals(CM_URL, element.getConceptMapUrl());

				TermConceptMapGroup group = element.getConceptMapGroup();
				assertEquals(CS_URL, group.getSource());
				assertEquals("Version 1", group.getSourceVersion());
				assertEquals(VS_URL, group.getSourceValueSet());
				assertEquals(CS_URL_2, group.getTarget());
				assertEquals("Version 2", group.getTargetVersion());
				assertEquals(VS_URL_2, group.getTargetValueSet());
				assertEquals(CM_URL, group.getConceptMapUrl());

				TermConceptMap conceptMap = group.getConceptMap();
				assertNotNull(conceptMap);
				assertEquals(VS_URL, conceptMap.getSource());
				assertEquals(VS_URL_2, conceptMap.getTarget());
				assertEquals(CM_URL, conceptMap.getUrl());
				// </editor-fold>
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
				List<TermConceptMapGroupElementTarget> targets = myTermSvc.translate(CS_URL, CS_URL_3, "BOGUS");
				assertNotNull(targets);
				assertTrue(targets.isEmpty());
				// </editor-fold>
			}
		});
	}
}
