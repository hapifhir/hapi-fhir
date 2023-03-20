/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatatypeUtil {

	/**
	 * Convert a list of FHIR String objects to a set of native java Strings
	 */
	public static Set<String> toStringSet(List<? extends IPrimitiveType<?>> theStringList) {
		HashSet<String> retVal = new HashSet<>();
		if (theStringList != null) {
			for (IPrimitiveType<?> string : theStringList) {
				if (string != null && string.getValue() != null) {
					retVal.add(string.getValueAsString());
				}
			}
		}
		return retVal;
	}

	/**
	 * Joins a list of strings with a single space (' ') between each string
	 */
	public static String joinStringsSpaceSeparated(List<? extends IPrimitiveType<String>> theStrings) {
		StringBuilder b = new StringBuilder();
		for (IPrimitiveType<String> next : theStrings) {
			if (next.isEmpty()) {
				continue;
			}
			if (b.length() > 0) {
				b.append(' ');
			}
			b.append(next.getValue());
		}
		return b.toString();
	}

	/**
	 * Returns {@link IPrimitiveType#getValueAsString()} if <code>thePrimitiveType</code> is
	 * not null, else returns null.
	 */
	public static String toStringValue(IPrimitiveType<?> thePrimitiveType) {
		return thePrimitiveType != null ? thePrimitiveType.getValueAsString() : null;
	}

	/**
	 * Returns {@link IPrimitiveType#getValue()} if <code>thePrimitiveType</code> is
	 * not null, else returns null.
	 */
	public static Boolean toBooleanValue(IPrimitiveType<Boolean> thePrimitiveType) {
		return thePrimitiveType != null ? thePrimitiveType.getValue() : null;
	}
}
