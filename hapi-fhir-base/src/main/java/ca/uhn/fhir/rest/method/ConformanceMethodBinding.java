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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {

	public ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(Conformance.class, theMethod, theContext, theProvider);

		if (getMethodReturnType() != MethodReturnTypeEnum.RESOURCE || theMethod.getReturnType() != Conformance.class) {
			throw new ConfigurationException("Conformance resource provider method '" + theMethod.getName() + "' should return type " + Conformance.class);
		}

	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		return new GetClientInvocation("metadata");
	}

	@Override
	public List<IResource> invokeServer(Object theResourceProvider, Request theRequest, Object[] theMethodParams) throws InvalidRequestException,
			InternalErrorException {
		IResource conf;
		try {
			conf = (Conformance) invokeServerMethod(theResourceProvider, theMethodParams);
		} catch (Exception e) {
			throw new InternalErrorException("Failed to call access method", e);
		}

		return Collections.singletonList(conf);
	}

	@Override
	public boolean matches(Request theRequest) {
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

}
