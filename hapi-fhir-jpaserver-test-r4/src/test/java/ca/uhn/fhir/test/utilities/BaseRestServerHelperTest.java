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
package ca.uhn.fhir.test.utilities;

import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers {@link BaseRestServerHelper#fhirRequest(String)} through its concrete subclass. Like
 * {@link ca.uhn.fhir.test.utilities.server.RestfulServerExtensionTest}, this lives here rather than
 * beside the class under test because {@code hapi-fhir-test-utilities} cannot depend on a real FHIR
 * structures JAR without creating a circular module dependency.
 */
// Created by claude-opus-5
class BaseRestServerHelperTest {

	@RegisterExtension
	public final RestServerR4Helper myHelper = RestServerR4Helper.newInitialized();

	@Test
	void fhirRequest_get_targetsThePlainHttpBaseUrl() {
		HttpTestResponse response = myHelper.fhirRequest("/metadata").get();

		response.assertStatus(200);
		assertThat(response.getBody()).contains("CapabilityStatement");
	}

	@Test
	void fhirRequest_postResource_encodesUsingTheHelperFhirContext() {
		Patient patient = new Patient();
		patient.setActive(true);

		HttpTestResponse response = myHelper.fhirRequest("/Patient").post(patient);

		response.assertStatus(201);
		assertThat(response.getHeader("Location")).contains("Patient/");
	}
}
