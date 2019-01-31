package ca.uhn.fhir.jpa.searchparam.retry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RetrierTest {

	@Rule
	public ExpectedException myExpectedException = ExpectedException.none();

	@Test
	public void happyPath() {
		Supplier<Boolean> supplier = () -> true;
		Retrier<Boolean> retrier = new Retrier<>(supplier, 5);
		assertTrue(retrier.runWithRetry());
	}

	@Test
	public void succeedBeforeMaxRetries() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 3) throw new RetryRuntimeException("test");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, 5);
		assertTrue(retrier.runWithRetry());
		assertEquals(3, counter.get());
	}

	@Test
	public void failMaxRetries() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 3) throw new RetryRuntimeException("test failure message");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, 1);

		myExpectedException.expect(RetryRuntimeException.class);
		myExpectedException.expectMessage("test failure message");
		retrier.runWithRetry();
		assertEquals(5, counter.get());
	}

	@Test
	public void failMaxRetriesZero() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};
		myExpectedException.expect(IllegalArgumentException.class);
		myExpectedException.expectMessage("maxRetries must be above zero.");
		Retrier<Boolean> retrier = new Retrier<>(supplier, 0);
		assertEquals(0, counter.get());
	}

	@Test
	public void failMaxRetriesNegative() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};
		myExpectedException.expect(IllegalArgumentException.class);
		myExpectedException.expectMessage("maxRetries must be above zero.");

		Retrier<Boolean> retrier = new Retrier<>(supplier, -1);
		assertEquals(0, counter.get());
	}

	class RetryRuntimeException extends RuntimeException {
		RetryRuntimeException(String message) {
			super(message);
		}
	}
}
