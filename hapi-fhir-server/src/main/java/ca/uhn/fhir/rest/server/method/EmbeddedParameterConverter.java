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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.Header;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Leveraged by {@link MethodUtil} exclusively to convert {@link OperationParam} parameters for a method to
 * either a {@link NullParameter} or an {@link OperationParam}.
 */
public class EmbeddedParameterConverter {
	private static final org.slf4j.Logger ourLog = getLogger(EmbeddedParameterConverter.class);

	private final FhirContext myContext;
	private final Method myMethod;
	private final Operation myOperation;
	private final Class<?> myOperationEmbeddedType;
	private final Constructor<?> myConstructor;

	public EmbeddedParameterConverter(FhirContext theContext, Method theMethod, Operation theOperation) {

		if (theOperation == null) {
			throw new ConfigurationException(Msg.code(846192641)
					+ "@OperationParam or OperationEmbeddedParam detected on method that is not annotated with @Operation: "
					+ theMethod.toGenericString());
		}

		final List<Class<?>> embeddedParamsClasses = Arrays.stream(theMethod.getParameterTypes())
				.filter(paramType -> paramType.isAnnotationPresent(EmbeddableOperationParams.class))
				.collect(Collectors.toUnmodifiableList());

		// LUKETODO;  better error?
		if (embeddedParamsClasses.isEmpty()) {
			throw new ConfigurationException(String.format(
					"%sThere is no param with @EmbeddableOperationParams is supported for now for method: %s",
					Msg.code(9999924), theMethod.getName()));
		}

		// LUKETODO;  better error?
		if (embeddedParamsClasses.size() > 1) {
			throw new ConfigurationException(String.format(
					"%sMore than one param with with @EmbeddableOperationParams  for method: %s",
					Msg.code(9999927), theMethod.getName()));
		}

		myContext = theContext;
		myMethod = theMethod;
		myOperation = theOperation;
		myOperationEmbeddedType = embeddedParamsClasses.get(0);
		myConstructor = EmbeddedOperationUtils.validateAndGetConstructor(myOperationEmbeddedType);
	}

	List<EmbeddedParameterConverterContext> convert() {
		return Arrays.stream(myConstructor.getParameters())
				.map(this::convertConstructorParameter)
				.collect(Collectors.toUnmodifiableList());
	}

	private EmbeddedParameterConverterContext convertConstructorParameter(Parameter theConstructorParameter) {
		final Class<?> constructorParamType = theConstructorParameter.getType();
		final Annotation[] constructorParamAnnotations = theConstructorParameter.getAnnotations();

		if (constructorParamAnnotations.length < 1) {
			throw new ConfigurationException(String.format(
					"%sNo annotations for field: %s for method: %s",
					Msg.code(9999926), constructorParamType, myMethod.getName()));
		}

		final Annotation constructorParamAnnotation = constructorParamAnnotations[0];

		if (constructorParamAnnotation instanceof IdParam) {
			return EmbeddedParameterConverterContext.forParameter(new NullParameter());
		} else if (constructorParamAnnotation instanceof Header) {
			return EmbeddedParameterConverterContext.forParameter(
				new HeaderParameter(((Header) constructorParamAnnotation).value()));
		} else if (constructorParamAnnotation instanceof OperationParam) {
			final OperationParameter operationParameter =
					getOperationParameter((OperationParam) constructorParamAnnotation);

			final ParamInitializationContext paramContext =
					buildParamContext(theConstructorParameter, operationParameter);

			return EmbeddedParameterConverterContext.forEmbeddedContext(paramContext);
		} else {
			final String error = String.format(
					"%sUnsupported annotation type: %s for a class: %s with OperationEmbeddedParams which is part of method: %s: ",
					Msg.code(912732197),
					myOperationEmbeddedType,
					constructorParamAnnotation.annotationType(),
					myMethod.getName());

			throw new ConfigurationException(error);
		}
	}

	private ParamInitializationContext buildParamContext(
			Parameter theConstructorParameter, OperationParameter theOperationParameter) {

		final Class<?> genericParameter =
				ReflectionUtil.getGenericCollectionTypeOfConstructorParameter(myConstructor, theConstructorParameter);

		Class<?> parameterType = theConstructorParameter.getType();
		Class<? extends java.util.Collection<?>> outerCollectionType = null;
		Class<? extends java.util.Collection<?>> innerCollectionType = null;

		// Flat collection
		if (Collection.class.isAssignableFrom(parameterType)) {
			innerCollectionType = unsafeCast(parameterType);
			parameterType = genericParameter;
			if (parameterType == null) {
				final String error = String.format(
						"%s Cannot find generic type for field: %s in class: %s for constructor: %s",
						Msg.code(724612469),
						theConstructorParameter.getName(),
						theConstructorParameter.getClass().getCanonicalName(),
						myConstructor.getName());
				throw new ConfigurationException(error);
			}

			// Collection of a Collection: Permitted
			if (Collection.class.isAssignableFrom(parameterType)) {
				outerCollectionType = innerCollectionType;
				innerCollectionType = unsafeCast(parameterType);
			}

			// Collection of a Collection of a Collection:  Prohibited
			if (Collection.class.isAssignableFrom(parameterType)) {
				final String error = String.format(
						"%sInvalid generic type (a collection of a collection of a collection) for field: %s in class: %s for constructor: %s",
						Msg.code(724612469),
						theConstructorParameter.getName(),
						theConstructorParameter.getClass().getCanonicalName(),
						myConstructor.getName());
				throw new ConfigurationException(error);
			}
		}

		// TODO: LD:  Don't worry about the OperationEmbeddedParam.type() for now until we chose to implement it later

		return new ParamInitializationContext(
				theOperationParameter, parameterType, outerCollectionType, innerCollectionType);
	}

	@Nonnull
	private OperationParameter getOperationParameter(OperationParam operationParam) {
		final Annotation[] fieldAnnotationArray = new Annotation[] {operationParam};

		return new OperationParameter(
				myContext,
				myOperation.name(),
				operationParam.name(),
				operationParam.min(),
				operationParam.max(),
				ParametersUtil.extractDescription(fieldAnnotationArray),
				ParametersUtil.extractExamples(fieldAnnotationArray),
				operationParam.sourceType(),
				operationParam.rangeType());
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
