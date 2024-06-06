package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenParamTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testEquals() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		TokenParam tokenParam2 = new TokenParam("foo", "bar");
		TokenParam tokenParam3 = new TokenParam("foo", "baz");
		assertNotNull(tokenParam1);
		assertEquals(tokenParam1, tokenParam2);
		assertThat(tokenParam3).isNotEqualTo(tokenParam1);
		assertThat("").isNotEqualTo(tokenParam1);
	}

	@Test
	public void testHashCode() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		assertEquals(4716638, tokenParam1.hashCode());
	}


	@Test
	public void testIsEmpty() {
		assertFalse(new TokenParam("foo", "bar").isEmpty());
		assertTrue(new TokenParam("", "").isEmpty());
		assertTrue(new TokenParam().isEmpty());
		assertEquals("", new TokenParam().getValueNotNull());
	}

	@Test
	public void testOfType() {
		TokenParam param = new TokenParam();
		param.setValueAsQueryToken(ourCtx, "identifier", Constants.PARAMQUALIFIER_TOKEN_OF_TYPE, "http://type-system|type-value|identifier-value");
		assertEquals(TokenParamModifier.OF_TYPE, param.getModifier());
		assertEquals("http://type-system", param.getSystem());
		assertEquals("type-value|identifier-value", param.getValue());
	}

	@Test
	public void testMdmQualifier() {
		final String value = "Patient/PJANE1";

		TokenParam param = new TokenParam();
		param.setValueAsQueryToken(ourCtx, "_id", Constants.PARAMQUALIFIER_MDM, value);
		assertNull(param.getModifier());
		assertNull(param.getSystem());
		assertTrue(param.isMdmExpand());
		assertEquals(value, param.getValue());
	}


}
