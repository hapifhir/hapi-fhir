package ca.uhn.fhir.jpa.util;

import java.util.ArrayDeque;
import java.util.Queue;

public class CurrentThreadCaptureQueriesListener extends BaseCaptureQueriesListener {

	private static final ThreadLocal<Queue<SqlQuery>> ourQueues = new ThreadLocal<>();

	@Override
	protected Queue<SqlQuery> provideQueryList() {
		return ourQueues.get();
	}


	/**
	 * Get the current queue of items and stop collecting
	 */
	public static SqlQueryList getCurrentQueueAndStopCapturing() {
		Queue<SqlQuery> retVal = ourQueues.get();
		ourQueues.remove();
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
	}

}
