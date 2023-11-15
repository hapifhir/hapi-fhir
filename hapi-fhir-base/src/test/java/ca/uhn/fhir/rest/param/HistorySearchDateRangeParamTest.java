package ca.uhn.fhir.rest.param;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HistorySearchDateRangeParamTest {
	private final int theOffset = 100;
	private final DateRangeParam dateRangeParam = new DateRangeParam();

	@Test
	public void testSearchDateRangeParamWithInvalidSearchType() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("Some key", new String[]{"value"}), dateRangeParam, theOffset);
		assertNull(param.getHistorySearchType());
		assertEquals(theOffset, param.getOffset());
	}

	@Test
	public void testSearchDateRangeParamWithSearchTypeAsAt() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_at", new String[]{"value"}), dateRangeParam, theOffset);
		assertEquals(HistorySearchStyleEnum.AT, param.getHistorySearchType());
		assertEquals(theOffset, param.getOffset());
	}
	@Test
	public void testSearchDateRangeParamWithSearchTypeAsSince() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_since", new String[]{"value"}), dateRangeParam, theOffset);
		assertEquals(HistorySearchStyleEnum.SINCE, param.getHistorySearchType());
		assertEquals(theOffset, param.getOffset());
	}


	@Test
	public void testSearchDateRangeParamWithSearchTypeAsCount() {
		HistorySearchDateRangeParam param = new HistorySearchDateRangeParam(Map.of("_count", new String[]{"value"}), dateRangeParam, theOffset);
		assertEquals(HistorySearchStyleEnum.COUNT, param.getHistorySearchType());
		assertEquals(theOffset, param.getOffset());

	}
}
