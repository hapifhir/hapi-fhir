/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.consent;

/**
 * Place to keep consent related constants.
 */
public class ConsentConstants {
	private ConsentConstants() {}

	/**
	 * User data key used for {@link ca.uhn.fhir.rest.api.server.RequestDetails}.
	 * When set to true, the request will bypass consent checking.
	 * Skipping consent is needed for resources that are modified in async system processing
	 * e.g. SearchParameter initialization with subscriptions and ResourceModifiedMessage enabled.
	 * This is a short term solution and is to be replaced by a long term solution.
	 * {@see https://gitlab.com/simpatico.ai/cdr/-/issues/8067}
	 */
	public static final String USER_DATA_SHOULD_SKIP_CONSENT_FOR_SYSTEM_OPERATIONS =
			"request_details_user_data_should_skip_consent";
}
