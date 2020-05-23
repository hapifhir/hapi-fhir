package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestUtil.class);
	private static boolean ourShouldRandomizeTimezones = true;

	public static void setShouldRandomizeTimezones(boolean theShouldRandomizeTimezones) {
		ourShouldRandomizeTimezones = theShouldRandomizeTimezones;
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * When we run the unit tests in cobertura, JUnit doesn't seem to clean up static fields which leads to
	 * tons of memory being used by the end and the JVM crashes in Travis. Manually clearing all of the
	 * static fields seems to solve this.
	 */
	public static void clearAllStaticFieldsForUnitTest() {
		HapiLocalizer.setOurFailOnMissingMessage(true);

		Class<?> theType;
		try {
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement[] st = e.getStackTrace();
			StackTraceElement elem = st[1];
			String clazzName = elem.getClassName();
			try {
				theType = Class.forName(clazzName);
			} catch (ClassNotFoundException e1) {
				throw new Error(e);
			}
		}

		for (Field next : Arrays.asList(theType.getDeclaredFields())) {
			if (Modifier.isStatic(next.getModifiers())) {
				if (!Modifier.isFinal(next.getModifiers()) && !next.getType().isPrimitive()) {
					ourLog.info("Clearing value of field: {}", next.toString());
					try {
						next.setAccessible(true);
						next.set(theType, null);
					} catch (Exception e) {
						throw new Error(e);
					}
				}
				if (Modifier.isFinal(next.getModifiers())) {
					if (next.getType().equals(FhirContext.class)) {
						throw new Error("Test has final field of type FhirContext: " + next);
					}
				}
			}

		}

		randomizeLocale();

		/*
		 * If we're running a CI build, set all loggers to TRACE level to ensure coverage
		 * on trace blocks
		 */
		try {
			if ("true".equals(System.getProperty("ci"))) {
				for (Logger next : ((LoggerContext) LoggerFactory.getILoggerFactory()).getLoggerList()) {
					next.setLevel(Level.TRACE);
				}
			}
		} catch (NoClassDefFoundError e) {
			// ignore
		}
	}

	/**
	 * Set some system properties randomly after each test.. this is kind of hackish,
	 * but it helps us make sure we don't have any tests that depend on a particular
	 * environment
	 */
	public static void randomizeLocale() {
//		Locale[] availableLocales = {Locale.CANADA, Locale.GERMANY, Locale.TAIWAN};
		Locale[] availableLocales = {Locale.US};
		Locale.setDefault(availableLocales[(int) (Math.random() * availableLocales.length)]);
		ourLog.info("Tests are running in locale: " + Locale.getDefault().getDisplayName());
		if (Math.random() < 0.5) {
			ourLog.info("Tests are using WINDOWS line endings and ISO-8851-1");
			System.setProperty("file.encoding", "ISO-8859-1");
			System.setProperty("line.separator", "\r\n");
		} else {
			ourLog.info("Tests are using UNIX line endings and UTF-8");
			System.setProperty("file.encoding", "UTF-8");
			System.setProperty("line.separator", "\n");
		}

		if (ourShouldRandomizeTimezones) {
			String availableTimeZones[] = {"GMT+08:00", "GMT-05:00", "GMT+00:00", "GMT+03:30"};
			String timeZone = availableTimeZones[(int) (Math.random() * availableTimeZones.length)];
			TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
		}

		ourLog.info("Tests are using time zone: {}", TimeZone.getDefault().getID());
	}


	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Wait for an atomicinteger to hit a given site and fail if it never does
	 */
	public static void waitForSize(int theTarget, AtomicInteger theInteger) {
		long start = System.currentTimeMillis();
		while (theInteger.get() != theTarget && (System.currentTimeMillis() - start) <= 15000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if ((System.currentTimeMillis() - start) >= 15000) {
			throw new IllegalStateException("Size " + theInteger.get() + " is != target " + theTarget);
		}
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Wait for an atomicinteger to hit a given site and fail if it never does
	 */
	public static void waitForSize(int theTarget, Callable<Integer> theSource) throws Exception {
		long start = System.currentTimeMillis();
		while (theSource.call() != theTarget && (System.currentTimeMillis() - start) <= 15000) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException theE) {
				throw new Error(theE);
			}
		}
		if ((System.currentTimeMillis() - start) >= 15000) {
			throw new IllegalStateException("Size " + theSource.call() + " is != target " + theTarget);
		}
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Strip \r chars from a string to account for line ending platform differences
	 */
	public static String stripReturns(String theString) {
		return defaultString(theString).replace("\r", "");
	}

	/**
	 * <b>THIS IS FOR UNIT TESTS ONLY - DO NOT CALL THIS METHOD FROM USER CODE</b>
	 * <p>
	 * Strip \r chars from a string to account for line ending platform differences
	 */
	public static String stripWhitespace(String theString) {
		return stripReturns(theString).replace(" ", "");
	}

}
