package ca.uhn.fhir.jpa.model.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceIndexedSearchParamCoordsTest {

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamCoords val1 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val1.calculateHashes();
		ResourceIndexedSearchParamCoords val2 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}
}
