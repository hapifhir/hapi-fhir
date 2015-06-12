package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

public class FhirContextIntro {

	@SuppressWarnings("unused")
	public static void creatingContext() {
// START SNIPPET: creatingContext
// Create a context for DSTU1
FhirContext ctx = FhirContext.forDstu1();

// Alternately, create a context for DSTU2
FhirContext ctxDstu2 = FhirContext.forDstu2();
// END SNIPPET: creatingContext
		
	}
	
   @SuppressWarnings("unused")
   public static void creatingContextHl7org() {
// START SNIPPET: creatingContextHl7org
// To use the RI structures, you need to specifically use a FhirContext configured
// for HL7.org Structures
FhirContext ctx = FhirContext.forDstu2Hl7Org();

// Working with RI structures is similar to how it works with the HAPI structures
org.hl7.fhir.instance.model.Patient patient = new org.hl7.fhir.instance.model.Patient();
patient.addName().addGiven("John").addFamily("Smith");
patient.getBirthDateElement().setValueAsString("1998-02-22");

// Parsing and encoding works the same way too
String encoded = ctx.newJsonParser().encodeResourceToString(patient);

// END SNIPPET: creatingContextHl7org
      
   }
	
	public static void main(String[] args) throws DataFormatException {

new FhirContextIntro().encodeMsg();
		
		
	}
		
	public void encodeMsg() throws DataFormatException {
FhirContext ctx = new FhirContext(Patient.class, Observation.class);
//START SNIPPET: encodeMsg

/**
 * FHIR model types in HAPI are simple POJOs. To create a new
 * one, invoke the default constructor and then
 * start populating values.
 */
Patient patient = new Patient();

// Add an MRN (a patient identifier)
IdentifierDt id = patient.addIdentifier();
id.setSystem("http://example.com/fictitious-mrns");
id.setValue("MRN001");

// Add a name
HumanNameDt name = patient.addName();
name.setUse(NameUseEnum.OFFICIAL);
name.addFamily("Tester");
name.addGiven("John");
name.addGiven("Q");

// We can now use a parser to encode this resource into a string.
String encoded = ctx.newXmlParser().encodeResourceToString(patient);
System.out.println(encoded);
//END SNIPPET: encodeMsg

//START SNIPPET: encodeMsgJson
IParser jsonParser = ctx.newJsonParser();
jsonParser.setPrettyPrint(true);
encoded = jsonParser.encodeResourceToString(patient);
System.out.println(encoded);
//END SNIPPET: encodeMsgJson

		
	}

	
public void fluent() throws DataFormatException {
FhirContext ctx = new FhirContext(Patient.class, Observation.class);
String encoded;
//START SNIPPET: encodeMsgFluent
Patient patient = new Patient();
patient.addIdentifier().setSystem("http://example.com/fictitious-mrns").setValue("MRN001");
patient.addName().setUse(NameUseEnum.OFFICIAL).addFamily("Tester").addGiven("John").addGiven("Q");

encoded = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(encoded);
//END SNIPPET: encodeMsgFluent
	
}
	
	
	public static void parseMsg() {
FhirContext ctx = FhirContext.forDstu1();

//START SNIPPET: parseMsg
// The following is an example Patient resource
String msgString = "<Patient xmlns=\"http://hl7.org/fhir\">" 
  + "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal</div></text>"
  + "<identifier><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
  + "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
  + "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
  + "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
  + "</Patient>";

// The hapi context object is used to create a new XML parser
// instance. The parser can then be used to parse (or unmarshall) the 
// string message into a Patient object 
IParser parser = ctx.newXmlParser();
Patient patient = parser.parseResource(Patient.class, msgString);

// The patient object has accessor methods to retrieve all of the
// data which has been parsed into the instance. 
String patientId = patient.getIdentifier().get(0).getValue();
String familyName = patient.getName().get(0).getFamily().get(0).getValue();
String gender = patient.getGender();

System.out.println(patientId); // PRP1660
System.out.println(familyName); // Cardinal
System.out.println(gender); // M
//END SNIPPET: parseMsg

	}

}
