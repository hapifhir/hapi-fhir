package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.r4.model.Subscription.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
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

/**
 * Interceptor which requires newly created {@link Subscription subscriptions} to be in
 * {@link SubscriptionStatus#REQUESTED} state and prevents clients from changing the status.
 */
public class SubscriptionsRequireManualActivationInterceptorR4 extends ServerOperationInterceptorAdapter {

	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	private IFhirResourceDao<Subscription> myDao;

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		if (myDao.getContext().getResourceType(theResource).equals(ResourceTypeEnum.SUBSCRIPTION.getCode())) {
			verifyStatusOk(RestOperationTypeEnum.CREATE, null, theResource);
		}
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		if (myDao.getContext().getResourceType(theNewResource).equals(ResourceTypeEnum.SUBSCRIPTION.getCode())) {
			verifyStatusOk(RestOperationTypeEnum.UPDATE, theOldResource, theNewResource);
		}
	}


	public void setDao(IFhirResourceDao<Subscription> theDao) {
		myDao = theDao;
	}

	private void verifyStatusOk(RestOperationTypeEnum theOperation, IBaseResource theOldResourceOrNull, IBaseResource theResource) {
		Subscription subscription = (Subscription) theResource;
		SubscriptionStatus newStatus = subscription.getStatusElement().getValue();

		if (newStatus == SubscriptionStatus.REQUESTED || newStatus == SubscriptionStatus.OFF) {
			return;
		}

		if (newStatus == null) {
			String actualCode = subscription.getStatusElement().getValueAsString();
			throw new UnprocessableEntityException(Msg.code(807) + "Can not " + theOperation.getCode() + " resource: Subscription.status must be populated on this server" + ((isNotBlank(actualCode)) ? " (invalid value " + actualCode + ")" : ""));
		}

		if (theOldResourceOrNull != null) {
			try {
				Subscription existing = (Subscription) theOldResourceOrNull;
				SubscriptionStatus existingStatus = existing.getStatusElement().getValue();
				if (existingStatus != newStatus) {
					verifyActiveStatus(theOperation, subscription, newStatus, existingStatus);
				}
			} catch (ResourceNotFoundException e) {
				verifyActiveStatus(theOperation, subscription, newStatus, null);
			}
		} else {
			verifyActiveStatus(theOperation, subscription, newStatus, null);
		}
	}

	private void verifyActiveStatus(RestOperationTypeEnum theOperation, Subscription theSubscription, SubscriptionStatus newStatus, SubscriptionStatus theExistingStatus) {
		SubscriptionChannelType channelType = theSubscription.getChannel().getTypeElement().getValue();

		if (channelType == null) {
			throw new UnprocessableEntityException(Msg.code(808) + "Subscription.channel.type must be populated");
		}

		if (channelType == SubscriptionChannelType.WEBSOCKET) {
			return;
		}

		if (theExistingStatus != null) {
			throw new UnprocessableEntityException(Msg.code(809) + "Subscription.status can not be changed from " + describeStatus(theExistingStatus) + " to " + describeStatus(newStatus));
		}

		if (theSubscription.getStatus() == null) {
			throw new UnprocessableEntityException(Msg.code(810) + "Can not " + theOperation.getCode().toLowerCase() + " resource: Subscription.status must be populated on this server");
		}

		throw new UnprocessableEntityException(Msg.code(811) + "Subscription.status must be '" + SubscriptionStatus.OFF.toCode() + "' or '" + SubscriptionStatus.REQUESTED.toCode() + "' on a newly created subscription");
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
