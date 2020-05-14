package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestR4ConfigWithElasticsearchClient extends TestR4ConfigWithElasticSearch {

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() {
		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
//		int elasticsearchPort = 9301;
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

}
