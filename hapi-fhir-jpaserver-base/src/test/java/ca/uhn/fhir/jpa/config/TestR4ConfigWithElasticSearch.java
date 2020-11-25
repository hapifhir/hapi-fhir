package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
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
	//TODO GGG HS update this to 7.9
	private static final String ELASTIC_VERSION = "6.5.4";
	protected final String elasticsearchHost = "localhost";
	protected final String elasticsearchUserId = "";
	protected final String elasticsearchPassword = "";


	@Override
	@Bean
	public Properties jpaProperties() {
		Properties retVal = super.jpaProperties();

		//Override default lucene settings
		// Force elasticsearch to start first
		int httpPort = embeddedElasticSearch().getHttpPort();
		ourLog.info("ElasticSearch started on port: {}", httpPort);


		new ElasticsearchHibernatePropertiesBuilder()
			//TODO GGG HS According to these docs (https://docs.jboss.org/hibernate/search/6.0/migration/html_single/#_configuration_property_reference) the best approximation to `refreshAfterWrite=True`,
			//Which was the previous behaviour
			.setDebugIndexSyncStrategy("read-sync")
			.setDebugPrettyPrintJsonLog(true)
			.setIndexSchemaManagementStrategy(SchemaManagementStrategyName.CREATE)
			.setIndexManagementWaitTimeoutMillis(10000)
			.setRequiredIndexStatus(IndexStatus.YELLOW)
			.setRestUrl(elasticsearchHost + ":" + httpPort)
			.setProtocol("http")
			.setUsername(elasticsearchUserId)
			.setPassword(elasticsearchPassword)
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
