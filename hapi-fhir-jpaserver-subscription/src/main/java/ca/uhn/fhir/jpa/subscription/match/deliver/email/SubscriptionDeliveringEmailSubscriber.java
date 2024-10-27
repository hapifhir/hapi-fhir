/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.match.deliver.email;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.deliver.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class SubscriptionDeliveringEmailSubscriber extends BaseSubscriptionDeliverySubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringEmailSubscriber.class);

	@Autowired
	private SubscriptionSettings mySubscriptionSettings;

	@Autowired
	private FhirContext myCtx;

	private IEmailSender myEmailSender;

	@Autowired
	public SubscriptionDeliveringEmailSubscriber(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws Exception {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// The Subscription.endpoint is treated as the email "to"
		String endpointUrl = subscription.getEndpointUrl();
		List<String> destinationAddresses = new ArrayList<>();
		String[] destinationAddressStrings = StringUtils.split(endpointUrl, ",");
		for (String next : destinationAddressStrings) {
			next = processEmailAddressUri(next);
			if (isNotBlank(next)) {
				destinationAddresses.add(next);
			}
		}

		String payload = "";
		if (isNotBlank(subscription.getPayloadString())) {
			EncodingEnum encoding = EncodingEnum.forContentType(subscription.getPayloadString());
			if (encoding != null) {
				payload = getPayloadStringFromMessageOrEmptyString(theMessage);
			}
		}

		String from = processEmailAddressUri(
				defaultString(subscription.getEmailDetails().getFrom(), mySubscriptionSettings.getEmailFromAddress()));
		String subjectTemplate =
				defaultString(subscription.getEmailDetails().getSubjectTemplate(), provideDefaultSubjectTemplate());

		EmailDetails details = new EmailDetails();
		details.setTo(destinationAddresses);
		details.setFrom(from);
		details.setBodyTemplate(payload);
		details.setSubjectTemplate(subjectTemplate);
		details.setSubscription(subscription.getIdElement(myFhirContext));

		myEmailSender.send(details);
	}

	private String processEmailAddressUri(String next) {
		next = trim(defaultString(next));
		if (next.startsWith("mailto:")) {
			next = next.substring("mailto:".length());
		}
		return next;
	}

	private String provideDefaultSubjectTemplate() {
		return "HAPI FHIR Subscriptions";
	}

	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}

	@VisibleForTesting
	public IEmailSender getEmailSender() {
		return myEmailSender;
	}

	/**
	 * Get the payload string, fetch it from the DB when the payload is null.
	 */
	private String getPayloadStringFromMessageOrEmptyString(ResourceDeliveryMessage theMessage) {
		String payload = theMessage.getPayloadString();

		if (theMessage.getPayload(myCtx) != null) {
			return payload;
		}

		Optional<ResourceModifiedMessage> inflatedMessage =
				inflateResourceModifiedMessageFromDeliveryMessage(theMessage);
		if (inflatedMessage.isEmpty()) {
			return "";
		}

		payload = inflatedMessage.get().getPayloadString();
		return payload;
	}
}
