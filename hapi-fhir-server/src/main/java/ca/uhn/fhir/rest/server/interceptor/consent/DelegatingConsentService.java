package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementation of {@link IConsentService} that forwards to another
 * implementation of {@link IConsentService}. This class is mostly
 * provided for testing purposes.
 */
public class DelegatingConsentService implements IConsentService {

	private IConsentService myTarget;

	@Override
	public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		return myTarget.startOperation(theRequestDetails, theContextServices);
	}

	@Override
	public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return myTarget.canSeeResource(theRequestDetails, theResource, theContextServices);
	}

	@Override
	public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		return myTarget.willSeeResource(theRequestDetails, theResource ,theContextServices);
	}

	@Override
	public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
		myTarget.completeOperationSuccess(theRequestDetails, theContextServices);
	}

	@Override
	public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
		myTarget.completeOperationFailure(theRequestDetails, theException, theContextServices);
	}

	public void setTarget(IConsentService theTarget) {
		myTarget = theTarget;
	}
}
