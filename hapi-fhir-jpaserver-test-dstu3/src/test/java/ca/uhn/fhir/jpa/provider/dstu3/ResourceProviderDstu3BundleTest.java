package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
			ourClient.operation().onServer().named(JpaConstants.OPERATION_PROCESS_MESSAGE).withParameters(parameters).execute();
			fail();
		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage(), containsString("This operation is not yet implemented on this server"));
		}
	}

	@Test
	public void testConformanceContainsIncludesAndRevIncludes() {
		CapabilityStatement execute = ourClient.capabilities().ofType(CapabilityStatement.class).execute();
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
	void testTransactionBundleEntryLinks() {
		CarePlan carePlan = new CarePlan();
		carePlan.getText().setDivAsString("A CarePlan");
//		client.create().resource(carePlan).prefer(PreferReturnEnum.REPRESENTATION).execute();
		ourClient.create().resource(carePlan).execute();
		ourClient.registerInterceptor(new LoggingInterceptor(true));

		// GET CarePlans from server
		Bundle bundle = ourClient.search()
			.byUrl(ourServerBase + "/CarePlan")
			.returnBundle(Bundle.class).execute();

		// Create and populate list of CarePlans
		List<CarePlan> carePlans = new ArrayList<>();
		bundle.getEntry().forEach(entry -> carePlans.add((CarePlan) entry.getResource()));

		Bundle b1 = new Bundle();
		b1.setType(Bundle.BundleType.TRANSACTION);
		for (CarePlan cp  : carePlans) {
			Bundle.BundleEntryComponent entry = b1.addEntry();
			entry.setResource(cp);
			Bundle.BundleEntryRequestComponent request = new Bundle.BundleEntryRequestComponent();
			request.setUrl("Careplan/" + cp.getIdElement().getIdPart());
			request.setMethod(Bundle.HTTPVerb.PUT);
			entry.setRequest(request);
		}

		ourClient.transaction().withBundle(b1).execute();
		ourLog.error("*******************************");
		// Post CarePlans
		ourClient.transaction().withResources(carePlans).execute();

	}


}
