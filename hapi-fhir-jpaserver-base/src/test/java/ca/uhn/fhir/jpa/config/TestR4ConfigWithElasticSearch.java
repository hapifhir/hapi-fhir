package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchIndexStatus;
import org.hibernate.search.elasticsearch.cfg.IndexSchemaManagementStrategy;
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
import java.util.concurrent.TimeUnit;

@Configuration
public class TestR4ConfigWithElasticSearch extends TestR4Config {

	private static final Logger ourLog = LoggerFactory.getLogger(TestR4ConfigWithElasticSearch.class);
	private static final String ELASTIC_VERSION = "6.5.4";

	@Override
	@Bean
	public Properties jpaProperties() {
		Properties retVal = super.jpaProperties();

		// Force elasticsearch to start first
		int httpPort = embeddedElasticSearch().getHttpPort();
		ourLog.info("ElasticSearch started on port: {}", httpPort);

		new ElasticsearchHibernatePropertiesBuilder()
			.setDebugRefreshAfterWrite(true)
			.setDebugPrettyPrintJsonLog(true)
			.setIndexSchemaManagementStrategy(IndexSchemaManagementStrategy.CREATE)
			.setIndexManagementWaitTimeoutMillis(10000)
			.setRequiredIndexStatus(ElasticsearchIndexStatus.YELLOW)
			.setRestUrl("http://localhost:" + httpPort)
			.setUsername("")
			.setPassword("")
			.apply(retVal);

		return retVal;
	}

	@Bean
	public EmbeddedElastic embeddedElasticSearch() {
		EmbeddedElastic embeddedElastic = null;
		try {
			embeddedElastic = EmbeddedElastic.builder()
				.withElasticVersion(ELASTIC_VERSION)
				.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 0)
				.withSetting(PopularProperties.HTTP_PORT, 0)
				.withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID())
				.withStartTimeout(60, TimeUnit.SECONDS)
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
