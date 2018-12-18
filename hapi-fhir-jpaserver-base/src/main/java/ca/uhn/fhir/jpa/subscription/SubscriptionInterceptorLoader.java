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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SubscriptionInterceptorLoader {
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	public void registerInterceptors() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (!supportedSubscriptionTypes.isEmpty()) {
			myDaoConfig.registerInterceptor(mySubscriptionActivatingInterceptor);
			myDaoConfig.registerInterceptor(mySubscriptionMatcherInterceptor);
		}
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myDaoConfig.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myDaoConfig.unregisterInterceptor(mySubscriptionMatcherInterceptor);
	}
}
