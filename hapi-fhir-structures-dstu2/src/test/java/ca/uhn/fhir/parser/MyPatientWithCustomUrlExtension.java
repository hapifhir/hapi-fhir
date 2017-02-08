package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef()
public class MyPatientWithCustomUrlExtension extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(name = "petName")
	@Extension(url = "/petname", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The name of the patient's favourite pet")
	private StringDt myPetName;

	public StringDt getPetName() {
		return myPetName;
	}

	public void setPetName(StringDt thePetName) {
		myPetName = thePetName;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}

}
