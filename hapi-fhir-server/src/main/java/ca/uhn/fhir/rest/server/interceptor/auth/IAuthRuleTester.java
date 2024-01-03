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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

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
		@Nonnull
		public final PolicyEnum mode;
		/**
		 * The FHIR operation being performed.
		 * Note that this is not necessarily the same as the value obtained from invoking
		 * {@link RequestDetails#getRestOperationType()} on {@literal requestDetails}
		 * because multiple operations can be nested within
		 * an HTTP request using FHIR transaction and batch operations
		 */
		@Nonnull
		public final RestOperationTypeEnum operation;

		@Nonnull
		public final RequestDetails requestDetails;

		@Nullable
		public final IIdType resourceId;

		@Nullable
		public final IBaseResource resource;
		/** supplier for support services */
		@Nonnull
		public final IRuleApplier ruleApplier;

		public RuleTestRequest(
				PolicyEnum theMode,
				@Nonnull RestOperationTypeEnum theOperation,
				@Nonnull RequestDetails theRequestDetails,
				@Nullable IIdType theResourceId,
				@Nullable IBaseResource theResource,
				@Nonnull IRuleApplier theRuleApplier) {
			Validate.notNull(theMode);
			Validate.notNull(theOperation);
			Validate.notNull(theRequestDetails);
			Validate.notNull(theRuleApplier);
			mode = theMode;
			operation = theOperation;
			requestDetails = theRequestDetails;
			resource = theResource;
			if (theResourceId == null && resource != null) {
				resourceId = resource.getIdElement();
			} else {
				resourceId = theResourceId;
			}
			ruleApplier = theRuleApplier;
		}
	}

	/**
	 * User supplied logic called just before the parent rule renders a verdict on the operation
	 * or input resource.
	 *
	 * Returning true will allow the verdict continue.
	 * Returning false will block the verdict and cause the rule to abstain (i.e. return null).
	 *
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theRequest The details of the operation or an INPUT resource to evaluate
	 * @return true if the verdict should continue
	 * @since 6.1.0
	 */
	default boolean matches(RuleTestRequest theRequest) {
		return this.matches(
				theRequest.operation, theRequest.requestDetails, theRequest.resourceId, theRequest.resource);
	}

	/**
	 * DO NOT IMPLEMENT - Old api.  {@link #matches(RuleTestRequest)} instead.
	 * @deprecated
	 */
	@Deprecated(since = "6.1.0")
	default boolean matches(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IIdType theInputResourceId,
			IBaseResource theInputResource) {
		return true;
	}

	/**
	 * User supplied logic called just before the parent rule renders a verdict on an output resource.
	 *
	 * Returning true will allow the verdict continue.
	 * Returning false will block the verdict and cause the rule to abstain (i.e. return null).
	 *
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theRequest The details of the operation or an INPUT resource to evaluate
	 * @return true if the verdict should continue
	 * @since 6.1.0	 */
	default boolean matchesOutput(RuleTestRequest theRequest) {
		return this.matchesOutput(theRequest.operation, theRequest.requestDetails, theRequest.resource);
	}

	/**
	 * DO NOT IMPLEMENT - Old api.  {@link #matches(RuleTestRequest)} instead.
	 * @deprecated
	 */
	@Deprecated(since = "6.1.0")
	default boolean matchesOutput(
			RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theOutputResource) {
		return true;
	}
}
