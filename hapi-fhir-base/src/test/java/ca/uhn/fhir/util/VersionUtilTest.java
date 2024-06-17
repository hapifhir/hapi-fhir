package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class VersionUtilTest {

	@Test
	public void testProperties() {
		assertThat(VersionUtil.getVersion()).isNotEmpty();
		assertThat(VersionUtil.getBuildNumber()).isNotEmpty();
		assertThat(VersionUtil.getBuildTime()).isNotEmpty();
	}


}
