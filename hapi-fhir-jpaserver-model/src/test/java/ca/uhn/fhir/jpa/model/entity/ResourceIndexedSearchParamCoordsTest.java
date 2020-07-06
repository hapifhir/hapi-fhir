package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}
}
