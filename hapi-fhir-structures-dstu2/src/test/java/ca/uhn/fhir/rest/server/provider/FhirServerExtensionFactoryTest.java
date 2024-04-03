package ca.uhn.fhir.rest.server.provider;

import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class FhirServerExtensionFactoryTest {

	private FhirServerExtensionFactory<TestObserver> myFactory = new FhirServerExtensionFactory<>();
	private TestObserver myObserver = new TestObserver();
	private AtomicInteger myCounter = new AtomicInteger();

	@RegisterExtension
	final LogbackCaptureTestExtension myLogger = new LogbackCaptureTestExtension((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FhirServerExtensionFactory.class));

	@BeforeEach
	public void before() {
		myFactory.attach(myObserver);
		myObserver.assertCalls(0, 0);
	}

	@Test
	public void observersNotifiedByAddRemove() {
		Supplier<Object> supplier = myCounter::incrementAndGet;
		myFactory.addSupplier(supplier);
		myObserver.assertCalls(1, 0);
		assertEquals(0, myCounter.get());
		assertThat(myFactory.getSupplierResults(), hasSize(1));
		assertEquals(1, myCounter.get());
		myFactory.removeSupplier(supplier);
		myObserver.assertCalls(1, 1);
		assertThat(myFactory.getSupplierResults(), hasSize(0));
	}

	@Test
	public void testRemoveWrongSupplier() {
        myFactory.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertEquals(0, myCounter.get());
		assertThat(myFactory.getSupplierResults(), hasSize(1));
		assertEquals(1, myCounter.get());

		// You might expect this to remove our supplier, but in fact it is a different lambda so it fails and logs a stack trace
		myFactory.removeSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertThat(myFactory.getSupplierResults(), hasSize(1));
		List<ILoggingEvent> events = myLogger.filterLoggingEventsWithMessageContaining("Failed to remove Fhir Extension");
		assertThat(events, hasSize(1));
		assertEquals(Level.WARN, events.get(0).getLevel());
	}

	@Test
	public void testDetach() {
		myFactory.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertThat(myFactory.getSupplierResults(), hasSize(1));

		myFactory.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(2, 0);
		assertThat(myFactory.getSupplierResults(), hasSize(2));

		myFactory.detach(myObserver);

		// We now have a third supplier but the observer was only called twice
		myFactory.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(2, 0);
		assertThat(myFactory.getSupplierResults(), hasSize(3));
	}

	private class TestObserver implements IFhirServerExtensionFactoryObserver {
		int updated = 0;
		int removed = 0;

		@Override
		public void update(@NotNull Supplier<Object> theSupplier) {
			++updated;
		}

		@Override
		public void remove(@NotNull Supplier<Object> theSupplier) {
			++ removed;
		}

		public void assertCalls(int theExpectedUpdated, int theExpectedRemoved) {
			assertEquals(theExpectedUpdated, updated);
			assertEquals(theExpectedRemoved, removed);
		}
	}
}
