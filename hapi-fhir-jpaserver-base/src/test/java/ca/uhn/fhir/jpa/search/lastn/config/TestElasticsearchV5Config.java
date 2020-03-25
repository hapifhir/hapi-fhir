package ca.uhn.fhir.jpa.search.lastn.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchV5SvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory")
@EnableTransactionManagement
public class TestElasticsearchV5Config {

	private final String elasticsearchHost = "127.0.0.1";
	private final String elasticsearchUserId = "";
	private final String elasticsearchPassword = "";

	private static final String ELASTIC_VERSION = "5.6.16";


	@Bean()
	public ElasticsearchV5SvcImpl myElasticsearchSvc() throws IOException {
		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
		return new ElasticsearchV5SvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
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

}
