package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheControlDirectiveTest {

	@Test
	public void testParseNoCache() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_CACHE);
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertThat(ccd.isNoCache()).isTrue();
		assertThat(ccd.isNoStore()).isFalse();
	}

	@Test
	public void testParseNoCacheNoStore() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_CACHE + "  ,   " + Constants.CACHE_CONTROL_NO_STORE);
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertThat(ccd.isNoCache()).isTrue();
		assertThat(ccd.isNoStore()).isTrue();
		assertThat(ccd.getMaxResults()).isEqualTo(null);
	}

	@Test
	public void testParseNoCacheNoStoreMaxResults() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_STORE + ", "+ Constants.CACHE_CONTROL_MAX_RESULTS + "=5");
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertThat(ccd.isNoCache()).isFalse();
		assertThat(ccd.isNoStore()).isTrue();
		assertThat(ccd.getMaxResults().intValue()).isEqualTo(5);
	}

	@Test
	public void testParseNoCacheNoStoreMaxResultsInvalid() {
		List<String> values = Arrays.asList(Constants.CACHE_CONTROL_NO_STORE + ", "+ Constants.CACHE_CONTROL_MAX_RESULTS + "=A");
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(values);
		assertThat(ccd.isNoCache()).isFalse();
		assertThat(ccd.isNoStore()).isTrue();
		assertThat(ccd.getMaxResults()).isEqualTo(null);
	}

	@Test
	public void testParseNull() {
		CacheControlDirective ccd = new CacheControlDirective();
		ccd.parse(null);
		assertThat(ccd.isNoCache()).isFalse();
		assertThat(ccd.isNoStore()).isFalse();
	}
}
