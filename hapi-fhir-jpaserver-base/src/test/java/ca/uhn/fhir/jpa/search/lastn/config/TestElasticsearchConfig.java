package ca.uhn.fhir.jpa.search.lastn.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class TestElasticsearchConfig {

	private final String elasticsearchHost = "localhost";
	private final String elasticsearchUserId = "";
	private final String elasticsearchPassword = "";

	private static final String ELASTIC_VERSION = "6.8.13";
	public static final String ELASTIC_IMAGE  = "docker.elastic.co/elasticsearch/elasticsearch:" + ELASTIC_VERSION;


	@Bean
	public ElasticsearchContainer embeddedElasticSearch() {
		try(ElasticsearchContainer container = new ElasticsearchContainer(ELASTIC_IMAGE)) {
			container.withStartupTimeout(Duration.of(60, SECONDS));
			container.start();
			return container;
		}
	}

	@Bean
	@Lazy
	public ElasticsearchSvcImpl myElasticsearchSvc(ElasticsearchContainer theContainer) {
		int elasticsearchPort = theContainer.getMappedPort(9200);
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

//	@Bean
//	public EmbeddedElastic embeddedElasticSearch() {
//		EmbeddedElastic embeddedElastic;
//		try {
//			embeddedElastic = EmbeddedElastic.builder()
//					.withElasticVersion(ELASTIC_VERSION)
//					.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 0)
//					.withSetting(PopularProperties.HTTP_PORT, 0)
//					.withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID())
//					.withStartTimeout(60, TimeUnit.SECONDS)
//					.build()
//					.start();
//		} catch (IOException | InterruptedException e) {
//			throw new ConfigurationException(e);
//		}
//
//		return embeddedElastic;
//	}

	@PreDestroy
	public void stop() throws IOException {
		myElasticsearchSvc(embeddedElasticSearch()).close();
		embeddedElasticSearch().stop();
	}

}
