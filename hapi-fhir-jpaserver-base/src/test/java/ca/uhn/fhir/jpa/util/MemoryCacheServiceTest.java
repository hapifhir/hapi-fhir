package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
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

	@Test
	public void ensureCaffeineHandlesSlowReaders() throws InterruptedException, ExecutionException {
		// mimic our tag cache under pathological unbounded tag usage

		// given a full cache
		Cache<Integer, Integer> cache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(10000).build();
		for (int i = 30000; i < 50000; i++) {
			cache.put(i, i);
		}
		//assertThat(cache.estimatedSize(), greaterThan(9999L));
		final int nThreads = 80;
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		// when we spill the cache, and have delayed calculation.
		final boolean[] canProceed = new boolean[]{ false };
		List<SlowFastJob> jobs = new ArrayList<>();
		List<SlowFastJob> slowJobs = new ArrayList<>();
		List<SlowFastJob> fastJobs = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			// block all but 1 of the workers with a slow job
			boolean slow = i < nThreads - 1;
			SlowFastJob job = new SlowFastJob(i, slow, canProceed);
			if (job.mySlowFlag) {
				slowJobs.add(job);
			} else {
				fastJobs.add(job);
			}
			jobs.add(job);
			job.submit(executor);
		}

		// wait for results to start appearing.
		SlowFastJob firstFastJob = fastJobs.get(0);
		SlowFastJob lastFastJob = fastJobs.get(fastJobs.size() - 1);

		firstFastJob.getOrTimeout("first future blocked by earlier waiters");
		lastFastJob.getOrTimeout("last future blocked by earlier waiters");

		for(SlowFastJob job: fastJobs) {
			assertTrue(job.future.isDone());
		}

		// blocked items not done
		for(SlowFastJob job: slowJobs) {
			assertFalse(job.future.isDone());
		}

		// blocked items released
		canProceed[0] = true;

		for(SlowFastJob job: slowJobs) {
			job.getOrTimeout("released job doesn't complete");
		}

		executor.shutdown();
	}

	static class SlowFastJob implements Callable<Integer> {
		final boolean mySlowFlag;
		final int myValue;
		final boolean[] myProceedFlag;
		Future<Integer> future;

		SlowFastJob(int theValue, boolean theSlowFlag, boolean[] theProceedFlag) {
			this.mySlowFlag = theSlowFlag;
			this.myValue = theValue;
			this.myProceedFlag = theProceedFlag;
		}

		@Override
		public Integer call() throws Exception {
			if (mySlowFlag) {
				while(!myProceedFlag[0]) {
					try {
						Thread.sleep(100);
						ourLog.debug("yawn " + myValue);
					} catch (InterruptedException e) { }
				}
			}
			return myValue;
		}

		public Integer getOrTimeout(String theMessage) throws InterruptedException, ExecutionException {
			try {
				return future.get(100, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				fail(theMessage);
				return null;
			}
		}

		public void submit(ExecutorService executor) {
			future = executor.submit(this);
		}
	}


}
