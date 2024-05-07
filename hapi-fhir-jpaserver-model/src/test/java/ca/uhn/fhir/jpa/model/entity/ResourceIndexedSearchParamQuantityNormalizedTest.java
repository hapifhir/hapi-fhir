package ca.uhn.fhir.jpa.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceIndexedSearchParamQuantityNormalizedTest {


	@Test
	public void testEquals() {
		BaseResourceIndexedSearchParamQuantity val1 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		BaseResourceIndexedSearchParamQuantity val2 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertNotNull(val1).isEqualTo(val1);
		assertEquals(val1, val2);
		assertThat("").isNotEqualTo(val1);
	}


}
