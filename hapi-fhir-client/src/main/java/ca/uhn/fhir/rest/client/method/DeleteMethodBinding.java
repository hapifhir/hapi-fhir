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
import java.lang.reflect.Method;
import java.util.*;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
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
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IBaseResource theResource) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(getContext().getResourceType(theResource));

		return new HttpPostClientInvocation(getContext(), theResource, urlExtension.toString());
	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IIdType id = (IIdType) theArgs[getIdParameterIndex()];
		if (id == null) {
			throw new NullPointerException(Msg.code(1472) + "ID can not be null");
		}

		if (id.hasResourceType() == false) {
			id = id.withResourceType(getResourceName());
		} else if (getResourceName().equals(id.getResourceType()) == false) {
			throw new InvalidRequestException(Msg.code(1473) + "ID parameter has the wrong resource type, expected '" + getResourceName() + "', found: " + id.getResourceType());
		}

		HttpDeleteClientInvocation retVal = createDeleteInvocation(getContext(), id, Collections.emptyMap());

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(FhirContext theContext, IIdType theId, Map<String, List<String>> theAdditionalParams) {
		return new HttpDeleteClientInvocation(theContext, theId, theAdditionalParams);
	}


	@Override
	protected String getMatchingOperation() {
		return null;
	}

	public static HttpDeleteClientInvocation createDeleteInvocation(FhirContext theContext, String theSearchUrl, Map<String, List<String>> theParams) {
		return new HttpDeleteClientInvocation(theContext, theSearchUrl, theParams);
	}

}
