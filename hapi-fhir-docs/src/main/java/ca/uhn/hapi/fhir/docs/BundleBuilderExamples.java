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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Patient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("unused")
public class BundleBuilderExamples {

	private FhirContext myFhirContext;
	private IGenericClient myFhirClient;

	public void update() throws FHIRException {
		//START SNIPPET: update
		// Create a TransactionBuilder
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		// Create a Patient to update
		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);

		// Add the patient as an update (aka PUT) to the Bundle
		builder.addTransactionUpdateEntry(patient);

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: update
	}

	public void updateConditional() throws FHIRException {
		//START SNIPPET: updateConditional
		// Create a TransactionBuilder
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		// Create a Patient to update
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://foo").setValue("bar");

		// Add the patient as an update (aka PUT) to the Bundle
		builder.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://foo|bar");

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: updateConditional
	}

	public void create() throws FHIRException {
		//START SNIPPET: create
		// Create a TransactionBuilder
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		// Create a Patient to create
		Patient patient = new Patient();
		patient.setActive(true);

		// Add the patient as a create (aka POST) to the Bundle
		builder.addTransactionCreateEntry(patient);

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: create
	}

	public void createConditional() throws FHIRException {
		//START SNIPPET: createConditional
		// Create a TransactionBuilder
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		// Create a Patient to create
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://foo").setValue("bar");

		// Add the patient as a create (aka POST) to the Bundle
		builder.addTransactionCreateEntry(patient).conditional("Patient?identifier=http://foo|bar");

		// Execute the transaction
		IBaseBundle outcome = myFhirClient.transaction().withBundle(builder.getBundle()).execute();
		//END SNIPPET: createConditional
	}

	public void customizeBundle() throws FHIRException {
		//START SNIPPET: customizeBundle
		// Create a TransactionBuilder
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		// Set bundle type to be searchset
		builder
			.setBundleField("type", "searchset")
			.setBundleField("id", UUID.randomUUID().toString())
			.setMetaField("lastUpdated", builder.newPrimitive("instant", new Date()));

		// Create bundle entry
		IBase entry = builder.addEntry();

		// Create a Patient to create
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://foo").setValue("bar");
		builder.addToEntry(entry, "resource", patient);

		// Add search results
		IBase search = builder.addSearch(entry);
		builder.setSearchField(search, "mode", "match");
		builder.setSearchField(search, "score", builder.newPrimitive("decimal", BigDecimal.ONE));
		//END SNIPPET: customizeBundle
	}

}
