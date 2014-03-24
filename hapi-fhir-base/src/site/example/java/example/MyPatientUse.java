package example;

import java.io.IOException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.MyPatient;

public class MyPatientUse {

@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {
//START SNIPPET: patientUse
MyPatient patient = new MyPatient();
patient.setPetName(new StringDt("Fido"));
patient.getImportantDates().add(new DateTimeDt("2010-01-02"));
patient.getImportantDates().add(new DateTimeDt("2014-01-26T11:11:11"));

patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");

IParser p = new FhirContext().newXmlParser().setPrettyPrint(true);
String messageString = p.encodeResourceToString(patient);

System.out.println(messageString);
//END SNIPPET: patientUse
	
//START SNIPPET: patientParse
IParser parser = new FhirContext().newXmlParser();
MyPatient newPatient = parser.parseResource(MyPatient.class, messageString);
//END SNIPPET: patientParse

{
	FhirContext ctx2 = new FhirContext();
	RuntimeResourceDefinition def = ctx2.getResourceDefinition(patient);
	System.out.println(ctx2.newXmlParser().setPrettyPrint(true).encodeResourceToString(def.toProfile()));
}
}
	
}
