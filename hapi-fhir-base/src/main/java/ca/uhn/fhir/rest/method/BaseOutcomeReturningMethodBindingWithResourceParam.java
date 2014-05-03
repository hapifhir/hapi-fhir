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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private int myResourceParameterIndex;
	private String myResourceName;

	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);

		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				myResourceName = theContext.getResourceDefinition(resourceParameter.getResourceType()).getName();
				myResourceParameterIndex = index;
			}
			index++;
		}

		if (resourceParameter == null) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with @" + ResourceParam.class.getSimpleName());
		}

	}

	/**
	 * For subclasses to override
	 */
	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		// nothing
	}

	

	

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IResource resource = (IResource) theArgs[myResourceParameterIndex];
		if (resource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		return createClientInvocation(theArgs, resource);
	}

}
