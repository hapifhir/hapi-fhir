package ca.uhn.fhir.jpa.model.entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ResourceIndexedSearchParamQuantityTest {

	private ResourceIndexedSearchParamQuantity createParam(String theParamName, String theValue, String theSystem, String theUnits) {
		ResourceIndexedSearchParamQuantity token = new ResourceIndexedSearchParamQuantity(theParamName, new BigDecimal(theValue), theSystem, theUnits);
		token.setResource(new ResourceTable().setResourceType("Patient"));
		return token;
	}

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamQuantity token = createParam("NAME", "123.001", "value", "VALUE");

		// Make sure our hashing function gives consistent results
		assertEquals(834432764963581074L, token.getHashIdentity().longValue());
		assertEquals(-1970227166134682431L, token.getHashIdentityAndUnits().longValue());
	}


}
