package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationSupportChainCreatorTest {

	private static FhirContext myContextDstu2 = FhirContext.forDstu2();
	private static FhirContext myContextR4 = FhirContext.forR4();

	private ValidateCommand myValidateCommand = new ValidateCommand();

	@Test
	public void getR4Chain_shouldProvideExpectedValidationSupport() throws ParseException {
		String patientJson = ValidateCommandTest.class.getResource("/validate/Patient-no-identifier.json").getFile();
		String patientProfile = ValidateCommandTest.class.getResource("/validate/PatientIn-Profile.json").getFile();

		String[] args = new String[]{
			"validate",
			"--fhir-version", "r4",
			"--profile",
			"--file", patientJson,
			"-l", patientProfile
		};

		CommandLine commandLine = new DefaultParser().parse(myValidateCommand.getOptions(), args);
		ValidationSupportChain chain = ValidationSupportChainCreator.getValidationSupportChainR4(myContextR4, commandLine);
		StructureDefinition structureDefinition = (StructureDefinition) chain.fetchStructureDefinition("https://www.medizininformatik-initiative.de/fhir/core/modul-person/StructureDefinition/Patient");

		assertEquals(structureDefinition.getName(), "Profile_MII_Patient_PatientIn");
	}
}
