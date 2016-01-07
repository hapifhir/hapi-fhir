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
import java.util.Collections;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;

public class CreateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	public CreateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Create.class, theProvider);
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.CREATE;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.POST);
	}
	
	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		FhirContext context = getContext();

		BaseHttpClientInvocation retVal = MethodUtil.createCreateInvocation(theResource, context);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
			}
		}

		return retVal;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

}
