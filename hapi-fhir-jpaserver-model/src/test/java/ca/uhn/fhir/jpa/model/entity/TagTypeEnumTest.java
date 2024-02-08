package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagTypeEnumTest {

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testOrder() {
		// Ordinals are used in DB columns so the order
		// shouldn't change
		assertThat(TagTypeEnum.TAG.ordinal()).isEqualTo(0);
		assertThat(TagTypeEnum.PROFILE.ordinal()).isEqualTo(1);
		assertThat(TagTypeEnum.SECURITY_LABEL.ordinal()).isEqualTo(2);
	}
	
}
