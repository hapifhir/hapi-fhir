package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR Library
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

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.PutClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;

	private Integer myVersionIdParameterIndex;

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = ParameterUtil.findIdParameterIndex(theMethod);
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}
		myVersionIdParameterIndex = ParameterUtil.findVersionIdParameterIndex(theMethod);
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
		 * We are being a bit lenient here, since technically the client is
		 * supposed to include the version in the Content-Location header, but
		 * we allow it in the PUT URL as well..
		 */
		String locationHeader = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_LOCATION);
		if (isNotBlank(locationHeader)) {
			MethodOutcome mo = new MethodOutcome();
			parseContentLocation(mo, getResourceName(), locationHeader);
			if (mo.getId() == null || mo.getId().isEmpty()) {
				throw new InvalidRequestException("Invalid Content-Location header for resource " + getResourceName() + ": " + locationHeader);
			}
			if (mo.getVersionId() != null && mo.getVersionId().isEmpty() == false) {
				theRequest.setVersion(mo.getVersionId());
			}
		}

		theParams[myIdParameterIndex] = theRequest.getId();
		if (myVersionIdParameterIndex != null) {
			theParams[myVersionIdParameterIndex] = theRequest.getVersionId();
		}
	}

	@Override
	protected BaseClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}

		IdDt versionIdDt = null;
		if (myVersionIdParameterIndex != null) {
			versionIdDt = (IdDt) theArgs[myVersionIdParameterIndex];
		}
		FhirContext context = getContext();

		PutClientInvocation retVal = createUpdateInvocation(theResource, idDt, versionIdDt, context);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(theArgs[idx], null, retVal);
		}

		return retVal;
	}

	public static PutClientInvocation createUpdateInvocation(IResource theResource, IdDt idDt, IdDt versionIdDt, FhirContext context) {
		String id = idDt.getValue();
		String resourceName = context.getResourceDefinition(theResource).getName();
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);
		urlExtension.append('/');
		urlExtension.append(id);
		PutClientInvocation retVal = new PutClientInvocation(context, theResource, urlExtension.toString());

		if (versionIdDt != null) {
			String versionId = versionIdDt.getValue();
			if (StringUtils.isNotBlank(versionId)) {
				StringBuilder b = new StringBuilder();
				b.append('/');
				b.append(urlExtension);
				b.append("/_history/");
				b.append(versionId);
				retVal.addHeader(Constants.HEADER_CONTENT_LOCATION, b.toString());
			}
		}
		return retVal;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (super.incomingServerRequestMatchesMethod(theRequest)) {
			if (myVersionIdParameterIndex != null) {
				if (theRequest.getVersionId() == null) {
					return false;
				}
			}else {
				if (theRequest.getVersionId() != null) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.PUT);
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

}
