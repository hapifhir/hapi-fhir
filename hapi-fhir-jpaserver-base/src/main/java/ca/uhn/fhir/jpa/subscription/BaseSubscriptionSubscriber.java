package ca.uhn.fhir.jpa.subscription;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.messaging.MessageHandler;

public abstract class BaseSubscriptionSubscriber implements MessageHandler {

	private final IFhirResourceDao<?> mySubscriptionDao;
	private final Subscription.SubscriptionChannelType myChannelType;
	private final BaseSubscriptionInterceptor mySubscriptionInterceptor;

	/**
	 * Constructor
	 */
	public BaseSubscriptionSubscriber(IFhirResourceDao<?> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		mySubscriptionDao = theSubscriptionDao;
		myChannelType = theChannelType;
		mySubscriptionInterceptor = theSubscriptionInterceptor;
	}

	public Subscription.SubscriptionChannelType getChannelType() {
		return myChannelType;
	}

	public FhirContext getContext() {
		return getSubscriptionDao().getContext();
	}

	public IFhirResourceDao getSubscriptionDao() {
		return mySubscriptionDao;
	}

	public BaseSubscriptionInterceptor getSubscriptionInterceptor() {
		return mySubscriptionInterceptor;
	}


	/**
	 * Does this subscription type (e.g. rest hook, websocket, etc) apply to this interceptor?
	 */
	protected boolean subscriptionTypeApplies(FhirContext theCtx, IBaseResource theSubscription) {
		Subscription.SubscriptionChannelType channelType = getChannelType();
		return subscriptionTypeApplies(theCtx, theSubscription, channelType);
	}

	/**
	 * Does this subscription type (e.g. rest hook, websocket, etc) apply to this interceptor?
	 */
	static boolean subscriptionTypeApplies(FhirContext theCtx, IBaseResource theSubscription, Subscription.SubscriptionChannelType theChannelType) {
		IPrimitiveType<?> subscriptionType = theCtx.newTerser().getSingleValueOrNull(theSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_TYPE, IPrimitiveType.class);
		boolean subscriptionTypeApplies = false;
 		if (subscriptionType != null) {
			if (theChannelType.toCode().equals(subscriptionType.getValueAsString())) {
				subscriptionTypeApplies = true;
			}
		}
		return subscriptionTypeApplies;
	}

}
