package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkPatchJobParametersValidatorTest {

	@Mock
	private IDaoRegistry myDaoRegistry;

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private BulkPatchJobParametersValidator<BulkPatchJobParameters> mySvc;

	@BeforeEach
	public void beforeEach() {
		mySvc = new BulkPatchJobParametersValidator<>(ourCtx, myDaoRegistry);
	}

	@Test
	void testValidate_NoPatchSpecified() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Patient?"));

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("No Patch document was provided");
	}

	@Test
	void testValidate_NoUrls() {
		Parameters patch = createGoodPatch();

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.setFhirPatch(ourCtx, patch);

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("No URLs were provided");
	}

	@Test
	void testValidate_InvalidPatchSpecified_Syntax() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Patient?"));
		params.setFhirPatch("A");

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Failed to parse FHIRPatch document: HAPI-1861: Failed to parse JSON encoded FHIR content: HAPI-1859: Content does not appear to be FHIR JSON, first non-whitespace character was: 'A' (must be '{')");
	}

	@Test
	void testValidate_InvalidPatchSpecified_BadDocument() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		Parameters patch = new Parameters();
		patch.addParameter("foo", new StringType("bar"));

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Patient?"));
		params.setFhirPatch(ourCtx, patch);

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Provided FHIRPatch document is invalid: HAPI-2756: Unknown patch parameter name: foo");
	}

	@Test
	void testValidate_InvalidRequestUrl_NoUrl() {
		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl());

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("No URLs were provided");
	}

	@Test
	void testValidate_InvalidRequestUrl_InvalidUrl() {
		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Patient=1"));

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Invalid/unsupported URL (must use syntax '{resourceType}?[optional params]': Patient=1");
	}

	@Test
	void testValidate_InvalidRequestUrl_InvalidResourceType() {
		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Foo?"));

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).contains("Resource type Foo is not supported");
	}

	@Test
	void testValidate_Good() {
		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);

		BulkPatchJobParameters params = new BulkPatchJobParameters();
		params.addPartitionedUrl(new PartitionedUrl().setUrl("Patient?"));
		params.setFhirPatch(ourCtx, createGoodPatch());

		List<String> outcome = mySvc.validate(null, params);
		assertThat(outcome).isEmpty();
	}

	@Nonnull
	private static Parameters createGoodPatch() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));
		return patch;
	}

}
