package ca.uhn.fhir.jpa.mdm.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class MdmLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLoader.class);

	@Autowired
	IMdmSettings myMdmSettings;
	@Autowired
	MdmProviderLoader myMdmProviderLoader;
	@Autowired
	MdmSubscriptionLoader myMdmSubscriptionLoader;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the MDM subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void updateSubscriptions() {
		if (!myMdmSettings.isEnabled()) {
			return;
		}

		myMdmProviderLoader.loadProvider();
		ourLog.info("MDM provider registered");

		myMdmSubscriptionLoader.daoUpdateMdmSubscriptions();
		ourLog.info("MDM subscriptions updated");
	}
}
