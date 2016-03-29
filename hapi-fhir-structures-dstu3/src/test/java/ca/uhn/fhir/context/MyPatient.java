package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef()
public class MyPatient extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(name = "importantDates", max = Child.MAX_UNLIMITED)
	@Extension(url = "http://example.com/dontuse#importantDates", definedLocally = false, isModifier = true)
	@Description(shortDefinition = "Some dates of note for the patient")
	private List<DateTimeDt> myImportantDates;

	@Child(name = "petName")
	@Extension(url = "http://example.com/dontuse#petname", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The name of the patient's favourite pet")
	private StringDt myPetName;

	public List<DateTimeDt> getImportantDates() {
		if (myImportantDates == null) {
			myImportantDates = new ArrayList<DateTimeDt>();
		}
		return myImportantDates;
	}

	public StringDt getPetName() {
		return myPetName;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}

	public void setImportantDates(List<DateTimeDt> theImportantDates) {
		myImportantDates = theImportantDates;
	}

	public void setPetName(StringDt thePetName) {
		myPetName = thePetName;
	}

}
