package ca.uhn.fhir.jpa.test.config;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.backend.lucene.cfg.LuceneIndexSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configurations for Hibernate Search: off, lucene in-memory, lucene on file system or elastic.
 *
 * We use {@link DefaultLuceneHeap} by default in our JPA test configs.
 * Turn off by adding {@link NoFT} to the test Contexts.
 * Use Elasticsearch instead via docker by adding {@link Elasticsearch} to the test Contexts;
 */
public class TestHibernateSearchAddInConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(TestHibernateSearchAddInConfig.class);

	/**
	 * Add Hibernate Search config to JPA properties.
	 */
	public interface IHibernateSearchConfigurer {
		void apply(Properties theJPAProperties);
	}

	/**
	 * Lucene on file system. Useful for debugging
	 * Uses temporary directory by default. Replace by permanent directory for debugging
	 */
	@Configuration
	public static class LuceneFilesystem {

		@Bean
		@Primary
		IHibernateSearchConfigurer hibernateSearchConfigurer() throws IOException {
			ourLog.warn("Hibernate Search: using lucene - filesystem");

			// replace by existing directory for debugging purposes
			Path tempDirPath = Files.createTempDirectory(null);
			String dirPath = tempDirPath.toString();


			Map<String, String> luceneProperties = new HashMap<>();
			luceneProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
			luceneProperties.put(BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER), HapiLuceneAnalysisConfigurer.class.getName());
			luceneProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-filesystem");
			luceneProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_ROOT), dirPath);
			ourLog.info("Using lucene root dir: {}", dirPath);
			luceneProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
			// for lucene trace logging
			luceneProperties.put(BackendSettings.backendKey(LuceneIndexSettings.IO_WRITER_INFOSTREAM), "true");
			luceneProperties.put(HibernateOrmMapperSettings.ENABLED, "true");

			return (theProperties) ->
				theProperties.putAll(luceneProperties);
		}


		public IFulltextSearchSvc fullTextSearchSvc() {
			ourLog.info("Hibernate Search: FulltextSearchSvcImpl present");
			return new FulltextSearchSvcImpl();
		}
	}


	/**
	 * Our default config - Lucene in-memory.
	 */
	@Configuration
	public static class DefaultLuceneHeap {

		@Bean
		IHibernateSearchConfigurer hibernateSearchConfigurer() {
			ourLog.warn("Hibernate Search: using lucene - local-heap");

			Map<String, String> luceneHeapProperties = new HashMap<>();
			luceneHeapProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
			luceneHeapProperties.put(BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER), HapiLuceneAnalysisConfigurer.class.getName());
			luceneHeapProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-heap");
			luceneHeapProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
			luceneHeapProperties.put(HibernateOrmMapperSettings.ENABLED, "true");

			return (theProperties) ->
				theProperties.putAll(luceneHeapProperties);
		}


		@Bean
		public IFulltextSearchSvc fullTextSearchSvc() {
			ourLog.info("Hibernate Search: FulltextSearchSvcImpl present");
			return new FulltextSearchSvcImpl();
		}
	}

	/**
	 * Disable Hibernate Search, and do not provide a IFulltextSearchSvc bean.
	 */
	@Configuration
	public static class NoFT {
		@Bean
		IHibernateSearchConfigurer hibernateSearchConfigurer() {
			ourLog.info("Hibernate Search is disabled");
			return (theProperties) -> theProperties.put("hibernate.search.enabled", "false");
		}

		@Primary
		@Bean
		public IFulltextSearchSvc fullTextSearchSvc() {
			ourLog.info("Hibernate Search: FulltextSearchSvcImpl not available");
			return null;
		}

	}


	/**
	 * Enable our Fulltext search with an Elasticsearch container instead of our default Lucene heap.
	 *
	 * Make sure you add {@link RequiresDocker} annotation to any uses.
	 */
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
