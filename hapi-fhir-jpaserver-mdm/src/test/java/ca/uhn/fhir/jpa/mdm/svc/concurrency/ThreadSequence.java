package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import java.util.Arrays;
import java.util.List;

/**
 * The scripted thread interleavings P1-P24 of the concurrency matrix, plus targeted scripts that the matrix skips.
 * <p>
 * A script is a list of steps: {@code Sn} lets thread n take its claims (its "start"), {@code Cn} lets it commit and
 * {@code An} makes it abort just before committing. A thread's second {@code S} step is the start of a restarted
 * attempt. Conflict retries and restarts that a script doesn't mention happen on their own.
 */
// Created by claude-opus-5-5
public enum ThreadSequence {
	P1(3, "S1 C1 S2 C2 S3 C3"),
	P4(3, "S1 S2 S3 C1 C2 C3"),
	P5(3, "S1 S2 S3 C1 C3 C2"),
	P6(3, "S1 S2 S3 C3 C1 C2"),
	P7(3, "S1 S2 C1 S3 C2 C3"),
	P8(3, "S1 S2 C1 S3 C3 C2"),
	P9(3, "S1 S2 S3 C2 C1 C3"),
	P10(3, "S1 S2 S3 C2 C3 C1"),
	P11(3, "S1 S2 S3 C3 C2 C1"),
	P12(3, "S1 S2 C2 S3 C1 C3"),
	P13(3, "S1 S2 C2 S3 C3 C1"),
	P14(3, "S1 C1 S2 S3 C2 C3"),
	P15(3, "S1 C1 S2 S3 C3 C2"),
	P16(3, "S1 S2 C1 C2 S3 C3"),
	P17(3, "S1 S2 C2 C1 S3 C3"),
	P23A(3, "S1 S2 S3 A1 C2 C3"),
	P23B(3, "S1 S2 S3 A1 C3 C2"),
	P24(3, "S1 S2 S3 S1 C2 C3 C1"),

	SERIAL2(2, "S1 C1 S2 C2"),
	P2(2, "S1 S2 C1 C2"),
	P3(2, "S1 S2 C2 C1"),
	P18(2, "S1 A1 S2 C2"),
	P19(2, "S1 S2 A1 C2"),
	P20(2, "S1 S2 S1 S2 C1 C2"),
	P21(2, "S1 S2 S2 C1 C2"),
	P22(2, "S1 S2 S1 C2 C1"),

	/**
	 * Targeted test T1: t1 joins an existing transaction and claims a newly found matching source mid-transaction (its
	 * second start), after t2 has restarted and claimed both sources in sorted order.
	 */
	T1_JOINED(2, false, "S1 S2 S2 S1 C1 C2");

	public enum ActionEnum {
		START,
		COMMIT,
		ABORT
	}

	public record Step(int thread, ActionEnum action) {
		@Override
		public String toString() {
			return action.name().charAt(0) + String.valueOf(thread);
		}
	}

	private final int myThreadCount;
	private final boolean myInMatrix;
	private final List<Step> mySteps;

	ThreadSequence(int theThreadCount, String theScript) {
		this(theThreadCount, true, theScript);
	}

	ThreadSequence(int theThreadCount, boolean theInMatrix, String theScript) {
		myThreadCount = theThreadCount;
		myInMatrix = theInMatrix;
		mySteps = Arrays.stream(theScript.split(" ")).map(ThreadSequence::parse).toList();
	}

	private static Step parse(String theToken) {
		ActionEnum action =
				switch (theToken.charAt(0)) {
					case 'S' -> ActionEnum.START;
					case 'C' -> ActionEnum.COMMIT;
					case 'A' -> ActionEnum.ABORT;
					default -> throw new IllegalArgumentException("Bad step " + theToken);
				};
		return new Step(Integer.parseInt(theToken.substring(1)), action);
	}

	public int threadCount() {
		return myThreadCount;
	}

	public List<Step> steps() {
		return mySteps;
	}

	/**
	 * The thread whose processing the script aborts, or 0 when none does.
	 */
	public int abortedThread() {
		return mySteps.stream()
				.filter(s -> s.action() == ActionEnum.ABORT)
				.mapToInt(Step::thread)
				.findFirst()
				.orElse(0);
	}

	int scriptedStarts(int theThread) {
		return (int) mySteps.stream()
				.filter(s -> s.thread() == theThread && s.action() == ActionEnum.START)
				.count();
	}

	/**
	 * The matrix sequences for the given number of threads.
	 */
	public static List<ThreadSequence> forThreadCount(int theThreadCount) {
		return Arrays.stream(values())
				.filter(s -> s.myInMatrix && s.myThreadCount == theThreadCount)
				.toList();
	}
}
