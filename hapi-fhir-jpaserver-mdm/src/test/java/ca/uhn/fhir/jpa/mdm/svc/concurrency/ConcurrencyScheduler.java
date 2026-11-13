package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Drives MDM worker threads through a scripted {@link ThreadSequence}, using the gates in
 * {@link GatingMdmMatchClaimSvc}, and records what actually happened.
 * <p>
 * A step that cannot happen yet (because the design makes the thread wait, for example on another thread's claim)
 * is recorded as blocked or deferred, and the scheduler moves on. Deferred steps are retried once the script has
 * finished, and then every remaining gate is opened so that all threads run to completion. Timeouts are only ever used
 * to detect that a thread is blocked, never to order events.
 */
// Created by claude-opus-5-5
public class ConcurrencyScheduler {
	private static final Logger ourLog = LoggerFactory.getLogger(ConcurrencyScheduler.class);

	/**
	 * How long to wait for a thread to reach a gate, or to finish a granted step, before deciding it is blocked.
	 */
	static final long STEP_TIMEOUT_MILLIS = 1000;

	/**
	 * How long to wait, once the rest of the script has run, for any deferred step to become possible.
	 */
	static final long DEFERRED_STEP_TIMEOUT_MILLIS = 10_000;

	private static final long COMPLETION_TIMEOUT_MILLIS = 120_000;

	public enum GateEnum {
		NONE,
		START,
		COMMIT
	}

	public enum EventTypeEnum {
		/** The attempt took its claims. */
		START,
		/** The attempt's claim conflicted with another thread's claim. */
		CONFLICT,
		/** The attempt rolled back for another reason, normally a restart to claim more matching sources. */
		ROLLBACK,
		/** The attempt was made to abort by the script. */
		ABORT,
		/** The attempt committed. */
		COMMIT
	}

	public enum StepOutcomeEnum {
		/** The step happened as scripted. */
		DONE,
		/** The thread was released but did not finish the step: it is waiting in the database. */
		BLOCKED,
		/** The thread never reached the gate in time; the step was retried after the script. */
		DEFERRED,
		/** The step no longer applies, for example a scripted restart that never happened. */
		SKIPPED
	}

	public record Event(String thread, String source, EventTypeEnum type) {}

	public record StepResult(ThreadSequence.Step step, StepOutcomeEnum outcome) {}

	private final Object myLock = new Object();
	private final ThreadLocal<Worker> myCurrentWorker = new ThreadLocal<>();
	private final List<Event> myEvents = new ArrayList<>();
	private final Map<Integer, Worker> myWorkers = new LinkedHashMap<>();
	private boolean myFreeRun;

	@Nullable
	Worker currentWorker() {
		return myCurrentWorker.get();
	}

	/**
	 * Runs the given tasks, one per thread, through the scripted sequence.
	 *
	 * @param theSequence the script
	 * @param theTasks    one task per thread, in thread order (thread 1 first)
	 * @param theSources  the source label processed by each thread, for the event log
	 * @return what happened
	 */
	public Run run(ThreadSequence theSequence, List<Runnable> theTasks, List<String> theSources) {
		synchronized (myLock) {
			myEvents.clear();
			myWorkers.clear();
			myFreeRun = false;
		}
		ExecutorService executor = Executors.newFixedThreadPool(theTasks.size());
		List<Future<?>> futures = new ArrayList<>();
		try {
			for (int i = 0; i < theTasks.size(); i++) {
				int threadNumber = i + 1;
				Worker worker = new Worker(
						"t" + threadNumber, theSources.get(i), theSequence.scriptedStarts(threadNumber));
				synchronized (myLock) {
					myWorkers.put(threadNumber, worker);
				}
				Runnable task = theTasks.get(i);
				futures.add(executor.submit(() -> worker.run(task)));
			}

			List<StepResult> results = new ArrayList<>();
			List<ThreadSequence.Step> deferred = new ArrayList<>();
			for (ThreadSequence.Step step : theSequence.steps()) {
				StepOutcomeEnum outcome = execute(step, STEP_TIMEOUT_MILLIS);
				if (outcome == StepOutcomeEnum.DEFERRED) {
					deferred.add(step);
				}
				results.add(new StepResult(step, outcome));
			}
			runDeferred(deferred);

			synchronized (myLock) {
				myFreeRun = true;
				myLock.notifyAll();
			}
			for (Future<?> future : futures) {
				future.get(COMPLETION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
			}

			synchronized (myLock) {
				List<Worker> workers = new ArrayList<>(myWorkers.values());
				return new Run(List.copyOf(myEvents), results, workers);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Scenario " + theSequence + " did not complete: " + e, e);
		} finally {
			executor.shutdownNow();
		}
	}

	/**
	 * Retries deferred steps round-robin until each has happened, or none can make progress before the deferred
	 * timeout. The design can force an order different from the script's: for example a thread that lost its place
	 * can only reach its commit gate after a thread whose step comes later in the script has committed.
	 */
	private void runDeferred(List<ThreadSequence.Step> theDeferred) throws InterruptedException {
		List<ThreadSequence.Step> pending = new ArrayList<>(theDeferred);
		long deadline = System.currentTimeMillis() + DEFERRED_STEP_TIMEOUT_MILLIS;
		while (!pending.isEmpty() && System.currentTimeMillis() < deadline) {
			for (java.util.Iterator<ThreadSequence.Step> iter = pending.iterator(); iter.hasNext(); ) {
				ThreadSequence.Step step = iter.next();
				StepOutcomeEnum outcome = execute(step, 200);
				if (outcome != StepOutcomeEnum.DEFERRED) {
					ourLog.info("Deferred step {} finished as {}", step, outcome);
					iter.remove();
					deadline = System.currentTimeMillis() + DEFERRED_STEP_TIMEOUT_MILLIS;
				}
			}
		}
		if (!pending.isEmpty()) {
			ourLog.warn("Deferred steps {} never became possible", pending);
		}
	}

	private StepOutcomeEnum execute(ThreadSequence.Step theStep, long theTimeoutMillis) throws InterruptedException {
		synchronized (myLock) {
			Worker worker = myWorkers.get(theStep.thread());
			return switch (theStep.action()) {
				case START -> worker.grantStart(theTimeoutMillis);
				case COMMIT -> worker.grantCommit(true, theTimeoutMillis);
				case ABORT -> worker.grantCommit(false, theTimeoutMillis);
			};
		}
	}

	private void record(Worker theWorker, EventTypeEnum theType) {
		myEvents.add(new Event(theWorker.myName, theWorker.mySource, theType));
		ourLog.info("Concurrency event: {} ({}) {}", theWorker.myName, theWorker.mySource, theType);
	}

	/**
	 * Waits on the shared lock until the condition holds or the timeout passes. Must hold {@link #myLock}.
	 */
	private boolean awaitCondition(java.util.function.BooleanSupplier theCondition, long theTimeoutMillis)
			throws InterruptedException {
		long deadline = System.currentTimeMillis() + theTimeoutMillis;
		while (!theCondition.getAsBoolean()) {
			long remaining = deadline - System.currentTimeMillis();
			if (remaining <= 0) {
				return false;
			}
			myLock.wait(remaining);
		}
		return true;
	}

	/**
	 * One MDM worker thread.
	 */
	public class Worker {
		private final String myName;
		private final String mySource;
		private final int myScriptedStarts;
		private int myStartArrivals;
		private int myStartsGranted;
		private GateEnum myParkedAt = GateEnum.NONE;
		private Boolean myCommitDecision;
		private boolean myClaimedThisAttempt;
		private boolean myClaimFailedThisAttempt;
		private boolean myAbortingThisAttempt;
		private int myCompletedAttempts;
		private boolean myDone;
		private Throwable myFailure;
		private boolean myAborted;

		Worker(String theName, String theSource, int theScriptedStarts) {
			myName = theName;
			mySource = theSource;
			myScriptedStarts = theScriptedStarts;
		}

		public String getName() {
			return myName;
		}

		public String getSource() {
			return mySource;
		}

		@Nullable
		public Throwable getFailure() {
			return myFailure;
		}

		public boolean isAborted() {
			return myAborted;
		}

		void run(Runnable theTask) {
			myCurrentWorker.set(this);
			try {
				theTask.run();
			} catch (Throwable t) {
				synchronized (myLock) {
					myFailure = t;
				}
			} finally {
				myCurrentWorker.remove();
				synchronized (myLock) {
					myDone = true;
					myLock.notifyAll();
				}
			}
		}

		void awaitStartGate() {
			synchronized (myLock) {
				myStartArrivals++;
				myClaimedThisAttempt = false;
				myClaimFailedThisAttempt = false;
				myAbortingThisAttempt = false;
				int arrival = myStartArrivals;
				if (!myFreeRun && arrival <= myScriptedStarts) {
					myParkedAt = GateEnum.START;
					myLock.notifyAll();
					waitUninterruptibly(() -> myFreeRun || myStartsGranted >= arrival);
					myParkedAt = GateEnum.NONE;
				}
				myLock.notifyAll();
			}
		}

		void claimed() {
			synchronized (myLock) {
				myClaimedThisAttempt = true;
				record(this, EventTypeEnum.START);
				myLock.notifyAll();
			}
		}

		void claimFailed() {
			synchronized (myLock) {
				myClaimFailedThisAttempt = true;
				record(this, EventTypeEnum.CONFLICT);
				myLock.notifyAll();
			}
		}

		void awaitCommitGate() {
			boolean commit;
			synchronized (myLock) {
				myParkedAt = GateEnum.COMMIT;
				myLock.notifyAll();
				waitUninterruptibly(() -> myFreeRun || myCommitDecision != null);
				myParkedAt = GateEnum.NONE;
				commit = myCommitDecision == null || myCommitDecision;
				myCommitDecision = null;
				if (!commit) {
					myAbortingThisAttempt = true;
					myAborted = true;
					record(this, EventTypeEnum.ABORT);
				}
				myLock.notifyAll();
			}
			if (!commit) {
				throw new IllegalStateException("Scripted abort of " + myName);
			}
		}

		void attemptCompleted(boolean theCommitted) {
			synchronized (myLock) {
				if (theCommitted) {
					record(this, EventTypeEnum.COMMIT);
				} else if (!myAbortingThisAttempt && !myClaimFailedThisAttempt) {
					record(this, EventTypeEnum.ROLLBACK);
				}
				myCompletedAttempts++;
				myLock.notifyAll();
			}
		}

		/**
		 * Lets the thread take its claims. Must hold {@link #myLock}.
		 */
		private StepOutcomeEnum grantStart(long theTimeoutMillis) throws InterruptedException {
			if (!awaitCondition(() -> myParkedAt != GateEnum.NONE || myDone, theTimeoutMillis)) {
				return StepOutcomeEnum.DEFERRED;
			}
			if (myParkedAt != GateEnum.START) {
				// The thread is ready to commit (or finished), so the scripted restart never happened
				return StepOutcomeEnum.SKIPPED;
			}
			myStartsGranted++;
			int arrival = myStartArrivals;
			myLock.notifyAll();
			boolean finished = awaitCondition(
					() -> myDone
							|| myStartArrivals > arrival
							|| myParkedAt == GateEnum.COMMIT
							|| myClaimedThisAttempt
							|| myClaimFailedThisAttempt,
					theTimeoutMillis);
			return finished ? StepOutcomeEnum.DONE : StepOutcomeEnum.BLOCKED;
		}

		/**
		 * Lets the thread commit, or makes it abort. Must hold {@link #myLock}.
		 */
		private StepOutcomeEnum grantCommit(boolean theCommit, long theTimeoutMillis) throws InterruptedException {
			if (!awaitCondition(() -> myParkedAt == GateEnum.COMMIT || myDone, theTimeoutMillis)) {
				return StepOutcomeEnum.DEFERRED;
			}
			if (myDone) {
				return StepOutcomeEnum.SKIPPED;
			}
			int completed = myCompletedAttempts;
			myCommitDecision = theCommit;
			myLock.notifyAll();
			boolean finished = awaitCondition(() -> myDone || myCompletedAttempts > completed, theTimeoutMillis);
			return finished ? StepOutcomeEnum.DONE : StepOutcomeEnum.BLOCKED;
		}

		private void waitUninterruptibly(java.util.function.BooleanSupplier theCondition) {
			while (!theCondition.getAsBoolean()) {
				try {
					myLock.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}

	/**
	 * What happened during one scripted run.
	 */
	public record Run(List<Event> events, List<StepResult> steps, List<Worker> workers) {

		/**
		 * The sources in the order their processing committed.
		 */
		public List<String> commitOrder() {
			return events.stream()
					.filter(e -> e.type() == EventTypeEnum.COMMIT)
					.map(Event::source)
					.toList();
		}

		public long count(EventTypeEnum theType) {
			return events.stream().filter(e -> e.type() == theType).count();
		}

		public boolean anyStep(StepOutcomeEnum theOutcome) {
			return steps.stream().anyMatch(s -> s.outcome() == theOutcome);
		}

		@Override
		public String toString() {
			return "events=" + events + ", steps=" + steps;
		}
	}
}
