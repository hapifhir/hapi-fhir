package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FhirResourceDaoR4TermConceptMapGroupElementTargetTest extends BaseJpaR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testFindTargetsByCodeSystemsAndSourceCode() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		// <editor-fold desc="Find a single target">
		List<TermConceptMapGroupElementTarget> targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_2, "12345");
		assertNotNull(targets);
		assertEquals(1, targets.size());

		TermConceptMapGroupElementTarget target = targets.get(0);
		assertEquals("34567", target.getCode());
		assertEquals("Target Code 34567", target.getDisplay());
		assertEquals(CS_URL_2, target.getSystem());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals(CM_URL, target.getConceptMapUrl());

		// <editor-fold desc="Access associated entities">
		TermConceptMapGroupElement element = target.getConceptMapGroupElement();
		assertEquals("12345", element.getCode());
		assertEquals("Source Code 12345", element.getDisplay());

		TermConceptMapGroup group = element.getConceptMapGroup();
		assertEquals(CS_URL, group.getSource());
		assertEquals(CS_URL_2, group.getTarget());

		TermConceptMap conceptMap = group.getConceptMap();
		assertNotNull(conceptMap);
		// </editor-fold>
		// </editor-fold>

		// <editor-fold desc="Find multiple targets">
		targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "12345");
		assertNotNull(targets);
		assertEquals(2, targets.size());

		target = targets.get(0);
		assertEquals("56789", target.getCode());
		assertEquals("Target Code 56789", target.getDisplay());
		assertEquals(CS_URL_3, target.getSystem());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals(CM_URL, target.getConceptMapUrl());

		target = targets.get(1);
		assertEquals("67890", target.getCode());
		assertEquals("Target Code 67890", target.getDisplay());
		assertEquals(CS_URL_3, target.getSystem());
		assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
		assertEquals(CM_URL, target.getConceptMapUrl());
		// </editor-fold>

		// <editor-fold desc="Find no targets">
		targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "BOGUS");
		assertNotNull(targets);
		assertTrue(targets.isEmpty());
		// </editor-fold>
	}
}
