package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class HistoryMethodBinding extends BaseResourceReturningMethodBinding {

	private final Integer myIdParamIndex;
	private String myResourceName;
	private final RestOperationTypeEnum myResourceOperationType;

	public HistoryMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(toReturnType(theMethod, theProvider), theMethod, theContext, theProvider);

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());

		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (Modifier.isInterface(type.getModifiers())) {
			if (theProvider instanceof IResourceProvider) {
				type = ((IResourceProvider) theProvider).getResourceType();
				if (myIdParamIndex != null) {
					myResourceOperationType = RestOperationTypeEnum.HISTORY_INSTANCE;
				} else {
					myResourceOperationType = RestOperationTypeEnum.HISTORY_TYPE;
				}
			} else {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_SYSTEM;
			}
		} else {
			if (myIdParamIndex != null) {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_INSTANCE;
			} else {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_TYPE;
			}
		}

		if (type != IBaseResource.class && type != IResource.class) {
			myResourceName = theContext.getResourceDefinition(type).getName();
		} else {
			myResourceName = null;
		}

	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myResourceOperationType;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.HISTORY;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	// ObjectUtils.equals is replaced by a JDK7 method..
	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (!Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			return false;
		}
		if (theRequest.getResourceName() == null) {
			return myResourceOperationType == RestOperationTypeEnum.HISTORY_SYSTEM;
		}
		if (!StringUtils.equals(theRequest.getResourceName(), myResourceName)) {
			return false;
		}

		boolean haveIdParam = theRequest.getId() != null && !theRequest.getId().isEmpty();
		boolean wantIdParam = myIdParamIndex != null;
		if (haveIdParam != wantIdParam) {
			return false;
		}

		if (theRequest.getId() == null) {
			return myResourceOperationType == RestOperationTypeEnum.HISTORY_TYPE;
		} else if (theRequest.getId().hasVersionIdPart()) {
			return false;
		}

		return true;
	}
	
	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IdDt id = null;
		String resourceName = myResourceName;
		if (myIdParamIndex != null) {
			id = (IdDt) theArgs[myIdParamIndex];
			if (id == null || isBlank(id.getValue())) {
				throw new NullPointerException("ID can not be null");
			}
		}

		String historyId = id != null ? id.getIdPart() : null;
		HttpGetClientInvocation retVal = createHistoryInvocation(getContext(), resourceName, historyId, null, null);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], retVal.getParameters(), null);
			}
		}

		return retVal;
	}

	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theServer, theRequest, theMethodParams);

		final IBundleProvider resources = toResourceList(response);
		
		/*
		 * We wrap the response so we can verify that it has the ID and version set,
		 * as is the contract for history
		 */
		return new IBundleProvider() {
			
			@Override
			public IPrimitiveType<Date> getPublished() {
				return resources.getPublished();
			}
			
			@Override
			public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
				List<IBaseResource> retVal = resources.getResources(theFromIndex, theToIndex);
				int index = theFromIndex;
				for (IBaseResource nextResource : retVal) {
					if (nextResource.getIdElement() == null || isBlank(nextResource.getIdElement().getIdPart())) {
						throw new InternalErrorException("Server provided resource at index " + index + " with no ID set (using IResource#setId(IdDt))");
					}
					if (isBlank(nextResource.getIdElement().getVersionIdPart()) && nextResource instanceof IResource) {
						//TODO: Use of a deprecated method should be resolved.
						IdDt versionId = (IdDt) ResourceMetadataKeyEnum.VERSION_ID.get((IResource) nextResource);
						if (versionId == null || versionId.isEmpty()) {
							throw new InternalErrorException("Server provided resource at index " + index + " with no Version ID set (using IResource#setId(IdDt))");
						}
					}
					index++;
				}
				return retVal;
			}
			
			@Override
			public Integer size() {
				return resources.size();
			}

			@Override
			public Integer preferredPageSize() {
				return resources.preferredPageSize();
			}

			@Override
			public String getUuid() {
				return resources.getUuid();
			}
		};
	}

	public static HttpGetClientInvocation createHistoryInvocation(FhirContext theContext, String theResourceName, String theId, IPrimitiveType<Date> theSince, Integer theLimit) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (isNotBlank(theId)) {
				b.append('/');
				b.append(theId);
			}
		}
		if (b.length() > 0) {
			b.append('/');
		}
		b.append(Constants.PARAM_HISTORY);

		boolean haveParam = false;
		if (theSince != null && !theSince.isEmpty()) {
			haveParam = true;
			b.append('?').append(Constants.PARAM_SINCE).append('=').append(theSince.getValueAsString());
		}
		if (theLimit != null) {
			b.append(haveParam ? '&' : '?');
			b.append(Constants.PARAM_COUNT).append('=').append(theLimit);
		}

		HttpGetClientInvocation retVal = new HttpGetClientInvocation(theContext, b.toString());
		return retVal;
	}

	private static Class<? extends IBaseResource> toReturnType(Method theMethod, Object theProvider) {
		if (theProvider instanceof IResourceProvider) {
			return ((IResourceProvider) theProvider).getResourceType();
		}
		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (type != IBaseResource.class && type != IResource.class) {
			return type;
		}
		return null;
	}

}
