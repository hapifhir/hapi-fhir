package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CacheControlDirectiveTest {

	@Test
	public void testParseNoCache() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_CACHE);
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertTrue(ccd.isNoCache());
		assertFalse(ccd.isNoStore());
	}

	@Test
	public void testParseNoCacheNoStore() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_CACHE + "  ,   " + Constants.CACHE_CONTROL_NO_STORE);
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertTrue(ccd.isNoCache());
		assertTrue(ccd.isNoStore());
		assertEquals(null, ccd.getMaxResults());
	}

	@Test
	public void testParseNoCacheNoStoreMaxResults() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_STORE + ", "+ Constants.CACHE_CONTROL_MAX_RESULTS + "=5");
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertFalse(ccd.isNoCache());
		assertTrue(ccd.isNoStore());
		assertEquals(5, ccd.getMaxResults().intValue());
	}

	@Test
	public void testParseNoCacheNoStoreMaxResultsInvalid() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_STORE + ", "+ Constants.CACHE_CONTROL_MAX_RESULTS + "=A");
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertFalse(ccd.isNoCache());
		assertTrue(ccd.isNoStore());
		assertEquals(null, ccd.getMaxResults());
	}

	@Test
	public void testParseNull() {
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(null);
		assertFalse(ccd.isNoCache());
		assertFalse(ccd.isNoStore());
	}
}
