package ca.uhn.fhir.jpa.subscription;

import java.util.List;

import ca.uhn.fhir.jpa.service.MatchUrlService;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.subscription.matcher.ISubscriptionMatcher;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Scope("prototype")
public class SubscriptionCheckingSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionCheckingSubscriber.class);

	private final ISubscriptionMatcher mySubscriptionMatcher;

	@Autowired
	private MatchUrlService myMatchUrlService;

	public SubscriptionCheckingSubscriber(Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor, ISubscriptionMatcher theSubscriptionMatcher) {
		super(theChannelType, theSubscriptionInterceptor);
		this.mySubscriptionMatcher = theSubscriptionMatcher;
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		switch (msg.getOperationType()) {
			case CREATE:
			case UPDATE:
			case MANUALLY_TRIGGERED:
				break;
			case DELETE:
			default:
				ourLog.trace("Not processing modified message for {}", msg.getOperationType());
				// ignore anything else
				return;
		}

		IIdType id = msg.getId(getContext());
		String resourceType = id.getResourceType();
		String resourceId = id.getIdPart();

		List<CanonicalSubscription> subscriptions = getSubscriptionInterceptor().getRegisteredSubscriptions();

		ourLog.trace("Testing {} subscriptions for applicability");

		for (CanonicalSubscription nextSubscription : subscriptions) {

			String nextSubscriptionId = nextSubscription.getIdElement(getContext()).toUnqualifiedVersionless().getValue();
			String nextCriteriaString = nextSubscription.getCriteriaString();

			if (isNotBlank(msg.getSubscriptionId())) {
				if (!msg.getSubscriptionId().equals(nextSubscriptionId)) {
					ourLog.debug("Ignoring subscription {} because it is not {}", nextSubscriptionId, msg.getSubscriptionId());
					continue;
				}
			}

			if (StringUtils.isBlank(nextCriteriaString)) {
				continue;
			}

			// see if the criteria matches the created object
			ourLog.trace("Checking subscription {} for {} with criteria {}", nextSubscriptionId, resourceType, nextCriteriaString);
			String criteriaResource = nextCriteriaString;
			int index = criteriaResource.indexOf("?");
			if (index != -1) {
				criteriaResource = criteriaResource.substring(0, criteriaResource.indexOf("?"));
			}

			if (resourceType != null && nextCriteriaString != null && !criteriaResource.equals(resourceType)) {
				ourLog.trace("Skipping subscription search for {} because it does not match the criteria {}", resourceType, nextCriteriaString);
				continue;
			}

			if (!mySubscriptionMatcher.match(nextCriteriaString, msg)) {
				continue;
			}

			ourLog.debug("Found match: queueing rest-hook notification for resource: {}", id.toUnqualifiedVersionless().getValue());

			ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
			deliveryMsg.setPayload(getContext(), msg.getNewPayload(getContext()));
			deliveryMsg.setSubscription(nextSubscription);
			deliveryMsg.setOperationType(msg.getOperationType());
			deliveryMsg.setPayloadId(msg.getId(getContext()));

			ResourceDeliveryJsonMessage wrappedMsg = new ResourceDeliveryJsonMessage(deliveryMsg);
			MessageChannel deliveryChannel = getSubscriptionInterceptor().getDeliveryChannel(nextSubscription);
			if (deliveryChannel != null) {
				deliveryChannel.send(wrappedMsg);
			} else {
				ourLog.warn("Do not have deliovery channel for subscription {}", nextSubscription.getIdElement(getContext()));
			}
		}


	}

	/**
	 * Subclasses may override
	 */
	protected String massageCriteria(String theCriteria) {
		return theCriteria;
	}

	/**
	 * Search based on a query criteria
	 */
	protected IBundleProvider performSearch(String theCriteria) {
		RuntimeResourceDefinition responseResourceDef = getSubscriptionDao().validateCriteriaAndReturnResourceDefinition(theCriteria);
		SearchParameterMap responseCriteriaUrl = myMatchUrlService.translateMatchUrl(getSubscriptionDao(), getSubscriptionDao().getContext(), theCriteria, responseResourceDef);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IFhirResourceDao<? extends IBaseResource> responseDao = getSubscriptionInterceptor().getDao(responseResourceDef.getImplementingClass());
		responseCriteriaUrl.setLoadSynchronousUpTo(1);

		IBundleProvider responseResults = responseDao.search(responseCriteriaUrl, req);
		return responseResults;
	}

}
