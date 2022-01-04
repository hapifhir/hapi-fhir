package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.i18n.Msg;
import static org.apache.commons.lang3.StringUtils.isBlank;
/*
 * #%L
 * HAPI FHIR - Server Framework
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import javax.annotation.Nonnull;

public class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);
	}

	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		IIdType id = theRequest.getId();
		id = applyETagAsVersion(theRequest, id);
		if (theRequest.getId() != null && theRequest.getId().hasVersionIdPart() == false) {
			if (id != null && id.hasVersionIdPart()) {
				theRequest.getId().setValue(id.getValue());
			}
		}
		super.addParametersForServerRequest(theRequest, theParams);
	}

	public static IIdType applyETagAsVersion(RequestDetails theRequest, IIdType id) {
		String ifMatchValue = theRequest.getHeader(Constants.HEADER_IF_MATCH);
		if (isNotBlank(ifMatchValue)) {
			ifMatchValue = ParameterUtil.parseETagValue(ifMatchValue);
			if (id != null && id.hasVersionIdPart() == false) {
				id = id.withVersion(ifMatchValue);
			}
		}
		return id;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.UPDATE;
	}

	/*
	 * @Override public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) { if
	 * (super.incomingServerRequestMatchesMethod(theRequest)) { if (myVersionIdParameterIndex != null) { if
	 * (theRequest.getVersionId() == null) { return false; } } else { if (theRequest.getVersionId() != null) { return
	 * false; } } return true; } else { return false; } }
	 */

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.PUT);
	}

	@Override
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		if (isBlank(theMatchUrl)) {
			if (isBlank(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInUrlForUpdate");
				throw new InvalidRequestException(Msg.code(418) + msg);
			}
			if (isBlank(theResourceId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInBodyForUpdate");
				throw new InvalidRequestException(Msg.code(419) + msg);
			}
			if (!theResourceId.equals(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "incorrectIdForUpdate", theResourceId, theUrlId);
				throw new InvalidRequestException(Msg.code(420) + msg);
			}
		} else {
			theResource.setId((IIdType) null);
		}

	}
}
