package ca.uhn.fhir.rest.param;

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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceParameter implements IParameter {

	private Class<? extends IResource> myResourceName ;

	public ResourceParameter(Class<? extends IResource> theParameterType) {
		myResourceName =theParameterType;
	}
	
	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		IResource resource = (IResource) theRequestContents;
		return resource;
	}

	public Class<? extends IResource> getResourceType() {
		return myResourceName;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore for now
	}
	
}
