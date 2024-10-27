/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class ObjectUtil {

	// hide
	private ObjectUtil() {}

	/**
	 * @deprecated Just use Objects.equals() instead;
	 */
	@Deprecated(since = "6.2")
	public static boolean equals(Object object1, Object object2) {
		return Objects.equals(object1, object2);
	}

	public static <T> T requireNonNull(T obj, String message) {
		if (obj == null) throw new NullPointerException(Msg.code(1776) + message);
		return obj;
	}

	public static void requireNotEmpty(String str, String message) {
		if (StringUtils.isBlank(str)) {
			throw new IllegalArgumentException(Msg.code(1777) + message);
		}
	}

	/**
	 * Cast the object to the type using Optional.
	 * Useful for streaming with flatMap.
	 * @param theObject any object
	 * @param theClass the class to check instanceof
	 * @return Optional present if theObject is of type theClass
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> castIfInstanceof(Object theObject, Class<T> theClass) {
		if (theClass.isInstance(theObject)) {
			return Optional.of((T) theObject);
		} else {
			return Optional.empty();
		}
	}
}
