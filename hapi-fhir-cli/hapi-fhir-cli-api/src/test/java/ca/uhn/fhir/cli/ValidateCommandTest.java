package ca.uhn.fhir.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidateCommandTest {

	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(ValidateCommand.class);
		private ListAppender<ILoggingEvent> myListAppender = new ListAppender<>();


	@BeforeEach
	public void beforeEach() {
		myListAppender = new ListAppender<>();
		myListAppender.start();
		ourLog.addAppender(myListAppender);
	}

	@AfterEach
	public void afterEach() {
		myListAppender.stop();
	}


	@BeforeEach
	public void before() {
		System.setProperty("test", "true");
	}

	@Test
	public void testValidateLocalProfileDstu3() {
		String resourcePath = ValidateCommandTest.class.getResource("/patient-uslab-example1.xml").getFile();

		App.main(new String[]{
			"validate",
			"-v", "dstu3",
			"-p",
			"-n", resourcePath});
	}

	@Test
	public void testValidateLocalProfileR4() {
		String resourcePath = ValidateCommandTest.class.getResource("/patient-uslab-example1.xml").getFile();
		ourLog.info(resourcePath);

		App.main(new String[]{
			"validate",
			"-v", "R4",
			"-p",
			"-n", resourcePath});
	}

	@Test
	public void validate_withLocalProfileR4_shouldLogErrorAboutCardinality_whenResourceDoesNotComplyWithProfile() {
		String patientJson = ValidateCommandTest.class.getResource("/validate/Patient-no-identifier.json").getFile();
		String patientProfile = ValidateCommandTest.class.getResource("/validate/PatientIn-Profile.json").getFile();

		assertThrows(CommandFailureException.class, () ->
			App.main(new String[]{
				"validate",
				"--fhir-version", "r4",
				"--profile",
				"--file", patientJson,
				"-l", patientProfile}));

		String expectedMessage = "expected message";
		Level expectedLevel = Level.INFO;
		List<ILoggingEvent> errorLogs = myListAppender
			.list
			.stream()
//			.filter(event -> expectedMessage.equals(event.getFormattedMessage()))
			.filter(event -> expectedLevel.equals(event.getLevel()))
			.collect(Collectors.toList());

		assertEquals(1, errorLogs.size());

	}

	@Test
	public void validate_withLocalProfileR4_shouldPass_whenResourceCompliesWithProfile() {
		String patientJson = ValidateCommandTest.class.getResource("/validate/Patient.json").getFile();
		String patientProfile = ValidateCommandTest.class.getResource("/validate/PatientIn-Profile.json").getFile();

		App.main(new String[]{
			"validate",
			"--fhir-version", "r4",
			"--profile",
			"--file", patientJson,
			"-l", patientProfile});
	}
}
