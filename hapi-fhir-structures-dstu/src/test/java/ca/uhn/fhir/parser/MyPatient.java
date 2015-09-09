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

	private static final long serialVersionUID = 1L;

	@Child(name = "importantDates", max = Child.MAX_UNLIMITED)
	@Extension(url = "http://example.com/dontuse#importantDates", definedLocally = false, isModifier = true)
	@Description(shortDefinition = "Some dates of note for the patient")
	private List<DateTimeDt> myImportantDates;

	@Child(name = "managingOrganization", order = Child.REPLACE_PARENT, min = 0, max = 1, type = { MyOrganization.class })
	@Description(shortDefinition = "Organization that is the custodian of the patient record", formalDefinition = "Organization that is the custodian of the patient record")
	private ResourceReferenceDt myManagingOrganization;

	@Child(name = "petName")
	@Extension(url = "http://example.com/dontuse#petname", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "The name of the patient's favourite pet")
	private StringDt myPetName;

	@Child(name = "someOrganization", min = 0, max = 1, type = { MyOrganization.class })
	@Extension(definedLocally = true, isModifier = false, url = "http://foo/someOrg")
	private ResourceReferenceDt mySomeOrganization;

	public List<DateTimeDt> getImportantDates() {
		if (myImportantDates == null) {
			myImportantDates = new ArrayList<DateTimeDt>();
		}
		return myImportantDates;
	}

	public ResourceReferenceDt getManagingOrganization() {
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReferenceDt();
		}
		return myManagingOrganization;
	}

	public StringDt getPetName() {
		return myPetName;
	}

	public ResourceReferenceDt getSomeOrganization() {
		if (mySomeOrganization == null) {
			mySomeOrganization = new ResourceReferenceDt();
		}
		return mySomeOrganization;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && myPetName.isEmpty();
	}

	public void setImportantDates(List<DateTimeDt> theImportantDates) {
		myImportantDates = theImportantDates;
	}

	public MyPatient setManagingOrganization(ResourceReferenceDt theManagingOrganization) {
		myManagingOrganization = theManagingOrganization;
		return this;
	}

	public void setPetName(StringDt thePetName) {
		myPetName = thePetName;
	}

	public void setSomeOrganization(ResourceReferenceDt theSomeOrganization) {
		mySomeOrganization = theSomeOrganization;
	}

}
