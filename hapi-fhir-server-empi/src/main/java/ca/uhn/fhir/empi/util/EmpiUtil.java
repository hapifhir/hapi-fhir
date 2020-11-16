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
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class EmpiUtil {
	private EmpiUtil() {}


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
		return theBaseResource.getMeta().getTag(EmpiConstants.SYSTEM_MDM_MANAGED, EmpiConstants.CODE_NO_EMPI_MANAGED) == null;
	}

	/**
	 * Checks for the presence of the EMPI-managed tag, indicating the EMPI system has ownership
	 * of this Person's links.
	 *
	 * @param theBaseResource the resource to check.
	 * @return a boolean indicating whether or not EMPI manages this Person.
	 */
	public static boolean isEmpiManaged(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, EmpiConstants.SYSTEM_MDM_MANAGED, EmpiConstants.CODE_HAPI_MDM_MANAGED);
	}

	public static boolean isGoldenRecord(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD);
	}

	public static boolean isGoldenRecordRedirected(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD_REDIRECTED);
	}

	private static boolean resourceHasTag(IBaseResource theTheBaseResource, String theSystem, String theCode) {
		return theTheBaseResource.getMeta().getTag(theSystem, theCode) != null;
	}

	private static Optional<? extends IBaseCoding> getTagWithSystem(IBaseResource theResource, String theSystem) {
		return theResource.getMeta().getTag().stream().filter(tag -> tag.getSystem().equalsIgnoreCase(theSystem)).findFirst();
	}

	public static void removeTagWithSystem(IBaseResource theResource, String theSystem) {
		theResource.getMeta().getTag().removeIf(tag -> tag.getSystem().equalsIgnoreCase(theSystem));
	}


	/**
	 * Sets the EMPI-managed tag, indicating the EMPI system has ownership of this
	 * Resource. No changes are made if resource is already maanged by EMPI.
	 *
	 * @param theBaseResource resource to set the tag for
	 * @return
	 * 		Returns resource with the tag set.
	 */
	public static IBaseResource setEmpiManaged(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, EmpiConstants.SYSTEM_MDM_MANAGED, EmpiConstants.CODE_HAPI_MDM_MANAGED, EmpiConstants.DISPLAY_HAPI_EMPI_MANAGED);
	}

	public static IBaseResource setGoldenResource(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD, EmpiConstants.DISPLAY_GOLDEN_RECORD);
	}

	public static IBaseResource setGoldenResourceRedirected(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD_REDIRECTED, EmpiConstants.DISPLAY_GOLDEN_REDIRECT);
	}

	/**
	 * WARNING: This code may _look_ like it replaces in place a code of a tag, but this DOES NOT ACTUALLY WORK!. In reality what will
	 * happen is a secondary tag will be created with the same system. the only way to actually remove a tag from a resource
	 * is by calling dao.removeTag(). This logic here is for the case where our representation of the resource still happens to contain
	 * a reference to a tag, to make sure it isn't double-added.
	 */
	@Nonnull
	private static IBaseResource setTagOnResource(IBaseResource theGoldenResource, String theSystem, String theCode, String theDisplay) {
		Optional<? extends IBaseCoding> tagWithSystem = getTagWithSystem(theGoldenResource, theSystem);
		if (tagWithSystem.isPresent()) {
			tagWithSystem.get().setCode(theCode);
			tagWithSystem.get().setDisplay(theDisplay);
		} else {
			IBaseCoding tag = theGoldenResource.getMeta().addTag();
			tag.setSystem(theSystem);
			tag.setCode(theCode);
			tag.setDisplay(theDisplay);

		}
		return theGoldenResource;
	}

	public static boolean isEmpiManagedPerson(FhirContext theFhirContext, IBaseResource theResource) {
		String resourceType = theFhirContext.getResourceType(theResource);

		return "Person".equals(resourceType) && isEmpiManaged(theResource);
	}
}
