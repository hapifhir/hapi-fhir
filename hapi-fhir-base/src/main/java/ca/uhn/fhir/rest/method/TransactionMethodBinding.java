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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TransactionMethodBinding extends BaseResourceReturningMethodBinding {

	private int myTransactionParamIndex;

	public TransactionMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(null, theMethod, theConetxt, theProvider);

		myTransactionParamIndex = -1;
		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof TransactionParamBinder) {
				myTransactionParamIndex = index;
			}
			index++;
		}

		if (myTransactionParamIndex == -1) {
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

	@SuppressWarnings("unchecked")
	@Override
	public IBundleProvider invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		// Grab the IDs of all of the resources in the transaction
		List<IResource> resources;
		if (theMethodParams[myTransactionParamIndex] instanceof Bundle) {
			resources = ((Bundle) theMethodParams[myTransactionParamIndex]).toListOfResources();
		} else {
			resources = (List<IResource>) theMethodParams[myTransactionParamIndex];
		}
		
		IdentityHashMap<IResource, IdDt> oldIds = new IdentityHashMap<IResource, IdDt>();
		for (IResource next : resources) {
			oldIds.put(next, next.getId());
		}

		// Call the server implementation method
		Object response = invokeServerMethod(theMethodParams);
		IBundleProvider retVal = toResourceList(response);

		/*
		int offset = 0;
		if (retVal.size() != resources.size()) {
			if (retVal.size() > 0 && retVal.getResources(0, 1).get(0) instanceof OperationOutcome) {
				offset = 1;
			} else {
				throw new InternalErrorException("Transaction bundle contained " + resources.size() + " entries, but server method response contained " + retVal.size() + " entries (must be the same)");
			}
		}
		 */
		
		List<IResource> retResources = retVal.getResources(0, retVal.size());
		for (int i = 0; i < retResources.size(); i++) {
			IdDt oldId = oldIds.get(retResources.get(i));
			IResource newRes = retResources.get(i);
			if (newRes.getId() == null || newRes.getId().isEmpty()) {
				if (!(newRes instanceof OperationOutcome)) {
					throw new InternalErrorException("Transaction method returned resource at index " + i + " with no id specified - IResource#setId(IdDt)");
				}
			}

			if (oldId != null && !oldId.isEmpty()) {
				if (!oldId.equals(newRes.getId())) {
					newRes.getResourceMetadata().put(ResourceMetadataKeyEnum.PREVIOUS_ID, oldId);
				}
			}
		}

		return retVal;

		
		
			}

	@Override
	protected Object parseRequestObject(Request theRequest) throws IOException {
		EncodingEnum encoding = RestfulServer.determineResponseEncoding(theRequest.getServletRequest());
		IParser parser = encoding.newParser(getContext());
		Bundle bundle = parser.parseBundle(theRequest.getServletRequest().getReader());
		return bundle;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		FhirContext context = getContext();
		if (theArgs[myTransactionParamIndex] instanceof Bundle) {
			Bundle bundle = (Bundle) theArgs[myTransactionParamIndex];
			return createTransactionInvocation(bundle, context);
		} else {
			@SuppressWarnings("unchecked")
			List<IResource> resources = (List<IResource>) theArgs[myTransactionParamIndex];
			return createTransactionInvocation(resources, context);
		}
	}

	public static BaseHttpClientInvocation createTransactionInvocation(List<IResource> theResources, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theResources);
	}

	public static BaseHttpClientInvocation createTransactionInvocation(Bundle theBundle, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theBundle);
	}

}
