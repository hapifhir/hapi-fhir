package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Queues;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.hl7.fhir.dstu3.model.InstantType;
import org.junit.rules.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a query listener designed to be plugged into a {@link ProxyDataSourceBuilder proxy DataSource}.
 * This listener keeps the last 100 queries across all threads in a LinkedList, dropping queries off the
 * end of the list as new ones are added.
 * <p>
 * Note that this class is really only designed for use in testing - It adds a non-trivial overhead
 * to each query.
 * </p>
 */
public class CaptureQueriesListener extends BaseCaptureQueriesListener {

	private static final int CAPACITY = 100;
	private static final Queue<Query> LAST_N_QUERIES = Queues.synchronizedQueue(new CircularFifoQueue<>(CAPACITY));
	private static final Logger ourLog = LoggerFactory.getLogger(CaptureQueriesListener.class);

	@Override
	protected Queue<Query> provideQueryList() {
		return LAST_N_QUERIES;
	}

	/**
	 * Clear all stored queries
	 */
	public static void clear() {
		LAST_N_QUERIES.clear();
	}

	/**
	 * Index 0 is oldest
	 */
	@SuppressWarnings("UseBulkOperation")
	public static List<Query> getCapturedQueries() {
		// Make a copy so that we aren't affected by changes to the list outside of the
		// synchronized block
		ArrayList<Query> retVal = new ArrayList<>(CAPACITY);
		LAST_N_QUERIES.forEach(retVal::add);
		return Collections.unmodifiableList(retVal);
	}

	/**
	 * Log all captured SELECT queries
	 */
	public static void logSelectQueriesForCurrentThread() {
		String currentThreadName = Thread.currentThread().getName();
		List<String> queries = getCapturedQueries()
			.stream()
			.filter(t -> t.getThreadName().equals(currentThreadName))
			.filter(t -> t.getSql(false, false).toLowerCase().contains("select"))
			.map(CaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));
	}

	/**
	 * Log all captured INSERT queries
	 */
	public static void logInsertQueriesForCurrentThread() {
		String currentThreadName = Thread.currentThread().getName();
		List<String> queries = getCapturedQueries()
			.stream()
			.filter(t -> t.getThreadName().equals(currentThreadName))
			.filter(t -> t.getSql(false, false).toLowerCase().contains("insert"))
			.map(CaptureQueriesListener::formatQueryAsSql)
			.collect(Collectors.toList());
		ourLog.info("Insert Queries:\n{}", String.join("\n", queries));
	}

	private static String formatQueryAsSql(Query theQuery) {
		String formattedSql = theQuery.getSql(true, true);
		return "Query at " + new InstantType(new Date(theQuery.getQueryTimestamp())).getValueAsString() + " took " + StopWatch.formatMillis(theQuery.getElapsedTime()) + "\nSQL:\n" + formattedSql;
	}

}
