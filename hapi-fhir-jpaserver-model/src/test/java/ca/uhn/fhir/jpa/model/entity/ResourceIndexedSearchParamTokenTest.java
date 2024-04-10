package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		calculateHashes(token);

		// Make sure our hashing function gives consistent results
		assertEquals(5093049432106713575L, token.getHashSystem().longValue());
		assertEquals(5277369581506551071L, token.getHashSystemAndValue().longValue());
		assertEquals(-4852130101009756551L, token.getHashValue().longValue());
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken("Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		calculateHashes(token);

		// Make sure our hashing function gives consistent results
		assertEquals(5093049432106713575L, token.getHashSystem().longValue());
		assertEquals(5277369581506551071L, token.getHashSystemAndValue().longValue());
		assertEquals(-4852130101009756551L, token.getHashValue().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamToken val1 = new ResourceIndexedSearchParamToken().setValue("AAA");
		ResourceIndexedSearchParamToken val2 = new ResourceIndexedSearchParamToken().setValue("AAA");
		calculateHashes(val1, val2);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

	private void calculateHashes(BaseResourceIndex... theParams) {
		Arrays.stream(theParams).forEach(param -> param.calculateHashes(new ResourceIndexHasher(new PartitionSettings(), new StorageSettings())));
	}
}
