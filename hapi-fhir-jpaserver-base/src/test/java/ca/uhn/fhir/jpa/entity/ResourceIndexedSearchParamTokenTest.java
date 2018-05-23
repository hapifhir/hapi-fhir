package ca.uhn.fhir.jpa.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

}
