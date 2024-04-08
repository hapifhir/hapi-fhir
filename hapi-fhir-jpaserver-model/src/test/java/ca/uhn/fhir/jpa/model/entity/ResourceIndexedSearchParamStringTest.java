package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("SpellCheckingInspection")
public class ResourceIndexedSearchParamStringTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString("Patient", "NAME", "value", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		calculateHashes(token);

		// Make sure our hashing function gives consistent results
		assertEquals(-3910281295602822115L, token.getHashNormalizedPrefix().longValue());
		assertEquals(-4852130101009756551L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString("Patient", "NAME", "vZZZZZZZZZZZZZZZZ", "VZZZZZZzzzZzzzZ");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		calculateHashes(token);

		// Should be the same as in testHashFunctions()
		assertEquals(-3910281295602822115L, token.getHashNormalizedPrefix().longValue());

		// Should be different from testHashFunctions()
		assertEquals(-7052799977993561763L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly_John_JN_vs_JAN() {
		final ResourceIndexedSearchParamString token1 = new ResourceIndexedSearchParamString("Patient", "query-1-param", "JN", "John");
		token1.setResource(new ResourceTable().setResourceType("Patient"));

		final ResourceIndexedSearchParamString token2 = new ResourceIndexedSearchParamString("Patient", "query-1-param", "JAN", "John");
		token2.setResource(new ResourceTable().setResourceType("Patient"));

		calculateHashes(token1, token2);

		assertAll(
			// We only hash on the first letter for performance reasons
			() -> assertEquals(token1.getHashNormalizedPrefix(), token2.getHashNormalizedPrefix()),
			() -> assertEquals(token1.getHashExact(), token2.getHashExact()),
			() -> assertNotEquals(token1.hashCode(), token2.hashCode())
		);
	}

	@Test
	public void testHashFunctionsPrefixOnly_Doe_T_vs_D() {
		final ResourceIndexedSearchParamString token1 = new ResourceIndexedSearchParamString("Patient", "query-1-param", "T", "Doe");
		token1.setResource(new ResourceTable().setResourceType("Patient"));
		final ResourceIndexedSearchParamString token2 = new ResourceIndexedSearchParamString("Patient", "query-1-param", "D", "Doe");
		token2.setResource(new ResourceTable().setResourceType("Patient"));

		calculateHashes(token1, token2);

		assertAll(
			() -> assertNotEquals(token1.getHashNormalizedPrefix(), token2.getHashNormalizedPrefix()),
			() -> assertEquals(token1.getHashExact(), token2.getHashExact()),
			() -> assertNotEquals(token1.hashCode(), token2.hashCode())
		);
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamString val1 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		calculateHashes(val1, val2);
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

	@Test
	public void testEqualsDifferentPartition() {
		ResourceIndexedSearchParamString val1 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
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
