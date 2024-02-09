package test;

import org.junit.jupiter.api.Test;

import ca.uhn.test.realstructs.resource.ListResource;

import static org.assertj.core.api.Assertions.assertThat;
import ca.uhn.test.realstructs.resource.AuditEvent.ObjectElement;

public class TestParticulars {

	@Test
	public void testElementsWithSpecialNames() {
		// This won't compile if tinder didn't generate the right names...
		assertThat(ListResource.class.getName()).isNotNull();
		assertThat(ObjectElement.class.getName()).isNotNull();
	}
	
	@Test
	public void testReferenceDoesntReturnNull() {
		ca.uhn.test.realstructs.resource.Patient p = new ca.uhn.test.realstructs.resource.Patient();
		assertThat(p.getManagingOrganization().toString()).isNotNull();
	}
	
}
