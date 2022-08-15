package ca.uhn.fhir.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidateCommandTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateCommandTest.class);

	@BeforeEach
	public void before() {
		System.setProperty("test", "true");
	}
	
	@Test
	public void testValidateLocalProfileDstu3() {
		String resourcePath = ValidateCommandTest.class.getResource("/patient-uslab-example1.xml").getFile();
		ourLog.info(resourcePath);
		
		App.main(new String[] {
			"validate",
			"-v", "dstu3",
			"-p",
			"-n", resourcePath});
	}

	@Test
	public void testValidateLocalProfileR4() {
		String resourcePath = ValidateCommandTest.class.getResource("/patient-uslab-example1.xml").getFile();
		ourLog.info(resourcePath);

		App.main(new String[] {
			"validate",
			"-v", "R4",
			"-p",
			"-n", resourcePath});
	}
}
