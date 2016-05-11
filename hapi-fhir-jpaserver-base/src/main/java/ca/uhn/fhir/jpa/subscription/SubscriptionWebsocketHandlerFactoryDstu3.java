package ca.uhn.fhir.jpa.subscription;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.springframework.beans.factory.FactoryBean;

public class SubscriptionWebsocketHandlerFactoryDstu3 implements FactoryBean<ISubscriptionWebsocketHandler> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionWebsocketHandlerDstu3.class);

	@Override
	public ISubscriptionWebsocketHandler getObject() throws Exception {
		return new SubscriptionWebsocketHandlerDstu3();
	}

	@Override
	public Class<ISubscriptionWebsocketHandler> getObjectType() {
		return ISubscriptionWebsocketHandler.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
