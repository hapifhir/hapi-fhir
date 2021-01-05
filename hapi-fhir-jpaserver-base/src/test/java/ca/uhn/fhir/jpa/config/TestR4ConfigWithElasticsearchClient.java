package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class TestR4ConfigWithElasticsearchClient extends TestR4ConfigWithElasticSearch {


	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() {
		int elasticsearchPort = elasticContainer().getMappedPort(9200);
		String host = elasticContainer().getHost();
		return new ElasticsearchSvcImpl(host, elasticsearchPort, "", "");
	}

	@PreDestroy
	public void stopEsClient() throws IOException {
		myElasticsearchSvc().close();
	}

}
