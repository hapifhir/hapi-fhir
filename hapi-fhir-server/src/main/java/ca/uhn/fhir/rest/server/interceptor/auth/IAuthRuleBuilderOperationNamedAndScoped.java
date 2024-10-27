/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderOperationNamedAndScoped {

	/**
	 * Responses for this operation will not be checked
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponses();

	/**
	 * Responses for this operation will not be checked and access to all resources is allowed. This
	 * is intended for operations which are known to fetch a graph of resources that is known to be
	 * safe, such as `$everything` which may access and fetch resources outside the patient's compartment
	 * but enforces safety in what it fetches via strict SQL queries.
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponsesWithAllResourcesAccess();

	/**
	 * Responses for this operation must be authorized by other rules. For example, if this
	 * rule is authorizing the Patient $everything operation, there must be a separate
	 * rule (or rules) that actually authorize the user to read the
	 * resources being returned
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andRequireExplicitResponseAuthorization();
}
