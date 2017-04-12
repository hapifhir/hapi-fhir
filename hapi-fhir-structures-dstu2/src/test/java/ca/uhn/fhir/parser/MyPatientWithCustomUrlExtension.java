package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef()
public class MyPatientWithCustomUrlExtension extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(name = "petName")
	@Extension(url = "/petname", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The name of the patient's favourite pet")
	private StringDt myPetName;

	@Child(name = "customid")
	@Extension(url = "/customid", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The customid of the patient's ")
	private IdDt myCustomId;

	public StringDt getPetName() {
		if (myPetName == null) {
			myPetName = new StringDt();
		}
		return myPetName;
	}

	public void setPetName(final StringDt thePetName) {
		myPetName = thePetName;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}

	public IdDt getCustomId() {
		if (myCustomId == null) {
			myCustomId = new IdDt();
		}
		return myCustomId;
	}

	public void setCustomId(final IdDt myCustomId) {
		this.myCustomId = myCustomId;
	}
}
