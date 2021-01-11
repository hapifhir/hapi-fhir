package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.backend.lucene.cfg.LuceneIndexSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Import(TestJpaConfig.class)
public class TestJpaR4Config extends BaseJavaConfigR4 {
	private static final Logger ourLog = LoggerFactory.getLogger(TestJpaR4Config.class);
	@Autowired
	FhirContext myFhirContext;

	@Bean
	public CircularQueueCaptureQueriesListener captureQueriesListener() {
		return new CircularQueueCaptureQueriesListener();
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();

		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:testdb_r4");
		retVal.setMaxWaitMillis(10000);
		retVal.setUsername("");
		retVal.setPassword("");

		SLF4JLogLevel level = SLF4JLogLevel.INFO;
		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
//			.logQueryBySlf4j(level, "SQL")
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS)
//			.countQuery(new ThreadQueryCountHolder())
//			.beforeQuery(new BlockLargeNumbersOfParamsListener())
			.afterQuery(captureQueriesListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.countQuery(singleQueryCountHolder())
			.build();

		return dataSource;
	}

	@Bean
	public SingleQueryCountHolder singleQueryCountHolder() {
		return new SingleQueryCountHolder();
	}

	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaR4");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	@Bean
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());

		extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER), HapiLuceneAnalysisConfigurer.class.getName());
		extraProperties.put(BackendSettings.backendKey(LuceneIndexSettings.DIRECTORY_TYPE), "local-heap");
		extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");
		extraProperties.put(HibernateOrmMapperSettings.ENABLED, "true");

		return extraProperties;
	}

	/**
	 * Bean which validates incoming requests
	 */
	@Bean
	@Lazy
	public RequestValidatingInterceptor requestValidatingInterceptor() {
		RequestValidatingInterceptor requestValidator = new RequestValidatingInterceptor();
		requestValidator.setFailOnSeverity(ResultSeverityEnum.ERROR);
		requestValidator.setAddResponseHeaderOnSeverity(null);
		requestValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.addValidatorModule(instanceValidator());

		return requestValidator;
	}

	@Bean
	public IBinaryStorageSvc binaryStorage() {
		return new MemoryBinaryStorageSvcImpl();
	}

	@Bean
	public DefaultProfileValidationSupport validationSupportChainR4() {
		return new DefaultProfileValidationSupport(myFhirContext);
	}
}
