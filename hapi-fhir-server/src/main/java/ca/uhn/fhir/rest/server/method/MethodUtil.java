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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.RawParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.binder.CollectionBinder;
import ca.uhn.fhir.rest.server.method.OperationParameter.IOperationParamConverter;
import ca.uhn.fhir.rest.server.method.ResourceParameter.Mode;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MethodUtil {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MethodUtil.class);

	/**
	 * Non instantiable
	 */
	private MethodUtil() {
		// nothing
	}

	public static void extractDescription(SearchParameter theParameter, Annotation[] theAnnotations) {
		for (Annotation annotation : theAnnotations) {
			if (annotation instanceof Description) {
				Description desc = (Description) annotation;
				String description = ParametersUtil.extractDescription(desc);
				theParameter.setDescription(description);
			}
		}
	}

	// LUKETODO:  extract annotations method and make sure it works with embedded params
	public static List<IParameter> getResourceParameters(
			final FhirContext theContext, final Method theMethod, Object theProvider) {
		// We mutate this variable so distinguish this from the argument to getResourceParameters
		List<IParameter> parameters = new ArrayList<>();

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;

		for (Annotation[] nextParameterAnnotations : theMethod.getParameterAnnotations()) {
			IParameter param = null;
			final List<ParamInitializationContext> paramContexts = new ArrayList<>();

			Class<?> declaredParameterType = parameterTypes[paramIndex];
			Class<?> parameterType = declaredParameterType;

			Class<? extends java.util.Collection<?>> outerCollectionType = null;
			Class<? extends java.util.Collection<?>> innerCollectionType = null;
			if (TagList.class.isAssignableFrom(parameterType)) {
				// TagList is handled directly within the method bindings
				param = new NullParameter();
			} else {
				final GenericsContext genericsContext =
						getGenericsContext(theContext, theMethod, parameterTypes, paramIndex);

				parameterType = genericsContext.getParameterType();
				declaredParameterType = genericsContext.getDeclaredParameterType();
				outerCollectionType = genericsContext.getOuterCollectionType();
				innerCollectionType = genericsContext.getInnerCollectionType();
			}

			if (ServletRequest.class.isAssignableFrom(parameterType)) {
				param = new ServletRequestParameter();
			} else if (ServletResponse.class.isAssignableFrom(parameterType)) {
				param = new ServletResponseParameter();
			} else if (parameterType.equals(RequestDetails.class)
					|| parameterType.equals(ServletRequestDetails.class)) {
				param = new RequestDetailsParameter();
			} else if (parameterType.equals(IInterceptorBroadcaster.class)) {
				param = new InterceptorBroadcasterParameter();
			} else if (parameterType.equals(SummaryEnum.class)) {
				param = new SummaryEnumParameter();
			} else if (parameterType.equals(PatchTypeEnum.class)) {
				param = new PatchTypeParameter();
			} else if (parameterType.equals(SearchContainedModeEnum.class)) {
				param = new SearchContainedModeParameter();
			} else if (parameterType.equals(SearchTotalModeEnum.class)) {
				param = new SearchTotalModeParameter();
			} else {
				for (int i = 0; i < nextParameterAnnotations.length && param == null; i++) {
					final Annotation nextParameterAnnotation = nextParameterAnnotations[i];

					final IParameter paramForNonOperationNonEmbeddedAnnotation =
							getParamForNonOperationNonEmbeddedAnnotation(
									theContext,
									theProvider,
									theMethod,
									nextParameterAnnotations,
									nextParameterAnnotation,
									parameterType,
									outerCollectionType,
									innerCollectionType,
									parameters);

					if (paramForNonOperationNonEmbeddedAnnotation != null) {
						param = paramForNonOperationNonEmbeddedAnnotation;
					} else if ((nextParameterAnnotation instanceof OperationParam)
							|| (nextParameterAnnotation instanceof EmbeddedOperationParams)) {
						final Operation op = theMethod.getAnnotation(Operation.class);

						if (op == null) {
							final String error = String.format("%s@OperationParam or @EmbeddedOperationParams detected on method: [%s] that is not annotated with @Operation: %s",
								Msg.code(404),
								theMethod.getName(),
								theMethod.toGenericString());
							throw new ConfigurationException(error);
						}

						if (nextParameterAnnotation instanceof OperationParam) {
							final ParamInitializationContext operationParamContext = createOperationParamContext(
									theContext,
									theMethod,
									nextParameterAnnotations,
									nextParameterAnnotation,
									parameterType,
									declaredParameterType,
									outerCollectionType,
									innerCollectionType);

							param = operationParamContext.getParam();
							parameterType = operationParamContext.getParameterType();
						}

						if (nextParameterAnnotation instanceof EmbeddedOperationParams) {
							final EmbeddedParameterConverter embeddedParameterConverter =
									new EmbeddedParameterConverter(theContext, theMethod, op);

							for (EmbeddedParameterConverterContext outerContext :
									embeddedParameterConverter.convert()) {
								if (outerContext.getParameter() != null) {
									parameters.add(outerContext.getParameter());
								}

								final ParamInitializationContext paramContext = outerContext.getParamContext();

								if (paramContext != null) {
									paramContexts.add(paramContext);

									// N.B. This a hack used only to pass the null check below, which is crucial to the
									// non-embedded params logic
									param = paramContext.getParam();
								}
							}
						} // else param is null, and we throw, assuming nothing next in the loop doesn't this variable
					}
				}
			}

			if (param == null) {
				throw new ConfigurationException(
						Msg.code(408) + "Parameter #" + (paramIndex + 1) + "/" + (parameterTypes.length)
								+ " of method '" + theMethod.getName() + "' on type '"
								+ theMethod.getDeclaringClass().getCanonicalName()
								+ "' has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
			}

			if (paramContexts.isEmpty()
					// Ensure that if we've processed embedded operations parameters and last parameter is a
					// RequestDetails, we don't miss it
					|| EmbeddedOperationUtils.typeHasNoEmbeddableOperationParamsAnnotation(parameterType)) {
				paramContexts.add(
						new ParamInitializationContext(param, parameterType, outerCollectionType, innerCollectionType));
			}

			for (ParamInitializationContext paramContext : paramContexts) {
				paramContext.initialize(theMethod);
				parameters.add(paramContext.getParam());
			}

			paramIndex++;
		}
		return parameters;
	}

	@Nullable
	private static IParameter getParamForNonOperationNonEmbeddedAnnotation(
			FhirContext theContext,
			Object theProvider,
			Method theMethod,
			Annotation[] theNextParameterAnnotations,
			Annotation theNextAnnotation,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType,
			List<IParameter> theParameters) {
		if (theNextAnnotation instanceof RequiredParam) {
			return createRequiredParam(
					theContext,
					theNextParameterAnnotations,
					(RequiredParam) theNextAnnotation,
					theParameterType,
					theInnerCollectionType,
					theOuterCollectionType);
		} else if (theNextAnnotation instanceof OptionalParam) {
			return createOptionalParam(
					theContext,
					theNextParameterAnnotations,
					(OptionalParam) theNextAnnotation,
					theParameterType,
					theInnerCollectionType,
					theOuterCollectionType);
		} else if (theNextAnnotation instanceof RawParam) {
			return new RawParamsParameter(theParameters);
		} else if (theNextAnnotation instanceof IncludeParam) {
			return createIncludeParam(
					theMethod, theParameterType, theInnerCollectionType, theOuterCollectionType, (IncludeParam)
							theNextAnnotation);
		} else if (theNextAnnotation instanceof ResourceParam) {
			return createResourceParam(theMethod, theProvider, theParameterType);
		} else if (theNextAnnotation instanceof IdParam) {
			return new NullParameter();
		} else if (theNextAnnotation instanceof ServerBase) {
			return new ServerBaseParamBinder();
		} else if (theNextAnnotation instanceof Elements) {
			return new ElementsParameter();
		} else if (theNextAnnotation instanceof Since) {
			return createSinceParameter(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		} else if (theNextAnnotation instanceof At) {
			return createAtParameter(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		} else if (theNextAnnotation instanceof Count) {
			return new CountParameter();
		} else if (theNextAnnotation instanceof Offset) {
			return new OffsetParameter();
		} else if (theNextAnnotation instanceof GraphQLQueryUrl) {
			return new GraphQLQueryUrlParameter();
		} else if (theNextAnnotation instanceof GraphQLQueryBody) {
			return new GraphQLQueryBodyParameter();
		} else if (theNextAnnotation instanceof Sort) {
			return new SortParameter(theContext);
		} else if (theNextAnnotation instanceof TransactionParam) {
			return new TransactionParameter(theContext);
		} else if (theNextAnnotation instanceof ConditionalUrlParam) {
			return new ConditionalParamBinder(((ConditionalUrlParam) theNextAnnotation).supportsMultiple());
		} else if (theNextAnnotation instanceof Validate.Mode) {
			return createValidateNode(theContext, theNextParameterAnnotations, theParameterType);
		} else if (theNextAnnotation instanceof Validate.Profile) {
			return createValidateProfile(theContext, theNextParameterAnnotations, theParameterType);
		}
		return null;
	}

	@Nonnull
	private static IParameter createAtParameter(
			FhirContext theContext,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<? extends Collection<?>> theOuterCollectionType) {
		final AtParameter param = new AtParameter();
		param.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		return param;
	}

	@Nonnull
	private static IParameter createSinceParameter(
			FhirContext theTheContext,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<? extends Collection<?>> theOuterCollectionType) {
		final SinceParameter param = new SinceParameter();
		param.setType(theTheContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		return param;
	}

	@Nonnull
	private static IParameter createRequiredParam(
			FhirContext theContext,
			Annotation[] theNextParameterAnnotations,
			RequiredParam theNextAnnotation,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<? extends Collection<?>> theOuterCollectionType) {
		IParameter param;
		SearchParameter parameter = new SearchParameter();
		parameter.setName(theNextAnnotation.name());
		parameter.setRequired(true);
		parameter.setDeclaredTypes(theNextAnnotation.targetTypes());
		parameter.setCompositeTypes(theNextAnnotation.compositeTypes());
		parameter.setChainLists(theNextAnnotation.chainWhitelist(), theNextAnnotation.chainBlacklist());
		parameter.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		MethodUtil.extractDescription(parameter, theNextParameterAnnotations);
		param = parameter;
		return param;
	}

	@Nonnull
	private static IParameter createOptionalParam(
			FhirContext theContext,
			Annotation[] theNextParameterAnnotations,
			OptionalParam theNextAnnotation,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<? extends Collection<?>> theOuterCollectionType) {
		IParameter param;
		SearchParameter parameter = new SearchParameter();
		parameter.setName(theNextAnnotation.name());
		parameter.setRequired(false);
		parameter.setDeclaredTypes(theNextAnnotation.targetTypes());
		parameter.setCompositeTypes(theNextAnnotation.compositeTypes());
		parameter.setChainLists(theNextAnnotation.chainWhitelist(), theNextAnnotation.chainBlacklist());
		parameter.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		MethodUtil.extractDescription(parameter, theNextParameterAnnotations);
		param = parameter;
		return param;
	}

	@Nonnull
	private static IParameter createIncludeParam(
			Method theMethod,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<? extends Collection<?>> theOuterCollectionType,
			IncludeParam theNextAnnotation) {
		IParameter param;
		Class<? extends Collection<Include>> instantiableCollectionType;
		Class<?> specType;

		if (theParameterType == String.class) {
			instantiableCollectionType = null;
			specType = String.class;
		} else if ((theParameterType != Include.class)
				|| theInnerCollectionType == null
				|| theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(402) + "Method '" + theMethod.getName()
					+ "' is annotated with @" + IncludeParam.class.getSimpleName()
					+ " but has a type other than Collection<" + Include.class.getSimpleName() + ">");
		} else {
			instantiableCollectionType = unsafeCast(CollectionBinder.getInstantiableCollectionType(
					theInnerCollectionType, "Method '" + theMethod.getName() + "'"));
			specType = theParameterType;
		}

		param = new IncludeParameter(theNextAnnotation, instantiableCollectionType, specType);
		return param;
	}

	@Nonnull
	private static IParameter createResourceParam(Method theMethod, Object theProvider, Class<?> theParameterType) {
		IParameter param;
		Mode mode;
		if (IBaseResource.class.isAssignableFrom(theParameterType)) {
			mode = Mode.RESOURCE;
		} else if (String.class.equals(theParameterType)) {
			mode = Mode.BODY;
		} else if (byte[].class.equals(theParameterType)) {
			mode = Mode.BODY_BYTE_ARRAY;
		} else if (EncodingEnum.class.equals(theParameterType)) {
			mode = Mode.ENCODING;
		} else {
			final String error = String.format(
					"%sMethod: '%s' is annotated with @%s but has a type that is not an implementation of %s or String or byte[]",
					Msg.code(403),
					theMethod.getName(),
					ResourceParam.class.getSimpleName(),
					IBaseResource.class.getCanonicalName());
			throw new ConfigurationException(error);
		}
		boolean methodIsOperation = theMethod.getAnnotation(Operation.class) != null;
		boolean methodIsPatch = theMethod.getAnnotation(Patch.class) != null;
		param = new ResourceParameter(
				unsafeCast(theParameterType), theProvider, mode, methodIsOperation, methodIsPatch);
		return param;
	}

	private static IParameter createValidateNode(
			FhirContext theContext, Annotation[] theNextParameterAnnotations, Class<?> theParameterType) {
		IParameter param;
		if (!theParameterType.equals(ValidationModeEnum.class)) {
			throw new ConfigurationException(Msg.code(406) + "Parameter annotated with @"
					+ Validate.class.getSimpleName() + "." + Validate.Mode.class.getSimpleName()
					+ " must be of type " + ValidationModeEnum.class.getName());
		}
		String description = ParametersUtil.extractDescription(theNextParameterAnnotations);
		List<String> examples = ParametersUtil.extractExamples(theNextParameterAnnotations);
		param = new OperationParameter(
						theContext,
						Constants.EXTOP_VALIDATE,
						Constants.EXTOP_VALIDATE_MODE,
						0,
						1,
						description,
						examples,
						Void.class,
						OperationParameterRangeType.NOT_APPLICABLE)
				.setConverter(new IOperationParamConverter() {
					@Override
					public Object incomingServer(Object theObject) {
						if (isNotBlank(theObject.toString())) {
							ValidationModeEnum retVal = ValidationModeEnum.forCode(theObject.toString());
							if (retVal == null) {
								OperationParameter.throwInvalidMode(theObject.toString());
							}
							return retVal;
						}
						return null;
					}

					@Override
					public Object outgoingClient(Object theObject) {
						return ParametersUtil.createString(theContext, ((ValidationModeEnum) theObject).getCode());
					}
				});
		return param;
	}

	private static IParameter createValidateProfile(
			FhirContext theContext, Annotation[] theNextParameterAnnotations, Class<?> theParameterType) {
		IParameter param;
		if (!theParameterType.equals(String.class)) {
			throw new ConfigurationException(Msg.code(407) + "Parameter annotated with @"
					+ Validate.class.getSimpleName() + "." + Validate.Profile.class.getSimpleName()
					+ " must be of type " + String.class.getName());
		}
		String description = ParametersUtil.extractDescription(theNextParameterAnnotations);
		List<String> examples = ParametersUtil.extractExamples(theNextParameterAnnotations);
		param = new OperationParameter(
						theContext,
						Constants.EXTOP_VALIDATE,
						Constants.EXTOP_VALIDATE_PROFILE,
						0,
						1,
						description,
						examples,
						Void.class,
						OperationParameterRangeType.NOT_APPLICABLE)
				.setConverter(new IOperationParamConverter() {
					@Override
					public Object incomingServer(Object theObject) {
						return theObject.toString();
					}

					@Override
					public Object outgoingClient(Object theObject) {
						return ParametersUtil.createString(theContext, theObject.toString());
					}
				});
		return param;
	}

	@Nonnull
	private static ParamInitializationContext createOperationParamContext(
			FhirContext theContext,
			Method theMethod,
			Annotation[] theNextParameterAnnotations,
			Annotation theNextAnnotation,
			Class<?> theParameterType,
			Class<?> theDeclaredParameterType,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType) {
		final Operation op = theMethod.getAnnotation(Operation.class);
		if (op == null) {
			throw new ConfigurationException(Msg.code(404)
					+ "@OperationParam detected on method that is not annotated with @Operation: "
					+ theMethod.toGenericString());
		}

		final OperationParam operationParam = (OperationParam) theNextAnnotation;
		final String description = ParametersUtil.extractDescription(theNextParameterAnnotations);
		final List<String> examples = ParametersUtil.extractExamples(theNextParameterAnnotations);
		Class<?> parameterTypeInner = theParameterType;

		final OperationParameter param = new OperationParameter(
				theContext,
				op.name(),
				operationParam.name(),
				operationParam.min(),
				operationParam.max(),
				description,
				examples,
				operationParam.sourceType(),
				operationParam.rangeType());

		if (isNotBlank(operationParam.typeName())) {
			BaseRuntimeElementDefinition<?> elementDefinition =
					theContext.getElementDefinition(operationParam.typeName());
			if (elementDefinition == null) {
				elementDefinition = theContext.getResourceDefinition(operationParam.typeName());
			}
			org.apache.commons.lang3.Validate.notNull(
					elementDefinition,
					"Unknown type name in @OperationParam: typeName=\"%s\"",
					operationParam.typeName());

			Class<?> newParameterType = elementDefinition.getImplementingClass();
			if (!theDeclaredParameterType.isAssignableFrom(newParameterType)) {
				throw new ConfigurationException(Msg.code(405) + "Non assignable parameter typeName=\""
						+ operationParam.typeName() + "\" specified on method " + theMethod);
			}
			parameterTypeInner = newParameterType;
		}

		return new ParamInitializationContext(
				param, parameterTypeInner, theOuterCollectionType, theInnerCollectionType);
	}

	private static GenericsContext getGenericsContext(
			FhirContext theContext, Method theMethod, Class<?>[] theParameterTypes, int theParamIndex) {

		Class<?> declaredParameterType = theParameterTypes[theParamIndex];
		Class<?> parameterType = declaredParameterType;
		Class<? extends java.util.Collection<?>> outerCollectionType = null;
		Class<? extends java.util.Collection<?>> innerCollectionType = null;

		if (Collection.class.isAssignableFrom(parameterType)) {
			innerCollectionType = unsafeCast(parameterType);
			parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
			if (parameterType == null && theMethod.getDeclaringClass().isSynthetic()) {
				try {
					theMethod = theMethod
							.getDeclaringClass()
							.getSuperclass()
							.getMethod(theMethod.getName(), theParameterTypes);
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
				} catch (NoSuchMethodException e) {
					throw new ConfigurationException(Msg.code(400) + "A method with name '"
							+ theMethod.getName() + "' does not exist for super class '"
							+ theMethod.getDeclaringClass().getSuperclass() + "'");
				}
			}
			declaredParameterType = parameterType;
		}
		if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
			outerCollectionType = innerCollectionType;
			innerCollectionType = unsafeCast(parameterType);
			parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
			declaredParameterType = parameterType;
		}
		if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
			throw new ConfigurationException(
					Msg.code(401) + "Argument #" + theParamIndex + " of Method '" + theMethod.getName()
							+ "' in type '"
							+ theMethod.getDeclaringClass().getCanonicalName()
							+ "' is of an invalid generic type (can not be a collection of a collection of a collection)");
		}

		/*
		 * If the user is trying to bind IPrimitiveType they are probably
		 * trying to write code that is compatible across versions of FHIR.
		 * We'll try and come up with an appropriate subtype to give
		 * them.
		 *
		 * This gets tested in HistoryR4Test
		 */
		if (IPrimitiveType.class.equals(parameterType)) {
			Class<?> genericType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
			if (Date.class.equals(genericType)) {
				BaseRuntimeElementDefinition<?> dateTimeDef = theContext.getElementDefinition("dateTime");
				parameterType = Optional.ofNullable(dateTimeDef)
						.map(BaseRuntimeElementDefinition::getImplementingClass)
						.orElse(null);
			} else if (String.class.equals(genericType) || genericType == null) {
				BaseRuntimeElementDefinition<?> dateTimeDef = theContext.getElementDefinition("string");
				parameterType = Optional.ofNullable(dateTimeDef)
						.map(BaseRuntimeElementDefinition::getImplementingClass)
						.orElse(null);
			}
		}

		return new GenericsContext(parameterType, declaredParameterType, outerCollectionType, innerCollectionType);
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}

	// LUKETODO:  top level?
	private static class GenericsContext {
		private final Class<?> parameterType;
		private final Class<?> declaredParameterType;
		private final Class<? extends java.util.Collection<?>> outerCollectionType;
		private final Class<? extends java.util.Collection<?>> innerCollectionType;

		public GenericsContext(
				Class<?> theParameterType,
				Class<?> theDeclaredParameterType,
				Class<? extends Collection<?>> theOuterCollectionType,
				Class<? extends Collection<?>> theInnerCollectionType) {
			parameterType = theParameterType;
			declaredParameterType = theDeclaredParameterType;
			outerCollectionType = theOuterCollectionType;
			innerCollectionType = theInnerCollectionType;
		}

		public Class<?> getParameterType() {
			return parameterType;
		}

		public Class<?> getDeclaredParameterType() {
			return declaredParameterType;
		}

		public Class<? extends Collection<?>> getOuterCollectionType() {
			return outerCollectionType;
		}

		public Class<? extends Collection<?>> getInnerCollectionType() {
			return innerCollectionType;
		}
	}

	// LUKETODO:  refactor to use only one of the Context classes
	private static class ParameterContext {
		private final Class<?> myParameterType;

		@Nullable
		private final IParameter myParameter;

		public ParameterContext(Class<?> theParameterType, @Nullable IParameter theParameter) {
			myParameter = theParameter;
			myParameterType = theParameterType;
		}

		public IParameter getParam() {
			return myParameter;
		}

		public Class<?> getParameterType() {
			return myParameterType;
		}
	}
}
