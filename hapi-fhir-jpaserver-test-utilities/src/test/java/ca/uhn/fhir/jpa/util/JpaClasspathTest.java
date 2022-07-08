package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class JpaClasspathTest {

	/**
	 * Make sure no dependencies start bringing in log4j - This makes hibernate decide to start using log4j instead of
	 * slf4j which is super annoying..
	 */
	@Test
	public void testNoLog4jOnClasspath() {

		try {
			Class.forName("org.apache.logging.log4j.core.appender");
			fail("org.apache.logging.log4j.core.appender" + " found on classpath - Make sure log4j isn't being introduced");
		} catch (ClassNotFoundException e) {
			// good
		}

	}

}
