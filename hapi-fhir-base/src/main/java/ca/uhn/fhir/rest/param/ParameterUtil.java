package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.util.UrlUtil;

public class ParameterUtil {

	private static final Set<Class<?>> BINDABLE_INTEGER_TYPES;

	static {
		HashSet<Class<?>> intTypes = new HashSet<Class<?>>();
		intTypes.add(IntegerDt.class);
		intTypes.add(Integer.class);
		BINDABLE_INTEGER_TYPES = Collections.unmodifiableSet(intTypes);

	}

	// public static Integer findSinceParameterIndex(Method theMethod) {
	// return findParamIndex(theMethod, Since.class);
	// }

	public static int nonEscapedIndexOf(String theString, char theCharacter) {
		for (int i = 0; i < theString.length(); i++) {
			if (theString.charAt(i) == theCharacter) {
				if (i == 0 || theString.charAt(i - 1) != '\\') {
					return i;
				}
			}
		}
		return -1;
	}


	public static Object fromInteger(Class<?> theType, IntegerDt theArgument) {
		if (theType.equals(IntegerDt.class)) {
			if (theArgument == null) {
				return null;
			}
			return theArgument;
		}
		if (theType.equals(Integer.class)) {
			if (theArgument == null) {
				return null;
			}
			return theArgument.getValue();
		}
		throw new IllegalArgumentException("Invalid Integer type:" + theType);
	}

	public static Set<Class<?>> getBindableIntegerTypes() {
		return BINDABLE_INTEGER_TYPES;
	}


	public static IntegerDt toInteger(Object theArgument) {
		if (theArgument instanceof IntegerDt) {
			return (IntegerDt) theArgument;
		}
		if (theArgument instanceof Integer) {
			return new IntegerDt((Integer) theArgument);
		}
		return null;
	}

	static List<String> splitParameterString(String theInput, boolean theUnescapeComponents) {
		return splitParameterString(theInput, ',', theUnescapeComponents);
	}

	static List<String> splitParameterString(String theInput, char theDelimiter, boolean theUnescapeComponents) {
		ArrayList<String> retVal = new ArrayList<String>();
		if (theInput != null) {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < theInput.length(); i++) {
				char next = theInput.charAt(i);
				if (next == theDelimiter) {
					if (i == 0) {
						b.append(next);
					} else {
						char prevChar = theInput.charAt(i - 1);
						if (prevChar == '\\') {
							b.append(next);
						} else {
							if (b.length() > 0) {
								retVal.add(b.toString());
							} else {
								retVal.add(null);
							}
							b.setLength(0);
						}
					}
				} else {
					b.append(next);
				}
			}
			if (b.length() > 0) {
				retVal.add(b.toString());
			}
		}

		if (theUnescapeComponents) {
			for (int i = 0; i < retVal.size(); i++) {
				retVal.set(i, unescape(retVal.get(i)));
			}
		}

		return retVal;
	}

	/**
	 * Escapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String escapeWithDefault(Object theValue) {
		if (theValue == null) {
			return "";
		}
		return escape(theValue.toString());
	}

	/**
	 * Escapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String escape(String theValue) {
		if (theValue == null) {
			return null;
		}
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < theValue.length(); i++) {
			char next = theValue.charAt(i);
			switch (next) {
			case '$':
			case ',':
			case '|':
			case '\\':
				b.append('\\');
				break;
			default:
				break;
			}
			b.append(next);
		}

		return b.toString();
	}

	/**
	 * Escapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String escapeAndUrlEncode(String theValue) {
		if (theValue == null) {
			return null;
		}
		
		String escaped = escape(theValue);
		return UrlUtil.escape(escaped);
	}

	/**
	 * Unescapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String unescape(String theValue) {
		if (theValue == null) {
			return theValue;
		}
		if (theValue.indexOf('\\') == -1) {
			return theValue;
		}

		StringBuilder b = new StringBuilder();

		for (int i = 0; i < theValue.length(); i++) {
			char next = theValue.charAt(i);
			if (next == '\\') {
				if (i == theValue.length() - 1) {
					b.append(next);
				} else {
					switch (theValue.charAt(i + 1)) {
					case '$':
					case ',':
					case '|':
					case '\\':
						continue;
					default:
						b.append(next);
					}
				}
			} else {
				b.append(next);
			}
		}

		return b.toString();
	}

}
