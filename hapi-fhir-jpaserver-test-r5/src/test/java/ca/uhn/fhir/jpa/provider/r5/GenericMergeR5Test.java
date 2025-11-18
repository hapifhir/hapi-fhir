// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericMergeR5Test extends BaseResourceProviderR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericMergeR5Test.class);

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
		inParams.addParameter(ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_RESOURCE, new Reference(sourcePractitioner.getIdElement().toVersionless()));
		inParams.addParameter(ProviderConstants.OPERATION_MERGE_PARAM_TARGET_RESOURCE, new Reference(targetPractitioner.getIdElement().toVersionless()));

		// execute
		Parameters outParams = myClient
			.operation()
			.onType(Practitioner.class)
			.named("$hapi-fhir-merge")
			.withParameters(inParams)
			.execute();

		// validate
		assertThat(outParams).isNotNull();
		ourLog.info("Generic merge endpoint returned successfully for R5 Practitioner");
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
		inParams.addParameter(ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_RESOURCE_IDENTIFIER, sourceId);
		inParams.addParameter(ProviderConstants.OPERATION_MERGE_PARAM_TARGET_RESOURCE_IDENTIFIER, targetId);

		// execute
		Parameters outParams = myClient
			.operation()
			.onType(Practitioner.class)
			.named("$hapi-fhir-merge")
			.withParameters(inParams)
			.execute();

		// validate
		assertThat(outParams).isNotNull();
		ourLog.info("Generic merge endpoint with identifiers returned successfully for R5");
	}
}
