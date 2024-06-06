package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		assertNotNull(val1);
		assertEquals(val1, val2);
		assertThat("").isNotEqualTo(val1);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		BaseResourceIndexedSearchParamQuantity param = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), "Patient", "param", 123.0, "http://unitsofmeasure.org", "kg");
		BaseResourceIndexedSearchParamQuantity param2 = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), "Patient", "param", 123.0, "http://unitsofmeasure.org", "kg");

		param2.optimizeIndexStorage();

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}

}
