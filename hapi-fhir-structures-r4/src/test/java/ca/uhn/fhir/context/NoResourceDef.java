package ca.uhn.fhir.context;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.ResourceType;

public class NoResourceDef extends DomainResource {
	@Override
	public DomainResource copy() {
		return null;
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}
}
