package ca.uhn.fhir.jpa.empi.interceptor;

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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionSubmitInterceptorLoader;
import org.hl7.fhir.dstu2.model.Subscription;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class EmpiSubmitterInterceptorLoader {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiSettings myEmpiProperties;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private IEmpiStorageInterceptor myIEmpiStorageInterceptor;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private SubscriptionSubmitInterceptorLoader mySubscriptionSubmitInterceptorLoader;

	@PostConstruct
	public void start() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.MESSAGE);
		myInterceptorService.registerInterceptor(myIEmpiStorageInterceptor);
		ourLog.info("EMPI interceptor registered");
		// We need to call SubscriptionSubmitInterceptorLoader.start() again in case there were no subscription types the first time it was called.
		mySubscriptionSubmitInterceptorLoader.start();
	}
}
