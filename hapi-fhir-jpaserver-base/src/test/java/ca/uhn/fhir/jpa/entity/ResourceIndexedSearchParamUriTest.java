package ca.uhn.fhir.jpa.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceIndexedSearchParamUriTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamUri token = new ResourceIndexedSearchParamUri("NAME", "http://example.com");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-6132951326739875838L, token.getHashUri().longValue());
	}


}
