package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Patient;

@SuppressWarnings("unused")
public class Narrative {

public static void main(String[] args) throws DataFormatException {

//START SNIPPET: example1
Patient patient = new Patient();
patient.addIdentifier().setSystem("urn:foo").setValue("7000135");
patient.addName().setFamily("Smith").addGiven("John").addGiven("Edward");
patient.addAddress().addLine("742 Evergreen Terrace").setCity("Springfield").setState("ZZ");

FhirContext ctx = FhirContext.forDstu2();

// Use the narrative generator
ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

// Encode the output, including the narrative
String output = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(output);
//END SNIPPET: example1

}

public void simple() {
//START SNIPPET: simple
Patient pat = new Patient();
pat.getText().setStatus(org.hl7.fhir.r4.model.Narrative.NarrativeStatus.GENERATED);
pat.getText().setDivAsString("<div>This is the narrative text<br/>this is line 2</div>");
//END SNIPPET: simple
}
	
}
