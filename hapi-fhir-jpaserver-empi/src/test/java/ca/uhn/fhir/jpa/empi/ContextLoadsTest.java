package ca.uhn.fhir.jpa.empi;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContextLoadsTest extends BaseEmpiR4Test{
	private static final Logger ourLog = LoggerFactory.getLogger(ContextLoadsTest.class);

	@Test
	public void testContextLoaded() {
		ourLog.info("Context loaded");
	}
}
