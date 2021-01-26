package ca.uhn.fhir.cql.common.helper;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateHelperTest {

	@Test
	public void testDateHelperProperlyResolvesValidDate() {
		DateHelper dateHelper = new DateHelper();
		Date result = dateHelper.resolveRequestDate("2001-01-29", false);
		assertNotNull("result should not be NULL!", result);
	}

	@Test
	public void testDateHelperProperlyResolvesValidDateWithYearOnly() {
		DateHelper dateHelper = new DateHelper();
		Date result = dateHelper.resolveRequestDate("2001", false);
		assertNotNull("result should not be NULL!", result);
	}

	@Test
	public void testDateHelperProperlyResolvesValidDateWithYearAndStart() {
		DateHelper dateHelper = new DateHelper();
		Date result = dateHelper.resolveRequestDate("2001", true);
		assertNotNull("result should not be NULL!", result);
	}

	@Test
	public void testDateHelperProperlyDetectsBlankDate() {
		DateHelper dateHelper = new DateHelper();
		Date result = null;
		try {
			result = dateHelper.resolveRequestDate(null, false);
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
			result = dateHelper.resolveRequestDate("aaa-bbb-ccc", false);
			fail();
		} catch (NumberFormatException e) {
			assertNull("result should be NULL!", result);
		}
	}
}
