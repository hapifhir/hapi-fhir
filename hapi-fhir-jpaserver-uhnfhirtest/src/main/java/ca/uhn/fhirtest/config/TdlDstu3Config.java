package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu3;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.util.DerbyTenSevenHapiFhirDialect;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu3;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhirtest.interceptor.TdlSecurityInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
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
public class TdlDstu3Config extends BaseJavaConfigDstu3 {

	public static final String FHIR_LUCENE_LOCATION_DSTU3 = "${fhir.lucene.location.tdl3}";
	public static final String FHIR_DB_LOCATION_DSTU3 = "${fhir.db.location.tdl3}";

	@Value(FHIR_DB_LOCATION_DSTU3)
	private String myFhirDbLocation;

	@Value(FHIR_LUCENE_LOCATION_DSTU3)
	private String myFhirLuceneLocation;

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setSubscriptionEnabled(true);
		retVal.setSubscriptionPollDelay(5000);
		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		retVal.setAllowMultipleDelete(true);
		retVal.setAllowInlineMatchUrlReferences(true);
		retVal.setAllowExternalReferences(true);
		retVal.getTreatBaseUrlsAsLocal().add("http://fhirtest.uhn.ca/testDataLibraryStu3");
		retVal.getTreatBaseUrlsAsLocal().add("https://fhirtest.uhn.ca/testDataLibraryStu3");
		retVal.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		retVal.setCountSearchResultsUpTo(TestR4Config.COUNT_SEARCH_RESULTS_UP_TO);
		return retVal;
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}

	@Bean(name = "myPersistenceDataSourceDstu3", destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
//		retVal.setDriver(new org.apache.derby.jdbc.ClientDriver());
		// retVal.setUrl("jdbc:derby:directory:" + myFhirDbLocation + ";create=true");
		retVal.setUrl("jdbc:derby://localhost:1527/" + myFhirDbLocation + ";create=true");
		retVal.setUsername("SA");
		retVal.setPassword("SA");
		return retVal;
	}
	
	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaDstu3");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", DerbyTenSevenHapiFhirDialect.class.getName());
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
		requestValidator.addValidatorModule(instanceValidatorDstu3());
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
		responseValidator.addValidatorModule(instanceValidatorDstu3());
		responseValidator.setIgnoreValidatorExceptions(true);

		return responseValidator;
	}

	@Bean
	public TdlSecurityInterceptor securityInterceptor() {
		return new TdlSecurityInterceptor();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor subscriptionSecurityInterceptor() {
		return new SubscriptionsRequireManualActivationInterceptorDstu3();
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
