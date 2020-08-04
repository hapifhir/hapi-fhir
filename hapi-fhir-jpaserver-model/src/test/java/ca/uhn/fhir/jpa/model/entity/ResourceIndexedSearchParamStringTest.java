package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("SpellCheckingInspection")
public class ResourceIndexedSearchParamStringTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new PartitionSettings(), new ModelConfig(), "Patient", "NAME", "value", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());
		assertEquals(-1970227166134682431L, token.getHashExact().longValue());
	}

	@Test
	public void testHashFunctionsPrefixOnly() {
		ResourceIndexedSearchParamString token = new ResourceIndexedSearchParamString(new PartitionSettings(), new ModelConfig(), "Patient", "NAME", "vZZZZZZZZZZZZZZZZ", "VZZZZZZzzzZzzzZ");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Should be the same as in testHashFunctions()
		assertEquals(6598082761639188617L, token.getHashNormalizedPrefix().longValue());

		// Should be different from testHashFunctions()
		assertEquals(7045214018927566109L, token.getHashExact().longValue());
	}


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamString val1 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		val1.setPartitionSettings(new PartitionSettings());
		val1.setModelConfig(new ModelConfig());
		val1.calculateHashes();
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		val2.setPartitionSettings(new PartitionSettings());
		val2.setModelConfig(new ModelConfig());
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
		val1.setModelConfig(new ModelConfig());
		val1.calculateHashes();
		ResourceIndexedSearchParamString val2 = new ResourceIndexedSearchParamString()
			.setValueExact("aaa")
			.setValueNormalized("AAA");
		val2.setPartitionSettings(new PartitionSettings().setIncludePartitionInSearchHashes(true));
		val2.setModelConfig(new ModelConfig());
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

}
