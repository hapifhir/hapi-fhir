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
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

import java.io.IOException;
import java.util.List;

public class ExtensionsDstu2 {

@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {

   {   
   Questionnaire q= new Questionnaire();
   Questionnaire.GroupQuestion item = q.getGroup().addQuestion();
   item.setText("Hello");
   
   ExtensionDt extension = new ExtensionDt(false, "http://hl7.org/fhir/StructureDefinition/translation");
   item.getTextElement().addUndeclaredExtension(extension);
   
   extension.addUndeclaredExtension(new ExtensionDt(false, "lang", new CodeDt("es")));
   extension.addUndeclaredExtension(new ExtensionDt(false, "cont", new StringDt("hola")));
   
   System.out.println(FhirContext.forDstu2().newJsonParser().setPrettyPrint(true).encodeResourceToString(q));
   }

   
// START SNIPPET: resourceExtension
// Create an example patient
Patient patient = new Patient();
patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:example").setValue("7000135");

// Create an extension
ExtensionDt ext = new ExtensionDt();
ext.setModifier(false);
ext.setUrl("http://example.com/extensions#someext");
ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));

// Add the extension to the resource
patient.addUndeclaredExtension(ext);
//END SNIPPET: resourceExtension


//START SNIPPET: resourceStringExtension
// Continuing the example from above, we will add a name to the patient, and then
// add an extension to part of that name
HumanNameDt name = patient.addName();
name.addFamily().setValue("Shmoe");

// Add a new "given name", which is of type StringDt 
StringDt given = name.addGiven();
given.setValue("Joe");

// Create an extension and add it to the StringDt
ExtensionDt givenExt = new ExtensionDt(false, "http://examples.com#moreext", new StringDt("Hello"));
given.addUndeclaredExtension(givenExt);
//END SNIPPET: resourceStringExtension

FhirContext ctx = FhirContext.forDstu2();
String output = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(output);


//START SNIPPET: parseExtension
// Get all extensions (modifier or not) for a given URL
List<ExtensionDt> resourceExts = patient.getUndeclaredExtensionsByUrl("http://fooextensions.com#exts");

// Get all non-modifier extensions regardless of URL
List<ExtensionDt> nonModExts = patient.getUndeclaredExtensions();

// Get all modifier extensions regardless of URL
List<ExtensionDt> modExts = patient.getUndeclaredModifierExtensions();
//END SNIPPET: parseExtension

}


public void foo() {
//START SNIPPET: subExtension
Patient patient = new Patient();

// Add an extension (initially with no contents) to the resource 
ExtensionDt parent = new ExtensionDt(false, "http://example.com#parent");
patient.addUndeclaredExtension(parent);

// Add two extensions as children to the parent extension
ExtensionDt child1 = new ExtensionDt(false, "http://example.com#childOne", new StringDt("value1"));
parent.addUndeclaredExtension(child1);

ExtensionDt child2 = new ExtensionDt(false, "http://example.com#childTwo", new StringDt("value1"));
parent.addUndeclaredExtension(child2);
//END SNIPPET: subExtension
	
}

}
