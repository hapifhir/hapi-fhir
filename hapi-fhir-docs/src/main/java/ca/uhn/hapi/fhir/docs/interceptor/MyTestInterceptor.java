package ca.uhn.hapi.fhir.docs.interceptor;

/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;

/**
 * Interceptor class
 */
@Interceptor
public class MyTestInterceptor {

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY)
	public boolean beforeRestHookDelivery(ResourceDeliveryMessage theDeliveryMessage, CanonicalSubscription theSubscription) {

		String header = "Authorization: Bearer 1234567";

		theSubscription.addHeader(header);

		return true;
	}

}
