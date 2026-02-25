/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.context;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.EnumUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RuntimeChildPrimitiveEnumerationDatatypeDefinition extends RuntimeChildPrimitiveDatatypeDefinition {
	private static final Logger ourLog = LoggerFactory.getLogger(RuntimeChildPrimitiveEnumerationDatatypeDefinition.class);
	private final Map<String, String> myLowerCaseCodeToCode;
	private Object myBinder;
	private final Class<? extends Enum<?>> myEnumType;

	public RuntimeChildPrimitiveEnumerationDatatypeDefinition(
			Field theField,
			String theElementName,
			Child theChildAnnotation,
			Description theDescriptionAnnotation,
			Class<? extends IBase> theDatatype,
			Class<? extends Enum<?>> theBinderType) {
		super(theField, theElementName, theDescriptionAnnotation, theChildAnnotation, theDatatype);

		myEnumType = theBinderType;

		myLowerCaseCodeToCode = getEnumCodes(theBinderType);
	}

	@Override
	public Class<? extends Enum<?>> getBoundEnumType() {
		return myEnumType;
	}

	/**
	 * Given a code this enumeration, tries to determine if there is a case-insensitive match.
	 * This method should not be called as a part of any happy-path code and is intended as a
	 * last-resort mechanism for determining an appropriate code.
	 *
	 * @since 8.10.0
	 */
	@Nullable
	public String getCodeCaseInsensitive(@Nonnull String theCode) {
		return myLowerCaseCodeToCode.get(theCode.toLowerCase(Locale.US));
	}

	@Override
	public Object getInstanceConstructorArguments() {
		Object retVal = myBinder;
		if (retVal == null) {
			retVal = toEnumFactory(myEnumType);
			myBinder = retVal;
		}
		return retVal;
	}

	/**
	 * Given a FHIR enumeration type, returns a map of lower-case codes to their original case codes.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Map<String, String> getEnumCodes(Class theBinderType) {
		Map<String, String> lowerCaseCodeToCode = new HashMap<>();
		try {
			Method toCodeMethod = theBinderType.getMethod("toCode");
			List<Enum<?>> enumValues = EnumUtils.getEnumList(theBinderType);
			for (Enum<?> nextEnum : enumValues) {
				String code = (String) toCodeMethod.invoke(nextEnum);
				if (isNotBlank(code)) {
					lowerCaseCodeToCode.put(code.toLowerCase(Locale.US), code);
				}
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			ourLog.warn("Failed to get enum codes for {}: {}", theBinderType.getCanonicalName(), e.toString());
		}
		return lowerCaseCodeToCode;
	}

	static Object toEnumFactory(Class<?> theEnumerationType) {
		Class<?> clazz;
		String className = theEnumerationType.getName() + "EnumFactory";
		Object retVal;
		try {
			clazz = Class.forName(className);
			retVal = clazz.getConstructor().newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1694) + "Failed to instantiate " + className, e);
		}
		return retVal;
	}
}
