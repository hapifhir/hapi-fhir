package example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

public class MyPatientUse {

   @ResourceDef
   public static class MyPatient extends Patient {

      @Child(name="petName")  
      @Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
      @Description(shortDefinition="The name of the patient's favourite pet")
      private StringDt myPetName;
      
      public StringDt getPetName() {
         if(myPetName==null) {
            myPetName = new StringDt();
         }
         return myPetName;
      }

      public void setPetName(StringDt thePetName) {
         myPetName = thePetName;
      }

      public List<DateTimeDt> getImportantDates() {
         if (myImportantDates==null) {
            myImportantDates=new ArrayList<DateTimeDt>();
         }
         return myImportantDates;
      }

      public void setImportantDates(List<DateTimeDt> theImportantDates) {
         myImportantDates = theImportantDates;
      }

      @Child(name="importantDates", max=Child.MAX_UNLIMITED)   
      @Extension(url="http://example.com/dontuse#importantDates", definedLocally=false, isModifier=true)
      @Description(shortDefinition="Some dates of note for the patient")
      private List<DateTimeDt> myImportantDates;

   }
   
@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {
//START SNIPPET: patientUse
MyPatient patient = new MyPatient();
patient.setPetName(new StringDt("Fido"));
patient.getImportantDates().add(new DateTimeDt("2010-01-02"));
patient.getImportantDates().add(new DateTimeDt("2014-01-26T11:11:11"));

patient.addName().addFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");

IParser p = FhirContext.forDstu2().newXmlParser().setPrettyPrint(true);
String messageString = p.encodeResourceToString(patient);

System.out.println(messageString);
//END SNIPPET: patientUse
	
//START SNIPPET: patientParse
IParser parser = FhirContext.forDstu2().newXmlParser();
MyPatient newPatient = parser.parseResource(MyPatient.class, messageString);
//END SNIPPET: patientParse

{
	FhirContext ctx2 = FhirContext.forDstu2();
	RuntimeResourceDefinition def = ctx2.getResourceDefinition(patient);
	System.out.println(ctx2.newXmlParser().setPrettyPrint(true).encodeResourceToString(def.toProfile()));
}
}
	
}
