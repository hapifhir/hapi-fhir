package ca.uhn.fhir.util;

import org.slf4j.Logger;

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

	public enum Level {
		TRACE, DEBUG, INFO, WARN, ERROR
	}

}
