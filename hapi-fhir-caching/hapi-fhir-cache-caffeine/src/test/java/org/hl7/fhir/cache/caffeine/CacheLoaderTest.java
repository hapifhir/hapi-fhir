package org.hl7.fhir.cache.caffeine;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.hl7.fhir.cache.CacheFactory;
import org.hl7.fhir.cache.LoadingCache;
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
