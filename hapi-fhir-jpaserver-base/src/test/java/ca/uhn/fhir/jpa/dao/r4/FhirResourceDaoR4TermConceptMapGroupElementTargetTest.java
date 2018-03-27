package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

		// <editor-fold desc="Find multiple targets">
		targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "12345");
		assertNotNull(targets);
		assertEquals(2, targets.size());

		target = targets.get(0);
		assertEquals("56789", target.getTargetCode());
		assertEquals("Target Code 56789", target.getTargetDisplay());

		target = targets.get(1);
		assertEquals("67890", target.getTargetCode());
		assertEquals("Target Code 67890", target.getTargetDisplay());
		// </editor-fold>

		// <editor-fold desc="Find no targets">
		targets = myTermConceptMapGroupElementTargetDao
			.findTargetsByCodeSystemsAndSourceCode(CS_URL, CS_URL_3, "BOGUS");
		assertNotNull(targets);
		assertTrue(targets.isEmpty());
		// </editor-fold>
	}
}
