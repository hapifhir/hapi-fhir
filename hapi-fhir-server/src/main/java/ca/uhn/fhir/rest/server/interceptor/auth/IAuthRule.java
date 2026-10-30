/*
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

/**
 * Note: At this time, this interface is considered internal API to HAPI FHIR,
 * and is subject to change without warning. Create your own implementations at
 * your own risk. If you have use cases that are not met by the current
 * implementation, please consider raising them on the HAPI FHIR
 * Google Group.
 */
public interface IAuthRule {

	/**
	 * This is the first phase of the authorization rule application for HTTP client requests.
	 * It is invoked very early in the HTTP request processing lifecycle, before the request
	 * payload is examined or any significant amount of work has been performed by the server.
	 * The primary purpose is to allow an early abort for requests that are clearly not
	 * going to be authorized, in order to prevent unnecessary work being performed by the
	 * server.
	 * <p>
	 * Returning a positive verdict from this method ({@link PolicyEnum#ALLOW}) does not
	 * mean that the request will ultimately be allowed. It only means that the request
	 * will continue to be processed, and {@link #applyRule(RestOperationTypeEnum, RequestDetails, IBaseResource, IIdType, IBaseResource, IRuleApplier, Set, Pointcut)}
	 * will eventually be invoked to fully authorize the request.
	 * </p>
	 * <p>
	 * Returning <code>null</code> just means that the rule is declaring that it is not
	 * applicable to the current request. If all rules return <code>null</code>, then the
	 * request will not be allowed to proceed.
	 * </p>
	 * <p>
	 * A negative verdict generally does not make sense to be returned by this method.
	 * </p>
	 *
	 * @since 8.0.0
	 */
	Verdict preFlightCheck(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails);

	/**
	 * Applies the rule and returns a policy decision, or <code>null</code> if the rule does not apply
	 *
	 * @param theOperation       The operation type
	 * @param theRequestDetails  The request
	 * @param theInputResource   The resource being input by the client, or <code>null</code>
	 * @param theInputResourceId TODO
	 * @param theOutputResource  The resource being returned by the server, or <code>null</code>
	 * @param theRuleApplier     The rule applying module (this can be used by rules to apply the rule set to
	 *                           nested objects in the request, such as nested requests in a transaction)
	 * @param theFlags           The flags configured in the authorization interceptor
	 * @param thePointcut        The pointcut hook that triggered this call
	 * @return Returns a policy decision, or <code>null</code> if the rule does not apply
	 */
	Verdict applyRule(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier,
			Set<AuthorizationFlagsEnum> theFlags,
			Pointcut thePointcut);

	/**
	 * Returns a name for this rule, to be used in logs and error messages
	 */
	String getName();
}
