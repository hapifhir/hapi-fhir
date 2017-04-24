package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isBlank;
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());
	}

	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		/*
		 * We are being a bit lenient here, since technically the client is supposed to include the version in the
		 * Content-Location header, but we allow it in the PUT URL as well..
		 */
		String locationHeader = theRequest.getHeader(Constants.HEADER_CONTENT_LOCATION);
		IIdType id = theRequest.getId();
		if (isNotBlank(locationHeader)) {
			id.setValue(locationHeader);
			if (isNotBlank(id.getResourceType())) {
				if (!getResourceName().equals(id.getResourceType())) {
					throw new InvalidRequestException(
							"Attempting to update '" + getResourceName() + "' but content-location header specifies different resource type '" + id.getResourceType() + "' - header value: " + locationHeader);
				}
			}
		}

		id = applyETagAsVersion(theRequest, id);

		if (theRequest.getId() != null && theRequest.getId().hasVersionIdPart() == false) {
			if (id != null && id.hasVersionIdPart()) {
				theRequest.getId().setValue(id.getValue());
			}
		}

		if (isNotBlank(locationHeader)) {
			MethodOutcome mo = new MethodOutcome();
			parseContentLocation(getContext(), mo, locationHeader);
			if (mo.getId() == null || mo.getId().isEmpty()) {
				throw new InvalidRequestException("Invalid Content-Location header for resource " + getResourceName() + ": " + locationHeader);
			}
		}

		super.addParametersForServerRequest(theRequest, theParams);
	}

	public static IIdType applyETagAsVersion(RequestDetails theRequest, IIdType id) {
		String ifMatchValue = theRequest.getHeader(Constants.HEADER_IF_MATCH);
		if (isNotBlank(ifMatchValue)) {
			ifMatchValue = MethodUtil.parseETagValue(ifMatchValue);
			if (id != null && id.hasVersionIdPart() == false) {
				id = id.withVersion(ifMatchValue);
			}
		}
		return id;
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}

		FhirContext context = getContext();

		HttpPutClientInvocation retVal = MethodUtil.createUpdateInvocation(theResource, null, idDt, context);

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
				throw new InvalidRequestException(msg);
			}
			if (isBlank(theResourceId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInBodyForUpdate");
				throw new InvalidRequestException(msg);
			}
			if (!theResourceId.equals(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "incorrectIdForUpdate", theResourceId, theUrlId);
				throw new InvalidRequestException(msg);
			}
		} else {
			theResource.setId((IIdType)null);
		}
		
	}
}
