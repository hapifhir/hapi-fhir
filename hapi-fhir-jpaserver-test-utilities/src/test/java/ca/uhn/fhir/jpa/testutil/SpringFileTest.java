package ca.uhn.fhir.jpa.testutil;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class SpringFileTest {


	@Test
	public void testNoBadResources() throws Exception {
		String text = IOUtils.toString(SpringFileTest.class.getResourceAsStream("/hapi-fhir-server-resourceproviders-dstu2.xml"));
//		assertThat(text, not(containsString("OperationDefinition")));
	}

}
