package ca.uhn.fhir.jpa.demo;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.util.DerbyTenSevenHapiFhirDialect;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;


/**
 * Common code for dstu2 and dstu3 classes moved into static methods so that they can be called from version specific class.
 * 
 * @author anoushmouradian
 *
 */
public class FhirServerConfigCommon {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FhirServerConfigCommon.class);

	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@SuppressWarnings("deprecation")
	public static DaoConfig getDaoConfig() {
		DaoConfig daoConfig = new DaoConfig();
		daoConfig.setSubscriptionEnabled(true);
		daoConfig.setSubscriptionPollDelay(5000);
		daoConfig.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		daoConfig.setAllowMultipleDelete(true);
		return daoConfig;
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:derby:directory:jpaserver_derby_files;create=true" indicates that the server should save resources in a
	 * directory called "jpaserver_derby_files".
	 * 
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	public static DataSource getDataSource(Environment env) {
		String dbUrl = (env.getProperty(Utils.DB_URL) != null)?env.getProperty(Utils.DB_URL).toLowerCase():"";
		String herokuDbUrl = env.getProperty(Utils.HEROKU_DATABASE_URL);
		
		if(herokuDbUrl != null) {
			// url will come as: postgres://user:pass@host:5432/dbname
			String fromUserName = herokuDbUrl.substring(herokuDbUrl.indexOf("//") + 2);
			String userName = fromUserName.substring(0, fromUserName.indexOf(":"));
			String pass = fromUserName.substring(fromUserName.indexOf(":") +1, fromUserName.indexOf("@"));
			String fromHost = fromUserName.substring(fromUserName.indexOf("@")+1);
			String schemaName = env.getProperty(Utils.SCHEMA_NAME);
			
			//build this url: jdbc:postgresql://host:5432/dbName?user=username&password=pass&currentSchema='
			dbUrl = "jdbc:postgresql://" + fromHost + "?user=" + userName + "&password=" + pass + "&sslmode=require";
			if(schemaName != null) {
				dbUrl += "&currentSchema=" + schemaName; 
			}
			logger.info("------DB Url: " + dbUrl);
		}
		BasicDataSource dataSource = new BasicDataSource();
		try {
			if(dbUrl.indexOf("mysql") > -1 ) {
				dataSource.setDriver(new com.mysql.jdbc.Driver());
			} else if(dbUrl.indexOf("postgres") > -1) {
				dataSource.setDriver(new org.postgresql.Driver()); 
			} else if(dbUrl.indexOf("derby") > -1) {
				dataSource.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
			}
		} catch (SQLException e) {
			logger.error("----FhiServerConfigCommon: getDataSource: setting driver error: " + e.getMessage());
		}
		dataSource.setUrl(dbUrl);
		return dataSource;
	}

	public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(Environment env, DataSource dataSource, FhirContext theCtx) {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		BaseConfig.configureEntityManagerFactory(retVal, theCtx);

		retVal.setPersistenceUnitName("HAPI_PU");
		retVal.setDataSource(dataSource);
		retVal.setJpaProperties(jpaProperties(env));

		return retVal;
	}


	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 * @return
	 */
	public static LoggingInterceptor loggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.access");
		retVal.setMessageFormat(
				"Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]");
		retVal.setLogExceptions(true);
		retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		return retVal;
	}

	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a browser is detected
	 * @return
	 */
	public static ResponseHighlighterInterceptor getResponseHighlighterInterceptor() {
		ResponseHighlighterInterceptor retVal = new ResponseHighlighterInterceptor();
		return retVal;
	}

	public static JpaTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}
	
	private static Properties jpaProperties(Environment env) {
		Properties extraProperties = new Properties();
		String dbUrl = (env.getProperty(Utils.HEROKU_DATABASE_URL) == null)?env.getProperty(Utils.DB_URL):env.getProperty(Utils.HEROKU_DATABASE_URL);
		if(dbUrl != null && dbUrl.indexOf("mysql") > -1) {
			extraProperties.put("hibernate.dialect", org.hibernate.dialect.MySQL5Dialect.class.getName());
			extraProperties.put("hibernate.dialect.storage_engine","innodb");
		} 
		else if(dbUrl != null && dbUrl.indexOf("postgres") > -1) {
			extraProperties.put("hibernate.dialect", org.hibernate.dialect.PostgreSQL9Dialect.class.getName());
		} 
		else if(dbUrl != null && dbUrl.indexOf("derby") > -1) {
			extraProperties.put("hibernate.dialect", DerbyTenSevenHapiFhirDialect.class.getName());
		} 
		boolean hibernateCreate = new Boolean(env.getProperty(Utils.HIBERNATE_CREATE));
		logger.info("------DB hibernateCreate: " + hibernateCreate);
		if(hibernateCreate){
			extraProperties.put("hibernate.hbm2ddl.auto", "create");
		} else {
			extraProperties.put("hibernate.hbm2ddl.auto", "validate");
		}
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.autoregister_listeners", "false");
		extraProperties.put("hibernate.search.default.indexBase", "./target/lucenefiles");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		extraProperties.put("hibernate.search.indexing_strategy", "manual");
		extraProperties.put("hibernate.search.default.worker.execution", "async");

		return extraProperties;
	}

}
