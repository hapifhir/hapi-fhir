/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderOperationNamedAndScoped {

	/**
	 * Responses for this operation will not be checked
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponses();

	/**
	 * @deprecated This is a synonym for {@link #andAllowAllResponses()}, use that method instead
	 */
	@Deprecated(since = "7.6.0")
	IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponsesWithAllResourcesAccess();

	/**
	 * Responses for this operation must be authorized by other rules. For example, if this
	 * rule is authorizing the Patient $everything operation, there must be a separate
	 * rule (or rules) that actually authorize the user to read the
	 * resources being returned
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andRequireExplicitResponseAuthorization();
}
