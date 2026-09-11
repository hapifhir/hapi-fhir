/*-
 * #%L
 * HAPI FHIR JPA Server Test R4
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is not JPA-backed; it lives alongside {@link ca.uhn.fhir.jpa.provider.r4.AuthorizationInterceptorWriteResponseJpaR4Test},
 * the existing {@code HttpTestRequest} consumer in this module, because {@code hapi-fhir-test-utilities}
 * cannot itself depend on a real FHIR structures JAR without creating a circular module dependency.
 */
// Created by claude-sonnet-5
class RestfulServerExtensionTest {

	@RegisterExtension
	private static final RestfulServerExtension ourServer =
			new RestfulServerExtension(FhirContext.forR4Cached(), new PatientProvider());

	@Test
	void fhirRequest_get_targetsServerBaseUrl() {
		HttpTestResponse response = ourServer.fhirRequest("/Patient/123").get();

		response.assertStatus(200);
		assertThat(response.getBody()).contains("123");
	}

	@Test
	void fhirRequest_postResource_encodesUsingServerFhirContext() {
		Patient patient = new Patient();
		patient.setActive(true);

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(patient);

		response.assertStatus(201);
	}

	public static class PatientProvider {

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient patient = new Patient();
			patient.setId(theId.getIdPart());
			patient.setActive(true);
			return patient;
		}

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			return new MethodOutcome(new IdType("Patient", "1"), true);
		}
	}
}
