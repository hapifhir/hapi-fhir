package ca.uhn.fhir.testmindeps;

import org.junit.jupiter.api.Test;

public class SetupTest {

	/**
	 * Ensure that Woodstox is on the classpath
	 */
	@Test()
	public void testValidateEnvironment() throws ClassNotFoundException {
		Class.forName("com.ctc.wstx.stax.WstxOutputFactory");
	}
	
}
