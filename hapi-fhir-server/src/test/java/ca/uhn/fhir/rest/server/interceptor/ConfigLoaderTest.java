package ca.uhn.fhir.rest.server.interceptor;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


class ConfigLoaderTest {

	@Test
	public void testConfigLoading() {
		Map config = ConfigLoader.loadJson("classpath:field-s13n-rules.json", Map.class);
		assertThat(config).isNotNull();
		assertThat(config.size() > 0).isTrue();

		Properties props = ConfigLoader.loadProperties("classpath:address-validation.properties");
		assertThat(props).isNotNull();
		assertThat(props.size() > 0).isTrue();

		String text = ConfigLoader.loadResourceContent("classpath:noise-chars.txt");
		assertThat(text).isNotNull();
		assertThat(text.length() > 0).isTrue();

		try {
			ConfigLoader.loadResourceContent("blah");
			fail("");		} catch (Exception e) {
		}
	}

}
