package ca.uhn.fhir.jpa.subscription.module.subscriber.email;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

@Component
@Scope("prototype")
public class SubscriptionDeliveringEmailSubscriber extends BaseSubscriptionDeliverySubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringEmailSubscriber.class);

	@Autowired
	private ModelConfig myModelConfig;

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

		String from = processEmailAddressUri(defaultString(subscription.getEmailDetails().getFrom(), myModelConfig.getEmailFromAddress()));
		String subjectTemplate = defaultString(subscription.getEmailDetails().getSubjectTemplate(), provideDefaultSubjectTemplate());

		EmailDetails details = new EmailDetails();
		details.setTo(destinationAddresses);
		details.setFrom(from);
		details.setBodyTemplate(subscription.getPayloadString());
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
}
