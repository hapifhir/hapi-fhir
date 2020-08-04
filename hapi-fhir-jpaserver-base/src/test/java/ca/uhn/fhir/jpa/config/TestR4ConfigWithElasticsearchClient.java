package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class TestR4ConfigWithElasticsearchClient extends TestR4ConfigWithElasticSearch {

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() {
		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

	@PreDestroy
	public void stopEsClient() throws IOException {
		myElasticsearchSvc().close();
	}

}
