package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderR4StructureDefinitionTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4StructureDefinitionTest.class);

	@Test
	public void testSearchAllStructureDefinitions() throws IOException {
		StructureDefinition sd = loadResource(myFhirContext, StructureDefinition.class, "/r4/sd-david-dhtest7.json");
		myStructureDefinitionDao.update(sd);

		Bundle response = myClient
			.search()
			.forResource(StructureDefinition.class)
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals(1, response.getEntry().size());
		assertEquals("dhtest7", response.getEntry().get(0).getResource().getIdElement().getIdPart());
	}

	@Test
	public void testSnapshotWithResourceParameter() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");

		StructureDefinition response = myClient
			.operation()
			.onType(StructureDefinition.class)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withParameter(Parameters.class, "definition", sd)
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertEquals(54, response.getSnapshot().getElement().size());
	}

	@Test
	public void testSnapshotWithId() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		IIdType id = myClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = myClient
			.operation()
			.onInstance(id)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withNoParameters(Parameters.class)
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertEquals(54, response.getSnapshot().getElement().size());
	}

	@Test
	public void testSnapshotWithUrl() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		IIdType id = myClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = myClient
			.operation()
			.onType(StructureDefinition.class)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withParameter(Parameters.class, "url", new StringType("http://example.com/fhir/StructureDefinition/patient-1a-extensions"))
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertEquals(54, response.getSnapshot().getElement().size());
	}

	@Test
	public void testSnapshotWithUrlAndId() {
		try {
			myClient
				.operation()
				.onInstance(new IdType("StructureDefinition/123"))
				.named(JpaConstants.OPERATION_SNAPSHOT)
				.withParameter(Parameters.class, "url", new StringType("http://example.com/fhir/StructureDefinition/patient-1a-extensions"))
				.returnResourceType(StructureDefinition.class)
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1770) + "Must supply either an ID or a StructureDefinition or a URL (but not more than one of these things)", e.getMessage());
		}
	}

	@Test
	public void testSnapshotWithInvalidUrl() {
		try {
			myClient
				.operation()
				.onType(StructureDefinition.class)
				.named(JpaConstants.OPERATION_SNAPSHOT)
				.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/FOO"))
				.returnResourceType(StructureDefinition.class)
				.execute();
		} catch (ResourceNotFoundException e) {
			assertEquals("HTTP 404 Not Found: " + Msg.code(1159) + "No StructureDefiniton found with url = 'http://hl7.org/fhir/StructureDefinition/FOO'", e.getMessage());
		}
	}

}
