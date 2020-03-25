package ca.uhn.fhir.jpa.dao.lastn.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.config.r4.BaseR4Config;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchIndexStatus;
import org.hibernate.search.elasticsearch.cfg.IndexSchemaManagementStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
		basePackages = {"ca.uhn.fhir.jpa.dao"})
@EnableTransactionManagement
public class TestObservationIndexSearchConfig extends TestR4Config {

	final String elasticsearchHost = "127.0.0.1";
	final String elasticsearchUserId = "";
	final String elasticsearchPassword = "";

	private static final String ELASTIC_VERSION = "6.5.4";

	@Override
	public Properties jpaProperties() {

		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", org.hibernate.dialect.H2Dialect.class);
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put(AvailableSettings.HBM2DDL_AUTO, "update");
		extraProperties.put("hibernate.jdbc.batch_size", "5000");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		extraProperties.put("hibernate.search.default.worker.execution", "sync");

		int elasticsearchPort = embeddedElasticSearch().getHttpPort();
		new ElasticsearchHibernatePropertiesBuilder()
				.setDebugRefreshAfterWrite(true)
				.setDebugPrettyPrintJsonLog(true)
				.setIndexSchemaManagementStrategy(IndexSchemaManagementStrategy.CREATE)
				.setIndexManagementWaitTimeoutMillis(10000)
				.setRequiredIndexStatus(ElasticsearchIndexStatus.YELLOW)
				.setRestUrl("http://" + elasticsearchHost + ":" + elasticsearchPort)
				.setUsername(elasticsearchUserId)
				.setPassword(elasticsearchPassword)
				.apply(extraProperties);

		extraProperties.setProperty("hibernate.search.default.elasticsearch.refresh_after_write", "true");
		return extraProperties;
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


}
