package ca.uhn.fhir.jpa.search.lastn.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class TestElasticsearchConfig {

	private final String elasticsearchHost = "localhost";
	private final String elasticsearchUserId = "";
	private final String elasticsearchPassword = "";

	private static final String ELASTIC_VERSION = "6.5.4";


	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() {
		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

	@Bean
	public EmbeddedElastic embeddedElasticSearch() {
		EmbeddedElastic embeddedElastic;
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
	public void stop() throws IOException {
		myElasticsearchSvc().close();
		embeddedElasticSearch().stop();
	}

}
