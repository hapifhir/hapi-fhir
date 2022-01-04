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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private String myResourceName;
	private int myResourceParameterIndex = -1;
	private Class<? extends IBaseResource> myResourceType;

	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);

		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				if (myResourceType != null) {
					throw new ConfigurationException(Msg.code(1468) + "Method " + theMethod.getName() + " on type " + theMethod.getDeclaringClass() + " has more than one @ResourceParam. Only one is allowed.");
				}

				myResourceType = resourceParameter.getResourceType();

				myResourceParameterIndex = index;
			}
			index++;
		}

		if (myResourceType == null) {
			throw new ConfigurationException(Msg.code(1469) + "Unable to determine resource type for method: " + theMethod);
		}

		myResourceName = theContext.getResourceType(myResourceType);

		if (resourceParameter == null) {
			throw new ConfigurationException(Msg.code(1470) + "Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a resource parameter annotated with @" + ResourceParam.class.getSimpleName());
		}

	}


	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IBaseResource resource = (IBaseResource) theArgs[myResourceParameterIndex];
		if (resource == null) {
			throw new NullPointerException(Msg.code(1471) + "Resource can not be null");
		}

		BaseHttpClientInvocation retVal = createClientInvocation(theArgs, resource);
		return retVal;
	}

	/**
	 * Subclasses may override
	 */
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		return;
	}

}
