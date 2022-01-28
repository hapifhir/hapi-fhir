package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import ca.uhn.fhir.jpa.search.elastic.TestElasticsearchContainerHelper;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PreDestroy;
import java.util.Properties;


@Configuration
public class TestR4ConfigWithElasticSearch extends TestR4Config {

	private static final Logger ourLog = LoggerFactory.getLogger(TestR4ConfigWithElasticSearch.class);

	@Override
	@Bean
	public Properties jpaProperties() {
		Properties retVal = super.jpaProperties();

		//Override default lucene settings
		// Force elasticsearch to start first
		int httpPort = elasticContainer().getMappedPort(9200);//9200 is the HTTP port
		String host = elasticContainer().getHost();

		ourLog.info("Hibernate Search: using elasticsearch - host {} {}", host, httpPort);

		new ElasticsearchHibernatePropertiesBuilder()
			.setDebugIndexSyncStrategy("read-sync")
			.setDebugPrettyPrintJsonLog(true)
			.setIndexSchemaManagementStrategy(SchemaManagementStrategyName.CREATE)
			.setIndexManagementWaitTimeoutMillis(10000)
			.setRequiredIndexStatus(IndexStatus.YELLOW)
			.setHosts(host + ":" + httpPort)
			.setProtocol("http")
			.setUsername("")
			.setPassword("")
			.apply(retVal);

		return retVal;
	}

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

}
