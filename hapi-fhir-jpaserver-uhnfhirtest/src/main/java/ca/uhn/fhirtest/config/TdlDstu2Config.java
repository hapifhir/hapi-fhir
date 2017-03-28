package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu2;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu2;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhirtest.interceptor.TdlSecurityInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
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
public class TdlDstu2Config extends BaseJavaConfigDstu2 {

	public static final String FHIR_LUCENE_LOCATION_DSTU2 = "${fhir.lucene.location.tdl2}";
	public static final String FHIR_DB_LOCATION_DSTU2 = "${fhir.db.location.tdl2}";

	@Value(FHIR_DB_LOCATION_DSTU2)
	private String myFhirDbLocation;

	@Value(FHIR_LUCENE_LOCATION_DSTU2)
	private String myFhirLuceneLocation;

	/**
	 * This lets the "@Value" fields reference properties from the properties file
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean 
	public IServerInterceptor securityInterceptor() {
		return new TdlSecurityInterceptor();
	}
	
	@Bean()
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setSubscriptionEnabled(true);
		retVal.setSubscriptionPollDelay(5000);
		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		retVal.setAllowMultipleDelete(true);
		retVal.setAllowInlineMatchUrlReferences(true);
		retVal.setAllowExternalReferences(true);
		retVal.getTreatBaseUrlsAsLocal().add("http://fhirtest.uhn.ca/testDataLibraryDstu2");
		retVal.getTreatBaseUrlsAsLocal().add("https://fhirtest.uhn.ca/testDataLibraryDstu2");
		return retVal;
	}

	@Bean(name = "myPersistenceDataSourceDstu1", destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
//		retVal.setDriver(new org.apache.derby.jdbc.ClientDriver());
		// retVal.setUrl("jdbc:derby:directory:" + myFhirDbLocation + ";create=true");
		retVal.setUrl("jdbc:derby://localhost:1527/" + myFhirDbLocation + ";create=true");
		retVal.setUsername("SA");
		retVal.setPassword("SA");
		return retVal;
	}

	@Bean()
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaDstu2");
		retVal.setDataSource(dataSource());
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", DerbyTenSevenDialect.class.getName());
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.default.directory_provider" ,"filesystem");
		extraProperties.put("hibernate.search.default.indexBase", myFhirLuceneLocation);
		extraProperties.put("hibernate.search.lucene_version","LUCENE_CURRENT");
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
		requestValidator.addValidatorModule(instanceValidatorDstu2());
		requestValidator.setIgnoreValidatorExceptions(true);

		return requestValidator;
	}

	/**
	 * Bean which validates outgoing responses
	 */
	@Bean
	@Lazy
	public ResponseValidatingInterceptor responseValidatingInterceptor() {
		ResponseValidatingInterceptor responseValidator = new ResponseValidatingInterceptor();
		responseValidator.setResponseHeaderValueNoIssues("Validation did not detect any issues");
		responseValidator.setFailOnSeverity(null);
		responseValidator.setAddResponseHeaderOnSeverity(null);
		responseValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.METADATA);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.GET_PAGE);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_INSTANCE);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_SYSTEM);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_TYPE);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.SEARCH_SYSTEM);
		responseValidator.addExcludeOperationType(RestOperationTypeEnum.SEARCH_TYPE);
		responseValidator.addValidatorModule(instanceValidatorDstu2());
		responseValidator.setIgnoreValidatorExceptions(true);

		return responseValidator;
	}

	@Bean(autowire=Autowire.BY_TYPE)
	public IServerInterceptor subscriptionSecurityInterceptor() {
		return new SubscriptionsRequireManualActivationInterceptorDstu2();
	}
	
	
}
