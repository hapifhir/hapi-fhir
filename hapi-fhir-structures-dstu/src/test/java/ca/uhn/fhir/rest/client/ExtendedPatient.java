package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef(name = "Patient", profile = "http://foo/profiles/Profile", id = "prof2")
public class ExtendedPatient extends Patient {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * Each extension is defined in a field. Any valid HAPI Data Type
	 * can be used for the field type. Note that the [name=""] attribute
	 * in the @Child annotation needs to match the name for the bean accessor
	 * and mutator methods.
	 */
	@Child(name = "petName")
	@Extension(url = "http://example.com/dontuse#petname", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The name of the patient's favourite pet")
	private StringDt myPetName;

	public StringDt getPetName() {
		if (myPetName == null) {
			myPetName = new StringDt();
		}
		return myPetName;
	}

	public void setPetName(StringDt thePetName) {
		myPetName = thePetName;
	}

}