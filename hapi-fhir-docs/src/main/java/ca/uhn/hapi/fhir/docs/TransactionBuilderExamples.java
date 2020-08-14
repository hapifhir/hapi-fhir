package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.TransactionBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Patient;

@SuppressWarnings("unused")
public class TransactionBuilderExamples {

	private FhirContext myFhirContext;
	private IGenericClient myFhirClient;

	public void update() throws FHIRException {
		//START SNIPPET: update
		// Create a TransactionBuilder
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		// Create a Patient to update
		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);

		// Add the patient as an update (aka PUT) to the Bundle
		builder.addUpdateEntry(patient);

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: update
	}

	public void updateConditional() throws FHIRException {
		//START SNIPPET: updateConditional
		// Create a TransactionBuilder
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		// Create a Patient to update
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://foo").setValue("bar");

		// Add the patient as an update (aka PUT) to the Bundle
		builder.addUpdateEntry(patient).conditional("Patient?identifier=http://foo|bar");

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: updateConditional
	}

	public void create() throws FHIRException {
		//START SNIPPET: create
		// Create a TransactionBuilder
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		// Create a Patient to create
		Patient patient = new Patient();
		patient.setActive(true);

		// Add the patient as a create (aka POST) to the Bundle
		builder.addCreateEntry(patient);

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: create
	}

	public void createConditional() throws FHIRException {
		//START SNIPPET: createConditional
		// Create a TransactionBuilder
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		// Create a Patient to create
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://foo").setValue("bar");

		// Add the patient as a create (aka POST) to the Bundle
		builder.addCreateEntry(patient).conditional("Patient?identifier=http://foo|bar");

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: createConditional
	}

}
