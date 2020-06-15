package ca.uhn.fhir.jpa.search.lastn;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *`
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.search.util.impl.Executors;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ObservationCodeIndexingSvcImpl {

	private static final Logger ourLog = LoggerFactory.getLogger(ObservationCodeIndexingSvcImpl.class);
	private final ReentrantLock myIndexingLock = new ReentrantLock();
	@Autowired
	private DaoConfig myDaoConfig;
	private final ThreadFactory myIndexingThreadFactory = new BasicThreadFactory.Builder().namingPattern("ResourceIndex-%d").build();
	private ThreadPoolExecutor myTaskExecutor;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IElasticsearchSvc myElasticsearchSvc;

	@PostConstruct
	public void start() {
		initExecutor();
		scheduleJob();
	}

	public void initExecutor() {
		// Create the threadpool executor used for index jobs
		int indexThreadCount = myDaoConfig.getReindexThreadCount();
		RejectedExecutionHandler rejectHandler = new Executors.BlockPolicy();
		myTaskExecutor = new ThreadPoolExecutor(0, indexThreadCount,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(100),
			myIndexingThreadFactory,
			rejectHandler
		);
	}

	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ObservationCodeIndexingSvcImpl myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runIndexingPass();
		}
	}

	public void runIndexingPass() {
		if (myDaoConfig.isSchedulingDisabled()) {
			return;
		}
		if (myIndexingLock.tryLock()) {
			try {
				doIndexingPassInsideLock();
			} finally {
				myIndexingLock.unlock();
			}
		}
	}

	private void doIndexingPassInsideLock() {
		runIndexJob();
	}

	public void forceIndexingPass() {
		myIndexingLock.lock();
		try {
			doIndexingPassInsideLock();
		} finally {
			myIndexingLock.unlock();
		}
	}

	public void cancelAndPurgeAllJobs() {
		ourLog.info("Cancelling and purging all resource indexing jobs");
		myIndexingLock.lock();
		try {

		myTaskExecutor.shutdown();
		initExecutor();

		} finally {
			myIndexingLock.unlock();
		}
	}

	private void runIndexJob() {

		ourLog.info("Performing Observation indexing pass");
		StopWatch sw = new StopWatch();
		Integer counter = 0;

		// Query for resources within threshold
		StopWatch pageSw = new StopWatch();
		List<String> range = myElasticsearchSvc.getObservationsNeedingCodeUpdate();
		Validate.notNull(range);
		int initialObservationListCount = range.size();
		ourLog.info("Loaded {} Observation resources for indexing in {}", initialObservationListCount, pageSw.toString());

		// Submit each resource requiring indexing
		List<Future<Integer>> futures = range
			.stream()
			.map(t -> myTaskExecutor.submit(new ObservationCodeIndexingTask(t, myElasticsearchSvc)))
			.collect(Collectors.toList());

		for (Future<Integer> next : futures) {
			try {
				counter += next.get();
			} catch (Exception e) {
				ourLog.error("Failure reindexing Observations", e);
			}

		}

		ourLog.info("Completed pass of indexing Observations - Indexed {} resources in {} ({} / sec)", counter, sw.toString(), sw.formatThroughput(initialObservationListCount, TimeUnit.SECONDS));
	}

	private static class ObservationCodeIndexingTask implements Callable<Integer> {
		private final String myNextId;
		private final IElasticsearchSvc myElasticsearchSvc;

		ObservationCodeIndexingTask(String theNextId, IElasticsearchSvc theIElasticsearchSvc) {
			myNextId = theNextId;
			myElasticsearchSvc = theIElasticsearchSvc;
		}

		@Override
		public Integer call() {
			myElasticsearchSvc.updateObservationCode(myNextId);
			// Return number of Observation documents updated.
			return 0;
		}
	}
}
