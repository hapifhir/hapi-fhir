package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.i18n.HapiLocalizer;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TermValueSetPreExpansionStatusEnumTest {
	@Test
	public void testHaveDescriptions() {
		HapiLocalizer localizer = new HapiLocalizer();

		for (TermValueSetPreExpansionStatusEnum next : TermValueSetPreExpansionStatusEnum.values()) {
			String key = "ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum." + next.getCode();
			String msg = localizer.getMessage(key);
			if (msg.equals(HapiLocalizer.UNKNOWN_I18N_KEY_MESSAGE)) {
				fail("No value for key: " + key);
			}
		}
	}
}
