/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;

public class ResourceRefs {

	public static void main(String[] args) {
		manualContained();
	}

	public static void manualContained() {
		// START SNIPPET: manualContained
		// Create a practitioner resource, and give it a local ID. This ID must be
		// unique within the containing resource, but does not need to be otherwise
		// unique.
		Practitioner pract = new Practitioner();
		pract.setId("my-practitioner");
		pract.addName().setFamily("Smith").addGiven("Juanita");
		pract.addTelecom().setValue("+1 (289) 555-1234");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("http://example.com/mrns").setValue("253345");

		// Set the reference, and manually add the contained resource
		patient.addGeneralPractitioner(new Reference("#my-practitioner"));
		patient.getContained().add(pract);

		String encoded = FhirContext.forR4Cached().newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		System.out.println(encoded);
		// END SNIPPET: manualContained
	}
}
