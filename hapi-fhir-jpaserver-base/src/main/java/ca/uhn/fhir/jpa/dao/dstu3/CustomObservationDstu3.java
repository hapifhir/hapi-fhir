package ca.uhn.fhir.jpa.dao.dstu3;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "Observation", profile = CustomObservationDstu3.PROFILE)
public class CustomObservationDstu3 extends Observation {

	public static final String PROFILE = "http://custom_ObservationDstu3";
	
	private static final long serialVersionUID = 1L;

	@Extension(definedLocally = false, isModifier = false, url = "http://eyeColour")
	@Child(name = "eyeColour")
	private StringType myEyeColour;

	public StringType getEyeColour() {
		return myEyeColour;
	}

	public void setEyeColour(StringType theEyeColour) {
		myEyeColour = theEyeColour;
	}
	
}