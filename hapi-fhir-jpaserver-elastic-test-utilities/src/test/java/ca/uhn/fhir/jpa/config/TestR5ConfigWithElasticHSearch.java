package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Combined config so we consistently share app contexts.
 *
 * Spring uses the context classes as the key for reuse between classes,
 * so let's try to use the same config as much as we can.
 */
@Configuration
@Import({
	TestR5Config.class,
	TestHSearchAddInConfig.Elasticsearch.class
})
public class TestR5ConfigWithElasticHSearch {
}
