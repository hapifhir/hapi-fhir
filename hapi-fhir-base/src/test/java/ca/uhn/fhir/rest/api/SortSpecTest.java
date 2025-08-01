package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SortSpecTest {

	@Test
	void testSortSpecCreation() {
		SortSpec sortSpec = new SortSpec("name", SortOrderEnum.ASC);
		assertEquals("name", sortSpec.getParamName());
		assertEquals(SortOrderEnum.ASC, sortSpec.getOrder());
	}

	@Test
	void testSortSpecWithChain() {
		SortSpec chain = new SortSpec("date", SortOrderEnum.DESC);
		SortSpec sortSpec = new SortSpec("name", SortOrderEnum.ASC, chain);
		assertEquals("name", sortSpec.getParamName());
		assertEquals(SortOrderEnum.ASC, sortSpec.getOrder());
		assertNotNull(sortSpec.getChain());
		assertEquals("date", sortSpec.getChain().getParamName());
		assertEquals(SortOrderEnum.DESC, sortSpec.getChain().getOrder());
	}

	@Test
	void testParse() {
	    // given
		String value = "date";

		// when
		SortSpec sortSpec = SortSpec.fromR3OrLaterParameterValue(value);

	    // then
	    assertEquals(new SortSpec("date", SortOrderEnum.ASC), sortSpec);
	}

	@Test
	void testParseDesc() {
		// given
		String value = "-date";

		// when
		SortSpec sortSpec = SortSpec.fromR3OrLaterParameterValue(value);

		// then
		assertEquals(new SortSpec("date", SortOrderEnum.DESC), sortSpec);
	}

}
