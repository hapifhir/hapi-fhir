package ca.uhn.fhir.sl.cache.guava;

import static org.junit.jupiter.api.Assertions.assertNull;

import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.sl.cache.LoadingCache;
import org.junit.jupiter.api.Test;

public class CacheLoaderTest {
	@Test
	void loaderReturnsNullTest() {
		LoadingCache<String, String> cache = CacheFactory.build(1000, key -> {
			return null;
		});
		assertNull(cache.get("1"));
	}
}
