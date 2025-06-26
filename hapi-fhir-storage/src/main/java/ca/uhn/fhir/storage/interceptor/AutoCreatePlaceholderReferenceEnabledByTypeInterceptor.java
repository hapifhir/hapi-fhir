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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;

import java.util.Set;

/**
 * This interceptor can be used on systems with
 * {@link ca.uhn.fhir.jpa.api.config.JpaStorageSettings#setAutoCreatePlaceholderReferenceTargets(boolean) Auto-Create Placeholder Reference Targets}
 * enabled in order to cause these placeholder resources to only be created for specific resource types.
 *
 * @since 8.4.0
 */
@Interceptor
public class AutoCreatePlaceholderReferenceEnabledByTypeInterceptor {

	private final Set<String> myResourceTypesToAllow;

	/**
	 * Constructor
	 *
	 * @param theResourceTypesToAllow The resource types to allow. For example, if you pass in the strings "Patient"
	 *                                and "Observation", then placeholder reference target resources will be created
	 *                                with these resource types but not with any other resource types.
	 */
	public AutoCreatePlaceholderReferenceEnabledByTypeInterceptor(String... theResourceTypesToAllow) {
		myResourceTypesToAllow = Set.of(theResourceTypesToAllow);
	}

	/**
	 * This method will be called automatically before each auto-created placeholder
	 * reference target resource.
	 */
	@Hook(Pointcut.STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE)
	public AutoCreatePlaceholderReferenceTargetResponse autoCreatePlaceholderReferenceTarget(
			AutoCreatePlaceholderReferenceTargetRequest theRequest) {

		String resourceType =
				theRequest.getTargetResourceToCreate().getIdElement().getResourceType();
		if (!myResourceTypesToAllow.contains(resourceType)) {
			return AutoCreatePlaceholderReferenceTargetResponse.doNotCreateTarget();
		}

		return AutoCreatePlaceholderReferenceTargetResponse.proceed();
	}
}
