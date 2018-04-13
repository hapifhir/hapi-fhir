package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.ConceptMap;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FhirResourceDaoR4TermConceptMapTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4TermConceptMapTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testFindConceptMapByUrl() {
		ConceptMap conceptMap = createConceptMap();
		myTermSvc.storeNewConceptMap(conceptMap);

		ourLog.info("ConceptMap:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		Optional<TermConceptMap> optionalTermConceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
		assertTrue(optionalTermConceptMap.isPresent());
		TermConceptMap termConceptMap = optionalTermConceptMap.get();
		assertEquals(VS_URL, termConceptMap.getSource());
		assertEquals(VS_URL_2, termConceptMap.getTarget());
		assertEquals(CM_URL, termConceptMap.getUrl());
	}
}
