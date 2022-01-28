package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class TestElasticSearchAddInConfig {

	@Bean
	public ElasticsearchContainer elasticContainer() {
		ElasticsearchContainer embeddedElasticSearch = TestElasticsearchContainerHelper.getEmbeddedElasticSearch();
		embeddedElasticSearch.start();
		return embeddedElasticSearch;
	}

	@PreDestroy
	public void stop() {
		elasticContainer().stop();
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() {
		int elasticsearchPort = elasticContainer().getMappedPort(9200);
		String host = elasticContainer().getHost();
		return new ElasticsearchSvcImpl("http", host + ":" + elasticsearchPort, null, null);
	}

	@PreDestroy
	public void stopEsClient() throws IOException {
		myElasticsearchSvc().close();
	}

}
