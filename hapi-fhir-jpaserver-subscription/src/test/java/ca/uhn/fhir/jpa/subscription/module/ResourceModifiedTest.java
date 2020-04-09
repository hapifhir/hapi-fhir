package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.r4.model.Organization;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ResourceModifiedTest {
	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testCreate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		assertEquals(org.getIdElement(), msg.getId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.CREATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertEquals(org.getId(), decodedOrg.getId());
		assertEquals(org.getName(), decodedOrg.getName());
	}

	@Test
	public void testUpdate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		assertEquals(org.getIdElement(), msg.getId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.UPDATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertEquals(org.getId(), decodedOrg.getId());
		assertEquals(org.getName(), decodedOrg.getName());
	}

	@Test
	public void testDelete() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.DELETE);
		assertEquals(org.getIdElement(), msg.getId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.DELETE, msg.getOperationType());
		assertNull(msg.getNewPayload(myFhirContext));
	}

}
