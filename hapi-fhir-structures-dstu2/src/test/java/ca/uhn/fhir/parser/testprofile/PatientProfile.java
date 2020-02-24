package ca.uhn.fhir.parser.testprofile;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;

import java.util.List;

@ResourceDef(name="Patient", profile="http://hl7.org/fhir/profiles/PatientProfile", id="patientProfile")
public class PatientProfile extends Patient {
	/**
	 * identifier
	 */
	@Child(name = "identifier", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {IdentifierDt.class})
	@Description(shortDefinition = "id", formalDefinition = "An identifier for this patient.")
	public List<IdentifierDt> myIdentifier;


	@Child(name = "name", min = 1, max = 1, order = Child.REPLACE_PARENT, summary = true, type = {HumanNameDt.class})
	@Description(shortDefinition = "", formalDefinition = "Name.")
	public HumanNameDt myName;

}
