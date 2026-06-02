package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("deprecation")
public class ResourceModifiedTest {
	private final FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testCreate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		assertEquals(org.getIdElement(), msg.getPayloadId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.CREATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewResource(myFhirContext);
		assertThat(decodedOrg)
				.returns(org.getId(), Organization::getId)
				.returns(org.getName(), Organization::getName);
		assertNull(msg.getPartitionId());
	}

	@Test
	public void testUpdate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		assertEquals(org.getIdElement(), msg.getPayloadId(myFhirContext));
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.UPDATE, msg.getOperationType());
		Organization decodedOrg = (Organization) msg.getNewResource(myFhirContext);
		assertThat(decodedOrg)
				.returns(org.getId(), Organization::getId)
				.returns(org.getName(), Organization::getName);
		assertNull(msg.getPartitionId());
	}

	@Test
	public void testDelete() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.DELETE);
		assertEquals("Organization/testOrgId", msg.getPayloadId(myFhirContext).getValue());
		assertEquals(ResourceModifiedMessage.OperationTypeEnum.DELETE, msg.getOperationType());
		assertNotNull(msg.getNewResource(myFhirContext));
		assertNull(msg.getPartitionId());
	}

	@Test
	public void testCreateWithPartition() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		msg.setPartitionId(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));

		assertEquals(1, msg.getPartitionId().getPartitionIds().size());
		assertEquals(123, msg.getPartitionId().getPartitionIds().get(0));
	}

}
