package ca.uhn.fhir.jpa.subscription.email;

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
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.ResourceDeliveryMessage;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

public class SubscriptionDeliveringEmailSubscriber extends BaseSubscriptionDeliverySubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringEmailSubscriber.class);

	private SubscriptionEmailInterceptor mySubscriptionEmailInterceptor;

	public SubscriptionDeliveringEmailSubscriber(IFhirResourceDao<?> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, SubscriptionEmailInterceptor theSubscriptionEmailInterceptor) {
		super(theSubscriptionDao, theChannelType, theSubscriptionEmailInterceptor);

		mySubscriptionEmailInterceptor = theSubscriptionEmailInterceptor;
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws Exception {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// The Subscription.endpoint is treated as the email "to"
		String endpointUrl = subscription.getEndpointUrl();
		List<String> destinationAddresses = new ArrayList<>();
		String[] destinationAddressStrings = StringUtils.split(endpointUrl, ",");
		for (String next : destinationAddressStrings) {
			if (isNotBlank(next)) {
				destinationAddresses.add(trim(next));
			}
		}

		String from = defaultString(subscription.getEmailDetails().getFrom(), provideDefaultFrom());
		String subjectTemplate = defaultString(subscription.getEmailDetails().getSubjectTemplate(), provideDefaultSubjectTemplate());
		String bodyTemplate = defaultString(subscription.getEmailDetails().getBodyTemplate(), provideDefaultBodyTemplate());

		EmailDetails details = new EmailDetails();
		details.setTo(destinationAddresses);
		details.setFrom(from);
		details.setBodyTemplate(bodyTemplate);
		details.setSubjectTemplate(subjectTemplate);

		IEmailSender emailSender = mySubscriptionEmailInterceptor.getEmailSender();
		emailSender.send(details);
	}

	private String provideDefaultBodyTemplate() {
		return "A subscription update has been received";
	}

	private String provideDefaultFrom() {
		return "unknown@sender.com";
	}

	private String provideDefaultSubjectTemplate() {
		return "HAPI FHIR Subscriptions";
	}
}
