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
