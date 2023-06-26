package ca.uhn.fhir.rest.client;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.DateType;
import org.hl7.fhir.dstu2016may.model.DomainResource;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.ResourceType;
import org.hl7.fhir.dstu2016may.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;


@ResourceDef(name="Patient", profile="http://example.com/StructureDefinition/patient_with_extensions")
public class MyPatientWithExtensions extends DomainResource {
	
	private static final long serialVersionUID = 1L;

	@Extension(url = "http://example.com/ext/date", definedLocally = false, isModifier = true)
	@Child(name = "modExt")
	private DateType myDateExt;

	@Extension(url = "http://example.com/ext/string", definedLocally = false, isModifier = false)
	@Child(name = "extAtt")
	private StringType myStringExt;

   @Child(name = "name", type = {HumanName.class}, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
   @Description(shortDefinition="A name associated with the patient", formalDefinition="A name associated with the individual." )
	private List<HumanName> myName;
	
	
	public List<HumanName> getName() {
		if (myName == null) {
			myName = new ArrayList<HumanName>();
		}
		return myName;
	}

	public void setName(List<HumanName> theName) {
		myName = theName;
	}

	public DateType getDateExt() {
		if (myDateExt == null) {
			myDateExt = new DateType();
		}
		return myDateExt;
	}

	public StringType getStringExt() {
		if (myStringExt == null) {
			myStringExt = new StringType();
		}
		return myStringExt;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myStringExt, myDateExt);
	}

	public void setDateExt(DateType theDateExt) {
		myDateExt = theDateExt;
	}

	public void setStringExt(StringType theStringExt) {
		myStringExt = theStringExt;
	}

	@Override
	public DomainResource copy() {
		return null;
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.Patient;
	}

	public HumanName addName() {
		HumanName retVal = new HumanName();
		getName().add(retVal);
		return retVal;
	}


}
