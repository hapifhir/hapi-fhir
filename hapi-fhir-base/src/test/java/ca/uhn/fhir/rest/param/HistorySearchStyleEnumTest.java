package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HistorySearchStyleEnumTest {
	@Test
	public void testParse(){
		assertThat(HistorySearchStyleEnum.parse("")).isNull();
		assertThat(HistorySearchStyleEnum.parse(null)).isNull();
		assertThat(HistorySearchStyleEnum.parse("Anything")).isNull();
		assertThat(HistorySearchStyleEnum.parse("_at")).isEqualTo(HistorySearchStyleEnum.AT);
		assertThat(HistorySearchStyleEnum.parse("_since")).isEqualTo(HistorySearchStyleEnum.SINCE);
		assertThat(HistorySearchStyleEnum.parse("_count")).isEqualTo(HistorySearchStyleEnum.COUNT);
	}

	@Test
	public void testIsAt(){
		assertThat(HistorySearchStyleEnum.AT.isAt()).isTrue();
		assertThat(HistorySearchStyleEnum.SINCE.isAt()).isFalse();
	}
}
