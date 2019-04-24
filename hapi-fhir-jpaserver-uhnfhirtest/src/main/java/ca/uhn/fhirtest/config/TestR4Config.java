package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.config.BaseJavaConfigR4;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.util.DerbyTenSevenHapiFhirDialect;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhirtest.interceptor.PublicSecurityInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(CommonConfig.class)
@EnableTransactionManagement()
public class TestR4Config extends BaseJavaConfigR4 {
	public static final String FHIR_DB_USERNAME = "${fhir.db.username}";
	public static final String FHIR_DB_PASSWORD = "${fhir.db.password}";
	public static final String FHIR_LUCENE_LOCATION_R4 = "${fhir.lucene.location.r4}";
	public static final Integer COUNT_SEARCH_RESULTS_UP_TO = 50000;

	@Value(TestR4Config.FHIR_DB_USERNAME)
	private String myDbUsername;

	@Value(TestR4Config.FHIR_DB_PASSWORD)
	private String myDbPassword;

	@Value(FHIR_LUCENE_LOCATION_R4)
	private String myFhirLuceneLocation;

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setSubscriptionEnabled(true);
		retVal.setSubscriptionPollDelay(5000);
		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		retVal.setAllowContainsSearches(true);
		retVal.setAllowMultipleDelete(true);
		retVal.setAllowInlineMatchUrlReferences(true);
		retVal.setAllowExternalReferences(true);
		retVal.getTreatBaseUrlsAsLocal().add("http://fhirtest.uhn.ca/baseR4");
		retVal.getTreatBaseUrlsAsLocal().add("https://fhirtest.uhn.ca/baseR4");
		retVal.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		retVal.setCountSearchResultsUpTo(TestR4Config.COUNT_SEARCH_RESULTS_UP_TO);
		retVal.setFetchSizeDefaultMaximum(10000);
		retVal.setExpungeEnabled(true);
		return retVal;
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}


	@Bean(name = "myPersistenceDataSourceR4", destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
		if (CommonConfig.isLocalTestMode()) {
			retVal.setUrl("jdbc:derby:memory:fhirtest_r4;create=true");
		} else {
			retVal.setDriver(new org.postgresql.Driver());
			retVal.setUrl("jdbc:postgresql://localhost/fhirtest_r4");
		}
		retVal.setUsername(myDbUsername);
		retVal.setPassword(myDbPassword);
		retVal.setDefaultQueryTimeout(20);
		return retVal;
	}

	@Override
	@Bean(autowire = Autowire.BY_TYPE)
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		DatabaseBackedPagingProvider retVal = super.databaseBackedPagingProvider();
		retVal.setDefaultPageSize(20);
		retVal.setMaximumPageSize(500);
		return retVal;
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

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		if (CommonConfig.isLocalTestMode()) {
			extraProperties.put("hibernate.dialect", DerbyTenSevenHapiFhirDialect.class.getName());
		} else {
			extraProperties.put("hibernate.dialect", PostgreSQL94Dialect.class.getName());
		}
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
		extraProperties.put("hibernate.search.default.indexBase", myFhirLuceneLocation);
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		return extraProperties;
	}

	/**
	 * Bean which validates incoming requests
	 */
	@Bean
	@Lazy
	public RequestValidatingInterceptor requestValidatingInterceptor() {
		RequestValidatingInterceptor requestValidator = new RequestValidatingInterceptor();
		requestValidator.setFailOnSeverity(null);
		requestValidator.setAddResponseHeaderOnSeverity(null);
		requestValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.addValidatorModule(instanceValidatorR4());
		requestValidator.setIgnoreValidatorExceptions(true);

		return requestValidator;
	}

	@Bean
	public PublicSecurityInterceptor securityInterceptor() {
		return new PublicSecurityInterceptor();
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}



}
