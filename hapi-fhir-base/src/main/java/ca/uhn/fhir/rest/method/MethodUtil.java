package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IDynamicSearchResourceProvider;
import ca.uhn.fhir.rest.server.SearchParameterMap;
import ca.uhn.fhir.util.ReflectionUtil;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

public class MethodUtil {
	private static final String LABEL = "label=\"";
	private static final String SCHEME = "scheme=\"";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MethodUtil.class);

	public static HttpPutClientInvocation createUpdateInvocation(IResource theResource, String theResourceBody, IdDt theId, FhirContext theContext) {
		String resourceName = theContext.getResourceDefinition(theResource).getName();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(resourceName);
		urlBuilder.append('/');
		urlBuilder.append(theId.getIdPart());

		HttpPutClientInvocation retVal;
		String urlExtension = urlBuilder.toString();
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPutClientInvocation(theContext, theResource, urlExtension);
		} else {
			retVal = new HttpPutClientInvocation(theContext, theResourceBody, false, urlExtension);
		}

		if (theId.hasVersionIdPart()) {
			String versionId = theId.getVersionIdPart();
			if (StringUtils.isNotBlank(versionId)) {
				urlBuilder.append('/');
				urlBuilder.append(Constants.PARAM_HISTORY);
				urlBuilder.append('/');
				urlBuilder.append(versionId);
				retVal.addHeader(Constants.HEADER_CONTENT_LOCATION, urlBuilder.toString());
			}
		}

		addTagsToPostOrPut(theResource, retVal);
//		addContentTypeHeaderBasedOnDetectedType(retVal, theResourceBody);
		
		return retVal;
	}

	public static void parseClientRequestResourceHeaders(Map<String, List<String>> theHeaders, IResource resource) {
		List<String> lmHeaders = theHeaders.get(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
		if (lmHeaders != null && lmHeaders.size() > 0 && StringUtils.isNotBlank(lmHeaders.get(0))) {
			String headerValue = lmHeaders.get(0);
			Date headerDateValue;
			try {
				headerDateValue = DateUtils.parseDate(headerValue);
				InstantDt lmValue = new InstantDt(headerDateValue);
				resource.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, lmValue);
			} catch (Exception e) {
				ourLog.warn("Unable to parse date string '{}'. Error is: {}", headerValue, e.toString());
			}
		}

		List<String> clHeaders = theHeaders.get(Constants.HEADER_CONTENT_LOCATION_LC);
		if (clHeaders != null && clHeaders.size() > 0 && StringUtils.isNotBlank(clHeaders.get(0))) {
			String headerValue = clHeaders.get(0);
			resource.getId().setValue(headerValue);
		}

		List<String> categoryHeaders = theHeaders.get(Constants.HEADER_CATEGORY_LC);
		if (categoryHeaders != null && categoryHeaders.size() > 0 && StringUtils.isNotBlank(categoryHeaders.get(0))) {
			TagList tagList = new TagList();
			for (String header : categoryHeaders) {
				parseTagValue(tagList, header);
			}
			ResourceMetadataKeyEnum.TAG_LIST.put(resource, tagList);
		}
	}

	public static void parseTagValue(TagList tagList, String nextTagComplete) {
		StringBuilder next = new StringBuilder(nextTagComplete);
		parseTagValue(tagList, nextTagComplete, next);
	}

	private static void parseTagValue(TagList theTagList, String theCompleteHeaderValue, StringBuilder theBuffer) {
		int firstSemicolon = theBuffer.indexOf(";");
		int deleteTo;
		if (firstSemicolon == -1) {
			firstSemicolon = theBuffer.indexOf(",");
			if (firstSemicolon == -1) {
				firstSemicolon = theBuffer.length();
				deleteTo = theBuffer.length();
			} else {
				deleteTo = firstSemicolon;
			}
		} else {
			deleteTo = firstSemicolon + 1;
		}

		String term = theBuffer.substring(0, firstSemicolon);
		String scheme = null;
		String label = null;
		if (isBlank(term)) {
			return;
		}

		theBuffer.delete(0, deleteTo);
		while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
			theBuffer.deleteCharAt(0);
		}

		while (theBuffer.length() > 0) {
			boolean foundSomething = false;
			if (theBuffer.length() > SCHEME.length() && theBuffer.substring(0, SCHEME.length()).equals(SCHEME)) {
				int closeIdx = theBuffer.indexOf("\"", SCHEME.length());
				scheme = theBuffer.substring(SCHEME.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			if (theBuffer.length() > LABEL.length() && theBuffer.substring(0, LABEL.length()).equals(LABEL)) {
				int closeIdx = theBuffer.indexOf("\"", LABEL.length());
				label = theBuffer.substring(LABEL.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			// TODO: support enc2231-string as described in
			// http://tools.ietf.org/html/draft-johnston-http-category-header-02
			// TODO: support multiple tags in one header as described in
			// http://hl7.org/implement/standards/fhir/http.html#tags

			while (theBuffer.length() > 0 && (theBuffer.charAt(0) == ' ' || theBuffer.charAt(0) == ';')) {
				theBuffer.deleteCharAt(0);
			}

			if (!foundSomething) {
				break;
			}
		}

		if (theBuffer.length() > 0 && theBuffer.charAt(0) == ',') {
			theBuffer.deleteCharAt(0);
			while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
				theBuffer.deleteCharAt(0);
			}
			theTagList.add(new Tag(scheme, term, label));
			parseTagValue(theTagList, theCompleteHeaderValue, theBuffer);
		} else {
			theTagList.add(new Tag(scheme, term, label));
		}

		if (theBuffer.length() > 0) {
			ourLog.warn("Ignoring extra text at the end of " + Constants.HEADER_CATEGORY + " tag '" + theBuffer.toString() + "' - Complete tag value was: " + theCompleteHeaderValue);
		}

	}

	static void addTagsToPostOrPut(IResource resource, BaseHttpClientInvocation retVal) {
		TagList list = (TagList) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (list != null) {
			for (Tag tag : list) {
				if (StringUtils.isNotBlank(tag.getTerm())) {
					retVal.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
				}
			}
		}
	}

	public static HttpPostClientInvocation createCreateInvocation(IResource theResource, FhirContext theContext) {
		return createCreateInvocation(theResource, null, null, theContext);
	}

	public static HttpPostClientInvocation createCreateInvocation(IResource theResource, String theResourceBody, String theId, FhirContext theContext) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);
		String resourceName = def.getName();

		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);
		if (StringUtils.isNotBlank(theId)) {
			urlExtension.append('/');
			urlExtension.append(theId);
		}

		HttpPostClientInvocation retVal;
		if (StringUtils.isBlank(theResourceBody)) {
			retVal = new HttpPostClientInvocation(theContext, theResource, urlExtension.toString());
		} else {
			retVal = new HttpPostClientInvocation(theContext, theResourceBody, false, urlExtension.toString());
		}
		addTagsToPostOrPut(theResource, retVal);

//		addContentTypeHeaderBasedOnDetectedType(retVal, theResourceBody);
		
		return retVal;
	}

	public static EncodingEnum detectEncoding(String theBody) {
		for (int i = 0; i < theBody.length(); i++) {
			switch (theBody.charAt(i)) {
			case '<':
				return EncodingEnum.XML;
			case '{':
				return EncodingEnum.JSON;
			}
		}
		return EncodingEnum.XML;
	}

	public static HttpGetClientInvocation createConformanceInvocation() {
		return new HttpGetClientInvocation("metadata");
	}

	public static MethodOutcome process2xxResponse(FhirContext theContext, String theResourceName, int theResponseStatusCode, String theResponseMimeType, Reader theResponseReader, Map<String, List<String>> theHeaders) {
		List<String> locationHeaders = new ArrayList<String>();
		List<String> lh = theHeaders.get(Constants.HEADER_LOCATION_LC);
		if (lh != null) {
			locationHeaders.addAll(lh);
		}
		List<String> clh = theHeaders.get(Constants.HEADER_CONTENT_LOCATION_LC);
		if (clh != null) {
			locationHeaders.addAll(clh);
		}

		MethodOutcome retVal = new MethodOutcome();
		if (locationHeaders != null && locationHeaders.size() > 0) {
			String locationHeader = locationHeaders.get(0);
			BaseOutcomeReturningMethodBinding.parseContentLocation(retVal, theResourceName, locationHeader);
		}
		if (theResponseStatusCode != Constants.STATUS_HTTP_204_NO_CONTENT) {
			EncodingEnum ct = EncodingEnum.forContentType(theResponseMimeType);
			if (ct != null) {
				PushbackReader reader = new PushbackReader(theResponseReader);

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
					IResource outcome = parser.parseResource(reader);
					if (outcome instanceof BaseOperationOutcome) {
						retVal.setOperationOutcome((BaseOperationOutcome) outcome);
					}
				}

			} else {
				BaseOutcomeReturningMethodBinding.ourLog.debug("Ignoring response content of type: {}", theResponseMimeType);
			}
		}
		return retVal;
	}

	public static Integer findParamAnnotationIndex(Method theMethod, Class<?> toFind) {
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {
			for (int annotationIndex = 0; annotationIndex < annotations.length; annotationIndex++) {
				Annotation nextAnnotation = annotations[annotationIndex];
				Class<? extends Annotation> class1 = nextAnnotation.getClass();
				if (toFind.isAssignableFrom(class1)) {
					return paramIndex;
				}
			}
			paramIndex++;
		}
		return null;
	}

	public static void extractDescription(SearchParameter theParameter, Annotation[] theAnnotations) {
		for (Annotation annotation : theAnnotations) {
			if (annotation instanceof Description) {
				Description desc = (Description) annotation;
				if (isNotBlank(desc.formalDefinition())) {
					theParameter.setDescription(desc.formalDefinition());
				} else {
					theParameter.setDescription(desc.shortDefinition());
				}
			}
		}
	}

	public static IQueryParameterOr<?> singleton(final IQueryParameterType theParam) {
		return new IQueryParameterOr<IQueryParameterType>() {

			@Override
			public void setValuesAsQueryTokens(QualifiedParamList theParameters) {
				if (theParameters.isEmpty()) {
					return;
				}
				if (theParameters.size() > 1) {
					throw new IllegalArgumentException("Type " + theParam.getClass().getCanonicalName() + " does not support multiple values");
				}
				theParam.setValueAsQueryToken(theParameters.getQualifier(), theParameters.get(0));
			}

			@Override
			public List<IQueryParameterType> getValuesAsQueryTokens() {
				return Collections.singletonList(theParam);
			}
		};
	}

	@SuppressWarnings("deprecation")
	public static Integer findVersionIdParameterIndex(Method theMethod) {
		return MethodUtil.findParamAnnotationIndex(theMethod, VersionIdParam.class);
	}

	public static Integer findIdParameterIndex(Method theMethod) {
		return MethodUtil.findParamAnnotationIndex(theMethod, IdParam.class);
	}

	public static Integer findTagListParameterIndex(Method theMethod) {
		return MethodUtil.findParamAnnotationIndex(theMethod, TagListParam.class);
	}

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(Method theMethod, Object theProvider) {
		List<IParameter> parameters = new ArrayList<IParameter>();

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {

			IParameter param = null;
			Class<?> parameterType = parameterTypes[paramIndex];
			Class<? extends java.util.Collection<?>> outerCollectionType = null;
			Class<? extends java.util.Collection<?>> innerCollectionType = null;
			if (SearchParameterMap.class.equals(parameterType)) {
				if (theProvider instanceof IDynamicSearchResourceProvider) {
					Search searchAnnotation = theMethod.getAnnotation(Search.class);
					if (searchAnnotation != null && searchAnnotation.dynamic()) {
						param = new DynamicSearchParameter((IDynamicSearchResourceProvider) theProvider);
					}
				}
			} else if (TagList.class.isAssignableFrom(parameterType)) {
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
					throw new ConfigurationException("Argument #" + paramIndex + " of Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of an invalid generic type (can not be a collection of a collection of a collection)");
				}
			}
			if (parameterType.equals(HttpServletRequest.class) || parameterType.equals(ServletRequest.class)) {
				param = new ServletRequestParameter();
			} else if (parameterType.equals(HttpServletResponse.class) || parameterType.equals(ServletResponse.class)) {
				param = new ServletResponseParameter();
			} else {
				for (int i = 0; i < annotations.length && param == null; i++) {
					Annotation nextAnnotation = annotations[i];

					if (nextAnnotation instanceof RequiredParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((RequiredParam) nextAnnotation).name());
						parameter.setRequired(true);
						parameter.setDeclaredTypes(((RequiredParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((RequiredParam) nextAnnotation).compositeTypes());
						parameter.setChainlists(((RequiredParam) nextAnnotation).chainWhitelist(), ((RequiredParam) nextAnnotation).chainBlacklist());
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						MethodUtil.extractDescription(parameter, annotations);
						param = parameter;
					} else if (nextAnnotation instanceof OptionalParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((OptionalParam) nextAnnotation).name());
						parameter.setRequired(false);
						parameter.setDeclaredTypes(((OptionalParam) nextAnnotation).targetTypes());
						parameter.setCompositeTypes(((OptionalParam) nextAnnotation).compositeTypes());
						parameter.setChainlists(((OptionalParam) nextAnnotation).chainWhitelist(), ((OptionalParam) nextAnnotation).chainBlacklist());
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						MethodUtil.extractDescription(parameter, annotations);
						param = parameter;
					} else if (nextAnnotation instanceof IncludeParam) {
						Class<? extends Collection<Include>> instantiableCollectionType;
						Class<?> specType;

						if (parameterType == String.class) {
							instantiableCollectionType = null;
							specType = String.class;
						} else if ((parameterType != Include.class && parameterType != PathSpecification.class) || innerCollectionType == null || outerCollectionType != null) {
							throw new ConfigurationException("Method '" + theMethod.getName() + "' is annotated with @" + IncludeParam.class.getSimpleName() + " but has a type other than Collection<" + Include.class.getSimpleName() + ">");
						} else {
							instantiableCollectionType = (Class<? extends Collection<Include>>) CollectionBinder.getInstantiableCollectionType(innerCollectionType, "Method '" + theMethod.getName() + "'");
							specType = parameterType;
						}

						param = new IncludeParameter((IncludeParam) nextAnnotation, instantiableCollectionType, specType);
					} else if (nextAnnotation instanceof ResourceParam) {
						if (!IResource.class.isAssignableFrom(parameterType)) {
							throw new ConfigurationException("Method '" + theMethod.getName() + "' is annotated with @" + ResourceParam.class.getSimpleName() + " but has a type that is not an implemtation of " + IResource.class.getCanonicalName());
						}
						param = new ResourceParameter((Class<? extends IResource>) parameterType);
					} else if (nextAnnotation instanceof IdParam || nextAnnotation instanceof VersionIdParam) {
						param = new NullParameter();
					} else if (nextAnnotation instanceof ServerBase) {
						param = new ServerBaseParamBinder();
					} else if (nextAnnotation instanceof Since) {
						param = new SinceParameter();
					} else if (nextAnnotation instanceof Count) {
						param = new CountParameter();
					} else if (nextAnnotation instanceof Sort) {
						param = new SortParameter();
					} else if (nextAnnotation instanceof TransactionParam) {
						param = new TransactionParamBinder();
					} else {
						continue;
					}

				}

			}

			if (param == null) {
				throw new ConfigurationException("Parameter #" + ((paramIndex + 1)) + "/" + (parameterTypes.length) + " of method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName()
						+ "' has no recognized FHIR interface parameter annotations. Don't know how to handle this parameter");
			}

			param.initializeTypes(theMethod, outerCollectionType, innerCollectionType, parameterType);
			parameters.add(param);

			paramIndex++;
		}
		return parameters;
	}

}
