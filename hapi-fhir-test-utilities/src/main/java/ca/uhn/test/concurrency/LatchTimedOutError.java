package ca.uhn.test.concurrency;

public class LatchTimedOutError extends AssertionError {
	public LatchTimedOutError(String theMessage) {
		super(theMessage);
	}
}
