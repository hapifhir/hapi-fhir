package test;

import org.junit.jupiter.api.Test;

import ca.uhn.test.realstructs.resource.ListResource;
import ca.uhn.test.realstructs.resource.AuditEvent.ObjectElement;

public class TestParticulars {

	@Test
	public void testElementsWithSpecialNames() {
		// This won't compile if tinder didn't generate the right names...
		ListResource.class.getName();
		ObjectElement.class.getName();
	}
	
	@Test
	public void testReferenceDoesntReturnNull() {
		ca.uhn.test.realstructs.resource.Patient p = new ca.uhn.test.realstructs.resource.Patient();
		p.getManagingOrganization().toString();
	}
	
}
