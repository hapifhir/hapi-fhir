package ca.uhn.fhir.util;

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

import org.slf4j.Logger;

import java.util.Optional;

public class IoUtils {

	private IoUtils() {}

	/**
	 * Replacement for the deprecated commons-lang method of the same name. Use sparingly
	 * since they are right that most uses of this should be replaced with try-with-resources
	 */
	public static void closeQuietly(final AutoCloseable theCloseable) {
		closeQuietly(theCloseable, null);
	}
	/**
	 * Closes quietly logging exceptions if any
	 *
	 * @param theCloseable Closeable instance to be closed
	 * @param theLog       Logger to log a potential exception
	 */
	public static void closeQuietly(AutoCloseable theCloseable, Logger theLog) {
		try {
			if (theCloseable != null) {
				theCloseable.close();
			}
		} catch (Throwable tx) {
			if (theLog != null) {
				theLog.warn("Unable to close {}", theCloseable, tx);
			}
		}
	}

	/**
	 * Cast the object to the target class, returning an empty optional if it fails.
	 */
	public static <T> Optional<T> safeCast(Object theObject, Class<T> theTargetClass) {
		if (theTargetClass.isInstance(theObject)) {
			return Optional.of(theTargetClass.cast(theObject));
		}
		return Optional.empty();
	}
}
