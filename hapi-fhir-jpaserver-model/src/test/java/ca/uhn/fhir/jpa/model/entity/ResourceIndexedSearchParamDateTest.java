package ca.uhn.fhir.jpa.model.entity;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class ResourceIndexedSearchParamDateTest {

	private Date date1A, date1B, date2A, date2B;
	private Timestamp timestamp1A, timestamp1B, timestamp2A, timestamp2B;

	@Before
	public void setUp() throws Exception {
		Calendar cal1 = Calendar.getInstance();
		cal1.set(1970, 01, 01, 10, 23, 33);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(1990, 01, 01, 5, 11, 0);

		date1A = cal1.getTime();
		date1B = cal1.getTime();
		date2A = cal2.getTime();
		date2B = cal2.getTime();

		timestamp1A = new Timestamp(date1A.getTime());
		timestamp1B = new Timestamp(date1B.getTime());
		timestamp2A = new Timestamp(date2A.getTime());
		timestamp2B = new Timestamp(date2B.getTime());
	}

	@Test
	public void equalsIsTrueForMatchingNullDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", null, null, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", null, null, "SomeValue");

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsTrueForMatchingDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1A, date2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1B, date2B, "SomeValue");

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsTrueForMatchingTimeStampsThatMatch() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp1A, timestamp2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp1B, timestamp2B, "SomeValue");

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}

	// Scenario that occurs when updating a resource with a date search parameter. One date will be a java.util.Date, the
	// other will be equivalent but will be a java.sql.Timestamp. Equals should work in both directions.
	@Test
	public void equalsIsTrueForMixedTimestampsAndDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1A, date2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp1A, timestamp2A, "SomeValue");

		assertTrue(param.equals(param2));
		assertTrue(param2.equals(param));
		assertEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingDates() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1A, date2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date2A, date1A, "SomeValue");

		assertFalse(param.equals(param2));
		assertFalse(param2.equals(param));
		assertNotEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingDatesNullCase() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1A, date2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", null, null, "SomeValue");

		assertFalse(param.equals(param2));
		assertFalse(param2.equals(param));
		assertNotEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsFalseForNonMatchingTimeStamps() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp1A, timestamp2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp2A, timestamp1A, "SomeValue");

		assertFalse(param.equals(param2));
		assertFalse(param2.equals(param));
		assertNotEquals(param.hashCode(), param2.hashCode());
	}

	@Test
	public void equalsIsFalseForMixedTimestampsAndDatesThatDoNotMatch() {
		ResourceIndexedSearchParamDate param = new ResourceIndexedSearchParamDate("Patient", "SomeResource", date1A, date2A, "SomeValue");
		ResourceIndexedSearchParamDate param2 = new ResourceIndexedSearchParamDate("Patient", "SomeResource", timestamp2A, timestamp1A, "SomeValue");

		assertFalse(param.equals(param2));
		assertFalse(param2.equals(param));
		assertNotEquals(param.hashCode(), param2.hashCode());
	}


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamDate val1 = new ResourceIndexedSearchParamDate()
			.setValueHigh(new Date(100000000L))
			.setValueLow(new Date(111111111L));
		val1.calculateHashes();
		ResourceIndexedSearchParamDate val2 = new ResourceIndexedSearchParamDate()
			.setValueHigh(new Date(100000000L))
			.setValueLow(new Date(111111111L));
		val2.calculateHashes();
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}
}
