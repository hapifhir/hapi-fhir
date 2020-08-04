package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;

public class VersionUtilTest {

	@Test
	public void testProperties() {
		assertThat(VersionUtil.getVersion(), not(blankOrNullString()));
		assertThat(VersionUtil.getBuildNumber(), not(blankOrNullString()));
		assertThat(VersionUtil.getBuildTime(), not(blankOrNullString()));
	}


}
