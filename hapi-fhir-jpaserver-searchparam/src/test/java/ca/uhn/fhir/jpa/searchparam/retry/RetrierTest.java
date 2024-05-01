package ca.uhn.fhir.jpa.searchparam.retry;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
		assertThat(counter.get()).isEqualTo(3);
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
			fail("");
		} catch (RetryRuntimeException e) {
			assertThat(e.getMessage()).isEqualTo("test failure message");
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
			fail("");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("maxRetries must be above zero.");
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
			fail("");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("maxRetries must be above zero.");
		}
		assertThat(counter.get()).isEqualTo(0);
	}

	class RetryRuntimeException extends RuntimeException {
		RetryRuntimeException(String message) {
			super(message);
		}
	}
}
