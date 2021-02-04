package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.parser.DataFormatException;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateParamTest {

	@Test
	public void testBasicParse() {
		DateParam input = new DateParam("2020-01-01");

		// too bad value is a j.u.Date instead of a new JSR-310 type
		// DataParam parses using default tz, so go backwards.
		ZonedDateTime zonedDateTime = input.getValue().toInstant().atZone(ZoneId.systemDefault());
		assertEquals(2020,zonedDateTime.getYear());
		assertEquals(Month.JANUARY,zonedDateTime.getMonth());
		assertEquals(1,zonedDateTime.getDayOfMonth());
		assertNull(input.getPrefix());
	}

	@Test
	public void testBadDateFormat() {
		try {
			DateParam input = new DateParam("09-30-1960");
			fail();
		} catch (DataFormatException e) {
			// expected
		}
	}

	@Test
	public void testPrefixParse() {
		DateParam input = new DateParam("gt2020-01-01");

		assertEquals(ParamPrefixEnum.GREATERTHAN, input.getPrefix());
	}

	@Test
	public void testLegacyPrefixParse() {
		DateParam input = new DateParam(">2020-01-01");

		assertEquals(ParamPrefixEnum.GREATERTHAN, input.getPrefix());
	}

	@Test
	public void testJunkDateIssue1465() {
		// Issue 1464 - the string "junk" wasn't returning 400 as expected.  Parsed as a prefix instead, and discarded.
		try {
			DateParam input = new DateParam("junk");
			fail();
		} catch (DataFormatException e) {
			// expected
		}
	}
}
