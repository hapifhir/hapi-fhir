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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.TransactionParameter;
import ca.uhn.fhir.rest.param.TransactionParameter.ParamStyle;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TransactionMethodBinding extends BaseResourceReturningMethodBinding {

	private int myTransactionParamIndex;
	private ParamStyle myTransactionParamStyle;

	public TransactionMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(null, theMethod, theConetxt, theProvider);

		myTransactionParamIndex = -1;
		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof TransactionParameter) {
				if (myTransactionParamIndex != -1) {
					throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " has multiple parameters annotated with the @" + TransactionParam.class + " annotation, exactly one is required for @" + Transaction.class
							+ " methods");
				}
				myTransactionParamIndex = index;
				myTransactionParamStyle = ((TransactionParameter) next).getParamStyle();
			}
			index++;
		}

		if (myTransactionParamIndex == -1) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with the @" + TransactionParam.class + " annotation");
		}
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.TRANSACTION;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.TRANSACTION_RESPONSE;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() != RequestTypeEnum.POST) {
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
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		FhirContext context = getContext();
		if (theArgs[myTransactionParamIndex] instanceof Bundle) {
			Bundle bundle = (Bundle) theArgs[myTransactionParamIndex];
			return createTransactionInvocation(bundle, context);
		} else {
			@SuppressWarnings("unchecked")
			List<IBaseResource> resources = (List<IBaseResource>) theArgs[myTransactionParamIndex];
			return createTransactionInvocation(resources, context);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invokeServer(RestfulServer theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {

		/*
		 * The design of HAPI's transaction method for DSTU1 support assumed that a transaction was just an update on a
		 * bunch of resources (because that's what it was), but in DSTU2 transaction has become much more broad, so we
		 * no longer hold the user's hand much here.
		 */
		if (myTransactionParamStyle == ParamStyle.RESOURCE_BUNDLE) {
			// This is the DSTU2 style
			Object response = invokeServerMethod(theServer, theRequest, theMethodParams);
			return response;
		}

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
		Object response = invokeServerMethod(theServer, theRequest, theMethodParams);
		IBundleProvider retVal = toResourceList(response);

		/*
		 * int offset = 0; if (retVal.size() != resources.size()) { if (retVal.size() > 0 && retVal.getResources(0,
		 * 1).get(0) instanceof OperationOutcome) { offset = 1; } else { throw new
		 * InternalErrorException("Transaction bundle contained " + resources.size() +
		 * " entries, but server method response contained " + retVal.size() + " entries (must be the same)"); } }
		 */

		List<IBaseResource> retResources = retVal.getResources(0, retVal.size());
		for (int i = 0; i < retResources.size(); i++) {
			IdDt oldId = oldIds.get(retResources.get(i));
			IBaseResource newRes = retResources.get(i);
			if (newRes.getIdElement() == null || newRes.getIdElement().isEmpty()) {
				if (!(newRes instanceof BaseOperationOutcome)) {
					throw new InternalErrorException("Transaction method returned resource at index " + i + " with no id specified - IResource#setId(IdDt)");
				}
			}

			if (oldId != null && !oldId.isEmpty()) {
				if (!oldId.equals(newRes.getIdElement()) && newRes instanceof IResource) {
					((IResource)newRes).getResourceMetadata().put(ResourceMetadataKeyEnum.PREVIOUS_ID, oldId);
				}
			}
		}

		return retVal;
	}

	public static BaseHttpClientInvocation createTransactionInvocation(Bundle theBundle, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theBundle);
	}

	public static BaseHttpClientInvocation createTransactionInvocation(IBaseBundle theBundle, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theBundle);
	}

	public static BaseHttpClientInvocation createTransactionInvocation(List<? extends IBaseResource> theResources, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theResources, BundleTypeEnum.TRANSACTION);
	}

	public static BaseHttpClientInvocation createTransactionInvocation(String theRawBundle, FhirContext theContext) {
		return new HttpPostClientInvocation(theContext, theRawBundle, true, "");
	}

}
