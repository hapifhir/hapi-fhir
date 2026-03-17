package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenOrListParamDstu3Test {

	private final FhirContext myCtx = FhirContext.forDstu3Cached();

	@Test
	public void testListConstructor() {
		List<TokenParam> params = List.of(
			 new TokenParam("system0", "code0"),
			 new TokenParam("system1", "code1")
		);

		TokenOrListParam orParams = new TokenOrListParam(params);
		assertEquals(2, orParams.size());
	}

	/**
	 * See #192
	 */
	@Test
	public void testParseEscaped() {
		TokenOrListParam params = new TokenOrListParam();
		params.setValuesAsQueryTokens(myCtx, null, QualifiedParamList.singleton("system|code-include-but-not-end-with-comma\\,suffix"));

		assertThat(params.getListAsCodings()).hasSize(1);
		assertEquals("system", params.getListAsCodings().get(0).getSystemElement().getValue());
		assertEquals("code-include-but-not-end-with-comma,suffix", params.getListAsCodings().get(0).getCodeElement().getValue());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
