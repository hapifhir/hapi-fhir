package ca.uhn.fhir.jpa.search.lastn.config;

import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory")
@EnableTransactionManagement
public class ElasticsearchConfig {

	private final String elasticsearchHost = "127.0.0.1";
	private final Integer elasticsearchPort = 9301;
	private final String elasticsearchUserId = "elastic";
	private final String elasticsearchPassword = "changeme";

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() throws IOException {
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

}
