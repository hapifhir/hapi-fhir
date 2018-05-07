package ca.uhn.fhir.jpa.subscription.websocket;

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

import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class SubscriptionWebsocketInterceptor extends BaseSubscriptionInterceptor {

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Override
	public Subscription.SubscriptionChannelType getChannelType() {
		return Subscription.SubscriptionChannelType.WEBSOCKET;
	}

	@Override
	protected void registerDeliverySubscriber() {
		/*
		 * nothing, since individual websocket connections
		 * register themselves
		 */
	}

	@Override
	protected void unregisterDeliverySubscriber() {

		/*
		 * nothing, since individual websocket connections
		 * register themselves
		 */

	}
}
