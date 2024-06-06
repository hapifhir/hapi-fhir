package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamToken val1 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamToken val2 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertNotNull(val1);
		assertEquals(val1, val2);
		assertThat("").isNotEqualTo(val1);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		ResourceIndexedSearchParamToken param = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		ResourceIndexedSearchParamToken param2 = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");

		param2.optimizeIndexStorage();

		assertEquals(param, param2);
		assertEquals(param2, param);
		assertEquals(param.hashCode(), param2.hashCode());
	}

}
