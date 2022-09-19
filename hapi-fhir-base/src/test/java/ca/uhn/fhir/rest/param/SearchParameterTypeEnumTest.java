package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchParameterTypeEnumTest {
	@Test
	public void testParse(){
		assertNull(SearchParameterTypeEnum.parse(""));
		assertNull(SearchParameterTypeEnum.parse(null));
		assertNull(SearchParameterTypeEnum.parse("Anything"));
		assertEquals(SearchParameterTypeEnum.AT, SearchParameterTypeEnum.parse("_at"));
		assertEquals(SearchParameterTypeEnum.SINCE, SearchParameterTypeEnum.parse("_since"));
		assertEquals(SearchParameterTypeEnum.COUNT, SearchParameterTypeEnum.parse("_count"));
	}

	@Test
	public void testIsAt(){
		assertTrue(SearchParameterTypeEnum.AT.isAt());
		assertFalse(SearchParameterTypeEnum.SINCE.isAt());
	}
}
