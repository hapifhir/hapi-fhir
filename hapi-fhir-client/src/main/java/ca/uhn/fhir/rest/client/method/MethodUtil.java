package ca.uhn.fhir.rest.client.method;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.method.OperationParameter.IOperationParamConverter;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.binder.CollectionBinder;
import ca.uhn.fhir.util.DateUtils;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class MethodUtil {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MethodUtil.class);

	/** Non instantiable */
	private MethodUtil() {
		// nothing
	}

	public static void addAcceptHeaderToRequest(EncodingEnum theEncoding, IHttpRequest theHttpRequest,
			FhirContext theContext) {
		if (theEncoding == null) {
			if (theContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2_1) == false) {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);
			} else {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
			}
		} else if (theEncoding == EncodingEnum.JSON) {
			if (theContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2_1) == false) {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);
			} else {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
			}
		} else if (theEncoding == EncodingEnum.XML) {
			if (theContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2_1) == false) {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML);
			} else {
				theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY);
			}
		}

	}

	public static HttpGetClientInvocation createConformanceInvocation(FhirContext theContext) {
		return new HttpGetClientInvocation(theContext, "metadata");
	}

	public static HttpPostClientInvocation createCreateInvocation(IBaseResource theResource, FhirContext theContext) {
		return createCreateInvocation(theResource, null, theContext);
	}

	public static HttpPostClientInvocation createCreateInvocation(IBaseResource theResource, String theResourceBody,
			FhirContext theContext) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);
		String resourceName = def.getName();

		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);

		HttpPostClientInvocation retVal;
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPostClientInvocation(theContext, theResource, urlExtension.toString());
		} else {
			retVal = new HttpPostClientInvocation(theContext, theResourceBody, false, urlExtension.toString());
		}

		retVal.setOmitResourceId(true);

		return retVal;
	}

	public static HttpPostClientInvocation createCreateInvocation(IBaseResource theResource, String theResourceBody,
			FhirContext theContext, Map<String, List<String>> theIfNoneExistParams) {
		HttpPostClientInvocation retVal = createCreateInvocation(theResource, theResourceBody, theContext);
		retVal.setIfNoneExistParams(theIfNoneExistParams);
		return retVal;
	}

	public static HttpPostClientInvocation createCreateInvocation(IBaseResource theResource, String theResourceBody,
			FhirContext theContext, String theIfNoneExistUrl) {
		HttpPostClientInvocation retVal = createCreateInvocation(theResource, theResourceBody, theContext);
		retVal.setIfNoneExistString(theIfNoneExistUrl);
		return retVal;
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, IIdType theId,
			PatchTypeEnum thePatchType, String theBody) {
		return PatchMethodBinding.createPatchInvocation(theContext, theId, thePatchType, theBody);
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, PatchTypeEnum thePatchType,
			String theBody, String theResourceType, Map<String, List<String>> theMatchParams) {
		return PatchMethodBinding.createPatchInvocation(theContext, thePatchType, theBody, theResourceType,
				theMatchParams);
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, String theUrl,
			PatchTypeEnum thePatchType, String theBody) {
		return PatchMethodBinding.createPatchInvocation(theContext, theUrl, thePatchType, theBody);
	}

	public static HttpPutClientInvocation createUpdateInvocation(FhirContext theContext, IBaseResource theResource,
			String theResourceBody, Map<String, List<String>> theMatchParams) {
		String resourceType = theContext.getResourceType(theResource);

		StringBuilder b = createUrl(resourceType, theMatchParams);

		HttpPutClientInvocation retVal;
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPutClientInvocation(theContext, theResource, b.toString());
		} else {
			retVal = new HttpPutClientInvocation(theContext, theResourceBody, false, b.toString());
		}

		return retVal;
	}

	public static HttpPutClientInvocation createUpdateInvocation(FhirContext theContext, IBaseResource theResource,
			String theResourceBody, String theMatchUrl) {
		HttpPutClientInvocation retVal;
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPutClientInvocation(theContext, theResource, theMatchUrl);
		} else {
			retVal = new HttpPutClientInvocation(theContext, theResourceBody, false, theMatchUrl);
		}

		return retVal;
	}

	public static HttpPutClientInvocation createUpdateInvocation(IBaseResource theResource, String theResourceBody,
			IIdType theId, FhirContext theContext) {
		String resourceName = theContext.getResourceType(theResource);
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(resourceName);
		urlBuilder.append('/');
		urlBuilder.append(theId.getIdPart());
		String urlExtension = urlBuilder.toString();

		HttpPutClientInvocation retVal;
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPutClientInvocation(theContext, theResource, urlExtension);
		} else {
			retVal = new HttpPutClientInvocation(theContext, theResourceBody, false, urlExtension);
		}

		retVal.setForceResourceId(theId);

		if (theId.hasVersionIdPart()) {
			retVal.addHeader(Constants.HEADER_IF_MATCH, '"' + theId.getVersionIdPart() + '"');
		}

		return retVal;
	}

	public static StringBuilder createUrl(String theResourceType, Map<String, List<String>> theMatchParams) {
		StringBuilder b = new StringBuilder();

		b.append(theResourceType);

		boolean haveQuestionMark = false;
		for (Entry<String, List<String>> nextEntry : theMatchParams.entrySet()) {
			for (String nextValue : nextEntry.getValue()) {
				b.append(haveQuestionMark ? '&' : '?');
				haveQuestionMark = true;
				b.append(UrlUtil.escapeUrlParam(nextEntry.getKey()));
				b.append('=');
				b.append(UrlUtil.escapeUrlParam(nextValue));
			}
		}
		return b;
	}

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(final FhirContext theContext, Method theMethod,
			Object theProvider, RestOperationTypeEnum theRestfulOperationTypeEnum) {
		List<IParameter> parameters = new ArrayList<>();

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {

			IParameter param = null;
			Class<?> parameterType = parameterTypes[paramIndex];
			Class<? extends java.util.Collection<?>> outerCollectionType = null;
			Class<? extends java.util.Collection<?>> innerCollectionType = null;
			if (TagList.class.isAssignableFrom(parameterType)) {
				// TagList is handled directly within the method bindings
				param = new NullParameter();
			} else {
				if (Collection.class.isAssignableFrom(parameterType)) {
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
				}
				if (Collection.class.isAssignableFrom(parameterType)) {
					outerCollectionType = innerCollectionType;
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
				}
				if (Collection.class.isAssignableFrom(parameterType)) {
					throw new ConfigurationException(Msg.code(1433) + "Argument #" + paramIndex + " of Method '" + theMethod.getName()
							+ "' in type '" + theMethod.getDeclaringClass().getCanonicalName()
							+ "' is of an invalid generic type (can not be a collection of a collection of a collection)");
				}
			}

			if (parameterType.equals(SummaryEnum.class)) {
				param = new SummaryEnumParameter();
			} else if (parameterType.equals(PatchTypeEnum.class)) {
				param = new PatchTypeParameter();
			} else {
				for (int i = 0; i < annotations.length && param == null; i++) {
					Annotation nextAnnotation = annotations[i];

					if (nextAnnotation instanceof RequiredParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((RequiredParam) nextAnnotation).name());
						parameter.setRequired(true);
						parameter.setDeclaredTypes(((RequiredParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((RequiredParam) nextAnnotation).compositeTypes());
						parameter.setChainlists(((RequiredParam) nextAnnotation).chainWhitelist());
						parameter.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
						param = parameter;
					} else if (nextAnnotation instanceof OptionalParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((OptionalParam) nextAnnotation).name());
						parameter.setRequired(false);
						parameter.setDeclaredTypes(((OptionalParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((OptionalParam) nextAnnotation).compositeTypes());
						parameter.setChainlists(((OptionalParam) nextAnnotation).chainWhitelist());
						parameter.setType(theContext, parameterType, innerCollectionType, outerCollectionType);
						param = parameter;
					} else if (nextAnnotation instanceof RawParam) {
						param = new RawParamsParmeter();
					} else if (nextAnnotation instanceof IncludeParam) {
						Class<? extends Collection<Include>> instantiableCollectionType;
						Class<?> specType;

						if (parameterType == String.class) {
							instantiableCollectionType = null;
							specType = String.class;
						} else if ((parameterType != Include.class) || innerCollectionType == null
								|| outerCollectionType != null) {
							throw new ConfigurationException(Msg.code(1434) + "Method '" + theMethod.getName() + "' is annotated with @"
									+ IncludeParam.class.getSimpleName() + " but has a type other than Collection<"
									+ Include.class.getSimpleName() + ">");
						} else {
							instantiableCollectionType = (Class<? extends Collection<Include>>) CollectionBinder
									.getInstantiableCollectionType(innerCollectionType,
											"Method '" + theMethod.getName() + "'");
							specType = parameterType;
						}

						param = new IncludeParameter((IncludeParam) nextAnnotation, instantiableCollectionType,								specType);
					} else if (nextAnnotation instanceof ResourceParam) {
						if (IBaseResource.class.isAssignableFrom(parameterType)) {
							// good
						} else if (String.class.equals(parameterType)) {
							// good
						} else {
							StringBuilder b = new StringBuilder();
							b.append("Method '");
							b.append(theMethod.getName());
							b.append("' is annotated with @");
							b.append(ResourceParam.class.getSimpleName());
							b.append(" but has a type that is not an implementation of ");
							b.append(IBaseResource.class.getCanonicalName());
							throw new ConfigurationException(Msg.code(1435) + b.toString());
						}
						param = new ResourceParameter(parameterType);
					} else if (nextAnnotation instanceof IdParam) {
						param = new NullParameter();
					} else if (nextAnnotation instanceof Elements) {
						param = new ElementsParameter();
					} else if (nextAnnotation instanceof Since) {
						param = new SinceParameter();
						((SinceParameter) param).setType(theContext, parameterType, innerCollectionType,
								outerCollectionType);
					} else if (nextAnnotation instanceof At) {
						param = new AtParameter();
						((AtParameter) param).setType(theContext, parameterType, innerCollectionType,
								outerCollectionType);
					} else if (nextAnnotation instanceof Count) {
						param = new CountParameter();
					} else if (nextAnnotation instanceof Offset) {
						param = new OffsetParameter();
					} else if (nextAnnotation instanceof Sort) {
						param = new SortParameter(theContext);
					} else if (nextAnnotation instanceof TransactionParam) {
						param = new TransactionParameter(theContext);
					} else if (nextAnnotation instanceof ConditionalUrlParam) {
						param = new ConditionalParamBinder(theRestfulOperationTypeEnum,
								((ConditionalUrlParam) nextAnnotation).supportsMultiple());
					} else if (nextAnnotation instanceof OperationParam) {
						Operation op = theMethod.getAnnotation(Operation.class);
						param = new OperationParameter(theContext, op.name(), ((OperationParam) nextAnnotation));
					} else if (nextAnnotation instanceof Validate.Mode) {
						if (parameterType.equals(ValidationModeEnum.class) == false) {
							throw new ConfigurationException(Msg.code(1436) + "Parameter annotated with @"
									+ Validate.class.getSimpleName() + "." + Validate.Mode.class.getSimpleName()
									+ " must be of type " + ValidationModeEnum.class.getName());
						}
						param = new OperationParameter(theContext, Constants.EXTOP_VALIDATE,
								Constants.EXTOP_VALIDATE_MODE, 0, 1).setConverter(new IOperationParamConverter() {
									@Override
									public Object outgoingClient(Object theObject) {
										return ParametersUtil.createString(theContext,
												((ValidationModeEnum) theObject).getCode());
									}
								});
					} else if (nextAnnotation instanceof Validate.Profile) {
						if (parameterType.equals(String.class) == false) {
							throw new ConfigurationException(Msg.code(1437) + "Parameter annotated with @"
									+ Validate.class.getSimpleName() + "." + Validate.Profile.class.getSimpleName()
									+ " must be of type " + String.class.getName());
						}
						param = new OperationParameter(theContext, Constants.EXTOP_VALIDATE,
								Constants.EXTOP_VALIDATE_PROFILE, 0, 1).setConverter(new IOperationParamConverter() {

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
				throw new ConfigurationException(Msg.code(1438) + "Parameter #" + ((paramIndex + 1)) + "/" + (parameterTypes.length)
						+ " of method '" + theMethod.getName() + "' on type '"
						+ theMethod.getDeclaringClass().getCanonicalName()
						+ "' has no recognized FHIR interface parameter annotations. Don't know how to handle this parameter");
			}

			param.initializeTypes(theMethod, outerCollectionType, innerCollectionType, parameterType);
			parameters.add(param);

			paramIndex++;
		}
		return parameters;
	}

	public static void parseClientRequestResourceHeaders(IIdType theRequestedId, Map<String, List<String>> theHeaders,
			IBaseResource resource) {
		List<String> lmHeaders = theHeaders.get(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
		if (lmHeaders != null && lmHeaders.size() > 0 && StringUtils.isNotBlank(lmHeaders.get(0))) {
			String headerValue = lmHeaders.get(0);
			Date headerDateValue;
			try {
				headerDateValue = DateUtils.parseDate(headerValue);
				if (resource instanceof IResource) {
					IResource iResource = (IResource) resource;
					InstantDt existing = ResourceMetadataKeyEnum.UPDATED.get(iResource);
					if (existing == null || existing.isEmpty()) {
						InstantDt lmValue = new InstantDt(headerDateValue);
						iResource.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, lmValue);
					}
				} else if (resource instanceof IAnyResource) {
					IAnyResource anyResource = (IAnyResource) resource;
					if (anyResource.getMeta().getLastUpdated() == null) {
						anyResource.getMeta().setLastUpdated(headerDateValue);
					}
				}
			} catch (Exception e) {
				ourLog.warn("Unable to parse date string '{}'. Error is: {}", headerValue, e.toString());
			}
		}

		List<String> clHeaders = theHeaders.get(Constants.HEADER_CONTENT_LOCATION_LC);
		if (clHeaders != null && clHeaders.size() > 0 && StringUtils.isNotBlank(clHeaders.get(0))) {
			String headerValue = clHeaders.get(0);
			if (isNotBlank(headerValue)) {
				new IdDt(headerValue).applyTo(resource);
			}
		}

		List<String> locationHeaders = theHeaders.get(Constants.HEADER_LOCATION_LC);
		if (locationHeaders != null && locationHeaders.size() > 0 && StringUtils.isNotBlank(locationHeaders.get(0))) {
			String headerValue = locationHeaders.get(0);
			if (isNotBlank(headerValue)) {
				new IdDt(headerValue).applyTo(resource);
			}
		}

		IdDt existing = IdDt.of(resource);

		List<String> eTagHeaders = theHeaders.get(Constants.HEADER_ETAG_LC);
		String eTagVersion = null;
		if (eTagHeaders != null && eTagHeaders.size() > 0) {
			eTagVersion = ParameterUtil.parseETagValue(eTagHeaders.get(0));
		}
		if (isNotBlank(eTagVersion)) {
			if (existing == null || existing.isEmpty()) {
				if (theRequestedId != null) {
					theRequestedId.withVersion(eTagVersion).applyTo(resource);
				}
			} else if (existing.hasVersionIdPart() == false) {
				existing.withVersion(eTagVersion).applyTo(resource);
			}
		} else if (existing == null || existing.isEmpty()) {
			if (theRequestedId != null) {
				theRequestedId.applyTo(resource);
			}
		}

	}

	public static MethodOutcome process2xxResponse(FhirContext theContext, int theResponseStatusCode,
			String theResponseMimeType, InputStream theResponseReader, Map<String, List<String>> theHeaders) {
		List<String> locationHeaders = new ArrayList<>();
		List<String> lh = theHeaders.get(Constants.HEADER_LOCATION_LC);
		if (lh != null) {
			locationHeaders.addAll(lh);
		}
		List<String> clh = theHeaders.get(Constants.HEADER_CONTENT_LOCATION_LC);
		if (clh != null) {
			locationHeaders.addAll(clh);
		}

		MethodOutcome retVal = new MethodOutcome();
		if (locationHeaders.size() > 0) {
			String locationHeader = locationHeaders.get(0);
			BaseOutcomeReturningMethodBinding.parseContentLocation(theContext, retVal, locationHeader);
		}
		if (theResponseStatusCode != Constants.STATUS_HTTP_204_NO_CONTENT) {
			EncodingEnum ct = EncodingEnum.forContentType(theResponseMimeType);
			if (ct != null) {
				PushbackInputStream reader = new PushbackInputStream(theResponseReader);

				try {
					int firstByte = reader.read();
					if (firstByte == -1) {
						BaseOutcomeReturningMethodBinding.ourLog.debug("No content in response, not going to read");
						reader = null;
					} else {
						reader.unread(firstByte);
					}
				} catch (IOException e) {
					BaseOutcomeReturningMethodBinding.ourLog.debug("No content in response, not going to read", e);
					reader = null;
				}

				if (reader != null) {
					IParser parser = ct.newParser(theContext);
					IBaseResource outcome = parser.parseResource(reader);
					if (outcome instanceof IBaseOperationOutcome) {
						retVal.setOperationOutcome((IBaseOperationOutcome) outcome);
					} else {
						retVal.setResource(outcome);
					}
				}

			} else {
				BaseOutcomeReturningMethodBinding.ourLog.debug("Ignoring response content of type: {}",
						theResponseMimeType);
			}
		}
		return retVal;
	}

}
