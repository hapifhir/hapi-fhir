package ca.uhn.fhir.jpa.model.entity;

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
		assertThat(val1).isNotNull().isEqualTo(val1);
		assertThat(val2).isEqualTo(val1);
		assertThat("").isNotEqualTo(val1);
	}


}
