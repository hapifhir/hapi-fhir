package ca.uhn.fhir.rest.method;

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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class HistoryMethodBinding extends BaseResourceReturningMethodBinding {

	private final Integer myIdParamIndex;
	private String myResourceName;
	private final RestfulOperationTypeEnum myResourceOperationType;
	private final RestfulOperationSystemEnum mySystemOperationType;

	public HistoryMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(toReturnType(theMethod, theProvider), theMethod, theConetxt, theProvider);

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);

		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IResource> type = historyAnnotation.type();
		if (type == IResource.class) {
			if (theProvider instanceof IResourceProvider) {
				type = ((IResourceProvider) theProvider).getResourceType();
				if (myIdParamIndex != null) {
					myResourceOperationType = RestfulOperationTypeEnum.HISTORY_INSTANCE;
				} else {
					myResourceOperationType = RestfulOperationTypeEnum.HISTORY_TYPE;
				}
				mySystemOperationType = null;
			} else {
				myResourceOperationType = null;
				mySystemOperationType = RestfulOperationSystemEnum.HISTORY_SYSTEM;
			}
		} else {
			if (myIdParamIndex != null) {
				myResourceOperationType = RestfulOperationTypeEnum.HISTORY_INSTANCE;
			} else {
				myResourceOperationType = RestfulOperationTypeEnum.HISTORY_TYPE;
			}
			mySystemOperationType = null;
		}

		if (type != IResource.class) {
			myResourceName = theConetxt.getResourceDefinition(type).getName();
		} else {
			myResourceName = null;
		}

	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return myResourceOperationType;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return mySystemOperationType;
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

		HttpGetClientInvocation retVal = createHistoryInvocation(resourceName, id, null, null);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], retVal.getParameters());
			}
		}

		return retVal;
	}

	public static HttpGetClientInvocation createHistoryInvocation(String theResourceName, IdDt theId, DateTimeDt theSince, Integer theLimit) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (theId != null && !theId.isEmpty()) {
				b.append('/');
				b.append(theId.getValue());
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

		HttpGetClientInvocation retVal = new HttpGetClientInvocation(b.toString());
		return retVal;
	}

	@Override
	public IBundleProvider invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theMethodParams);

		final IBundleProvider resources = toResourceList(response);
		
		/*
		 * We wrap the response so we can verify that it has the ID and version set,
		 * as is the contract for history
		 */
		return new IBundleProvider() {
			
			@Override
			public int size() {
				return resources.size();
			}
			
			@Override
			public List<IResource> getResources(int theFromIndex, int theToIndex) {
				List<IResource> retVal = resources.getResources(theFromIndex, theToIndex);
				int index = theFromIndex;
				for (IResource nextResource : retVal) {
					if (nextResource.getId() == null || isBlank(nextResource.getId().getIdPart())) {
						throw new InternalErrorException("Server provided resource at index " + index + " with no ID set (using IResource#setId(IdDt))");
					}
					if (isBlank(nextResource.getId().getVersionIdPart())) {
						IdDt versionId = (IdDt) ResourceMetadataKeyEnum.VERSION_ID.get(nextResource);
						if (versionId == null || versionId.isEmpty()) {
							throw new InternalErrorException("Server provided resource at index " + index + " with no Version ID set (using IResource#setId(IdDt))");
						}
					}
					index++;
				}
				return retVal;
			}
			
			@Override
			public InstantDt getPublished() {
				return resources.getPublished();
			}
		};
	}

	// ObjectUtils.equals is replaced by a JDK7 method..
	@SuppressWarnings("deprecation")
	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (!Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			return false;
		}
		if (theRequest.getResourceName() == null) {
			return mySystemOperationType == RestfulOperationSystemEnum.HISTORY_SYSTEM;
		}
		if (!ObjectUtils.equals(theRequest.getResourceName(), myResourceName)) {
			return false;
		}

		boolean haveIdParam = theRequest.getId() != null && !theRequest.getId().isEmpty();
		boolean wantIdParam = myIdParamIndex != null;
		if (haveIdParam != wantIdParam) {
			return false;
		}

		if (theRequest.getId() == null) {
			return myResourceOperationType == RestfulOperationTypeEnum.HISTORY_TYPE;
		} else if (theRequest.getId().hasVersionIdPart()) {
			return false;
		}

		return true;
	}

	private static Class<? extends IResource> toReturnType(Method theMethod, Object theProvider) {
		if (theProvider instanceof IResourceProvider) {
			return ((IResourceProvider) theProvider).getResourceType();
		}
		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IResource> type = historyAnnotation.type();
		if (type != IResource.class) {
			return type;
		}
		return null;
	}

}
