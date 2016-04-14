package ca.uhn.fhir.jpa.provider;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.util.TestUtil;

public class SearchParameterMapTest {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	/**
	 * {@link Search} uses these ordinals so they shouldn't get out of order
	 */
	@Test
	public void testEverythingOrdinals() {
		assertEquals(0, EverythingModeEnum.ENCOUNTER_INSTANCE.ordinal());
		assertEquals(1, EverythingModeEnum.ENCOUNTER_TYPE.ordinal());
		assertEquals(2, EverythingModeEnum.PATIENT_INSTANCE.ordinal());
		assertEquals(3, EverythingModeEnum.PATIENT_TYPE.ordinal());
	}
	
}
