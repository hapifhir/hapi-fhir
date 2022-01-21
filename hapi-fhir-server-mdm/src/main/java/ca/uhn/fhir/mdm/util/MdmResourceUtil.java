package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.MdmConstants;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class MdmResourceUtil {

	private MdmResourceUtil() {
	}

	/**
	 * If the resource is tagged as not managed by MDM, return false. Otherwise true.
	 *
	 * @param theBaseResource The FHIR resource that is potentially managed by MDM.
	 * @return A boolean indicating whether MDM can manage this resource.
	 */
	public static boolean isMdmAllowed(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_NO_MDM_MANAGED) == null;
	}

	/**
	 * Checks for the presence of the MDM-managed tag, indicating the MDM system has ownership
	 * of this golden resource's links.
	 *
	 * @param theBaseResource the resource to check.
	 * @return a boolean indicating whether or not MDM manages this FHIR resource.
	 */
	public static boolean isMdmManaged(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_HAPI_MDM_MANAGED);
	}

	public static boolean isGoldenRecord(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD);
	}

	public static boolean hasGoldenRecordSystemTag(IBaseResource theIBaseResource) {
		return resourceHasTagWithSystem(theIBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS);
	}

	public static boolean containsTagWithSystem(IBaseResource theBaseResource) {
		return resourceHasTagWithSystem(theBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS);
	}

	public static boolean isGoldenRecordRedirected(IBaseResource theBaseResource) {
		return resourceHasTag(theBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED);
	}

	private static boolean resourceHasTag(IBaseResource theBaseResource, String theSystem, String theCode) {
		if (theBaseResource == null) {
			return false;
		}
		return theBaseResource.getMeta().getTag(theSystem, theCode) != null;
	}

	private static boolean resourceHasTagWithSystem(IBaseResource theBaseResource, @Nonnull String theSystem) {
		if (theBaseResource == null) {
			return false;
		}
		return theBaseResource.getMeta().getTag().stream().anyMatch(tag -> theSystem.equalsIgnoreCase(tag.getSystem()));
	}

	private static Optional<? extends IBaseCoding> getTagWithSystem(IBaseResource theResource, @Nonnull String theSystem) {
		return theResource.getMeta().getTag().stream().filter(tag -> theSystem.equalsIgnoreCase(tag.getSystem())).findFirst();
	}

	public static void removeTagWithSystem(IBaseResource theResource, @Nonnull String theSystem) {
		theResource.getMeta().getTag().removeIf(tag -> theSystem.equalsIgnoreCase(tag.getSystem()));
	}

	/**
	 * Sets the MDM-managed tag, indicating the MDM system has ownership of this
	 * Resource. No changes are made if resource is already maanged by MDM.
	 *
	 * @param theBaseResource resource to set the tag for
	 * @return Returns resource with the tag set.
	 */
	public static IBaseResource setMdmManaged(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_HAPI_MDM_MANAGED, MdmConstants.DISPLAY_HAPI_MDM_MANAGED);
	}

	public static IBaseResource setGoldenResource(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD, MdmConstants.DISPLAY_GOLDEN_RECORD);
	}

	public static IBaseResource setGoldenResourceRedirected(IBaseResource theBaseResource) {
		return setTagOnResource(theBaseResource, MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED, MdmConstants.DISPLAY_GOLDEN_REDIRECT);
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
}
