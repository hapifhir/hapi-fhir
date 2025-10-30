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

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This object is used as a method parameter for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE}
 * pointcut.
 *
 * @since 8.4.0
 */
public class AutoCreatePlaceholderReferenceTargetRequest {

	private final IBaseResource myTargetResourceToCreate;

	/**
	 * Constructor
	 */
	public AutoCreatePlaceholderReferenceTargetRequest(IBaseResource theTargetResourceToCreate) {
		myTargetResourceToCreate = theTargetResourceToCreate;
	}

	/**
	 * Provides the resource that is going to be automatically created. Interceptors may make changes
	 * to the resource, but they must not modify its ID.
	 */
	public IBaseResource getTargetResourceToCreate() {
		return myTargetResourceToCreate;
	}
}
