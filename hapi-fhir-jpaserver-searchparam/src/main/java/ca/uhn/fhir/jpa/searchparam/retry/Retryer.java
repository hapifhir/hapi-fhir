package ca.uhn.fhir.jpa.searchparam.retry;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Retryer<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(Retryer.class);

	private final Supplier<T> mySupplier;
	private final int myMaxRetries;
	private final int mySecondsBetweenRetries;
	private final String myDescription;

	public Retryer(Supplier<T> theSupplier, int theMaxRetries, int theSecondsBetweenRetries, String theDescription) {
		mySupplier = theSupplier;
		myMaxRetries = theMaxRetries;
		mySecondsBetweenRetries = theSecondsBetweenRetries;
		myDescription = theDescription;
	}

	public T runWithRetry() {
		RuntimeException lastException = null;
		for (int retryCount = 0; retryCount < myMaxRetries; ++retryCount) {
			try {
				return mySupplier.get();
			} catch(RuntimeException e) {
				ourLog.info("Failed to {}.  Attempt {} / {}: {}", myDescription, retryCount, myMaxRetries, e.getMessage());
				lastException = e;
				try {
					Thread.sleep(mySecondsBetweenRetries * DateUtils.MILLIS_PER_SECOND);
				} catch (InterruptedException ie) {
					lastException = null;
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
		if (lastException != null) {
			throw lastException;
		}
		return null;
	}
}
