package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@Configuration
public class TestR4ConfigWithElasticSearch extends TestR4Config {

	private static final Logger ourLog = LoggerFactory.getLogger(TestR4ConfigWithElasticSearch.class);
	private static final String ELASTIC_VERSION = "6.5.4";

	@Override
	@Bean
	public Properties jpaProperties() {
		Properties retVal = super.jpaProperties();

		// the belowing properties are used for ElasticSearch integration
		retVal.put("hibernate.search.elasticsearch.analyzer_definition_provider", ElasticsearchMappingProvider.class.getName());
		retVal.put("hibernate.search.default.indexmanager", "elasticsearch");
		retVal.put("hibernate.search.default.elasticsearch.host", "http://127.0.0.1:9200");
		retVal.put("hibernate.search.default.elasticsearch.index_schema_management_strategy", "CREATE");
		retVal.put("hibernate.search.default.elasticsearch.index_management_wait_timeout", "10000");
		retVal.put("hibernate.search.default.elasticsearch.required_index_status", "yellow");

		// Only for unit tests
		retVal.put("hibernate.search.default.elasticsearch.refresh_after_write", "true");

		// Force elasticsearch to start first
		ourLog.info("ElasticSearch started on port: {}", embeddedElasticSearch().getHttpPort());

		return retVal;
	}

	@Bean
	public EmbeddedElastic embeddedElasticSearch() {
		EmbeddedElastic embeddedElastic = null;
		try {
			embeddedElastic = EmbeddedElastic.builder()
				.withElasticVersion(ELASTIC_VERSION)
				.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 0)
				.withSetting(PopularProperties.HTTP_PORT, 9200)
				.withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID())
				.build()
				.start();
		} catch (IOException | InterruptedException e) {
			throw new ConfigurationException(e);
		}

		return embeddedElastic;
	}


	@PreDestroy
	public void stop() {
		embeddedElasticSearch().stop();
	}

}
