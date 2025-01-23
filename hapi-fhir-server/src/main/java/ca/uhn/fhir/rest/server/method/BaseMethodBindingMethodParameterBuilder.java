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
import ca.uhn.fhir.rest.annotation.EmbeddedParameterRangeType;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

// LUKETODO:  should this be responsible for invoking the method as well?
/**
 * Responsible for either passing to objects params straight through to the method call or converting them to
 * fit within a class that has fields annotated with {@link OperationEmbeddedParam} and to also handle placement
 * of {@link RequestDetails} in those params
 */
class BaseMethodBindingMethodParameterBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilder.class);

	// LUKETODO: constructor param or something?
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

		final List<Class<?>> parameterTypesWithOperationEmbeddedParam =
				ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(
						myMethod, OperationEmbeddedParam.class);

		if (parameterTypesWithOperationEmbeddedParam.size() > 1) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation embedded parameters.  More than a single such class is part of method definition: %s",
					Msg.code(924469634), myMethod.getName()));
		}

		if (parameterTypesWithOperationEmbeddedParam.isEmpty()) {
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

		final Class<?> parameterTypeWithOperationEmbeddedParam = parameterTypesWithOperationEmbeddedParam.get(0);

		return determineMethodParamsForOperationEmbeddedParams(parameterTypeWithOperationEmbeddedParam);
	}

	private Object[] determineMethodParamsForOperationEmbeddedParams(
			Class<?> theParameterTypeWithOperationEmbeddedParam)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		final String methodName = myMethod.getName();

		ourLog.info(
				"1234: invoking parameterTypeWithOperationEmbeddedParam: {} and theMethod: {}",
				theParameterTypeWithOperationEmbeddedParam,
				methodName);

		final Object operationEmbeddedType = buildOperationEmbeddedObject(
				methodName, theParameterTypeWithOperationEmbeddedParam, myInputMethodParams);

		ourLog.info(
				"1234: build method params with embedded object and requestDetails (if applicable) for: {}",
				operationEmbeddedType);

		return buildMethodParamsInCorrectPositions(operationEmbeddedType);
	}

	@Nonnull
	private Object buildOperationEmbeddedObject(
			String theMethodName, Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		final Constructor<?> constructor =
				EmbeddedOperationUtils.validateAndGetConstructor(theParameterTypeWithOperationEmbeddedParam);

		final Object[] methodParamsWithoutRequestDetails = cloneWithRemovedRequestDetails(theMethodParams);

		final Annotation[] annotations = Arrays.stream(theParameterTypeWithOperationEmbeddedParam.getDeclaredFields())
				.map(AccessibleObject::getAnnotations)
				.filter(array -> array.length == 1)
				.flatMap(Arrays::stream)
				.toArray(Annotation[]::new);

		if (methodParamsWithoutRequestDetails.length != constructor.getParameterCount()) {
			final String error = String.format(
					"%smismatch between constructor args: %s and non-request details parameter args: %s",
					Msg.code(475326592),
					Arrays.toString(constructor.getParameterTypes()),
					Arrays.toString(methodParamsWithoutRequestDetails));
			throw new InternalErrorException(error);
		}

		if (methodParamsWithoutRequestDetails.length != annotations.length) {
			final String error = String.format(
					"%smismatch between non-request details parameter args: %s and number of annotations: %s",
					Msg.code(475326593),
					Arrays.toString(methodParamsWithoutRequestDetails),
					Arrays.toString(annotations));
			throw new InternalErrorException(error);
		}

		final Parameter[] constructorParameters = validateAndGetConstructorParameters(constructor);

		validMethodParamTypes(methodParamsWithoutRequestDetails, constructorParameters, annotations);

		final Object[] convertedParams =
				convertParamsIfNeeded(methodParamsWithoutRequestDetails, constructorParameters, annotations);

		return constructor.newInstance(convertedParams);
	}

	private Object[] convertParamsIfNeeded(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations) {
		// LUKETODO:  rangetype check?
		// LUKETODO: warnings?
		if (Arrays.stream(theMethodParamsWithoutRequestDetails).noneMatch(String.class::isInstance)
				&& Arrays.stream(theAnnotations)
						.filter(OperationEmbeddedParam.class::isInstance)
						.map(OperationEmbeddedParam.class::cast)
						.map(OperationEmbeddedParam::sourceType)
						.noneMatch(ZonedDateTime.class::isInstance)) {

			// Nothing to do:
			return theMethodParamsWithoutRequestDetails;
		}

		return IntStream.range(0, theMethodParamsWithoutRequestDetails.length)
				.mapToObj(index -> convertParamIfNeeded(
						theMethodParamsWithoutRequestDetails, theConstructorParameters, theAnnotations, index))
				.toArray(Object[]::new);
	}

	@Nullable
	private Object convertParamIfNeeded(
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations,
			int theIndex) {

		final Object paramAtIndex = theMethodParamsWithoutRequestDetails[theIndex];
		final Annotation annotation = theAnnotations[theIndex];

		if (paramAtIndex == null) {
			return paramAtIndex;
		}

		if (!(annotation instanceof OperationEmbeddedParam)) {
			return paramAtIndex;
		}

		final OperationEmbeddedParam embeddedParamAtIndex = (OperationEmbeddedParam) annotation;
		final Class<?> paramClassAtIndex = paramAtIndex.getClass();
		final EmbeddedParameterRangeType rangeType = embeddedParamAtIndex.rangeType();
		final Parameter constructorParameter = theConstructorParameters[theIndex];
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
					// LUKETODO:  message, code, etc
					throw new IllegalArgumentException();
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
			Object[] theMethodParamsWithoutRequestDetails,
			Parameter[] theConstructorParameters,
			Annotation[] theAnnotations) {

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
					theConstructorParameters[index].getType(),
					theAnnotations[index]);
		}
	}

	private void validateMethodParamType(Object theMethodParam, Class<?> theParameterClass, Annotation theAnnotation) {

		if (theMethodParam == null) {
			// argument is null, so we can't the type, so skip it:
			return;
		}

		final Class<?> methodParamClass = theMethodParam.getClass();

		//		LUKETODO:  HAPI-4313421: Mismatch between methodParamClass: class org.hl7.fhir.r4.model.IdType and
		// OperationEmbeddedParam source type: class java.lang.String for method: evaluateMeasure

		final Optional<OperationEmbeddedParam> optOperationEmbeddedParam =
				theAnnotation instanceof OperationEmbeddedParam
						? Optional.of((OperationEmbeddedParam) theAnnotation)
						: Optional.empty();

		optOperationEmbeddedParam.ifPresent(embeddedParam -> {
			// LUKETODO: is this wise?
			if (embeddedParam.sourceType() != Void.class && methodParamClass != embeddedParam.sourceType()) {
				final String error = String.format(
						"%sMismatch between methodParamClass: %s and OperationEmbeddedParam source type: %s for method: %s",
						Msg.code(4313421), methodParamClass, embeddedParam.sourceType(), myMethod.getName());
				throw new InternalErrorException(error);
			}
		});

		if (Collection.class.isAssignableFrom(methodParamClass)
				|| Collection.class.isAssignableFrom(theParameterClass)) {
			// ex:  List and ArrayList
			if (methodParamClass.isAssignableFrom(theParameterClass)) {
				final String error = String.format(
						"%sMismatch between methodParamClass: %s and parameterClassAtIndex: %s for method: %s",
						Msg.code(236146124), methodParamClass, theParameterClass, myMethod.getName());

				throw new InternalErrorException(error);
			}
			// Ex:  Field is declared as an IIdType, but argument is an IdDt
			// or supported type conversion: String to ZonedDateTime
		} else if (!theParameterClass.isAssignableFrom(methodParamClass)
				&& !optOperationEmbeddedParam
						.map(embeddedParam -> EmbeddedOperationUtils.isValidSourceTypeConversion(
								methodParamClass, theParameterClass, embeddedParam.rangeType()))
						.orElse(false)) {
			final String error = String.format(
					"%sMismatch between methodParamClass: %s and parameterClassAtIndex: %s for method: %s",
					Msg.code(236146125), methodParamClass, theParameterClass, myMethod.getName());
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
