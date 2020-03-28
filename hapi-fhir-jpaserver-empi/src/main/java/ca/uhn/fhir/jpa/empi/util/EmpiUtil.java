package ca.uhn.fhir.jpa.empi.util;

public final class EmpiUtil {
	private EmpiUtil() {}

	public static boolean supportedResourceType(String theResourceType) {
		return ("Patient".equals(theResourceType) || "Practitioner".equals(theResourceType));
	}
}
