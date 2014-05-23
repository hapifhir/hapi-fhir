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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.param.TransactionParameter;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TransactionMethodBinding extends BaseResourceReturningMethodBinding {

	private int myResourceParameterIndex;

	public TransactionMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(null, theMethod, theConetxt, theProvider);
		
		myResourceParameterIndex = -1;
				int index=0;
		for (IParameter next : getParameters()) {
			if (next instanceof TransactionParameter) {
				myResourceParameterIndex = index;
			}
			index++;
		}

		if (myResourceParameterIndex==-1) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with the @" + TransactionParam.class + " annotation");
		}
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return RestfulOperationSystemEnum.TRANSACTION;
	}


	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (theRequest.getRequestType() != RequestType.POST) {
			return false;
		}
		if (isNotBlank(theRequest.getOperation())) {
			return false;
		}
		if (isNotBlank(theRequest.getResourceName())) {
			return false;
		}
		return true;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public List<IResource> invokeServer(Request theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		@SuppressWarnings("unchecked")
		List<IResource> retVal=(List<IResource>) invokeServerMethod(theMethodParams);
		return retVal;
	}

	@Override
	protected Object parseRequestObject(Request theRequest) {
		EncodingEnum encoding = determineResponseEncoding(theRequest);
		IParser parser = encoding.newParser(getContext());
		Bundle bundle = parser.parseBundle(theRequest.getInputReader());
		return bundle;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		List<IResource> resources = (List<IResource>) theArgs[myResourceParameterIndex];
		FhirContext context = getContext();
		
		return new HttpPostClientInvocation(context, resources);
	}

}
