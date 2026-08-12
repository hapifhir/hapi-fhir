/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class SubscriptionInterceptors {

	// START SNIPPET: validatingInterceptor
	@Interceptor
	public class MySubscriptionValidatingInterceptor extends SubscriptionValidatingInterceptor {

		@Override
		protected boolean isUserAuthorizedToWriteSubscriptions(
				@Nonnull IBaseResource theSubscription,
				@Nullable RequestDetails theRequestDetails,
				@Nullable RequestPartitionId theRequestPartitionId,
				@Nonnull Pointcut thePointcut) {
			// Custom authorization logic here
			// return true to allow the request, false to deny

			return true;
		}
	}
	// END SNIPPET: validatingInterceptor
}
