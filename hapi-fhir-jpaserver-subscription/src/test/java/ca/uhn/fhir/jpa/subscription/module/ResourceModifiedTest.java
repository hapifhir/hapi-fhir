package ca.uhn.fhir.jpa.subscription.module;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceModifiedTest {
	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testCreate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		assertThat(msg.getPayloadId(myFhirContext)).isEqualTo(org.getIdElement());
		assertThat(msg.getOperationType()).isEqualTo(ResourceModifiedMessage.OperationTypeEnum.CREATE);
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertThat(decodedOrg.getId()).isEqualTo(org.getId());
		assertThat(decodedOrg.getName()).isEqualTo(org.getName());
		assertThat(RequestPartitionId.defaultPartition().toJson()).isEqualTo(msg.getPartitionId().toJson());
	}

	@Test
	public void testUpdate() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		assertThat(msg.getPayloadId(myFhirContext)).isEqualTo(org.getIdElement());
		assertThat(msg.getOperationType()).isEqualTo(ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		Organization decodedOrg = (Organization) msg.getNewPayload(myFhirContext);
		assertThat(decodedOrg.getId()).isEqualTo(org.getId());
		assertThat(decodedOrg.getName()).isEqualTo(org.getName());
		assertThat(RequestPartitionId.defaultPartition().toJson()).isEqualTo(msg.getPartitionId().toJson());
	}

	@Test
	public void testDelete() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.DELETE);
		assertThat(msg.getPayloadId(myFhirContext).getValue()).isEqualTo("Organization/testOrgId");
		assertThat(msg.getOperationType()).isEqualTo(ResourceModifiedMessage.OperationTypeEnum.DELETE);
		assertNotNull(msg.getNewPayload(myFhirContext));
		assertThat(RequestPartitionId.defaultPartition().toJson()).isEqualTo(msg.getPartitionId().toJson());
	}

	@Test
	public void testCreateWithPartition() {
		Organization org = new Organization();
		org.setName("testOrgName");
		org.setId("Organization/testOrgId");
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, org, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		msg.setPartitionId(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));

		assertThat(1).isEqualTo(msg.getPartitionId().getPartitionIds().size());
		assertThat(123).isEqualTo(msg.getPartitionId().getPartitionIds().get(0));
	}

}
