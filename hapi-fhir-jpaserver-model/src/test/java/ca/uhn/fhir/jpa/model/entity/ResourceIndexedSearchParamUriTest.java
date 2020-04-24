package ca.uhn.fhir.jpa.model.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceIndexedSearchParamUriTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamUri token = new ResourceIndexedSearchParamUri("Patient", "NAME", "http://example.com");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-6132951326739875838L, token.getHashUri().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamUri val1 = new ResourceIndexedSearchParamUri()
			.setUri("http://foo");
		val1.calculateHashes();
		ResourceIndexedSearchParamUri val2 = new ResourceIndexedSearchParamUri()
			.setUri("http://foo");
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}


}
