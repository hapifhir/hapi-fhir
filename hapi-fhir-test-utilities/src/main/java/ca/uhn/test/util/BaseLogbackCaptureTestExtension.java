package ca.uhn.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public abstract class BaseLogbackCaptureTestExtension<T extends Appender<ILoggingEvent>> implements BeforeEachCallback, AfterEachCallback {
	private final Logger myLogger;
	private final Level myLevel;
	private Level mySavedLevel;
	private T myAppender = null;

	protected BaseLogbackCaptureTestExtension(Logger theLogger, Level theTestLogLevel) {
		myLogger = theLogger;
		myLevel = theTestLogLevel;
	}

	protected BaseLogbackCaptureTestExtension(Logger theLogger) {
		this(theLogger,null);
	}

	protected BaseLogbackCaptureTestExtension(String theLoggerName, Level theTestLogLevel) {
		this((Logger) LoggerFactory.getLogger(theLoggerName), theTestLogLevel);
	}

	protected BaseLogbackCaptureTestExtension(String theLoggerName) {
		this((Logger) LoggerFactory.getLogger(theLoggerName), null);
	}

	/**
	 * Get the wrapped logger
	 */
	public Logger getLogger() {
		return myLogger;
	}

	/**
	 * @return the capture appender.
	 */
	public T getAppender() {
		return myAppender;
	}

	/**
	 * Add theFilter to the capture appender.
	 * @param theFilter
	 */
	public void addFilter(Filter<ILoggingEvent> theFilter) {
		getAppender().addFilter(theFilter);
	}


	// junit wiring

	public void beforeEach(ExtensionContext context) throws Exception {
		setUp();
	}

	@Override
	public void afterEach(ExtensionContext context) {
		tearDown();
	}

	/**
	 * Guts of beforeEach exposed for manual lifecycle.
	 */
	public void setUp() {
		myAppender = buildAppender();
		myAppender.start();
		myLogger.addAppender(myAppender);
		if (myLevel != null) {
			mySavedLevel = myLogger.getLevel();
			myLogger.setLevel(myLevel);
		}
	}


	/**
	 * Guts of afterEach for manual lifecycle
	 */
	public void tearDown() {
		myLogger.detachAppender(myAppender);
		myAppender.stop();
		if (myLevel != null) {
			myLogger.setLevel(mySavedLevel);
		}
	}

	/**
	 * Extension point for different appenders
	 * @return a configured appender - will be started after.
	 */
	@Nonnull
	abstract T buildAppender();
}
