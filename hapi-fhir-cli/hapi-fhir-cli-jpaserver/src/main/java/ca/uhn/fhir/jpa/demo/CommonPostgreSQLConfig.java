package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.search.elastic.ElasticsearchHibernatePropertiesBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchIndexStatus;
import org.hibernate.search.elasticsearch.cfg.IndexSchemaManagementStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("Duplicates")
@Configuration
public class CommonPostgreSQLConfig {

	static String elasticsearchHost = "localhost";
	static String elasticsearchUserId = "";
	static String elasticsearchPassword = "";
	static Integer elasticsearchPort;

	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setSubscriptionEnabled(true);
		retVal.setSubscriptionPollDelay(5000);
		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		retVal.setAllowMultipleDelete(true);
		return retVal;
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:postgresql://localhost:5432/hapi" indicates that the server should save resources in a
	 * PostgreSQL database named "hapi".
	 *
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		String dbUrl = "jdbc:postgresql://localhost:5432/hapi";
		String dbUsername = "hapi";
		String dbPassword = "HapiFHIR";
		if (isNotBlank(ContextPostgreSQLHolder.getDatabaseUrl())) {
			dbUrl = ContextPostgreSQLHolder.getDatabaseUrl();
		}

		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriverClassName("org.postgresql.Driver");
		retVal.setUrl(dbUrl);
		retVal.setUsername(dbUsername);
		retVal.setPassword(dbPassword);
		return retVal;
	}

	@Bean
	public Properties jpaProperties() {

		if(ContextPostgreSQLHolder.isExternalElasticsearch()) {
			elasticsearchUserId = "elastic";
			elasticsearchPassword = "changeme";
			elasticsearchPort = 9301;
		} else {
			elasticsearchPort = embeddedElasticSearch().getHttpPort();
		}

		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", org.hibernate.dialect.PostgreSQL94Dialect.class.getName());
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "local-heap");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		extraProperties.put("hibernate.search.default.worker.execution", "sync");
		
		if (System.getProperty("lowmem") != null) {
			extraProperties.put("hibernate.search.autoregister_listeners", "false");
		}

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

//		extraProperties.setProperty("hibernate.search.default.elasticsearch.refresh_after_write", "true");
		return extraProperties;
	}

	@Bean()
	public ElasticsearchSvcImpl myElasticsearchSvc() throws IOException {
		if(ContextPostgreSQLHolder.isExternalElasticsearch()) {
			elasticsearchUserId = "elastic";
			elasticsearchPassword = "changeme";
			elasticsearchPort = 9301;
		} else {
			elasticsearchPort = embeddedElasticSearch().getHttpPort();
		}
		return new ElasticsearchSvcImpl(elasticsearchHost, elasticsearchPort, elasticsearchUserId, elasticsearchPassword);
	}

	@Bean
	public EmbeddedElastic embeddedElasticSearch() {
		String ELASTIC_VERSION = "6.5.4";

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

	@PreDestroy
	public void stop() {
		embeddedElasticSearch().stop();
	}

}
