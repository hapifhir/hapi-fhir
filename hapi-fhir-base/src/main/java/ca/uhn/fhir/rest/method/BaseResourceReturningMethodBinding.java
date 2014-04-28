package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR Library
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingUtil;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding {

	protected static final Set<String> ALLOWED_PARAMS;
	static {
		HashSet<String> set = new HashSet<String>();
		set.add(Constants.PARAM_FORMAT);
		set.add(Constants.PARAM_NARRATIVE);
		set.add(Constants.PARAM_PRETTY);
		ALLOWED_PARAMS = Collections.unmodifiableSet(set);
	}

	private MethodReturnTypeEnum myMethodReturnType;
	private String myResourceName;

	
	public BaseResourceReturningMethodBinding(Class<? extends IResource> theReturnResourceType, Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(theMethod, theConetxt, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();
		if (Collection.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
		} else if (IResource.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.RESOURCE;
		} else if (Bundle.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE;
		} else {
			throw new ConfigurationException("Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			ResourceDef resourceDefAnnotation = theReturnResourceType.getAnnotation(ResourceDef.class);
			if (resourceDefAnnotation == null) {
				throw new ConfigurationException(theReturnResourceType.getCanonicalName() + " has no @" + ResourceDef.class.getSimpleName() + " annotation");
			}
			myResourceName = resourceDefAnnotation.name();
		}
	}

	public MethodReturnTypeEnum getMethodReturnType() {
		return myMethodReturnType;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	public abstract ReturnTypeEnum getReturnType();

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException {
		IParser parser = createAppropriateParser(theResponseMimeType, theResponseReader, theResponseStatusCode);

		switch (getReturnType()) {
		case BUNDLE: {
			Bundle bundle = parser.parseBundle(theResponseReader);
			switch (getMethodReturnType()) {
			case BUNDLE:
				return bundle;
			case LIST_OF_RESOURCES:
				return bundle.toListOfResources();
			case RESOURCE:
				List<IResource> list = bundle.toListOfResources();
				if (list.size() == 0) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new InvalidResponseException(theResponseStatusCode, "FHIR server call returned a bundle with multiple resources, but this method is only able to returns one.");
				}
			}
			break;
		}
		case RESOURCE: {
			IResource resource = parser.parseResource(theResponseReader);

			List<String> lmHeaders = theHeaders.get(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
			if (lmHeaders != null && lmHeaders.size() > 0 && StringUtils.isNotBlank(lmHeaders.get(0))) {
				InstantDt lmValue = new InstantDt(lmHeaders.get(0));
				resource.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, lmValue);
			}

			switch (getMethodReturnType()) {
			case BUNDLE:
				return Bundle.withSingleResource(resource);
			case LIST_OF_RESOURCES:
				return Collections.singletonList(resource);
			case RESOURCE:
				return resource;
			}
			break;
		}
		}

		throw new IllegalStateException("Should not get here!");
	}

	public abstract List<IResource> invokeServer(Object theResourceProvider, Request theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException;

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {

		// Pretty print
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] pretty = requestParams.remove(Constants.PARAM_PRETTY);
		boolean prettyPrint = false;
		if (pretty != null && pretty.length > 0) {
			if ("true".equals(pretty[0])) {
				prettyPrint = true;
			}
		}

		// Narrative mode
		String[] narrative = requestParams.remove(Constants.PARAM_NARRATIVE);
		NarrativeModeEnum narrativeMode = null;
		if (narrative != null && narrative.length > 0) {
			narrativeMode = NarrativeModeEnum.valueOfCaseInsensitive(narrative[0]);
		}
		if (narrativeMode == null) {
			narrativeMode = NarrativeModeEnum.NORMAL;
		}

		// Determine response encoding
		EncodingUtil responseEncoding = determineResponseEncoding(theRequest.getServletRequest(), requestParams);

		// Is this request coming from a browser
		String uaHeader = theRequest.getServletRequest().getHeader("user-agent");
		boolean requestIsBrowser = false;
		if (uaHeader != null && uaHeader.contains("Mozilla")) {
			requestIsBrowser = true;
		}

		// Method params
		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param != null) {
				params[i] = param.translateQueryParametersIntoServerArgument(theRequest, null);
			}
		}

		List<IResource> result = invokeServer(getProvider(), theRequest, params);
		switch (getReturnType()) {
		case BUNDLE:
			streamResponseAsBundle(theServer, theResponse, result, responseEncoding, theRequest.getFhirServerBase(), theRequest.getCompleteUrl(), prettyPrint, requestIsBrowser, narrativeMode);
			break;
		case RESOURCE:
			if (result.size() == 0) {
				throw new ResourceNotFoundException(theRequest.getId());
			} else if (result.size() > 1) {
				throw new InternalErrorException("Method returned multiple resources");
			}
			streamResponseAsResource(theServer, theResponse, result.get(0), responseEncoding, prettyPrint, requestIsBrowser, narrativeMode);
			break;
		}
	}

	private IdDt getIdFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum, Object> theResourceMetadata, ResourceMetadataKeyEnum theKey) {
		Object retValObj = theResourceMetadata.get(theKey);
		if (retValObj == null) {
			return null;
		} else if (retValObj instanceof String) {
			if (isNotBlank((String) retValObj)) {
				return new IdDt((String) retValObj);
			} else {
				return null;
			}
		} else if (retValObj instanceof IdDt) {
			if (((IdDt) retValObj).isEmpty()) {
				return null;
			} else {
				return (IdDt) retValObj;
			}
		}
		throw new InternalErrorException("Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected " + IdDt.class.getCanonicalName());
	}

	private InstantDt getInstantFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum, Object> theResourceMetadata, ResourceMetadataKeyEnum theKey) {
		Object retValObj = theResourceMetadata.get(theKey);
		if (retValObj == null) {
			return null;
		} else if (retValObj instanceof Date) {
			return new InstantDt((Date) retValObj);
		} else if (retValObj instanceof InstantDt) {
			if (((InstantDt) retValObj).isEmpty()) {
				return null;
			} else {
				return (InstantDt) retValObj;
			}
		}
		throw new InternalErrorException("Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected " + InstantDt.class.getCanonicalName());
	}

	private IParser getNewParser(EncodingUtil theResponseEncoding, boolean thePrettyPrint, NarrativeModeEnum theNarrativeMode) {
		IParser parser;
		switch (theResponseEncoding) {
		case JSON:
			parser = getContext().newJsonParser();
			break;
		case XML:
		default:
			parser = getContext().newXmlParser();
			break;
		}
		return parser.setPrettyPrint(thePrettyPrint).setSuppressNarratives(theNarrativeMode == NarrativeModeEnum.SUPPRESS);
	}

	private void streamResponseAsBundle(RestfulServer theServer, HttpServletResponse theHttpResponse, List<IResource> theResult, EncodingUtil theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint, boolean theRequestIsBrowser,
			NarrativeModeEnum theNarrativeMode) throws IOException {
		assert !theServerBase.endsWith("/");

		theHttpResponse.setStatus(200);

		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(theResponseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(theResponseEncoding.getBundleContentType());
		}

		theHttpResponse.setCharacterEncoding("UTF-8");

		theServer.addHeadersToResponse(theHttpResponse);

		Bundle bundle = new Bundle();
		bundle.getAuthorName().setValue(getClass().getCanonicalName());
		bundle.getBundleId().setValue(UUID.randomUUID().toString());
		bundle.getPublished().setToCurrentTimeInLocalTimeZone();
		bundle.getLinkBase().setValue(theServerBase);
		bundle.getLinkSelf().setValue(theCompleteUrl);

		for (IResource next : theResult) {
			BundleEntry entry = new BundleEntry();
			bundle.getEntries().add(entry);

			entry.setResource(next);

			RuntimeResourceDefinition def = getContext().getResourceDefinition(next);

			if (next.getId() != null && StringUtils.isNotBlank(next.getId().getValue())) {
				entry.getId().setValue(next.getId().getValue());
				entry.getTitle().setValue(def.getName() + " " + next.getId().getValue());

				StringBuilder b = new StringBuilder();
				b.append(theServerBase);
				b.append('/');
				b.append(def.getName());
				b.append('/');
				String resId = next.getId().getValue();
				b.append(resId);

				/*
				 * If this is a history operation, we add the version of the
				 * resource to the self link to indicate the version
				 */
				if (getResourceOperationType() == RestfulOperationTypeEnum.HISTORY_INSTANCE || getResourceOperationType() == RestfulOperationTypeEnum.HISTORY_TYPE || getSystemOperationType() == RestfulOperationSystemEnum.HISTORY_SYSTEM) {
					IdDt versionId = getIdFromMetadataOrNullIfNone(next.getResourceMetadata(), ResourceMetadataKeyEnum.VERSION_ID);
					if (versionId != null) {
						b.append('/');
						b.append(Constants.PARAM_HISTORY);
						b.append('/');
						b.append(versionId.getValue());
					} else {
						throw new InternalErrorException("Server did not provide a VERSION_ID in the resource metadata for resource with ID " + resId);
					}
				}

				InstantDt published = getInstantFromMetadataOrNullIfNone(next.getResourceMetadata(), ResourceMetadataKeyEnum.PUBLISHED);
				if (published == null) {
					entry.getPublished().setToCurrentTimeInLocalTimeZone();
				} else {
					entry.setPublished(published);
				}

				InstantDt updated = getInstantFromMetadataOrNullIfNone(next.getResourceMetadata(), ResourceMetadataKeyEnum.UPDATED);
				if (updated != null) {
					entry.setUpdated(updated);
				}

				boolean haveQ = false;
				if (thePrettyPrint) {
					b.append('?').append(Constants.PARAM_PRETTY).append("=true");
					haveQ = true;
				}
				if (theResponseEncoding == EncodingUtil.JSON) {
					if (!haveQ) {
						b.append('?');
						haveQ = true;
					} else {
						b.append('&');
					}
					b.append(Constants.PARAM_FORMAT).append("=json");
				}
				if (theNarrativeMode != NarrativeModeEnum.NORMAL) {
					b.append(Constants.PARAM_NARRATIVE).append("=").append(theNarrativeMode.name().toLowerCase());
				}
				entry.getLinkSelf().setValue(b.toString());
			}
		}

		bundle.getTotalResults().setValue(theResult.size());

		PrintWriter writer = theHttpResponse.getWriter();
		try {
			if (theNarrativeMode == NarrativeModeEnum.ONLY) {
				for (IResource next : theResult) {
					writer.append(next.getText().getDiv().getValueAsString());
					writer.append("<hr/>");
				}
			} else {
				getNewParser(theResponseEncoding, thePrettyPrint, theNarrativeMode).encodeBundleToWriter(bundle, writer);
			}
		} finally {
			writer.close();
		}
	}

	private void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingUtil theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, NarrativeModeEnum theNarrativeMode) throws IOException {

		theHttpResponse.setStatus(200);
		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(theResponseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(theResponseEncoding.getResourceContentType());
		}
		theHttpResponse.setCharacterEncoding("UTF-8");

		InstantDt lastUpdated = getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), ResourceMetadataKeyEnum.UPDATED);
		if (lastUpdated != null) {
			theHttpResponse.addHeader(Constants.HEADER_LAST_MODIFIED, lastUpdated.getValueAsString());
		}

		PrintWriter writer = theHttpResponse.getWriter();
		try {
			if (theNarrativeMode == NarrativeModeEnum.ONLY) {
				writer.append(theResource.getText().getDiv().getValueAsString());
			} else {
				getNewParser(theResponseEncoding, thePrettyPrint, theNarrativeMode).encodeResourceToWriter(theResource, writer);
			}
		} finally {
			writer.close();
		}

	}

	public enum MethodReturnTypeEnum {
		BUNDLE, LIST_OF_RESOURCES, RESOURCE
	}

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}

}
