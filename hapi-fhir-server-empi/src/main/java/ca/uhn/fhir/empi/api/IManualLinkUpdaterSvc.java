package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IManualLinkUpdaterSvc {
	void updateLink(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchResultEnum theMatchResult);
}
