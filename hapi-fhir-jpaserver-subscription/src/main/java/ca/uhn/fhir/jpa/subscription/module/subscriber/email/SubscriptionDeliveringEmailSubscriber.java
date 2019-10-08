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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

@Component
@Scope("prototype")
public class SubscriptionDeliveringEmailSubscriber extends BaseSubscriptionDeliverySubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringEmailSubscriber.class);

	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

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

		EmailDetails details = new EmailDetails();

		if (isNotBlank(subscription.getPayloadString())) {
			String[] contentTypeSplit = subscription.getPayloadString().split(";");
			String bodyText = null;
			Boolean bodyNarrative = false;
			Boolean attachResource = false;

			if (contentTypeSplit.length == 2) {
				// Custom mime-type parameters have been specified. Read them.
				String[] propertiesSplit = contentTypeSplit[1].split(",");

				for (String property : propertiesSplit) {
					if (property.indexOf("=") > 0) {
						String propertyName = property.substring(0, property.indexOf("="));
						String propertyValue = property.substring(property.indexOf("=") + 1);

						switch (propertyName.toLowerCase()) {
							case "bodynarrative":
								bodyNarrative = Boolean.parseBoolean(propertyValue);
								break;
							case "bodytext":
								bodyText = propertyValue;
								break;
						}
					}
				}
			}

			// Set the body of the email to the narrative/text of the resource. This takes priority over customized bodyText
			if (bodyNarrative) {
				IDomainResource domainResource = (IDomainResource) theMessage.getResource();

				if (domainResource != null && domainResource.getText() != null) {
					details.setBodyTemplate(domainResource.getText().getDivAsString());
					details.setBodyContentType("text/html");
					attachResource = true;
				}
			}

			// Set the body of the email to specific text specified in the subscription, if the body of the email has not already
			// been set elsewhere (ex: by the subscriber asking for the narrative to be put in the body, assuming there *is* narrative)
			if (bodyText != null && !bodyText.isEmpty() && (details.getBodyTemplate() == null || details.getBodyTemplate().isEmpty())) {
				byte[] bodyBytes = Base64.decodeBase64(bodyText);
				details.setBodyTemplate(new String(bodyBytes));
				details.setBodyContentType("text/plain");
				attachResource = true;
			}

			// Make the resource an attachment on the email instead of the body of the email. The body of the email
			// has been set to something else.
			if (attachResource) {
				BodyPart attachmentPart = new MimeBodyPart();

				if (theMessage.getResourceEncoding() == EncodingEnum.XML) {
					attachmentPart.setContent(theMessage.getResourceString(), "application/xml");
					attachmentPart.setFileName("resource.xml");
				} else if (theMessage.getResourceEncoding() == EncodingEnum.JSON) {
					attachmentPart.setContent(theMessage.getResourceString(), "application/json");
					attachmentPart.setFileName("resource.json");
				}

				if (attachmentPart.getFileName() != null && !attachmentPart.getFileName().isEmpty()) {
					details.getAttachments().add(attachmentPart);
				}
			}

			// If we got to this point and we still don't have any body specified for the email, use generic/default
			// behavior of attaching the resource to the body of the email
			if (details.getBodyTemplate() == null || details.getBodyTemplate().isEmpty()) {
				details.setBodyTemplate(theMessage.getResourceString());

				if (theMessage.getResourceEncoding() == EncodingEnum.XML) {
					details.setBodyContentType("application/xml");
				} else if (theMessage.getResourceEncoding() == EncodingEnum.JSON) {
					details.setBodyContentType("application/json");
				}
			}
		}

		String from = processEmailAddressUri(defaultString(subscription.getEmailDetails().getFrom(), myModelConfig.getEmailFromAddress()));
		String subjectTemplate = defaultString(subscription.getEmailDetails().getSubjectTemplate(), provideDefaultSubjectTemplate());

		details.setTo(destinationAddresses);
		details.setFrom(from);
		details.setSubjectTemplate(subjectTemplate);
		details.setSubscription(subscription.getIdElement(myFhirContext));

		// Interceptor call: SUBSCRIPTION_BEFORE_EMAIL_DELIVERY
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(EmailDetails.class, details);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_BEFORE_EMAIL_DELIVERY, params)) {
			return;
		}

		myEmailSender.send(details);

		// Interceptor call: SUBSCRIPTION_AFTER_EMAIL_DELIVERY
		params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(EmailDetails.class, details);
		myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_EMAIL_DELIVERY, params);
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
