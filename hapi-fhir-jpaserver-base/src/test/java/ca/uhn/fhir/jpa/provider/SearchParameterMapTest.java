package ca.uhn.fhir.jpa.provider;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.uhn.fhir.jpa.dao.SearchParameterMap.EverythingModeEnum;

public class SearchParameterMapTest {

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
