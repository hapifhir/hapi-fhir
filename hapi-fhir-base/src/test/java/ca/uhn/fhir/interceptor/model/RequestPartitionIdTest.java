package ca.uhn.fhir.interceptor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestPartitionIdTest {
	private static final Logger ourLog = LoggerFactory.getLogger(RequestPartitionIdTest.class);

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

	@Test
	public void testSerDeserSer() throws JsonProcessingException {
		{
			RequestPartitionId start = RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1));
			String json = assertSerDeserSer(start);
			assertThat(json, containsString("\"partitionDate\":[2020,1,1]"));
			assertThat(json, containsString("\"partitionIds\":[123]"));
		}
		{
			RequestPartitionId start = RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null);
			String json = assertSerDeserSer(start);
			assertThat(json, containsString("partitionNames\":[\"Name1\",\"Name2\"]"));
		}
		assertSerDeserSer(RequestPartitionId.allPartitions());
		assertSerDeserSer(RequestPartitionId.defaultPartition());
	}

	private String assertSerDeserSer(RequestPartitionId start) throws JsonProcessingException {
		String json = start.asJson();
		ourLog.info(json);
		RequestPartitionId end = RequestPartitionId.fromJson(json);
		assertEquals(start, end);
		String json2 = end.asJson();
		assertEquals(json, json2);
		return json;
	}
}
