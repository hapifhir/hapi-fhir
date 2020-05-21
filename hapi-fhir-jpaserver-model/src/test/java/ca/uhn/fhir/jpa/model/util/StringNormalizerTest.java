package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.util.StringNormalizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringNormalizerTest {
	@Test
	public void testNormalizeString() {
		assertEquals("TEST TEST", StringNormalizer.normalizeStringForSearchIndexing("TEST teSt"));
		assertEquals("AEIØU", StringNormalizer.normalizeStringForSearchIndexing("åéîøü"));
		assertEquals("杨浩", StringNormalizer.normalizeStringForSearchIndexing("杨浩"));
	}
}
