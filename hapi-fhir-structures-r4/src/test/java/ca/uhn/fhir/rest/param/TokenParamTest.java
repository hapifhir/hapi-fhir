package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TokenParamTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testEquals() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		TokenParam tokenParam2 = new TokenParam("foo", "bar");
		TokenParam tokenParam3 = new TokenParam("foo", "baz");
		assertEquals(tokenParam1, tokenParam1);
		assertEquals(tokenParam1, tokenParam2);
		assertNotEquals(tokenParam1, tokenParam3);
		assertNotEquals(tokenParam1, null);
		assertNotEquals(tokenParam1, "");
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
	public void testNameNickname() {
		StringParam param = new StringParam();
		assertFalse(param.isNicknameExpand());
		param.setValueAsQueryToken(ourCtx, "name", Constants.PARAMQUALIFIER_NICKNAME, "kenny");
		assertTrue(param.isNicknameExpand());
	}

	@Test
	public void testGivenNickname() {
		StringParam param = new StringParam();
		assertFalse(param.isNicknameExpand());
		param.setValueAsQueryToken(ourCtx, "given", Constants.PARAMQUALIFIER_NICKNAME, "kenny");
		assertTrue(param.isNicknameExpand());
	}

	@Test
	public void testInvalidNickname() {
		StringParam param = new StringParam();
		assertFalse(param.isNicknameExpand());
		try {
			param.setValueAsQueryToken(ourCtx, "family", Constants.PARAMQUALIFIER_NICKNAME, "kenny");
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2077: Modifier :nickname may only be used with 'name' and 'given' search parameters", e.getMessage());
		}
	}

}
