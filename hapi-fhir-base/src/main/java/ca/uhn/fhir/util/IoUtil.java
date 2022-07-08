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

public class IoUtil {

	/**
	 * Replacement for the deprecated commons-lang method of the same name. Use sparingly
	 * since they are right that most uses of this should be replaced with try-with-resources
	 */
	public static void closeQuietly(final AutoCloseable theCloseable) {
		try {
			if (theCloseable != null) {
				theCloseable.close();
			}
		} catch (final Exception ioe) {
			// ignore
		}
	}

}
