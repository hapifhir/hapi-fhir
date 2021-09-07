package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.BlockLargeNumbersOfParamsListener;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.search.elastic.HapiElasticsearchAnalysisConfigurer;
import ca.uhn.fhir.jpa.search.elastic.IndexNamePrefixLayoutStrategy;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchIndexSettings;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
public class DummyConfig {

	@Bean
	public DaoConfig daoConfig() {
		return new DaoConfig();
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
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new HapiFhirLocalContainerEntityManagerFactoryBean();
		retVal.setJpaDialect(new HapiFhirHibernateJpaDialect(fhirContext().getLocalizer()));
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.model.entity", "ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
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

	@Bean
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());
		//Override default lucene settings
		// Force elasticsearch to start first
		int httpPort = elasticContainer().getMappedPort(9200);//9200 is the HTTP port
		String host = elasticContainer().getHost();
		// the below properties are used for ElasticSearch integration
		extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "elasticsearch");
		extraProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.ANALYSIS_CONFIGURER), HapiElasticsearchAnalysisConfigurer.class.getName());
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
		return extraProperties;
	}

	@Bean
	public ElasticsearchContainer elasticContainer() {
		ElasticsearchContainer embeddedElasticSearch = TestElasticsearchContainerHelper.getEmbeddedElasticSearch();
		embeddedElasticSearch.start();
		return embeddedElasticSearch;
	}
}
