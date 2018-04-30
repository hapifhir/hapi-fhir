package ca.uhn.fhir.cli;

import org.junit.Before;
import org.junit.Test;

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
		
		App.main(new String[] {"validate", "-v", "dstu3", "-p", "-n", resourcePath});
	}
	

}
