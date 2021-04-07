package ca.uhn.fhir.cql.common.helper;

import ca.uhn.fhir.parser.DataFormatException;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class DateHelperTest {

	@Test
	public void testDateHelperProperlyResolvesValidDate() {
		DateHelper dateHelper = new DateHelper();
		Date result = DateHelper.resolveRequestDate("param", "2001-01-29");
		assertNotNull("result should not be NULL!", result);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(result.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals("Got the wrong parsed Date value from initial date String", "2001-01-29", formatter.format(cal.getTime()));
	}

	@Test
	public void testDateHelperProperlyResolvesValidDateWithYearOnly() {
		DateHelper dateHelper = new DateHelper();
		Date result = DateHelper.resolveRequestDate("param", "2001");
		assertNotNull("result should not be NULL!", result);
	}

	@Test
	public void testDateHelperProperlyDetectsBlankDate() {
		DateHelper dateHelper = new DateHelper();
		Date result = null;
		try {
			result = DateHelper.resolveRequestDate("param", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull("result should be NULL!", result);
		}
	}

	@Test
	public void testDateHelperProperlyDetectsNonNumericDateCharacters() {
		DateHelper dateHelper = new DateHelper();
		Date result = null;
		try {
			result = DateHelper.resolveRequestDate("param", "aaa-bbb-ccc");
			fail();
		} catch (DataFormatException e) {
			assertNull("result should be NULL!", result);
		}
	}
}
