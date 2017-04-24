package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DeleteMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody {

	public DeleteMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider, Delete.class, theMethod.getAnnotation(Delete.class).type());
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.DELETE;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.DELETE);
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(getContext().getResourceDefinition(theResource).getName());

		return new HttpPostClientInvocation(getContext(), theResource, urlExtension.toString());
	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IIdType idDt = (IIdType) theArgs[getIdParameterIndex()];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}

		if (idDt.hasResourceType() == false) {
			idDt = idDt.withResourceType(getResourceName());
		} else if (getResourceName().equals(idDt.getResourceType()) == false) {
			throw new InvalidRequestException("ID parameter has the wrong resource type, expected '" + getResourceName() + "', found: " + idDt.getResourceType());
		}

		HttpDeleteClientInvocation retVal = createDeleteInvocation(getContext(), idDt);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(FhirContext theContext, IIdType theId) {
		HttpDeleteClientInvocation retVal = new HttpDeleteClientInvocation(theContext, theId);
		return retVal;
	}

	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		theParams[getIdParameterIndex()] = theRequest.getId();
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(FhirContext theContext, String theSearchUrl) {
		HttpDeleteClientInvocation retVal = new HttpDeleteClientInvocation(theContext, theSearchUrl);
		return retVal;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(FhirContext theContext, String theResourceType, Map<String, List<String>> theParams) {
		return new HttpDeleteClientInvocation(theContext, theResourceType, theParams);
	}

}
