package ca.uhn.fhir.jpa.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTypeEnumTest {

	@Test
	public void testOrder() {
		// Ordinals are used in DB columns so the order
		// shouldn't change
		assertEquals(0, TagTypeEnum.TAG.ordinal());
		assertEquals(1, TagTypeEnum.PROFILE.ordinal());
		assertEquals(2, TagTypeEnum.SECURITY_LABEL.ordinal());
	}
	
}
