package ca.uhn.fhir.jpa.config.r4;

/*
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

import javax.annotation.PostConstruct;

import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.subscription.r4.SubscriptionWebsocketHandlerR4;

@Configuration
public class WebsocketR4DispatcherConfig {
	
	@Autowired
	private FhirContext myCtx;

	@Autowired
	private IFhirResourceDao<Subscription> mySubscriptionDao;

	@PostConstruct
	public void postConstruct() {
		SubscriptionWebsocketHandlerR4.setCtx(myCtx);
		SubscriptionWebsocketHandlerR4.setSubscriptionDao((IFhirResourceDaoSubscription<Subscription>) mySubscriptionDao);
	}
	
}
