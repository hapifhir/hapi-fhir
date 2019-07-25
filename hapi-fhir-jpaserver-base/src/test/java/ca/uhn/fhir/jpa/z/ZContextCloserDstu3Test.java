package ca.uhn.fhir.jpa.z;

import org.junit.Test;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;

public class ZContextCloserDstu3Test extends BaseJpaDstu3Test {

	/**
	 *  this is just here to close the context when this package's test are done
	 */
	@Test
//	@DirtiesContext()
	public void testCloseContext() {
		// nothing
	}
	
	
}
