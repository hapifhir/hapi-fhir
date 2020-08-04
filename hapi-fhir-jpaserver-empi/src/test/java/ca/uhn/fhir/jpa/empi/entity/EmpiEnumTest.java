package ca.uhn.fhir.jpa.empi.entity;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmpiEnumTest {
	@Test
	public void empiEnumOrdinals() {
		// This test is here to enforce that new values in these enums are always added to the end

		assertEquals(5, EmpiMatchResultEnum.values().length);
		assertEquals(EmpiMatchResultEnum.GOLDEN_RECORD, EmpiMatchResultEnum.values()[EmpiMatchResultEnum.values().length - 1]);

		assertEquals(2, EmpiLinkSourceEnum.values().length);
		assertEquals(EmpiLinkSourceEnum.MANUAL, EmpiLinkSourceEnum.values()[EmpiLinkSourceEnum.values().length - 1]);
	}
}
