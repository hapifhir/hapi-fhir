package ca.uhn.fhir.util;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * Utility to fill a glaring gap in SLF4j's API - The fact that you can't
 * specify a log level at runtime.
 *
 * See here for a discussion:
 * https://jira.qos.ch/browse/SLF4J-124
 */
public class LogUtil {

	public static void log(Logger theLogger, Level theLevel, String theMessage, Object... theArgs) {
		switch (theLevel) {
			case TRACE:
				theLogger.trace(theMessage, theArgs);
				break;
			case DEBUG:
				theLogger.debug(theMessage, theArgs);
				break;
			case INFO:
				theLogger.info(theMessage, theArgs);
				break;
			case WARN:
				theLogger.warn(theMessage, theArgs);
				break;
			case ERROR:
				theLogger.error(theMessage, theArgs);
				break;
		}
	}

}
