package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.lang.reflect.Modifier;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private Integer myIdParamIndex;
	private String myResourceName;
	private int myResourceParameterIndex = -1;
	private Class<? extends IBaseResource> myResourceType;
	private Class<? extends IIdType> myIdParamType;

	@SuppressWarnings("unchecked")
	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);

		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				if (resourceParameter.getMode() != ResourceParameter.Mode.RESOURCE) {
					continue;
				}
				if (myResourceType != null) {
					throw new ConfigurationException("Method " + theMethod.getName() + " on type " + theMethod.getDeclaringClass() + " has more than one @ResourceParam. Only one is allowed.");
				}

				myResourceType = resourceParameter.getResourceType();

				myResourceParameterIndex = index;
			}
			index++;
		}

		if ((myResourceType == null || Modifier.isAbstract(myResourceType.getModifiers())) && (theProvider instanceof IResourceProvider)) {
			myResourceType = ((IResourceProvider) theProvider).getResourceType();
		}
		if (myResourceType == null) {
			throw new ConfigurationException("Unable to determine resource type for method: " + theMethod);
		}

		myResourceName = theContext.getResourceDefinition(myResourceType).getName();
		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());
		if (myIdParamIndex != null) {
			myIdParamType = (Class<? extends IIdType>) theMethod.getParameterTypes()[myIdParamIndex];
		}
		
		if (resourceParameter == null) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a resource parameter annotated with @" + ResourceParam.class.getSimpleName());
		}

	}

	@Override
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		super.populateActionRequestDetailsForInterceptor(theRequestDetails, theDetails, theMethodParams);
		
		/*
		 * If the method has no parsed resource parameter, we parse here in order to have something for the interceptor.
		 */
		if (myResourceParameterIndex != -1) {
			theDetails.setResource((IBaseResource) theMethodParams[myResourceParameterIndex]);
		} else {
			theDetails.setResource(ResourceParameter.parseResourceFromRequest(theRequestDetails, this, myResourceType));
		}
		
		
	}

	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		if (myIdParamIndex != null) {
			theParams[myIdParamIndex] = MethodUtil.convertIdToType(theRequest.getId(), myIdParamType);
		}
	}


	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IResource resource = (IResource) theArgs[myResourceParameterIndex]; // TODO: use IBaseResource
		if (resource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		BaseHttpClientInvocation retVal = createClientInvocation(theArgs, resource);
		return retVal;
	}

}
