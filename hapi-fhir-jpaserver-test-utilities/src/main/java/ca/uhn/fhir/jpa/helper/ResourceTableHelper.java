package ca.uhn.fhir.jpa.helper;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceTableHelper {
	private static final String RESOURCE_PID = "RESOURCE_PID";

	public static Long getPidOrNull(IBaseResource theResource) {
		IAnyResource anyResource = (IAnyResource) theResource;
		return (Long) anyResource.getUserData(RESOURCE_PID);
	}
}
