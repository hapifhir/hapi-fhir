
package ca.uhn.fhir.jpa.interceptor.r4;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;

public class WebSocketSubscriptionR4Interceptor extends ServerOperationInterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(WebSocketSubscriptionR4Interceptor.class);

	private IFhirResourceDaoSubscription<Subscription> mySubscriptionDaoCasted;

	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	private IFhirResourceDao<Subscription> mySubscriptionDao;

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		if (theRequestDetails.getRestOperationType().equals(RestOperationTypeEnum.DELETE)) {
			mySubscriptionDaoCasted.pollForNewUndeliveredResources(theRequestDetails.getResourceName());
		}

		return super.incomingRequestPostProcessed(theRequestDetails, theRequest, theResponse);
	}

	/**
	 * Checks for websocket subscriptions
	 * 
	 * @param theRequestDetails
	 *           A bean containing details about the request that is about to be processed, including details such as the
	 *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
	 *           pulled out of the {@link HttpServletRequest servlet request}.
	 * @param theResponseObject
	 *           The actual object which is being streamed to the client as a response
	 * @return
	 */
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		if (theRequestDetails.getResourceName() == null ||
				theRequestDetails.getResourceName().isEmpty() ||
				theRequestDetails.getResourceName().equals("Subscription")) {
			return super.outgoingResponse(theRequestDetails, theResponseObject);
		}

		if (theRequestDetails.getRequestType().equals(RequestTypeEnum.POST) || theRequestDetails.getRequestType().equals(RequestTypeEnum.PUT)) {
			ourLog.info("Found POST or PUT for a non-subscription resource");
			mySubscriptionDaoCasted.pollForNewUndeliveredResources(theRequestDetails.getResourceName());
		}

		return super.outgoingResponse(theRequestDetails, theResponseObject);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void postConstruct() {
		mySubscriptionDaoCasted = (IFhirResourceDaoSubscription) mySubscriptionDao;
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		// nothing
	}
}
