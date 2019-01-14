package ca.uhn.fhir.jpa.searchparam.retry;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class RetrierTest {
	@Test
	public void happyPath() {
		Supplier<Boolean> supplier = () -> true;
		Retrier<Boolean> retrier = new Retrier<>(supplier, 5, 0, "test");
		assertTrue(retrier.runWithRetry());
	}

	@Test
	public void succeedBeforeMaxRetries() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 3) throw new RetryRuntimeException("test");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, 5, 0, "test");
		assertTrue(retrier.runWithRetry());
		assertEquals(3, counter.get());
	}

	@Test
	public void failMaxRetries() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, 5, 0, "test");
		try {
			retrier.runWithRetry();
			fail();
		} catch (RetryRuntimeException e) {
			assertEquals(5, counter.get());
		}
	}

	@Test
	public void failMaxRetriesZero() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, 0, 0, "test");
		try {
			retrier.runWithRetry();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(0, counter.get());
			assertEquals("maxRetries must be above zero." ,e.getMessage());
		}
	}

	@Test
	public void failMaxRetriesNegative() {
		AtomicInteger counter = new AtomicInteger();
		Supplier<Boolean> supplier = () -> {
			if (counter.incrementAndGet() < 10) throw new RetryRuntimeException("test");
			return true;
		};
		Retrier<Boolean> retrier = new Retrier<>(supplier, -1, 0, "test");
		try {
			retrier.runWithRetry();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(0, counter.get());
			assertEquals("maxRetries must be above zero." ,e.getMessage());
		}
	}



	class RetryRuntimeException extends RuntimeException {
		RetryRuntimeException(String message) {
			super(message);
		}
	}
}
