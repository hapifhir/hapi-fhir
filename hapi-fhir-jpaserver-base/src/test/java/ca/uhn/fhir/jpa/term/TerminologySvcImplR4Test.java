package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TerminologySvcImplR4Test extends BaseJpaR4Test {

	private static final String CM_URL = "http://example.com/my_concept_map";
	private static final String CS_URL = "http://example.com/my_code_system";
	private static final String CS_URL_2 = "http://example.com/my_code_system2";
	private static final String CS_URL_3 = "http://example.com/my_code_system3";

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testStoreNewConceptMap() {
		ConceptMap newConceptMap = createTermConceptMap();
		myTermSvc.storeNewConceptMap(newConceptMap);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="ConceptMap">
				TermConceptMap conceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
				assertNotNull(conceptMap);
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
		ConceptMap newConceptMap = createTermConceptMap();
		myTermSvc.storeNewConceptMap(newConceptMap);

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

	/**
	 * Creates a single {@link org.hl7.fhir.r4.model.ConceptMap} entity that includes:
	 * <br>
	 * <ul>
	 *     <li>
	 *         One group with two elements, each identifying one target apiece.
	 *     </li>
	 *     <li>
	 *         One group with one element, identifying two targets.
	 *     </li>
	 * </ul>
	 * </br>
	 * Both groups identify the same source code system and different target code systems.
	 * </br>
	 * Both groups also include an element with the same source code.
	 *
	 * @return A TermConceptMap {@link org.hl7.fhir.r4.model.ConceptMap} entity for testing.
	 */
	private ConceptMap createTermConceptMap() {
		// <editor-fold desc="ConceptMap">
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);

		// <editor-fold desc="ConceptMap.group(0)">
		ConceptMapGroupComponent group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setTarget(CS_URL_2);

		// <editor-fold desc="ConceptMap.group(0).element(0))">
		SourceElementComponent element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		// <editor-fold desc="ConceptMap.group(0).element(0).target(0)">
		TargetElementComponent target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		// End ConceptMap.group(0).element(0).target(0)
		// </editor-fold>

		// End ConceptMap.group(0).element(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(0).element(1))">
		element = group.addElement();
		element.setCode("23456");
		element.setDisplay("Source Code 23456");

		// <editor-fold desc="ConceptMap.group(0).element(1).target(0)">
		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		// End ConceptMap.group(0).element(1).target(0)
		// </editor-fold>

		// End ConceptMap.group(0).element(1)
		// </editor-fold>

		// End ConceptMap.group(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(1)">
		group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setTarget(CS_URL_3);

		// <editor-fold desc="ConceptMap.group(1).element(0))">
		element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		// <editor-fold desc="ConceptMap.group(1).element(0).target(0)">
		target = element.addTarget();
		target.setCode("56789");
		target.setDisplay("Target Code 56789");
		// End ConceptMap.group(1).element(0).target(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(1).element(0).target(1)">
		target = element.addTarget();
		target.setCode("67890");
		target.setDisplay("Target Code 67890");
		// End ConceptMap.group(1).element(0).target(1)
		// </editor-fold>

		// End ConceptMap.group(1).element(0)
		// </editor-fold>

		// End ConceptMap.group(1)
		// </editor-fold>

		// End ConceptMap
		// </editor-fold>

		return conceptMap;
	}
}
