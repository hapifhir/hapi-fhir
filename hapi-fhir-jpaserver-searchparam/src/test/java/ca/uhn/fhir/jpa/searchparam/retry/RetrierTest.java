package ca.uhn.fhir.jpa.searchparam.retry;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RetrierTest {

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

		try {
			retrier.runWithRetry();
			fail();
		} catch (RetryRuntimeException e) {
			assertEquals("test failure message", e.getMessage());
		}
	}

	@Test
	public void failMaxRetriesZero() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};

		try {
			new Retrier<>(supplier, 0);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("maxRetries must be above zero.", e.getMessage());
		}
	}

	@Test
	public void failMaxRetriesNegative() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};

		try {
			new Retrier<>(supplier, -1);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("maxRetries must be above zero.", e.getMessage());
		}
		assertEquals(0, counter.get());
	}

	class RetryRuntimeException extends RuntimeException {
		RetryRuntimeException(String message) {
			super(message);
		}
	}
}
