package ca.uhn.fhir.rest.server.interceptor;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

	@Test
	public void testConfigLoading() {
		Map config = ConfigLoader.loadJson("classpath:field-s13n-rules.json", Map.class);
		assertNotNull(config);
		assertTrue(config.size() > 0);

		Properties props = ConfigLoader.loadProperties("classpath:address-validation.properties");
		assertNotNull(props);
		assertTrue(props.size() > 0);

		String text = ConfigLoader.loadResourceContent("classpath:noise-chars.txt");
		assertNotNull(text);
		assertTrue(text.length() > 0);

		try {
			ConfigLoader.loadResourceContent("blah");
			fail();
		} catch (Exception e) {
		}
	}

}
