package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

// LUKETODO:  should this be responsible for invoking the method as well?
/**
 * Responsible for either passing to objects params straight through to the method call or converting them to
 * fit within a class that has fields annotated with {@link OperationEmbeddedParam} and to also handle placement
 * of {@link RequestDetails} in those params
 */
class BaseMethodBindingMethodParameterBuilder {

	private static final Logger ourLog =
			LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilder.class);

	// LUKETODO: constructor param or something?
	private final StringTimePeriodHandler myStringTimePeriodHandler = new StringTimePeriodHandler(ZoneOffset.UTC);

	private BaseMethodBindingMethodParameterBuilder() {}

	static Object[] buildMethodParams(Method theMethod, Object[] theMethodParams, RequestDetails theRequestDetails) {
		try {
			return tryBuildMethodParams(theMethod, theMethodParams);
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | ConfigurationException exception) {
			throw new InternalErrorException(
					String.format(
							"%sError building method params: %s", Msg.code(234198928), exception.getMessage()),
					exception);
		}
	}

	static Object[] tryBuildMethodParams(Method theMethod, Object[] theMethodParams)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		if (theMethod == null || theMethodParams == null) {
			throw new InternalErrorException(String.format(
					"%s Either theMethod: %s or theMethodParams: %s is null",
					Msg.code(234198927), theMethod, Arrays.toString(theMethodParams)));
		}

		final List<Class<?>> parameterTypesWithOperationEmbeddedParam =
				ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(
						theMethod, OperationEmbeddedParam.class);

		if (parameterTypesWithOperationEmbeddedParam.size() > 1) {
			throw new InternalErrorException(String.format(
					"%sInvalid operation embedded parameters.  More than a single such class is part of method definition: %s",
					Msg.code(924469634), theMethod.getName()));
		}

		if (parameterTypesWithOperationEmbeddedParam.isEmpty()) {
			return theMethodParams;
		}

		final Class<?>[] methodParameterTypes = theMethod.getParameterTypes();
		final String methodName = theMethod.getName();

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

		return determineMethodParamsForOperationEmbeddedParams(
				theMethod, parameterTypeWithOperationEmbeddedParam, theMethodParams);
	}

	private static Object[] determineMethodParamsForOperationEmbeddedParams(
			Method theMethod, Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		final String methodName = theMethod.getName();

		ourLog.info(
				"1234: invoking parameterTypeWithOperationEmbeddedParam: {} and theMethod: {}",
				theParameterTypeWithOperationEmbeddedParam,
			methodName);

		final Object operationEmbeddedType = buildOperationEmbeddedObject(
			methodName,
			theParameterTypeWithOperationEmbeddedParam,
			theMethodParams);

		ourLog.info(
				"1234: build method params with embedded object and requestDetails (if applicable) for: {}",
				operationEmbeddedType);

		return buildMethodParamsInCorrectPositions(methodName,theMethodParams, operationEmbeddedType);
	}

	@Nonnull
	private static Object buildOperationEmbeddedObject(
		String theMethodName, Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		final Constructor<?> constructor = EmbeddedOperationUtils.validateAndGetConstructor(theParameterTypeWithOperationEmbeddedParam);

		final Object[] methodParamsWithoutRequestDetails = cloneWithRemovedRequestDetails(theMethodParams);

		validMethodParamTypes(theMethodName, methodParamsWithoutRequestDetails, validateAndGetConstructorParameters(constructor));

		if (methodParamsWithoutRequestDetails.length != constructor.getParameterCount()) {
			throw new InternalErrorException(String.format(
				"1234: mismatch between constructor args: %s and non-request details parameter args: %s",
				Arrays.toString(constructor.getParameterTypes()),
				Arrays.toString(methodParamsWithoutRequestDetails)));
		}

		return constructor.newInstance(methodParamsWithoutRequestDetails);
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
	private static Object[] buildMethodParamsInCorrectPositions(
		String theMethodName, Object[] theMethodParams, Object operationEmbeddedType) {

		final List<RequestDetails> requestDetailsMultiple = Arrays.stream(theMethodParams)
				.filter(RequestDetails.class::isInstance)
				.map(RequestDetails.class::cast)
				.collect(Collectors.toUnmodifiableList());

		if (requestDetailsMultiple.size() > 1) {
			final String error = String.format(
				"%sCannot define a request with more than one RequestDetails for method: %s",
				Msg.code(562462),
				theMethodName);

			throw new InternalErrorException(error);
		}

		if (requestDetailsMultiple.isEmpty()) {
			// No RequestDetails at all
			return new Object[] {operationEmbeddedType};
		}

		final RequestDetails requestDetails = requestDetailsMultiple.get(0);

		final int indexOfRequestDetails = Arrays.asList(theMethodParams).indexOf(requestDetails);

		if (indexOfRequestDetails == 0) {
			// RequestDetails goes first
			return new Object[] {requestDetails, operationEmbeddedType};
		}

		// RequestDetails goes last
		return new Object[] {operationEmbeddedType, requestDetails};
	}

	private static void validMethodParamTypes(
			String theMethodName, Object[] methodParamsWithoutRequestDetails, Parameter[] constructorParameters) {
		if (methodParamsWithoutRequestDetails.length != constructorParameters.length) {
			// LUKETODO:  exception message
			final String error = String.format(
				"%sMismatch between length of non-RequestedDetails params: %s and constructor params: %s for method: %s",
				Msg.code(234198921),
				methodParamsWithoutRequestDetails.length,
				constructorParameters.length,
				theMethodName);
			throw new InternalErrorException(error);
		}

		for (int index = 0; index < methodParamsWithoutRequestDetails.length; index++) {
			validateMethodParamType(theMethodName, methodParamsWithoutRequestDetails[index], constructorParameters[index].getType());
		}
	}

	private static void validateMethodParamType(String theMethodName, Object methodParamAtIndex, Class<?> parameterClassAtIndex) {
		if (methodParamAtIndex == null) {
			// argument is null, so we can't the type, so skip it:
			return;
		}

		final Class<?> methodParamClassAtIndex = methodParamAtIndex.getClass();

		if (Collection.class.isAssignableFrom(methodParamClassAtIndex)
				|| Collection.class.isAssignableFrom(parameterClassAtIndex)) {
			// ex:  List and ArrayList
			if (methodParamClassAtIndex.isAssignableFrom(parameterClassAtIndex)) {
				final String error = String.format(
					"%sMismatch between methodParamClassAtIndex: %s and parameterClassAtIndex: %s for method: %s",
					Msg.code(236146124), methodParamClassAtIndex, parameterClassAtIndex, theMethodName);

				throw new InternalErrorException(error);
			}
			// Ex:  Field is declared as an IIdType, but argument is an IdDt
		} else if (! parameterClassAtIndex.isAssignableFrom(methodParamClassAtIndex)) {
			final String error = String.format(
				"%sMismatch between methodParamClassAtIndex: %s and parameterClassAtIndex: %s for method: %s",
				Msg.code(236146125), methodParamClassAtIndex, parameterClassAtIndex, theMethodName);
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
