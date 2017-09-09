package ca.uhn.fhir.jpa.subscription.r4;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionWebsocketInterceptor;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

public class WebSocketSubscriptionR4Interceptor extends BaseSubscriptionWebsocketInterceptor {

	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	private IFhirResourceDao<org.hl7.fhir.r4.model.Subscription> mySubscriptionDao;

	@Override
	protected CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		return RestHookSubscriptionR4Interceptor.doCanonicalize(theSubscription);
	}

	@Override
	public Subscription.SubscriptionChannelType getChannelType() {
		return Subscription.SubscriptionChannelType.WEBSOCKET;
	}

	@Override
	protected IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}
}
