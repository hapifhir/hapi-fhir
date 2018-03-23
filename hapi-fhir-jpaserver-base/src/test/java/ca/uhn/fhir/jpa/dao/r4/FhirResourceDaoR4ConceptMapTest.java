package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FhirResourceDaoR4ConceptMapTest extends BaseJpaR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testFindConceptMapByUrl() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		Optional<TermConceptMap> optionalTermConceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
		assertTrue(optionalTermConceptMap.isPresent());
	}

	@Test
	public void testSearchForMissingConceptMapByUrl() {
		Optional<TermConceptMap> optionalTermConceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
		assertFalse(optionalTermConceptMap.isPresent());
//		assertNull(optionalTermConceptMap.get());
		// TODO: NoSuchElementException is thrown instead of returning null.
	}
}
