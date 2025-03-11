package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderDstu3StructureDefinitionTest extends BaseResourceProviderDstu3Test {


	@Test
	public void testSnapshotWithResourceParameter() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");

		StructureDefinition response = myClient
			.operation()
			.onType(StructureDefinition.class)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withParameter(Parameters.class, "definition", sd)
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertThat(response.getSnapshot().getElement()).hasSize(54);
	}


	@Test
	public void testSnapshotWithId() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		IIdType id = myClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = myClient
			.operation()
			.onInstance(id)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withNoParameters(Parameters.class)
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertThat(response.getSnapshot().getElement()).hasSize(54);
	}


	@Test
	public void testSnapshotWithUrl() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		IIdType id = myClient.create().resource(sd).execute().getId().toUnqualifiedVersionless();

		StructureDefinition response = myClient
			.operation()
			.onType(StructureDefinition.class)
			.named(JpaConstants.OPERATION_SNAPSHOT)
			.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/MyPatient421"))
			.returnResourceType(StructureDefinition.class)
			.execute();
		assertThat(response.getSnapshot().getElement()).hasSize(54);
	}

	@Test
	public void testSnapshotWithUrlAndId() {
		try {
			myClient
				.operation()
				.onInstance(new IdType("StructureDefinition/123"))
				.named(JpaConstants.OPERATION_SNAPSHOT)
				.withParameter(Parameters.class, "url", new StringType("http://hl7.org/fhir/StructureDefinition/MyPatient421"))
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
			assertEquals("HTTP 404 Not Found: " + Msg.code(1162) + "No StructureDefiniton found with url = 'http://hl7.org/fhir/StructureDefinition/FOO'", e.getMessage());
		}
	}
}
