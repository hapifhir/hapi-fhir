package ca.uhn.fhir.jpa.empi.util;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.api.EmpiConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;

public final class EmpiUtil {
	private EmpiUtil() {}

	public static boolean supportedResourceType(String theResourceType) {
		return ("Patient".equals(theResourceType) || "Practitioner".equals(theResourceType));
	}

	/**
	 * If the resource is tagged as not managed by empi, return false. Otherwise true.
	 * @param theBaseResource The Patient/Practitioner that is potentially managed by EMPI.
	 * @return A boolean indicating whether EMPI should manage this resource.
	 */
	public static boolean isManagedByEmpi(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_NO_EMPI_MANAGED) == null;
	}
}
