package org.hl7.fhir.r4.model.sp;

import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class SearchParametersTest {

	@Test
	public void testStatus() {
		String resource = ClasspathUtil.loadResource("org/hl7/fhir/r4/model/sp/search-parameters.json");
		assertThat(resource, not(containsString("\"draft\"")));
	}

}
