package ca.uhn.fhir.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.read.ListAppender;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ValidateCommandTest {

	private final Logger ourLog = (Logger) LoggerFactory.getLogger(ValidateCommand.class);
	private ValidateCommand myValidateCommand = new ValidateCommand();
	private ListAppender<ILoggingEvent> myListAppender = new ListAppender<>();


	@BeforeEach
	public void beforeEach() {
		ourLog.setLevel(Level.ALL);
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
