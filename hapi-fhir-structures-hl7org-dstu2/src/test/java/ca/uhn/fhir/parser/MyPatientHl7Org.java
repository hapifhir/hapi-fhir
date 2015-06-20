package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Extension;
import org.hl7.fhir.instance.model.annotations.ResourceDef;


@ResourceDef()
public class MyPatientHl7Org extends Patient {
	
	private static final long serialVersionUID = 1L;

	@Child(name="petName")	
	@Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
	@Description(shortDefinition="The name of the patient's favourite pet")
	private StringType myPetName;
	
	@Child(name="importantDates", max=Child.MAX_UNLIMITED)	
	@Extension(url="http://example.com/dontuse#importantDates", definedLocally=false, isModifier=true)
	@Description(shortDefinition="Some dates of note for the patient")
	private List<DateTimeType> myImportantDates;
	
	@Child(name="managingOrganization", order=Child.REPLACE_PARENT, min=0, max=1, type={
			MyOrganizationDstu2.class	})
		@Description(
			shortDefinition="Organization that is the custodian of the patient record",
			formalDefinition="Organization that is the custodian of the patient record"
		)
	private Reference myManagingOrganization;
	
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}
	
	
	public List<DateTimeType> getImportantDates() {
		if (myImportantDates==null) {
			myImportantDates = new ArrayList<DateTimeType>();
		}
		return myImportantDates;
	}

	public StringType getPetName() {
		return myPetName;
	}

	public void setImportantDates(List<DateTimeType> theImportantDates) {
		myImportantDates = theImportantDates;
	}

	public void setPetName(StringType thePetName) {
		myPetName = thePetName;
	}
	
}
