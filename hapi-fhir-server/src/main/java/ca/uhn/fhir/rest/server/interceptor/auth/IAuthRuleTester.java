package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows user-supplied logic for authorization rules.
 * <p>
 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
 * may change.
 *
 * @since 3.4.0
 */
public interface IAuthRuleTester {
	/**
	 * A request object to make this easier to extend.
	 */
	class RuleTestRequest {
		// fake record pattern
		/** the mode of the calling rule context */
		@Nonnull public final PolicyEnum mode;
		/**
		 * The FHIR operation being performed.
		 * Note that this is not necessarily the same as the value obtained from invoking
		 * {@link RequestDetails#getRestOperationType()} on {@literal requestDetails}
		 * because multiple operations can be nested within
		 * an HTTP request using FHIR transaction and batch operations
		 */
		@Nonnull public final RestOperationTypeEnum operation;
		@Nonnull public final RequestDetails requestDetails;
		@Nullable public final IIdType resourceId;
		@Nullable public final IBaseResource resource;
		/** supplier for support services */
		@Nonnull public final IRuleApplier ruleApplier;

		public RuleTestRequest(PolicyEnum theMode, @Nonnull RestOperationTypeEnum theOperation, @Nonnull RequestDetails theRequestDetails, @Nullable IIdType theResourceId, @Nullable IBaseResource theResource, @Nonnull IRuleApplier theRuleApplier) {
			Validate.notNull(theMode);
			Validate.notNull(theOperation);
			Validate.notNull(theRequestDetails);
			mode = theMode;
			operation = theOperation;
			requestDetails = theRequestDetails;
			resourceId = theResourceId;
			resource = theResource;
			ruleApplier = theRuleApplier;
		}
	}

	/**
	 * Allows user-supplied logic for authorization rules.
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theRequest The details to evaluate
	 * @since 6.1.0
	 */
	default boolean matches(RuleTestRequest theRequest) {
		return this.matches(theRequest.operation, theRequest.requestDetails, theRequest.resourceId, theRequest.resource);
	}


	/**
	 * DO NOT IMPLEMENT - Old api.  {@link #matches(RuleTestRequest)} instead.
	 * @deprecated
	 */
	@Deprecated(since = "6.1.0")
	default boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
		return true;
	}

	/**
	 * Allows user-supplied logic for authorization rules.
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theRequest The details to evaluate
	 * @since 6.1.0
	 */
	default boolean matchesOutput(RuleTestRequest theRequest) {
		return this.matchesOutput(theRequest.operation, theRequest.requestDetails, theRequest.resource);
	}

	/**
	 * DO NOT IMPLEMENT - Old api.  {@link #matches(RuleTestRequest)} instead.
	 * @deprecated
	 */
	@Deprecated(since = "6.1.0")
	default boolean matchesOutput(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theOutputResource) {
		return true;
	}

}
