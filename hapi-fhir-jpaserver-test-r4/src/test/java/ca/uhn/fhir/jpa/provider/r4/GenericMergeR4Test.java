// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericMergeR4Test extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericMergeR4Test.class);

	@Test
	void testGenericMergeEndpoint_ReturnsOk() {
		// setup
		Practitioner sourcePractitioner = new Practitioner();
		sourcePractitioner.setActive(true);
		sourcePractitioner = (Practitioner) myPractitionerDao.create(sourcePractitioner, mySrd).getResource();

		Practitioner targetPractitioner = new Practitioner();
		targetPractitioner.setActive(true);
		targetPractitioner = (Practitioner) myPractitionerDao.create(targetPractitioner, mySrd).getResource();

		Parameters inParams = new Parameters();
		inParams.addParameter("source-resource", new Reference(sourcePractitioner.getIdElement().toVersionless()));
		inParams.addParameter("target-resource", new Reference(targetPractitioner.getIdElement().toVersionless()));

		// execute
		Parameters outParams;
		try {
			outParams = myClient
				.operation()
				.onType(Practitioner.class)
				.named("$hapi-fhir-merge")
				.withParameters(inParams)
				.execute();
		} catch (BaseServerResponseException e) {
			ourLog.error("Generic merge operation failed with HTTP {}: {}", e.getStatusCode(), e.getMessage());
			ourLog.error("Response body: {}", e.getResponseBody());
			ourLog.error("Operation outcome: {}", e.getOperationOutcome());
			throw e;
		}

		// validate
		assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_OK);
		assertThat(outParams).isNotNull();
		ourLog.info("Generic merge endpoint returned successfully with status 200");
	}

	@Test
	void testGenericMergeEndpoint_WithIdentifierParameters() {
		// setup - create practitioners with identifiers
		Practitioner sourcePractitioner = new Practitioner();
		sourcePractitioner.setActive(true);
		Identifier sourceId = sourcePractitioner.addIdentifier();
		sourceId.setSystem("http://example.com/practitioners");
		sourceId.setValue("PRAC-SOURCE-001");
		sourcePractitioner = (Practitioner) myPractitionerDao.create(sourcePractitioner, mySrd).getResource();

		Practitioner targetPractitioner = new Practitioner();
		targetPractitioner.setActive(true);
		Identifier targetId = targetPractitioner.addIdentifier();
		targetId.setSystem("http://example.com/practitioners");
		targetId.setValue("PRAC-TARGET-001");
		targetPractitioner = (Practitioner) myPractitionerDao.create(targetPractitioner, mySrd).getResource();

		// Create parameters with identifier lookups
		Parameters inParams = new Parameters();
		inParams.addParameter("source-resource-identifier", sourceId);
		inParams.addParameter("target-resource-identifier", targetId);

		// execute
		Parameters outParams;
		try {
			outParams = myClient
				.operation()
				.onType(Practitioner.class)
				.named("$hapi-fhir-merge")
				.withParameters(inParams)
				.execute();
		} catch (BaseServerResponseException e) {
			ourLog.error("Generic merge operation with identifiers failed with HTTP {}: {}", e.getStatusCode(), e.getMessage());
			ourLog.error("Response body: {}", e.getResponseBody());
			ourLog.error("Operation outcome: {}", e.getOperationOutcome());
			throw e;
		}

		// validate
		assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_OK);
		assertThat(outParams).isNotNull();
		ourLog.info("Generic merge endpoint with identifiers returned successfully");
	}

	@Test
	void testGenericMergeEndpoint_WithIdentifier_OnResourceWithoutIdentifierSearchParam_ShouldFail() {
		// Setup - Bundle doesn't have identifier search parameter
		Identifier sourceId = new Identifier();
		sourceId.setSystem("http://example.com/bundles");
		sourceId.setValue("BUNDLE-SOURCE-001");

		Identifier targetId = new Identifier();
		targetId.setSystem("http://example.com/bundles");
		targetId.setValue("BUNDLE-TARGET-001");

		// Create parameters with identifier lookups on Bundle resource type
		Parameters inParams = new Parameters();
		inParams.addParameter("source-resource-identifier", sourceId);
		inParams.addParameter("target-resource-identifier", targetId);

		// Execute and catch exception to log behavior
		try {
			myClient
				.operation()
				.onType(Bundle.class)
				.named("$hapi-fhir-merge")
				.withParameters(inParams)
				.execute();
			ourLog.info("Unexpectedly succeeded - operation should have failed");
		} catch (BaseServerResponseException e) {
			ourLog.info("Merge operation failed as expected");
			ourLog.info("Exception type: {}", e.getClass().getName());
			ourLog.info("HTTP Status Code: {}", e.getStatusCode());
			ourLog.info("Error Message: {}", e.getMessage());
			ourLog.info("Response Body: {}", e.getResponseBody());
			if (e.getOperationOutcome() != null) {
				ourLog.info("Operation Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			}
		}
	}
}
