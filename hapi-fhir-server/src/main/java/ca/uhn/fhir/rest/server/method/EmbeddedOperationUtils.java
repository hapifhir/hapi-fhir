/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import jakarta.annotation.Nonnull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Common operations for any functionality that work with {@link EmbeddedOperationParams}
 */
public class EmbeddedOperationUtils {

	private EmbeddedOperationUtils() {}

	/**
	 * Validate that for a class that has a {@link EmbeddableOperationParams} its constructor hass parameters that are
	 * {@link OperationParam}.  It also validates that the fields are
	 * final.  It also takes into account Collections and generic types, as well as whether there is a source to
	 * target type conversion, such as String to ZonedDateTime.
	 *
	 * @param theParameterTypeWithOperationEmbeddedParam the class that constructor params that are
	 * annotated with {@link OperationParam}
	 * @return the constructor for the class
	 */
	static Constructor<?> validateAndGetConstructor(Class<?> theParameterTypeWithOperationEmbeddedParam) {
		final Constructor<?>[] constructors = theParameterTypeWithOperationEmbeddedParam.getConstructors();

		if (constructors.length == 0) {
			throw new ConfigurationException(String.format(
					"%sInvalid operation embedded parameters.  Class has no constructor: %s",
					Msg.code(561293645), theParameterTypeWithOperationEmbeddedParam));
		}

		if (constructors.length > 1) {
			final String error = String.format(
					"%sInvalid operation embedded parameters.  Class has more than one constructor: %s",
					Msg.code(9132164), theParameterTypeWithOperationEmbeddedParam);
			throw new ConfigurationException(error);
		}

		final Constructor<?> soleConstructor = constructors[0];

		validateConstructorArgs(soleConstructor, theParameterTypeWithOperationEmbeddedParam.getDeclaredFields());

		return soleConstructor;
	}

	/**
	 * Checks to see if the constructor in question has any parameters that should be subject to a parameter conversion
	 * from a source type to a target type.  This is currently only supported for String to ZonedDateTime.
	 *
	 * @param theMethodParamsWithoutRequestDetails method parameters without the request details
	 * @param theConstructorParameters constructor parameters for the embedded params class
	 * @param theAnnotations the annotations on the constructor params
	 * @return true if this is an expected source type conversion
	 */
	static boolean hasAnyValidSourceTypeConversions(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations) {
		if (theMethodParamsWithoutRequestDetails.length != theAnnotations.length
				|| theMethodParamsWithoutRequestDetails.length != theConstructorParameters.length) {
			// This is probably an error but not this method's responsibility to check
			return false;
		}

		return IntStream.range(0, theMethodParamsWithoutRequestDetails.length)
				.mapToObj(index -> isValidSourceTypeConversion(
						theMethodParamsWithoutRequestDetails, theConstructorParameters, theAnnotations, index))
				.anyMatch(Boolean::booleanValue);
	}

	/**
	 * Indicate whether or not this is currently a supported type conversion
	 * We currently only support converting from a String to a ZonedDateTime
	 *
	 * @param theSourceType The source type for the class, which can be different from the declared type
	 * @param theTargetType The target type for the class, which can be different from the source type
	 * @param theOperationParameterRangeType Whether the embedded parameter is a range and if so, start or end
	 * @return true if the type conversion is supported
	 */
	static boolean isValidSourceTypeConversion(
			Class<?> theSourceType,
			Class<?> theTargetType,
			OperationParameterRangeType theOperationParameterRangeType) {
		return String.class == theSourceType
				&& ZonedDateTime.class == theTargetType
				&& OperationParameterRangeType.NOT_APPLICABLE != theOperationParameterRangeType;
	}

	static List<Class<?>> getMethodParamsAnnotatedWithEmbeddableOperationParams(Method theMethod) {
		return Arrays.stream(theMethod.getParameterTypes())
				.filter(EmbeddedOperationUtils::hasEmbeddableOperationParamsAnnotation)
				.collect(Collectors.toUnmodifiableList());
	}

	static boolean typeHasNoEmbeddableOperationParamsAnnotation(Class<?> theType) {
		return !hasEmbeddableOperationParamsAnnotation(theType);
	}

	private static boolean hasEmbeddableOperationParamsAnnotation(Class<?> theType) {
		final Annotation[] annotations = theType.getAnnotations();

		if (annotations.length == 0) {
			return false;
		}

		final List<EmbeddableOperationParams> embeddableOperationParams = Arrays.stream(annotations)
				.filter(EmbeddableOperationParams.class::isInstance)
				.map(EmbeddableOperationParams.class::cast)
				.collect(Collectors.toUnmodifiableList());

		if (embeddableOperationParams.isEmpty()) {
			return false;
		}

		if (annotations.length > 1) {
			throw new ConfigurationException(String.format(
					"%sInvalid operation embedded parameters.  Class has more than one annotation: %s",
					Msg.code(9132164), theType));
		}

		return true;
	}

	@Nonnull
	private static Boolean isValidSourceTypeConversion(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations,
			int theIndex) {
		final Object methodParam = theMethodParamsWithoutRequestDetails[theIndex];

		if (methodParam == null) {
			return false;
		}

		final Class<?> methodParamClass = methodParam.getClass();
		final Class<?> constructorParamType = theConstructorParameters[theIndex].getType();
		final Annotation annotation = theAnnotations[theIndex];

		if (annotation instanceof OperationParam) {
			final OperationParam operationParam = (OperationParam) annotation;
			final OperationParameterRangeType operationParameterRangeType = operationParam.rangeType();

			if (isValidSourceTypeConversion(methodParamClass, constructorParamType, operationParameterRangeType)) {
				return true;
			}
		}

		return false;
	}

	private static void validateConstructorArgs(Constructor<?> theConstructor, Field[] theDeclaredFields) {
		final Parameter[] constructorParameters = theConstructor.getParameters();
		final Class<?>[] constructorParameterTypes = theConstructor.getParameterTypes();

		if (constructorParameterTypes.length != theDeclaredFields.length) {
			final String error = String.format(
					"%sInvalid operation embedded parameters.  Constructor parameter count does not match field count: %s",
					Msg.code(42374927), theConstructor);
			throw new ConfigurationException(error);
		}

		final Type[] constructorGenericParameterTypes = theConstructor.getGenericParameterTypes();

		for (int index = 0; index < constructorParameters.length; index++) {
			final Parameter constructorParameterAtIndex = constructorParameters[index];
			final Class<?> constructorParameterTypeAtIndex = constructorParameterTypes[index];

			final Annotation[] constructorParamAnnotations = constructorParameterAtIndex.getAnnotations();

			if (constructorParamAnnotations.length < 1) {
				throw new ConfigurationException(String.format(
						"%sNo annotations for constructor class: %s param: %s",
						Msg.code(9999926), theConstructor.getDeclaringClass(), constructorParameterAtIndex));
			}

			if (constructorParamAnnotations.length > 1) {
				throw new ConfigurationException(String.format(
						"%sMore than one annotation for constructor: %s param: %s ",
						Msg.code(999998), theConstructor, constructorParameterTypeAtIndex));
			}

			final Field declaredFieldAtIndex = theDeclaredFields[index];
			final Class<?> fieldTypeAtIndex = declaredFieldAtIndex.getType();

			if (!Modifier.isFinal(declaredFieldAtIndex.getModifiers())) {
				final String error = String.format(
						"%sInvalid operation embedded parameters.  All fields must be final for class: %s",
						Msg.code(87421741), theConstructor.getDeclaringClass());
				throw new ConfigurationException(error);
			}

			if (constructorParameterTypeAtIndex != fieldTypeAtIndex) {
				final String error = String.format(
						"%sInvalid operation embedded parameters.  Constructor parameter type does not match field type: %s",
						Msg.code(87421741), theConstructor.getDeclaringClass());
				throw new ConfigurationException(error);
			}

			if (Collection.class.isAssignableFrom(constructorParameterTypeAtIndex)
					&& Collection.class.isAssignableFrom(fieldTypeAtIndex)) {
				final Type constructorGenericParameterType = constructorGenericParameterTypes[index];
				final Type fieldGenericType = declaredFieldAtIndex.getGenericType();

				validateGenericTypes(
						constructorGenericParameterType, fieldGenericType, theConstructor.getDeclaringClass());
			}
		}
	}

	private static void validateGenericTypes(
			Type theConstructorParameterType, Type theFieldType, Class<?> theDeclaringClass) {
		if (theConstructorParameterType instanceof ParameterizedType && theFieldType instanceof ParameterizedType) {
			final ParameterizedType parameterizedParameterType = (ParameterizedType) theConstructorParameterType;
			final ParameterizedType parameterizedFieldType = (ParameterizedType) theFieldType;

			final Type[] parameterTypeArguments = parameterizedParameterType.getActualTypeArguments();
			final Type[] fieldTypeArguments = parameterizedFieldType.getActualTypeArguments();

			if (parameterTypeArguments.length != fieldTypeArguments.length) {
				final String error =
						String.format("Generic type argument count does not match: for class: %s", theDeclaringClass);
				throw new ConfigurationException(error);
			}

			for (int index = 0; index < parameterTypeArguments.length; index++) {
				final Type parameterTypeArgumentAtIndex = parameterTypeArguments[index];
				final Type fieldTypeArgumentAtIndex = fieldTypeArguments[index];

				if (!parameterTypeArgumentAtIndex.equals(fieldTypeArgumentAtIndex)) {
					final String error = String.format(
							"Generic type argument does not match constructor: %s, field: %s for class: %s",
							parameterTypeArgumentAtIndex, fieldTypeArgumentAtIndex, theDeclaringClass);
					throw new ConfigurationException(error);
				}
			}
		} else {
			final String error = String.format(
					"Constructor parameter: %s or field: %s is not parameterized for class: %s",
					theConstructorParameterType, theFieldType, theDeclaringClass);
			throw new ConfigurationException(error);
		}
	}
}
