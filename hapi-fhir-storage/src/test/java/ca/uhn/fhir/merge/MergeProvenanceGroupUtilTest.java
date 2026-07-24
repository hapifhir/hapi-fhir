package ca.uhn.fhir.merge;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by Claude Fable 5
class MergeProvenanceGroupUtilTest {

	private static final IdDt SOURCE_ID = new IdDt("Patient/src-id-with-dashes");
	private static final IdDt TARGET_ID = new IdDt("Patient/tgt-id-with-dashes");

	@Test
	void generateGroupId_containsTypeAndIdParts_andIsUniquePerInvocation() {
		String groupId1 = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		String groupId2 = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);

		assertThat(groupId1).startsWith("merge-Patient-src-id-with-dashes-tgt-id-with-dashes-");
		assertThat(groupId1).isNotEqualTo(groupId2);
	}

	@Test
	void buildMemberProvenanceGroupValue_numericPartition_roundTrips() {
		String groupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		String groupValue = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, RequestPartitionId.fromPartitionId(42), MergeProvenanceOperation.UPDATE);

		assertThat(groupValue).isEqualTo(groupId + ";partition=42;operation=update");
		assertThat(MergeProvenanceGroupUtil.extractGroupId(groupValue)).isEqualTo(groupId);
		assertThat(MergeProvenanceGroupUtil.extractPartition(groupValue))
				.contains(RequestPartitionId.fromPartitionId(42));
		assertThat(MergeProvenanceGroupUtil.extractOperation(groupValue)).contains(MergeProvenanceOperation.UPDATE);
	}

	@Test
	void buildMemberProvenanceGroupValue_nullIdDefaultPartition_roundTrips() {
		String groupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		RequestPartitionId nullIdDefaultPartition = RequestPartitionId.fromPartitionId((Integer) null);
		String groupValue = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, nullIdDefaultPartition, MergeProvenanceOperation.DELETE);

		assertThat(groupValue).isEqualTo(groupId + ";partition=default;operation=delete");
		assertThat(MergeProvenanceGroupUtil.extractPartition(groupValue)).contains(nullIdDefaultPartition);
		assertThat(MergeProvenanceGroupUtil.extractOperation(groupValue)).contains(MergeProvenanceOperation.DELETE);
	}

	@Test
	void buildMemberProvenanceGroupValue_samePartitionDifferentOperations_producesDistinctValuesWithSameGroupId() {
		String groupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		RequestPartitionId partition = RequestPartitionId.fromPartitionId(1);

		String createGroupValue = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, partition, MergeProvenanceOperation.CREATE);
		String deleteGroupValue = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, partition, MergeProvenanceOperation.DELETE);

		assertThat(createGroupValue).isNotEqualTo(deleteGroupValue);
		assertThat(MergeProvenanceGroupUtil.extractGroupId(createGroupValue)).isEqualTo(groupId);
		assertThat(MergeProvenanceGroupUtil.extractGroupId(deleteGroupValue)).isEqualTo(groupId);
		assertThat(MergeProvenanceGroupUtil.isInGroup(createGroupValue, groupId)).isTrue();
		assertThat(MergeProvenanceGroupUtil.isInGroup(deleteGroupValue, groupId)).isTrue();
	}

	@Test
	void undoOrder_undeletesBeforeUpdatesBeforeDeletes() {
		assertThat(MergeProvenanceOperation.DELETE.getUndoOrder())
				.isLessThan(MergeProvenanceOperation.UPDATE.getUndoOrder());
		assertThat(MergeProvenanceOperation.UPDATE.getUndoOrder())
				.isLessThan(MergeProvenanceOperation.CREATE.getUndoOrder());
	}

	@Test
	void isInGroup_matchesSameGroupIdAcrossPartitions() {
		String groupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		String groupValuePartition1 = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, RequestPartitionId.fromPartitionId(1), MergeProvenanceOperation.UPDATE);
		String groupValuePartition2 = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				groupId, RequestPartitionId.fromPartitionId(2), MergeProvenanceOperation.UPDATE);

		assertThat(MergeProvenanceGroupUtil.isInGroup(groupValuePartition1, groupId)).isTrue();
		assertThat(MergeProvenanceGroupUtil.isInGroup(groupValuePartition2, groupId)).isTrue();
		assertThat(MergeProvenanceGroupUtil.isInGroup(groupId, groupId)).isTrue();
	}

	@Test
	void isInGroup_rejectsOtherGroups() {
		String groupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		String otherGroupId = MergeProvenanceGroupUtil.generateGroupId(SOURCE_ID, TARGET_ID);
		String otherGroupValue = MergeProvenanceGroupUtil.buildMemberProvenanceGroupValue(
				otherGroupId, RequestPartitionId.fromPartitionId(1), MergeProvenanceOperation.UPDATE);

		assertThat(MergeProvenanceGroupUtil.isInGroup(otherGroupValue, groupId)).isFalse();
		assertThat(MergeProvenanceGroupUtil.isInGroup(groupId, groupId + "-longer")).isFalse();
	}

	@Test
	void isInGroup_groupIdThatIsPrefixOfAnotherDoesNotMatch() {
		String groupId = "merge-Patient-a-b-uuid";
		String lookalike = "merge-Patient-a-b-uuid2;partition=1";

		assertThat(MergeProvenanceGroupUtil.isInGroup(lookalike, groupId)).isFalse();
	}

	@Test
	void extractPartition_noSuffix_returnsEmpty() {
		assertThat(MergeProvenanceGroupUtil.extractPartition("merge-Patient-a-b-uuid")).isEmpty();
	}

	@Test
	void extractPartition_invalidSuffix_throws() {
		assertThatThrownBy(() -> MergeProvenanceGroupUtil.extractPartition("merge-Patient-a-b-uuid;partition=abc"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid partition id");
	}

	@Test
	void extractPartition_stopsAtOperationDelimiter() {
		assertThat(MergeProvenanceGroupUtil.extractPartition("merge-Patient-a-b-uuid;partition=7;operation=create"))
				.contains(RequestPartitionId.fromPartitionId(7));
	}

	@Test
	void extractOperation_noSuffix_returnsEmpty() {
		assertThat(MergeProvenanceGroupUtil.extractOperation("merge-Patient-a-b-uuid;partition=1"))
				.isEmpty();
	}

	@Test
	void extractOperation_invalidSuffix_throws() {
		assertThatThrownBy(() ->
						MergeProvenanceGroupUtil.extractOperation("merge-Patient-a-b-uuid;partition=1;operation=bogus"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid operation");
	}
}
