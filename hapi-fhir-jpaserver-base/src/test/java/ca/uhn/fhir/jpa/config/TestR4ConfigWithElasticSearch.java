package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static java.time.temporal.ChronoUnit.SECONDS;

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

		PutIndexTemplateRequest ngramTemplate = new PutIndexTemplateRequest("ngram-template")
			.patterns(Arrays.asList("resourcetable-*", "termconcept-*"))
			.settings(Settings.builder().put("index.max_ngram_diff", 50));

		ourLog.info("ElasticSearch started on port: {}", httpPort);
		try {
			RestHighLevelClient elasticsearchHighLevelRestClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(host, httpPort, "", "");
			ourLog.info("Adding starter template for large ngram diffs");
			AcknowledgedResponse acknowledgedResponse = elasticsearchHighLevelRestClient.indices().putTemplate(ngramTemplate, RequestOptions.DEFAULT);
			assert acknowledgedResponse.isAcknowledged();
		} catch (IOException theE) {
			theE.printStackTrace();
		}


		new ElasticsearchHibernatePropertiesBuilder()
			.setDebugIndexSyncStrategy("read-sync")
			.setDebugPrettyPrintJsonLog(true)
			.setIndexSchemaManagementStrategy(SchemaManagementStrategyName.CREATE)
			.setIndexManagementWaitTimeoutMillis(10000)
			.setRequiredIndexStatus(IndexStatus.YELLOW)
			.setRestUrl(host+ ":" + httpPort)
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
