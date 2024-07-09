package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.util.HapiEntityManagerFactoryUtil;
import ca.uhn.fhir.jpa.dao.r4.ElasticsearchPrefixTest;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import ca.uhn.fhir.jpa.search.elastic.IndexNamePrefixLayoutStrategy;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import ca.uhn.fhir.jpa.test.config.BlockLargeNumbersOfParamsListener;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.PutTemplateResponse;
import co.elastic.clients.json.JsonData;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchIndexSettings;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * The only reason this is its own class is so that we can set a dao config setting before the whole test framework comes online.
 * We need to do this as it is during bean creation that HS bootstrapping occurs.
 */
@Configuration
@Import(TestHSearchAddInConfig.PooledElasticsearchContainerConfig.class)
public class ElasticsearchWithPrefixConfig {

	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setHSearchIndexPrefix(ElasticsearchPrefixTest.ELASTIC_PREFIX);
		return storageSettings;
	}

	@Bean
	public IndexNamePrefixLayoutStrategy indexNamePrefixLayoutStrategy() {
		return new IndexNamePrefixLayoutStrategy();
	}
	@Bean
	public FhirContext fhirContext() {
		return FhirContext.forR4();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(ConfigurableListableBeanFactory theConfigurableListableBeanFactory, FhirContext theFhirContext, JpaStorageSettings theStorageSettings) {
		LocalContainerEntityManagerFactoryBean retVal = HapiEntityManagerFactoryUtil.newEntityManagerFactory(theConfigurableListableBeanFactory, theFhirContext, theStorageSettings);

		retVal.setPersistenceUnitName("PU_HapiFhirJpaR4");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}
	@Bean
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:testdb_r4");
		retVal.setMaxWaitMillis(30000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(5);

		SLF4JLogLevel level = SLF4JLogLevel.INFO;
		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS, level)
			.beforeQuery(new BlockLargeNumbersOfParamsListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.build();

		return dataSource;
	}

	@Autowired ElasticsearchContainer myElasticsearchContainer;
	@Bean
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", HapiFhirH2Dialect.class.getName());
		//Override default lucene settings
		// Force elasticsearch to start first
		int httpPort = myElasticsearchContainer.getMappedPort(9200);//9200 is the HTTP port
		String host = myElasticsearchContainer.getHost();
		// the below properties are used for ElasticSearch integration
		extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "elasticsearch");
		extraProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.ANALYSIS_CONFIGURER),
			HapiHSearchAnalysisConfigurers.HapiElasticsearchAnalysisConfigurer.class.getName());
		extraProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS), host + ":" + httpPort);
		extraProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PROTOCOL), "http");
		extraProperties.put(HibernateOrmMapperSettings.SCHEMA_MANAGEMENT_STRATEGY, SchemaManagementStrategyName.CREATE.externalRepresentation());
		extraProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS_WAIT_TIMEOUT), Long.toString(10000));
		extraProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS), IndexStatus.YELLOW.externalRepresentation());
		// Need the mapping to be dynamic because of terminology indexes.
		extraProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.DYNAMIC_MAPPING), "true");
		// Only for unit tests
		extraProperties.put(HibernateOrmMapperSettings.AUTOMATIC_INDEXING_SYNCHRONIZATION_STRATEGY, "read-sync");
		extraProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.LOG_JSON_PRETTY_PRINTING), Boolean.toString(true));

		//This tells elasticsearch to use our custom index naming strategy.
		extraProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.LAYOUT_STRATEGY), IndexNamePrefixLayoutStrategy.class.getName());

		try {
			ElasticsearchClient elasticsearchHighLevelRestClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient("http", host + ":" + httpPort, "", "");
			PutTemplateResponse acknowledgedResponse = elasticsearchHighLevelRestClient
				.indices()
				.putTemplate(b -> b
					.name("ngram-template")
					.indexPatterns("*resourcetable-*", "*termconcept-*")
					.settings(Map.of("index.max_ngram_diff", JsonData.of(50))));
			assert acknowledgedResponse.acknowledged();
		} catch (IOException theE) {
			theE.printStackTrace();
			throw new ConfigurationException("Couldn't connect to the elasticsearch server to create necessary templates. Ensure the Elasticsearch user has permissions to create templates.");
		}
		return extraProperties;
	}

}
