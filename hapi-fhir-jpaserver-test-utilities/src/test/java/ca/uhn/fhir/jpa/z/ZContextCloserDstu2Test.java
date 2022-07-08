package ca.uhn.fhir.jpa.z;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;

public class ZContextCloserDstu2Test extends BaseJpaDstu2Test {

	/**
	 *  this is just here to close the context when this package's test are done
	 */
	@Test
	@DirtiesContext()
	public void testCloseContext() {
		// nothing
	}
	
	
}
