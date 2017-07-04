package ca.uhn.fhir.rest.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.util.*;

import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.*;
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
	 * Escapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String escapeWithDefault(Object theValue) {
		if (theValue == null) {
			return "";
		}
		return escape(theValue.toString());
	}

	public static Integer findIdParameterIndex(Method theMethod, FhirContext theContext) {
		Integer index = findParamAnnotationIndex(theMethod, IdParam.class);
		if (index != null) {
			Class<?> paramType = theMethod.getParameterTypes()[index];
			if (IIdType.class.equals(paramType)) {
				return index;
			}
			boolean isRi = theContext.getVersion().getVersion().isRi();
			boolean usesHapiId = IdDt.class.equals(paramType);
			if (isRi == usesHapiId) {
				throw new ConfigurationException("Method uses the wrong Id datatype (IdDt / IdType) for the given context FHIR version: " + theMethod.toString());
			}
		}
		return index;
	}

	public static Integer findParamAnnotationIndex(Method theMethod, Class<?> toFind) {
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {
			for (int annotationIndex = 0; annotationIndex < annotations.length; annotationIndex++) {
				Annotation nextAnnotation = annotations[annotationIndex];
				Class<? extends Annotation> class1 = nextAnnotation.getClass();
				if (toFind.isAssignableFrom(class1)) {
					return paramIndex;
				}
			}
			paramIndex++;
		}
		return null;
	}

	public static Integer findTagListParameterIndex(Method theMethod) {
		return findParamAnnotationIndex(theMethod, TagListParam.class);
	}

	public static Integer findVersionIdParameterIndex(Method theMethod) {
		return findParamAnnotationIndex(theMethod, VersionIdParam.class);
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

	public static IntegerDt toInteger(Object theArgument) {
		if (theArgument instanceof IntegerDt) {
			return (IntegerDt) theArgument;
		}
		if (theArgument instanceof Integer) {
			return new IntegerDt((Integer) theArgument);
		}
		return null;
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
