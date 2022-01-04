package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * hapi-fhir-jpa
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CurrentThreadCaptureQueriesListener extends BaseCaptureQueriesListener {

	private static final ThreadLocal<Queue<SqlQuery>> ourQueues = new ThreadLocal<>();
	private static final ThreadLocal<AtomicInteger> ourCommits = new ThreadLocal<>();
	private static final ThreadLocal<AtomicInteger> ourRollbacks = new ThreadLocal<>();
	private static final Logger ourLog = LoggerFactory.getLogger(CurrentThreadCaptureQueriesListener.class);

	@Override
	protected Queue<SqlQuery> provideQueryList() {
		return ourQueues.get();
	}

	@Override
	protected AtomicInteger provideCommitCounter() {
		return ourCommits.get();
	}

	@Override
	protected AtomicInteger provideRollbackCounter() {
		return ourRollbacks.get();
	}

	/**
	 * Get the current queue of items and stop collecting
	 */
	public static SqlQueryList getCurrentQueueAndStopCapturing() {
		Queue<SqlQuery> retVal = ourQueues.get();
		ourQueues.remove();
		ourCommits.remove();
		ourRollbacks.remove();
		if (retVal == null) {
			return new SqlQueryList();
		}
		return new SqlQueryList(retVal);
	}


	/**
	 * Starts capturing queries for the current thread.
	 * <p>
	 * Note that you should strongly consider calling this in a
	 * try-finally block to ensure that you also call
	 * {@link #getCurrentQueueAndStopCapturing()} afterward. Otherwise
	 * this method is a potential memory leak!
	 * </p>
	 */
	public static void startCapturing() {
		ourQueues.set(new ArrayDeque<>());
		ourCommits.set(new AtomicInteger(0));
		ourRollbacks.set(new AtomicInteger(0));
	}

	/**
	 * Log all captured SELECT queries
	 *
	 * @return Returns the number of queries captured
	 */
	public static int logQueriesForCurrentThreadAndStopCapturing(int... theIndexes) {
		List<String> queries = getCurrentQueueAndStopCapturing()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());

		if (theIndexes != null && theIndexes.length > 0) {
			List<String> newList = new ArrayList<>();
			for (int i = 0; i < theIndexes.length; i++) {
				newList.add(queries.get(theIndexes[i]));
			}
			queries = newList;
		}

		ourLog.info("Select Queries:\n{}", String.join("\n", queries));

		return queries.size();
	}
}
