package ca.uhn.fhir.rest.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SortSpecTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		name , ASC , active  , name , ASC , active , true
		
		name ,     , active  , name , ASC , active , false
		name , ASC , active  , name ,     , active , false
		name , ASC ,         , name , ASC , active , false
		name , ASC , active  , name , ASC ,        , false
		""")
	void testEqualsAndHashCode(String theSpec0, SortOrderEnum theSpec0Order, String theSpec0Chain, String theSpec1, SortOrderEnum theSpec1Order, String theSpec1Chain, boolean theExpectMatch) {

		SortSpec p0 = new SortSpec(theSpec0);
		p0.setOrder(theSpec0Order);
		if (isNotBlank(theSpec0Chain)) {
			p0.setChain(new SortSpec(theSpec0Chain));
		}

		SortSpec p1 = new SortSpec(theSpec1);
		p1.setOrder(theSpec1Order);
		if (isNotBlank(theSpec1Chain)) {
			p1.setChain(new SortSpec(theSpec1Chain));
		}

		if (theExpectMatch) {
			assertEquals(p0, p1);
			assertEquals(p0.hashCode(), p1.hashCode());
			assertEquals(p0.toString(), p1.toString());
		} else {
			assertNotEquals(p0, p1);
			assertNotEquals(p0.hashCode(), p1.hashCode());
			assertNotEquals(p0.toString(), p1.toString());
		}
	}

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
