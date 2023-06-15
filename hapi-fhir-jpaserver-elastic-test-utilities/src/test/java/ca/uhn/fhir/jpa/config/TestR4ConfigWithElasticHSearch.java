package ca.uhn.fhir.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;

/**
 * Combined config so we consistently share app contexts.
 *
 * <p>Spring uses the context classes as the key for reuse between classes, so let's try to use the
 * same config as much as we can.
 */
@Configuration
@Import({TestR4Config.class, TestHSearchAddInConfig.Elasticsearch.class})
public class TestR4ConfigWithElasticHSearch {}
