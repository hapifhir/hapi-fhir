package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("SpellCheckingInspection")
public class ResourceIndexedSearchParamStringTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "NAME", "value", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());
		assertEquals(-1970227166134682431L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "NAME", "vZZZZZZZZZZZZZZZZ", "VZZZZZZzzzZzzzZ");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Should be the same as in testHashFunctions()
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());

		// Should be different from testHashFunctions()
		assertEquals(7045214018927566109L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly_John_JN_vs_JAN() {
		final ResourceIndexedSearchParamString token1 = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "query-1-param", "JN", "John");
		token1.setResource(new ResourceTable().setResourceType("Patient"));
		token1.calculateHashes();

		final ResourceIndexedSearchParamString token2 = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "query-1-param", "JAN", "John");
		token2.setResource(new ResourceTable().setResourceType("Patient"));
		token2.calculateHashes();

		assertAll(
			// We only hash on the first letter for performance reasons
			() -> assertEquals(token1.getHashNormalizedPrefix(), token2.getHashNormalizedPrefix()),
			() -> assertEquals(token1.getHashExact(), token2.getHashExact()),
			() -> assertNotEquals(token1.hashCode(), token2.hashCode())
		);
	}

	@Test
	public void testHashFunctionsPrefixOnly_Doe_T_vs_D() {
		final ResourceIndexedSearchParamString token1 = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "query-1-param", "T", "Doe");
		token1.setResource(new ResourceTable().setResourceType("Patient"));
		token1.calculateHashes();

		final ResourceIndexedSearchParamString token2 = new ResourceIndexedSearchParamString(new PartitionSettings(), new StorageSettings(), "Patient", "query-1-param", "D", "Doe");
		token2.setResource(new ResourceTable().setResourceType("Patient"));
		token2.calculateHashes();

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
		val1.setPartitionSettings(new PartitionSettings());
		val1.setStorageSettings(new StorageSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		val2.setPartitionSettings(new PartitionSettings());
		val2.setStorageSettings(new StorageSettings());
		val2.calculateHashes();
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
		val1.setPartitionSettings(new PartitionSettings().setIncludePartitionInSearchHashes(true));
		val1.setStorageSettings(new StorageSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		val2.setPartitionSettings(new PartitionSettings().setIncludePartitionInSearchHashes(true));
		val2.setStorageSettings(new StorageSettings());
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

}
