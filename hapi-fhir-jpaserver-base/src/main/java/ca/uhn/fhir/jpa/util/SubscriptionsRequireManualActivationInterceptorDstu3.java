package ca.uhn.fhir.jpa.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoSubscriptionDstu3;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Interceptor which requires newly created {@link Subscription subscriptions} to be in
 * {@link SubscriptionStatus#REQUESTED} state and prevents clients from changing the status.
 */
public class SubscriptionsRequireManualActivationInterceptorDstu3 extends InterceptorAdapter {

	public static final ResourceMetadataKeyEnum<Object> ALLOW_STATUS_CHANGE = new ResourceMetadataKeyEnum<Object>(FhirResourceDaoSubscriptionDstu3.class.getName() + "_ALLOW_STATUS_CHANGE") {
		private static final long serialVersionUID = 1;

		@CoverageIgnore
		@Override
		public Object get(IResource theResource) {
			throw new UnsupportedOperationException();
		}

		@CoverageIgnore
		@Override
		public void put(IResource theResource, Object theObject) {
			throw new UnsupportedOperationException();
		}
	};

	@Autowired
	@Qualifier("mySubscriptionDaoDstu3")
	private IFhirResourceDao<Subscription> myDao;

	@Override
	public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
		switch (theOperation) {
		case CREATE:
		case UPDATE:
			if (theProcessedRequest.getResourceType().equals("Subscription")) {
				verifyStatusOk(theOperation, theProcessedRequest);
			}
			break;
		default:
			break;
		}
	}

	public void setDao(IFhirResourceDao<Subscription> theDao) {
		myDao = theDao;
	}

	private void verifyStatusOk(RestOperationTypeEnum theOperation, ActionRequestDetails theRequestDetails) {
		Subscription subscription = (Subscription) theRequestDetails.getResource();
		SubscriptionStatus newStatus = subscription.getStatusElement().getValue();

		if (newStatus == SubscriptionStatus.REQUESTED || newStatus == SubscriptionStatus.OFF) {
			return;
		}

		if (newStatus == null) {
			String actualCode = subscription.getStatusElement().getValueAsString();
			throw new UnprocessableEntityException("Can not " + theOperation.getCode() + " resource: Subscription.status must be populated" + ((isNotBlank(actualCode)) ? " (invalid value " + actualCode + ")" : ""));
		}

		IIdType requestId = theRequestDetails.getId();
		if (requestId != null && requestId.hasIdPart()) {
			Subscription existing;
			try {
				existing = myDao.read(requestId, new ServletRequestDetails());
				SubscriptionStatus existingStatus = existing.getStatusElement().getValue();
				if (existingStatus != newStatus) {
					verifyActiveStatus(subscription, newStatus, existingStatus);
				}
			} catch (ResourceNotFoundException e) {
				verifyActiveStatus(subscription, newStatus, null);
			}
		} else {
			verifyActiveStatus(subscription, newStatus, null);
		}
	}

	private void verifyActiveStatus(Subscription theSubscription, SubscriptionStatus newStatus, SubscriptionStatus theExistingStatus) {
		SubscriptionChannelType channelType = theSubscription.getChannel().getTypeElement().getValue();

		if (channelType == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated");
		}

		if (channelType == SubscriptionChannelType.WEBSOCKET) {
			return;
		}

		if (theExistingStatus != null) {
			throw new UnprocessableEntityException("Subscription.status can not be changed from " + describeStatus(theExistingStatus) + " to " + describeStatus(newStatus));
		}

		throw new UnprocessableEntityException("Subscription.status must be '" + SubscriptionStatus.OFF.toCode() + "' or '" + SubscriptionStatus.REQUESTED.toCode() + "' on a newly created subscription");
	}

	private String describeStatus(SubscriptionStatus existingStatus) {
		String existingStatusString;
		if (existingStatus != null) {
			existingStatusString = '\'' + existingStatus.toCode() + '\'';
		} else {
			existingStatusString = "null";
		}
		return existingStatusString;
	}

}
