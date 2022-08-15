package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenOrListParamDstu2Test {
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

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
