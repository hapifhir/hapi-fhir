package ca.uhn.fhir.util;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;

import org.junit.jupiter.api.Test;

public class VersionUtilTest {

    @Test
    public void testProperties() {
        assertThat(VersionUtil.getVersion(), not(blankOrNullString()));
        assertThat(VersionUtil.getBuildNumber(), not(blankOrNullString()));
        assertThat(VersionUtil.getBuildTime(), not(blankOrNullString()));
    }
}
