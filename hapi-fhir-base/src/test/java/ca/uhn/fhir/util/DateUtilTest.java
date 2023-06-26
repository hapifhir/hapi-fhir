package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

	@Test
	public void testCompletedDate() {
		
		Pair<String, String> result = DateUtils.getCompletedDate(null);		
		assertNull(result.getLeft());		
		assertNull(result.getRight());	
		
		result = DateUtils.getCompletedDate("2020");
		assertEquals("2020-01-01", result.getLeft());
		assertEquals("2020-12-31", result.getRight());
		
		result = DateUtils.getCompletedDate("202001a");
		assertEquals("202001a", result.getLeft());
		assertEquals("202001a", result.getRight());
		
		result = DateUtils.getCompletedDate("202001");
		assertEquals("202001", result.getLeft());
		assertEquals("202001", result.getRight());
		
		result = DateUtils.getCompletedDate("2020-01");
		assertEquals("2020-01-01", result.getLeft());
		assertEquals("2020-01-31", result.getRight());
		
		result = DateUtils.getCompletedDate("2020-02");
		assertEquals("2020-02-01", result.getLeft());
		assertEquals("2020-02-29", result.getRight());
		
		result = DateUtils.getCompletedDate("2021-02");
		assertEquals("2021-02-01", result.getLeft());
		assertEquals("2021-02-28", result.getRight());
		
		result = DateUtils.getCompletedDate("2020-04");
		assertEquals("2020-04-01", result.getLeft());
		assertEquals("2020-04-30", result.getRight());
		
		result = DateUtils.getCompletedDate("2020-05-16");
		assertEquals("2020-05-16", result.getLeft());
		assertEquals("2020-05-16", result.getRight());
	}
}
