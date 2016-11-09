package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.util.TestUtil;

import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokenOrListParamTest {
	@Test
	public void testWhenParamListHasAnyMatchingCodingsForCodingList_doesCodingListMatch_shouldBeTrue() {
		TokenOrListParam params = new TokenOrListParam();
		params.add("http://foo.org", "53");
		params.add("http://bar.org", "52");

		List<CodingDt> codings = new ArrayList<CodingDt>();
		codings.add(new CodingDt("http://baz.org", "53"));
		codings.add(new CodingDt("http://bar.org", "52"));

		assertTrue(params.doesCodingListMatch(codings));
	}

	@Test
	public void testWhenParamListHasNoMatchingCodingsForCodingList_doesCodingListMatch_shouldBeFalse() {
		TokenOrListParam params = new TokenOrListParam();
		params.add("http://foo.org", "53");
		params.add("http://bar.org", "52");

		List<CodingDt> codings = new ArrayList<CodingDt>();
		codings.add(new CodingDt("http://baz.org", "53"));
		codings.add(new CodingDt("http://bar.org", "11"));

		assertFalse(params.doesCodingListMatch(codings));
	}

	@Test
	public void testWhenParamListHasNoMatchingCodingsForCodingList_doesCodingListMatch_shouldBeFalse2() {
		TokenOrListParam params = new TokenOrListParam();
		params.add("http://foo.org", "53");
		params.add("http://bar.org", "52");

		List<CodingDt> codings = new ArrayList<CodingDt>();
		codings.add(new CodingDt("http://baz.org", "53"));
		codings.add(new CodingDt("http://bar.org", "11"));

		assertFalse(params.doesCodingListMatch(codings));
	}

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
	

	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
