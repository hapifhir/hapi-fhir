package ca.uhn.fhir.jpa.empi.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class EmpiLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLoader.class);

	@Autowired
	IEmpiSettings myEmpiProperties;
	@Autowired
	EmpiProviderLoader myEmpiProviderLoader;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Autowired
	EmpiSearchParameterLoader myEmpiSearchParameterLoader;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the EMPI subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void updateSubscriptions() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myEmpiProviderLoader.loadProvider();
		ourLog.info("EMPI provider registered");

		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
		ourLog.info("EMPI subscriptions updated");

		myEmpiSearchParameterLoader.daoUpdateEmpiSearchParameters();
		ourLog.info("EMPI search parameters updated");
	}
}
