/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.system;

import java.util.concurrent.TimeUnit;

public final class HapiSystemProperties {
	static final String SUPPRESS_HAPI_FHIR_VERSION_LOG = "suppress_hapi_fhir_version_log";
	static final String DISABLE_STATUS_BASED_REINDEX = "disable_status_based_reindex";
	/**
	 * This is provided for testing only! Use with caution as this property may change.
	 */
	static final String VALIDATION_RESOURCE_CACHE_TIMEOUT_MILLIS = "VALIDATION_RESOURCE_CACHE_EXPIRY_MS";

	static final String UNIT_TEST_CAPTURE_STACK = "unit_test_capture_stack";
	static final String STACKFILTER_PATTERN_PROP = "log.stackfilter.pattern";
	static final String HAPI_CLIENT_KEEPRESPONSES = "hapi.client.keepresponses";
	static final String TEST_MODE = "test";
	static final String UNIT_TEST_MODE = "unit_test_mode";
	static final long DEFAULT_VALIDATION_RESOURCE_CACHE_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(10);
	static final String PREVENT_INVALIDATING_CONDITIONAL_MATCH_CRITERIA =
			"hapi.storage.prevent_invalidating_conditional_match_criteria";

	private HapiSystemProperties() {}

	/**
	 * This property is used by unit tests - do not rely on it in production code
	 * as it may change at any time. If you want to capture responses in a reliable
	 * way in your own code, just use client interceptors
	 */
	public static void enableHapiClientKeepResponses() {
		System.setProperty(HAPI_CLIENT_KEEPRESPONSES, Boolean.TRUE.toString());
	}

	public static void disableHapiClientKeepResponses() {
		System.clearProperty(HAPI_CLIENT_KEEPRESPONSES);
	}

	/**
	 * This property is used by unit tests - do not rely on it in production code
	 * as it may change at any time. If you want to capture responses in a reliable
	 * way in your own code, just use client interceptors
	 */
	public static boolean isHapiClientKeepResponsesEnabled() {
		return (Boolean.parseBoolean(System.getProperty(HAPI_CLIENT_KEEPRESPONSES)));
	}

	/**
	 * This property gets used in the logback.xml file.
	 * It causes logged stack traces to skip a number of packages that are
	 * just noise.
	 */
	public static void setStackFilterPattern(String thePattern) {
		System.setProperty(STACKFILTER_PATTERN_PROP, thePattern);
	}

	/**
	 * Set the validation resource cache expireAfterWrite timeout in milliseconds
	 *
	 * @param theMillis the timeout value to set (in milliseconds)
	 */
	public static void setValidationResourceCacheTimeoutMillis(long theMillis) {
		System.setProperty(VALIDATION_RESOURCE_CACHE_TIMEOUT_MILLIS, "" + theMillis);
	}

	/**
	 * Get the validation resource cache expireAfterWrite timeout in milliseconds.  If it has not been set, the default
	 * value is 10 seconds.
	 */
	public static long getValidationResourceCacheTimeoutMillis() {
		String property = System.getProperty(VALIDATION_RESOURCE_CACHE_TIMEOUT_MILLIS);
		if (property == null) {
			return DEFAULT_VALIDATION_RESOURCE_CACHE_TIMEOUT_MILLIS;
		}
		return Long.parseLong(property);
	}

	/**
	 * When this property is primarily used to control application shutdown behavior
	 */
	public static void enableTestMode() {
		System.setProperty(TEST_MODE, Boolean.TRUE.toString());
	}

	public static boolean isTestModeEnabled() {
		return Boolean.parseBoolean(System.getProperty(TEST_MODE));
	}

	/**
	 * This property is used to ensure unit test behaviour is deterministic.
	 */
	public static void enableUnitTestMode() {
		System.setProperty(UNIT_TEST_MODE, Boolean.TRUE.toString());
	}

	public static void disableUnitTestMode() {
		System.setProperty(UNIT_TEST_MODE, Boolean.FALSE.toString());
	}

	public static boolean isUnitTestModeEnabled() {
		return Boolean.parseBoolean(System.getProperty(UNIT_TEST_MODE));
	}

	/**
	 * This property prevents stack traces from getting truncated and includes the full stack trace in failed search responses.
	 */
	public static void enableUnitTestCaptureStack() {
		System.setProperty(UNIT_TEST_CAPTURE_STACK, Boolean.TRUE.toString());
	}

	public static void disableUnitTestCaptureStack() {
		System.clearProperty(HapiSystemProperties.UNIT_TEST_CAPTURE_STACK);
	}

	public static boolean isUnitTestCaptureStackEnabled() {
		return Boolean.parseBoolean(System.getProperty(HapiSystemProperties.UNIT_TEST_CAPTURE_STACK));
	}

	public static boolean isDisableStatusBasedReindex() {
		return Boolean.parseBoolean(System.getProperty(DISABLE_STATUS_BASED_REINDEX));
	}

	public static void disableStatusBasedReindex() {
		System.setProperty(DISABLE_STATUS_BASED_REINDEX, Boolean.TRUE.toString());
	}

	/**
	 * This property sets {@link JpaStorageSettings#setStatusBasedReindexingDisabled(Boolean)} to true when the system starts up.
	 */
	public static void enableStatusBasedReindex() {
		System.clearProperty(DISABLE_STATUS_BASED_REINDEX);
	}

	/**
	 * This property is used to suppress the logging of the HAPI FHIR version on startup.
	 */
	// TODO KHS use this in cdr
	public static void enableSuppressHapiFhirVersionLog() {
		System.setProperty(SUPPRESS_HAPI_FHIR_VERSION_LOG, Boolean.TRUE.toString());
	}

	public static boolean isSuppressHapiFhirVersionLogEnabled() {
		return Boolean.parseBoolean(System.getProperty(SUPPRESS_HAPI_FHIR_VERSION_LOG));
	}

	public static boolean isPreventInvalidatingConditionalMatchCriteria() {
		return Boolean.parseBoolean(System.getProperty(
				HapiSystemProperties.PREVENT_INVALIDATING_CONDITIONAL_MATCH_CRITERIA, Boolean.FALSE.toString()));
	}
}
