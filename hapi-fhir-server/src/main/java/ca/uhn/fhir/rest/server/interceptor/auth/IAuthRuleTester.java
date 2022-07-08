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
	 * Allows user-supplied logic for authorization rules.
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theOperation The FHIR operation being performed - Note that this is not necessarily the same as the value obtained from invoking
	 *                     {@link RequestDetails#getRestOperationType()} on {@literal theRequestDetails} because multiple operations can be nested within
	 *                     an HTTP request using FHIR transaction and batch operations
	 * @since 3.4.0
	 */
	default boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
		return true;
	}

	/**
	 * Allows user-supplied logic for authorization rules.
	 * <p>
	 * THIS IS AN EXPERIMENTAL API! Feedback is welcome, and this API
	 * may change.
	 *
	 * @param theOperation The FHIR operation being performed - Note that this is not necessarily the same as the value obtained from invoking
	 *                     {@link RequestDetails#getRestOperationType()} on {@literal theRequestDetails} because multiple operations can be nested within
	 *                     an HTTP request using FHIR transaction and batch operations
	 * @since 5.0.0
	 */
	default boolean matchesOutput(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theOutputResource) {
		return true;
	}

}
