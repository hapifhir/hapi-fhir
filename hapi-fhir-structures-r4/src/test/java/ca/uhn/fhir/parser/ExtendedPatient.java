package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Patient;


@ResourceDef(
	name = "Patient",
	profile = "http://acme.org//StructureDefinition/patient-with-eyes"
)
public class ExtendedPatient extends Patient {

	@Child(name = "eyeColour")
	@Extension(url = "http://acme.org/#extpt", definedLocally = false, isModifier = false)
	private CodeType myEyeColour;

	public CodeType getEyeColour() {
		if (myEyeColour == null) {
			myEyeColour = new CodeType();
		}
		return myEyeColour;
	}

	public void setEyeColour(CodeType theEyeColour) {
		myEyeColour = theEyeColour;
	}

}
