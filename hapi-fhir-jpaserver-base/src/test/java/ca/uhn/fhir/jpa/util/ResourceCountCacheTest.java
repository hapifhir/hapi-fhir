package ca.uhn.fhir.jpa.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceCountCacheTest {

	@Mock
	private Callable<Map<String, Long>> myFetcher;

	@After
	public void after() {
		ResourceCountCache.setNowForUnitTest(null);
	}

	@Before
	public void before() throws Exception {
		AtomicLong id = new AtomicLong();
		when(myFetcher.call()).thenAnswer(t->{
			Map<String, Long> retVal = new HashMap<>();
			retVal.put("A", id.incrementAndGet());
			return retVal;
		});
	}

	@Test
	public void testCache() {
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
