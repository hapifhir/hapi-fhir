package ca.uhn.fhir.rest.param;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class HasParamTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		:1:2:3 , ABC , :1:2:3  , ABC , true
		:1:1:3 , ABC , :1:2:3  , ABC , false
		:1:2:1 , ABC , :1:2:3  , ABC , false
		:1:2:3 , ZZZ , :1:2:3  , ABC , false
		::2:3  , ABC , :1:2:3  , ABC , false
		:1::3  , ABC , :1:2:3  , ABC , false
		:1:2:  , ABC , :1:2:3  , ABC , false
		""")
	void testEqualsAndHashCode(String theToken0Qualifier, String theToken0Value, String theToken1Qualifier, String theToken1Value, boolean theExpectMatch) {

		HasParam p0 = new HasParam();
		p0.setValueAsQueryToken(null, null, theToken0Qualifier, theToken0Value);

		HasParam p1 = new HasParam();
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
