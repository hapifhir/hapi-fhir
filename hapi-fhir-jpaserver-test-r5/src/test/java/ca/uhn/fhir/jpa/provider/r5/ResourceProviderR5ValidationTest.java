package ca.uhn.fhir.jpa.provider.r5;

import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProviderR5ValidationTest extends BaseResourceProviderR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5ValidationTest.class);

	@ParameterizedTest
	@EnumSource(LevelEnum.class)
	void testValidateAgainstProfileSpecifiedInRequest(LevelEnum theLevel) {
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
		String response = switch (theLevel) {
			case TYPE -> myServer.fhirRequest("/Patient/$validate?profile=http://profile-noname-patient").post(patient).getBody();
			case INSTANCE -> myServer.fhirRequest("/Patient/P/$validate?profile=http://profile-noname-patient").get().getBody();
		};

		ourLog.info(response);
	}

	enum LevelEnum {
		TYPE,
		INSTANCE
	}

}
