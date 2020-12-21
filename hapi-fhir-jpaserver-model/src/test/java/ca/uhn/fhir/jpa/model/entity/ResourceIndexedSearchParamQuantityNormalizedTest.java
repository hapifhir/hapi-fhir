package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.util.UcumServiceUtil;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityNormalizedTest {

	private ResourceIndexedSearchParamQuantityNormalized createParam(String theParamName, String theValue, String theSystem, String theUnits) {
		ResourceIndexedSearchParamQuantityNormalized token = new ResourceIndexedSearchParamQuantityNormalized(new PartitionSettings(), "Observation", theParamName, new BigDecimal(theValue), theSystem, theUnits);
		token.setResource(new ResourceTable().setResourceType("Patient"));
		return token;
	}

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamQuantityNormalized token = createParam("Quanity", "123.001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm");
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(5219730978980909111L, token.getHashIdentity().longValue());
		assertEquals(-2454931617586657338L, token.getHashIdentityAndUnits().longValue());
		assertEquals(878263047209296227L, token.getHashIdentitySystemAndUnits().longValue());		
	}


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

	@Test
	public void testUcum() {
		
		//-- system is ucum
		ResourceIndexedSearchParamQuantityNormalized token1 = createParam("Quanity", "123.001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm");
		token1.calculateHashes();
		
		assertEquals("m", token1.getUnits());
		assertEquals(Double.parseDouble("1.23001"),  token1.getValue());
		
		//-- small number
		token1 = createParam("Quanity", "0.000001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "mm");
		token1.calculateHashes();
		
		assertEquals("m", token1.getUnits());
		assertEquals(Double.parseDouble("0.000000001"),  token1.getValue());

		
		// -- non ucum system
		ResourceIndexedSearchParamQuantityNormalized token2 = createParam("Quanity", "123.001", "http://abc.org", "cm");
		token2.calculateHashes();
		
		assertEquals("cm", token2.getUnits());
		assertEquals(Double.parseDouble("123.001"),  token2.getValue());
		
		// -- unsupported ucum code
		ResourceIndexedSearchParamQuantityNormalized token3 = createParam("Quanity", "123.001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "unknown");
		token3.calculateHashes();
		
		assertEquals("unknown", token3.getUnits());
		assertEquals(Double.parseDouble("123.001"),  token3.getValue());
	}

}
