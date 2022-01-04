package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.method.TransactionParameter.ParamStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TransactionMethodBinding extends BaseResourceReturningMethodBinding {

	private int myTransactionParamIndex;
	private ParamStyle myTransactionParamStyle;

	public TransactionMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(null, theMethod, theContext, theProvider);

		myTransactionParamIndex = -1;
		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof TransactionParameter) {
				if (myTransactionParamIndex != -1) {
					throw new ConfigurationException(Msg.code(372) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " has multiple parameters annotated with the @"
							+ TransactionParam.class + " annotation, exactly one is required for @" + Transaction.class
							+ " methods");
				}
				myTransactionParamIndex = index;
				myTransactionParamStyle = ((TransactionParameter) next).getParamStyle();
			}
			index++;
		}

		if (myTransactionParamIndex == -1) {
			throw new ConfigurationException(Msg.code(373) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with the @"
					+ TransactionParam.class + " annotation");
		}
	}

	@Nonnull
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
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() != RequestTypeEnum.POST) {
			return MethodMatchEnum.NONE;
		}
		if (isNotBlank(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}
		if (isNotBlank(theRequest.getResourceName())) {
			return MethodMatchEnum.NONE;
		}
		return MethodMatchEnum.EXACT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {

		/*
		 * The design of HAPI's transaction method for DSTU1 support assumed that a transaction was just an update on a
		 * bunch of resources (because that's what it was), but in DSTU2 transaction has become much more broad, so we
		 * no longer hold the user's hand much here.
		 */
		if (myTransactionParamStyle == ParamStyle.RESOURCE_BUNDLE) {
			// This is the DSTU2 style
			Object response = invokeServerMethod(theRequest, theMethodParams);
			return response;
		}

		// Call the server implementation method
		Object response = invokeServerMethod(theRequest, theMethodParams);
		IBundleProvider retVal = toResourceList(response);

		/*
		 * int offset = 0; if (retVal.size() != resources.size()) { if (retVal.size() > 0 && retVal.getResources(0,
		 * 1).get(0) instanceof OperationOutcome) { offset = 1; } else { throw new
		 * InternalErrorException("Transaction bundle contained " + resources.size() +
		 * " entries, but server method response contained " + retVal.size() + " entries (must be the same)"); } }
		 */

		List<IBaseResource> retResources = retVal.getAllResources();
		for (int i = 0; i < retResources.size(); i++) {
			IBaseResource newRes = retResources.get(i);
			if (newRes.getIdElement() == null || newRes.getIdElement().isEmpty()) {
				if (!(newRes instanceof BaseOperationOutcome)) {
					throw new InternalErrorException(Msg.code(374) + "Transaction method returned resource at index " + i + " with no id specified - IResource#setId(IdDt)");
				}
			}
		}

		return retVal;
	}

	@Override
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		super.populateActionRequestDetailsForInterceptor(theRequestDetails, theDetails, theMethodParams);

		/*
		 * If the method has no parsed resource parameter, we parse here in order to have something for the interceptor.
		 */
		IBaseResource resource;
		if (myTransactionParamIndex != -1) {
			resource = (IBaseResource) theMethodParams[myTransactionParamIndex];
		} else {
			Class<? extends IBaseResource> resourceType = getContext().getResourceDefinition("Bundle").getImplementingClass();
			resource = ResourceParameter.parseResourceFromRequest(theRequestDetails, this, resourceType);
		}

		theRequestDetails.setResource(resource);
		if (theDetails != null) {
			theDetails.setResource(resource);
		}

	}

}
