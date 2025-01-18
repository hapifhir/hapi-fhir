package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

// LUKETODO:  javadoc
// LUKETODO:  should this be responsible for invoking the method as well?
class BaseMethodBindingMethodParameterBuilder {

	private static final org.slf4j.Logger ourLog =
			LoggerFactory.getLogger(BaseMethodBindingMethodParameterBuilder.class);

	private BaseMethodBindingMethodParameterBuilder() {}

	static Object[] buildMethodParams(Method theMethod, Object[] theMethodParams)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		if (theMethod == null || theMethodParams == null) {
			throw new InternalErrorException(String.format("%s Either theMethod: %s or theMethodParams: %s is null",
					Msg.code(234198927), theMethod, Arrays.toString(theMethodParams)));
		}

		final Class<?>[] methodParameterTypes = theMethod.getParameterTypes();

		if (Arrays.stream(methodParameterTypes)
			.filter(RequestDetails.class::isAssignableFrom).count() > 1) {
			throw new InternalErrorException(String.format(
				"%s1234:  Invalid operation with embedded parameters.  Cannot have more than one RequestDetails: %s",
				Msg.code(924469635), theMethod.getName()));
		}

		final List<Class<?>> parameterTypesWithOperationEmbeddedParam =
				ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(
						theMethod, OperationEmbeddedParam.class);

		if (parameterTypesWithOperationEmbeddedParam.size() > 1) {
			throw new InternalErrorException(String.format(
					"%s1234:  Invalid operation embedded parameters.  More than a single such class is part of method definition: %s",
					Msg.code(924469634), theMethod.getName()));
		}

		if (parameterTypesWithOperationEmbeddedParam.isEmpty()) {
			return theMethodParams;
		}

		final long numRequestDetails = Arrays.stream(methodParameterTypes)
			.filter(RequestDetails.class::isAssignableFrom)
			.count();

		if (numRequestDetails == 0 && methodParameterTypes.length > 1) {
			throw new InternalErrorException(String.format(
				"%s1234:  Invalid operation with embedded parameters.  Cannot have more than 1 params and no RequestDetails: %s",
				Msg.code(924469634), theMethod.getName()));
		}

		if (numRequestDetails > 0 && methodParameterTypes.length > 2) {
			throw new InternalErrorException(String.format(
				"%s1234:  Invalid operation with embedded parameters.  Cannot have more than 2 params and a RequestDetails: %s",
				Msg.code(924469634), theMethod.getName()));
		}

		final Class<?> parameterTypeWithOperationEmbeddedParam = parameterTypesWithOperationEmbeddedParam.get(0);

		return determineMethodParamsForOperationEmbeddedParams(
				theMethod, parameterTypeWithOperationEmbeddedParam, theMethodParams);
	}

	// LUKETODO:  UNIT TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private static Object[] determineMethodParamsForOperationEmbeddedParams(
			Method theMethod, Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InvocationTargetException, IllegalAccessException, InstantiationException {

		ourLog.info(
				"1234: invoking parameterTypeWithOperationEmbeddedParam: {} and theMethod: {}",
				theParameterTypeWithOperationEmbeddedParam,
				theMethod.getName());

		final Object operationEmbeddedType =
				buildOperationEmbeddedObject(theParameterTypeWithOperationEmbeddedParam, theMethodParams);

		ourLog.info(
				"1234: build method params with embedded object and requestDetails (if applicable) for: {}",
				operationEmbeddedType);

		return buildMethodParamsInCorrectPositions(theMethodParams, operationEmbeddedType);
	}

	@Nonnull
	private static Object buildOperationEmbeddedObject(
			Class<?> theParameterTypeWithOperationEmbeddedParam, Object[] theMethodParams)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		final Constructor<?> constructor = validateAndGetConstructor(theParameterTypeWithOperationEmbeddedParam);

		final Object[] methodParamsWithoutRequestDetails = cloneWithRemovedRequestDetails(theMethodParams);

		validMethodParamTypes(methodParamsWithoutRequestDetails, validateAndGetConstructorParameters(constructor));

		if (methodParamsWithoutRequestDetails.length != constructor.getParameterCount()) {
			throw new InternalErrorException(String.format("1234: mismatch between constructor args: %s and non-request details parameter args: %s",
				Arrays.toString(constructor.getParameterTypes()),
				Arrays.toString(methodParamsWithoutRequestDetails)));
		}

		return constructor.newInstance(methodParamsWithoutRequestDetails);
	}

	@Nonnull
	private static Parameter[] validateAndGetConstructorParameters(Constructor<?> constructor) {
		final Parameter[] constructorParameters = constructor.getParameters();

		// LUKETODO:  mandate an immutable class with a constructor to set params
		if (constructorParameters.length == 0) {
			throw new InternalErrorException(Msg.code(234198927) + "No constructor that takes parameters!!!");
		}
		return constructorParameters;
	}

	private static Constructor<?> validateAndGetConstructor(Class<?> theParameterTypeWithOperationEmbeddedParam) {
		final Constructor<?>[] constructors = theParameterTypeWithOperationEmbeddedParam.getConstructors();

		if (constructors.length == 0) {
			throw new InternalErrorException(String.format(
					"%s1234:  Invalid operation embedded parameters.  Class has no constructor: %s",
					Msg.code(561293645), theParameterTypeWithOperationEmbeddedParam));
		}

		if (constructors.length > 1) {
			throw new InternalErrorException(String.format(
					"%s1234:  Invalid operation embedded parameters.  Class has more than one constructor: %s",
					Msg.code(9132164), theParameterTypeWithOperationEmbeddedParam));
		}

		return constructors[0];
	}

	// LUKETODO:  design for future use factory methods

	// RequestDetails must be dealt with separately because there is no such concept in clinical-reasoning and the
	// operation params classes must be defined in that project
	@Nonnull
	private static Object[] buildMethodParamsInCorrectPositions(Object[] theMethodParams, Object operationEmbeddedType) {

		final List<RequestDetails> requestDetailsMultiple = Arrays.stream(theMethodParams)
			.filter(RequestDetails.class::isInstance)
			.map(RequestDetails.class::cast)
			.collect(Collectors.toUnmodifiableList());

		if (requestDetailsMultiple.size() > 1) {
			throw new InternalErrorException(
				Msg.code(562462) + "1234: cannot define a request with more than one RequestDetails");
		}

		if (requestDetailsMultiple.isEmpty()) {
			// No RequestDetails at all
			return new Object[] {operationEmbeddedType};
		}

		final RequestDetails requestDetails = requestDetailsMultiple.get(0);

		final int indexOfRequestDetails = Arrays.asList(theMethodParams)
			.indexOf(requestDetails);

		if (indexOfRequestDetails == 0) {
			// RequestDetails goes first
			return new Object[] {requestDetails, operationEmbeddedType};
		}

		// RequestDetails goes last
		return new Object[] {operationEmbeddedType, requestDetails};
	}

	private static void validMethodParamTypes(
			Object[] methodParamsWithoutRequestDetails, Parameter[] constructorParameters) {
		if (methodParamsWithoutRequestDetails.length != constructorParameters.length) {
			// LUKETODO:  exception message
			throw new InternalErrorException(Msg.code(234198921) + "1234: bad params");
		}

		for (int index = 0; index < methodParamsWithoutRequestDetails.length; index++) {
			validateMethodParamType(methodParamsWithoutRequestDetails[index], constructorParameters[index].getType());
		}
	}

	private static void validateMethodParamType(Object methodParamAtIndex, Class<?> parameterClassAtIndex) {
		if (methodParamAtIndex == null) {
			// argument is null, so we can't the type, so skip it:
			return;
		}

		final Class<?> methodParamClassAtIndex = methodParamAtIndex.getClass();

		// LUKETODO:  fix this this is gross
		if (Collection.class.isAssignableFrom(methodParamClassAtIndex)
				|| Collection.class.isAssignableFrom(parameterClassAtIndex)) {
			// ex:  List and ArrayList
			if (methodParamClassAtIndex.isAssignableFrom(parameterClassAtIndex)) {
				throw new InternalErrorException(String.format(
						"%s1234: Mismatch between methodParamClassAtIndex: %s and parameterClassAtIndex: %s",
						Msg.code(236146124), methodParamClassAtIndex, parameterClassAtIndex));
			}
		// Ex:  Field is declared as an IIdType, but argument is an IdDt
		} else if (! parameterClassAtIndex.isAssignableFrom(methodParamClassAtIndex)) {
			throw new InternalErrorException(String.format(
					"%s1234: Mismatch between methodParamClassAtIndex: %s and parameterClassAtIndex: %s",
					Msg.code(236146125), methodParamClassAtIndex, parameterClassAtIndex));
		}
	}

	// LUKETODO:  code reuse?
	private static Object[] cloneWithRemovedRequestDetails(Object[] theMethodParams) {
		return Arrays.stream(theMethodParams)
				.filter(not(RequestDetails.class::isInstance).and(not(SystemRequestDetails.class::isInstance)))
				.toArray();
	}
}
