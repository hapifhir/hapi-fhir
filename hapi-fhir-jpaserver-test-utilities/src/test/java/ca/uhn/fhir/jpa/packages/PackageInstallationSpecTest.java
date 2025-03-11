package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageInstallationSpecTest {

	@Test
	public void testExampleSupplier() throws IOException {
		PackageInstallationSpec output = new PackageInstallationSpec.ExampleSupplier().get();
		String json = JsonUtil.serialize(output);
		assertThat(json).contains("\"name\" : \"hl7.fhir.us.core\"");

		output = new PackageInstallationSpec.ExampleSupplier2().get();
		json = JsonUtil.serialize(output);
		assertThat(json).contains("\"packageUrl\" : \"classpath:/my-resources.tgz\"");
	}

}
