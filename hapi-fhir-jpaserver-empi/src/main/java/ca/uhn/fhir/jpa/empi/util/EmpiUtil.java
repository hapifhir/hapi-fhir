package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.empi.api.Constants;
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
		return theBaseResource.getMeta().getTag(Constants.SYSTEM_EMPI_MANAGED, Constants.CODE_NO_EMPI_MANAGED) == null;
	}
}
