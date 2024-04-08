package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamUriTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamUri token = new ResourceIndexedSearchParamUri("Patient", "NAME", "http://example.com");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		calculateHashes(token);

		// Make sure our hashing function gives consistent results
		assertEquals(-1155826022777655430L, token.getHashUri().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamUri val1 = new ResourceIndexedSearchParamUri().setUri("http://foo");
		ResourceIndexedSearchParamUri val2 = new ResourceIndexedSearchParamUri().setUri("http://foo");
		calculateHashes(val1, val2);
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

	private void calculateHashes(BaseResourceIndex... theParams) {
		Arrays.stream(theParams).forEach(param -> param.calculateHashes(new ResourceIndexHasher(new PartitionSettings(), new StorageSettings())));
	}
}
