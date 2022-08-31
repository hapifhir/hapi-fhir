package ca.uhn.fhir.jpa.packages;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IIGBlacklistInterceptor {
	default boolean isValidForUpload(IBaseResource theResource) {
		return true;
	}
}
