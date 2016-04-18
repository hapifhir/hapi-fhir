import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.cli.App;

public class ValidateTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateTest.class);

	@Before
	public void before() {
		System.setProperty("noexit", "true");
	}
	
	@Test
	@Ignore
	public void testValidateLocalProfile() {
		String profilePath = ValidateTest.class.getResource("/uslab-patient.profile.xml").getFile(); 		
		String resourcePath = ValidateTest.class.getResource("/patient-uslab-example1.xml").getFile();
		ourLog.info(profilePath);
		ourLog.info(resourcePath);
		
		App.main(new String[] {"validate", "-p", "-n", resourcePath, "-l", profilePath});
	}
	
	@Test
	@Ignore
	public void testValidateLocalProfileWithReferenced() {
		String profilePath = ValidateTest.class.getResource("/nl/nl-core-patient.dstu2.xml").getFile(); 		
		String resourcePath = ValidateTest.class.getResource("/nl/patient-example-a.xml").getFile();
		ourLog.info(profilePath);
		ourLog.info(resourcePath);
		
		App.main(new String[] {"validate", "-p", "-n", resourcePath, "-l", profilePath});
	}

}
