package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenOrListParamDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();

	/**
	 * See #192
	 */
	@Test
	public void testParseExcaped() {
		TokenOrListParam params = new TokenOrListParam();
		params.setValuesAsQueryTokens(ourCtx, null, QualifiedParamList.singleton("system|code-include-but-not-end-with-comma\\,suffix"));

		assertEquals(1, params.getListAsCodings().size());
		assertEquals("system", params.getListAsCodings().get(0).getSystemElement().getValue());
		assertEquals("code-include-but-not-end-with-comma,suffix", params.getListAsCodings().get(0).getCodeElement().getValue());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
