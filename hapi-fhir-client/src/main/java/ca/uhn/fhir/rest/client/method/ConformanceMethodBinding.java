package ca.uhn.fhir.rest.client.method;

/*
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

import org.hl7.fhir.instance.model.api.IBaseConformance;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {

	public ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod.getReturnType(), theMethod, theContext, theProvider);

		MethodReturnTypeEnum methodReturnType = getMethodReturnType();
		Class<?> genericReturnType = (Class<?>) theMethod.getGenericReturnType();
		if (methodReturnType != MethodReturnTypeEnum.RESOURCE || !IBaseConformance.class.isAssignableFrom(genericReturnType)) {
			throw new ConfigurationException(Msg.code(1426) + "Conformance resource provider method '" + theMethod.getName() + "' should return a Conformance resource class, returns: " + theMethod.getReturnType());
		}

	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public HttpGetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpGetClientInvocation retVal = MethodUtil.createConformanceInvocation(getContext());

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
			}
		}

		return retVal;
	}


	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.METADATA;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

}
