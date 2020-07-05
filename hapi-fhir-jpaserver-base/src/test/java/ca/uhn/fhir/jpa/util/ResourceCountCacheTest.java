package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceCountCacheTest {

	@Mock
	private Callable<Map<String, Long>> myFetcher;

	@AfterEach
	public void after() {
		ResourceCountCache.setNowForUnitTest(null);
	}

	@Test
	public void testCache() throws Exception {
		AtomicLong id = new AtomicLong();
		when(myFetcher.call()).thenAnswer(t->{
			Map<String, Long> retVal = new HashMap<>();
			retVal.put("A", id.incrementAndGet());
			return retVal;
		});

		long start = System.currentTimeMillis();
		ResourceCountCache.setNowForUnitTest(start);

		// Cache is initialized on startup
		ResourceCountCache cache = new ResourceCountCache(myFetcher);
		cache.setCacheMillis(500);
		assertEquals(null, cache.get());

		// Not time to update yet
		cache.update();
		assertEquals(Long.valueOf(1), cache.get().get("A"));

		// Wait a bit, still not time to update
		ResourceCountCache.setNowForUnitTest(start + 400);
		cache.update();
		assertEquals(Long.valueOf(1), cache.get().get("A"));

		// Wait a bit more and the cache is expired
		ResourceCountCache.setNowForUnitTest(start + 800);
		cache.update();
		assertEquals(Long.valueOf(2), cache.get().get("A"));

	}

	@Test
	public void testCacheWithLoadingDisabled() {
		long start = System.currentTimeMillis();
		ResourceCountCache.setNowForUnitTest(start);

		// Cache of 0 means "never load"
		ResourceCountCache cache = new ResourceCountCache(myFetcher);
		cache.setCacheMillis(0);

		/*
		 * No matter how long we wait it should never load...
		 */

		assertEquals(null, cache.get());

		cache.update();
		assertEquals(null, cache.get());

		ResourceCountCache.setNowForUnitTest(start + 400);
		cache.update();
		assertEquals(null, cache.get());

		ResourceCountCache.setNowForUnitTest(start + 80000);
		cache.update();
		assertEquals(null, cache.get());

	}

}
