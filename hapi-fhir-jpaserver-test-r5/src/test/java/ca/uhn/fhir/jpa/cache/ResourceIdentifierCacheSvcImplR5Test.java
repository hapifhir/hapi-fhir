package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.util.ThreadPoolUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ResourceIdentifierCacheSvcImplR5Test extends BaseJpaR5Test {

	private RequestPartitionId myDefaultPartition;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myDefaultPartition = RequestPartitionId.defaultPartition(myPartitionSettings);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testAddSystem(boolean theUseCacheForSecondRead) {
		// First read
		long foo = myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem(newSrd(), myDefaultPartition, "http://foo");
		assertEquals(-5303389482335639648L, foo);
		long bar = myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem(newSrd(), myDefaultPartition, "http://bar");
		assertEquals(6706433576001826864L, bar);
		assertNotEquals(foo, bar);

		if (!theUseCacheForSecondRead) {
			myMemoryCacheSvc.invalidateAllCaches();
		}

		// Second Read
		ourLog.info("About to perform pass 2");
		myCaptureQueriesListener.clear();
		long fooPass2 = myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem(newSrd(), myDefaultPartition, "http://foo");
		long barPass2 = myResourceIdentifierCacheSvc.getOrCreateResourceIdentifierSystem(newSrd(), myDefaultPartition, "http://bar");

		assertEquals(foo, fooPass2);
		assertEquals(bar, barPass2);

		if (theUseCacheForSecondRead) {
			assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(0, myCaptureQueriesListener.countCommits());
		} else {
			assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(2, myCaptureQueriesListener.countCommits());
		}
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testGetFhirIdAssociatedWithUniquePatientIdentifier(boolean theUseCacheForSecondRead) {
		// First read
		String foo = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "foo", () -> "FOO");
		assertEquals("FOO", foo);
		String bar = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://bar", "bar", () -> "BAR");
		assertEquals("BAR", bar);

		if (!theUseCacheForSecondRead) {
			myMemoryCacheSvc.invalidateAllCaches();
		}

		// Second Read
		ourLog.info("About to perform pass 2");
		myCaptureQueriesListener.clear();
		String fooPass2 = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "foo", () -> "FOO");
		assertEquals("FOO", fooPass2);
		String barPass2 = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://bar", "bar", () -> "BAR");
		assertEquals("BAR", barPass2);

		if (theUseCacheForSecondRead) {
			assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(0, myCaptureQueriesListener.countCommits());
		} else {
			// 2 for identifier system entity, 2 for patient identifier entity
			assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(4, myCaptureQueriesListener.countCommits());
		}
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
	}


	@Test
	void testGetFhirIdAssociatedWithUniquePatientIdentifier_NoCreate() {
		// Setup - Create one entry FOO
		myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "foo", () -> "FOO");

		// Clear memory cache
		myMemoryCacheSvc.invalidateAllCaches();

		// Entry FOO shoud be returned
		assertEquals("FOO", myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "foo").orElseThrow());

		// Entry BAR should not be present
		assertFalse(myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "bar").isPresent());

		// Entry BAR should still not be present a second time
		assertFalse(myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "bar").isPresent());

		// Now create a BAR entry
		assertEquals("BAR", myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "bar", () -> "BAR"));

		// Entry BAR should show up now
		assertEquals("BAR", myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "bar").orElseThrow());

		// Entry BAR should show up even after cache invalidation
		myMemoryCacheSvc.invalidateAllCaches();
		assertEquals("BAR", myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "bar").orElseThrow());
	}

	@Test
	void testConcurrentlyCreateIdentifiersWithSameSystem() throws ExecutionException, InterruptedException {
		ThreadPoolTaskExecutor threadPool = ThreadPoolUtil.newThreadPool(3, 3, "testConcurrentlyCreateIdentifiersWithSameSystem");
		try {

			List<Future<String>> futures = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				Callable<String> func = () -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", UUID.randomUUID().toString(), () -> UUID.randomUUID().toString());
				Future<String> future = threadPool.submit(func);
				futures.add(future);
			}

			Set<String> fhirIds = new HashSet<>();
			for (Future<String> future : futures) {
				fhirIds.add(future.get());
			}

			assertEquals(10, fhirIds.size());

		} finally {
			threadPool.shutdown();
		}
	}

	/**
	 * If multiple threads try to create the same system+value at the same time, we should
	 * fail with an exception and not create an entry.
	 */
	@Test
	void testConcurrentlyCreateIdentifiersWithSameSystemAndValue() throws InterruptedException {
		ThreadPoolTaskExecutor threadPool = ThreadPoolUtil.newThreadPool(3, 3, "testConcurrentlyCreateIdentifiersWithSameSystem");
		try {

			List<Future<String>> futures = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				Callable<String> func = () -> myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(newSrd(), myDefaultPartition, "http://foo", "1", () -> "FOO");
				Future<String> future = threadPool.submit(func);
				futures.add(future);
			}

			Set<String> fhirIds = new HashSet<>();
			for (Future<String> future : futures) {
				try {
					fhirIds.add(future.get());
				} catch (ExecutionException e) {
					// expected
				}
			}

			assertEquals(1, fhirIds.size());

		} finally {
			threadPool.shutdown();
		}
	}


}
