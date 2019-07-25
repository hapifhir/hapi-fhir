package ca.uhn.fhir.jpa.subscription.email;

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

import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

import java.util.Optional;

public class SubscriptionEmailInterceptor extends BaseSubscriptionInterceptor {

	/**
	 * This is set to autowired=false just so that implementors can supply this
	 * with a mechanism other than autowiring if they want
	 */
	@Autowired(required = false)
	private IEmailSender myEmailSender;
	private String myDefaultFromAddress = "noreply@unknown.com";

	@Override
	protected Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription) {
		return Optional.of(new SubscriptionDeliveringEmailSubscriber(getSubscriptionDao(), getChannelType(), this));
	}

	@Override
	public org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType getChannelType() {
		return org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.EMAIL;
	}

	/**
	 * The "from" address to use for any sent emails that to not explicitly specity a from address
	 */
	public String getDefaultFromAddress() {
		return myDefaultFromAddress;
	}

	/**
	 * The "from" address to use for any sent emails that to not explicitly specity a from address
	 */
	public void setDefaultFromAddress(String theDefaultFromAddress) {
		Validate.notBlank(theDefaultFromAddress, "theDefaultFromAddress must not be null or blank");
		myDefaultFromAddress = theDefaultFromAddress;
	}

	public IEmailSender getEmailSender() {
		return myEmailSender;
	}

	/**
	 * Set the email sender (this method does not need to be explicitly called if you
	 * are using autowiring to supply the sender)
	 */
	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}


}
