package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceProviderR5ValidationTest extends BaseResourceProviderR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5ValidationTest.class);

	@ParameterizedTest
	@EnumSource(LevelEnum.class)
	void testValidateAgainstProfileSpecifiedInRequest(LevelEnum theLevel) throws IOException {
		// Setup - Create a profile which bans names
		StructureDefinition sd = new StructureDefinition();
		sd.setId("StructureDefinition/profile-noname-patient");
		sd.setUrl("http://profile-noname-patient");
		sd.setType("Patient");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient");
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.getDifferential().addElement().setPath("Patient.name").setMax("0");
		myStructureDefinitionDao.update(sd, mySrd);

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName().setFamily("Simpson"); // Banned by the profile!

		if (theLevel == LevelEnum.INSTANCE) {
			patient.setId("Patient/P");
			myPatientDao.update(patient, mySrd);
		}

		// Test
		HttpUriRequest request = switch (theLevel) {
			case TYPE -> {
				HttpPost post = new HttpPost(myServerBase + "/Patient/$validate?profile=http://profile-noname-patient");
				post.setEntity(new ResourceEntity(myFhirContext, patient));
				yield post;
			}
			case INSTANCE -> new HttpGet(myServerBase + "/Patient/P/$validate?profile=http://profile-noname-patient");
		};

		try (CloseableHttpResponse outcome = ourHttpClient.execute(request)) {
			String response = IOUtils.toString(outcome.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
		}
	}

	enum LevelEnum {
		TYPE,
		INSTANCE
	}

}
