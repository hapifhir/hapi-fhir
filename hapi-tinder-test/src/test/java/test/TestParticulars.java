package test;

import org.junit.jupiter.api.Test;

import ca.uhn.test.realstructs.resource.ListResource;
import ca.uhn.test.realstructs.resource.AuditEvent.ObjectElement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestParticulars {

	@Test
	public void testElementsWithSpecialNames() {
		// This won't compile if tinder didn't generate the right names...
		assertNotNull(ListResource.class.getName());
		assertNotNull(ObjectElement.class.getName());
	}
	
	@Test
	public void testReferenceDoesntReturnNull() {
		ca.uhn.test.realstructs.resource.Patient p = new ca.uhn.test.realstructs.resource.Patient();
		assertNotNull(p.getManagingOrganization().toString());
	}
	
}
