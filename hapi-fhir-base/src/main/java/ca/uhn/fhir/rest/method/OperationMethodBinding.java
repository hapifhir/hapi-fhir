package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseParameters;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class OperationMethodBinding extends BaseResourceReturningMethodBinding {

	private Integer myIdParamIndex;
	private String myName;

	public OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider, Operation theAnnotation) {
		super(theReturnResourceType, theMethod, theContext, theProvider);

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);
		myName = theAnnotation.name();
		if (isBlank(myName)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getName() + " is annotated with @" + Operation.class.getSimpleName() + " but this annotation has no name defined");
		}
		if (myName.startsWith("$") == false) {
			myName = "$" + myName;
		}

		if (theContext.getVersion().getVersion().isEquivalentTo(FhirVersionEnum.DSTU1)) {
			throw new ConfigurationException("@" + Operation.class.getSimpleName() + " methods are not supported on servers for FHIR version " + theContext.getVersion().getVersion().name());
		}
		
		if (theReturnTypeFromRp != null) {
			setResourceName(theContext.getResourceDefinition(theReturnTypeFromRp).getName());
		} else {
			if (Modifier.isAbstract(theAnnotation.type().getModifiers())== false) {
				setResourceName(theContext.getResourceDefinition(theAnnotation.type()).getName());
			} else {
				setResourceName(null);
			}
		}
		
		if (theMethod.getReturnType().isAssignableFrom(Bundle.class)) {
			throw new ConfigurationException("Can not return a DSTU1 bundle from an @" + Operation.class.getSimpleName() + " method. Found in method " + theMethod.getName() + " defined in type " + theMethod.getDeclaringClass().getName());
		}
		
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (getResourceName() == null) {
			if (isNotBlank(theRequest.getResourceName())) {
				return false;
			}
		} else if (!getResourceName().equals(theRequest.getResourceName())) {
			return false;
		}

		boolean requestHasId = theRequest.getId() != null;
		if (requestHasId != (myIdParamIndex != null)) {
			return false;
		}

		return myName.equals(theRequest.getOperation());
	}

	@Override
	protected Object parseRequestObject(Request theRequest) throws IOException {
		EncodingEnum encoding = RestfulServerUtils.determineRequestEncoding(theRequest);
		IParser parser = encoding.newParser(getContext());
		BufferedReader requestReader = theRequest.getServletRequest().getReader();

		Class<? extends IBaseResource> wantedResourceType = getContext().getResourceDefinition("Parameters").getImplementingClass();
		return parser.parseResource(wantedResourceType, requestReader);
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		String id = null;
		if (myIdParamIndex != null) {
			IdDt idDt = (IdDt) theArgs[myIdParamIndex];
			id = idDt.getValue();
		}
		IBaseParameters parameters = (IBaseParameters) getContext().getResourceDefinition("Parameters").newInstance();

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, parameters);
			}
		}

		return createOperationInvocation(getContext(), getResourceName(), id, myName, parameters);
	}

	@Override
	public Object invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theMethodParams);
		IBundleProvider retVal = toResourceList(response);
		return retVal;
	}

	public static HttpPostClientInvocation createOperationInvocation(FhirContext theContext, String theResourceName, String theId, String theOperationName, IBaseParameters theInput) {
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
		if (!theOperationName.startsWith("$")) {
			b.append("$");
		}
		b.append(theOperationName);

		return new HttpPostClientInvocation(theContext, theInput, b.toString());
	}

}
