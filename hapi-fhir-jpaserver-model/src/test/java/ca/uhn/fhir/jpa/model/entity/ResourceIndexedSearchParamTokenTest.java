package ca.uhn.fhir.jpa.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertThat(token.getHashSystem().longValue()).isEqualTo(-8558989679010582575L);
		assertThat(token.getHashSystemAndValue().longValue()).isEqualTo(-8644532105141886455L);
		assertThat(token.getHashValue().longValue()).isEqualTo(-1970227166134682431L);
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertThat(token.getHashSystem().longValue()).isEqualTo(-8558989679010582575L);
		assertThat(token.getHashSystemAndValue().longValue()).isEqualTo(-8644532105141886455L);
		assertThat(token.getHashValue().longValue()).isEqualTo(-1970227166134682431L);
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
		assertThat(val1).isNotNull().isEqualTo(val1);
		assertThat(val2).isEqualTo(val1);
		assertThat("").isNotEqualTo(val1);
	}

}
