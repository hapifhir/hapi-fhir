package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class MemoryCacheServiceTest {
	private static final Logger ourLog = LoggerFactory.getLogger(MemoryCacheServiceTest.class);
	MemoryCacheService mySvc;

	@BeforeEach
	public void setUp() {
		DaoConfig daoConfig = new DaoConfig();
		daoConfig.setMassIngestionMode(false);
		mySvc = new MemoryCacheService();
		mySvc.myDaoConfig = daoConfig;
	}

	@Test
	public void simpleTagCacheRetrieve() {
		String system = "http://example.com";
		TagTypeEnum type = TagTypeEnum.TAG;
		String code = "t";

		MemoryCacheService.TagDefinitionCacheKey cacheKey = new MemoryCacheService.TagDefinitionCacheKey(type, system, code);
		mySvc.start();

		TagDefinition retVal = mySvc.getIfPresent(MemoryCacheService.CacheEnum.TAG_DEFINITION, cacheKey);
		assertThat(retVal, nullValue());

		TagDefinition tagDef = new TagDefinition(type, system, code, "theLabel");
		mySvc.put(MemoryCacheService.CacheEnum.TAG_DEFINITION, cacheKey, tagDef);

		retVal = mySvc.getIfPresent(MemoryCacheService.CacheEnum.TAG_DEFINITION, cacheKey);
		assertThat(retVal, equalTo(tagDef));
	}

	@Nested
	public static class CaffeineAbuseTest {

		Cache<Integer, Integer> myCache;
		List<SlowFastJob> mySlowJobs;
		List<SlowFastJob> myFastJobs;
		ExecutorService myExecutor;
		final boolean[] canProceed = new boolean[]{ false };

		@AfterEach
		public void tearDown() {
			myExecutor.shutdown();
		}

		void withCacheOfSize(int theMaxSize) {
			myCache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(theMaxSize).build();
		}

		void fillCacheWithRange(int theStart, int theEnd) {
			for (int i = theStart; i < theEnd; i++) {
				myCache.put(i, i);
			}
		}

		@Test
		public void fullCacheHandlesSlowReaders() throws InterruptedException, ExecutionException {
			// mimic our tag cache under pathological unbounded tag usage

			// given a full cache
			withCacheOfSize(10000);
			fillCacheWithRange(30000, 30000 + 10000);

			final int nThreads = 40;
			withExecutorOfSize(nThreads);

			// when we spill the cache, and have delayed calculation.
			// block all but 1 of the workers with a slow job
			startJobs(1000, (j) ->  (j < nThreads - 1));

			// wait for results to start appearing.
			SlowFastJob firstFastJob = myFastJobs.get(0);
			SlowFastJob lastFastJob = myFastJobs.get(myFastJobs.size() - 1);

			firstFastJob.getOrTimeout("first fast job blocked by earlier waiters");
			lastFastJob.getOrTimeout("last fast job blocked by earlier waiters");

			// fast jobs done
			myFastJobs.stream().forEach(SlowFastJob::assertDone);
			// slow jobs still blocked
			mySlowJobs.stream().forEach(SlowFastJob::assertNotDone);

			// blocked items released
			canProceed[0] = true;

			for(SlowFastJob job: mySlowJobs) {
				job.getOrTimeout("released job doesn't complete");
			}
		}

		/**
		 * Identifies our problem with Caffeine.
		 *
		 * computeIfAbsent locks the hash node.
		 * This is not a problem with a full cache, since it will only block colliding entries.
		 * But it prevents growing the map, so until we hit the full 10k in Tags, it was single-threading ingestion.
		 *
		 */
		@Test
		@Disabled("Fails with empty cache")
		public void emptyCacheHandlesSlowReaders() throws InterruptedException, ExecutionException {
			// mimic our tag cache under pathological unbounded tag usage

			// given an empty cache
			withCacheOfSize(10000);

			final int nThreads = 10;
			withExecutorOfSize(nThreads);

			// when we spill the cache, and have delayed calculation.
			// block only a single thread
			startJobs(1000, (j) ->  (j == nThreads));

			// wait for results to start appearing.
			SlowFastJob firstFastJob = myFastJobs.get(0);
			SlowFastJob lastFastJob = myFastJobs.get(myFastJobs.size() - 1);

			firstFastJob.getOrTimeout("first fast job blocked by earlier waiters");
			lastFastJob.getOrTimeout("last fast job blocked by earlier waiters");

			// blocked items released
			canProceed[0] = true;

			for(SlowFastJob job: mySlowJobs) {
				job.getOrTimeout("released job doesn't complete");
			}
		}

		private void startJobs(int jobCount, Predicate<Integer> slowPredicate) {
			mySlowJobs = new ArrayList<>();
			myFastJobs = new ArrayList<>();
			for (int i = 0; i < jobCount; i++) {
				boolean slow = slowPredicate.test(i);
				//boolean slow = i == 0;
				SlowFastJob job = new SlowFastJob(i, slow, myCache, canProceed);
				if (job.mySlowFlag) {
					mySlowJobs.add(job);
				} else {
					myFastJobs.add(job);
				}
				job.submit(myExecutor);
			}
		}

		private void withExecutorOfSize(int nThreads) {
			myExecutor = Executors.newFixedThreadPool(nThreads);
		}

		static class SlowFastJob implements Callable<Integer> {
			final boolean mySlowFlag;
			final int myValue;
			final boolean[] myProceedFlag;
			final Cache<Integer, Integer> myCache;
			Future<Integer> future;

			SlowFastJob(int theValue, boolean theSlowFlag, Cache<Integer, Integer> theCache, boolean[] theProceedFlag) {
				this.mySlowFlag = theSlowFlag;
				this.myValue = theValue;
				this.myProceedFlag = theProceedFlag;
				this.myCache = theCache;
			}

			@Override
			public Integer call() throws Exception {
				return myCache.get(myValue, i -> computeValue());
			}

			private int computeValue() {
				if (mySlowFlag) {
					while(!myProceedFlag[0]) {
						try {
							Thread.sleep(100);
							ourLog.debug("yawn " + myValue);
						} catch (InterruptedException e) {
							// empty
						}
					}
				}
				return myValue;
			}

			public Integer getOrTimeout(String theMessage) throws InterruptedException, ExecutionException {
				try {
					return future.get(1, TimeUnit.SECONDS);
				} catch (TimeoutException e) {
					fail(theMessage);
					return null;
				}
			}

			public void submit(ExecutorService executor) {
				future = executor.submit(this);
			}

			void assertDone() {
				assertTrue(future.isDone());
			}

			void assertNotDone() {
				assertFalse(future.isDone(), "job " + myValue + " not done");
			}
		}
	}


}
