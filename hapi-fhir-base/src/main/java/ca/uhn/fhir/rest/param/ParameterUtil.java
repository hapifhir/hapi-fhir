package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class ParameterUtil {

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
	 * Removes :modifiers and .chains from URL parameter names
	 */
	public static String stripModifierPart(String theParam) {
		for (int i = 0; i < theParam.length(); i++) {
			char nextChar = theParam.charAt(i);
			if (nextChar == ':' || nextChar == '.') {
				return theParam.substring(0, i);
			}
		}
		return theParam;
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
	public static String escapeWithDefault(Object theValue) {
		if (theValue == null) {
			return "";
		}
		return escape(theValue.toString());
	}

	/**
	 * Applies {@link #escapeWithDefault(Object)} followed by {@link UrlUtil#escapeUrlParam(String)}
	 */
	public static String escapeAndUrlEncode(String theInput) {
		return UrlUtil.escapeUrlParam(escapeWithDefault(theInput));
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
				throw new ConfigurationException(Msg.code(1936) + "Method uses the wrong Id datatype (IdDt / IdType) for the given context FHIR version: " + theMethod.toString());
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
			for (Annotation nextAnnotation : annotations) {
				Class<? extends Annotation> class1 = nextAnnotation.annotationType();
				if (toFind.isAssignableFrom(class1)) {
					return paramIndex;
				}
			}
			paramIndex++;
		}
		return null;
	}

	public static Object fromInteger(Class<?> theType, IntegerDt theArgument) {
		if (theArgument == null) {
			return null;
		}
		if (theType.equals(Integer.class)) {
			return theArgument.getValue();
		}
		IPrimitiveType<?> retVal = (IPrimitiveType<?>) ReflectionUtil.newInstance(theType);
		retVal.setValueAsString(theArgument.getValueAsString());
		return retVal;
	}

	public static boolean isBindableIntegerType(Class<?> theClass) {
		return Integer.class.isAssignableFrom(theClass)
			|| IPrimitiveType.class.isAssignableFrom(theClass);
	}

	public static String escapeAndJoinOrList(Collection<String> theValues) {
		return theValues
			.stream()
			.map(ParameterUtil::escape)
			.collect(Collectors.joining(","));
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
					throw new IllegalArgumentException(Msg.code(1937) + "Type " + theParam.getClass().getCanonicalName() + " does not support multiple values");
				}
				theParam.setValueAsQueryToken(theContext, theParamName, theParameters.getQualifier(),
					theParameters.get(0));
			}
		};
	}

	static List<String> splitParameterString(String theInput, char theDelimiter, boolean theUnescapeComponents) {
		ArrayList<String> retVal = new ArrayList<>();
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
		if (theArgument instanceof IPrimitiveType) {
			IPrimitiveType<?> pt = (IPrimitiveType<?>) theArgument;
			return new IntegerDt(pt.getValueAsString());
		}
		return null;
	}

	/**
	 * Unescapes a string according to the rules for parameter escaping specified in the <a href="http://www.hl7.org/implement/standards/fhir/search.html#escaping">FHIR Specification Escaping
	 * Section</a>
	 */
	public static String unescape(String theValue) {
		if (theValue == null) {
			return null;
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

	/**
	 * Returns true if the value is :iterate or :recurse (the former name of :iterate) for an _include parameter
	 */
	public static boolean isIncludeIterate(String theQualifier) {
		return Constants.PARAM_INCLUDE_QUALIFIER_RECURSE.equals(theQualifier) || Constants.PARAM_INCLUDE_QUALIFIER_ITERATE.equals(theQualifier);
	}
}
