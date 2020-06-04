package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class NpmInstallationSpecTest {

	@Test
	public void testExampleSupplier() throws IOException {
		NpmInstallationSpec output = new NpmInstallationSpec.ExampleSupplier().get();
		String json = JsonUtil.serialize(output);
		assertThat(json, containsString("\"name\" : \"hl7.fhir.us.core\""));

		output = new NpmInstallationSpec.ExampleSupplier2().get();
		json = JsonUtil.serialize(output);
		assertThat(json, containsString("\"packageUrl\" : \"classpath:my-resources.tgz\""));
	}

}
