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
		MdmMatchClaimKey key = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L, 7));

		assertThat(key.type()).isEqualTo(MdmMatchClaimKey.ClaimTypeEnum.PID);
		assertThat(key.canonicalKey()).isEqualTo("PID|Patient|123");
	}

	@Test
	void forSourcePid_isEqualRegardlessOfHowThePidWasObtained() {
		MdmMatchClaimKey withPartition = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L, 7));
		MdmMatchClaimKey withoutPartition = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(123L));

		assertThat(withPartition).isEqualTo(withoutPartition);
	}

	@Test
	void forEid_usesExactSystemAndValue() {
		MdmMatchClaimKey key = MdmMatchClaimKey.forEid("Patient", new CanonicalEID("http://mrn", "00042", null));

		assertThat(key.type()).isEqualTo(MdmMatchClaimKey.ClaimTypeEnum.EID);
		assertThat(key.canonicalKey()).isEqualTo("EID|Patient|http://mrn|00042");
	}

	@Test
	void forEid_withoutValue_isRejected() {
		CanonicalEID eid = new CanonicalEID("http://mrn", "", null);

		assertThatThrownBy(() -> MdmMatchClaimKey.forEid("Patient", eid))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void pidAndEidKeys_withTheSameText_areDistinct() {
		MdmMatchClaimKey pid = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(1L));
		MdmMatchClaimKey eid = MdmMatchClaimKey.forEid("Patient", new CanonicalEID("x", "1", null));

		assertThat(pid.canonicalKey()).isNotEqualTo(eid.canonicalKey());
	}

	@Test
	void sortOrder_isByCanonicalKey() {
		MdmMatchClaimKey b = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.PID, "b");
		MdmMatchClaimKey a = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.EID, "a");
		MdmMatchClaimKey c = new MdmMatchClaimKey(MdmMatchClaimKey.ClaimTypeEnum.PID, "c");

		List<MdmMatchClaimKey> sorted = Stream.of(b, c, a).sorted().toList();

		assertThat(sorted).containsExactly(a, b, c);
	}
}
