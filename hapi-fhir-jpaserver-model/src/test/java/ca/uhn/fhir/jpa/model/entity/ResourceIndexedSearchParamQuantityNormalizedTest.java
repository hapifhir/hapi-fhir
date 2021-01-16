package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityNormalizedTest {


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamBaseQuantity val1 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamBaseQuantity val2 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}


}
