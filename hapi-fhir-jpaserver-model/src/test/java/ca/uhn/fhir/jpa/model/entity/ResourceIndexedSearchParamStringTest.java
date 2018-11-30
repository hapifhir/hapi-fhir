package ca.uhn.fhir.jpa.model.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class ResourceIndexedSearchParamStringTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new ModelConfig(), "NAME", "value", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());
		assertEquals(-1970227166134682431L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new ModelConfig(), "NAME", "vZZZZZZZZZZZZZZZZ", "VZZZZZZzzzZzzzZ");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Should be the same as in testHashFunctions()
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());

		// Should be different from testHashFunctions()
		assertEquals(7045214018927566109L, token.getHashExact().longValue());
	}

}
