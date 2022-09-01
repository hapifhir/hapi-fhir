package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContextLoads extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TestContextLoads.class);

	@Test
	public void testContextLoads() {
		ourLog.info("Yay it loads!");
	}
}
