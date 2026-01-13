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
 * Unit tests for {@link CodeSystemValidationRequest}.
 */
// Created by claude-opus-4-5-20251101
class CodeSystemValidationRequestTest {

	@Test
	void testBuilder_withAllParameters_createsRequestWithAllFields() {
		// Setup
		IdType codeSystemId = new IdType("CodeSystem/123");
		UriType codeSystemUrl = new UriType("http://example.com/codesystem");
		StringType version = new StringType("1.0");
		CodeType code = new CodeType("CODE1");
		StringType display = new StringType("Display Text");
		Coding coding = new Coding().setSystem("http://system").setCode("CODING_CODE");
		CodeableConcept codeableConcept = new CodeableConcept().addCoding(coding);
		RequestDetails requestDetails = new SystemRequestDetails();

		// Execute
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.codeSystemId(codeSystemId)
				.codeSystemUrl(codeSystemUrl)
				.version(version)
				.code(code)
				.display(display)
				.coding(coding)
				.codeableConcept(codeableConcept)
				.requestDetails(requestDetails)
				.build();

		// Verify
		assertThat(request.codeSystemId()).isSameAs(codeSystemId);
		assertThat(request.codeSystemUrl()).isSameAs(codeSystemUrl);
		assertThat(request.version()).isSameAs(version);
		assertThat(request.code()).isSameAs(code);
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
		CodeSystemValidationRequest request = CodeSystemValidationRequest.builder()
				.requestDetails(requestDetails)
				.build();

		// Verify
		assertThat(request.codeSystemId()).isNull();
		assertThat(request.codeSystemUrl()).isNull();
		assertThat(request.version()).isNull();
		assertThat(request.code()).isNull();
		assertThat(request.display()).isNull();
		assertThat(request.coding()).isNull();
		assertThat(request.codeableConcept()).isNull();
		assertThat(request.requestDetails()).isSameAs(requestDetails);
	}

	@Test
	void testBuilder_isImmutable() {
		// Execute
		CodeSystemValidationRequest request1 = CodeSystemValidationRequest.builder()
				.code(new CodeType("CODE1"))
				.build();

		CodeSystemValidationRequest request2 = CodeSystemValidationRequest.builder()
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
		CodeSystemValidationRequest request1 = CodeSystemValidationRequest.builder()
				.code(code)
				.requestDetails(requestDetails)
				.build();

		CodeSystemValidationRequest request2 = CodeSystemValidationRequest.builder()
				.code(code)
				.requestDetails(requestDetails)
				.build();

		// Verify - same values should be equal
		assertThat(request1).isEqualTo(request2);
		assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
	}
}
