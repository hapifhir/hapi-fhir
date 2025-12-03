package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceParamTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		            , Patient/123 ,             , Patient/123 , true
		.identifier , Patient/123 , .identifier , Patient/123 , true
		:missing    , true        , :missing    , true        , true
		:missing    , false       , :missing    , true        , false
		.blah       , Patient/123 , .identifier , Patient/123 , false
		:mdm        , Patient/123 ,             , Patient/123 , false
		:mdm        , Patient/123 , :mdm        , Patient/123 , true
		""")
	void testEqualsAndHashCode(String theToken0Qualifier, String theToken0Value, String theToken1Qualifier, String theToken1Value, boolean theExpectMatch) {

		ReferenceParam p0 = new ReferenceParam();
		p0.setValueAsQueryToken(null, null, theToken0Qualifier, theToken0Value);

		ReferenceParam p1 = new ReferenceParam();
		p1.setValueAsQueryToken(null, null, theToken1Qualifier, theToken1Value);

		if (theExpectMatch) {
			assertEquals(p0, p1);
			assertEquals(p0.hashCode(), p1.hashCode());
		} else {
			assertNotEquals(p0, p1);
			assertNotEquals(p0.hashCode(), p1.hashCode());
		}
	}

	@Test
	void testChainedParamWithMdmExpand() {
		ReferenceParam param = new ReferenceParam();
		param.setValueAsQueryToken(null, "patient", ".name:mdm", "Smith");

		assertThat(param.isMdmExpand()).isTrue();
		assertThat(param.getChain()).isEqualTo("name");
		assertThat(param.getValue()).isEqualTo("Smith");
	}
}
