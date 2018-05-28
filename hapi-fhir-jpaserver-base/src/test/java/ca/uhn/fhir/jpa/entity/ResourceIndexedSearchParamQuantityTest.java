package ca.uhn.fhir.jpa.entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

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
		assertEquals(945335027461836896L, token.getHashUnitsAndValPrefix().longValue());
		assertEquals(5549105497508660145L, token.getHashValPrefix().longValue());
	}

	@Test
	public void testValueTrimming() {
		assertEquals(7265149425397186226L, createParam("NAME", "401.001", "value", "VALUE").getHashUnitsAndValPrefix().longValue());
		assertEquals(7265149425397186226L, createParam("NAME", "401.99999", "value", "VALUE").getHashUnitsAndValPrefix().longValue());
		assertEquals(7265149425397186226L, createParam("NAME", "401", "value", "VALUE").getHashUnitsAndValPrefix().longValue());
		// Should be different
		assertEquals(-8387917096585386046L, createParam("NAME", "400.9999999", "value", "VALUE").getHashUnitsAndValPrefix().longValue());
		// Should be different
		assertEquals(8819656626732693650L, createParam("NAME", "402.000000", "value", "VALUE").getHashUnitsAndValPrefix().longValue());
	}


}
