package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.ResourceType;

@ResourceDef(name = "Clock", profile = "http://example.org/StructureDefinition/Clock")
public class Clock extends DomainResource {
	@Override
	public DomainResource copy() {
		return null;
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}
}
