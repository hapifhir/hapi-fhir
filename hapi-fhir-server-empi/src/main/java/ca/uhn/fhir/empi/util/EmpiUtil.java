package ca.uhn.fhir.empi.util;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;

public final class EmpiUtil {
	private EmpiUtil() {}

	public static boolean supportedTargetType(String theResourceType) {
		return ("Patient".equals(theResourceType) || "Practitioner".equals(theResourceType));
	}

	public static boolean isEmpiResourceType(FhirContext theFhirContext, IBaseResource theResource) {
		String resourceType = theFhirContext.getResourceType(theResource);
		return ("Patient".equals(resourceType) ||
			"Practitioner".equals(resourceType)) ||
			"Person".equals(resourceType);
	}

	/**
	 * If the resource is tagged as not managed by empi, return false. Otherwise true.
	 * @param theBaseResource The Patient/Practitioner that is potentially managed by EMPI.
	 * @return A boolean indicating whether EMPI should manage this resource.
	 */
	public static boolean isEmpiAccessible(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_NO_EMPI_MANAGED) == null;
	}

	/**
	 * Checks for the presence of the EMPI-managed tag, indicating the EMPI system has ownership
	 * of this Person's links.
	 *
	 * @param theBaseResource the resource to check.
	 * @return a boolean indicating whether or not EMPI manages this Person.
	 */
	public static boolean isEmpiManaged(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_HAPI_EMPI_MANAGED) != null;
	}

	public static boolean isEmpiManagedPerson(FhirContext theFhirContext, IBaseResource theResource) {
		String resourceType = theFhirContext.getResourceType(theResource);

		return "Person".equals(resourceType) && isEmpiManaged(theResource);
	}
}
