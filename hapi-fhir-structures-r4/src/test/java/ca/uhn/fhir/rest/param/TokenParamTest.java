package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenParamTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testEquals() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		TokenParam tokenParam2 = new TokenParam("foo", "bar");
		TokenParam tokenParam3 = new TokenParam("foo", "baz");
		assertThat(tokenParam1).isNotNull().isEqualTo(tokenParam1);
		assertThat(tokenParam2).isEqualTo(tokenParam1);
		assertThat(tokenParam3).isNotEqualTo(tokenParam1);
		assertThat("").isNotEqualTo(tokenParam1);
	}

	@Test
	public void testHashCode() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		assertThat(tokenParam1.hashCode()).isEqualTo(4716638);
	}


	@Test
	public void testIsEmpty() {
		assertFalse(new TokenParam("foo", "bar").isEmpty());
		assertTrue(new TokenParam("", "").isEmpty());
		assertTrue(new TokenParam().isEmpty());
		assertThat(new TokenParam().getValueNotNull()).isEqualTo("");
	}

	@Test
	public void testOfType() {
		TokenParam param = new TokenParam();
		param.setValueAsQueryToken(ourCtx, "identifier", Constants.PARAMQUALIFIER_TOKEN_OF_TYPE, "http://type-system|type-value|identifier-value");
		assertThat(param.getModifier()).isEqualTo(TokenParamModifier.OF_TYPE);
		assertThat(param.getSystem()).isEqualTo("http://type-system");
		assertThat(param.getValue()).isEqualTo("type-value|identifier-value");
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

}
