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
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.*;

public class FhirDataModel {

   public static void datatypes() {
      // START SNIPPET: datatypes
      Observation obs = new Observation();

      // These are all equivalent
      obs.setIssuedElement(new InstantType(new Date()));
		obs.setIssuedElement(new InstantType(new Date(), TemporalPrecisionEnum.MILLI));
		obs.setIssued(new Date());

      // The InstantType also lets you work with the instant as a Java Date
      // object or as a FHIR String.
      Date date = obs.getIssuedElement().getValue(); // A date object
      String dateString = obs.getIssuedElement().getValueAsString(); // "2014-03-08T12:59:58.068-05:00"
      // END SNIPPET: datatypes

      System.out.println(date);
      System.out.println(dateString);

   }

   @SuppressWarnings("unused")
   public void nonNull() {
      // START SNIPPET: nonNull
      Observation observation = new Observation();

      // None of these calls will not return null, but instead create their
      // respective
      // child elements.
      List<Identifier> identifierList = observation.getIdentifier();
      CodeableConcept code = observation.getCode();
      StringType textElement = observation.getCode().getTextElement();

      // DateTimeDt is a FHIR primitive however, so the following will return
      // null
      // unless a value has been placed there.
      Date active = observation.addIdentifier().getPeriod().getStartElement().getValue();
      // END SNIPPET: nonNull

   }

   @SuppressWarnings("unused")
   public static void codes() {
      // START SNIPPET: codes
      Patient patient = new Patient();

      // You can set this code using a String if you want. Note that
      // for "closed" valuesets (such as the one used for Patient.gender)
      // you must use one of the strings defined by the FHIR specification.
      // You must not define your own.
      patient.getGenderElement().setValueAsString("male");
      
      // HAPI also provides Java enumerated types which make it easier to
      // deal with coded values. This code achieves the exact same result
      // as the code above.
      patient.setGender(Enumerations.AdministrativeGender.MALE);
      
      // You can also retrieve coded values the same way
      String genderString = patient.getGenderElement().getValueAsString();
      Enumerations.AdministrativeGender genderEnum = patient.getGenderElement().getValue();
      
      // END SNIPPET: codes

   }

   
   @SuppressWarnings("unused")
   public static void codeableConcepts() {
      // START SNIPPET: codeableConcepts
      Patient patient = new Patient();

      // Coded types can naturally be set using plain strings
      Coding statusCoding = patient.getMaritalStatus().addCoding();
      statusCoding.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
      statusCoding.setCode("M");
      statusCoding.setDisplay("Married");

      // You could add a second coding to the field if needed too. This
      // can be useful if you want to convey the concept using different
      // codesystems.
      Coding secondStatus = patient.getMaritalStatus().addCoding();
      secondStatus.setCode("H");
      secondStatus.setSystem("http://example.com#maritalStatus");
      secondStatus.setDisplay("Happily Married");
      
      // CodeableConcept also has a text field meant to convey 
      // a user readable version of the concepts it conveys.
      patient.getMaritalStatus().setText("Happily Married");
      
      // There are also accessors for retrieving values
      String firstCode  = patient.getMaritalStatus().getCoding().get(0).getCode();
      String secondCode = patient.getMaritalStatus().getCoding().get(1).getCode();
      // END SNIPPET: codeableConcepts

   }


   
   public static void main(String[] args) {
      tmp();
      
      
      datatypes();

      // START SNIPPET: observation
      // Create an Observation instance
      Observation observation = new Observation();
      
      // Give the observation a status
      observation.setStatus(Observation.ObservationStatus.FINAL);
      
      // Give the observation a code (what kind of observation is this)
      Coding coding = observation.getCode().addCoding();
      coding.setCode("29463-7").setSystem("http://loinc.org").setDisplay("Body Weight");
      
      // Create a quantity datatype
      Quantity value = new Quantity();
      value.setValue(83.9).setSystem("http://unitsofmeasure.org").setCode("kg");
      observation.setValue(value);
      
      // Set the reference range
      SimpleQuantity low = new SimpleQuantity();
      low.setValue(45).setSystem("http://unitsofmeasure.org").setCode("kg");
      observation.getReferenceRangeFirstRep().setLow(low);
      SimpleQuantity high = new SimpleQuantity();
      low.setValue(90).setSystem("http://unitsofmeasure.org").setCode("kg");
      observation.getReferenceRangeFirstRep().setHigh(high);
      
      // END SNIPPET: observation
      
      
   }

   private static void tmp() {
// Create a FHIR Context
FhirContext ctx = FhirContext.forR4();

// Create a client
IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseR4");

// Read a patient with the given ID
Patient patient = client
   .read()
   .resource(Patient.class)
   .withId("952975")
   .execute();

// Print the patient's name
String string = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(string);

   }

   public void namesHard() {
      // START SNIPPET: namesHard
      Patient patient = new Patient();
      HumanName name = patient.addName();
      name.setFamily("Smith");
      StringType firstName = name.addGivenElement();
      firstName.setValue("Rob");
      StringType secondName = name.addGivenElement();
      secondName.setValue("Bruce");
      // END SNIPPET: namesHard
   }

   public void namesEasy() {
      // START SNIPPET: namesEasy
      Patient patient = new Patient();
      patient.addName().setFamily("Smith").addGiven("Rob").addGiven("Bruce");
      // END SNIPPET: namesEasy
   }

}
