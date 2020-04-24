package ca.uhn.fhir.jpa.model.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamToken val1 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val1.calculateHashes();
		ResourceIndexedSearchParamToken val2 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

}
