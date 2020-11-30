package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class TestR4ConfigWithElasticSearch extends TestR4Config {

	private static final Logger ourLog = LoggerFactory.getLogger(TestR4ConfigWithElasticSearch.class);
	public static final String ELASTIC_VERSION = "7.9.2";
	public static final String ELASTIC_IMAGE  = "docker.elastic.co/elasticsearch/elasticsearch:" + ELASTIC_VERSION;
	protected final String elasticsearchHost = "localhost";
	protected final String elasticsearchUserId = "";
	protected final String elasticsearchPassword = "";


	@Override
	@Bean
	public Properties jpaProperties() {
		Properties retVal = super.jpaProperties();

		//Override default lucene settings
		// Force elasticsearch to start first
		int httpPort = elasticContainer().getMappedPort(9200);//9200 is the HTTP port
		ourLog.info("ElasticSearch started on port: {}", httpPort);


		new ElasticsearchHibernatePropertiesBuilder()
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
	public ElasticsearchContainer elasticContainer() {
		ElasticsearchContainer container = new ElasticsearchContainer(ELASTIC_IMAGE);
		container.withStartupTimeout(Duration.of(60, SECONDS));
		container.start();
		return container;
	}


	@PreDestroy
	public void stop() {
		elasticContainer().stop();
	}

}
