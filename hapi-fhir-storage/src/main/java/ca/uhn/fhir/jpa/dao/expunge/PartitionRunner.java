package ca.uhn.fhir.jpa.dao.expunge;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PartitionRunner {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionRunner.class);
	private static final int MAX_POOL_SIZE = 1000;

	private final String myProcessName;
	private final String myThreadPrefix;
	private final int myBatchSize;
	private final int myThreadCount;

	public PartitionRunner(String theProcessName, String theThreadPrefix, int theBatchSize, int theThreadCount) {
		myProcessName = theProcessName;
		myThreadPrefix = theThreadPrefix;
		myBatchSize = theBatchSize;
		myThreadCount = theThreadCount;
	}

	public void runInPartitionedThreads(List<ResourcePersistentId> theResourceIds, Consumer<List<ResourcePersistentId>> partitionConsumer) {

		List<Callable<Void>> callableTasks = buildCallableTasks(theResourceIds, partitionConsumer);
		if (callableTasks.size() == 0) {
			return;
		}

		if (callableTasks.size() == 1) {
			try {
				callableTasks.get(0).call();
				return;
			} catch (Exception e) {
				ourLog.error("Error while " + myProcessName, e);
				throw new InternalErrorException(Msg.code(1084) + e);
			}
		}

		ExecutorService executorService = buildExecutor(callableTasks.size());
		try {
			List<Future<Void>> futures = executorService.invokeAll(callableTasks);
			// wait for all the threads to finish
			for (Future<Void> future : futures) {
				future.get();
			}
		} catch (InterruptedException e) {
			ourLog.error("Interrupted while " + myProcessName, e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			ourLog.error("Error while " + myProcessName, e);
			throw new InternalErrorException(Msg.code(1085) + e);
		} finally {
			executorService.shutdown();
		}
	}

	private List<Callable<Void>> buildCallableTasks(List<ResourcePersistentId> theResourceIds, Consumer<List<ResourcePersistentId>> partitionConsumer) {
		List<Callable<Void>> retval = new ArrayList<>();

		if (myBatchSize > theResourceIds.size()) {
			ourLog.info("Splitting batch job of {} entries into chunks of {}", theResourceIds.size(), myBatchSize);
		} else {
			ourLog.info("Creating batch job of {} entries", theResourceIds.size());
		}
		List<List<ResourcePersistentId>> partitions = Lists.partition(theResourceIds, myBatchSize);

		for (List<ResourcePersistentId> nextPartition : partitions) {
			if (nextPartition.size() > 0) {
				Callable<Void> callableTask = () -> {
					ourLog.info(myProcessName + " {} resources", nextPartition.size());
					partitionConsumer.accept(nextPartition);
					return null;
				};
				retval.add(callableTask);
			}
		}

		return retval;
	}

	private ExecutorService buildExecutor(int numberOfTasks) {
		int threadCount = Math.min(numberOfTasks, myThreadCount);
		assert (threadCount > 0);

		ourLog.info(myProcessName + " with {} threads", threadCount);
		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern(myThreadPrefix + "-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
			ourLog.info("Note: " + myThreadPrefix + " executor queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
			StopWatch sw = new StopWatch();
			try {
				executorQueue.put(theRunnable);
			} catch (InterruptedException e) {
				throw new RejectedExecutionException(Msg.code(1086) + "Task " + theRunnable.toString() +
					" rejected from " + e);
			}
			ourLog.info("Slot become available after {}ms", sw.getMillis());
		};
		return new ThreadPoolExecutor(
			threadCount,
			MAX_POOL_SIZE,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);
	}
}
