package test;

import org.junit.Test;

public class TestParticulars {

	@Test
	public void testReferenceDoesntReturnNull() {
		ca.uhn.test.realstructs.resource.Patient p = new ca.uhn.test.realstructs.resource.Patient();
		p.getManagingOrganization().toString();
	}
	
}
