package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HistorySearchDateRangeParamTest {
	private final int theOffset = 100;
	private final DateRangeParam dateRangeParam = new DateRangeParam();

	@Test
	public void testSearchDateRangeParamWithInvalidSearchType() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("Some key", new String[]{"value"}), dateRangeParam, theOffset);
		assertThat(param.getHistorySearchType()).isNull();
		assertThat(param.getOffset()).isEqualTo(theOffset);
	}

	@Test
	public void testSearchDateRangeParamWithSearchTypeAsAt() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_at", new String[]{"value"}), dateRangeParam, theOffset);
		assertThat(param.getHistorySearchType()).isEqualTo(HistorySearchStyleEnum.AT);
		assertThat(param.getOffset()).isEqualTo(theOffset);
	}
	@Test
	public void testSearchDateRangeParamWithSearchTypeAsSince() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_since", new String[]{"value"}), dateRangeParam, theOffset);
		assertThat(param.getHistorySearchType()).isEqualTo(HistorySearchStyleEnum.SINCE);
		assertThat(param.getOffset()).isEqualTo(theOffset);
	}


	@Test
	public void testSearchDateRangeParamWithSearchTypeAsCount() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_count", new String[]{"value"}), dateRangeParam, theOffset);
		assertThat(param.getHistorySearchType()).isEqualTo(HistorySearchStyleEnum.COUNT);
		assertThat(param.getOffset()).isEqualTo(theOffset);

	}
}
