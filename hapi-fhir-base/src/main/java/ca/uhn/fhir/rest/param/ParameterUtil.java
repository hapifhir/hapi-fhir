package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;

public class ParameterUtil {

	private static final Set<Class<?>> BINDABLE_INTEGER_TYPES;
	private static final String LABEL = "label=\"";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParameterUtil.class);

	private static final String SCHEME = "scheme=\"";

	static {
		HashSet<Class<?>> intTypes = new HashSet<Class<?>>();
		intTypes.add(IntegerDt.class);
		intTypes.add(Integer.class);
		BINDABLE_INTEGER_TYPES = Collections.unmodifiableSet(intTypes);

	}

	@SuppressWarnings("unchecked")
	public static <T extends IIdType> T convertIdToType(IIdType value, Class<T> theIdParamType) {
		if (value != null && !theIdParamType.isAssignableFrom(value.getClass())) {
			IIdType newValue = ReflectionUtil.newInstance(theIdParamType);
			newValue.setValue(value.getValue());
			value = newValue;
		}
		return (T) value;
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

	// public static Integer findSinceParameterIndex(Method theMethod) {
	// return findParamIndex(theMethod, Since.class);
	// }

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

	public static String parseETagValue(String value) {
		String eTagVersion;
		value = value.trim();
		if (value.length() > 1) {
			if (value.charAt(value.length() - 1) == '"') {
				if (value.charAt(0) == '"') {
					eTagVersion = value.substring(1, value.length() - 1);
				} else if (value.length() > 3 && value.charAt(0) == 'W' && value.charAt(1) == '/'
						&& value.charAt(2) == '"') {
					eTagVersion = value.substring(3, value.length() - 1);
				} else {
					eTagVersion = value;
				}
			} else {
				eTagVersion = value;
			}
		} else {
			eTagVersion = value;
		}
		return eTagVersion;
	}

	@Deprecated
	public static void parseTagValue(TagList tagList, String nextTagComplete) {
		StringBuilder next = new StringBuilder(nextTagComplete);
		parseTagValue(tagList, nextTagComplete, next);
	}

	@Deprecated
	private static void parseTagValue(TagList theTagList, String theCompleteHeaderValue, StringBuilder theBuffer) {
		int firstSemicolon = theBuffer.indexOf(";");
		int deleteTo;
		if (firstSemicolon == -1) {
			firstSemicolon = theBuffer.indexOf(",");
			if (firstSemicolon == -1) {
				firstSemicolon = theBuffer.length();
				deleteTo = theBuffer.length();
			} else {
				deleteTo = firstSemicolon;
			}
		} else {
			deleteTo = firstSemicolon + 1;
		}

		String term = theBuffer.substring(0, firstSemicolon);
		String scheme = null;
		String label = null;
		if (isBlank(term)) {
			return;
		}

		theBuffer.delete(0, deleteTo);
		while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
			theBuffer.deleteCharAt(0);
		}

		while (theBuffer.length() > 0) {
			boolean foundSomething = false;
			if (theBuffer.length() > SCHEME.length() && theBuffer.substring(0, SCHEME.length()).equals(SCHEME)) {
				int closeIdx = theBuffer.indexOf("\"", SCHEME.length());
				scheme = theBuffer.substring(SCHEME.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			if (theBuffer.length() > LABEL.length() && theBuffer.substring(0, LABEL.length()).equals(LABEL)) {
				int closeIdx = theBuffer.indexOf("\"", LABEL.length());
				label = theBuffer.substring(LABEL.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			// TODO: support enc2231-string as described in
			// http://tools.ietf.org/html/draft-johnston-http-category-header-02
			// TODO: support multiple tags in one header as described in
			// http://hl7.org/implement/standards/fhir/http.html#tags

			while (theBuffer.length() > 0 && (theBuffer.charAt(0) == ' ' || theBuffer.charAt(0) == ';')) {
				theBuffer.deleteCharAt(0);
			}

			if (!foundSomething) {
				break;
			}
		}

		if (theBuffer.length() > 0 && theBuffer.charAt(0) == ',') {
			theBuffer.deleteCharAt(0);
			while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
				theBuffer.deleteCharAt(0);
			}
			theTagList.add(new Tag(scheme, term, label));
			parseTagValue(theTagList, theCompleteHeaderValue, theBuffer);
		} else {
			theTagList.add(new Tag(scheme, term, label));
		}

		if (theBuffer.length() > 0) {
			ourLog.warn("Ignoring extra text at the end of " + Constants.HEADER_CATEGORY + " tag '"
					+ theBuffer.toString() + "' - Complete tag value was: " + theCompleteHeaderValue);
		}

	}

	public static IQueryParameterOr<?> singleton(final IQueryParameterType theParam, final String theParamName) {
		return new IQueryParameterOr<IQueryParameterType>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IQueryParameterType> getValuesAsQueryTokens() {
				return Collections.singletonList(theParam);
			}

			@Override
			public void setValuesAsQueryTokens(FhirContext theContext, String theParamName,
					QualifiedParamList theParameters) {
				if (theParameters.isEmpty()) {
					return;
				}
				if (theParameters.size() > 1) {
					throw new IllegalArgumentException(
							"Type " + theParam.getClass().getCanonicalName() + " does not support multiple values");
				}
				theParam.setValueAsQueryToken(theContext, theParamName, theParameters.getQualifier(),
						theParameters.get(0));
			}
		};
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
