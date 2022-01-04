package ca.uhn.fhir.rest.server.method;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ReadMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadMethodBinding.class);

	private Integer myIdIndex;
	private boolean mySupportsVersion;
	private Class<? extends IIdType> myIdParameterType;

	@SuppressWarnings("unchecked")
	public ReadMethodBinding(Class<? extends IBaseResource> theAnnotatedResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theAnnotatedResourceType, theMethod, theContext, theProvider);

		Validate.notNull(theMethod, "Method must not be null");

		Integer idIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());

		Class<?>[] parameterTypes = theMethod.getParameterTypes();

		mySupportsVersion = theMethod.getAnnotation(Read.class).version();
		myIdIndex = idIndex;

		if (myIdIndex == null) {
			throw new ConfigurationException(Msg.code(382) + "@" + Read.class.getSimpleName() + " method " + theMethod.getName() + " on type \"" + theMethod.getDeclaringClass().getName() + "\" does not have a parameter annotated with @" + IdParam.class.getSimpleName());
		}
		myIdParameterType = (Class<? extends IIdType>) parameterTypes[myIdIndex];

		if (!IIdType.class.isAssignableFrom(myIdParameterType)) {
			throw new ConfigurationException(Msg.code(383) + "ID parameter must be of type IdDt or IdType - Found: " + myIdParameterType);
		}

	}

	@Override
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		if (mySupportsVersion && theRequestDetails.getId().hasVersionIdPart()) {
			return RestOperationTypeEnum.VREAD;
		}
		return RestOperationTypeEnum.READ;
	}

	@Override
	public List<Class<?>> getAllowableParamAnnotations() {
		ArrayList<Class<?>> retVal = new ArrayList<Class<?>>();
		retVal.add(IdParam.class);
		retVal.add(Elements.class);
		return retVal;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return isVread() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			return MethodMatchEnum.NONE;
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (!next.startsWith("_")) {
				return MethodMatchEnum.NONE;
			}
		}
		if (theRequest.getId() == null) {
			return MethodMatchEnum.NONE;
		}
		if (mySupportsVersion == false) {
			if (theRequest.getId().hasVersionIdPart()) {
				return MethodMatchEnum.NONE;
			}
		}
		if (isNotBlank(theRequest.getCompartmentName())) {
			return MethodMatchEnum.NONE;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET && theRequest.getRequestType() != RequestTypeEnum.HEAD ) {
			ourLog.trace("Method {} doesn't match because request type is not GET or HEAD: {}", theRequest.getId(), theRequest.getRequestType());
			return MethodMatchEnum.NONE;
		}
		if (Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			if (mySupportsVersion == false) {
				return MethodMatchEnum.NONE;
			} else if (theRequest.getId().hasVersionIdPart() == false) {
				return MethodMatchEnum.NONE;
			}
		} else if (!StringUtils.isBlank(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}
		return MethodMatchEnum.EXACT;
	}


	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		IIdType requestId = theRequest.getId();
		FhirContext ctx = theRequest.getServer().getFhirContext();

		String[] invalidQueryStringParams = new String[]{Constants.PARAM_CONTAINED, Constants.PARAM_COUNT, Constants.PARAM_INCLUDE, Constants.PARAM_REVINCLUDE, Constants.PARAM_SORT, Constants.PARAM_SEARCH_TOTAL_MODE};
		List<String> invalidQueryStringParamsInRequest = new ArrayList<>();
		Set<String> queryStringParamsInRequest = theRequest.getParameters().keySet();

		for (String queryStringParamName : queryStringParamsInRequest) {
			String lowercaseQueryStringParamName = queryStringParamName.toLowerCase();
			if (StringUtils.startsWithAny(lowercaseQueryStringParamName, invalidQueryStringParams)) {
				invalidQueryStringParamsInRequest.add(queryStringParamName);
			}
		}

		if (!invalidQueryStringParamsInRequest.isEmpty()) {
			throw new InvalidRequestException(Msg.code(384) + ctx.getLocalizer().getMessage(ReadMethodBinding.class, "invalidParamsInRequest", invalidQueryStringParamsInRequest));
		}

		theMethodParams[myIdIndex] = ParameterUtil.convertIdToType(requestId, myIdParameterType);

		Object response = invokeServerMethod(theRequest, theMethodParams);
		IBundleProvider retVal = toResourceList(response);


		if (Integer.valueOf(1).equals(retVal.size())) {
			List<IBaseResource> responseResources = retVal.getResources(0, 1);
			IBaseResource responseResource = responseResources.get(0);

			// If-None-Match
			if (theRequest.getServer().getETagSupport() == ETagSupportEnum.ENABLED) {
				String ifNoneMatch = theRequest.getHeader(Constants.HEADER_IF_NONE_MATCH_LC);
				if (StringUtils.isNotBlank(ifNoneMatch)) {
					ifNoneMatch = ParameterUtil.parseETagValue(ifNoneMatch);
					String versionIdPart = responseResource.getIdElement().getVersionIdPart();
					if (StringUtils.isBlank(versionIdPart)) {
						versionIdPart = responseResource.getMeta().getVersionId();
					}
					if (ifNoneMatch.equals(versionIdPart)) {
						ourLog.debug("Returning HTTP 304 because request specified {}={}", Constants.HEADER_IF_NONE_MATCH, ifNoneMatch);
						throw new NotModifiedException(Msg.code(385) + "Not Modified");
					}
				}
			}
				
			// If-Modified-Since
			String ifModifiedSince = theRequest.getHeader(Constants.HEADER_IF_MODIFIED_SINCE_LC);
			if (isNotBlank(ifModifiedSince)) {
				Date ifModifiedSinceDate = DateUtils.parseDate(ifModifiedSince);
				Date lastModified = null;
				if (responseResource instanceof IResource) {
					InstantDt lastModifiedDt = ResourceMetadataKeyEnum.UPDATED.get((IResource) responseResource);
					if (lastModifiedDt != null) {
						lastModified = lastModifiedDt.getValue();
					}
				} else {
					lastModified = responseResource.getMeta().getLastUpdated();
				}
				
				if (lastModified != null && lastModified.getTime() <= ifModifiedSinceDate.getTime()) {
					ourLog.debug("Returning HTTP 304 because If-Modified-Since does not match");
					throw new NotModifiedException(Msg.code(386) + "Not Modified");
				}
			}
				
		} // if we have at least 1 result

		
		return retVal;
	}

	public boolean isVread() {
		return mySupportsVersion;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

}
