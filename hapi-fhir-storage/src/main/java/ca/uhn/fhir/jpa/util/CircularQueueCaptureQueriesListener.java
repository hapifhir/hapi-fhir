/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Queues;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 * This is a query listener designed to be plugged into a {@link ProxyDataSourceBuilder proxy DataSource}.
 * This listener keeps the last 1000 queries across all threads in a {@link CircularFifoQueue}, dropping queries off the
 * end of the list as new ones are added.
 * <p>
 * Note that this class is really only designed for use in testing - It adds a non-trivial overhead
 * to each query.
 * </p>
 */
public class CircularQueueCaptureQueriesListener extends BaseCaptureQueriesListener {

	private static final int CAPACITY = 1000;
	private static final Logger ourLog = LoggerFactory.getLogger(CircularQueueCaptureQueriesListener.class);
	private Queue<SqlQuery> myQueries;
	private AtomicInteger myCommitCounter;
	private AtomicInteger myRollbackCounter;

	/**
	 * Constructor
	 */
	public CircularQueueCaptureQueriesListener() {
		startCollecting();
	}

	@Override
	protected Queue<SqlQuery> provideQueryList() {
		return myQueries;
	}

	@Override
	protected AtomicInteger provideCommitCounter() {
		return myCommitCounter;
	}

	@Override
	protected AtomicInteger provideRollbackCounter() {
		return myRollbackCounter;
	}

	/**
	 * Clear all stored queries
	 */
	public void clear() {
		myQueries.clear();
		myCommitCounter.set(0);
		myRollbackCounter.set(0);
	}

	/**
	 * Start collecting queries (this is the default)
	 */
	public void startCollecting() {
		myQueries = Queues.synchronizedQueue(new CircularFifoQueue<>(CAPACITY));
		myCommitCounter = new AtomicInteger(0);
		myRollbackCounter = new AtomicInteger(0);
	}

	/**
	 * Stop collecting queries and discard any collected ones
	 */
	public void stopCollecting() {
		myQueries = null;
		myCommitCounter = null;
		myRollbackCounter = null;
	}

	/**
	 * Index 0 is oldest
	 */
	@SuppressWarnings("UseBulkOperation")
	public List<SqlQuery> getCapturedQueries() {
		// Make a copy so that we aren't affected by changes to the list outside of the
		// synchronized block
		ArrayList<SqlQuery> retVal = new ArrayList<>(CAPACITY);
		myQueries.forEach(retVal::add);
		return Collections.unmodifiableList(retVal);
	}

	private List<SqlQuery> getQueriesForCurrentThreadStartingWith(String theStart) {
		String threadName = Thread.currentThread().getName();
		return getQueriesStartingWith(theStart, threadName);
	}

	private List<SqlQuery> getQueriesStartingWith(String theStart, String theThreadName) {
		return getCapturedQueries().stream()
				.filter(t -> theThreadName == null || t.getThreadName().equals(theThreadName))
				.filter(t -> t.getSql(false, false).toLowerCase().startsWith(theStart))
				.collect(Collectors.toList());
	}

	private List<SqlQuery> getQueriesStartingWith(String theStart) {
		return getQueriesStartingWith(theStart, null);
	}

	public int getCommitCount() {
		return myCommitCounter.get();
	}

	public int getRollbackCount() {
		return myRollbackCounter.get();
	}

	/**
	 * Returns all SELECT queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getSelectQueries() {
		return getQueriesStartingWith("select");
	}

	/**
	 * Returns all INSERT queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getInsertQueries() {
		return getQueriesStartingWith("insert");
	}

	/**
	 * Returns all UPDATE queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getUpdateQueries() {
		return getQueriesStartingWith("update");
	}

	/**
	 * Returns all UPDATE queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getDeleteQueries() {
		return getQueriesStartingWith("delete");
	}

	/**
	 * Returns all SELECT queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getSelectQueriesForCurrentThread() {
		return getQueriesForCurrentThreadStartingWith("select");
	}

	/**
	 * Returns all INSERT queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getInsertQueriesForCurrentThread() {
		return getQueriesForCurrentThreadStartingWith("insert");
	}

	/**
	 * Returns all queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getAllQueriesForCurrentThread() {
		return getQueriesForCurrentThreadStartingWith("");
	}

	/**
	 * Returns all UPDATE queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getUpdateQueriesForCurrentThread() {
		return getQueriesForCurrentThreadStartingWith("update");
	}

	/**
	 * Returns all UPDATE queries executed on the current thread - Index 0 is oldest
	 */
	public List<SqlQuery> getDeleteQueriesForCurrentThread() {
		return getQueriesForCurrentThreadStartingWith("delete");
	}

	/**
	 * Log all captured UPDATE queries
	 */
	public String logUpdateQueriesForCurrentThread() {
		List<String> queries = getUpdateQueriesForCurrentThread().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		String joined = String.join("\n", queries);
		ourLog.info("Update Queries:\n{}", joined);
		return joined;
	}

	/**
	 * Log all captured SELECT queries
	 *
	 * @return
	 */
	public String logSelectQueriesForCurrentThread(int... theIndexes) {
		List<String> queries = getSelectQueriesForCurrentThread().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());

		List<String> newList = new ArrayList<>();
		if (theIndexes != null && theIndexes.length > 0) {
			for (int i = 0; i < theIndexes.length; i++) {
				int index = theIndexes[i];
				newList.add("[" + index + "] " + queries.get(index));
			}
		} else {
			for (int i = 0; i < queries.size(); i++) {
				newList.add("[" + i + "] " + queries.get(i));
			}
		}
		queries = newList;

		String queriesAsString = String.join("\n", queries);
		ourLog.info("Select Queries:\n{}", queriesAsString);
		return queriesAsString;
	}

	/**
	 * Log all captured SELECT queries
	 */
	public List<SqlQuery> logSelectQueries() {
		return logSelectQueries(true, true);
	}

	/**
	 * Log all captured SELECT queries
	 */
	public List<SqlQuery> logSelectQueries(boolean theInlineParams, boolean theFormatSql) {
		List<SqlQuery> queries = getSelectQueries();
		List<String> queriesStrings = queries.stream()
				.map(t -> CircularQueueCaptureQueriesListener.formatQueryAsSql(t, theInlineParams, theFormatSql))
				.collect(Collectors.toList());
		ourLog.info("Select Queries:\n{}", String.join("\n", queriesStrings));
		return queries;
	}

	/**
	 * Log first captured SELECT query
	 */
	public void logFirstSelectQueryForCurrentThread() {
		String firstSelectQuery = getSelectQueriesForCurrentThread().stream()
				.findFirst()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.orElse("NONE FOUND");
		ourLog.info("First select SqlQuery:\n{}", firstSelectQuery);
	}

	/**
	 * Log all captured INSERT queries
	 */
	public String logInsertQueriesForCurrentThread() {
		List<String> queries = getInsertQueriesForCurrentThread().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		String queriesAsString = String.join("\n", queries);
		ourLog.info("Insert Queries:\n{}", queriesAsString);
		return queriesAsString;
	}

	/**
	 * Log all captured queries
	 */
	public void logAllQueriesForCurrentThread() {
		List<String> queries = getAllQueriesForCurrentThread().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		ourLog.info("Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured queries
	 */
	public void logAllQueries() {
		List<String> queries = getCapturedQueries().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		ourLog.info("Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured INSERT queries
	 */
	public int logInsertQueries() {
		return logInsertQueries(null);
	}

	/**
	 * Log all captured INSERT queries
	 */
	public int logInsertQueries(Predicate<SqlQuery> theInclusionPredicate) {
		List<SqlQuery> insertQueries = getInsertQueries();
		List<String> queries = insertQueries.stream()
				.filter(t -> theInclusionPredicate == null || theInclusionPredicate.test(t))
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));

		return countQueries(insertQueries);
	}

	/**
	 * Log all captured INSERT queries
	 */
	public int logUpdateQueries() {
		List<SqlQuery> updateQueries = getUpdateQueries();
		List<String> queries = updateQueries.stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		ourLog.info("Update Queries:\n{}", String.join("\n", queries));

		return countQueries(updateQueries);
	}

	/**
	 * Log all captured DELETE queries
	 */
	public String logDeleteQueriesForCurrentThread() {
		List<String> queries = getDeleteQueriesForCurrentThread().stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		String joined = String.join("\n", queries);
		ourLog.info("Delete Queries:\n{}", joined);
		return joined;
	}

	/**
	 * Log all captured DELETE queries
	 */
	public int logDeleteQueries() {
		List<SqlQuery> deleteQueries = getDeleteQueries();
		List<String> queries = deleteQueries.stream()
				.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
				.collect(Collectors.toList());
		ourLog.info("Delete Queries:\n{}", String.join("\n", queries));

		return countQueries(deleteQueries);
	}

	public int countSelectQueries() {
		return countQueries(getSelectQueries());
	}

	public int countInsertQueries() {
		return countQueries(getInsertQueries());
	}

	public int countUpdateQueries() {
		return countQueries(getUpdateQueries());
	}

	public int countDeleteQueries() {
		return countQueries(getDeleteQueries());
	}

	public int countSelectQueriesForCurrentThread() {
		return countQueries(getSelectQueriesForCurrentThread());
	}

	public int countInsertQueriesForCurrentThread() {
		return countQueries(getInsertQueriesForCurrentThread());
	}

	public int countUpdateQueriesForCurrentThread() {
		return countQueries(getUpdateQueriesForCurrentThread());
	}

	public int countDeleteQueriesForCurrentThread() {
		return countQueries(getDeleteQueriesForCurrentThread());
	}

	@Nonnull
	private static Integer countQueries(List<SqlQuery> theQueries) {
		return theQueries.stream().map(t -> t.getSize()).reduce(0, Integer::sum);
	}

	@Nonnull
	static String formatQueryAsSql(SqlQuery theQuery) {
		boolean inlineParams = true;
		boolean formatSql = true;
		return formatQueryAsSql(theQuery, inlineParams, formatSql);
	}

	@Nonnull
	static String formatQueryAsSql(SqlQuery theQuery, boolean inlineParams, boolean formatSql) {
		String formattedSql = theQuery.getSql(inlineParams, formatSql);
		StringBuilder b = new StringBuilder();
		b.append("SqlQuery at ");
		b.append(new InstantType(new Date(theQuery.getQueryTimestamp())).getValueAsString());
		if (theQuery.getRequestPartitionId() != null
				&& theQuery.getRequestPartitionId().hasPartitionIds()) {
			b.append(" on partition ");
			b.append(theQuery.getRequestPartitionId().getPartitionIds());
		}
		b.append(" took ").append(StopWatch.formatMillis(theQuery.getElapsedTime()));
		b.append(" on Thread: ").append(theQuery.getThreadName());
		if (theQuery.getSize() > 1) {
			b.append("\nExecution Count: ")
					.append(theQuery.getSize())
					.append(" (parameters shown are for first execution)");
		}
		b.append("\nSQL:\n").append(formattedSql);
		if (theQuery.getStackTrace() != null) {
			b.append("\nStack:\n   ");
			Stream<String> stackTraceStream = Arrays.stream(theQuery.getStackTrace())
					.map(StackTraceElement::toString)
					.filter(t -> t.startsWith("ca."));
			b.append(stackTraceStream.collect(Collectors.joining("\n   ")));
		}
		b.append("\n");
		return b.toString();
	}
}
