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
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

/**
 * Responsible for either passing to objects params straight through to the method call or converting them to
 * fit within a class that has constructor parameters annotated with {@link OperationParam} and to also handle placement
 * of {@link RequestDetails} in those params
 */
class BaseMethodBindingMethodParameterBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilder.class);

	private final StringTimePeriodHandler myStringTimePeriodHandler = new StringTimePeriodHandler(ZoneOffset.UTC);

	private final Method myMethod;
	private final RequestDetails myRequestDetails;
	private final Object[] myInputMethodParams;
	private final Object[] myOutputMethodParams;

	BaseMethodBindingMethodParameterBuilder(
			Method theMethod, RequestDetails theRequestDetails, Object[] theInputMethodParams) {
		myMethod = theMethod;
		myRequestDetails = theRequestDetails;
		myInputMethodParams = theInputMethodParams;
		myOutputMethodParams = initMethodParams();
	}

	public Object[] build() {
		return myOutputMethodParams;
	}

	private Object[] initMethodParams() {
		try {
			return tryBuildMethodParams();
		} catch (InvocationTargetException
				| IllegalAccessException
				| InstantiationException
				| ConfigurationException exception) {
			throw new InternalErrorException(
					String.format("%sError building method params: %s", Msg.code(234198928), exception.getMessage()),
					exception);
		}
	}

	private Object[] tryBuildMethodParams()
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		if (myMethod == null || myInputMethodParams == null) {
			throw new InternalErrorException(String.format(
					"%s Either theMethod: %s or theMethodParams: %s is null",
					Msg.code(234198927), myMethod, Arrays.toString(myInputMethodParams)));
		}

		ourLog.info(
				"1234: START building for method: {}, requestDetails: {}, inputMethodParams: {}",
				myMethod.getName(),
				myRequestDetails,
				Arrays.toString(myInputMethodParams));

		final List<Class<?>> parameterTypesWithOperationEmbeddedParams =
				EmbeddedOperationUtils.getMethodParamsAnnotatedWithEmbeddableOperationParams(myMethod);

		if (parameterTypesWithOperationEmbeddedParams.size() > 1) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation embedded parameters.  More than a single such class is part of method definition: %s",
					Msg.code(924469634), myMethod.getName()));
		}

		if (parameterTypesWithOperationEmbeddedParams.isEmpty()) {
			return myInputMethodParams;
		}

		final Class<?>[] methodParameterTypes = myMethod.getParameterTypes();
		final String methodName = myMethod.getName();

		if (Arrays.stream(methodParameterTypes)
						.filter(RequestDetails.class::isAssignableFrom)
						.count()
				> 1) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation with embedded parameters.  Cannot have more than one RequestDetails: %s",
					Msg.code(924469635), methodName));
		}

		final long numRequestDetails = Arrays.stream(methodParameterTypes)
				.filter(RequestDetails.class::isAssignableFrom)
				.count();

		if (numRequestDetails == 0 && methodParameterTypes.length > 1) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation with embedded parameters.  Cannot have more than 1 params and no RequestDetails: %s",
					Msg.code(924469634), methodName));
		}

		if (numRequestDetails > 0 && methodParameterTypes.length > 2) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation with embedded parameters.  Cannot have more than 2 params and a RequestDetails: %s",
					Msg.code(924469634), methodName));
		}

		final Class<?> parameterTypeWithOperationEmbeddedParams = parameterTypesWithOperationEmbeddedParams.get(0);

		return determineMethodParamsForOperationEmbeddedParams(parameterTypeWithOperationEmbeddedParams);
	}

	private Object[] determineMethodParamsForOperationEmbeddedParams(
			Class<?> theParameterTypeWithOperationEmbeddedParams)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		final String methodName = myMethod.getName();

		ourLog.info(
				"1234: invoking parameterTypeWithOperationEmbeddedParams: {} and theMethod: {}",
				theParameterTypeWithOperationEmbeddedParams,
				methodName);

		final Object operationEmbeddedType =
				buildOperationEmbeddedObject(theParameterTypeWithOperationEmbeddedParams, myInputMethodParams);

		ourLog.info(
				"1234: build method params with embedded object and requestDetails (if applicable) for: {}",
				operationEmbeddedType);

		final Object[] params = buildMethodParamsInCorrectPositions(operationEmbeddedType);

		ourLog.info(
				"1234: END: method: {}, requestDetails: {}, inputMethodParams: {}, outputMethodParams: {}",
				myMethod.getName(),
				myRequestDetails,
				Arrays.toString(myInputMethodParams),
				Arrays.toString(params));

		return params;
	}

	@Nonnull
	private Object buildOperationEmbeddedObject(
			Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		final Constructor<?> constructor =
				EmbeddedOperationUtils.validateAndGetConstructor(theParameterTypeWithOperationEmbeddedParam);

		final Object[] methodParamsWithoutRequestDetails = cloneWithRemovedRequestDetails(theMethodParams);

		final Annotation[] constructorAnnotations = constructor.getAnnotations();

		final Parameter[] constructorParameters = validateAndGetConstructorParameters(constructor);

		validMethodParamTypes(methodParamsWithoutRequestDetails, constructorParameters);

		final Object[] convertedParams =
				convertParamsIfNeeded(methodParamsWithoutRequestDetails, constructorParameters, constructorAnnotations);

		return constructor.newInstance(convertedParams);
	}

	private Object[] convertParamsIfNeeded(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations) {

		final Annotation[] annotations = Arrays.stream(theConstructorParameters)
				.map(Parameter::getAnnotations)
				.filter(array -> array.length == 1)
				.map(array -> array[0])
				.toArray(Annotation[]::new);

		if (!EmbeddedOperationUtils.hasAnyValidSourceTypeConversions(
				theMethodParamsWithoutRequestDetails, theConstructorParameters, annotations)) {
			return theMethodParamsWithoutRequestDetails;
		}

		return IntStream.range(0, theMethodParamsWithoutRequestDetails.length)
				.mapToObj(index -> convertParamIfNeeded(
						theMethodParamsWithoutRequestDetails, theConstructorParameters, index))
				.toArray(Object[]::new);
	}

	@Nullable
	private Object convertParamIfNeeded(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			int theIndex) {

		final Object paramAtIndex = theMethodParamsWithoutRequestDetails[theIndex];

		if (paramAtIndex == null) {
			return paramAtIndex;
		}

		final Parameter constructorParameter = theConstructorParameters[theIndex];
		// Ne already validated that there is at least one annotation earlier
		final Annotation annotation = constructorParameter.getAnnotations()[0];

		if (!(annotation instanceof OperationParam)) {
			return paramAtIndex;
		}

		final OperationParam operationParamAtIndex = (OperationParam) annotation;
		final Class<?> paramClassAtIndex = paramAtIndex.getClass();
		final OperationParameterRangeType rangeType = operationParamAtIndex.rangeType();
		final Class<?> constructorParameterType = constructorParameter.getType();

		if (EmbeddedOperationUtils.isValidSourceTypeConversion(
				paramClassAtIndex, constructorParameterType, rangeType)) {
			final String paramAtIndexAsString = (String) paramAtIndex;
			switch (rangeType) {
				case START:
					return myStringTimePeriodHandler.getStartZonedDateTime(paramAtIndexAsString, myRequestDetails);
				case END:
					return myStringTimePeriodHandler.getEndZonedDateTime(paramAtIndexAsString, myRequestDetails);
				default:
					// This should never happen
					throw new IllegalArgumentException(Msg.code(217312) + "Invalid range type: " + rangeType);
			}
		} else {
			return paramAtIndex;
		}
	}

	@Nonnull
	private static Parameter[] validateAndGetConstructorParameters(Constructor<?> constructor) {
		final Parameter[] constructorParameters = constructor.getParameters();

		if (constructorParameters.length == 0) {
			throw new InternalErrorException(Msg.code(234198927) + "No constructor that takes parameters!!!");
		}
		return constructorParameters;
	}

	// RequestDetails must be dealt with separately because there is no such concept in clinical-reasoning and the
	// operation params classes must be defined in that project
	@Nonnull
	private Object[] buildMethodParamsInCorrectPositions(Object operationEmbeddedType) {

		final List<RequestDetails> requestDetailsMultiple = Arrays.stream(myInputMethodParams)
				.filter(RequestDetails.class::isInstance)
				.map(RequestDetails.class::cast)
				.collect(Collectors.toUnmodifiableList());

		if (requestDetailsMultiple.size() > 1) {
			final String error = String.format(
					"%sCannot define a request with more than one RequestDetails for method: %s",
					Msg.code(562462), myMethod.getName());

			throw new InternalErrorException(error);
		}

		if (requestDetailsMultiple.isEmpty()) {
			// No RequestDetails at all
			return new Object[] {operationEmbeddedType};
		}

		// Don't try to get cute and use the RequestDetails field:  just grab it from the params
		final RequestDetails requestDetails = requestDetailsMultiple.get(0);

		final int indexOfRequestDetails = Arrays.asList(myInputMethodParams).indexOf(requestDetails);

		if (indexOfRequestDetails == 0) {
			// RequestDetails goes first
			return new Object[] {requestDetails, operationEmbeddedType};
		}

		// RequestDetails goes last
		return new Object[] {operationEmbeddedType, requestDetails};
	}

	private void validMethodParamTypes(
			Object[] theMethodParamsWithoutRequestDetails, Parameter[] theConstructorParameters) {

		if (theMethodParamsWithoutRequestDetails.length != theConstructorParameters.length) {
			final String error = String.format(
					"%sMismatch between length of non-RequestedDetails params: %s and constructor params: %s for method: %s",
					Msg.code(234198921),
					theMethodParamsWithoutRequestDetails.length,
					theConstructorParameters.length,
					myMethod.getName());
			throw new InternalErrorException(error);
		}

		for (int index = 0; index < theMethodParamsWithoutRequestDetails.length; index++) {
			validateMethodParamType(
					theMethodParamsWithoutRequestDetails[index],
					theConstructorParameters[index]);
		}
	}

	private void validateMethodParamType(Object theMethodParam, Parameter theConstructorParameter) {

		if (theMethodParam == null) {
			// argument is null, so we can't the type, so skip it:
			return;
		}

		final Class<?> methodParamClass = theMethodParam.getClass();

		final Optional<OperationParam> optOperationEmbeddedParam =
			Optional.ofNullable(theConstructorParameter.getAnnotation(OperationParam.class));

		optOperationEmbeddedParam.ifPresent(embeddedParam -> {
			if (embeddedParam.sourceType() != Void.class && methodParamClass != embeddedParam.sourceType()) {
				final String error = String.format(
						"%sMismatch between methodParamClass: %s and OperationEmbeddedParam source type: %s for method: %s",
						Msg.code(4313421), methodParamClass, embeddedParam.sourceType(), myMethod.getName());
				throw new InternalErrorException(error);
			}
		});

		final Class<?> parameterType = theConstructorParameter.getType();

		if (Collection.class.isAssignableFrom(methodParamClass)
				|| Collection.class.isAssignableFrom(parameterType)) {
			// ex:  List and ArrayList
			if (methodParamClass.isAssignableFrom(parameterType)) {
				final String error = String.format(
						"%sMismatch between methodParamClass: %s and parameterClassAtIndex: %s for method: %s",
						Msg.code(236146124), methodParamClass, parameterType, myMethod.getName());

				throw new InternalErrorException(error);
			}
			// Ex:  Field is declared as an IIdType, but argument is an IdDt
			// or supported type conversion: String to ZonedDateTime
		} else if (!parameterType.isAssignableFrom(methodParamClass)
				&& !optOperationEmbeddedParam
						.map(embeddedParam -> EmbeddedOperationUtils.isValidSourceTypeConversion(
								methodParamClass, parameterType, embeddedParam.rangeType()))
						.orElse(false)) {
			final String error = String.format(
					"%sMismatch between methodParamClass: %s and parameterClassAtIndex: %s for method: %s",
					Msg.code(236146125), methodParamClass, parameterType, myMethod.getName());
			throw new InternalErrorException(error);
		}
	}

	// LUKETODO:  code reuse?
	private static Object[] cloneWithRemovedRequestDetails(Object[] theMethodParams) {
		return Arrays.stream(theMethodParams)
				.filter(not(RequestDetails.class::isInstance).and(not(SystemRequestDetails.class::isInstance)))
				.toArray();
	}
}
