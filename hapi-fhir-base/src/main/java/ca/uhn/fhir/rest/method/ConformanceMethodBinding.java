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

import java.lang.reflect.Method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {

	public ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod.getReturnType(), theMethod, theContext, theProvider);

//		if (Modifier.isAbstract(theMethod.getReturnType().getModifiers())) {
//			throw new ConfigurationException("Conformance resource provider method '" + theMethod.getName() + "' must not be abstract");
//		}
		if (getMethodReturnType() != MethodReturnTypeEnum.RESOURCE || !BaseConformance.class.isAssignableFrom(theMethod.getReturnType())) {
			throw new ConfigurationException("Conformance resource provider method '" + theMethod.getName() + "' should return a Conformance resource class, returns: " + theMethod.getReturnType());
		}

	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public HttpGetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpGetClientInvocation retVal = MethodUtil.createConformanceInvocation();

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null);
			}
		}

		return retVal;
	}

	@Override
	public IBundleProvider invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws BaseServerResponseException {
		IResource conf = (IResource) invokeServerMethod(theMethodParams);
		return new SimpleBundleProvider(conf);
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (theRequest.getRequestType() == RequestType.OPTIONS) {
			return true;
		}

		if (theRequest.getRequestType() == RequestType.GET && "metadata".equals(theRequest.getOperation())) {
			return true;
		}

		return false;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public OtherOperationTypeEnum getOtherOperationType() {
		return OtherOperationTypeEnum.METADATA;
	}

}
