package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-5-5
class MdmMatchClaimKeyTest {

	@Test
	void forSourcePid_usesTypeAndIdOnly() {
		MdmMatchClaimKey key = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L, 7), null);

		assertThat(key.type()).isEqualTo(MdmMatchClaimKey.ClaimTypeEnum.PID);
		assertThat(key.canonicalKey()).isEqualTo("PID|Patient|123");
		assertThat(key.partitionId()).isEqualTo(MdmMatchClaimKey.ALL_PARTITIONS);
	}

	@Test
	void forSourcePid_isEqualRegardlessOfHowThePidWasObtained() {
		MdmMatchClaimKey withPartition = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L, 7), null);
		MdmMatchClaimKey withoutPartition = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L), null);

		assertThat(withPartition).isEqualTo(withoutPartition);
	}

	@Test
	void forEid_usesExactSystemAndValue() {
		MdmMatchClaimKey key = MdmMatchClaimKey.forEid("Patient", new CanonicalEID("http://mrn", "00042", null), null);

		assertThat(key.type()).isEqualTo(MdmMatchClaimKey.ClaimTypeEnum.EID);
		assertThat(key.canonicalKey()).isEqualTo("EID|Patient|http://mrn|00042");
	}

	@Test
	void forEid_withoutValue_isRejected() {
		CanonicalEID eid = new CanonicalEID("http://mrn", "", null);

		assertThatThrownBy(() -> MdmMatchClaimKey.forEid("Patient", eid, null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void pidAndEidKeys_withTheSameText_areDistinct() {
		MdmMatchClaimKey pid = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(1L), null);
		MdmMatchClaimKey eid = MdmMatchClaimKey.forEid("Patient", new CanonicalEID("x", "1", null), null);

		assertThat(pid.canonicalKey()).isNotEqualTo(eid.canonicalKey());
	}

	@Test
	void sortOrder_isByPartitionThenKey() {
		MdmMatchClaimKey b = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.PID, "b", 1);
		MdmMatchClaimKey a1 = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.PID, "a", 1);
		MdmMatchClaimKey c0 = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.PID, "c", 0);

		List<MdmMatchClaimKey> sorted = Stream.of(b, a1, c0).sorted().toList();

		assertThat(sorted).containsExactly(c0, a1, b);
	}
}
