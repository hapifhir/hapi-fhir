package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceIndexedSearchParamCoordsTest {

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamCoords val1 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamCoords val2 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertNotNull(val1);
		assertEquals(val1, val2);
		assertThat("").isNotEqualTo(val1);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		ResourceIndexedSearchParamCoords param = new ResourceIndexedSearchParamCoords(new PartitionSettings(), "Patient", "param", 100, 10);
		ResourceIndexedSearchParamCoords param2 = new ResourceIndexedSearchParamCoords(new PartitionSettings(), "Patient", "param", 100, 10);

		param2.optimizeIndexStorage();

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}
}
