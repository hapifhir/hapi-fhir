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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;

import java.io.IOException;
import java.util.List;

public class ExtensionsDstu3 {

   public void customType() {

IGenericClient client = FhirContext.forDstu3().newRestfulGenericClient("http://foo");

//START SNIPPET: customTypeClientSimple
// Create an example patient
MyPatient custPatient = new MyPatient();
custPatient.addName().setFamily("Smith").addGiven("John");
custPatient.setPetName(new StringType("Rover")); // populate the extension

// Create the resource like normal
client.create().resource(custPatient).execute();

// You can also read the resource back like normal
custPatient = client.read().resource(MyPatient.class).withId("123").execute();
//END SNIPPET: customTypeClientSimple

//START SNIPPET: customTypeClientSearch
// Perform the search using the custom type
Bundle bundle = client
   .search()
   .forResource(MyPatient.class)
   .returnBundle(Bundle.class)
   .execute();

// Entries in the return bundle will use the given type
MyPatient pat0 = (MyPatient) bundle.getEntry().get(0).getResource();
//END SNIPPET: customTypeClientSearch
      
//START SNIPPET: customTypeClientSearch2
//Perform the search using the custom type
bundle = client
   .history()
   .onInstance(new IdType("Patient/123"))
   .andReturnBundle(Bundle.class)
   .preferResponseType(MyPatient.class)
   .execute();

//Entries in the return bundle will use the given type
MyPatient historyPatient0 = (MyPatient) bundle.getEntry().get(0).getResource();
//END SNIPPET: customTypeClientSearch2

   }

   public void customTypeDeclared() {


//START SNIPPET: customTypeClientDeclared
FhirContext ctx = FhirContext.forDstu3();

// Instruct the context that if it receives a resource which
// claims to conform to the given profile (by URL), it should
// use the MyPatient type to parse this resource
ctx.setDefaultTypeForProfile("http://example.com/StructureDefinition/mypatient", MyPatient.class);

// You can declare as many default types as you like
ctx.setDefaultTypeForProfile("http://foo.com/anotherProfile", CustomObservation.class);

// Create a client
IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

// You can also read the resource back like normal
Patient patient = client.read().resource(Patient.class).withId("123").execute();
if (patient instanceof MyPatient) {
   // If the server supplied a resource which declared to conform
   // to the given profile, MyPatient will have been returned so
   // process it differently..
}

//END SNIPPET: customTypeClientDeclared

      
   }

@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {

	
// START SNIPPET: resourceExtension
// Create an example patient
Patient patient = new Patient();
patient.addIdentifier().setUse(Identifier.IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

// Create an extension
Extension ext = new Extension();
ext.setUrl("http://example.com/extensions#someext");
ext.setValue(new DateTimeType("2011-01-02T11:13:15"));

// Add the extension to the resource
patient.addExtension(ext);
//END SNIPPET: resourceExtension


//START SNIPPET: resourceStringExtension
// Continuing the example from above, we will add a name to the patient, and then
// add an extension to part of that name
HumanName name = patient.addName();
name.setFamily("Shmoe");

// Add a new "given name", which is of type String 
StringType given = name.addGivenElement();
given.setValue("Joe");

// Create an extension and add it to the String
Extension givenExt = new Extension("http://examples.com#moreext", new StringType("Hello"));
given.addExtension(givenExt);
//END SNIPPET: resourceStringExtension

FhirContext ctx = FhirContext.forDstu3();
String output = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(output);


//START SNIPPET: parseExtension
// Get all extensions (modifier or not) for a given URL
List<Extension> resourceExts = patient.getExtensionsByUrl("http://fooextensions.com#exts");

// Get all non-modifier extensions regardless of URL
List<Extension> nonModExts = patient.getExtension();

// Get all modifier extensions regardless of URL
List<Extension> modExts = patient.getModifierExtension();
//END SNIPPET: parseExtension

}


public void foo() {
//START SNIPPET: subExtension
Patient patient = new Patient();

// Add an extension (initially with no contents) to the resource 
Extension parent = new Extension("http://example.com#parent");
patient.addExtension(parent);

// Add two extensions as children to the parent extension
Extension child1 = new Extension("http://example.com#childOne", new StringType("value1"));
parent.addExtension(child1);

Extension child2 = new Extension("http://example.com#chilwo", new StringType("value1"));
parent.addExtension(child2);
//END SNIPPET: subExtension
	
}

}
