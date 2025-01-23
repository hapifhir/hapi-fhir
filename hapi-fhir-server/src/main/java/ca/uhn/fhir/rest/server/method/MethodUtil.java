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
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParam;
import ca.uhn.fhir.rest.annotation.EmbeddedOperationParams;
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

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(
			final FhirContext theContext, final Method theMethod, Object theProvider) {
		// We mutate this variable so distinguish this from the argument to getResourceParameters
		Method methodToUse = theMethod;
		List<IParameter> parameters = new ArrayList<>();

		Class<?>[] parameterTypes = methodToUse.getParameterTypes();
		int paramIndex = 0;

		for (Annotation[] nextParameterAnnotations : methodToUse.getParameterAnnotations()) {
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
				if (Collection.class.isAssignableFrom(parameterType)) {
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(methodToUse, paramIndex);
					if (parameterType == null && methodToUse.getDeclaringClass().isSynthetic()) {
						try {
							methodToUse = methodToUse
									.getDeclaringClass()
									.getSuperclass()
									.getMethod(methodToUse.getName(), parameterTypes);
							parameterType =
									ReflectionUtil.getGenericCollectionTypeOfMethodParameter(methodToUse, paramIndex);
						} catch (NoSuchMethodException e) {
							throw new ConfigurationException(Msg.code(400) + "A method with name '"
									+ methodToUse.getName() + "' does not exist for super class '"
									+ methodToUse.getDeclaringClass().getSuperclass() + "'");
						}
					}
					declaredParameterType = parameterType;
				}

				if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
					outerCollectionType = innerCollectionType;
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(methodToUse, paramIndex);
					declaredParameterType = parameterType;
				}
				if (parameterType == null || Collection.class.isAssignableFrom(parameterType)) {
					throw new ConfigurationException(
							Msg.code(401) + "Argument #" + paramIndex + " of Method '" + methodToUse.getName()
									+ "' in type '"
									+ methodToUse.getDeclaringClass().getCanonicalName()
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
							ReflectionUtil.getGenericCollectionTypeOfMethodParameter(methodToUse, paramIndex);
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
				final Operation op = methodToUse.getAnnotation(Operation.class);
				// LUKETODO:  delete this after all existing providers have migrated.
				// There are no annotations on this parameter, so we check to see if the parameter class has fields
				// annotated OperationEmbeddedParam
				if (nextParameterAnnotations.length == 0) {
					final List<Class<?>> operationEmbeddedTypes =
							ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(
									methodToUse, EmbeddedOperationParam.class);

					if (op == null) {
						throw new ConfigurationException(Msg.code(846192641)
								+ "@OperationParam or OperationEmbeddedParam detected on method that is not annotated with @Operation: "
								+ methodToUse.toGenericString());
					}

					if (operationEmbeddedTypes.size() > 1) {
						throw new ConfigurationException(String.format(
								"%sOnly one type with embedded params is supported for now for method: %s",
								Msg.code(9999927), methodToUse.getName()));
					}

					if (!operationEmbeddedTypes.isEmpty()) {
						final EmbeddedParameterConverter embeddedParameterConverter = new EmbeddedParameterConverter(
								theContext, theMethod, op, operationEmbeddedTypes.get(0));

						final List<EmbeddedParameterConverterContext> outerContexts =
								embeddedParameterConverter.convert();

						for (EmbeddedParameterConverterContext outerContext : outerContexts) {
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
					} else {
						// More than likely this will result in the param == null Exception below
						ourLog.warn(
								"Method '{}' has no parameters with annotations. Don't know how to handle this parameter",
								methodToUse.getName());
					}
				} // else there are no embedded params and let execution fall to the for loop below

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
							throw new ConfigurationException(Msg.code(402) + "Method '" + methodToUse.getName()
									+ "' is annotated with @" + IncludeParam.class.getSimpleName()
									+ " but has a type other than Collection<" + Include.class.getSimpleName() + ">");
						} else {
							instantiableCollectionType = (Class<? extends Collection<Include>>)
									CollectionBinder.getInstantiableCollectionType(
											innerCollectionType, "Method '" + methodToUse.getName() + "'");
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
							b.append(methodToUse.getName());
							b.append("' is annotated with @");
							b.append(ResourceParam.class.getSimpleName());
							b.append(" but has a type that is not an implementation of ");
							b.append(IBaseResource.class.getCanonicalName());
							b.append(" or String or byte[]");
							throw new ConfigurationException(Msg.code(403) + b.toString());
						}
						boolean methodIsOperation = methodToUse.getAnnotation(Operation.class) != null;
						boolean methodIsPatch = methodToUse.getAnnotation(Patch.class) != null;
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
						if (op == null) {
							throw new ConfigurationException(Msg.code(404)
									+ "@OperationParam detected on method that is not annotated with @Operation: "
									+ methodToUse.toGenericString());
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
										+ operationParam.typeName() + "\" specified on method " + methodToUse);
							}
							parameterType = newParameterType;
						}
					} else if (nextAnnotation instanceof EmbeddedOperationParams) {
						final List<Class<?>> operationEmbeddedTypes =
								ReflectionUtil.getMethodParamsWithClassesWithFieldsWithAnnotation(
										methodToUse, EmbeddedOperationParam.class);

						if (op == null) {
							throw new ConfigurationException(Msg.code(846192641)
									+ "@OperationParam or OperationEmbeddedParam detected on method that is not annotated with @Operation: "
									+ methodToUse.toGenericString());
						}

						if (operationEmbeddedTypes.size() > 1) {
							throw new ConfigurationException(String.format(
									"%sOnly one type with embedded params is supported for now for method: %s",
									Msg.code(9999927), methodToUse.getName()));
						}

						if (!operationEmbeddedTypes.isEmpty()) {
							final EmbeddedParameterConverter embeddedParameterConverter =
									new EmbeddedParameterConverter(
											theContext, theMethod, op, operationEmbeddedTypes.get(0));

							final List<EmbeddedParameterConverterContext> outerContexts =
									embeddedParameterConverter.convert();

							for (EmbeddedParameterConverterContext outerContext : outerContexts) {
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
						} else {
							// More than likely this will result in the param == null Exception below
							ourLog.warn(
									"Method '{}' has no parameters with annotations. Don't know how to handle this parameter",
									methodToUse.getName());
						}
						ourLog.info("1234:  NEW CODE PATH!!!!!");
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
					} else {
						if (nextAnnotation instanceof Validate.Profile) {
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
						}
					}
				}
			}

			if (paramContexts.isEmpty() || !(param instanceof EmbeddedOperationParameter)) {
				// RequestDetails if it's last
				paramContexts.add(
						new ParamInitializationContext(param, parameterType, outerCollectionType, innerCollectionType));
			}

			if (param == null) {
				throw new ConfigurationException(
						Msg.code(408) + "Parameter #" + (paramIndex + 1) + "/" + (parameterTypes.length)
								+ " of method '" + methodToUse.getName() + "' on type '"
								+ methodToUse.getDeclaringClass().getCanonicalName()
								+ "' has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
			}

			for (ParamInitializationContext paramContext : paramContexts) {
				paramContext.initialize(methodToUse);
				parameters.add(paramContext.getParam());
			}

			paramIndex++;
		}
		return parameters;
	}
}
