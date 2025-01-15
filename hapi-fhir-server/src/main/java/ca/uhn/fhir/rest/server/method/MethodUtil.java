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
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(
			final FhirContext theContext, Method theMethod, Object theProvider) {
		ourLog.info("1234: getResourceParameters: " + theMethod.getName());
		List<IParameter> parameters = new ArrayList<>();

		// LUKETODO:  why no caregaps here????
		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;
		// LUKETODO:  one param per method parameter:  what happens if we expand this?

		// LUKETODO:  UNIT TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		final List<Class<?>> operationEmbeddedTypes = Arrays.stream(parameterTypes)
			.filter(paramType -> paramType.isAnnotationPresent(OperationEmbeddedType.class))
			.collect(Collectors.toUnmodifiableList());

		if (! operationEmbeddedTypes.isEmpty()) {
			ourLog.info("1234: isOperationEmbeddedType!!!!!!! method: {}", theMethod.getName());

			// This is the @Operation parameter on the method itself (ex: evaluateMeasure)
			final Operation op = theMethod.getAnnotation(Operation.class);

			if (operationEmbeddedTypes.size() > 1) {
				// LUKETODO:  error
				throw new ConfigurationException(
					String.format("%sOnly one OperationEmbeddedType is supported for now for method: %s", Msg.code(99999), theMethod.getName()));
			}

			// LUKETODO:  handle multiple RequestDetails with an error

			for (Class<?> parameterType : parameterTypes) {
				final IParameter param;
				// If either the first or second parameter is a RequestDetails, handle it
				if (parameterType.equals(RequestDetails.class) || parameterType.equals(ServletRequestDetails.class)) {
					parameters.add(new RequestDetailsParameter());
				} else { // LUKETODO:  specific check here?
					// LUKETODO:  limit to a single Params object
					final Field[] fields = parameterType.getDeclaredFields();

					for (Field field : fields) {
						final String fieldName = field.getName();
						final Class<?> fieldType = field.getType();
						final Annotation[] fieldAnnotations = field.getAnnotations();

						if (fieldAnnotations.length < 1) {
							throw new ConfigurationException(String.format("%sNo annotations for field: %s for method: %s", Msg.code(99999), fieldName, theMethod.getName()));
						}

						if (fieldAnnotations.length > 1) {
							// LUKETODO:  error
							throw new ConfigurationException(String.format("%sMore than one annotation for field: %s for method: %s", Msg.code(99999), fieldName, theMethod.getName()));
						}

						final Set<String> annotationClassNames = Arrays.stream(fieldAnnotations)
								.map(Annotation::annotationType)
								.map(Class::getName)
								.collect(Collectors.toUnmodifiableSet());

						ourLog.info(
								"1234: MethodUtil:  OperationEmbeddedType: fieldName: {}, class: {}, fieldAnnotations: {}",
								fieldName,
								fieldType.getName(),
								annotationClassNames);


						// This is the parameter on the field in question on the OperationEmbeddedType class:  ex myCount
						final Annotation fieldAnnotation = fieldAnnotations[0];

						// LUKETODO:  what if this is not a IdParam or an OperationParam?
						if (fieldAnnotation instanceof IdParam) {
							parameters.add(new NullParameter());
						} else if (fieldAnnotation instanceof OperationEmbeddedParam) {
							// LUKETODO:  use OperationEmbeddedParam instead
							final OperationEmbeddedParam operationParam = (OperationEmbeddedParam) fieldAnnotation;

							final Annotation[] fieldAnnotationArray = new Annotation[] {fieldAnnotation};
							final String description = ParametersUtil.extractDescription(fieldAnnotationArray);
							final List<String> examples = ParametersUtil.extractExamples(fieldAnnotationArray);

							// LUKETODO:  capabilities statemenet provider
							// LUKETODO:  consider taking  ALL  hapi-fhir storage-cr INTO the clinical-reasoning repo
							final OperationEmbeddedParameter operationParameter = new OperationEmbeddedParameter(
									theContext,
									op.name(),
									operationParam.name(),
									operationParam.min(),
									operationParam.max(),
									description,
									examples);

							// Not sure what these are, but I think they're for params that are part of a Collection parameter
							// and may have soemthing to do with a SearchParameter
							final Class<? extends java.util.Collection<?>> outerCollectionType = null;
							final Class<? extends java.util.Collection<?>> innerCollectionType = null;

							operationParameter.initializeTypes(theMethod, outerCollectionType, innerCollectionType, fieldType);

							parameters.add(operationParameter);
						} else {
							throw new ConfigurationException(Msg.code(99999) + "Unsupported param fieldType: " + fieldAnnotation);
						}
					}
				}
			}

			// LUKETODO:  short-circuit for now
			return parameters;
		}

		for (Annotation[] nextParameterAnnotations : theMethod.getParameterAnnotations()) {

			IParameter param = null;
			Class<?> declaredParameterType = parameterTypes[paramIndex];
			Class<?> parameterType = declaredParameterType;

			Class<? extends java.util.Collection<?>> outerCollectionType = null;
			Class<? extends java.util.Collection<?>> innerCollectionType = null;
			if (TagList.class.isAssignableFrom(parameterType)) {
				// TagList is handled directly within the method bindings
				param = new NullParameter();
			} else {
				if (Collection.class.isAssignableFrom(parameterType)) {
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
					if (parameterType == null && theMethod.getDeclaringClass().isSynthetic()) {
						try {
							theMethod = theMethod
									.getDeclaringClass()
									.getSuperclass()
									.getMethod(theMethod.getName(), parameterTypes);
							parameterType =
									ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
						} catch (NoSuchMethodException e) {
							throw new ConfigurationException(Msg.code(400) + "A method with name '"
									+ theMethod.getName() + "' does not exist for super class '"
									+ theMethod.getDeclaringClass().getSuperclass() + "'");
						}
					}
					declaredParameterType = parameterType;
				}
				// LUKETODO:  now we're processing the generic parameter, so capture the inner and outer types
				// Collection<X>
				// LUKETODO:  using reflection, find the
				if (Collection.class.isAssignableFrom(parameterType)) {
					outerCollectionType = innerCollectionType;
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
					declaredParameterType = parameterType;
				}
				// LUKETODO:  as a guard:  if this is still a Collection, then throw because something went wrong
				if (Collection.class.isAssignableFrom(parameterType)) {
					throw new ConfigurationException(
							Msg.code(401) + "Argument #" + paramIndex + " of Method '" + theMethod.getName()
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
					Class<?> genericType =
							ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
					if (Date.class.equals(genericType)) {
						BaseRuntimeElementDefinition<?> dateTimeDef = theContext.getElementDefinition("dateTime");
						parameterType = dateTimeDef.getImplementingClass();
					} else if (String.class.equals(genericType) || genericType == null) {
						BaseRuntimeElementDefinition<?> dateTimeDef = theContext.getElementDefinition("string");
						parameterType = dateTimeDef.getImplementingClass();
					}
				}
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
					Annotation nextAnnotation = nextParameterAnnotations[i];

					if (nextAnnotation instanceof RequiredParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((RequiredParam) nextAnnotation).name());
						parameter.setRequired(true);
						parameter.setDeclaredTypes(((RequiredParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((RequiredParam) nextAnnotation).compositeTypes());
						parameter.setChainLists(
								((RequiredParam) nextAnnotation).chainWhitelist(),
								((RequiredParam) nextAnnotation).chainBlacklist());
						parameter.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
						MethodUtil.extractDescription(parameter, nextParameterAnnotations);
						param = parameter;
					} else if (nextAnnotation instanceof OptionalParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((OptionalParam) nextAnnotation).name());
						parameter.setRequired(false);
						parameter.setDeclaredTypes(((OptionalParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((OptionalParam) nextAnnotation).compositeTypes());
						parameter.setChainLists(
								((OptionalParam) nextAnnotation).chainWhitelist(),
								((OptionalParam) nextAnnotation).chainBlacklist());
						parameter.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
						MethodUtil.extractDescription(parameter, nextParameterAnnotations);
						param = parameter;
					} else if (nextAnnotation instanceof RawParam) {
						param = new RawParamsParameter(parameters);
					} else if (nextAnnotation instanceof IncludeParam) {
						Class<? extends Collection<Include>> instantiableCollectionType;
						Class<?> specType;

						if (parameterType == String.class) {
							instantiableCollectionType = null;
							specType = String.class;
						} else if ((parameterType != Include.class)
								|| innerCollectionType == null
								|| outerCollectionType != null) {
							throw new ConfigurationException(Msg.code(402) + "Method '" + theMethod.getName()
									+ "' is annotated with @" + IncludeParam.class.getSimpleName()
									+ " but has a type other than Collection<" + Include.class.getSimpleName() + ">");
						} else {
							instantiableCollectionType = (Class<? extends Collection<Include>>)
									CollectionBinder.getInstantiableCollectionType(
											innerCollectionType, "Method '" + theMethod.getName() + "'");
							specType = parameterType;
						}

						param = new IncludeParameter(
								(IncludeParam) nextAnnotation, instantiableCollectionType, specType);
					} else if (nextAnnotation instanceof ResourceParam) {
						Mode mode;
						if (IBaseResource.class.isAssignableFrom(parameterType)) {
							mode = Mode.RESOURCE;
						} else if (String.class.equals(parameterType)) {
							mode = ResourceParameter.Mode.BODY;
						} else if (byte[].class.equals(parameterType)) {
							mode = ResourceParameter.Mode.BODY_BYTE_ARRAY;
						} else if (EncodingEnum.class.equals(parameterType)) {
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
						param = new ResourceParameter(
								(Class<? extends IBaseResource>) parameterType,
								theProvider,
								mode,
								methodIsOperation,
								methodIsPatch);
					} else if (nextAnnotation instanceof IdParam) {
						param = new NullParameter();
					} else if (nextAnnotation instanceof ServerBase) {
						param = new ServerBaseParamBinder();
					} else if (nextAnnotation instanceof Elements) {
						param = new ElementsParameter();
					} else if (nextAnnotation instanceof Since) {
						param = new SinceParameter();
						((SinceParameter) param)
								.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
					} else if (nextAnnotation instanceof At) {
						param = new AtParameter();
						((AtParameter) param)
								.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
					} else if (nextAnnotation instanceof Count) {
						param = new CountParameter();
					} else if (nextAnnotation instanceof Offset) {
						param = new OffsetParameter();
					} else if (nextAnnotation instanceof GraphQLQueryUrl) {
						param = new GraphQLQueryUrlParameter();
					} else if (nextAnnotation instanceof GraphQLQueryBody) {
						param = new GraphQLQueryBodyParameter();
					} else if (nextAnnotation instanceof Sort) {
						param = new SortParameter(theContext);
					} else if (nextAnnotation instanceof TransactionParam) {
						param = new TransactionParameter(theContext);
					} else if (nextAnnotation instanceof ConditionalUrlParam) {
						param = new ConditionalParamBinder(((ConditionalUrlParam) nextAnnotation).supportsMultiple());
					} else if (nextAnnotation instanceof OperationParam) {
						Operation op = theMethod.getAnnotation(Operation.class);
						if (op == null) {
							throw new ConfigurationException(Msg.code(404)
									+ "@OperationParam detected on method that is not annotated with @Operation: "
									+ theMethod.toGenericString());
						}

						OperationParam operationParam = (OperationParam) nextAnnotation;
						String description = ParametersUtil.extractDescription(nextParameterAnnotations);
						List<String> examples = ParametersUtil.extractExamples(nextParameterAnnotations);

						param = new OperationParameter(
								theContext,
								op.name(),
								operationParam.name(),
								operationParam.min(),
								operationParam.max(),
								description,
								examples);
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
							if (!declaredParameterType.isAssignableFrom(newParameterType)) {
								throw new ConfigurationException(Msg.code(405) + "Non assignable parameter typeName=\""
										+ operationParam.typeName() + "\" specified on method " + theMethod);
							}
							parameterType = newParameterType;
						}
					} else if (nextAnnotation instanceof Validate.Mode) {
						if (!parameterType.equals(ValidationModeEnum.class)) {
							throw new ConfigurationException(Msg.code(406) + "Parameter annotated with @"
									+ Validate.class.getSimpleName() + "." + Validate.Mode.class.getSimpleName()
									+ " must be of type " + ValidationModeEnum.class.getName());
						}
						String description = ParametersUtil.extractDescription(nextParameterAnnotations);
						List<String> examples = ParametersUtil.extractExamples(nextParameterAnnotations);
						param = new OperationParameter(
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
											ValidationModeEnum retVal =
													ValidationModeEnum.forCode(theObject.toString());
											if (retVal == null) {
												OperationParameter.throwInvalidMode(theObject.toString());
											}
											return retVal;
										}
										return null;
									}

									@Override
									public Object outgoingClient(Object theObject) {
										return ParametersUtil.createString(
												theContext, ((ValidationModeEnum) theObject).getCode());
									}
								});
					} else if (nextAnnotation instanceof Validate.Profile) {
						if (!parameterType.equals(String.class)) {
							throw new ConfigurationException(Msg.code(407) + "Parameter annotated with @"
									+ Validate.class.getSimpleName() + "." + Validate.Profile.class.getSimpleName()
									+ " must be of type " + String.class.getName());
						}
						String description = ParametersUtil.extractDescription(nextParameterAnnotations);
						List<String> examples = ParametersUtil.extractExamples(nextParameterAnnotations);
						param = new OperationParameter(
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
					} else {
						continue;
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

			// LUKETODO:  if we call this with an @OperationEmbeddedType, we get an Exceptioon here
			// LUKETODO:  Or do we expand the paramters here, and then foreaach parameters.add() ???
			//			ourLog.info("1234: param class: {}, method: {}", param.getClass().getCanonicalName(),
			// theMethod.getName());
			param.initializeTypes(theMethod, outerCollectionType, innerCollectionType, parameterType);
			parameters.add(param);

			paramIndex++;
		}
		return parameters;
	}
}
