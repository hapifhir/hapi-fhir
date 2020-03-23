package ca.uhn.fhir.empi.jpalink.entity;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.rules.EmpiMatchResultEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmpiEnumTest {
	@Test
	public void empiEnumOrdinals() {
		// This test is here to enforce that new values in these enums are always added to the end

		assertEquals(3, EmpiMatchResultEnum.values().length);
		assertEquals(EmpiMatchResultEnum.MATCH, EmpiMatchResultEnum.values()[EmpiMatchResultEnum.values().length - 1]);

		assertEquals(2, EmpiLinkSourceEnum.values().length);
		assertEquals(EmpiLinkSourceEnum.MANUAL, EmpiLinkSourceEnum.values()[EmpiLinkSourceEnum.values().length - 1]);
	}
}
