/*-
 * #%L
 * hapi-fhir-storage-mdm
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.mdm.interceptor.MdmSearchExpandingInterceptor;
import ca.uhn.fhir.mdm.log.Logs;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.dstu2.model.Subscription;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmSubmitterInterceptorLoader {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	SubscriptionSettings mySubscriptionSettings;

	@Autowired(required = false)
	private IMdmStorageInterceptor myMdmStorageInterceptor;

	@Autowired
	private MdmSearchExpandingInterceptor myMdmSearchExpandingInterceptorInterceptor;

	@Autowired
	private IInterceptorService myInterceptorService;

	@PostConstruct
	public void loadInterceptors() {
		if (!myMdmSettings.isEnabled()) {
			return;
		}

		if (!mySubscriptionSettings
				.getSupportedSubscriptionTypes()
				.contains(Subscription.SubscriptionChannelType.MESSAGE)) {
			throw new ConfigurationException(
					Msg.code(2421) + "MDM requires Message Subscriptions to be enabled in the Storage Settings");
		}
		if (myMdmStorageInterceptor != null) {
			myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
		}
		myInterceptorService.registerInterceptor(myMdmSearchExpandingInterceptorInterceptor);
		ourLog.info("MDM interceptors registered");
	}
}
