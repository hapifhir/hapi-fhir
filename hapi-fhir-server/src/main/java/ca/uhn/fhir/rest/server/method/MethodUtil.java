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
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MethodUtil {

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

	/**
	 * Encapsulates collection type information for method parameters.
	 * Handles single-level and nested collections.
	 */
	private static class CollectionTypeInfo {
		final Class<?> effectiveParameterType;
		final Class<?> declaredParameterType;
		final Class<? extends java.util.Collection<?>> innerCollectionType;
		final Class<? extends java.util.Collection<?>> outerCollectionType;

		CollectionTypeInfo(
				Class<?> effectiveParameterType,
				Class<?> declaredParameterType,
				Class<? extends java.util.Collection<?>> innerCollectionType,
				Class<? extends java.util.Collection<?>> outerCollectionType) {
			this.effectiveParameterType = effectiveParameterType;
			this.declaredParameterType = declaredParameterType;
			this.innerCollectionType = innerCollectionType;
			this.outerCollectionType = outerCollectionType;
		}
	}

	/**
	 * Detects and unwraps collection types for a method parameter.
	 * Handles single-level and nested collections, synthetic classes (lambdas/proxies),
	 * and validates against triple-nested collections.
	 *
	 * @param theMethod the method being analyzed
	 * @param theParamIndex the parameter index
	 * @param theParameterType the original parameter type
	 * @return CollectionTypeInfo containing unwrapped type information
	 * @throws ConfigurationException if triple-nested collections are detected
	 */
	@SuppressWarnings("unchecked")
	private static CollectionTypeInfo detectCollectionTypes(
			Method theMethod, int theParamIndex, Class<?> theParameterType) {
		Class<?> parameterType = theParameterType;
		Class<?> declaredParameterType = theParameterType;
		Class<? extends java.util.Collection<?>> innerCollectionType = null;
		Class<? extends java.util.Collection<?>> outerCollectionType = null;

		// First level collection unwrapping
		if (Collection.class.isAssignableFrom(parameterType)) {
			innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
			parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);

			// Handle synthetic classes (lambdas, proxies)
			if (parameterType == null && theMethod.getDeclaringClass().isSynthetic()) {
				try {
					Class<?>[] parameterTypes = theMethod.getParameterTypes();
					theMethod = theMethod
							.getDeclaringClass()
							.getSuperclass()
							.getMethod(theMethod.getName(), parameterTypes);
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
				} catch (NoSuchMethodException e) {
					throw new ConfigurationException(Msg.code(400) + "A method with name '" + theMethod.getName()
							+ "' does not exist for super class '"
							+ theMethod.getDeclaringClass().getSuperclass() + "'");
				}
			}
			declaredParameterType = parameterType;
		}

		// Second level collection unwrapping
		if (Collection.class.isAssignableFrom(parameterType)) {
			outerCollectionType = innerCollectionType;
			innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
			parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
			declaredParameterType = parameterType;
		}

		// Validate no triple-nested collections
		if (Collection.class.isAssignableFrom(parameterType)) {
			throw new ConfigurationException(Msg.code(401) + "Argument #" + theParamIndex + " of Method '"
					+ theMethod.getName() + "' in type '"
					+ theMethod.getDeclaringClass().getCanonicalName()
					+ "' is of an invalid generic type (can not be a collection of a collection of a collection)");
		}

		return new CollectionTypeInfo(parameterType, declaredParameterType, innerCollectionType, outerCollectionType);
	}

	/**
	 * Resolves IPrimitiveType parameters to concrete implementation classes.
	 * This allows code to be compatible across FHIR versions by using IPrimitiveType&lt;Date&gt;
	 * or IPrimitiveType&lt;String&gt; and having the appropriate DateTimeType or StringType
	 * bound at runtime based on the FhirContext version.
	 *
	 * @param theContext the FHIR context
	 * @param theMethod the method being analyzed
	 * @param theParamIndex the parameter index
	 * @param theParameterType the current parameter type
	 * @return the resolved concrete type, or the original type if not IPrimitiveType
	 */
	private static Class<?> resolvePrimitiveType(
			FhirContext theContext, Method theMethod, int theParamIndex, Class<?> theParameterType) {
		if (IPrimitiveType.class.equals(theParameterType)) {
			Class<?> genericType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, theParamIndex);
			if (Date.class.equals(genericType)) {
				BaseRuntimeElementDefinition<?> dateTimeDef = theContext.getElementDefinition("dateTime");
				return dateTimeDef.getImplementingClass();
			} else if (String.class.equals(genericType) || genericType == null) {
				BaseRuntimeElementDefinition<?> stringDef = theContext.getElementDefinition("string");
				return stringDef.getImplementingClass();
			}
		}
		return theParameterType;
	}

	/**
	 * Creates type-based parameters that don't require annotations.
	 * These parameters are detected solely by their Java type.
	 *
	 * @param theParameterType the parameter type
	 * @return the appropriate IParameter implementation, or null if not a recognized type
	 */
	private static IParameter createTypeBasedParameter(Class<?> theParameterType) {
		if (ServletRequest.class.isAssignableFrom(theParameterType)) {
			return new ServletRequestParameter();
		} else if (ServletResponse.class.isAssignableFrom(theParameterType)) {
			return new ServletResponseParameter();
		} else if (theParameterType.equals(RequestDetails.class)
				|| theParameterType.equals(ServletRequestDetails.class)) {
			return new RequestDetailsParameter();
		} else if (theParameterType.equals(IInterceptorBroadcaster.class)) {
			return new InterceptorBroadcasterParameter();
		} else if (theParameterType.equals(SummaryEnum.class)) {
			return new SummaryEnumParameter();
		} else if (theParameterType.equals(PatchTypeEnum.class)) {
			return new PatchTypeParameter();
		} else if (theParameterType.equals(SearchContainedModeEnum.class)) {
			return new SearchContainedModeParameter();
		} else if (theParameterType.equals(SearchTotalModeEnum.class)) {
			return new SearchTotalModeParameter();
		}
		return null;
	}

	/**
	 * Creates a SearchParameter for @RequiredParam or @OptionalParam annotations.
	 *
	 * @param theAnnotation the RequiredParam or OptionalParam annotation
	 * @param theContext the FHIR context
	 * @param theParameterType the parameter type
	 * @param theInnerCollectionType the inner collection type (may be null)
	 * @param theOuterCollectionType the outer collection type (may be null)
	 * @param theParameterAnnotations all annotations on the parameter (for description extraction)
	 * @return configured SearchParameter
	 */
	private static SearchParameter createSearchParameter(
			Annotation theAnnotation,
			FhirContext theContext,
			Class<?> theParameterType,
			Class<? extends java.util.Collection<?>> theInnerCollectionType,
			Class<? extends java.util.Collection<?>> theOuterCollectionType,
			Annotation[] theParameterAnnotations) {
		SearchParameter parameter = new SearchParameter();

		if (theAnnotation instanceof RequiredParam) {
			RequiredParam required = (RequiredParam) theAnnotation;
			parameter.setName(required.name());
			parameter.setRequired(true);
			parameter.setDeclaredTypes(required.targetTypes());
			parameter.setCompositeTypes(required.compositeTypes());
			parameter.setChainLists(required.chainWhitelist(), required.chainBlacklist());
		} else if (theAnnotation instanceof OptionalParam) {
			OptionalParam optional = (OptionalParam) theAnnotation;
			parameter.setName(optional.name());
			parameter.setRequired(false);
			parameter.setDeclaredTypes(optional.targetTypes());
			parameter.setCompositeTypes(optional.compositeTypes());
			parameter.setChainLists(optional.chainWhitelist(), optional.chainBlacklist());
		}

		parameter.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
		MethodUtil.extractDescription(parameter, theParameterAnnotations);
		return parameter;
	}

	/**
	 * Creates an IncludeParameter for @IncludeParam annotation.
	 * Validates that the parameter type is either String or Collection&lt;Include&gt;.
	 *
	 * @param theAnnotation the IncludeParam annotation
	 * @param theMethod the method being analyzed
	 * @param theParameterType the parameter type
	 * @param theInnerCollectionType the inner collection type (may be null)
	 * @param theOuterCollectionType the outer collection type (may be null)
	 * @return configured IncludeParameter
	 * @throws ConfigurationException if parameter type is invalid
	 */
	@SuppressWarnings("unchecked")
	private static IParameter createIncludeParameter(
			IncludeParam theAnnotation,
			Method theMethod,
			Class<?> theParameterType,
			Class<? extends java.util.Collection<?>> theInnerCollectionType,
			Class<? extends java.util.Collection<?>> theOuterCollectionType) {
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
			instantiableCollectionType =
					(Class<? extends Collection<Include>>) CollectionBinder.getInstantiableCollectionType(
							theInnerCollectionType, "Method '" + theMethod.getName() + "'");
			specType = theParameterType;
		}

		return new IncludeParameter(theAnnotation, instantiableCollectionType, specType);
	}

	/**
	 * Creates a ResourceParameter for @ResourceParam annotation.
	 * Determines the mode based on parameter type and checks for @Operation/@Patch annotations.
	 *
	 * @param theMethod the method being analyzed
	 * @param theProvider the provider instance
	 * @param theParameterType the parameter type
	 * @return configured ResourceParameter
	 * @throws ConfigurationException if parameter type is invalid
	 */
	@SuppressWarnings("unchecked")
	private static IParameter createResourceParameter(Method theMethod, Object theProvider, Class<?> theParameterType) {
		Mode mode;
		if (IBaseResource.class.isAssignableFrom(theParameterType)) {
			mode = Mode.RESOURCE;
		} else if (String.class.equals(theParameterType)) {
			mode = ResourceParameter.Mode.BODY;
		} else if (byte[].class.equals(theParameterType)) {
			mode = ResourceParameter.Mode.BODY_BYTE_ARRAY;
		} else if (EncodingEnum.class.equals(theParameterType)) {
			mode = Mode.ENCODING;
		} else {
			StringBuilder b = new StringBuilder();
			b.append("Method '");
			b.append(theMethod.getName());
			b.append("' is annotated with @");
			b.append(ResourceParam.class.getSimpleName());
			b.append(" but has a type that is not an implementation of ");
			b.append(IBaseResource.class.getCanonicalName());
			b.append(" or String or byte[]");
			throw new ConfigurationException(Msg.code(403) + b.toString());
		}

		boolean methodIsOperation = theMethod.getAnnotation(Operation.class) != null;
		boolean methodIsPatch = theMethod.getAnnotation(Patch.class) != null;
		return new ResourceParameter(
				(Class<? extends IBaseResource>) theParameterType, theProvider, mode, methodIsOperation, methodIsPatch);
	}

	/**
	 * Creates an OperationParameter for @OperationParam annotation.
	 * Handles type name resolution and validation.
	 *
	 * @param theAnnotation the OperationParam annotation
	 * @param theMethod the method being analyzed
	 * @param theContext the FHIR context
	 * @param theDeclaredParameterType the declared parameter type
	 * @param theParameterAnnotations all annotations on the parameter
	 * @return configured OperationParameter and resolved parameter type
	 * @throws ConfigurationException if method is not annotated with @Operation or type is invalid
	 */
	private static OperationParamResult createOperationParameter(
			OperationParam theAnnotation,
			Method theMethod,
			FhirContext theContext,
			Class<?> theDeclaredParameterType,
			Annotation[] theParameterAnnotations) {
		Operation op = theMethod.getAnnotation(Operation.class);
		if (op == null) {
			throw new ConfigurationException(Msg.code(404)
					+ "@OperationParam detected on method that is not annotated with @Operation: "
					+ theMethod.toGenericString());
		}

		String description = ParametersUtil.extractDescription(theParameterAnnotations);
		List<String> examples = ParametersUtil.extractExamples(theParameterAnnotations);
		OperationParameter param = new OperationParameter(
				theContext,
				op.name(),
				theAnnotation.name(),
				theAnnotation.min(),
				theAnnotation.max(),
				description,
				examples);

		Class<?> resolvedParameterType = theDeclaredParameterType;
		if (isNotBlank(theAnnotation.typeName())) {
			BaseRuntimeElementDefinition<?> elementDefinition =
					theContext.getElementDefinition(theAnnotation.typeName());
			if (elementDefinition == null) {
				elementDefinition = theContext.getResourceDefinition(theAnnotation.typeName());
			}
			org.apache.commons.lang3.Validate.notNull(
					elementDefinition,
					"Unknown type name in @OperationParam: typeName=\"%s\"",
					theAnnotation.typeName());

			Class<?> newParameterType = elementDefinition.getImplementingClass();
			if (!theDeclaredParameterType.isAssignableFrom(newParameterType)) {
				throw new ConfigurationException(Msg.code(405) + "Non assignable parameter typeName=\""
						+ theAnnotation.typeName() + "\" specified on method " + theMethod);
			}
			resolvedParameterType = newParameterType;
		}

		return new OperationParamResult(param, resolvedParameterType);
	}

	/**
	 * Helper class to return both the OperationParameter and resolved type.
	 */
	private static class OperationParamResult {
		final OperationParameter parameter;
		final Class<?> resolvedType;

		OperationParamResult(OperationParameter parameter, Class<?> resolvedType) {
			this.parameter = parameter;
			this.resolvedType = resolvedType;
		}
	}

	/**
	 * Creates an OperationParameter for @Validate.Mode annotation.
	 *
	 * @param theContext the FHIR context
	 * @param theParameterType the parameter type
	 * @param theParameterAnnotations all annotations on the parameter
	 * @return configured OperationParameter with validation mode converter
	 * @throws ConfigurationException if parameter type is not ValidationModeEnum
	 */
	private static IParameter createValidationModeParameter(
			FhirContext theContext, Class<?> theParameterType, Annotation[] theParameterAnnotations) {
		if (theParameterType.equals(ValidationModeEnum.class) == false) {
			throw new ConfigurationException(Msg.code(406) + "Parameter annotated with @"
					+ Validate.class.getSimpleName() + "." + Validate.Mode.class.getSimpleName()
					+ " must be of type " + ValidationModeEnum.class.getName());
		}

		String description = ParametersUtil.extractDescription(theParameterAnnotations);
		List<String> examples = ParametersUtil.extractExamples(theParameterAnnotations);
		return new OperationParameter(
						theContext,
						Constants.EXTOP_VALIDATE,
						Constants.EXTOP_VALIDATE_MODE,
						0,
						1,
						description,
						examples)
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
	}

	/**
	 * Creates an OperationParameter for @Validate.Profile annotation.
	 *
	 * @param theContext the FHIR context
	 * @param theParameterType the parameter type
	 * @param theParameterAnnotations all annotations on the parameter
	 * @return configured OperationParameter with profile converter
	 * @throws ConfigurationException if parameter type is not String
	 */
	private static IParameter createValidationProfileParameter(
			FhirContext theContext, Class<?> theParameterType, Annotation[] theParameterAnnotations) {
		if (!theParameterType.equals(String.class)) {
			throw new ConfigurationException(Msg.code(407) + "Parameter annotated with @"
					+ Validate.class.getSimpleName() + "." + Validate.Profile.class.getSimpleName()
					+ " must be of type " + String.class.getName());
		}

		String description = ParametersUtil.extractDescription(theParameterAnnotations);
		List<String> examples = ParametersUtil.extractExamples(theParameterAnnotations);
		return new OperationParameter(
						theContext,
						Constants.EXTOP_VALIDATE,
						Constants.EXTOP_VALIDATE_PROFILE,
						0,
						1,
						description,
						examples)
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
	}

	/**
	 * Creates parameters for simple annotations that require minimal configuration.
	 * These are straightforward one-to-one mappings from annotation to parameter type.
	 *
	 * @param theAnnotation the annotation to process
	 * @param theContext the FHIR context
	 * @param theParameterType the parameter type
	 * @param theInnerCollectionType the inner collection type (may be null)
	 * @param theOuterCollectionType the outer collection type (may be null)
	 * @param theParameters the current parameters list (for RawParam)
	 * @return the created IParameter, or null if annotation not recognized
	 */
	private static IParameter createSimpleAnnotationParameter(
			Annotation theAnnotation,
			FhirContext theContext,
			Class<?> theParameterType,
			Class<? extends java.util.Collection<?>> theInnerCollectionType,
			Class<? extends java.util.Collection<?>> theOuterCollectionType,
			List<IParameter> theParameters) {
		if (theAnnotation instanceof IdParam) {
			return new NullParameter();
		} else if (theAnnotation instanceof ServerBase) {
			return new ServerBaseParamBinder();
		} else if (theAnnotation instanceof Elements) {
			return new ElementsParameter();
		} else if (theAnnotation instanceof Since) {
			SinceParameter param = new SinceParameter();
			param.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
			return param;
		} else if (theAnnotation instanceof At) {
			AtParameter param = new AtParameter();
			param.setType(theContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
			return param;
		} else if (theAnnotation instanceof Count) {
			return new CountParameter();
		} else if (theAnnotation instanceof Offset) {
			return new OffsetParameter();
		} else if (theAnnotation instanceof GraphQLQueryUrl) {
			return new GraphQLQueryUrlParameter();
		} else if (theAnnotation instanceof GraphQLQueryBody) {
			return new GraphQLQueryBodyParameter();
		} else if (theAnnotation instanceof Sort) {
			return new SortParameter(theContext);
		} else if (theAnnotation instanceof TransactionParam) {
			return new TransactionParameter(theContext);
		} else if (theAnnotation instanceof ConditionalUrlParam) {
			return new ConditionalParamBinder(((ConditionalUrlParam) theAnnotation).supportsMultiple());
		} else if (theAnnotation instanceof RawParam) {
			return new RawParamsParameter(theParameters);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(
			final FhirContext theContext, Method theMethod, Object theProvider) {
		List<IParameter> parameters = new ArrayList<>();

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] nextParameterAnnotations : theMethod.getParameterAnnotations()) {

			IParameter param = null;
			Class<?> originalParameterType = parameterTypes[paramIndex];
			Class<?> parameterType;
			Class<?> declaredParameterType;
			Class<? extends java.util.Collection<?>> innerCollectionType;
			Class<? extends java.util.Collection<?>> outerCollectionType;

			// Handle TagList special case
			if (TagList.class.isAssignableFrom(originalParameterType)) {
				// TagList is handled directly within the method bindings
				param = new NullParameter();
				// Set dummy values for variables used later
				parameterType = originalParameterType;
				declaredParameterType = originalParameterType;
				innerCollectionType = null;
				outerCollectionType = null;
			} else {
				// Detect and unwrap collection types
				CollectionTypeInfo collectionInfo = detectCollectionTypes(theMethod, paramIndex, originalParameterType);
				parameterType = collectionInfo.effectiveParameterType;
				declaredParameterType = collectionInfo.declaredParameterType;
				innerCollectionType = collectionInfo.innerCollectionType;
				outerCollectionType = collectionInfo.outerCollectionType;

				// Resolve IPrimitiveType to concrete implementation for version compatibility
				parameterType = resolvePrimitiveType(theContext, theMethod, paramIndex, parameterType);

				// Try type-based parameter creation first
				param = createTypeBasedParameter(parameterType);

				if (param == null) {
					// Try annotation-based parameter creation
					for (int i = 0; i < nextParameterAnnotations.length && param == null; i++) {
						Annotation nextAnnotation = nextParameterAnnotations[i];

						// Try complex annotations first
						if (nextAnnotation instanceof RequiredParam || nextAnnotation instanceof OptionalParam) {
							param = createSearchParameter(
									nextAnnotation,
									theContext,
									parameterType,
									innerCollectionType,
									outerCollectionType,
									nextParameterAnnotations);
						} else if (nextAnnotation instanceof IncludeParam) {
							param = createIncludeParameter(
									(IncludeParam) nextAnnotation,
									theMethod,
									parameterType,
									innerCollectionType,
									outerCollectionType);
						} else if (nextAnnotation instanceof ResourceParam) {
							param = createResourceParameter(theMethod, theProvider, parameterType);
						} else if (nextAnnotation instanceof OperationParam) {
							OperationParamResult result = createOperationParameter(
									(OperationParam) nextAnnotation,
									theMethod,
									theContext,
									parameterType, // Use current parameterType (already resolved from IPrimitiveType)
									nextParameterAnnotations);
							param = result.parameter;
							parameterType = result.resolvedType;
						} else if (nextAnnotation instanceof Validate.Mode) {
							param = createValidationModeParameter(theContext, parameterType, nextParameterAnnotations);
						} else if (nextAnnotation instanceof Validate.Profile) {
							param = createValidationProfileParameter(
									theContext, parameterType, nextParameterAnnotations);
						} else {
							// Try simple annotations
							param = createSimpleAnnotationParameter(
									nextAnnotation,
									theContext,
									parameterType,
									innerCollectionType,
									outerCollectionType,
									parameters);
						}
					}
				}
			}

			if (param == null) {
				throw new ConfigurationException(
						Msg.code(408) + "Parameter #" + ((paramIndex + 1)) + "/" + (parameterTypes.length)
								+ " of method '" + theMethod.getName() + "' on type '"
								+ theMethod.getDeclaringClass().getCanonicalName()
								+ "' has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
			}

			param.initializeTypes(theMethod, outerCollectionType, innerCollectionType, parameterType);
			parameters.add(param);

			paramIndex++;
		}
		return parameters;
	}
}
