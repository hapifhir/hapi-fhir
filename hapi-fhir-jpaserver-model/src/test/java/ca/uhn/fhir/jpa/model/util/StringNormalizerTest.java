package ca.uhn.fhir.jpa.model.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringNormalizerTest {
	@Test
	public void testNormalizeString() {
		assertEquals("TEST TEST", StringNormalizer.normalizeString("TEST teSt"));
		assertEquals("AEIØU", StringNormalizer.normalizeString("åéîøü"));
		assertEquals("杨浩", StringNormalizer.normalizeString("杨浩"));
	}
}
