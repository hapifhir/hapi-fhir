package ca.uhn.fhir.jpa.api.pid;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

class AutoClosingStreamTemplateTest {

	@Test
	void templatePassesStreamToCallback() {
	    // given
	    Stream<String> concreteStream = Stream.of("one", "two");
		StreamTemplate<String> streamTemplate = StreamTemplate.fromSupplier(() -> concreteStream);

		// when
		streamTemplate.call(s -> {
			assertSame(concreteStream, s);
			return 0;
		});
	}

	@Test
	void templateClosesStreamOnExit() {
		// given
		AtomicBoolean wasClosed = new AtomicBoolean(false);
		Stream<String> concreteStream = Stream.of("one", "two")
			.onClose(()->wasClosed.set(true));
		StreamTemplate<String> streamTemplate = StreamTemplate.fromSupplier(() -> concreteStream);

		// when
		streamTemplate.call(s -> {
			// don't touch the stream;
			return 0;
		});

		assertThat(wasClosed.get()).as("stream was closed").isTrue();

	}


	@Test
	void templateClosesStreamOnException() {
		// given
		AtomicBoolean wasClosed = new AtomicBoolean(false);
		Stream<String> concreteStream = Stream.of("one", "two")
			.onClose(()->wasClosed.set(true));
		StreamTemplate<String> streamTemplate = StreamTemplate.fromSupplier(() -> concreteStream);

		// when
		try {
			streamTemplate.call(s -> {
				throw new RuntimeException("something failed");
			});
		} catch (RuntimeException e) {
			// expected;
		}

		assertThat(wasClosed.get()).as("stream was closed").isTrue();

	}

}
