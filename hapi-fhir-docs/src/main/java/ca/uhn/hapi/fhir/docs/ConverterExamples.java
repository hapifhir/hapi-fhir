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

import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_30;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_14_30;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.exceptions.FHIRException;

public class ConverterExamples {

	@SuppressWarnings("unused")
	public void c1020() throws FHIRException {
		//START SNIPPET: 1020
		// Create an input resource to convert
		org.hl7.fhir.dstu2.model.Observation input = new org.hl7.fhir.dstu2.model.Observation();
		input.setEncounter(new org.hl7.fhir.dstu2.model.Reference("Encounter/123"));

		// Convert the resource
		org.hl7.fhir.dstu3.model.Observation output = (Observation) VersionConvertorFactory_10_30.convertResource(input);
		String context = output.getContext().getReference();
		//END SNIPPET: 1020
	}

	@SuppressWarnings("unused")
	public void c1420() throws FHIRException {
		//START SNIPPET: 1420
		// Create a resource to convert
		org.hl7.fhir.dstu2016may.model.Questionnaire input = new org.hl7.fhir.dstu2016may.model.Questionnaire();
		input.setTitle("My title");

		// Convert the resource
		org.hl7.fhir.dstu3.model.Questionnaire output = (Questionnaire) VersionConvertorFactory_14_30.convertResource(input);
		String context = output.getTitle();
		//END SNIPPET: 1420
	}

}
