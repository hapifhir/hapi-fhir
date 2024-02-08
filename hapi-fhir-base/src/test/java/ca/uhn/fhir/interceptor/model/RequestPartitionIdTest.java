package ca.uhn.fhir.interceptor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestPartitionIdTest {
	private static final Logger ourLog = LoggerFactory.getLogger(RequestPartitionIdTest.class);

	@Test
	public void testHashCode() {
		assertThat(RequestPartitionId.allPartitions().hashCode()).isEqualTo(31860737);
	}

	@Test
	public void testEquals() {
		assertThat(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1))).isEqualTo(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertThat(null).isNotEqualTo(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertThat("123").isNotEqualTo(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
	}

	@Test
	public void testPartition() {
		assertThat(RequestPartitionId.allPartitions().isDefaultPartition()).isFalse();
		assertThat(RequestPartitionId.defaultPartition().isAllPartitions()).isFalse();
		assertThat(RequestPartitionId.defaultPartition().isDefaultPartition()).isTrue();
		assertThat(RequestPartitionId.allPartitions().isAllPartitions()).isTrue();
		assertThat(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isAllPartitions()).isFalse();
		assertThat(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isDefaultPartition()).isFalse();
		assertThat(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isAllPartitions()).isFalse();
		assertThat(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isDefaultPartition()).isFalse();
	}

	@Test
	public void testSerDeserSer() throws JsonProcessingException {
		{
			RequestPartitionId start = RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1));
			String json = assertSerDeserSer(start);
			assertThat(json).contains("\"partitionDate\":[2020,1,1]");
			assertThat(json).contains("\"partitionIds\":[123]");
		}
		{
			RequestPartitionId start = RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null);
			String json = assertSerDeserSer(start);
			assertThat(json).contains("partitionNames\":[\"Name1\",\"Name2\"]");
		}
		assertSerDeserSer(RequestPartitionId.allPartitions());
		assertSerDeserSer(RequestPartitionId.defaultPartition());
	}

	private String assertSerDeserSer(RequestPartitionId start) throws JsonProcessingException {
		String json = start.asJson();
		ourLog.info(json);
		RequestPartitionId end = RequestPartitionId.fromJson(json);
		assertThat(end).isEqualTo(start);
		String json2 = end.asJson();
		assertThat(json2).isEqualTo(json);
		return json;
	}
}
