package ca.uhn.fhir.jpa.mdm.entity;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MdmEnumTest {

	@Test
	public void mdmEnumOrdinals() {
		// This test is here to enforce that new values in these enums are always added to the end
		assertThat(MdmMatchResultEnum.values().length).isEqualTo(6);
		assertThat(MdmMatchResultEnum.values()[MdmMatchResultEnum.values().length - 1]).isEqualTo(MdmMatchResultEnum.REDIRECT);

		assertThat(MdmLinkSourceEnum.values().length).isEqualTo(2);
		assertThat(MdmLinkSourceEnum.values()[MdmLinkSourceEnum.values().length - 1]).isEqualTo(MdmLinkSourceEnum.MANUAL);
	}
}
