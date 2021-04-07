package ca.uhn.fhir.interceptor.model;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestPartitionIdTest {

	@Test
	public void testHashCode() {
		assertEquals(31860737, RequestPartitionId.allPartitions().hashCode());
	}

	@Test
	public void testEquals() {
		assertEquals(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)), RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertNotEquals(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)), null);
		assertNotEquals(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)), "123");
	}

	@Test
	public void testPartition() {
		assertFalse(RequestPartitionId.allPartitions().isDefaultPartition());
		assertFalse(RequestPartitionId.defaultPartition().isAllPartitions());
		assertTrue(RequestPartitionId.defaultPartition().isDefaultPartition());
		assertTrue(RequestPartitionId.allPartitions().isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isDefaultPartition());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isDefaultPartition());
	}


}
