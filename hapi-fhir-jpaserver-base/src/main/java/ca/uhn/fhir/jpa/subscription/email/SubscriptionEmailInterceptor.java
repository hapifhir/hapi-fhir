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
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import java.util.List;

public class SubscriptionEmailInterceptor extends BaseSubscriptionInterceptor {
	private SubscriptionDeliveringEmailSubscriber mySubscriptionDeliverySubscriber;
	private IEmailSender myEmailSender;

	@Override
	public org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType getChannelType() {
		return org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.EMAIL;
	}

	public IEmailSender getEmailSender() {
		return myEmailSender;
	}

	@Required
	public void setEmailSender(IEmailSender theEmailSender) {
		myEmailSender = theEmailSender;
	}

	@Override
	protected void registerDeliverySubscriber() {
		if (mySubscriptionDeliverySubscriber == null) {
			mySubscriptionDeliverySubscriber = new SubscriptionDeliveringEmailSubscriber(getSubscriptionDao(), getChannelType(), this);
		}
		getDeliveryChannel().subscribe(mySubscriptionDeliverySubscriber);
	}

	@PostConstruct
	public void start() {
		Validate.notNull(myEmailSender, "emailSender has not been configured");

		super.start();
	}

	@Override
	protected void unregisterDeliverySubscriber() {
		getDeliveryChannel().unsubscribe(mySubscriptionDeliverySubscriber);
	}
}
