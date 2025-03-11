package ca.uhn.fhir.sl.cache.caffeine;

import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.sl.cache.LoadingCache;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CacheLoaderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(CacheLoaderTest.class);

	@Order(0)
	@Test
	public void testConcurrentInitialization() throws ExecutionException, InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		try {
			List<Future<Cache<?, ?>>> futures = new ArrayList<>(5);
			for (int i = 0; i < 5; i++) {
				Future<Cache<?, ?>> future = executor.submit(() -> CacheFactory.build(1000));
				futures.add(future);
			}

			for (var next : futures) {
				Cache<?, ?> actual = next.get(1, TimeUnit.MINUTES);
				ourLog.info("Got cache: {}", actual);
				assertNotNull(actual);
			}
		} finally {
			executor.shutdown();
		}
	}

	@Order(1)
	@Test
	void loaderReturnsNullTest() {
		LoadingCache<String, String> cache = CacheFactory.build(1000, key -> {
			return null;
		});
		assertNull(cache.get("1"));
	}
}
