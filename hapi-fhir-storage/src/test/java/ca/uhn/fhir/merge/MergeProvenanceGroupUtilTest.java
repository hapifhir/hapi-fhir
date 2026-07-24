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
	void generateGroupIdPrefix_containsTypeAndIdParts_andIsUniquePerInvocation() {
		String prefix1 = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String prefix2 = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);

		assertThat(prefix1).startsWith("merge-Patient-src-id-with-dashes-tgt-id-with-dashes-");
		assertThat(prefix1).isNotEqualTo(prefix2);
	}

	@Test
	void buildGroupId_numericPartition_roundTrips() {
		String prefix = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String groupId = MergeProvenanceGroupUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(42));

		assertThat(groupId).isEqualTo(prefix + ";partition=42");
		assertThat(MergeProvenanceGroupUtil.extractGroupIdPrefix(groupId)).isEqualTo(prefix);
		assertThat(MergeProvenanceGroupUtil.extractPartition(groupId))
				.contains(RequestPartitionId.fromPartitionId(42));
	}

	@Test
	void buildGroupId_defaultPartition_roundTrips() {
		String prefix = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		RequestPartitionId defaultPartition = RequestPartitionId.fromPartitionId((Integer) null);
		String groupId = MergeProvenanceGroupUtil.buildGroupId(prefix, defaultPartition);

		assertThat(groupId).isEqualTo(prefix + ";partition=default");
		assertThat(MergeProvenanceGroupUtil.extractPartition(groupId)).contains(defaultPartition);
	}

	@Test
	void isInGroup_matchesSamePrefixAcrossPartitions() {
		String prefix = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String groupIdPartition1 = MergeProvenanceGroupUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(1));
		String groupIdPartition2 = MergeProvenanceGroupUtil.buildGroupId(prefix, RequestPartitionId.fromPartitionId(2));

		assertThat(MergeProvenanceGroupUtil.isInGroup(groupIdPartition1, prefix)).isTrue();
		assertThat(MergeProvenanceGroupUtil.isInGroup(groupIdPartition2, prefix)).isTrue();
		assertThat(MergeProvenanceGroupUtil.isInGroup(prefix, prefix)).isTrue();
	}

	@Test
	void isInGroup_rejectsOtherGroups() {
		String prefix = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String otherPrefix = MergeProvenanceGroupUtil.generateGroupIdPrefix(SOURCE_ID, TARGET_ID);
		String otherGroupId = MergeProvenanceGroupUtil.buildGroupId(otherPrefix, RequestPartitionId.fromPartitionId(1));

		assertThat(MergeProvenanceGroupUtil.isInGroup(otherGroupId, prefix)).isFalse();
		assertThat(MergeProvenanceGroupUtil.isInGroup(prefix, prefix + "-longer")).isFalse();
	}

	@Test
	void isInGroup_prefixOfAnotherPrefixDoesNotMatch() {
		String prefix = "merge-Patient-a-b-uuid";
		String lookalike = "merge-Patient-a-b-uuid2;partition=1";

		assertThat(MergeProvenanceGroupUtil.isInGroup(lookalike, prefix)).isFalse();
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
}
