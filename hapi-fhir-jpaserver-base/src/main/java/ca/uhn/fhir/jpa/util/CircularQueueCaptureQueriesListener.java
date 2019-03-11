package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Queues;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.hl7.fhir.dstu3.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

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
	private final Queue<Query> myQueries = Queues.synchronizedQueue(new CircularFifoQueue<>(CAPACITY));

	@Override
	protected Queue<Query> provideQueryList() {
		return myQueries;
	}

	/**
	 * Clear all stored queries
	 */
	public void clear() {
		myQueries.clear();
	}

	/**
	 * Index 0 is oldest
	 */
	@SuppressWarnings("UseBulkOperation")
	public List<Query> getCapturedQueries() {
		// Make a copy so that we aren't affected by changes to the list outside of the
		// synchronized block
		ArrayList<Query> retVal = new ArrayList<>(CAPACITY);
		myQueries.forEach(retVal::add);
		return Collections.unmodifiableList(retVal);
	}

	/**
	 * Returns all SELECT queries executed on the current thread - Index 0 is oldest
	 */
	public List<Query> getSelectQueriesForCurrentThread() {
		String currentThreadName = Thread.currentThread().getName();
		return getCapturedQueries()
			.stream()
			.filter(t -> t.getThreadName().equals(currentThreadName))
			.filter(t -> t.getSql(false, false).toLowerCase().contains("select"))
			.collect(Collectors.toList());
	}

	/**
	 * Returns all INSERT queries executed on the current thread - Index 0 is oldest
	 */
	public List<Query> getInsertQueriesForCurrentThread() {
		return getCapturedQueries()
			.stream()
			.filter(t -> t.getThreadName().equals(Thread.currentThread().getName()))
			.filter(t -> t.getSql(false, false).toLowerCase().contains("insert"))
			.collect(Collectors.toList());
	}

	/**
	 * Log all captured SELECT queries
	 */
	public void logSelectQueriesForCurrentThread() {
		List<String> queries = getSelectQueriesForCurrentThread()
			.stream()
			.map(CircularQueueCaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Select Queries:\n{}", String.join("\n", queries));
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

	private static String formatQueryAsSql(Query theQuery) {
		String formattedSql = theQuery.getSql(true, true);
		return "Query at " + new InstantType(new Date(theQuery.getQueryTimestamp())).getValueAsString() + " took " + StopWatch.formatMillis(theQuery.getElapsedTime()) + " on Thread: " + theQuery.getThreadName() + "\nSQL:\n" + formattedSql;
	}

}
