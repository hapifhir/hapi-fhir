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

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

import java.util.Collections;
import java.util.List;

//START SNIPPET: provider

/**
 * All resource providers must implement IResourceProvider
 */
public class RestfulObservationResourceProvider implements IResourceProvider {

	/**
	 * The getResourceType method comes from IResourceProvider, and must
	 * be overridden to indicate what type of resource this provider
	 * supplies.
	 */
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
	
	/**
	 * The "@Read" annotation indicates that this method supports the
	 * read operation. It takes one argument, the Resource type being returned. 
	 * 
	 * @param theId
	 *    The read operation takes one parameter, which must be of type
	 *    IdType and must be annotated with the "@Read.IdParam" annotation.
	 * @return 
	 *    Returns a resource matching this identifier, or null if none exists.
	 */
	@Read()
	public Patient getResourceById(@IdParam IdType theId) {
		Patient patient = new Patient();
		patient.addIdentifier();
		patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
		patient.getIdentifier().get(0).setValue("00002");
		patient.addName().setFamily("Test");
		patient.getName().get(0).addGiven("PatientOne");
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		return patient;
	}

	/**
	 * The "@Search" annotation indicates that this method supports the 
	 * search operation. You may have many different methods annotated with
	 * this annotation, to support many different search criteria. This
	 * example searches by family name.
	 * 
	 * @param theFamilyName
	 *    This operation takes one parameter which is the search criteria. It is
	 *    annotated with the "@Required" annotation. This annotation takes one argument,
	 *    a string containing the name of the search criteria. The datatype here
	 *    is StringDt, but there are other possible parameter types depending on the
	 *    specific search criteria.
	 * @return
	 *    This method returns a list of Patients. This list may contain multiple
	 *    matching resources, or it may also be empty.
	 */
	@Search()
	public List<Patient> getPatient(@RequiredParam(name = Patient.SP_FAMILY) StringDt theFamilyName) {
		Patient patient = new Patient();
		patient.addIdentifier();
		patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
		patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
		patient.getIdentifier().get(0).setValue("00001");
		patient.addName();
		patient.getName().get(0).setFamily("Test");
		patient.getName().get(0).addGiven("PatientOne");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		return Collections.singletonList(patient);
	}

}
//END SNIPPET: provider
