package ca.uhn.fhir.testmindeps;

import org.junit.Test;

public class SetupTest {

	/**
	 * Ensure that Woodstox is not on the classpath (we're testing that the library works ok without it
	 * elsewhere)
	 */
	@Test(expected=ClassNotFoundException.class)
	public void testValidateEnvironment() throws ClassNotFoundException {
		Class.forName("com.ctc.wstx.stax.WstxOutputFactory");
	}
	
}
