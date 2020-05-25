package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityTest {

	private ResourceIndexedSearchParamQuantity createParam(String theParamName, String theValue, String theSystem, String theUnits) {
		ResourceIndexedSearchParamQuantity token = new ResourceIndexedSearchParamQuantity(new PartitionSettings(), "Patient", theParamName, new BigDecimal(theValue), theSystem, theUnits);
		token.setResource(new ResourceTable().setResourceType("Patient"));
		return token;
	}

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamQuantity token = createParam("NAME", "123.001", "value", "VALUE");
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(834432764963581074L, token.getHashIdentity().longValue());
		assertEquals(-1970227166134682431L, token.getHashIdentityAndUnits().longValue());
	}


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamQuantity val1 = new ResourceIndexedSearchParamQuantity()
			.setValue(new BigDecimal(123));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamQuantity val2 = new ResourceIndexedSearchParamQuantity()
			.setValue(new BigDecimal(123));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}


}
