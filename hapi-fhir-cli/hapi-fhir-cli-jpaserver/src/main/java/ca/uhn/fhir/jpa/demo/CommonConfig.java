package ca.uhn.fhir.jpa.demo;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import org.apache.commons.dbcp2.BasicDataSource;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.backend.lucene.cfg.LuceneIndexSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("Duplicates")
@Configuration
public class CommonConfig {

	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setAllowMultipleDelete(true);
		return retVal;
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:h2:file:target./jpaserver_h2_files" indicates that the server should save resources in a
	 * directory called "jpaserver_h2_files".
	 * <p>
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		String url = "jdbc:h2:file:./target/jpaserver_h2_files";
		if (isNotBlank(ContextHolder.getDatabaseUrl())) {
			url = ContextHolder.getDatabaseUrl();
		}

		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl(url);
		retVal.setUsername("");
		retVal.setPassword("");
		return retVal;
	}

	@Bean
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();

		//Regular Hibernate Settings
		extraProperties.put("hibernate.dialect", HapiFhirH2Dialect.class.getName());
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.backend.type", "lucene");

//		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER), HapiLuceneAnalysisConfigurer.class.getName());
//		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-filesystem");
//		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_ROOT), "target/lucenefiles");
//		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
//		if (System.getProperty("lowmem") != null) {
			extraProperties.put(HibernateOrmMapperSettings.ENABLED, "false");
//		}

		return extraProperties;
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

}
