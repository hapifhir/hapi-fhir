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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;
	private Integer myVersionIdParameterIndex;

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = MethodUtil.findIdParameterIndex(theMethod);
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}
		myVersionIdParameterIndex = MethodUtil.findVersionIdParameterIndex(theMethod);
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.UPDATE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		/*
		 * We are being a bit lenient here, since technically the client is supposed to include the version in the
		 * Content-Location header, but we allow it in the PUT URL as well..
		 */
		String locationHeader = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_LOCATION);
		IdDt id = new IdDt(locationHeader);
		if (isNotBlank(id.getResourceType())) {
			if (!getResourceName().equals(id.getResourceType())) {
				throw new InvalidRequestException("Attempting to update '" + getResourceName() + "' but content-location header specifies different resource type '" + id.getResourceType() + "' - header value: " + locationHeader);
			}
		}

		if (theRequest.getId() != null && theRequest.getId().hasVersionIdPart() == false) {
			if (id != null && id.hasVersionIdPart()) {
				theRequest.setId(id);
			}
		}
		
		if (isNotBlank(locationHeader)) {
			MethodOutcome mo = new MethodOutcome();
			parseContentLocation(mo, getResourceName(), locationHeader);
			if (mo.getId() == null || mo.getId().isEmpty()) {
				throw new InvalidRequestException("Invalid Content-Location header for resource " + getResourceName() + ": " + locationHeader);
			}
		}

		theParams[myIdParameterIndex] = theRequest.getId();
		if (myVersionIdParameterIndex != null) {
			theParams[myVersionIdParameterIndex] = theRequest.getId();
		}
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}

		if (myVersionIdParameterIndex != null) {
			IdDt versionIdDt = (IdDt) theArgs[myVersionIdParameterIndex];
			if (idDt.hasVersionIdPart() == false) {
				idDt = idDt.withVersion(versionIdDt.getIdPart());
			}
		}
		FhirContext context = getContext();

		HttpPutClientInvocation retVal = MethodUtil.createUpdateInvocation(theResource, null,idDt, context);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null);
		}

		return retVal;
	}

	

	/*
	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (super.incomingServerRequestMatchesMethod(theRequest)) {
			if (myVersionIdParameterIndex != null) {
				if (theRequest.getVersionId() == null) {
					return false;
				}
			} else {
				if (theRequest.getVersionId() != null) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	*/
	
	@Override
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.PUT);
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

}
