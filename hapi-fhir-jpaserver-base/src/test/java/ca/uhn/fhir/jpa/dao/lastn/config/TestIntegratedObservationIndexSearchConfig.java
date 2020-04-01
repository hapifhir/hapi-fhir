package ca.uhn.fhir.jpa.dao.lastn.config;

import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
		basePackages = {"ca.uhn.fhir.jpa.dao.data"})
@EnableTransactionManagement
public class TestIntegratedObservationIndexSearchConfig extends TestObservationIndexSearchConfig {

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() throws IOException {
		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

}
