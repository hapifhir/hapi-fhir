package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
