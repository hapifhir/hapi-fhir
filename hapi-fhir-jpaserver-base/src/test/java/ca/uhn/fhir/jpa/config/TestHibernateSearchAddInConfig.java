package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class TestHibernateSearchAddInConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(TestHibernateSearchAddInConfig.class);

	/**
	 * Add Hibernate Search config to JPA properties.
	 */
	public interface IHibernateSearchConfigurer {
		void apply(Properties theJPAProperties);
	}

	/**
	 * Our default config - Lucene in-memory or disabled by property;
	 *
	 * wipmb can we move the property to a configuration?
	 */
	@Configuration
	public static class DefaultLuceneOrNone {
		@Autowired
		protected Environment myEnv;

		// wipmb split this into default and NoFT configs.
		boolean isLuceneEnabled() {
			boolean enableLucene = myEnv.getProperty(BaseJpaTest.CONFIG_ENABLE_LUCENE, Boolean.TYPE, BaseJpaTest.CONFIG_ENABLE_LUCENE_DEFAULT_VALUE);
			return enableLucene;
		}

		@Bean
		IHibernateSearchConfigurer hibernateSearchConfigurer() {
			Map<String, String> hibernateSearchProperties = BaseJpaTest.buildHibernateSearchProperties(isLuceneEnabled());
			return (theProperties) -> {
				theProperties.putAll(hibernateSearchProperties);
			};
		}

		@Bean(autowire = Autowire.BY_TYPE)
		public IFulltextSearchSvc searchDaoR4() {
			if (isLuceneEnabled()) {
				ourLog.info("Hibernate Search: FulltextSearchSvcImpl present");
				return new FulltextSearchSvcImpl();
			} else {
				ourLog.info("Hibernate Search: FulltextSearchSvcImpl not available");
				return null;
			}
		}
	}

	/**
	 * Disable Hibernate Search, and do not provide a IFulltextSearchSvc bean.
	 @TestPropertySource(properties = {
	 BaseJpaTest.CONFIG_ENABLE_LUCENE_FALSE
	 })

	 */
	@Configuration
	public static class None {
		@Bean
		IHibernateSearchConfigurer hibernateSearchConfigurer() {
			Map<String, String> hibernateSearchProperties = BaseJpaTest.buildHibernateSearchProperties(false);
			return (theProperties) -> {
				theProperties.putAll(hibernateSearchProperties);
			};
		}

		@Bean(autowire = Autowire.BY_TYPE)
		@Primary
		public IFulltextSearchSvc searchDaoR4() {
			ourLog.info("Hibernate Search: FulltextSearchSvcImpl not available");
			return null;
		}

	}


		@Configuration
	public static class Elasticsearch {
		@Bean
		@Primary // override the default
		IHibernateSearchConfigurer hibernateSearchConfigurer(ElasticsearchContainer theContainer) {
			return (theProperties) -> {
				int httpPort = theContainer.getMappedPort(9200);//9200 is the HTTP port
				String host = theContainer.getHost();

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
					.apply(theProperties);
			};
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

		@Bean
		public PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}

		@Bean()
		public ElasticsearchSvcImpl myElasticsearchSvc() {
			int elasticsearchPort = elasticContainer().getMappedPort(9200);
			String host = elasticContainer().getHost();
			return new ElasticsearchSvcImpl("http", host + ":" + elasticsearchPort, null, null);
		}

		@PreDestroy
		public void stopEsClient() throws IOException {
			myElasticsearchSvc().close();
		}

	}
}
