package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceIndexedSearchParamUriTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamUri token = new ResourceIndexedSearchParamUri("NAME", "http://example.com");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-6132951326739875838L, token.getHashUri().longValue());
	}


}
