package example;

import java.io.IOException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.DataFormatException;

public class Narrative {

public static void main(String[] args) throws DataFormatException, IOException {

//START SNIPPET: example1
Patient patient = new Patient();
patient.addIdentifier().setSystem("urn:foo").setValue("7000135");
patient.addName().addFamily("Smith").addGiven("John").addGiven("Edward");
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
pat.getText().setStatus(NarrativeStatusEnum.GENERATED);
pat.getText().setDiv("<div>This is the narrative text<br/>this is line 2</div>");
//END SNIPPET: simple
}
	
}
