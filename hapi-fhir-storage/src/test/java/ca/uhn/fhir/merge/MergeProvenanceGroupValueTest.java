package ca.uhn.fhir.merge;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

// Created by Claude Opus 4.8
class MergeProvenanceGroupValueTest {

	private static final IdDt SOURCE_ID = new IdDt("Patient/src-id-with-dashes");
	private static final IdDt TARGET_ID = new IdDt("Patient/tgt-id-with-dashes");

	@Nested
	class ValidInput {

		@Test
		void newGroup_containsTypeAndIdParts_andIsUniquePerInvocation() {
			MergeProvenanceGroupValue group1 = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			MergeProvenanceGroupValue group2 = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);

			assertThat(group1.getGroupId()).startsWith("merge|Patient|src-id-with-dashes|tgt-id-with-dashes|");
			assertThat(group1.getGroupId()).isNotEqualTo(group2.getGroupId());
			assertThat(group1.isMain()).isTrue();
			assertThat(group1.getPartition()).isEmpty();
			assertThat(group1.getChangeType()).isEmpty();
			assertThat(group1.encode()).isEqualTo(group1.getGroupId());
		}

		static Stream<Arguments> validPartitionIdsAndChangeTypes() {
			return Stream.of(
					arguments(0, "0", MergeChangeType.CREATE), // zero
					arguments(7, "7", MergeChangeType.UPDATE), // single digit
					arguments(42, "42", MergeChangeType.DELETE), // two digits
					arguments(null, "null", MergeChangeType.UPDATE)); // null-id default partition
		}

		@ParameterizedTest
		@MethodSource("validPartitionIdsAndChangeTypes")
		void member_roundTripsThroughEncodeAndParse(
				Integer thePartitionId, String theExpectedPartitionValue, MergeChangeType theChangeType) {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			RequestPartitionId partition = RequestPartitionId.fromPartitionId(thePartitionId);

			String encoded = group.member(partition, theChangeType).encode();
			assertThat(encoded)
					.isEqualTo(group.getGroupId() + ";partition=" + theExpectedPartitionValue + ";changeType="
							+ theChangeType.getCode());

			MergeProvenanceGroupValue parsed = MergeProvenanceGroupValue.parse(encoded);
			assertThat(parsed.getGroupId()).isEqualTo(group.getGroupId());
			assertThat(parsed.getPartition()).hasValue(partition);
			assertThat(parsed.getChangeType()).hasValue(theChangeType);
			assertThat(parsed.isMain()).isFalse();
		}

		@Test
		void member_samePartitionDifferentOperations_produceDistinctValuesInSameGroup() {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			RequestPartitionId partition = RequestPartitionId.fromPartitionId(1);

			String createEncoded = group.member(partition, MergeChangeType.CREATE).encode();
			String deleteEncoded = group.member(partition, MergeChangeType.DELETE).encode();

			assertThat(createEncoded).isNotEqualTo(deleteEncoded);
			assertThat(MergeProvenanceGroupValue.parse(createEncoded).getGroupId()).isEqualTo(group.getGroupId());
			assertThat(MergeProvenanceGroupValue.parse(deleteEncoded).getGroupId()).isEqualTo(group.getGroupId());
			assertThat(MergeProvenanceGroupValue.parse(createEncoded).isSameGroup(group.getGroupId())).isTrue();
		}

		@Test
		void isSameGroup_matchesSameGroupIdAcrossPartitions() {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			String groupId = group.getGroupId();

			MergeProvenanceGroupValue member1 =
					group.member(RequestPartitionId.fromPartitionId(1), MergeChangeType.UPDATE);
			MergeProvenanceGroupValue member2 =
					group.member(RequestPartitionId.fromPartitionId(2), MergeChangeType.UPDATE);

			assertThat(member1.isSameGroup(groupId)).isTrue();
			assertThat(member2.isSameGroup(groupId)).isTrue();
			assertThat(group.isSameGroup(groupId)).isTrue();
		}

		@Test
		void isSameGroup_rejectsOtherGroups() {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			MergeProvenanceGroupValue otherGroup = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);

			assertThat(otherGroup
							.member(RequestPartitionId.fromPartitionId(1), MergeChangeType.UPDATE)
							.isSameGroup(group.getGroupId()))
					.isFalse();
			assertThat(group.isSameGroup(group.getGroupId() + "-longer")).isFalse();
		}

		@Test
		void parse_groupIdThatIsPrefixOfAnotherDoesNotMatch() {
			MergeProvenanceGroupValue lookalike =
					MergeProvenanceGroupValue.parse("merge|Patient|a|b|uuid2;partition=1;changeType=update");
			assertThat(lookalike.isSameGroup("merge|Patient|a|b|uuid")).isFalse();
			assertThat(lookalike.getGroupId()).isEqualTo("merge|Patient|a|b|uuid2");
		}

		@Test
		void parse_bareGroupId_isMainWithNoQualifiers() {
			MergeProvenanceGroupValue parsed = MergeProvenanceGroupValue.parse("merge|Patient|a|b|uuid");
			assertThat(parsed.isMain()).isTrue();
			assertThat(parsed.getPartition()).isEmpty();
			assertThat(parsed.getChangeType()).isEmpty();
		}

		@Test
		void parse_qualifiedGroupValue_isNotMainWithQualifiers() {
			MergeProvenanceGroupValue parsed =
					MergeProvenanceGroupValue.parse("merge|Patient|a|b|uuid;partition=1;changeType=update");
			assertThat(parsed.isMain()).isFalse();
			assertThat(parsed.getPartition()).hasValue(RequestPartitionId.fromPartitionId(1));
			assertThat(parsed.getChangeType()).hasValue(MergeChangeType.UPDATE);
		}
	}

	@Nested
	class InvalidInput {

		@Test
		void newGroup_nullSource_throws() {
			assertThatThrownBy(() -> MergeProvenanceGroupValue.newGroup(null, TARGET_ID))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("requires a source id");
		}

		@Test
		void newGroup_nullTarget_throws() {
			assertThatThrownBy(() -> MergeProvenanceGroupValue.newGroup(SOURCE_ID, null))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("requires a target id");
		}

		@Test
		void member_partitionThatIsNotASinglePartition_throws() {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);

			assertThatThrownBy(() -> group.member(RequestPartitionId.allPartitions(), MergeChangeType.UPDATE))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageStartingWith(Msg.code(3014))
					.hasMessageContaining("must record exactly one partition");

			assertThatThrownBy(() -> group.member(RequestPartitionId.fromPartitionIds(1, 2), MergeChangeType.UPDATE))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageStartingWith(Msg.code(3014))
					.hasMessageContaining("must record exactly one partition");

			assertThatThrownBy(() ->
							group.member(RequestPartitionId.allPartitionsWithPartitionIds(5), MergeChangeType.UPDATE))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageStartingWith(Msg.code(3014))
					.hasMessageContaining("must record exactly one partition");
		}

		@ParameterizedTest
		@EnumSource(MergeChangeType.class)
		void member_nullPartition_throws(MergeChangeType theChangeType) {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			assertThatThrownBy(() -> group.member(null, theChangeType))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("requires a partition");
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(ints = {0, 7, 42})
		void member_nullChangeType_throws(Integer thePartitionId) {
			MergeProvenanceGroupValue group = MergeProvenanceGroupValue.newGroup(SOURCE_ID, TARGET_ID);
			assertThatThrownBy(() -> group.member(RequestPartitionId.fromPartitionId(thePartitionId), null))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("requires a change type");
		}

		@Test
		void parse_invalidPartition_throws() {
			assertThatThrownBy(
							() -> MergeProvenanceGroupValue.parse("merge|Patient|a|b|uuid;partition=abc;changeType=update"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageStartingWith(Msg.code(2975))
					.hasMessageContaining("Invalid partition id");
		}

		@Test
		void parse_invalidChangeType_throws() {
			assertThatThrownBy(
							() -> MergeProvenanceGroupValue.parse("merge|Patient|a|b|uuid;partition=1;changeType=bogus"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageStartingWith(Msg.code(3012))
					.hasMessageContaining("Invalid change type");
		}
	}
}
