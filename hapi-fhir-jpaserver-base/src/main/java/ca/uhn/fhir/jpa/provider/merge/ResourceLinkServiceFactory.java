// Created by claude-sonnet-4-5
/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Factory for selecting the appropriate IResourceLinkService implementation based on resource type.
 *
 * This factory provides the routing logic to determine whether to use:
 * - PatientNativeLinkService: for Patient resources (uses native Patient.link field)
 * - ExtensionBasedLinkService: for all other resources (uses FHIR extensions)
 *
 * The decision is made based on the resource type string in a version-agnostic manner.
 */
public class ResourceLinkServiceFactory {

	private final PatientNativeLinkService myPatientService;
	private final ExtensionBasedLinkService myExtensionService;

	public ResourceLinkServiceFactory(
			PatientNativeLinkService thePatientService, ExtensionBasedLinkService theExtensionService) {
		myPatientService = thePatientService;
		myExtensionService = theExtensionService;
	}

	/**
	 * Get the appropriate link service for a given resource type.
	 *
	 * @param theResourceType the FHIR resource type (e.g., "Patient", "Observation", "Practitioner")
	 * @return PatientNativeLinkService for "Patient", ExtensionBasedLinkService for all others
	 */
	public IResourceLinkService getServiceForResourceType(String theResourceType) {
		if ("Patient".equalsIgnoreCase(theResourceType)) {
			return myPatientService;
		}
		return myExtensionService;
	}

	/**
	 * Get the appropriate link service for a given resource instance.
	 *
	 * @param theResource the FHIR resource instance
	 * @return PatientNativeLinkService for Patient resources, ExtensionBasedLinkService for all others
	 */
	public IResourceLinkService getServiceForResource(IBaseResource theResource) {
		return getServiceForResourceType(theResource.fhirType());
	}
}
