package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistorySearchStyleEnumTest {
	@Test
	public void testParse(){
		assertNull(HistorySearchStyleEnum.parse(""));
		assertNull(HistorySearchStyleEnum.parse(null));
		assertNull(HistorySearchStyleEnum.parse("Anything"));
		assertEquals(HistorySearchStyleEnum.AT, HistorySearchStyleEnum.parse("_at"));
		assertEquals(HistorySearchStyleEnum.SINCE, HistorySearchStyleEnum.parse("_since"));
		assertEquals(HistorySearchStyleEnum.COUNT, HistorySearchStyleEnum.parse("_count"));
	}

	@Test
	public void testIsAt(){
		assertTrue(HistorySearchStyleEnum.AT.isAt());
		assertFalse(HistorySearchStyleEnum.SINCE.isAt());
	}
}
