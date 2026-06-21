package ca.uhn.fhir.interceptor.model;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static ca.uhn.fhir.interceptor.model.RequestPartitionId.allPartitions;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.defaultPartition;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionId;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionIds;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionNames;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.stringifyForKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestPartitionIdTest {

	private static final LocalDate PARTITION_DATE = LocalDate.of(2020, 1, 1);
	private static final String PARTITION_NAME_1 = "Name1";
	private static final String PARTITION_NAME_2 = "Name2";
	private static final Integer PARTITION_ID_1 = 1;
	private static final Integer PARTITION_ID_2 = 2;
	private static final Integer PARTITION_ID_123 = 123;

	@Test
	void testEqualsAndHashCode() {
		RequestPartitionId expected = fromPartitionId(PARTITION_ID_123, PARTITION_DATE);
		RequestPartitionId actual = fromPartitionId(PARTITION_ID_123, PARTITION_DATE);

		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertNotEquals(expected, "123");
		assertNotEquals(expected, null);
	}

	@Test
	void testPartitionHelpers() {
		IDefaultPartitionSettings settings = new IDefaultPartitionSettings() {
			@Override
			public Integer getDefaultPartitionId() {
				return 0;
			}
		};

		assertTrue(fromPartitionId(null).isPartition(null));
		assertTrue(fromPartitionId(null).isDefaultPartition());
		assertThat(fromPartitionId(null).hasDefaultPartitionId(null)).isTrue();
		assertThat(defaultPartition(settings).isPartition(0)).isTrue();
		assertThat(defaultPartition(settings).isDefaultPartition()).isFalse();
		assertThat(allPartitions().isAllPartitions()).isTrue();
		assertThat(allPartitions().isPartition(0)).isFalse();
		assertThat(fromPartitionIds(0, 2).isPartition(0)).isFalse();
	}

	@Test
	void testMergeIds() {
		assertEquals(fromPartitionIds(1, 2, 3, 4), fromPartitionIds(1, 2, 3).mergeIds(fromPartitionIds(1, 2, 4)));
		assertEquals(allPartitions(), allPartitions().mergeIds(fromPartitionIds(1, 2, 4)));
		assertEquals(allPartitions(), fromPartitionIds(1, 2, 3).mergeIds(allPartitions()));
		assertEquals(fromPartitionIds(1, 2, 3, null), fromPartitionIds(1, 2, 3).mergeIds(fromPartitionId(null)));
	}

	@ParameterizedTest
	@MethodSource("testStringifyForKeyTestCases")
	void testStringifyForKey(RequestPartitionId theRequestPartitionId, String theExpectedString) {
		assertEquals(theExpectedString, stringifyForKey(theRequestPartitionId));
	}

	static Stream<Object[]> testStringifyForKeyTestCases() {
		return Stream.of(
			new Object[] {allPartitions(), "(all)"},
			new Object[] {fromPartitionId(null), "null"},
			new Object[] {fromPartitionIds(1, 2, 3), "1 2 3"},
			new Object[] {fromPartitionIds(null, 2, 3), "null 2 3"},
			new Object[] {RequestPartitionId.allPartitionsWithPartitionIds(1, 2, 3), "(all) 1 2 3"});
	}

	record ContainsTestCase(String description, RequestPartitionId left, RequestPartitionId right, Comparison comparison) {
		enum Comparison {
			LEFT_CONTAINS_RIGHT,
			RIGHT_CONTAINS_LEFT,
			EQUAL,
			NEITHER;

			boolean expectLeftContainsRight() {
				return this == LEFT_CONTAINS_RIGHT || this == EQUAL;
			}

			boolean expectRightContainsLeft() {
				return this == RIGHT_CONTAINS_LEFT || this == EQUAL;
			}

			boolean expectEqual() {
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
		RequestPartitionId left = theTestCase.left();
		RequestPartitionId right = theTestCase.right();
		ContainsTestCase.Comparison comparison = theTestCase.comparison();

		assertThat(left.contains(right)).describedAs("%s: left contains right", theTestCase.description).isEqualTo(comparison.expectLeftContainsRight());
		assertThat(right.contains(left)).describedAs("%s: right contains left", theTestCase.description).isEqualTo(comparison.expectRightContainsLeft());
		assertThat(right.equals(left)).describedAs("%s: are equal", theTestCase.description).isEqualTo(comparison.expectEqual());
	}

	static ContainsTestCase[] getContainsTestCases() {
		IDefaultPartitionSettings nullDefaultPartition = new IDefaultPartitionSettings() {};
		return new ContainsTestCase[] {
			new ContainsTestCase("all vs all", allPartitions(), allPartitions(), ContainsTestCase.Comparison.EQUAL),
			new ContainsTestCase("all vs normal", allPartitions(), fromPartitionIds(1, 2, 3), ContainsTestCase.Comparison.LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("equal partition id lists", fromPartitionIds(1, 2, 3), fromPartitionIds(1, 2, 3), ContainsTestCase.Comparison.EQUAL),
			new ContainsTestCase("different id lists incomparable", fromPartitionIds(1, 2, 5), fromPartitionIds(1, 2, 9), ContainsTestCase.Comparison.NEITHER),
			new ContainsTestCase("default as null contains", fromPartitionIds(1, 2, null), defaultPartition(nullDefaultPartition), ContainsTestCase.Comparison.LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("names equivalent", fromPartitionNames("A", "B"), fromPartitionNames("A", "B"), ContainsTestCase.Comparison.EQUAL),
			new ContainsTestCase("names left contains right", fromPartitionNames("A", "B"), fromPartitionNames("A"), ContainsTestCase.Comparison.LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("names left ids right", fromPartitionNames("A", "B"), fromPartitionIds(1, 2), ContainsTestCase.Comparison.NEITHER)
		};
	}

	@Test
	void testJsonRoundTripPreservesSemanticFields() throws Exception {
		RequestPartitionId start = RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList(PARTITION_NAME_1, PARTITION_NAME_2), Lists.newArrayList(PARTITION_ID_1, PARTITION_ID_2), PARTITION_DATE);

		RequestPartitionId end = assertSerDeserSer(start);

		assertThat(end.getPartitionDate()).isEqualTo(PARTITION_DATE);
		assertThat(end.getPartitionNames()).containsExactly(PARTITION_NAME_1, PARTITION_NAME_2);
		assertThat(end.getPartitionIds()).containsExactly(PARTITION_ID_1, PARTITION_ID_2);
		assertThat(end.getPartitionIdsWithoutDefault()).containsExactly(PARTITION_ID_1, PARTITION_ID_2);
		assertThat(end.getFirstPartitionIdOrNull()).isEqualTo(PARTITION_ID_1);
		assertThat(end.getFirstPartitionNameOrNull()).isEqualTo(PARTITION_NAME_1);
	}

	@Test
	void testJsonRoundTripPreservesAllAndDefaultPartitions() throws Exception {
		RequestPartitionId all = assertSerDeserSer(allPartitions());
		assertThat(all.isAllPartitions()).isTrue();
		assertThat(all.hasPartitionIds()).isFalse();

		RequestPartitionId defaultPartition = assertSerDeserSer(fromPartitionId(null));
		assertThat(defaultPartition.getPartitionIds()).containsExactly((Integer) null);
		assertThat(defaultPartition.isDefaultPartition()).isTrue();
	}

	private RequestPartitionId assertSerDeserSer(RequestPartitionId start) throws Exception {
		String json = start.asJson();
		RequestPartitionId end = RequestPartitionId.fromJson(json);
		assertEquals(start, end);
		assertEquals(start.asJson(), end.asJson());
		return end;
	}
}
