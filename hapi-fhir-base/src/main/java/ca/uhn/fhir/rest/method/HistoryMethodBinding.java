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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.Constants;
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

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod);

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
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IdDt id = null;
		String resourceName = myResourceName;
		if (myIdParamIndex != null) {
			id = (IdDt) theArgs[myIdParamIndex];
			if (id == null || isBlank(id.getValue())) {
				throw new NullPointerException("ID can not be null");
			}
		}
		
		GetClientInvocation retVal = createHistoryInvocation(resourceName, id);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(theArgs[idx], retVal.getParameters(),retVal);
			}
		}

		return retVal;
	}

	public static GetClientInvocation createHistoryInvocation(String theResourceName, IdDt theId) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (theId != null) {
				b.append('/');
				b.append(theId.getValue());
			}
		}
		if (b.length() > 0) {
			b.append('/');
		}
		b.append(Constants.PARAM_HISTORY);
		GetClientInvocation retVal = new GetClientInvocation(b.toString());
		return retVal;
	}

	@Override
	public List<IResource> invokeServer(Request theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theMethodParams);

		List<IResource> resources = toResourceList(response);
		int index=0;
		for (IResource nextResource : resources) {
			if (nextResource.getId() == null || nextResource.getId().isEmpty()) {
				throw new InternalErrorException("Server provided resource at index " + index + " with no ID set (using IResource#setId(IdDt))");
			}
			IdDt versionId = getIdFromMetadataOrNullIfNone(nextResource.getResourceMetadata(),ResourceMetadataKeyEnum.VERSION_ID);
			if (versionId == null||versionId.isEmpty()) {
				throw new InternalErrorException("Server provided resource at index " + index + " with no Version ID set (using IResource#Resource.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, Object))");
			}
			index++;
		}
		
		return resources;
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

		if (theRequest.getVersionId() != null && !theRequest.getVersionId().isEmpty()) {
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
