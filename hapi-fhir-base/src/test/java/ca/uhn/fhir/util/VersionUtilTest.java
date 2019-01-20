package ca.uhn.fhir.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.junit.Assert.*;

public class VersionUtilTest {

	@Test
	public void testProperties() {
		assertThat(VersionUtil.getVersion(), not(blankOrNullString()));
		assertThat(VersionUtil.getBuildNumber(), not(blankOrNullString()));
		assertThat(VersionUtil.getBuildTime(), not(blankOrNullString()));
	}


}
