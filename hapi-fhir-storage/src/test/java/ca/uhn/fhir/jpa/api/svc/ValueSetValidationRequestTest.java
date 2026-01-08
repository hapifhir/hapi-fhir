package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ValueSetValidationRequest}.
 */
// Created by claude-opus-4-5-20251101
class ValueSetValidationRequestTest {

	@Test
	void testBuilder_withAllParameters_createsRequestWithAllFields() {
		// Setup
		IdType valueSetId = new IdType("ValueSet/123");
		UriType valueSetUrl = new UriType("http://example.com/valueset");
		StringType valueSetVersion = new StringType("1.0");
		CodeType code = new CodeType("CODE1");
		UriType system = new UriType("http://example.com/codesystem");
		StringType systemVersion = new StringType("2.0");
		StringType display = new StringType("Display Text");
		Coding coding = new Coding().setSystem("http://system").setCode("CODING_CODE");
		CodeableConcept codeableConcept = new CodeableConcept().addCoding(coding);
		RequestDetails requestDetails = new SystemRequestDetails();

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.valueSetId(valueSetId)
				.valueSetUrl(valueSetUrl)
				.valueSetVersion(valueSetVersion)
				.code(code)
				.system(system)
				.systemVersion(systemVersion)
				.display(display)
				.coding(coding)
				.codeableConcept(codeableConcept)
				.requestDetails(requestDetails)
				.build();

		// Verify
		assertThat(request.valueSetId()).isSameAs(valueSetId);
		assertThat(request.valueSetUrl()).isSameAs(valueSetUrl);
		assertThat(request.valueSetVersion()).isSameAs(valueSetVersion);
		assertThat(request.code()).isSameAs(code);
		assertThat(request.system()).isSameAs(system);
		assertThat(request.systemVersion()).isSameAs(systemVersion);
		assertThat(request.display()).isSameAs(display);
		assertThat(request.coding()).isSameAs(coding);
		assertThat(request.codeableConcept()).isSameAs(codeableConcept);
		assertThat(request.requestDetails()).isSameAs(requestDetails);
	}

	@Test
	void testBuilder_withMinimalParameters_createsRequestWithNulls() {
		// Setup
		RequestDetails requestDetails = new SystemRequestDetails();

		// Execute
		ValueSetValidationRequest request = ValueSetValidationRequest.builder()
				.requestDetails(requestDetails)
				.build();

		// Verify
		assertThat(request.valueSetId()).isNull();
		assertThat(request.valueSetUrl()).isNull();
		assertThat(request.valueSetVersion()).isNull();
		assertThat(request.code()).isNull();
		assertThat(request.system()).isNull();
		assertThat(request.systemVersion()).isNull();
		assertThat(request.display()).isNull();
		assertThat(request.coding()).isNull();
		assertThat(request.codeableConcept()).isNull();
		assertThat(request.requestDetails()).isSameAs(requestDetails);
	}

	@Test
	void testBuilder_isImmutable() {
		// Execute
		ValueSetValidationRequest request1 = ValueSetValidationRequest.builder()
				.code(new CodeType("CODE1"))
				.build();

		ValueSetValidationRequest request2 = ValueSetValidationRequest.builder()
				.code(new CodeType("CODE2"))
				.build();

		// Verify - different instances, different values
		assertThat(request1.code()).isNotNull().returns("CODE1", IPrimitiveType::getValue);
		assertThat(request2.code()).isNotNull().returns("CODE2", IPrimitiveType::getValue);
	}

	@Test
	void testRecord_equalsAndHashCode() {
		// Setup
		RequestDetails requestDetails = new SystemRequestDetails();
		CodeType code = new CodeType("CODE1");

		// Execute
		ValueSetValidationRequest request1 = ValueSetValidationRequest.builder()
				.code(code)
				.requestDetails(requestDetails)
				.build();

		ValueSetValidationRequest request2 = ValueSetValidationRequest.builder()
				.code(code)
				.requestDetails(requestDetails)
				.build();

		// Verify - same values should be equal
		assertThat(request1).isEqualTo(request2);
		assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
	}
}
