package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncUtil.class);

	/**
	 * Non instantiable
	 */
	private AsyncUtil() {
	}

	/**
	 * Calls Thread.sleep and if an InterruptedException occurs, logs a warning but otherwise continues
	 *
	 * @param theMillis The number of millis to sleep
	 * @return Did we sleep the whole amount
	 */
	public static boolean sleep(long theMillis) {
		try {
			Thread.sleep(theMillis);
			return true;
		} catch (InterruptedException theE) {
			Thread.currentThread().interrupt();
			ourLog.warn("Sleep for {}ms was interrupted", theMillis);
			return false;
		}
	}

	public static boolean awaitLatchAndThrowInternalErrorExceptionOnInterrupt(CountDownLatch theInitialCollectionLatch, long theTime, TimeUnit theTimeUnit) {
		try {
			return theInitialCollectionLatch.await(theTime, theTimeUnit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(e);
		}
	}

	public static boolean awaitLatchAndIgnoreInterrupt(CountDownLatch theInitialCollectionLatch, long theTime, TimeUnit theTimeUnit) {
		try {
			return theInitialCollectionLatch.await(theTime, theTimeUnit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ourLog.warn("Interrupted while waiting for latch");
			return false;
		}
	}
}
