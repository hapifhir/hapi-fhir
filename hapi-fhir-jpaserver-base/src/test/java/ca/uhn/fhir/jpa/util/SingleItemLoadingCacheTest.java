package ca.uhn.fhir.jpa.util;

import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleItemLoadingCacheTest {

	@Mock
	private Callable<CapabilityStatement> myFetcher;

	@After
	public void after() {
		SingleItemLoadingCache.setNowForUnitTest(null);
	}

	@Before
	public void before() throws Exception {
		AtomicInteger id = new AtomicInteger();
		when(myFetcher.call()).thenAnswer(t->{
			CapabilityStatement retVal = new CapabilityStatement();
			retVal.setId("" + id.incrementAndGet());
			return retVal;
		});
	}

	@Test
	public void testCache() {
		long start = System.currentTimeMillis();
		SingleItemLoadingCache.setNowForUnitTest(start);

		// Cache is initialized on startup
		SingleItemLoadingCache<CapabilityStatement> cache = new SingleItemLoadingCache<>(myFetcher);
		cache.setCacheMillis(500);
		assertEquals(null, cache.get());

		// Not time to update yet
		cache.update();
		assertEquals("1", cache.get().getId());

		// Wait a bit, still not time to update
		SingleItemLoadingCache.setNowForUnitTest(start + 400);
		cache.update();
		assertEquals("1", cache.get().getId());

		// Wait a bit more and the cache is expired
		SingleItemLoadingCache.setNowForUnitTest(start + 800);
		cache.update();
		assertEquals("2", cache.get().getId());

	}

}
