package ca.uhn.fhir.jpa.searchparam.retry;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Retrier<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(Retrier.class);

	private final Supplier<T> mySupplier;
	private final int myMaxRetries;
	private final int mySecondsBetweenRetries;
	private final String myDescription;

	public Retrier(Supplier<T> theSupplier, int theMaxRetries, int theSecondsBetweenRetries, String theDescription) {
		mySupplier = theSupplier;
		myMaxRetries = theMaxRetries;
		mySecondsBetweenRetries = theSecondsBetweenRetries;
		myDescription = theDescription;
	}

	public T runWithRetry() {
		RuntimeException lastException = new IllegalStateException("maxRetries must be above zero.");
		for (int retryCount = 1; retryCount <= myMaxRetries; ++retryCount) {
			try {
				return mySupplier.get();
			} catch(RuntimeException e) {
				ourLog.info("Failed to {}.  Attempt {} / {}: {}", myDescription, retryCount, myMaxRetries, e.getMessage());
				lastException = e;
				try {
					Thread.sleep(mySecondsBetweenRetries * DateUtils.MILLIS_PER_SECOND);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw lastException;
				}
			}
		}
		throw lastException;
	}
}
