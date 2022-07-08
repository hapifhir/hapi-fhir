package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceModifiedTest {
	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testCreate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		assertEquals(org.getIdElement(), msg.getPayloadId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.CREATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertEquals(org.getId(), decodedOrg.getId());
		assertEquals(org.getName(), decodedOrg.getName());
		assertEquals(msg.getPartitionId().toJson(), RequestPartitionId.defaultPartition().toJson());
	}

	@Test
	public void testUpdate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		assertEquals(org.getIdElement(), msg.getPayloadId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.UPDATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertEquals(org.getId(), decodedOrg.getId());
		assertEquals(org.getName(), decodedOrg.getName());
		assertEquals(msg.getPartitionId().toJson(), RequestPartitionId.defaultPartition().toJson());
	}

	@Test
	public void testDelete() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.DELETE);
		assertEquals("Organization/testOrgId", msg.getPayloadId(myFhirContext).getValue());
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.DELETE, msg.getOperationType());
		assertNotNull(msg.getNewPayload(myFhirContext));
		assertEquals(msg.getPartitionId().toJson(), RequestPartitionId.defaultPartition().toJson());
	}

	@Test
	public void testCreateWithPartition() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		msg.setPartitionId(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));

		assertEquals(msg.getPartitionId().getPartitionIds().size(), 1);
		assertEquals(msg.getPartitionId().getPartitionIds().get(0), 123);
	}

}
