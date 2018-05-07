package ca.uhn.fhir.rest.server.method;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.*;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.util.DateUtils;

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
			throw new ConfigurationException("@" + Read.class.getSimpleName() + " method " + theMethod.getName() + " on type \"" + theMethod.getDeclaringClass().getName() + "\" does not have a parameter annotated with @" + IdParam.class.getSimpleName());
		}
		myIdParameterType = (Class<? extends IIdType>) parameterTypes[myIdIndex];

		if (!IIdType.class.isAssignableFrom(myIdParameterType)) {
			throw new ConfigurationException("ID parameter must be of type IdDt or IdType - Found: " + myIdParameterType);
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

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return isVread() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			return false;
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (!ALLOWED_PARAMS.contains(next)) {
				return false;
			}
		}
		if (theRequest.getId() == null) {
			return false;
		}
		if (mySupportsVersion == false) {
			if (theRequest.getId().hasVersionIdPart()) {
				return false;
			}
		}
		if (isNotBlank(theRequest.getCompartmentName())) {
			return false;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET) {
			ourLog.trace("Method {} doesn't match because request type is not GET: {}", theRequest.getId(), theRequest.getRequestType());
			return false;
		}
		if (Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			if (mySupportsVersion == false) {
				return false;
			}
			if (theRequest.getId().hasVersionIdPart() == false) {
				return false;
			}
		} else if (!StringUtils.isBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}


	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		IIdType requestId = theRequest.getId();

		theMethodParams[myIdIndex] = ParameterUtil.convertIdToType(requestId, myIdParameterType);

		Object response = invokeServerMethod(theServer, theRequest, theMethodParams);
		IBundleProvider retVal = toResourceList(response);


		if (retVal.size() == 1) {
			List<IBaseResource> responseResources = retVal.getResources(0, 1);
			IBaseResource responseResource = responseResources.get(0);

			// If-None-Match
			if (theRequest.getServer().getETagSupport() == ETagSupportEnum.ENABLED) {
				String ifNoneMatch = theRequest.getHeader(Constants.HEADER_IF_NONE_MATCH_LC);
				if (StringUtils.isNotBlank(ifNoneMatch)) {
					ifNoneMatch = ParameterUtil.parseETagValue(ifNoneMatch);
					if (responseResource.getIdElement() != null && responseResource.getIdElement().hasVersionIdPart()) {
						if (responseResource.getIdElement().getVersionIdPart().equals(ifNoneMatch)) {
							ourLog.debug("Returning HTTP 304 because request specified {}={}", Constants.HEADER_IF_NONE_MATCH, ifNoneMatch);
							throw new NotModifiedException("Not Modified");
						}
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
					throw new NotModifiedException("Not Modified");
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
