package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
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
		assertNull(cache.get());

		// Not time to update yet
		cache.update();
		assertThat(cache.get()).containsEntry("A", Long.valueOf(1));

		// Wait a bit, still not time to update
		ResourceCountCache.setNowForUnitTest(start + 400);
		cache.update();
		assertThat(cache.get()).containsEntry("A", Long.valueOf(1));

		// Wait a bit more and the cache is expired
		ResourceCountCache.setNowForUnitTest(start + 800);
		cache.update();
		assertThat(cache.get()).containsEntry("A", Long.valueOf(2));

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

		assertNull(cache.get());

		cache.update();
		assertNull(cache.get());

		ResourceCountCache.setNowForUnitTest(start + 400);
		cache.update();
		assertNull(cache.get());

		ResourceCountCache.setNowForUnitTest(start + 80000);
		cache.update();
		assertNull(cache.get());

	}

}
