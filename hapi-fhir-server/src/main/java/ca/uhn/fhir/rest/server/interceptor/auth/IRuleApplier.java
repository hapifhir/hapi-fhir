/*
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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;

public interface IRuleApplier {

	@Nonnull
	Logger getTroubleshootingLog();

	Verdict applyRulesAndReturnDecision(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			Pointcut thePointcut);

	@Nullable
	IValidationSupport getValidationSupport();

	@Nullable
	default IAuthorizationSearchParamMatcher getSearchParamMatcher() {
		return null;
	}

	/**
	 * The auth resource resolve is a service that allows you to query the DB for a resource, given a resource ID
	 * WARNING: This is slow, and should have limited use in authorization.
	 *
	 * It is currently used for bulk-export, to support permissible Group/Patient exports by matching a FHIR query
	 * This is ok, since bulk-export is a slow and (relatively) rare operation
	 */
	default IAuthResourceResolver getAuthResourceResolver() {
		return null;
	}
}
