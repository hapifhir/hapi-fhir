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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionCheckingSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionCheckingSubscriber.class);

	public SubscriptionCheckingSubscriber(IFhirResourceDao theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theProcessingChannel);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceModifiedMessage)) {
			return;
		}

		ResourceModifiedMessage msg = (ResourceModifiedMessage) theMessage.getPayload();
		switch (msg.getOperationType()) {
			case CREATE:
			case UPDATE:
				break;
			default:
				// ignore anything else
				return;
		}

		String resourceType = msg.getId().getResourceType();
		String resourceId = msg.getId().getIdPart();

		for (IBaseResource nextSubscription : getIdToSubscription().values()) {

			String nextSubscriptionId = nextSubscription.getIdElement().toUnqualifiedVersionless().getValue();
			IPrimitiveType<?> nextCriteria = getContext().newTerser().getSingleValueOrNull(nextSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_CRITERIA, IPrimitiveType.class);
			String nextCriteriaString = nextCriteria != null ? nextCriteria.getValueAsString() : null;

			if (StringUtils.isBlank(nextCriteriaString)) {
				continue;
			}

			// see if the criteria matches the created object
			ourLog.info("Checking subscription {} for {} with criteria {}", nextSubscriptionId, resourceType, nextCriteriaString);

			String criteriaResource = nextCriteriaString;
			int index = criteriaResource.indexOf("?");
			if (index != -1) {
				criteriaResource = criteriaResource.substring(0, criteriaResource.indexOf("?"));
			}

			if (resourceType != null && nextCriteriaString != null && !criteriaResource.equals(resourceType)) {
				ourLog.info("Skipping subscription search for {} because it does not match the criteria {}", resourceType, nextCriteriaString);
				continue;
			}

			// run the subscriptions query and look for matches, add the id as part of the criteria to avoid getting matches of previous resources rather than the recent resource
			String criteria = nextCriteriaString;
			criteria += "&_id=" + resourceType + "/" + resourceId;
			criteria = massageCriteria(criteria);

			IBundleProvider results = performSearch(criteria);
			if (results.size() == 0) {
				continue;
			}

			// should just be one resource as it was filtered by the id
			for (IBaseResource nextBase : results.getResources(0, results.size())) {
				ourLog.info("Found match: queueing rest-hook notification for resource: {}", nextBase.getIdElement());

				ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
				deliveryMsg.setPayoad(nextBase);
				deliveryMsg.setSubscription(nextSubscription);
				deliveryMsg.setOperationType(msg.getOperationType());
				deliveryMsg.setPayloadId(msg.getId());

				getProcessingChannel().send(new GenericMessage<>(deliveryMsg));
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
		SearchParameterMap responseCriteriaUrl = BaseHapiFhirDao.translateMatchUrl(getSubscriptionDao(), getSubscriptionDao().getContext(), theCriteria, responseResourceDef);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IFhirResourceDao<? extends IBaseResource> responseDao = getSubscriptionDao().getDao(responseResourceDef.getImplementingClass());
		responseCriteriaUrl.setLoadSynchronousUpTo(1);

		IBundleProvider responseResults = responseDao.search(responseCriteriaUrl, req);
		return responseResults;
	}

}
