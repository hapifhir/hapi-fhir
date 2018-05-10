package ca.uhn.fhir.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ValidateTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateTest.class);

	@Before
	public void before() {
		System.setProperty("test", "true");
	}
	
	@Test
	public void testValidateLocalProfile() {
		String resourcePath = ValidateTest.class.getResource("/patient-uslab-example1.xml").getFile();
		ourLog.info(resourcePath);
		
		App.main(new String[] {
			"validate",
			"-v", "dstu3",
			"-p",
			"-n", resourcePath});
	}

	@Test
	public void testValidateUsingIgPackSucceedingDstu2() {
		String resourcePath = ValidateTest.class.getResource("/argo-dstu2-observation-good.json").getFile();
		ourLog.info(resourcePath);

		App.main(new String[] {
			"validate",
			"-v", "dstu2",
			"-p",
			"--igpack", "src/test/resources/argo-dstu2.pack",
			"-n", resourcePath});
	}

	@Test
	public void testValidateUsingIgPackFailingDstu2() {
		String resourcePath = ValidateTest.class.getResource("/argo-dstu2-observation-bad.json").getFile();
		ourLog.info(resourcePath);

		try {
			App.main(new String[] {
				"validate",
				"-v", "dstu2",
				"-p",
				"--igpack", "src/test/resources/argo-dstu2.pack",
				"-n", resourcePath});
			// Should not get here
			fail();
		} catch (CommandFailureException e) {
			// good
		}
	}

}
