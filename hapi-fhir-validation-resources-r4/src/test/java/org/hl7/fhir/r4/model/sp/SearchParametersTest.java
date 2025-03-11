package org.hl7.fhir.r4.model.sp;

import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchParametersTest {

	@Test
	public void testStatus() {
		String resource = ClasspathUtil.loadResource("org/hl7/fhir/r4/model/sp/search-parameters.json");
		assertThat(resource).doesNotContain("\"draft\"");
	}

}
