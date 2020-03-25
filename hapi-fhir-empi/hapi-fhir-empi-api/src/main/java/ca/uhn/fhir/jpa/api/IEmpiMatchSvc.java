package ca.uhn.fhir.jpa.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IEmpiMatchSvc {
	void updatePatientLinks(IBaseResource theResource);
}
