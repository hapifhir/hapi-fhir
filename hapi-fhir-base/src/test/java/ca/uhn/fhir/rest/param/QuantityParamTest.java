package ca.uhn.fhir.rest.param;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class QuantityParamTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		gt1.0|1000-0|http://loinc , gt1.0|1000-0|http://loinc , true
		gt1.1|1000-0|http://loinc , gt1.0|1000-0|http://loinc , false
		lt1.0|1000-0|http://loinc , gt1.0|1000-0|http://loinc , false
		gt1.0|1000-1|http://loinc , gt1.0|1000-0|http://loinc , false
		gt1.0|1000-0|http://foo12 , gt1.0|1000-0|http://loinc , false
		1.0|1000-0|http://loinc   , gt1.0|1000-0|http://loinc , false
		gt1.0||http://loinc       , gt1.0|1000-0|http://loinc , false
		gt1.0|1000-0|             , gt1.0|1000-0|http://loinc , false
		""")
	void testEqualsAndHashCode(String theToken0, String theToken1, boolean theExpectMatch) {

		QuantityParam p0 = new QuantityParam();
		p0.setValueAsQueryToken(null, null, null, theToken0);

		QuantityParam p1 = new QuantityParam();
		p1.setValueAsQueryToken(null, null, null, theToken1);

		if (theExpectMatch) {
			assertEquals(p0, p1);
			assertEquals(p0.hashCode(), p1.hashCode());
		} else {
			assertNotEquals(p0, p1);
			assertNotEquals(p0.hashCode(), p1.hashCode());
		}
	}

}
