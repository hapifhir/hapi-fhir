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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@Test
	void getBodyAsResource_jsonResponse_parsesWithServerFhirContext() {
		Patient patient = ourServer.fhirRequest("/Patient/123").get()
				.assertStatus(200)
				.getBodyAsResource(Patient.class);

		assertThat(patient.getIdElement().getIdPart()).isEqualTo("123");
		assertThat(patient.getActive()).isTrue();
	}

	@Test
	void getBodyAsResource_xmlResponse_picksParserFromContentType() {
		Patient patient = ourServer.fhirRequest("/Patient/123")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW)
				.get()
				.assertContentType(Constants.CT_FHIR_XML_NEW)
				.getBodyAsResource(Patient.class);

		assertThat(patient.getIdElement().getIdPart()).isEqualTo("123");
	}

	@Test
	void getBodyAsResource_errorResponse_parsesOperationOutcome() {
		OperationOutcome outcome = ourServer.fhirRequest("/Foo/123").get()
				.assertStatus(404)
				.getBodyAsResource(OperationOutcome.class);

		assertThat(outcome.getIssueFirstRep().getDiagnostics()).contains("Unknown resource type 'Foo'");
	}

	@Test
	void getBodyAsResource_bodyIsAnotherResourceType_failsWithBodyInMessage() {
		HttpTestResponse response = ourServer.fhirRequest("/Patient/123").get();

		assertThatThrownBy(() -> response.getBodyAsResource(OperationOutcome.class))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("OperationOutcome")
				.hasMessageContaining("Response body:")
				.hasMessageContaining("\"Patient\"");
	}

	@Test
	void getLocationId_create_returnsIdOfCreatedResource() {
		IIdType id = ourServer.fhirRequest("/Patient").post(new Patient())
				.assertStatus(201)
				.getLocationId();

		assertThat(id.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/1");
	}

	@Test
	void fhirRequestToUrl_absoluteUrl_requestsItWithServerClientAndFhirContext() {
		Patient patient = ourServer.fhirRequestToUrl(ourServer.getBaseUrl() + "/Patient/456").get()
				.assertStatus(200)
				.getBodyAsResource(Patient.class);

		assertThat(patient.getIdElement().getIdPart()).isEqualTo("456");
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
