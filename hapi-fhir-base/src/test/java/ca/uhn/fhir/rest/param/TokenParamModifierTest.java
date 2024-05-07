package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

class TokenParamModifierTest {

	@ParameterizedTest
	@EnumSource()
	void negativeModifiers(TokenParamModifier theTokenParamModifier) {
		EnumSet<TokenParamModifier> negativeSet = EnumSet.of(
			TokenParamModifier.NOT,
			TokenParamModifier.NOT_IN
		);

		assertEquals(negativeSet.contains(theTokenParamModifier), theTokenParamModifier.isNegative());
	}

}
