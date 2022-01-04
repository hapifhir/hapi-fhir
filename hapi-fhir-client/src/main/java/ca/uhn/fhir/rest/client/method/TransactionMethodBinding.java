package ca.uhn.fhir.rest.client.method;

/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.Method;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TransactionMethodBinding extends BaseResourceReturningMethodBinding {

	private int myTransactionParamIndex;

	public TransactionMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(null, theMethod, theContext, theProvider);

		myTransactionParamIndex = -1;
		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof TransactionParameter) {
				if (myTransactionParamIndex != -1) {
					throw new ConfigurationException(Msg.code(1418) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " has multiple parameters annotated with the @" + TransactionParam.class + " annotation, exactly one is required for @" + Transaction.class
							+ " methods");
				}
				myTransactionParamIndex = index;
			}
			index++;
		}

		if (myTransactionParamIndex == -1) {
			throw new ConfigurationException(Msg.code(1419) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with the @" + TransactionParam.class + " annotation");
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
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		FhirContext context = getContext();
		Object arg = theArgs[myTransactionParamIndex];
		
		if (arg instanceof IBaseBundle) {
			return createTransactionInvocation((IBaseBundle) arg, context);
		}
		
		@SuppressWarnings("unchecked")
		List<IBaseResource> resources = (List<IBaseResource>) arg;
		return createTransactionInvocation(resources, context);
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
