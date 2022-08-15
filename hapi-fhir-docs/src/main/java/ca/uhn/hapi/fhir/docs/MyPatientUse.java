package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPatientUse {

   @ResourceDef()
   public static class MyPatient extends Patient {

      @Child(name="petName")  
      @Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
      @Description(shortDefinition="The name of the patient's favourite pet")
      private StringType myPetName;
      
      public StringType getPetName() {
         if(myPetName==null) {
            myPetName = new StringType();
         }
         return myPetName;
      }

      public void setPetName(StringType thePetName) {
         myPetName = thePetName;
      }

      public List<DateTimeType> getImportantDates() {
         if (myImportantDates==null) {
            myImportantDates= new ArrayList<>();
         }
         return myImportantDates;
      }

      public void setImportantDates(List<DateTimeType> theImportantDates) {
         myImportantDates = theImportantDates;
      }

      @Child(name="importantDates", max=Child.MAX_UNLIMITED)   
      @Extension(url="http://example.com/dontuse#importantDates", definedLocally=false, isModifier=true)
      @Description(shortDefinition="Some dates of note for the patient")
      private List<DateTimeType> myImportantDates;

   }
   
@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {
//START SNIPPET: patientUse
MyPatient patient = new MyPatient();
patient.setPetName(new StringType("Fido"));
patient.getImportantDates().add(new DateTimeType("2010-01-02"));
patient.getImportantDates().add(new DateTimeType("2014-01-26T11:11:11"));

patient.addName().setFamily("Smith").addGiven("John").addGiven("Quincy").addSuffix("Jr");

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
