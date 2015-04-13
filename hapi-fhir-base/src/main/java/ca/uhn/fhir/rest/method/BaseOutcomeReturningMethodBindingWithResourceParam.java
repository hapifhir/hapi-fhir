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

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private int myResourceParameterIndex;
	private String myResourceName;
	private boolean myBinary;
	private Class<? extends IBaseResource> myResourceType;
	private Integer myIdParamIndex;

	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);

		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				if (myResourceType != null) {
					throw new ConfigurationException("Method " + theMethod.getName() + " on type " + theMethod.getDeclaringClass() + " has more than one @ResourceParam. Only one is allowed.");
				}
				
				resourceParameter = (ResourceParameter) next;
				Class<? extends IBaseResource> providerResourceType = resourceParameter.getResourceType();

				if (theProvider instanceof IResourceProvider) {
					providerResourceType = ((IResourceProvider) theProvider).getResourceType();
				}

				if (IBaseBinary.class.isAssignableFrom(providerResourceType)) {
					myBinary = true;
				}

				myResourceType = resourceParameter.getResourceType();
				if (Modifier.isAbstract(myResourceType.getModifiers())) {
					myResourceType = providerResourceType;
				}
				
				myResourceName = theContext.getResourceDefinition(providerResourceType).getName();

				myResourceParameterIndex = index;
			}
			index++;
		}
		
		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);

		if (resourceParameter == null) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with @" + ResourceParam.class.getSimpleName());
		}

	}

	@Override
	protected Class<? extends IBaseResource> requestContainsResourceType() {
		return myResourceType;
	}

	@Override
	protected IBaseResource parseIncomingServerResource(Request theRequest) throws IOException {
		if (myBinary) {
			String ct = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_TYPE);
			byte[] contents = IOUtils.toByteArray(theRequest.getServletRequest().getInputStream());
			
			IBaseBinary binary = (IBaseBinary) getContext().getResourceDefinition("Binary").newInstance();
			binary.setContentType(ct);
			binary.setContent(contents);
			
			return binary;
		} else {
			return super.parseIncomingServerResource(theRequest);
		}
	}

	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		if (myIdParamIndex != null) {
			theParams[myIdParamIndex] = theRequest.getId();
		}
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IResource resource = (IResource) theArgs[myResourceParameterIndex];
		if (resource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		BaseHttpClientInvocation retVal = createClientInvocation(theArgs, resource);
		return retVal;
	}

}
