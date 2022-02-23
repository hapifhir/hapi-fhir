package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
	Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut);

	/**
	 * Returns a name for this rule, to be used in logs and error messages
	 */
	String getName();

}
