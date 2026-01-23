/*-
 * #%L
 * hapi-fhir-spring-boot-sample-server-jersey
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
package sample.fhir.server.plain.provider;

import java.util.HashMap;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

@Component
public class PatientResourceProvider implements IResourceProvider {

	private static final HashMap<String, Patient> patients = new HashMap<>();

	static {
		patients.put(String.valueOf(1L), createPatient("1", "Doe", "Jane"));
		patients.put(String.valueOf(2L), createPatient("2", "Doe", "John"));
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Read
	public Patient find(@IdParam final IdType theId) {
		if (patients.containsKey(theId.getIdPart())) {
			return patients.get(theId.getIdPart());
		} else {
			throw new ResourceNotFoundException(Msg.code(2005) + theId);
		}
	}

	private static Patient createPatient(final String id, final String family, final String given) {
		final Patient patient = new Patient();
		patient.getName().add(new HumanName().setFamily(family).addGiven(given));
		patient.setId(id);
		return patient;
	}

}
