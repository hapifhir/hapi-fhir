package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BulkPatchJobParametersValidatorTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private BulkPatchJobParametersValidator mySvc;

	@BeforeEach
	public void beforeEach() {
		mySvc = new BulkPatchJobParametersValidator(ourCtx);
	}

	@Test
	void testValidate_NoPatchSpecified() {
		BulkPatchJobParameters params = new BulkPatchJobParameters();

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("No Patch document was provided");
	}

	@Test
	void testValidate_InvalidPatchSpecified_Syntax() {
		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.setFhirPatch("A");

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Failed to parse FHIRPatch document: HAPI-1861: Failed to parse JSON encoded FHIR content: HAPI-1859: Content does not appear to be FHIR JSON, first non-whitespace character was: 'A' (must be '{')");
	}

	@Test
	void testValidate_InvalidPatchSpecified_BadDocument() {
		Parameters patch = new Parameters();
		patch.addParameter("foo", new StringType("bar"));

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.setFhirPatch(ourCtx, patch);

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Provided FHIRPatch document is invalid: HAPI-2756: Unknown patch parameter name: foo");
	}

	@Test
	void testValidate_Good() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.setFhirPatch(ourCtx, patch);

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).isEmpty();
	}

}
