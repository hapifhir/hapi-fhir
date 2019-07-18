package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ResourceProviderDstu3StructureDefinitionTest extends BaseResourceProviderDstu3Test {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testSnapshotWithResourceParameter() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");

		StructureDefinition response = ourClient
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
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		IIdType id = ourClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = ourClient
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
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		IIdType id = ourClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = ourClient
			.operation()
			.onType(StructureDefinition.class)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/MyPatient421"))
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertEquals(54, response.getSnapshot().getElement().size());
	}

	@Test
	public void testSnapshotWithUrlAndId() {
		try {
			ourClient
				.operation()
				.onInstance(new IdType("StructureDefinition/123"))
				.named(JpaConstants.OPERATION_SNAPSHOT)
				.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/MyPatient421"))
				.returnResourceType(StructureDefinition.class)
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Must supply either an ID or a StructureDefinition or a URL (but not more than one of these things)", e.getMessage());
		}
	}

	@Test
	public void testSnapshotWithInvalidUrl() {
		try {
			ourClient
				.operation()
				.onType(StructureDefinition.class)
				.named(JpaConstants.OPERATION_SNAPSHOT)
				.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/FOO"))
				.returnResourceType(StructureDefinition.class)
				.execute();
		} catch (ResourceNotFoundException e) {
			assertEquals("HTTP 404 Not Found: No StructureDefiniton found with url = 'http://hl7.org/fhir/StructureDefinition/FOO'", e.getMessage());
		}
	}
}
