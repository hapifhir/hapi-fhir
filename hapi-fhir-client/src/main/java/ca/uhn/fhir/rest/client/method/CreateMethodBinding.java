package ca.uhn.fhir.rest.client.method;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class CreateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	public CreateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Create.class, theProvider);
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IBaseResource theResource) {
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

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.CREATE;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.POST);
	}

	@Override
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		if (isNotBlank(theUrlId)) {
			String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "idInUrlForCreate", theUrlId);
			throw new InvalidRequestException(msg);
		}
		if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
			if (isNotBlank(theResourceId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "idInBodyForCreate", theResourceId);
				throw new InvalidRequestException(msg);
			}
		} else {
			theResource.setId((IIdType)null);
		}
	}

}
