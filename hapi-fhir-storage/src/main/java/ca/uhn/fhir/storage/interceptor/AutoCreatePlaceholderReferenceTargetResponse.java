/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.storage.interceptor;

/**
 * This object is used as a return type for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE}
 * pointcut.
 *
 * @since 8.4.0
 * @see #doNotCreateTarget()
 * @see #proceed()
 */
public class AutoCreatePlaceholderReferenceTargetResponse {

	private boolean myDoNotCreateTarget;

	/**
	 * Use the static factory methods to create this object
	 */
	private AutoCreatePlaceholderReferenceTargetResponse(boolean theDoNotCreateTarget) {
		myDoNotCreateTarget = theDoNotCreateTarget;
	}

	/**
	 * Should the placeholder resource not be created?
	 */
	public boolean isDoNotCreateTarget() {
		return myDoNotCreateTarget;
	}

	/**
	 * Create response: The placeholder reference target should <b>not</b> be created.
	 * Under most circumstances this will cause the operation to be aborted due to
	 * the invalid reference.
	 */
	public static AutoCreatePlaceholderReferenceTargetResponse doNotCreateTarget() {
		return new AutoCreatePlaceholderReferenceTargetResponse(true);
	}

	/**
	 * Create response: The placeholder reference target should be created.
	 */
	public static AutoCreatePlaceholderReferenceTargetResponse proceed() {
		return new AutoCreatePlaceholderReferenceTargetResponse(false);
	}
}
