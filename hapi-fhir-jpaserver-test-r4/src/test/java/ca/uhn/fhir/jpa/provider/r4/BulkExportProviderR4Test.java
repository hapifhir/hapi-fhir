package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_EXPORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BulkExportProviderR4Test extends BaseResourceProviderR4Test {
	@Test
	void testBulkExport_groupNotExists_throws404() {
	    // given no data

		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
			() -> myClient
				.operation().onInstance("Group/ABC_not_exist").named(OPERATION_EXPORT)
				.withNoParameters(Parameters.class)
				.withAdditionalHeader("Prefer", "respond-async")
				.returnMethodOutcome()
				.execute(),
			"$export of missing Group throws 404");

		assertEquals(404, e.getStatusCode());
	}

	@Test
	void testBulkExport_patientNotExists_throws404() {
		// given no data

		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
			() -> myClient
				.operation().onInstance("Patient/ABC_not_exist").named(OPERATION_EXPORT)
				.withNoParameters(Parameters.class)
				.withAdditionalHeader("Prefer", "respond-async")
				.returnMethodOutcome()
				.execute(),
			"$export of missing Patient throws 404");

		assertEquals(404, e.getStatusCode());
	}
	@Test
	void testBulkExport_typePatientIdNotExists_throws404() {
		// given no data

		ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
			() -> myClient
				.operation().onType("Patient").named(OPERATION_EXPORT)
				.withParameter(Parameters.class, "patient", new StringType("Patient/abc-no-way"))
				.withAdditionalHeader("Prefer", "respond-async")
				.returnMethodOutcome()
				.execute(),
			"Patient/$export with missing patient throws 404");

		assertEquals(404, e.getStatusCode());
	}
}
