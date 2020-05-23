package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PartitionSettingsSvcImplTest extends BaseJpaR4Test {

	@Test
	public void testCreateAndFetchPartition() {

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME123");
		partition.setDescription("A description");
		myPartitionConfigSvc.createPartition(partition);

		partition = myPartitionConfigSvc.getPartitionById(123);
		assertEquals("NAME123", partition.getName());

		partition = myPartitionConfigSvc.getPartitionByName("NAME123");
		assertEquals("NAME123", partition.getName());
	}

	@Test
	public void testDeletePartition() {

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME123");
		partition.setDescription("A description");
		myPartitionConfigSvc.createPartition(partition);

		partition = myPartitionConfigSvc.getPartitionById(123);
		assertEquals("NAME123", partition.getName());

		myPartitionConfigSvc.deletePartition(123);

		try {
			myPartitionConfigSvc.getPartitionById(123);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("No partition exists with ID 123", e.getMessage());
		}

	}

	@Test
	public void testDeletePartition_TryToDeleteDefault() {

		try {
			myPartitionConfigSvc.deletePartition(0);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Can not delete default partition", e.getMessage());
		}

	}

	@Test
	public void testUpdatePartition_TryToUseExistingName() {

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME123");
		partition.setDescription("A description");
		myPartitionConfigSvc.createPartition(partition);

		partition = new PartitionEntity();
		partition.setId(111);
		partition.setName("NAME111");
		partition.setDescription("A description");
		myPartitionConfigSvc.createPartition(partition);

		partition = new PartitionEntity();
		partition.setId(111);
		partition.setName("NAME123");
		partition.setDescription("A description");
		try {
			myPartitionConfigSvc.updatePartition(partition);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Partition name \"NAME123\" is already defined", e.getMessage());
		}
	}

	@Test
	public void testUpdatePartition_TryToRenameDefault() {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(0);
		partition.setName("NAME123");
		partition.setDescription("A description");
		try {
			myPartitionConfigSvc.updatePartition(partition);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Can not rename default partition", e.getMessage());
		}
	}

	@Test
	public void testUpdatePartition() {

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME123");
		partition.setDescription("A description");
		myPartitionConfigSvc.createPartition(partition);

		partition = myPartitionConfigSvc.getPartitionById(123);
		assertEquals("NAME123", partition.getName());

		partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME-NEW");
		partition.setDescription("A description");
		myPartitionConfigSvc.updatePartition(partition);

		partition = myPartitionConfigSvc.getPartitionById(123);
		assertEquals("NAME-NEW", partition.getName());
	}

	@Test
	public void testCreatePartition_InvalidName() {

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME 123");
		partition.setDescription("A description");
		try {
			myPartitionConfigSvc.createPartition(partition);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Partition name \"NAME 123\" is not valid", e.getMessage());
		}

	}

	@Test
	public void testCreatePartition_0Blocked() {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(0);
		partition.setName("NAME123");
		partition.setDescription("A description");
		try {
			myPartitionConfigSvc.createPartition(partition);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Can not create a partition with ID 0 (this is a reserved value)", e.getMessage());
		}

	}

	@Test
	public void testUpdatePartition_UnknownPartitionBlocked() {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("NAME123");
		partition.setDescription("A description");
		try {
			myPartitionConfigSvc.updatePartition(partition);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("No partition exists with ID 123", e.getMessage());
		}

	}

}
