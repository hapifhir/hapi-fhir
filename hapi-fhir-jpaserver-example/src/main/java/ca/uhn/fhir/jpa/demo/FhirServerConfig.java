package ca.uhn.fhir.jpa.demo;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu3;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu3;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * This is the primary configuration file for the example server
 */
@Configuration
@EnableTransactionManagement()
public class FhirServerConfig extends BaseJavaConfigDstu3 {

	private static Properties properties = null;
	private static String un = null;
	private static String pd = null;
	private static String in = null;
	private static String luceneDirectory = "target/lucenefiles";

	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@Bean()
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setAllowMultipleDelete(true);
//		retVal.setAllowContainsSearches(false);
		return retVal;
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:derby:directory:jpaserver_derby_files;create=true" indicates that the server should save resources in a
	 * directory called "jpaserver_derby_files".
	 * 
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		if (properties == null) {
			try {
				properties = new Properties();
				properties.load(new FileInputStream(FhirServerProperties.serverPropertiesFile));
				in = (properties.getProperty("in") == null)
					? in : properties.getProperty("in");
				un = (properties.getProperty("un") == null)
					? un : properties.getProperty("un");
				pd = (properties.getProperty("pd") == null)
					? pd : properties.getProperty("pd");
				luceneDirectory = (properties.getProperty("LuceneDirectory") == null)
					? luceneDirectory : properties.getProperty("LuceneDirectory");
			} catch (IOException e) {
				throw new InternalErrorException(String.format("Error encountered loading %s file, info=%s",
					FhirServerProperties.serverPropertiesFile,
					e.getMessage()),
					e);
			}
		}

		if (((un == null) ||
			(un.length() == 0)) ||
			((pd == null) ||
				(pd.length() == 0)) ||
			((in == null) ||
				(in.length() == 0))) {
			throw new InternalErrorException("Database instance name and/or un and/or pd parameters not specified in properties file, cannot start server");
		}

		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.postgresql.Driver());
		retVal.setUrl(in);
		retVal.setUsername(un);
		retVal.setPassword(pd);
		return retVal;

//		BasicDataSource retVal = new BasicDataSource();
//		retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
//		retVal.setUrl("jdbc:derby:directory:target/jpaserver_derby_files;create=true");
//		retVal.setUsername("");
//		retVal.setPassword("");
//		return retVal;
	}

	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPersistenceUnitName("HAPI_PU");
		retVal.setDataSource(dataSource());
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", org.hibernate.dialect.PostgreSQL94Dialect.class.getName() /*org.hibernate.dialect.DerbyTenSevenDialect.class.getName()*/);
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
		extraProperties.put("hibernate.search.default.indexBase", luceneDirectory);
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
//		extraProperties.put("hibernate.search.default.worker.execution", "async");
		return extraProperties;
	}

	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a browser is detected
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor responseHighlighterInterceptor() {
		ResponseHighlighterInterceptor retVal = new ResponseHighlighterInterceptor();
		return retVal;
	}

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 */
	public IServerInterceptor loggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.access");
		retVal.setMessageFormat(
				"Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]");
		retVal.setLogExceptions(true);
		retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		return retVal;
	}

	@Bean
	@Lazy
	public RequestValidatingInterceptor requestValidatingInterceptor() {
		RequestValidatingInterceptor requestValidator = new RequestValidatingInterceptor();
		requestValidator.setResponseHeaderValueNoIssues("Validation did not detect any issues");
		requestValidator.setFailOnSeverity(ResultSeverityEnum.ERROR);
		requestValidator.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");

		IValidationSupport ocpValidationSupport = new OcpValidationSupport();
		ValidationSupportChain support = new ValidationSupportChain(ocpValidationSupport,
			new DefaultProfileValidationSupport());
		FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator();
		fhirInstanceValidator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Warning);
		fhirInstanceValidator.setValidationSupport(support);
		requestValidator.addValidatorModule(fhirInstanceValidator);

		requestValidator.setIgnoreValidatorExceptions(true);

		return requestValidator;
	}

//	@Bean
//	@Lazy
//	public ResponseValidatingInterceptor responseValidatingInterceptor() {
//		ResponseValidatingInterceptor responseValidator = new ResponseValidatingInterceptor();
//		responseValidator.setResponseHeaderValueNoIssues("Validation did not detect any issues");
//		responseValidator.setFailOnSeverity(null);
//		responseValidator.setAddResponseHeaderOnSeverity(null);
//		responseValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
//		responseValidator.addExcludeOperationType(RestOperationTypeEnum.METADATA);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.GET_PAGE);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_INSTANCE);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_SYSTEM);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.HISTORY_TYPE);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.SEARCH_SYSTEM);
////		responseValidator.addExcludeOperationType(RestOperationTypeEnum.SEARCH_TYPE);
//
//		IValidationSupport ocpValidationSupport = new OcpValidationSupport();
//		ValidationSupportChain support = new ValidationSupportChain(ocpValidationSupport,
//			new DefaultProfileValidationSupport());
//		FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator();
//		fhirInstanceValidator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Warning);
//		fhirInstanceValidator.setValidationSupport(support);
//		responseValidator.addValidatorModule(fhirInstanceValidator);
//
//		responseValidator.setIgnoreValidatorExceptions(true);
//
//		return responseValidator;
//	}

	@Bean(autowire = Autowire.BY_TYPE)
	public AuthorizationInterceptor OAuthAuthorizationInterceptor() {
		OAuthAuthorizationInterceptor retVal = new OAuthAuthorizationInterceptor();
		return retVal;
	}

//	/**
//	 * This interceptor adds some pretty syntax highlighting in responses when a browser is detected
//	 */
//	@Bean(autowire = Autowire.BY_TYPE)
//	public IServerInterceptor responseHighlighterInterceptor() {
//		ResponseHighlighterInterceptor retVal = new ResponseHighlighterInterceptor();
//		return retVal;
//	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor subscriptionSecurityInterceptor() {
		SubscriptionsRequireManualActivationInterceptorDstu3 retVal = new SubscriptionsRequireManualActivationInterceptorDstu3();
		return retVal;
	}

	@Bean()
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

}
