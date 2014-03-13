package ca.uhn.fhir.model.dstu.resource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name="Binary", profile="http://hl7.org/fhir/profiles/Binary", id="binary")
public class Binary implements IResource {

	// TODO: implement binary
	
	@Override
	public boolean isEmpty() {
		return true;
	}

}
