package ca.uhn.fhir.interceptor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static ca.uhn.fhir.interceptor.model.RequestPartitionId.allPartitions;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.defaultPartition;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionIds;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.EQUAL;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.LEFT_CONTAINS_RIGHT;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.NEITHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestPartitionIdTest {
	private static final Logger ourLog = LoggerFactory.getLogger(RequestPartitionIdTest.class);

	private static final Integer ourDefaultPartitionId = 0;
	
	@Test
	public void testHashCode() {
		assertEquals(31860737, allPartitions().hashCode());
	}

	@Test
	public void testEquals() {
		assertEquals(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)), RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertNotNull(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertThat("123").isNotEqualTo(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
	}

	@Test
	public void testPartition() {
		assertFalse(allPartitions().isDefaultPartition());
		assertFalse(defaultPartition().isAllPartitions());
		assertTrue(defaultPartition().isDefaultPartition());
		assertTrue(allPartitions().isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isDefaultPartition());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isDefaultPartition());
	}

	@Test
	public void testIsDefaultPartition_withPartitionAsParameter() {

		assertThat(defaultPartition().isPartition(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId).isPartition(ourDefaultPartitionId)).isTrue();

		assertThat(defaultPartition().isPartition(ourDefaultPartitionId)).isFalse();
		assertThat(allPartitions().isPartition(ourDefaultPartitionId)).isFalse();
		assertThat(fromPartitionIds(ourDefaultPartitionId, 2).isPartition(ourDefaultPartitionId)).isFalse();
	}

	@Test
	public void testHasDefaultPartition_withDefaultPartitionAsParameter() {

		assertThat(defaultPartition().hasDefaultPartitionId(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId).hasDefaultPartitionId(ourDefaultPartitionId)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId, null).hasDefaultPartitionId(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId, null).hasDefaultPartitionId(ourDefaultPartitionId)).isTrue();

		assertThat(fromPartitionIds(ourDefaultPartitionId).hasDefaultPartitionId(null)).isFalse();
		assertThat(defaultPartition().hasDefaultPartitionId(ourDefaultPartitionId)).isFalse();
	}

	@Test
	public void testMergeIds() {
		RequestPartitionId input0 = fromPartitionIds(1, 2, 3);
		RequestPartitionId input1 = fromPartitionIds(1, 2, 4);

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = fromPartitionIds(1, 2, 3, 4);
		assertEquals(expected, actual);

	}

	@Test
	public void testMergeIds_ThisAllPartitions() {
		RequestPartitionId input0 = allPartitions();
		RequestPartitionId input1 = fromPartitionIds(1, 2, 4);

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = allPartitions();
		assertEquals(expected, actual);

	}

	@Test
	public void testMergeIds_OtherAllPartitions() {
		RequestPartitionId input0 = fromPartitionIds(1, 2, 3);
		RequestPartitionId input1 = allPartitions();

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = allPartitions();
		assertEquals(expected, actual);

	}

	@Test
	public void testMergeIds_IncludesDefault() {
		RequestPartitionId input0 = fromPartitionIds(1, 2, 3);
		RequestPartitionId input1 = defaultPartition();

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = fromPartitionIds(1, 2, 3, null);
		assertEquals(expected, actual);

	}

	record ContainsTestCase(String description, RequestPartitionId left, RequestPartitionId right, Comparison comparison) {
		enum Comparison {
			LEFT_CONTAINS_RIGHT,
			RIGHT_CONTAINS_LEFT,
			EQUAL,
			NEITHER;

			public boolean expectLeftContainsRight() {
				return this == LEFT_CONTAINS_RIGHT || this == EQUAL;
			}

			public boolean expectRightContainsLeft() {
				return this == RIGHT_CONTAINS_LEFT || this == EQUAL;
			}

			public boolean expectEqual() {
				return this == EQUAL;
			}
		}

		@Override
		public String toString() {
			return "%s: %s %s".formatted(description, left, right);
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("getContainsTestCases")
	void testContains(ContainsTestCase theTestCase) {
	    // given
		RequestPartitionId left = theTestCase.left();
		RequestPartitionId right = theTestCase.right();
		ContainsTestCase.Comparison comparison = theTestCase.comparison();

		// when
		assertThat(left.contains(right)).describedAs("%s: left contains right", theTestCase.description).isEqualTo(comparison.expectLeftContainsRight());
		assertThat(right.contains(left)).describedAs("%s: right contains left", theTestCase.description).isEqualTo(comparison.expectRightContainsLeft());
		assertThat(right.equals(left)).describedAs("%s: are equal", theTestCase.description).isEqualTo(comparison.expectEqual());

	}

	static ContainsTestCase[] getContainsTestCases() {
		return new ContainsTestCase[]{
			new ContainsTestCase("all vs all", allPartitions(), allPartitions(), EQUAL),
			new ContainsTestCase("all vs normal", allPartitions(), fromPartitionIds(1, 2, 3), LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("equal partition id lists", fromPartitionIds(1, 2, 3), fromPartitionIds(1, 2, 3), EQUAL),
			new ContainsTestCase("different id lists incomparable", fromPartitionIds(1, 2, 5), fromPartitionIds(1, 2, 9), NEITHER),
			new ContainsTestCase("default as null contains", fromPartitionIds(1, 2, null), defaultPartition(), LEFT_CONTAINS_RIGHT),
		};
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
		assertSerDeserSer(allPartitions());
		assertSerDeserSer(defaultPartition());
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
