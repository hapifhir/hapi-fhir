package ca.uhn.fhir.util;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * A multipurpose stopwatch which can be used to time tasks and produce
 * human readable output about task duration, throughput, estimated task completion,
 * etc.
 * <p>
 * <p>
 * <b>Thread Safety Note: </b> StopWatch is not intended to be thread safe.
 * </p>
 *
 * @since HAPI FHIR 3.3.0
 */
public class StopWatch {

	private static Long ourNowForUnitTest;
	private long myStarted = now();
	private TaskTiming myCurrentTask;
	private LinkedList<TaskTiming> myTasks;

	/**
	 * Constructor
	 */
	public StopWatch() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theStart The time to record as the start for this timer
	 */
	public StopWatch(Date theStart) {
		myStarted = theStart.getTime();
	}

	/**
	 * Constructor
	 *
	 * @param theStart The time that the stopwatch was started
	 */
	public StopWatch(long theStart) {
		myStarted = theStart;
	}

	private void addNewlineIfContentExists(StringBuilder theB) {
		if (theB.length() > 0) {
			theB.append("\n");
		}
	}

	/**
	 * Finish the counter on the current task (which was started by calling
	 * {@link #startTask(String)}. This method has no effect if no task
	 * is currently started so it's ok to call it more than once.
	 */
	public void endCurrentTask() {
		ensureTasksListExists();
		if (myCurrentTask != null) {
			myCurrentTask.setEnd(now());
		}
		myCurrentTask = null;
	}

	private void ensureTasksListExists() {
		if (myTasks == null) {
			myTasks = new LinkedList<>();
		}
	}

	/**
	 * Returns a nice human-readable display of the time taken per
	 * operation. Note that this may not actually output the number
	 * of milliseconds if the time taken per operation was very long (over
	 * 10 seconds)
	 *
	 * @see #formatMillis(long)
	 */
	public String formatMillisPerOperation(long theNumOperations) {
		double millisPerOperation = (((double) getMillis()) / Math.max(1.0, theNumOperations));
		return formatMillis(millisPerOperation);
	}

	/**
	 * Returns a string providing the durations of all tasks collected by {@link #startTask(String)}
	 */
	public String formatTaskDurations() {

		ensureTasksListExists();
		StringBuilder b = new StringBuilder();

		if (myTasks.size() > 0) {
			long delta = myTasks.getFirst().getStart() - myStarted;
			if (delta > 10) {
				addNewlineIfContentExists(b);
				b.append("Before first task");
				b.append(": ");
				b.append(formatMillis(delta));
			}
		} else {
			b.append("No tasks");
		}

		TaskTiming last = null;
		for (TaskTiming nextTask : myTasks) {

			if (last != null) {
				long delta = nextTask.getStart() - last.getEnd();
				if (delta > 10) {
					addNewlineIfContentExists(b);
					b.append("Between");
					b.append(": ");
					b.append(formatMillis(delta));
				}
			}

			addNewlineIfContentExists(b);
			b.append(nextTask.getTaskName());
			b.append(": ");
			long delta = nextTask.getMillis();
			b.append(formatMillis(delta));

			last = nextTask;
		}

		if (myTasks.size() > 0) {
			long delta = now() - myTasks.getLast().getEnd();
			if (delta > 10) {
				addNewlineIfContentExists(b);
				b.append("After last task");
				b.append(": ");
				b.append(formatMillis(delta));
			}
		}

		return b.toString();
	}

	/**
	 * Determine the current throughput per unit of time (specified in theUnit)
	 * assuming that theNumOperations operations have happened.
	 * <p>
	 * For example, if this stopwatch has 2 seconds elapsed, and this method is
	 * called for theNumOperations=30 and TimeUnit=SECONDS,
	 * this method will return 15
	 * </p>
	 *
	 * @see #getThroughput(long, TimeUnit)
	 */
	public String formatThroughput(long theNumOperations, TimeUnit theUnit) {
		double throughput = getThroughput(theNumOperations, theUnit);
		return formatThroughput(throughput);
	}

	/**
	 * Given an amount of something completed so far, and a total amount, calculates how long it will take for something to complete
	 *
	 * @param theCompleteToDate The amount so far
	 * @param theTotal          The total (must be higher than theCompleteToDate
	 * @return A formatted amount of time
	 */
	public String getEstimatedTimeRemaining(double theCompleteToDate, double theTotal) {
		double millis = getMillis();
		return formatEstimatedTimeRemaining(theCompleteToDate, theTotal, millis);
	}

	/**
	 * Given an amount of something completed so far, and a total amount, calculates how long it will take for something to complete
	 *
	 * @param theCompleteToDate The amount so far
	 * @param theTotal          The total (must be higher than theCompleteToDate
	 * @return A formatted amount of time
	 */
	public static String formatEstimatedTimeRemaining(double theCompleteToDate, double theTotal, double millis) {
		long millisRemaining = (long) (((theTotal / theCompleteToDate) * millis) - millis);
		return formatMillis(millisRemaining);
	}

	public long getMillis(Date theNow) {
		return theNow.getTime() - myStarted;
	}

	public long getMillis() {
		long now = now();
		return now - myStarted;
	}

	public long getMillisAndRestart() {
		long now = now();
		long retVal = now - myStarted;
		myStarted = now;
		return retVal;
	}

	/**
	 * @param theNumOperations Ok for this to be 0, it will be treated as 1
	 */
	public long getMillisPerOperation(long theNumOperations) {
		return (long) (((double) getMillis()) / Math.max(1.0, theNumOperations));
	}

	public Date getStartedDate() {
		return new Date(myStarted);
	}

	/**
	 * Determine the current throughput per unit of time (specified in theUnit)
	 * assuming that theNumOperations operations have happened.
	 * <p>
	 * For example, if this stopwatch has 2 seconds elapsed, and this method is
	 * called for theNumOperations=30 and TimeUnit=SECONDS,
	 * this method will return 15
	 * </p>
	 *
	 * @see #formatThroughput(long, TimeUnit)
	 */
	public double getThroughput(long theNumOperations, TimeUnit theUnit) {
		long millis = getMillis();
		return getThroughput(theNumOperations, millis, theUnit);
	}

	public void restart() {
		myStarted = now();
	}

	/**
	 * Starts a counter for a sub-task
	 * <p>
	 * <b>Thread Safety Note: </b> This method is not threadsafe! Do not use subtasks in a
	 * multithreaded environment.
	 * </p>
	 *
	 * @param theTaskName Note that if theTaskName is blank or empty, no task is started
	 */
	public void startTask(String theTaskName) {
		endCurrentTask();
		Validate.notBlank(theTaskName, "Task name must not be blank");
		myCurrentTask = new TaskTiming()
			.setTaskName(theTaskName)
			.setStart(now());
		myTasks.add(myCurrentTask);
	}

	/**
	 * Formats value in an appropriate format. See {@link #formatMillis(long)}}
	 * for a description of the format
	 *
	 * @see #formatMillis(long)
	 */
	@Override
	public String toString() {
		return formatMillis(getMillis());
	}

	/**
	 * Format a throughput number (output does not include units)
	 */
	public static String formatThroughput(double throughput) {
		return new DecimalFormat("0.0").format(throughput);
	}

	/**
	 * Calculate throughput
	 *
	 * @param theNumOperations The number of operations completed
	 * @param theMillisElapsed The time elapsed
	 * @param theUnit          The unit for the throughput
	 */
	public static double getThroughput(long theNumOperations, long theMillisElapsed, TimeUnit theUnit) {
		if (theNumOperations <= 0) {
			return 0.0f;
		}
		long millisElapsed = Math.max(1, theMillisElapsed);
		long periodMillis = theUnit.toMillis(1);

		double denominator = ((double) millisElapsed) / ((double) periodMillis);

		double throughput = (double) theNumOperations / denominator;
		if (throughput > theNumOperations) {
			throughput = theNumOperations;
		}

		return throughput;
	}

	private static NumberFormat getDayFormat() {
		return new DecimalFormat("0.0");
	}

	private static NumberFormat getTenDayFormat() {
		return new DecimalFormat("0");
	}

	private static NumberFormat getSubMillisecondMillisFormat() {
		return new DecimalFormat("0.000");
	}

	/**
	 * Append a right-aligned and zero-padded numeric value to a `StringBuilder`.
	 */
	static void appendRightAlignedNumber(StringBuilder theStringBuilder, String thePrefix, int theNumberOfDigits, long theValueToAppend) {
		theStringBuilder.append(thePrefix);
		if (theNumberOfDigits > 1) {
			int pad = (theNumberOfDigits - 1);
			for (long xa = theValueToAppend; xa > 9 && pad > 0; xa /= 10) {
				pad--;
			}
			for (int xa = 0; xa < pad; xa++) {
				theStringBuilder.append('0');
			}
		}
		theStringBuilder.append(theValueToAppend);
	}

	/**
	 * Formats a number of milliseconds for display (e.g.
	 * in a log file), tailoring the output to how big
	 * the value actually is.
	 * <p>
	 * Example outputs:
	 * </p>
	 * <ul>
	 * <li>133ms</li>
	 * <li>00:00:10.223</li>
	 * <li>1.7 days</li>
	 * <li>64 days</li>
	 * </ul>
	 */
	public static String formatMillis(long theMillis) {
		return formatMillis((double) theMillis);
	}

	/**
	 * Formats a number of milliseconds for display (e.g.
	 * in a log file), tailoring the output to how big
	 * the value actually is.
	 * <p>
	 * Example outputs:
	 * </p>
	 * <ul>
	 * <li>133ms</li>
	 * <li>00:00:10.223</li>
	 * <li>1.7 days</li>
	 * <li>64 days</li>
	 * </ul>
	 */
	public static String formatMillis(double theMillis) {
		StringBuilder buf = new StringBuilder(20);
		if (theMillis > 0.0 && theMillis < 1.0) {
			buf.append(getSubMillisecondMillisFormat().format(theMillis));
			buf.append("ms");
		} else if (theMillis < (10 * DateUtils.MILLIS_PER_SECOND)) {
			buf.append((int) theMillis);
			buf.append("ms");
		} else if (theMillis >= DateUtils.MILLIS_PER_DAY) {
			double days = theMillis / DateUtils.MILLIS_PER_DAY;
			if (days >= 10) {
				buf.append(getTenDayFormat().format(days));
				buf.append(" days");
			} else if (days != 1.0f) {
				buf.append(getDayFormat().format(days));
				buf.append(" days");
			} else {
				buf.append(getDayFormat().format(days));
				buf.append(" day");
			}
		} else {
			long millisAsLong = (long) theMillis;
			appendRightAlignedNumber(buf, "", 2, ((millisAsLong % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR));
			appendRightAlignedNumber(buf, ":", 2, ((millisAsLong % DateUtils.MILLIS_PER_HOUR) / DateUtils.MILLIS_PER_MINUTE));
			appendRightAlignedNumber(buf, ":", 2, ((millisAsLong % DateUtils.MILLIS_PER_MINUTE) / DateUtils.MILLIS_PER_SECOND));
			if (theMillis <= DateUtils.MILLIS_PER_MINUTE) {
				appendRightAlignedNumber(buf, ".", 3, (millisAsLong % DateUtils.MILLIS_PER_SECOND));
			}
		}
		return buf.toString();
	}

	private static long now() {
		if (ourNowForUnitTest != null) {
			return ourNowForUnitTest;
		}
		return System.currentTimeMillis();
	}

	@VisibleForTesting
	static void setNowForUnitTestForUnitTest(Long theNowForUnitTest) {
		ourNowForUnitTest = theNowForUnitTest;
	}

	private static class TaskTiming {
		private long myStart;
		private long myEnd;
		private String myTaskName;

		public long getEnd() {
			if (myEnd == 0) {
				return now();
			}
			return myEnd;
		}

		public TaskTiming setEnd(long theEnd) {
			myEnd = theEnd;
			return this;
		}

		public long getMillis() {
			return getEnd() - getStart();
		}

		public long getStart() {
			return myStart;
		}

		public TaskTiming setStart(long theStart) {
			myStart = theStart;
			return this;
		}

		public String getTaskName() {
			return myTaskName;
		}

		public TaskTiming setTaskName(String theTaskName) {
			myTaskName = theTaskName;
			return this;
		}
	}

}
