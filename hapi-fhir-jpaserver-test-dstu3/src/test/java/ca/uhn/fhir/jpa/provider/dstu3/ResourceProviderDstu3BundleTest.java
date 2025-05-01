package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderDstu3BundleTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3BundleTest.class);

	@Test
	public void testProcessMessage() {

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);
		try {
			myClient.operation().onServer().named(JpaConstants.OPERATION_PROCESS_MESSAGE).withParameters(parameters).execute();
			fail("");
		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage()).contains("This operation is not yet implemented on this server");
		}
	}

	@Test
	public void testConformanceContainsIncludesAndRevIncludes() {
		CapabilityStatement execute = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		Optional<CapabilityStatement.CapabilityStatementRestResourceComponent> patient = execute.getRestFirstRep().getResource().stream().filter(resource -> resource.getType().equalsIgnoreCase("Patient")).findFirst();
		if (patient.isEmpty()) {
			fail("No Patient resource found in conformance statement");
		} else {
			List<StringType> searchInclude = patient.get().getSearchInclude();
			List<StringType> searchRevInclude = patient.get().getSearchRevInclude();

			assertTrue(searchRevInclude.stream().map(PrimitiveType::getValue).anyMatch(stringRevIncludes -> stringRevIncludes.equals("Observation:subject")));
			assertEquals(searchRevInclude.size(), 152);

			assertTrue(searchInclude.stream().map(PrimitiveType::getValue).anyMatch(stringRevIncludes -> stringRevIncludes.equals("Patient:general-practitioner")));
			assertEquals(searchInclude.size(), 4);
		}
	}

	@Test
	void testTransactionBundleEntryUri() {
		CarePlan carePlan = new CarePlan();
		carePlan.getText().setDivAsString("A CarePlan");
		carePlan.setId("ACarePlan");
		myClient.create().resource(carePlan).execute();

		// GET CarePlans from server
		Bundle bundle = myClient.search()
			.byUrl(myServerBase + "/CarePlan")
			.returnBundle(Bundle.class).execute();

		// Create and populate list of CarePlans
		List<CarePlan> carePlans = new ArrayList<>();
		bundle.getEntry().forEach(entry -> carePlans.add((CarePlan) entry.getResource()));

		// Post CarePlans should not get: HAPI-2006: Unable to perform PUT, URL provided is invalid...
		myClient.transaction().withResources(carePlans).execute();
	}


}
