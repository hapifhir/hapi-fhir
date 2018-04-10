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
	public void testFindTargetsByCodeSystemsAndSourceCodeManyToOne() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		// <editor-fold desc="Find multiple targets">
		List<TermConceptMapGroupElementTarget> targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "12345");
		assertNotNull(targets);
		assertEquals(2, targets.size());

		TermConceptMapGroupElementTarget target = targets.get(0);
		assertEquals("56789", target.getCode());
		assertEquals("Target Code 56789", target.getDisplay());
		assertEquals(CS_URL_3, target.getSystem());
		assertEquals(ConceptMapEquivalence.EQUAL, target.getEquivalence());
		assertEquals(VS_URL_2, target.getValueSet());
		assertEquals(CM_URL, target.getConceptMapUrl());

		target = targets.get(1);
		assertEquals("67890", target.getCode());
		assertEquals("Target Code 67890", target.getDisplay());
		assertEquals(CS_URL_3, target.getSystem());
		assertEquals(ConceptMapEquivalence.WIDER, target.getEquivalence());
		assertEquals(VS_URL_2, target.getValueSet());
		assertEquals(CM_URL, target.getConceptMapUrl());
		// </editor-fold>
	}

	@Test
	public void testFindTargetsByCodeSystemsAndSourceCodeOneToOne() {
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

	@Test
	public void testFindTargetsByCodeSystemsAndSourceUnmapped() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		// <editor-fold desc="Find no targets">
		List<TermConceptMapGroupElementTarget> targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "BOGUS");
		assertNotNull(targets);
		assertTrue(targets.isEmpty());
		// </editor-fold>
	}
}
