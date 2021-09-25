package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTypeEnumTest {

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testOrder() {
		// Ordinals are used in DB columns so the order
		// shouldn't change
		assertEquals(0, TagTypeEnum.TAG.ordinal());
		assertEquals(1, TagTypeEnum.PROFILE.ordinal());
		assertEquals(2, TagTypeEnum.SECURITY_LABEL.ordinal());
	}
	
}
