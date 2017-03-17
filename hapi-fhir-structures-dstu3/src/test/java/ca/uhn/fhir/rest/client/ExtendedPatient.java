package ca.uhn.fhir.rest.client;

import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "Patient", profile = ExtendedPatient.HTTP_FOO_PROFILES_PROFILE)
public class ExtendedPatient extends Patient {

	static final String HTTP_FOO_PROFILES_PROFILE = "http://foo/profiles/Profile";
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
	private StringType myPetName;

	public StringType getPetName() {
		if (myPetName == null) {
			myPetName = new StringType();
		}
		return myPetName;
	}

	public void setPetName(StringType thePetName) {
		myPetName = thePetName;
	}

}