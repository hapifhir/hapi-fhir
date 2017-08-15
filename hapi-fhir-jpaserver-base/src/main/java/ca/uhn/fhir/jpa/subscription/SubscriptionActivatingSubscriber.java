package ca.uhn.fhir.jpa.subscription;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class SubscriptionActivatingSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);

	/**
	 * Constructor
	 */
	public SubscriptionActivatingSubscriber(IFhirResourceDao<? extends IBaseResource> theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);
	}

	private void activateAndRegisterSubscriptionIfRequired(ResourceModifiedMessage theMsg) {
		FhirContext ctx = getSubscriptionDao().getContext();
		IBaseResource subscription = theMsg.getNewPayload();
		IPrimitiveType<?> status = ctx.newTerser().getSingleValueOrNull(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_STATUS, IPrimitiveType.class);
		String statusString = status.getValueAsString();

		String requestedStatus = Subscription.SubscriptionStatus.REQUESTED.toCode();
		String activeStatus = Subscription.SubscriptionStatus.ACTIVE.toCode();
		if (requestedStatus.equals(statusString)) {
			status.setValueAsString(activeStatus);
			ourLog.info("Activating and registering subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), requestedStatus, activeStatus);
			getSubscriptionDao().update(subscription);
			getIdToSubscription().put(subscription.getIdElement().getIdPart(), subscription);
		} else if (activeStatus.equals(statusString)) {
			ourLog.info("Registering active subscription {}", subscription.getIdElement().toUnqualified().getValue());
			getIdToSubscription().put(subscription.getIdElement().getIdPart(), subscription);
		} else {
			if (getIdToSubscription().containsKey(subscription.getIdElement().getIdPart())) {
				ourLog.info("Removing {} subscription {}", statusString, subscription.getIdElement().toUnqualified().getValue());
			}
			getIdToSubscription().remove(subscription.getIdElement().getIdPart());
		}
	}

	private void handleCreate(ResourceModifiedMessage theMsg) {
		if (!theMsg.getId().getResourceType().equals("Subscription")) {
			return;
		}

		boolean subscriptionTypeApplies = subscriptionTypeApplies(theMsg);
		if (subscriptionTypeApplies == false) {
			return;
		}

		activateAndRegisterSubscriptionIfRequired(theMsg);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {

		if (!(theMessage.getPayload() instanceof ResourceModifiedMessage)) {
			return;
		}

		ResourceModifiedMessage msg = (ResourceModifiedMessage) theMessage.getPayload();
		IIdType id = msg.getId();

		switch (msg.getOperationType()) {
			case DELETE:
				getIdToSubscription().remove(id.getIdPart());
				return;
			case CREATE:
				handleCreate(msg);
				break;
			case UPDATE:
				handleUpdate(msg);
				break;
		}

	}

	private void handleUpdate(ResourceModifiedMessage theMsg) {
		if (!theMsg.getId().getResourceType().equals("Subscription")) {
			return;
		}

		boolean subscriptionTypeApplies = subscriptionTypeApplies(theMsg);
		if (subscriptionTypeApplies == false) {
			return;
		}

		activateAndRegisterSubscriptionIfRequired(theMsg);
	}
}
