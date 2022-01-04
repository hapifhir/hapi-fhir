package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseSubscriberForSubscriptionResources implements MessageHandler {

	@Autowired
	protected FhirContext myFhirContext;

	protected boolean isSubscription(ResourceModifiedMessage theNewResource) {
		String payloadIdType = null;
		IIdType payloadId = theNewResource.getPayloadId(myFhirContext);
		if (payloadId != null) {
			payloadIdType = payloadId.getResourceType();
		}
		if (isBlank(payloadIdType)) {
			IBaseResource payload = theNewResource.getNewPayload(myFhirContext);
			if (payload != null) {
				payloadIdType = myFhirContext.getResourceType(payload);
			}
		}

		return ResourceTypeEnum.SUBSCRIPTION.getCode().equals(payloadIdType);
	}

}
