package ca.uhn.fhir.rest.client.method;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UpdateHistoryRewriteMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;

	public UpdateHistoryRewriteMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.UPDATE;
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IBaseResource theResource) {
		IIdType idDt = (IIdType) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException(Msg.code(2092) + "ID can not be null");
		}

		FhirContext context = getContext();

		HttpPutClientInvocation retVal = MethodUtil.createUpdateHistoryRewriteInvocation(theResource, null, idDt, context);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}
		return retVal;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.PUT);
	}

	@Override
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		if (isBlank(theMatchUrl)) {
			if (isBlank(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInUrlForUpdate");
				throw new InvalidRequestException(Msg.code(2093) + msg);
			}
			if (isBlank(theResourceId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInBodyForUpdate");
				throw new InvalidRequestException(Msg.code(2094) + msg);
			}
			if (!theResourceId.equals(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "incorrectIdForUpdate", theResourceId, theUrlId);
				throw new InvalidRequestException(Msg.code(2095) + msg);
			}
		} else {
			theResource.setId((IIdType) null);
		}

	}
}
