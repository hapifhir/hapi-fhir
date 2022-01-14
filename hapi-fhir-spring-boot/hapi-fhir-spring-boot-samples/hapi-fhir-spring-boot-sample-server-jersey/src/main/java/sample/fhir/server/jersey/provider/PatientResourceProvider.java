package sample.fhir.server.jersey.provider;

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

import ca.uhn.fhir.i18n.Msg;
import java.util.concurrent.ConcurrentHashMap;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;

import org.springframework.stereotype.Component;

@Component
public class PatientResourceProvider extends AbstractJaxRsResourceProvider<Patient> {

    private static Long counter = 1L;

    private static final ConcurrentHashMap<String, Patient> patients = new ConcurrentHashMap<>();

    static {
        patients.put(String.valueOf(counter), createPatient("Van Houte"));
        patients.put(String.valueOf(counter), createPatient("Agnew"));
        for (int i = 0; i < 20; i++) {
            patients.put(String.valueOf(counter), createPatient("Random Patient " + counter));
        }
    }

    public PatientResourceProvider(FhirContext fhirContext) {
        super(fhirContext);
    }

    @Read
    public Patient find(@IdParam final IdType theId) {
        if (patients.containsKey(theId.getIdPart())) {
            return patients.get(theId.getIdPart());
        } else {
            throw new ResourceNotFoundException(Msg.code(2005) + theId);
        }
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient patient) {

        patient.setId(createId(counter, 1L));
        patients.put(String.valueOf(counter), patient);

        return new MethodOutcome(patient.getIdElement());
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    private static IdType createId(final Long id, final Long theVersionId) {
        return new IdType("Patient", "" + id, "" + theVersionId);
    }

    private static Patient createPatient(final String name) {
        final Patient patient = new Patient();
        patient.getName().add(new HumanName().setFamily(name));
        patient.setId(createId(counter, 1L));
        counter++;
        return patient;
    }

}
