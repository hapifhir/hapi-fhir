package ca.uhn.fhir.rest.server.provider;

import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static ca.uhn.test.util.LoggingEventPredicates.makeMessageContains;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObservableSupplierSetTest {

	private final ObservableSupplierSet<TestObserver> myObservableSupplierSet = new ObservableSupplierSet<>();
	private final TestObserver myObserver = new TestObserver();
	private final AtomicInteger myCounter = new AtomicInteger();

	@RegisterExtension
	final LogbackTestExtension myLogger = new LogbackTestExtension((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ObservableSupplierSet.class));

	@BeforeEach
	public void before() {
		myObservableSupplierSet.attach(myObserver);
		myObserver.assertCalls(0, 0);
	}

	@Test
	public void observersNotifiedByAddRemove() {
		Supplier<Object> supplier = myCounter::incrementAndGet;
		myObservableSupplierSet.addSupplier(supplier);
		myObserver.assertCalls(1, 0);
		assertEquals(0, myCounter.get());
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(1);
		assertEquals(1, myCounter.get());
		myObservableSupplierSet.removeSupplier(supplier);
		myObserver.assertCalls(1, 1);
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(0);
	}

	@Test
	public void testRemoveWrongSupplier() {
        myObservableSupplierSet.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertEquals(0, myCounter.get());
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(1);
		assertEquals(1, myCounter.get());

		// You might expect this to remove our supplier, but in fact it is a different lambda, so it fails and logs a stack trace
		myObservableSupplierSet.removeSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(1);
        List<ILoggingEvent> events = myLogger.getLogEvents(makeMessageContains("Failed to remove supplier"));
		assertThat(events).hasSize(1);
		assertEquals(Level.WARN, events.get(0).getLevel());
	}

	@Test
	public void testDetach() {
		myObservableSupplierSet.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(1, 0);
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(1);

		myObservableSupplierSet.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(2, 0);
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(2);

		myObservableSupplierSet.detach(myObserver);

		// We now have a third supplier but the observer has been detached, so it was not notified of the third supplier
		myObservableSupplierSet.addSupplier(myCounter::incrementAndGet);
		myObserver.assertCalls(2, 0);
		assertThat(myObservableSupplierSet.getSupplierResults()).hasSize(3);
	}

	private static class TestObserver implements IObservableSupplierSetObserver {
		int updated = 0;
		int removed = 0;

		@Override
		public void update(@Nonnull Supplier<Object> theSupplier) {
			++updated;
		}

		@Override
		public void remove(@Nonnull Supplier<Object> theSupplier) {
			++removed;
		}

		public void assertCalls(int theExpectedUpdated, int theExpectedRemoved) {
			assertEquals(theExpectedUpdated, updated);
			assertEquals(theExpectedRemoved, removed);
		}
	}
}
