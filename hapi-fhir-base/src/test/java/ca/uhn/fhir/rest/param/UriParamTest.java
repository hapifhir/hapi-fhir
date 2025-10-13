package ca.uhn.fhir.rest.param;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UriParamTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		            , http://foo ,             , http://foo , true
		:below      , http://foo , :below      , http://foo , true
		            , http://foo ,             , http://123 , false
		:below      , http://foo ,             , http://foo , false
		:missing    , true       , :missing    , true       , true
		:missing    , true       , :missing    , false      , false
		""")
	void testEqualsAndHashCode(String theToken0Qualifier, String theToken0Value, String theToken1Qualifier, String theToken1Value, boolean theExpectMatch) {

		UriParam p0 = new UriParam();
		p0.setValueAsQueryToken(null, null, theToken0Qualifier, theToken0Value);

		UriParam p1 = new UriParam();
		p1.setValueAsQueryToken(null, null, theToken1Qualifier, theToken1Value);

		if (theExpectMatch) {
			assertEquals(p0, p1);
			assertEquals(p0.hashCode(), p1.hashCode());
		} else {
			assertNotEquals(p0, p1);
			assertNotEquals(p0.hashCode(), p1.hashCode());
		}
	}

}
