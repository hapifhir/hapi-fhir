package ca.uhn.fhir.merge;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by Claude Fable 5
class MergeProvenanceGroupIdUtilTest {

	private static final IdDt SOURCE_ID = new IdDt("Patient/src-id-with-dashes");
	private static final IdDt TARGET_ID = new IdDt("Patient/tgt-id-with-dashes");

	@Test
	void generateGroupIdPrefix_containsTypeAndIdParts_andIsUniquePerInvocation() {
		String prefix1 = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String prefix2 = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);

		assertThat(prefix1).startsWith("merge-Patient-src-id-with-dashes-tgt-id-with-dashes-");
		assertThat(prefix1).isNotEqualTo(prefix2);
	}

	@Test
	void buildGroupId_numericPartition_roundTrips() {
		String prefix = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String groupId = MergeProvenanceGroupIdUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(42));

		assertThat(groupId).isEqualTo(prefix + ";partition=42");
		assertThat(MergeProvenanceGroupIdUtil.extractGroupIdPrefix(groupId)).isEqualTo(prefix);
		assertThat(MergeProvenanceGroupIdUtil.extractPartition(groupId))
				.isEqualTo(RequestPartitionId.fromPartitionId(42));
	}

	@Test
	void buildGroupId_defaultPartition_roundTrips() {
		String prefix = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		RequestPartitionId defaultPartition = RequestPartitionId.fromPartitionId((Integer) null);
		String groupId = MergeProvenanceGroupIdUtil.buildGroupId(prefix, defaultPartition);

		assertThat(groupId).isEqualTo(prefix + ";partition=default");
		assertThat(MergeProvenanceGroupIdUtil.extractPartition(groupId)).isEqualTo(defaultPartition);
	}

	@Test
	void isInGroup_matchesSamePrefixAcrossPartitions() {
		String prefix = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String groupIdPartition1 = MergeProvenanceGroupIdUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(1));
		String groupIdPartition2 = MergeProvenanceGroupIdUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(2));

		assertThat(MergeProvenanceGroupIdUtil.isInGroup(groupIdPartition1, prefix)).isTrue();
		assertThat(MergeProvenanceGroupIdUtil.isInGroup(groupIdPartition2, prefix)).isTrue();
		assertThat(MergeProvenanceGroupIdUtil.isInGroup(prefix, prefix)).isTrue();
	}

	@Test
	void isInGroup_rejectsOtherGroupsAndNull() {
		String prefix = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String otherPrefix = MergeProvenanceGroupIdUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String otherGroupId = MergeProvenanceGroupIdUtil.buildGroupId(otherPrefix, RequestPartitionId.fromPartitionId(1));

		assertThat(MergeProvenanceGroupIdUtil.isInGroup(otherGroupId, prefix)).isFalse();
		assertThat(MergeProvenanceGroupIdUtil.isInGroup(null, prefix)).isFalse();
		assertThat(MergeProvenanceGroupIdUtil.isInGroup(prefix, prefix + "-longer")).isFalse();
	}

	@Test
	void isInGroup_prefixOfAnotherPrefixDoesNotMatch() {
		String prefix = "merge-Patient-a-b-uuid";
		String lookalike = "merge-Patient-a-b-uuid2;partition=1";

		assertThat(MergeProvenanceGroupIdUtil.isInGroup(lookalike, prefix)).isFalse();
	}

	@Test
	void extractPartition_noSuffix_returnsNull() {
		assertThat(MergeProvenanceGroupIdUtil.extractPartition("merge-Patient-a-b-uuid")).isNull();
	}

	@Test
	void extractPartition_invalidSuffix_throws() {
		assertThatThrownBy(() -> MergeProvenanceGroupIdUtil.extractPartition("merge-Patient-a-b-uuid;partition=abc"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid partition id");
	}
}
