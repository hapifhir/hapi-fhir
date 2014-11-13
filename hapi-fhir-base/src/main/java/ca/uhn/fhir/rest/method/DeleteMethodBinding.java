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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DeleteMethodBinding extends BaseOutcomeReturningMethodBinding {

	private String myResourceName;
	private Integer myIdParameterIndex;

	public DeleteMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Delete.class, 
				theProvider);

		Delete deleteAnnotation = theMethod.getAnnotation(Delete.class);
		Class<? extends IResource> resourceType = deleteAnnotation.type();
		if (resourceType != IResource.class) {
			RuntimeResourceDefinition def = theContext.getResourceDefinition(resourceType);
			myResourceName = def.getName();
		} else {
			if (theProvider != null && theProvider instanceof IResourceProvider) {
				RuntimeResourceDefinition def = theContext.getResourceDefinition(((IResourceProvider) theProvider).getResourceType());
				myResourceName = def.getName();
			} else {
				throw new ConfigurationException("Can not determine resource type for method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getCanonicalName() + " - Did you forget to include the resourceType() value on the @"
						+ Delete.class.getSimpleName() + " method annotation?");
			}
		}

		myIdParameterIndex = MethodUtil.findIdParameterIndex(theMethod);
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}

		Integer versionIdParameterIndex = MethodUtil.findVersionIdParameterIndex(theMethod);
		if (versionIdParameterIndex != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has a parameter annotated with the @" + VersionIdParam.class.getSimpleName()
					+ " annotation but delete methods may not have this annotation");
		}

	}

	@Override
	protected boolean requestContainsResource() {
		return false;
	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.DELETE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.DELETE);
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(getContext().getResourceDefinition(theResource).getName());

		return new HttpPostClientInvocation(getContext(), theResource, urlExtension.toString());
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}
		
		if (idDt.hasResourceType()==false) {
			idDt = idDt.withResourceType(getResourceName());
		}else if (getResourceName().equals(idDt.getResourceType())==false) {
			throw new InvalidRequestException("ID parameter has the wrong resource type, expected '" + getResourceName() + "', found: " + idDt.getResourceType());
		}
		
		HttpDeleteClientInvocation retVal = createDeleteInvocation(idDt);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null);
		}

		return retVal;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(IdDt theId) {
		HttpDeleteClientInvocation retVal = new HttpDeleteClientInvocation(theId);
		return retVal;
	}

	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		theParams[myIdParameterIndex] = theRequest.getId();
	}


	@Override
	protected String getMatchingOperation() {
		return null;
	}

	
}
