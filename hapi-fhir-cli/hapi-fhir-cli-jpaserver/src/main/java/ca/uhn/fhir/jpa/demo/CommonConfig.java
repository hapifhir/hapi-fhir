/*-
 * #%L
 * HAPI FHIR - Command Line Client - Server WAR
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.demo;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.dbcp2.BasicDataSource;
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
	 * Configure FHIR properties around the JPA server via this bean
	 */
	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings retVal = new JpaStorageSettings();
		retVal.setAllowMultipleDelete(true);
		return retVal;
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
		extraProperties.put(HibernateOrmMapperSettings.ENABLED, "false");
		extraProperties.put(Constants.HIBERNATE_INTEGRATION_ENVERS_ENABLED, storageSettings().isNonResourceDbHistoryEnabled());

		return extraProperties;
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

}
