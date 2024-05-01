package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HistorySearchStyleEnumTest {
	@Test
	public void testParse(){
		assertNull(HistorySearchStyleEnum.parse(""));
		assertNull(HistorySearchStyleEnum.parse(null));
		assertNull(HistorySearchStyleEnum.parse("Anything"));
		assertThat(HistorySearchStyleEnum.parse("_at")).isEqualTo(HistorySearchStyleEnum.AT);
		assertThat(HistorySearchStyleEnum.parse("_since")).isEqualTo(HistorySearchStyleEnum.SINCE);
		assertThat(HistorySearchStyleEnum.parse("_count")).isEqualTo(HistorySearchStyleEnum.COUNT);
	}

	@Test
	public void testIsAt(){
		assertTrue(HistorySearchStyleEnum.AT.isAt());
		assertFalse(HistorySearchStyleEnum.SINCE.isAt());
	}
}
