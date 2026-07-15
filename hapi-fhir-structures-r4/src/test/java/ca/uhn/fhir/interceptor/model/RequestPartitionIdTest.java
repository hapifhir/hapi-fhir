package ca.uhn.fhir.interceptor.model;

import ca.uhn.fhir.rest.api.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.interceptor.model.RequestPartitionId.allPartitions;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.defaultPartition;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionId;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionIds;
import static ca.uhn.fhir.interceptor.model.RequestPartitionId.fromPartitionNames;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.EQUAL;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.LEFT_CONTAINS_RIGHT;
import static ca.uhn.fhir.interceptor.model.RequestPartitionIdTest.ContainsTestCase.Comparison.NEITHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class RequestPartitionIdTest {

	private static final Integer ourDefaultPartitionId = 0;
	private static final LocalDate PARTITION_DATE = LocalDate.of(2020, 1, 1);
	private static final String PARTITION_NAME_1 = "Name1";
	private static final String PARTITION_NAME_2 = "Name2";
	private static final Integer PARTITION_ID_1 = 1;
	private static final Integer PARTITION_ID_2 = 2;
	private static final Integer PARTITION_ID_123 = 123;

	@Mock
	private IDefaultPartitionSettings myPartitionSettings;

	@Test
	public void testHashCode() {
		assertEquals(31860737, allPartitions().hashCode());
	}

	@SuppressWarnings("AssertBetweenInconvertibleTypes")
	@Test
	public void testEquals() {
		assertEquals(fromPartitionId(123, LocalDate.of(2020, 1, 1)), fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		assertNotNull(fromPartitionId(123, LocalDate.of(2020, 1, 1)));

		assertNotEquals(fromPartitionId(123, LocalDate.of(2020, 1, 1)), "123");
		assertNotEquals(fromPartitionId(123, LocalDate.of(2020, 1, 1)), null);
	}

	@Test
	public void testPartition() {
		when(myPartitionSettings.getDefaultPartitionId()).thenReturn(null);

		assertFalse(allPartitions().isDefaultPartition());
		assertFalse(allPartitions().isPartition(null));
		assertFalse(fromPartitionId(null).isAllPartitions());
		assertTrue(fromPartitionId(null).isDefaultPartition());
		assertTrue(fromPartitionId(null).isPartition(myPartitionSettings.getDefaultPartitionId()));
		assertFalse(defaultPartition(myPartitionSettings).isAllPartitions());
		assertTrue(defaultPartition(myPartitionSettings).isDefaultPartition());
		assertTrue(defaultPartition(myPartitionSettings).isPartition(myPartitionSettings.getDefaultPartitionId()));
		assertTrue(allPartitions().isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null).isDefaultPartition());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isAllPartitions());
		assertFalse(RequestPartitionId.forPartitionIdsAndNames(null, Lists.newArrayList(1, 2), null).isDefaultPartition());
	}

	@Test
	public void testIsDefaultPartition_withPartitionAsParameter() {
		assertThat(fromPartitionId(null).isPartition(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId).isPartition(ourDefaultPartitionId)).isTrue();

		assertThat(fromPartitionId(null).isPartition(ourDefaultPartitionId)).isFalse();
		assertThat(allPartitions().isPartition(ourDefaultPartitionId)).isFalse();
		assertThat(fromPartitionIds(ourDefaultPartitionId, 2).isPartition(ourDefaultPartitionId)).isFalse();
	}

	@Test
	public void testHasDefaultPartition_withDefaultPartitionAsParameter() {

		assertThat(fromPartitionId(null).hasDefaultPartitionId(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId).hasDefaultPartitionId(ourDefaultPartitionId)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId, null).hasDefaultPartitionId(null)).isTrue();
		assertThat(fromPartitionIds(ourDefaultPartitionId, null).hasDefaultPartitionId(ourDefaultPartitionId)).isTrue();

		assertThat(fromPartitionIds(ourDefaultPartitionId).hasDefaultPartitionId(null)).isFalse();
		assertThat(fromPartitionId(null).hasDefaultPartitionId(ourDefaultPartitionId)).isFalse();
	}

	@Test
	public void testMergeIds() {
		RequestPartitionId input0 = fromPartitionIds(1, 2, 3);
		RequestPartitionId input1 = fromPartitionIds(1, 2, 4);

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = fromPartitionIds(1, 2, 3, 4);
		assertEquals(expected, actual);

		assertEquals(fromPartitionIds(1, 2, 3, 4), fromPartitionIds(1, 2, 3).mergeIds(fromPartitionIds(1, 2, 4)));
		assertEquals(allPartitions(), allPartitions().mergeIds(fromPartitionIds(1, 2, 4)));
		assertEquals(allPartitions(), fromPartitionIds(1, 2, 3).mergeIds(allPartitions()));
		assertEquals(fromPartitionIds(1, 2, 3, null), fromPartitionIds(1, 2, 3).mergeIds(fromPartitionId(null)));
	}

	@Test
	public void testMergeIds_ThisAllPartitions() {
		RequestPartitionId input0 = allPartitions();
		RequestPartitionId input1 = fromPartitionIds(1, 2, 4);

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = allPartitions();
		assertEquals(expected, actual);

	}

	@SuppressWarnings("removal")
	@Test
	public void testGetPartitionFromUserDataIfPresent_None() {

		Patient resource = new Patient();
		assertThat(RequestPartitionId.getPartitionFromUserDataIfPresent(resource)).isNotPresent();
		assertThat(RequestPartitionId.getPartitionIfAssigned(resource)).isNotPresent();
	}

	@SuppressWarnings("removal")
	@Test
	public void testGetPartitionFromUserDataIfPresent_Present() {

		Patient resource = new Patient();
		resource.setUserData(Constants.RESOURCE_PARTITION_ID, RequestPartitionId.fromPartitionIds(1, 2, 3));
		assertEquals(RequestPartitionId.fromPartitionIds(1, 2, 3), RequestPartitionId.getPartitionFromUserDataIfPresent(resource).orElseThrow());
		assertEquals(RequestPartitionId.fromPartitionIds(1, 2, 3), RequestPartitionId.getPartitionIfAssigned(resource).orElseThrow());
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
		RequestPartitionId input1 = fromPartitionId(null);

		RequestPartitionId actual = input0.mergeIds(input1);
		RequestPartitionId expected = fromPartitionIds(1, 2, 3, null);
		assertEquals(expected, actual);

	}

	@ParameterizedTest
	@MethodSource("testCompareToTestCases")
	void testCompareTo(RequestPartitionId thePartitionId0, RequestPartitionId thePartitionId1, ExpectedOrderEnum theExpectedOrder) {
		List<RequestPartitionId> inputLR = new ArrayList<>();
		inputLR.add(thePartitionId0);
		inputLR.add(thePartitionId1);
		inputLR.sort(RequestPartitionId::compareTo);
		switch (theExpectedOrder) {
			case LEFT_FIRST, EQUAL -> {
				assertSame(thePartitionId0, inputLR.get(0));
				assertSame(thePartitionId1, inputLR.get(1));
			}
			case RIGHT_FIRST -> {
				assertSame(thePartitionId0, inputLR.get(1));
				assertSame(thePartitionId1, inputLR.get(0));
			}
		}

		// Now start with the opposite order
		List<RequestPartitionId> inputRL = new ArrayList<>();
		inputRL.add(thePartitionId1);
		inputRL.add(thePartitionId0);
		inputRL.sort(RequestPartitionId::compareTo);
		switch (theExpectedOrder) {
			case LEFT_FIRST -> {
				assertSame(thePartitionId0, inputRL.get(0));
				assertSame(thePartitionId1, inputRL.get(1));
			}
			case RIGHT_FIRST, EQUAL -> {
				assertSame(thePartitionId0, inputRL.get(1));
				assertSame(thePartitionId1, inputRL.get(0));
			}
		}
	}

	static Stream<Object[]> testCompareToTestCases() {
		return Stream.of(
			new Object[] { fromPartitionIds(1, 3, 5), fromPartitionIds(2, 4, 6), ExpectedOrderEnum.LEFT_FIRST },
			new Object[] { fromPartitionIds(1), fromPartitionIds(2, 4, 6), ExpectedOrderEnum.LEFT_FIRST },
			new Object[] { fromPartitionIds(1), fromPartitionId(null), ExpectedOrderEnum.LEFT_FIRST },
			new Object[] { fromPartitionId(null), fromPartitionId(null), ExpectedOrderEnum.EQUAL },
			new Object[] { fromPartitionIds(1,2,3), fromPartitionIds(1,3,5), ExpectedOrderEnum.EQUAL }
		);
	}

	private enum ExpectedOrderEnum {
		LEFT_FIRST,
		RIGHT_FIRST,
		EQUAL
	}

	@ParameterizedTest
	@MethodSource("testStringifyForKeyTestCases")
	public void testStringifyForKey(RequestPartitionId theRequestPartitionId, String theExpectedString) {
		String actual = RequestPartitionId.stringifyForKey(theRequestPartitionId);
		assertEquals(theExpectedString, actual);
	}

	@ParameterizedTest
	@MethodSource("testStringifyForKeyTestCases")
	public void testFromStringifedKey(RequestPartitionId theExpected, String theStringifiedKey) {
		RequestPartitionId actual = RequestPartitionId.fromStringifiedKey(theStringifiedKey);
		assertEquals(theExpected, actual);
	}


	static Stream<Object[]> testStringifyForKeyTestCases() {
		return Stream.of(
			new Object[]{RequestPartitionId.allPartitions(), "(all)"},
			new Object[]{fromPartitionId(null), "null"},
			new Object[]{RequestPartitionId.fromPartitionIds(), ""},
			new Object[]{RequestPartitionId.fromPartitionIds(1), "1"},
			new Object[]{RequestPartitionId.fromPartitionIds(1, 2, 3), "1_2_3"},
			new Object[]{RequestPartitionId.fromPartitionIds(null, 2, 3), "null_2_3"},
			new Object[]{RequestPartitionId.fromPartitionIds(1, null, 3), "1_null_3"},
			new Object[]{RequestPartitionId.allPartitionsWithPartitionIds(1, 2, 3), "(all)_1_2_3"},
			new Object[]{RequestPartitionId.allPartitionsWithPartitionIds(1, null, 3), "(all)_1_null_3"}
		);
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

		@Nonnull
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
		IDefaultPartitionSettings nullDefaultPartition = new IDefaultPartitionSettings() {};
		return new ContainsTestCase[]{
			new ContainsTestCase("all vs all", allPartitions(), allPartitions(), EQUAL),
			new ContainsTestCase("all vs normal", allPartitions(), fromPartitionIds(1, 2, 3), LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("equal partition id lists", fromPartitionIds(1, 2, 3), fromPartitionIds(1, 2, 3), EQUAL),
			new ContainsTestCase("different id lists incomparable", fromPartitionIds(1, 2, 5), fromPartitionIds(1, 2, 9), NEITHER),
			new ContainsTestCase("default as null contains", fromPartitionIds(1, 2, null), defaultPartition(nullDefaultPartition), LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("names equivalent", fromPartitionNames("A", "B"), fromPartitionNames("A", "B"), EQUAL),
			new ContainsTestCase("names left contains right", fromPartitionNames("A", "B"), fromPartitionNames("A"), LEFT_CONTAINS_RIGHT),
			new ContainsTestCase("names left ids right", fromPartitionNames("A", "B"), fromPartitionIds(1, 2), NEITHER),
		};
	}

	@Test
	public void testSerDeserSer() throws JsonProcessingException {
		{
			RequestPartitionId start = fromPartitionId(123, LocalDate.of(2020, 1, 1));
			String json = assertSerDeserSer(start).asJson();
			assertThat(json).contains("\"partitionDate\":[2020,1,1]");
			assertThat(json).contains("\"partitionIds\":[123]");
		}
		{
			RequestPartitionId start = RequestPartitionId.forPartitionIdsAndNames(Lists.newArrayList("Name1", "Name2"), null, null);
			String json = assertSerDeserSer(start).asJson();
			assertThat(json).contains("partitionNames\":[\"Name1\",\"Name2\"]");
		}
		assertSerDeserSer(allPartitions());
		assertSerDeserSer(fromPartitionId(null));
	}


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

	private RequestPartitionId assertSerDeserSer(RequestPartitionId start) throws JsonProcessingException {
		String json = start.asJson();
		RequestPartitionId end = RequestPartitionId.fromJson(json);
		assertEquals(start, end);
		assertEquals(start.asJson(), end.asJson());
		return end;
	}

}
