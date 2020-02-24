package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Queues;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.hl7.fhir.dstu3.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	/**
	 * Clear all stored queries
	 */
	public void clear() {
		myQueries.clear();
	}

	/**
	 * Start collecting queries (this is the default)
	 */
	public void startCollecting() {
		myQueries = Queues.synchronizedQueue(new CircularFifoQueue<>(CAPACITY));
	}

	/**
	 * Stop collecting queries and discard any collected ones
	 */
	public void stopCollecting() {
		myQueries = null;
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
		return getCapturedQueries()
			.stream()
			.filter(t -> theThreadName == null || t.getThreadName().equals(theThreadName))
			.filter(t -> t.getSql(false, false).toLowerCase().startsWith(theStart))
			.collect(Collectors.toList());
	}

	private List<SqlQuery> getQueriesStartingWith(String theStart) {
		return getQueriesStartingWith(theStart, null);
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
	 * Returns all INSERT queries executed on the current thread - Index 0 is oldest
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
	public void logUpdateQueriesForCurrentThread() {
		List<String> queries = getUpdateQueriesForCurrentThread()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Update Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured SELECT queries
	 */
	public void logSelectQueriesForCurrentThread(int... theIndexes) {
		List<String> queries = getSelectQueriesForCurrentThread()
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
	}

	/**
	 * Log all captured SELECT queries
	 */
	public void logSelectQueries() {
		List<String> queries = getSelectQueries()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Select Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log first captured SELECT query
	 */
	public void logFirstSelectQueryForCurrentThread() {
		String firstSelectQuery = getSelectQueriesForCurrentThread()
			.stream()
			.findFirst()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.orElse("NONE FOUND");
		ourLog.info("First select SqlQuery:\n{}", firstSelectQuery);
	}

	/**
	 * Log all captured INSERT queries
	 */
	public void logInsertQueriesForCurrentThread() {
		List<String> queries = getInsertQueriesForCurrentThread()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured INSERT queries
	 */
	public void logAllQueriesForCurrentThread() {
		List<String> queries = getAllQueriesForCurrentThread()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured INSERT queries
	 */
	public void logInsertQueries() {
		List<String> queries = getInsertQueries()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured INSERT queries
	 */
	public void logUpdateQueries() {
		List<String> queries = getUpdateQueries()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Update Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured DELETE queries
	 */
	public void logDeleteQueriesForCurrentThread() {
		List<String> queries = getDeleteQueriesForCurrentThread()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Delete Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured DELETE queries
	 */
	public void logDeleteQueries() {
		List<String> queries = getDeleteQueries()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Delete Queries:\n{}", String.join("\n", queries));
	}

	public int countSelectQueries() {
		return getSelectQueries().size();
	}

	public int countInsertQueries() {
		return getInsertQueries().size();
	}

	public int countUpdateQueries() {
		return getUpdateQueries().size();
	}

	public int countDeleteQueries() {
		return getDeleteQueries().size();
	}

	public int countSelectQueriesForCurrentThread() {
		return getSelectQueriesForCurrentThread().size();
	}

	public int countInsertQueriesForCurrentThread() {
		return getInsertQueriesForCurrentThread().size();
	}

	public int countUpdateQueriesForCurrentThread() {
		return getUpdateQueriesForCurrentThread().size();
	}

	public int countDeleteQueriesForCurrentThread() {
		return getDeleteQueriesForCurrentThread().size();
	}


	static String formatQueryAsSql(SqlQuery theQuery) {
		String formattedSql = theQuery.getSql(true, true);
		StringBuilder b = new StringBuilder();
		b.append("SqlQuery at ");
		b.append(new InstantType(new Date(theQuery.getQueryTimestamp())).getValueAsString());
		b.append(" took ").append(StopWatch.formatMillis(theQuery.getElapsedTime()));
		b.append(" on Thread: ").append(theQuery.getThreadName());
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
