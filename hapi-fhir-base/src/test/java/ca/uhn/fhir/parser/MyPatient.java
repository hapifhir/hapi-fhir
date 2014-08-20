package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef()
public class MyPatient extends Patient {
	
	@Child(name="petName")	
	@Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
	@Description(shortDefinition="The name of the patient's favourite pet")
	private StringDt myPetName;
	
	@Child(name="importantDates", max=Child.MAX_UNLIMITED)	
	@Extension(url="http://example.com/dontuse#importantDates", definedLocally=false, isModifier=true)
	@Description(shortDefinition="Some dates of note for the patient")
	private List<DateTimeDt> myImportantDates;
	
	@Child(name="managingOrganization", order=Child.REPLACE_PARENT, min=0, max=1, type={
			MyOrganization.class	})
		@Description(
			shortDefinition="Organization that is the custodian of the patient record",
			formalDefinition="Organization that is the custodian of the patient record"
		)
	private ResourceReferenceDt myManagingOrganization;
	
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}
	
	
	public List<DateTimeDt> getImportantDates() {
		if (myImportantDates==null) {
			myImportantDates = new ArrayList<DateTimeDt>();
		}
		return myImportantDates;
	}

	public StringDt getPetName() {
		return myPetName;
	}

	public void setImportantDates(List<DateTimeDt> theImportantDates) {
		myImportantDates = theImportantDates;
	}

	public void setPetName(StringDt thePetName) {
		myPetName = thePetName;
	}
	
}
