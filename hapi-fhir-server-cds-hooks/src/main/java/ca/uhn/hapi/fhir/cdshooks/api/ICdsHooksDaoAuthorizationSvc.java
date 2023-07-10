package ca.uhn.hapi.fhir.cdshooks.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ICdsHooksDaoAuthorizationSvc {
	void authorizePreShow(IBaseResource theResource);
}
