package ca.uhn.fhir.jpa.mdm.entity;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MdmEnumTest {

	@Test
	public void mdmEnumOrdinals() {
		// This test is here to enforce that new values in these enums are always added to the end
		assertEquals(6, MdmMatchResultEnum.values().length);
		assertEquals(MdmMatchResultEnum.REDIRECT, MdmMatchResultEnum.values()[MdmMatchResultEnum.values().length - 1]);

		assertEquals(2, MdmLinkSourceEnum.values().length);
		assertEquals(MdmLinkSourceEnum.MANUAL, MdmLinkSourceEnum.values()[MdmLinkSourceEnum.values().length - 1]);
	}
}
