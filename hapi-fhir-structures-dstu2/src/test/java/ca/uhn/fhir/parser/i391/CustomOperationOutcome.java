package ca.uhn.fhir.parser.i391;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;

@ResourceDef(name = "OperationOutcome", profile = "http://hl7.org/fhir/profiles/custom-operation-outcome", id = "custom-operation-outcome")
public class CustomOperationOutcome extends OperationOutcome {

	@Child(name = "someElement2", type = CustomBlock.class)
	@Extension(definedLocally = false, isModifier = false, url = "#someElement2")
	public CustomBlock element2;
	
}
